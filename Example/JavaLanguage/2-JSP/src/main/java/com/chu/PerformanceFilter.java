package com.chu;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter(filterName = "performance", urlPatterns = {"/*"})
public class PerformanceFilter implements Filter {
    private FilterConfig config;

    @Override
    public void init(FilterConfig config) throws ServletException {
        this.config = config;
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        long begin = System.currentTimeMillis();
        chain.doFilter(request, response);
        config.getServletContext().log("Request process in " +
                (System.currentTimeMillis() - begin) + " milliseconds");
        System.out.println("Request process in " +
                (System.currentTimeMillis() - begin) + " milliseconds");
    }

    @Override
    public void destroy() {
    }
}
