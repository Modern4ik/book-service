package com.library.book_service.constants;

public final class MessageTemplates {

    public static final String AUTHOR_NOT_FOUND_MESSAGE = "Author with ID = %d not found!";
    public static final String BASE_UNKNOWN_AUTHOR_ERROR_DELETE_MESSAGE = "Cant delete base unknown author with ID = 1!";

    public static final String BOOK_NOT_FOUND_MESSAGE = "Book with ID = %d not found!";

    public static final String GENRE_BY_ID_NOT_FOUND_MESSAGE = "Genre with ID = %d not found!";
    public static final String GENRE_BY_NAME_NOT_FOUND_MESSAGE = "Genre with Name = %s not found!";

    public final static String COMMENT_NOT_FOUND_MESSAGE = "Comment with ID = %d not found!";

    public static final String USER_NOT_FOUND_MESSAGE = "User with ID = %d not found!";

    private MessageTemplates() {
    }

}
