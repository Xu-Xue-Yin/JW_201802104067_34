//信管182 徐学印 201802104067
package com.example.demo.filter;

import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Order(3)
@WebFilter(filterName = "Filter 3", urlPatterns = {"/*"})
public class Filter30_Login implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Filter 3 - log begins");
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpServletResponse =(HttpServletResponse)response;
        HttpSession session = httpServletRequest.getSession(false);
        String path= httpServletRequest.getRequestURI();
        if (path.contains("/login.ctl") || path.contains("/logout.ctl") ){
            filterChain.doFilter(request,response);
        }else if(session != null && session.getAttribute("currentUser") != null) {
            filterChain.doFilter(request,response);
            System.out.println("Filter 3 - log ends");
        } else {
            response.getWriter().println("请先进行登录");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}

}
