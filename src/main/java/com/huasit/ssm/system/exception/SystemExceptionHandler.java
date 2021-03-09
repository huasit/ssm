package com.huasit.ssm.system.exception;

import com.huasit.ssm.system.locale.Response;
import org.apache.logging.log4j.LogManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 */
@ControllerAdvice
public class SystemExceptionHandler {

    /**
     *
     */
    @ResponseBody
    @ExceptionHandler(value = SystemException.class)
    public ResponseEntity<Map<String, Object>> systemExceptionHandler(HttpServletRequest request, SystemException e) {
        if (e.msg == SystemError.UNKNOWN_ERROR) {
            LogManager.getLogger().error(e);
        }
        if (e.params == null) {
            return Response.error(e.msg, request).entity();
        }
        return Response.error(e.msg, e.params, request).entity();
    }

    /**
     *
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> exceptionHandler(HttpServletRequest request, MethodArgumentNotValidException e) {
        return Response.error(0, "").entity();
    }

    /**
     *
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Map<String, Object>> exceptionHandler(HttpServletRequest request, Exception e) {
        LogManager.getLogger().error(e);
        return Response.innerError(SystemError.UNKNOWN_ERROR, e.getMessage()).entity();
    }
}