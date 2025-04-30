package com.books.holder.service.genre;

import com.books.holder.constants.MessageTemplates;
import com.books.holder.dto.genre.GenreRequestCreateDto;
import com.books.holder.dto.genre.GenreResponseDto;
import com.books.holder.entity.Genre;
import com.books.holder.mappers.GenreMapper;
import com.books.holder.repository.GenreRepository;
import com.books.holder.service.cache.CacheVersionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;
    private final CacheVersionService cacheVersionService;
    private final CacheManager cacheManager;

    @Override
    @Transactional
    public GenreResponseDto saveGenre(GenreRequestCreateDto genreRequestCreateDto) {
        GenreResponseDto newGenreDto = genreMapper.toDto(
                genreRepository.save(genreMapper.toEntity(genreRequestCreateDto)));

        cacheVersionService.incrementVersion(CACHE_NAMESPACE);
        return newGenreDto;
    }

    @Override
    @Cacheable(value = "genreById", key = "#id")
    public GenreResponseDto getGenreById(Integer id) {
        return genreMapper.toDto(
                genreRepository.findById(id).orElseThrow(() ->
                        new EntityNotFoundException(MessageTemplates.GENRE_BY_ID_NOT_FOUND_MESSAGE.formatted(id)))
        );
    }

    @Override
    @Cacheable(value = "genreByName", key = "#name.toLowerCase()")
    public GenreResponseDto getGenreByName(String name) {
        return genreMapper.toDto(
                genreRepository.findByNameIgnoreCase(name).orElseThrow(() ->
                        new EntityNotFoundException(MessageTemplates.GENRE_BY_NAME_NOT_FOUND_MESSAGE.formatted(name))));
    }

    @Override
    @Cacheable(value = "allGenres", key = "{'all', @cacheVersionService.getCurrentVersion('genres')}")
    public List<GenreResponseDto> getGenres() {
        return genreMapper.mapToDto(genreRepository.findAll());
    }

    @Override
    @Transactional
    @CacheEvict(value = "genreById", key = "#id")
    public GenreResponseDto updateGenreNameById(Integer id, String newName) {
        Genre foundedGenre = genreRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageTemplates.GENRE_BY_ID_NOT_FOUND_MESSAGE.formatted(id)));

        foundedGenre.setName(newName);
        GenreResponseDto updatedGenreDto = genreMapper.toDto(foundedGenre);

        cacheVersionService.incrementVersion(CACHE_NAMESPACE);
        return updatedGenreDto;
    }

    @Override
    @Transactional
    public void deleteGenreById(Integer id) {
        String genreName = genreRepository.findById(id)
                .map(Genre::getName)
                .orElseThrow(() ->
                        new EntityNotFoundException(MessageTemplates.GENRE_BY_ID_NOT_FOUND_MESSAGE.formatted(id)));

        genreRepository.deleteById(id);

        evictCachedGenreByIdAndName(id, genreName);
        cacheVersionService.incrementVersion(CACHE_NAMESPACE);
    }

    @Override
    @Transactional
    public boolean genreExists(String genreName) {
        return genreRepository.existsByName(genreName);
    }

    private void evictCachedGenreByIdAndName(Integer id, String genreName) {
        Cache idCache = cacheManager.getCache("genreById");
        Cache nameCache = cacheManager.getCache("genreByName");

        if (idCache != null) {
            idCache.evict(id);
        }

        if (nameCache != null) {
            nameCache.evict(genreName.toLowerCase());
        }
    }

}
