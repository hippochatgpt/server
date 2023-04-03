package com.hippo.chatgpt.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hippo.chatgpt.config.IgnoreUrlsConfig;
import com.hippo.chatgpt.entity.database.ChatUserEntity;
import com.hippo.chatgpt.service.ChatUserService;
import com.hippo.chatgpt.utils.HeaderMapRequestWrapper;
import com.hippo.chatgpt.utils.ResultCode;
import com.hippo.chatgpt.utils.ResultUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author xiewei
 */
@Component
public class LoginFilter implements Filter {

    @Resource
    private IgnoreUrlsConfig ignoreUrlsConfig;
    @Resource
    private ChatUserService chatUserService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = request.getRequestURI();
        PathMatcher pathMatcher = new AntPathMatcher();
        //白名单路径放过请求
        List<String> ignoreUrls = ignoreUrlsConfig.getUrls();
        for (String ignoreUrl : ignoreUrls) {
            if (pathMatcher.match(ignoreUrl, uri)) {
                HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(request);
                requestWrapper.addHeader("Authorization", "");
                chain.doFilter(requestWrapper, servletResponse);
                return;
            }
        }
        // 不在白名单，校验token
        String token = request.getHeader("Authorization");
        if (ObjectUtil.isNotEmpty(token)) {
            QueryWrapper<ChatUserEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("token", token);
            ChatUserEntity user = chatUserService.getBaseMapper().selectOne(queryWrapper);
            if (ObjectUtil.isNotEmpty(user)) {
                // 验证通过，将userId存入header
                HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(request);
                requestWrapper.addHeader("userId", user.getId().toString());
                chain.doFilter(requestWrapper, servletResponse);
                return;
            }
        }
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setStatus(HttpStatus.OK.value());
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        ResultUtil res = ResultUtil.error();
        res.setCode(ResultCode.UN_AUTHORIZED.getCode());
        res.setMsg(ResultCode.UN_AUTHORIZED.getMsg());
        String body= JSONUtil.toJsonStr(res);
        PrintWriter pri = response.getWriter();
        pri.println(body);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
