package com.huasit.ssm.system.locale;

import com.google.common.collect.ImmutableMap;
import com.huasit.ssm.system.exception.SystemError;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class Response {

    /**
     *
     */
    private final ResponseEntity<Map<String, Object>> result;

    /**
     *
     */
    public Response(ResponseEntity<Map<String, Object>> result) {
        this.result = result;
    }

    /**
     *
     */
    public ResponseEntity<Map<String, Object>> entity() {
        return result;
    }

    /**
     *
     */
    public static Response success(String k1, Object v1) {
        return new Response(new ResponseEntity<>(ImmutableMap.of(k1, v1), HttpStatus.OK));
    }

    /**
     *
     */
    public static Response success(String k1, Object v1, String k2, Object v2) {
        return new Response(new ResponseEntity<>(ImmutableMap.of(k1, v1, k2, v2), HttpStatus.OK));
    }

    /**
     *
     */
    public static Response success(String k1, Object v1, String k2, Object v2, String k3, Object v3) {
        return new Response(new ResponseEntity<>(ImmutableMap.of(k1, v1, k2, v2, k3, v3), HttpStatus.OK));
    }

    /**
     *
     */
    public static Response success(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4) {
        return new Response(new ResponseEntity<>(ImmutableMap.of(k1, v1, k2, v2, k3, v3, k4, v4), HttpStatus.OK));
    }

    /**
     *
     */
    public static Response error(int code, String message, HttpStatus httpStatus) {
        Map<String, Object> data = ImmutableMap.of("error_code", code, "error_msg", message);
        return new Response(new ResponseEntity<>(data, httpStatus));
    }

    /**
     *
     */
    public static Response error(int code, String message) {
        Map<String, Object> data = ImmutableMap.of("error_code", code, "error_msg", message);
        return new Response(new ResponseEntity<>(data, HttpStatus.BAD_REQUEST));
    }

    /**
     *
     */
    public static Response error(SystemError e, HttpServletRequest request) {
        String msg = Locale.getInstance(request).getMessage("err_" + e.code);
        return e.httpStatus == null ? Response.error(e.code, msg) : Response.error(e.code, msg, e.httpStatus);
    }

    /**
     *
     */
    public static Response error(SystemError e,Object[] params, HttpServletRequest request) {
        String msg = Locale.getInstance(request).getMessage("err_" + e.code, params);
        return e.httpStatus == null ? Response.error(e.code, msg) : Response.error(e.code, msg, e.httpStatus);
    }

    /**
     *
     */
    public void write(HttpServletResponse response) throws IOException {
        response.setStatus(result.getStatusCodeValue());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.getWriter().write(new JSONObject(Objects.requireNonNull(result.getBody())).toString());
    }
}
