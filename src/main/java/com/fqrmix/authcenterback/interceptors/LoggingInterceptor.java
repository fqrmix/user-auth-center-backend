package com.fqrmix.authcenterback.interceptors;

import com.fqrmix.authcenterback.interceptors.helpers.SensitiveContentCachingRequestWrapper;
import com.fqrmix.authcenterback.interceptors.helpers.SensitiveContentCachingResponseWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import jakarta.servlet.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

import static java.util.Collections.list;

/**
 * Component class for logging HTTP request and response data.
 */
@Component
@Slf4j
public class LoggingInterceptor extends GenericFilterBean {

    /**
     * Intercept HTTP requests and responses to log their data.
     *
     * @param request  the servlet request
     * @param response the servlet response
     * @param chain    the filter chain
     * @throws ServletException if a servlet error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {

        SensitiveContentCachingRequestWrapper requestWrapper = requestWrapper(request);
        SensitiveContentCachingResponseWrapper responseWrapper = responseWrapper(response);

        chain.doFilter(requestWrapper, responseWrapper);

        if (Objects.equals(requestWrapper.getMethod(), HttpMethod.POST.name())) {
            logRequest(requestWrapper);
            logResponse(responseWrapper);
        }

        responseWrapper.copyBodyToResponse();
    }

    /**
     * Logs the request data including headers and content.
     *
     * @param request the wrapped request
     */
    private void logRequest(SensitiveContentCachingRequestWrapper request) {
        StringBuilder builder = new StringBuilder();
        builder.append(headersToString(list(request.getHeaderNames()), request::getMaskedHeader));
        builder.append(request.getContentAsMaskedString());
        log.info("request: {}", builder);
    }

    /**
     * Logs the response data including headers and content.
     *
     * @param response the wrapped response
     */
    private void logResponse(SensitiveContentCachingResponseWrapper response) {
        StringBuilder builder = new StringBuilder();
        builder.append(headersToString(response.getHeaderNames(), response::getMaskedHeader));
        builder.append(response.getContentAsMaskedString());
        log.info("response: {}", builder);
    }

    /**
     * Converts header names and values to a string.
     *
     * @param headerNames       the collection of header names
     * @param headerValueResolver a function to retrieve header values
     * @return a string representation of headers
     */
    private String headersToString(Collection<String> headerNames, Function<String, String> headerValueResolver) {
        StringBuilder builder = new StringBuilder();
        builder.append("Headers: {");
        Iterator<String> iterator = headerNames.iterator();
        while (iterator.hasNext()) {
            String headerName = iterator.next();
            String header = headerValueResolver.apply(headerName);
            builder.append("%s=%s".formatted(headerName, header));
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        builder.append("}\n");
        return builder.toString();
    }

    /**
     * Wraps the request with SensitiveContentCachingRequestWrapper if it's not already wrapped.
     *
     * @param request the servlet request
     * @return the wrapped request
     */
    private SensitiveContentCachingRequestWrapper requestWrapper(ServletRequest request) {
        if (request instanceof SensitiveContentCachingRequestWrapper requestWrapper) {
            return requestWrapper;
        }
        return new SensitiveContentCachingRequestWrapper((HttpServletRequest) request);
    }

    /**
     * Wraps the response with SensitiveContentCachingResponseWrapper if it's not already wrapped.
     *
     * @param response the servlet response
     * @return the wrapped response
     */
    private SensitiveContentCachingResponseWrapper responseWrapper(ServletResponse response) {
        if (response instanceof SensitiveContentCachingResponseWrapper responseWrapper) {
            return responseWrapper;
        }
        return new SensitiveContentCachingResponseWrapper((HttpServletResponse) response);
    }
}