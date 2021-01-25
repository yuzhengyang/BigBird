package com.yuzhyn.bigbird.app.system.filter;

import com.alibaba.fastjson.JSONObject;
import com.yuzhyn.bigbird.app.system.wrapper.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
public class AppFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("过滤器初始化");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("过滤器处理");
        Long beginTime = System.currentTimeMillis();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String method = request.getMethod();
        log.info("AppFilter, " + request.getRequestURI());

        ResponseWrapper wrapper = new ResponseWrapper((HttpServletResponse) servletResponse);
        //里面的是wrapper
        filterChain.doFilter(servletRequest, wrapper);

        Long finishTime = System.currentTimeMillis();
        Long runTime = finishTime - beginTime;
        log.info("请求执行时长：" + runTime + " ms");

        Collection<String> headers = ((HttpServletResponse) servletResponse).getHeaderNames();
        wrapper.setHeader("biz-op-time", runTime.toString());

        // 根据返回值类型处理数据
        String contentType = servletResponse.getContentType();
        String result = wrapper.getResponseData(servletResponse.getCharacterEncoding());
        switch (contentType) {
            case "application/json;charset=UTF-8":
                JSONObject jsonObject = JSONObject.parseObject(result);
                jsonObject.put("biz-op-time", runTime);
                result = jsonObject.toString();
                break;
            case "application/octet-stream":
                break;
            default:
                break;
        }
        //对result进行一些处理（比如先把json字符串转换为map，然后进行修改，再转换为json字符串）
        byte[] bytes = result.getBytes();
        servletResponse.setContentLength(bytes.length);
        servletResponse.getOutputStream().write(bytes);
    }

    @Override
    public void destroy() {
        log.info("过滤器销毁");
    }
}