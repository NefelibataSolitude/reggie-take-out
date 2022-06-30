/**
 * @Author: Tecoy
 * Contact: itecoy@163.com
 * @Date: 2022/6/27 21:55
 * @Version: 1.0
 * @Description:
 */
package net.tecoy.reggie.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.tecoy.reggie.common.BaseContext;
import net.tecoy.reggie.common.R;
import net.tecoy.reggie.constant.Constants;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录拦截
 *
 * @author Tecoy
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    /**
     * 路径匹配器, 支持通配符
     */
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 获取uri
        String uri = request.getRequestURI();
        // 定义不需要处理的请求
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"
        };

        boolean check = check(urls, uri);
        // 判断请求是否需要处理(后端)
        Long id = (Long) request.getSession().getAttribute(Constants.EMPLOYEE);
        // 移动端
        id = (id != null) ? id : (Long) request.getSession().getAttribute(Constants.USER);
        if (!check && id == null) {
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            return;
        }
        BaseContext.setCurrentId(id);
        filterChain.doFilter(request, response);
    }

    /**
     * 路径匹配
     * @param urls
     * @param uri
     * @return
     */
    public boolean check(String[] urls, String uri) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, uri);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
