package com.cista.pidb.core;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * UTF-8 charset filter.
 * @author Matrix
 *
 */
public class CharsetFilter implements Filter {
    @SuppressWarnings("unused")
    private FilterConfig config = null;

    private String defaultEncode = "UTF-8";

    /**
     * Filter initialize.
     * @param _config config.
     * @throws ServletException ServletException
     */
    public void init(final FilterConfig _config) throws ServletException {
        this.config = _config;
        if (config.getInitParameter("Charset") != null) {
            defaultEncode = config.getInitParameter("Charset");
        }
    }

    /**
     * Filter destroy.
     */
    public void destroy() {
        config = null;
    }

    /**
     * Filter process.
     * @param request HttpRequest
     * @param response HttpResponse
     * @param chain FilterChain
     * @throws IOException IOException
     * @throws ServletException ServletException
     */
    public void doFilter(final ServletRequest request, final ServletResponse response,
            final FilterChain chain) throws IOException, ServletException {
        ServletRequest srequest = request;
        srequest.setCharacterEncoding(defaultEncode);
        chain.doFilter(srequest, response);
    }
}
