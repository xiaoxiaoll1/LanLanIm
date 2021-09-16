package com.zj.lib.filter;


import com.zj.lib.wrapper.WrapperRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author xiaozj
 */
@Slf4j
@Component
@WebFilter(urlPatterns = "/userLogin/login", filterName = "customFilter")
public class CustomFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        WrapperRequest wrapperRequest = new WrapperRequest(request);
        filterChain.doFilter(wrapperRequest, servletResponse);
    }


}
