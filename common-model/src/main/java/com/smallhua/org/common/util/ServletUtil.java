package com.smallhua.org.common.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 〈一句话功能简述〉<br>
 * 〈获得request,respose对象〉
 *
 * @author ZZH
 * @create 2021/4/29
 * @since 1.0.0
 */
public class ServletUtil {

    /**
     * 获得HttpServletRequest对象
     * @return HttpServletRequest对象
     */
    public static HttpServletRequest getRequest(){
        return  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     *   获得HttpServletResponse对象
     *    @return HttpServletResponse对象
     */
    public static HttpServletResponse getResponse(){
        return  ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }
}