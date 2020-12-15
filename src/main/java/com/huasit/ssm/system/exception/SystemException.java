package com.huasit.ssm.system.exception;

/**
 *
 */
public class SystemException extends RuntimeException {

    /**
     *
     */
    public SystemError msg;

    /**
     *
     */
    public Object[] params;

    /**
     *
     */
    public SystemException() {
        this(SystemError.UNKNOWN_ERROR);
    }

    /**
     *
     */
    public SystemException(SystemError msg) {
        super();
        this.msg = msg;
    }

    /**
     *
     */
    public SystemException(SystemError msg, Object... params) {
        super();
        this.msg = msg;
        this.params = params;
    }

    /**
     *
     */
    public SystemException(Exception e, SystemError msg) {
        super(e);
        this.msg = msg;
    }
}