package com.cista.pidb.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.cista.pidb.admin.to.UserTo;

public class AuthenFilter implements Filter {
    private final Log logger = LogFactory.getLog(this.getClass());
    
    private PathMatcher pathMatcher = new AntPathMatcher();

    private static final List<String> NOT_MATCHING = new ArrayList<String>();

    static {
        NOT_MATCHING.add("/index.jsp");
        NOT_MATCHING.add("/ajax/**");
        NOT_MATCHING.add("/admin/error_handler.do");
        NOT_MATCHING.add("/common/**");
        NOT_MATCHING.add("/images/**");
        NOT_MATCHING.add("/css/**");
        NOT_MATCHING.add("/js/**");
        NOT_MATCHING.add("/dialog/**");

    }

    public void destroy() {
        // TODO Auto-generated method stub
    }

    public void doFilter(ServletRequest arg0, ServletResponse arg1,
            FilterChain arg2) throws IOException, ServletException {

        String currentUrl = ((HttpServletRequest) arg0).getRequestURI();
        String cp = ((HttpServletRequest) arg0).getContextPath();
        currentUrl = currentUrl.substring(cp.length());
        if (!currentUrl.startsWith("/")) {
            currentUrl += "/";
        }

        if (!matching(NOT_MATCHING, currentUrl)) {
            
            UserTo loginUser = PIDBContext
                    .getLoginUser((HttpServletRequest) arg0);
            
            if (((HttpServletRequest) arg0).getQueryString() != null && !((HttpServletRequest) arg0).getQueryString().equals("")) {
                currentUrl = currentUrl + "?" + ((HttpServletRequest) arg0).getQueryString();
            }
            
            if (loginUser == null) {
                PIDBContext.setUserSavedUrl((HttpServletRequest) arg0, currentUrl);
                ((HttpServletRequest) arg0).setAttribute("errorCode", "nologin");
                ((HttpServletRequest) arg0).getRequestDispatcher(
                        "/admin/error_handler.do").forward(arg0, arg1);
            } else {
                List<String> uriList = PIDBContext
                        .getUserAuth((HttpServletRequest) arg0);


                if (matching(uriList, currentUrl)) {
                    arg2.doFilter(arg0, arg1);
                } else {
                    ((HttpServletRequest) arg0).setAttribute("errorCode",
                            "accessdenied");
                    ((HttpServletRequest) arg0).getRequestDispatcher(
                            "/admin/error_handler.do").forward(arg0, arg1);
                }
            }
        } else {
            arg2.doFilter(arg0, arg1);
        }

    }

    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

    private boolean matching(final List<String> uriList, final String currentUrl) {
        boolean flag = false;

        if (uriList != null) {
            // sort authen list
            //Collections.sort(uriList);
            //Collections.reverse(uriList);

            for (String uri : uriList) {
                flag = pathMatcher.match(uri, currentUrl);
                
                if (flag) {
                    break;
                }
            }
        }

        if (!flag) {
            logger.debug("currentUrl=" + currentUrl + ",not matching!");
        }
        return flag;
    }

}
