package com.ifitmix.common.spring.interceptor;


import com.ifitmix.common.context.SystemContext;
import com.ifitmix.common.context.SystemContextHolder;
import com.ifitmix.utils.HttpUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangtao on 2017/4/1.
 * 基本参数拦截器
 */
public class AppBaseInterceptor implements HandlerInterceptor {

    /**
     * 请求里k参数的对应的KEY
     */
    private static final String PARAM_DICK_VALUE_KEY = "_k";
    /**
     * 请求参数 lan 语言
     */
    private static final String PARAM_LANGUAGE = "_lan";
    /**
     * 请求参数 channel 渠道
     */
    private static final String PARAM_CHANNEL = "_ch";
    /**
     * 请求参数 version_i 版本
     */
    private static final String PARAM_VERSION = "_v";
    /**
     * 请求参数 sdk
     */
    private static final String PARAM_SDK = "_sdk";
    /**
     * 请求参数 network 网络情况
     */
    private static final String PARAM_NETWORK = "_nw";
    /**
     * 请求参数 uid 用户编号
     */
    private static final String PARAM_UID = "uid";
    /**
     * 请求参数 udid 设备编号
     */
    private static final String PARAM_UDID = "udid";

    private static final String HEADER_USER_AGENT_REGEX =  "Fitmix\\/[0-9]{1,3}\\/[0-9]\\.[0-9]\\.[0-9]\\s\\((Android.*|iOS.*|iPhone.*|iPad.*|iPod.*)\\)";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {



        String dickVal = HttpUtil.getParameter(request, PARAM_DICK_VALUE_KEY);
        String language = HttpUtil.getParameter(request, PARAM_LANGUAGE);
        String channel = HttpUtil.getParameter(request, PARAM_CHANNEL);
        String version = HttpUtil.getParameter(request, PARAM_VERSION);
        String network = HttpUtil.getParameter(request, PARAM_NETWORK);
        String uid = HttpUtil.getParameter(request, PARAM_UID);
        String sdk = HttpUtil.getParameter(request, PARAM_SDK);


//        // 没有渠道则不验证
//        if (StringUtils.isEmpty(language) ||
//                StringUtils.isEmpty(channel) ||
//                StringUtils.isEmpty(version) ||
//                StringUtils.isEmpty(network) ||
//                StringUtils.isEmpty(sdk) ||
//                !version.matches("\\d+")) {
//            response.getWriter().write("{\"code\":9800,\"msg\":\"auth error\"}");
//            return false;
//        }

        // 过滤 请求 Header
        // User-Agent: Fitmix/23/2.3.0 (Android 4.0 or IOS 10)
        // User-Agent: Fitmix/44/2.3.0 (Android 4.0)
        // User-Agent: Fitmix/5/2.3.0 (IOS 4.0)
//        Integer InternalVersion = Integer.valueOf(version);
//        String userAgent = request.getHeader("User-Agent");
//        String regexString = StringUtil.regexFirst(userAgent == null ? "" : userAgent, HEADER_USER_AGENT_REGEX);
//        if (sdk.contains("OS")) {
//            if ( InternalVersion >= VersionConstants.IOS.VERSION_12.getVersion()) {
//                // 过滤 User-Agent 验证 key
//                // TODO【sin to sin】 dick 验证 和 login 用户数据加密
//                if (StringUtils.isEmpty(regexString) /*|| StringUtils.isEmpty(dickVal)*/) {
//                    return false;
//                }
//            }
//        } else if(!sdk.equals("web")) {// Android
//            if ( InternalVersion >= VersionConstants.Android.VERSION_45.getVersion()) {
//                // 过滤 User-Agent 验证 key
//                // TODO【sin to sin】 dick 验证 和 login 用户数据加密
//                if (StringUtils.isEmpty(regexString) /*|| StringUtils.isEmpty(dickVal)*/) {
//                    return false;
//                }
//            }
//        }
//
        // 客户端一直带着的参数
        SystemContext systemContext = new SystemContext(request, response, request.getRequestURI());
        systemContext.setServerReceiveTime(System.currentTimeMillis());
        systemContext.setLanguage(language);
        systemContext.setVersion(Integer.valueOf(version == null ? "-1" : version));
        systemContext.setNetwork(network);
        systemContext.setSdk(sdk);
        systemContext.setMatchedPath(request.getRequestURI());
        systemContext.setChannel(channel);
        systemContext.setIp(HttpUtil.getIP(request));
        // 设置 加密 key
        SystemContextHolder.put(systemContext);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        System.out.println(request.getRequestURI() + " 执行时间  ： " + (System.currentTimeMillis() - SystemContextHolder.get().getServerReceiveTime()) + "ms" );
        SystemContextHolder.remove();
    }
}
