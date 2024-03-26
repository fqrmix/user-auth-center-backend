package com.fqrmix.authcenterback.interceptors.helpers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.ContentCachingRequestWrapper;

/**
 * Extends ContentCachingRequestWrapper to provide additional functionality for masking sensitive data in requests.
 */
public class SensitiveContentCachingRequestWrapper extends ContentCachingRequestWrapper {

    /**
     * Constructs a new SensitiveContentCachingRequestWrapper for the given servlet request.
     *
     * @param request the original servlet request
     */
    public SensitiveContentCachingRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * Retrieves the content of the request as a String with sensitive data masked.
     *
     * @return the content of the request as a String with sensitive data masked
     */
    public String getContentAsMaskedString() {
        return maskSensitiveData(this.getContentAsString());
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
