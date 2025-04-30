package com.books.holder.service.cache;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CacheVersionService {

    private final ConcurrentMap<String, AtomicLong> versions = new ConcurrentHashMap<>();

    public long getCurrentVersion(String namespace) {
        return versions.computeIfAbsent(namespace, v -> new AtomicLong()).get();
    }

    public void incrementVersion(String namespace) {
        versions.computeIfAbsent(namespace, v -> new AtomicLong()).incrementAndGet();
    }

    @Scheduled(fixedRate = 86_400_000)
    public void resetDailyCounters() {
        versions.replaceAll((k, v) -> new AtomicLong());
    }

}
