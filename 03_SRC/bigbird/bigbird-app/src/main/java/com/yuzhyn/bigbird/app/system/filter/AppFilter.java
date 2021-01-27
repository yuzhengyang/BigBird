package com.yuzhyn.bigbird.app.system.filter;

import com.alibaba.fastjson.JSONObject;
import com.yuzhyn.bigbird.app.system.wrapper.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
public class AppFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
        log.info("过滤器初始化");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Long beginTime = System.currentTimeMillis();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = URLDecoder.decode(request.getRequestURI(), "utf-8");
        String method = request.getMethod();
        log.info("AppFilter 过滤器处理: " + uri + " ,method: " + method);

        // *************************************************************************************
        // *************************************************************************************
        ResponseWrapper wrapper = new ResponseWrapper((HttpServletResponse) servletResponse);
        filterChain.doFilter(servletRequest, wrapper);
        // *************************************************************************************
        // *************************************************************************************

        Long runTime = System.currentTimeMillis() - beginTime;
        log.info("请求执行时长：" + runTime + " ms");

        // 处理header数据
        Collection<String> headers = ((HttpServletResponse) servletResponse).getHeaderNames();
        wrapper.setHeader("biz-op-time", runTime.toString());

        // 处理response数据（依据返回数据类型）
        String contentType = servletResponse.getContentType();
        byte[] responseData = wrapper.getResponseData();
        switch (contentType) {
            case "application/json;charset=UTF-8":
                // 对json数据进行处理，解析-处理-转换
                String result = new String(responseData, servletResponse.getCharacterEncoding());
                JSONObject jsonObject = JSONObject.parseObject(result);
                jsonObject.put("biz-op-time", runTime);
                result = jsonObject.toString();
                responseData = result.getBytes();
                break;
            case "application/octet-stream":
            default:
                break;
        }

        // 写出最终response数据
        servletResponse.setContentLength(responseData.length);
        servletResponse.getOutputStream().write(responseData);
    }

    @Override
    public void destroy() {
        log.info("过滤器销毁");
    }
}