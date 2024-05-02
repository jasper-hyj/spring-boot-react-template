package com.example.exception;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;

import com.example.model.ApiResponse;
import com.example.service.logging.LogUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

/**
 * Controller level Exception Handling
 */
@ControllerAdvice
@Log4j2
public class ControllerExceptionHandler {

    private final HttpServletRequest request;

    public ControllerExceptionHandler(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Static File not found/resource not found exception handling
     * Ignore and forward the request to react
     *
     * @return forward to "/" (default react api)
     * @throws IOException
     * @throws DatabindException
     * @throws StreamWriteException
     * @throws ServletException
     */
    @ExceptionHandler({ NoResourceFoundException.class })
    public String handleNoResourceFoundException(Exception ex) {
        return "forward:/";
    }

    /**
     * Handles AccessDeniedException thrown by PreAuthorize/PostAuthorize
     * annotations.
     * Redirects the user to the portal page (default backend page) if access is
     * denied.
     *
     * @param request  The HTTP servlet request.
     * @param ex       The AccessDeniedException object.
     * @param response The HTTP servlet response.
     */
    @ExceptionHandler({ AccessDeniedException.class})
    public ResponseEntity<ApiResponse> handleAccessDeniedException(Exception ex) {
        if (request.getRequestURI().contains("/api/")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error(HttpStatus.FORBIDDEN,
                            "Request denied: " + request.getRequestURI() + " " + ex.getMessage()));
        } else {
            return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "/").build();
        }
    }

    // @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public String handleMethodArugmentTypeMismatchException() {
        log.debug("Test");
        return "forward:/";
    }

    /**
     * Handles all other exceptions not covered by specific handlers.
     * Logs detailed exception information and forwards the request to the React
     * application.
     *
     * @param request The HttpServletRequest containing request URL and method.
     * @param ex      The exception object.
     */

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ApiResponse> handleException(Exception ex) {
        // Log the exception if needed
        log.error("{} - {}:{} -- {}:{}\n {}", LogUtil.getIp(request),
                request.getMethod(), request.getRequestURL(),
                ex.getClass().getSimpleName(), ex.getMessage(),
                LogUtil.getStackTraceAsString(ex));
        // Redirect to the home page
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "Something went wrong: " + ex.getMessage()));
    }
}