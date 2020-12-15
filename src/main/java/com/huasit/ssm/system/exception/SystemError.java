package com.huasit.ssm.system.exception;

import org.springframework.http.HttpStatus;

public enum SystemError {

    UNKNOWN_ERROR(100),
    ACCESS_DENIED(1000, HttpStatus.UNAUTHORIZED),
    CREDENTIALS_EXPIRED(1001, HttpStatus.UNAUTHORIZED),
    FORMDATA_ERROR(2000),
    USERNAME_NOTFOUND(2001),
    USERNAME_EXISTS(2002),
    USERNAME_ERROR(2003),
    BAD_CREDENTIALS(2004),
    ROLE_SIGN_EXISTS(2005),
    TERM_ERROR(3000),
    EXAM_DATA_ERROR(4000),
    EXAM_QUEST_BANK_COUNT_LIMIT(4001),
    EXAM_OVER_BUT_ANSWER(4002),
    EXAM_COUNT_LIMIT(4003),
    LABORATORY_BOOK_USER_HAS_BOOK_IN_SAMETIME(5000),
    LABORATORY_BOOK_CAPACITY_LIMIT(5001),
    LABORATORY_BOOK_NOT_IN_ALLOW_HOURS(5002),
    LABORATORY_BOOK_TIME_NOT_AVAILABLE(5003),
    LABORATORY_BOOK_TIME_NOT_AVAILABLE_WITH_DELETE(5004),
    LABORATORY_BOOK_HAS_NO_BOOK(5005);

    /**
     *
     */
    public int code;

    /**
     *
     */
    public HttpStatus httpStatus;

    /**
     *
     */
    SystemError(int code) {
        this.code = code;
    }

    /**
     *
     */
    SystemError(int code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }
}
