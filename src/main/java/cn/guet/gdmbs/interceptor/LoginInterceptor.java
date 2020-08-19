package cn.guet.gdmbs.interceptor;

import cn.guet.gdmbs.entity.Admin;
import cn.guet.gdmbs.entity.Teacher;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Component
public class LoginInterceptor implements HandlerInterceptor {
    //这个方法是在访问接口之前执行的，我们只需要在这里写验证登陆状态的业务逻辑，就可以在用户调用指定接口之前验证登陆状态了
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object studentId=session.getAttribute("studentId");
        Admin admin=(Admin)session.getAttribute("admin");
        Teacher teacher=(Teacher)session.getAttribute("teacher");
        String uri=request.getRequestURI();
        if(uri.contains("teacher")){
            if(teacher!=null) {
                return true;
            }
            else {
                redirect(request,response,"/login.html");
                return false;
            }
        }
        else if(uri.contains("student")){
            if(studentId!=null) {
                return true;
            }
            else {
                redirect(request,response,"/login.html");
                return false;
            }
        }
        else if(uri.contains("admin")){
            if(admin!=null) {
                return true;
            }
            else {
                redirect(request,response,"/login.html");
                return false;
            }
        }
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }

    public void redirect(HttpServletRequest request, HttpServletResponse response,String url) throws IOException {
        //获取当前请求的路径
        String basePath = request.getScheme() + "://" + request.getServerName() + ":"  + request.getServerPort()+request.getContextPath();
        //如果request.getHeader("X-Requested-With") 返回的是"XMLHttpRequest"说明就是ajax请求，需要特殊处理 否则直接重定向就可以了
        if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
            //告诉ajax我是重定向
            response.setHeader("REDIRECT", "REDIRECT");
            //告诉ajax我重定向的路径
            response.setHeader("CONTEXTPATH", basePath+url);
            //response.addHeader("Access-Control-Expose-Headers", "REDIRECT,CONTEXTPATH");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }else{
            response.sendRedirect(basePath + url);
        }
    }
}
