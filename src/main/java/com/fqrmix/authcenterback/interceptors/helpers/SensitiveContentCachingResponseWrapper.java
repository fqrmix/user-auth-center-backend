package com.fqrmix.authcenterback.interceptors.helpers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 * Extends ContentCachingResponseWrapper to provide additional functionality for masking sensitive data in responses.
 */
public class SensitiveContentCachingResponseWrapper extends ContentCachingResponseWrapper {

    /**
     * Constructs a new SensitiveContentCachingResponseWrapper for the given servlet response.
     *
     * @param response the original servlet response
     */
    public SensitiveContentCachingResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    /**
     * Retrieves the value of the specified header as a String. If the header contains sensitive information
     * it returns "****" instead of the actual value.
     *
     * @param name the name of the header
     * @return the value of the header, or "****" if it contains sensitive information
     */
    public String getMaskedHeader(String name) {
        String header = super.getHeader(name);

        if (name.toLowerCase().contains("cookie") ||
                name.toLowerCase().contains("authorization") ||
                name.toLowerCase().contains("token")) {
            return "****";
        }
        return header;
    }

    /**
     * Retrieves the content of the response as a String.
     *
     * @return the content of the response as a String
     */
    public String getContentAsString() {
        return new String(this.getContentAsByteArray());
    }

    /**
     * Retrieves the content of the response as a String with sensitive data masked.
     *
     * @return the content of the response as a String with sensitive data masked
     */
    public String getContentAsMaskedString() {
        return maskSensitiveData(this.getContentAsString());
    }

    /**
     * Masks sensitive data in the given string by replacing occurrences with "****".
     *
     * @param line the string to mask sensitive data in
     * @return the string with sensitive data masked
     */
    private String maskSensitiveData(String line) {
        return line
                .replaceAll("\"password\"\\s*:\\s*\"[^\"]+\"", "\"password\":\"****\"")
                .replaceAll("\"token\"\\s*:\\s*\"[^\"]+\"", "\"token\":\"****\"");
    }
}
