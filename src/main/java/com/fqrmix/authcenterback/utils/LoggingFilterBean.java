package com.fqrmix.authcenterback.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import jakarta.servlet.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

import static java.util.Collections.list;

/**
 * Component class for logging HTTP request and response data.
 */
@Component
@Slf4j
public class LoggingFilterBean extends GenericFilterBean {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        ContentCachingRequestWrapper requestWrapper = requestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = responseWrapper(response);

        chain.doFilter(requestWrapper, responseWrapper);

        if (Objects.equals(requestWrapper.getMethod(), HttpMethod.POST.name())) {
            logRequest(requestWrapper);
            logResponse(responseWrapper);
        }

        responseWrapper.copyBodyToResponse();
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        StringBuilder builder = new StringBuilder();
        builder.append(headersToString(list(request.getHeaderNames()), request::getHeader));
        builder.append(new String(request.getContentAsByteArray()));
        log.info("request: {}", builder);
    }

    private void logResponse(ContentCachingResponseWrapper response) {
        StringBuilder builder = new StringBuilder();
        builder.append(headersToString(response.getHeaderNames(), response::getHeader));
        builder.append(new String(response.getContentAsByteArray()));
        log.info("response: {}", builder);
    }

    private String headersToString(
            Collection<String> headerNames,
            Function<String, String> headerValueResolver
    ) {
        StringBuilder builder = new StringBuilder();
        for (String headerName : headerNames) {
            String header = headerValueResolver.apply(headerName);
            builder.append("%s=%s".formatted(headerName, header)).append("\n");
        }
        return builder.toString();
    }

    private ContentCachingRequestWrapper requestWrapper(ServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper requestWrapper) {
            return requestWrapper;
        }
        return new ContentCachingRequestWrapper((HttpServletRequest) request);
    }

    private ContentCachingResponseWrapper responseWrapper(ServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper responseWrapper) {
            return responseWrapper;
        }
        return new ContentCachingResponseWrapper((HttpServletResponse) response);
    }
}
