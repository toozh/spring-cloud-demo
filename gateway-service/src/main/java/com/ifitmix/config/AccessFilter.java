package com.ifitmix.config;

import com.ifitmix.base.api.CommonErrorCode;
import com.ifitmix.base.api.ResponseEntity;
import com.ifitmix.base.constants.CodeConstants;
import com.ifitmix.user.api.dtos.UserDto;
import com.ifitmix.utils.HttpUtil;
import com.ifitmix.utils.JsonUtils;
import com.ifitmix.utils.MD5Util;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangtao on 2017/5/25.
 */
public class AccessFilter extends ZuulFilter {

    private static Logger logger = LoggerFactory.getLogger(AccessFilter.class);

    private RedisTemplate redisTemplate;

    private MongoTemplate mongoTemplate;

    private MultipartResolver commonsMultipartResolver;

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

    // TODO 用户登录也迁移过来后 在修改KEY
    private static final String REDIS_USER_KEY = "_%s_com.business.core.entity.user.User_";

    @Override
    public String filterType() {
        return "pre";
    }
    @Override
    public int filterOrder() {
        return 0;
    }
    @Override
    public boolean shouldFilter() {
        return true;
    }
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        logger.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

        // 获取token
        String accessToken = null;
        String authHeader = request.getHeader("Authorization");
        MultipartHttpServletRequest multipartHttpServletRequest = null;
        String contentType = request.getContentType();
        if(StringUtils.isNotBlank(contentType) && contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
            CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
            multipartHttpServletRequest = commonsMultipartResolver.resolveMultipart(request);
        }

        // 0. 检查必要参数
        String dickVal = HttpUtil.getParameter(request, multipartHttpServletRequest, PARAM_DICK_VALUE_KEY);
        String language = HttpUtil.getParameter(request, multipartHttpServletRequest, PARAM_LANGUAGE);
        String channel = HttpUtil.getParameter(request, multipartHttpServletRequest, PARAM_CHANNEL);
        String version = HttpUtil.getParameter(request, multipartHttpServletRequest, PARAM_VERSION);
        String network = HttpUtil.getParameter(request, multipartHttpServletRequest, PARAM_NETWORK);
        // TODO 暂时设置成为必传参数，之后通过redis 共享session，然后从session 获取
        String uid = HttpUtil.getParameter(request, multipartHttpServletRequest, PARAM_UID);
        String sdk = HttpUtil.getParameter(request, multipartHttpServletRequest, PARAM_SDK);

        if (StringUtils.isEmpty(language) ||
                StringUtils.isEmpty(channel) ||
                StringUtils.isEmpty(version) ||
                StringUtils.isEmpty(network) ||
                StringUtils.isEmpty(sdk) ||
                StringUtils.isEmpty(uid) ||
                !version.matches("\\d+")) {
            createResponse(ctx, CommonErrorCode.BAD_REQUEST);
            return false;
        }

        // 1. 对比token

        if(authHeader != null) {
            String pattern = "FITMIX (.*)";
            Pattern authHeaderPattern = Pattern.compile(pattern);
            Matcher authHeaderMatcher = authHeaderPattern.matcher(authHeader);
            if(authHeaderMatcher.find()) {
                accessToken = authHeaderMatcher.group(1);
                logger.info("accessToken : " + accessToken);
            }
        }
        if(accessToken == null) {
            logger.warn("access token is empty");
            createResponse(ctx, CommonErrorCode.FORBIDDEN);
            return null;
        }

        // 2. 判断登录信息 & sign
        Object user = redisTemplate.opsForValue().get(String.format(REDIS_USER_KEY, uid));
        String userSign = null;
        if(user == null) {
            logger.info("USER IS NULL");
            UserDto userDto = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(Integer.parseInt(uid))), UserDto.class, "User");
            userSign = MD5Util.MD5Encode( userDto.getId() + "[" + userDto.getPassword() + "]", "utf-8");
        } else {
            String userDtoJson = user.toString();
            UserDto userDto = JsonUtils.json2Object(userDtoJson, UserDto.class);
            userSign = userDto.getSign();
        }
        String sign = HttpUtil.getParameter(request, multipartHttpServletRequest, "sign");
        
        // TODO: 2017/6/2 如果不传sign 暂时全部让通过，等全部都换成微服务后 再统一做校验
        if(sign != null) {
            if(!userSign.equals(sign)) {
                createResponse(ctx, CodeConstants.LOGIN_USER_STATE_NO_LOGIN, "用户没有登录");
                return null;
            }

            // 3. 鉴权
            Map<String, String[]> paramMap = multipartHttpServletRequest == null ? request.getParameterMap() : multipartHttpServletRequest.getParameterMap();
            if(!getSignVerify(paramMap, accessToken)) {
                createResponse(ctx, CodeConstants.VERIFY_FAIL, "鉴权失败");
                return null;
            }
        }




        logger.info("access token ok");
        return null;
    }

    public AccessFilter(RedisTemplate redisTemplate, MongoTemplate mongoTemplate, MultipartResolver commonsMultipartResolver) {
        this.redisTemplate = redisTemplate;
        this.mongoTemplate = mongoTemplate;
        this.commonsMultipartResolver = commonsMultipartResolver;
    }

    private void createResponse(RequestContext ctx, CommonErrorCode  commonErrorCode) {
        ctx.setSendZuulResponse(false);
        try {
            ctx.getResponse().setCharacterEncoding("utf-8");
            ctx.getResponse().setContentType("application/json");

            ResponseEntity responseEntity = new ResponseEntity(commonErrorCode.getCode(), commonErrorCode.getMessage());
            ctx.getResponse().getWriter().print(JsonUtils.object2Json(responseEntity));
            ctx.getResponse().getWriter().flush();
            ctx.getResponse().getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createResponse(RequestContext ctx, int code, String message) {
        ctx.setSendZuulResponse(false);
        try {
            ctx.getResponse().setCharacterEncoding("utf-8");
            ctx.getResponse().setContentType("application/json");

            ResponseEntity responseEntity = new ResponseEntity(code, message);
            ctx.getResponse().getWriter().print(JsonUtils.object2Json(responseEntity));
            ctx.getResponse().getWriter().flush();
            ctx.getResponse().getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createLinkString(Map<String, String[]> map) {
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i ++) {
            String key = keys.get(i);
            String[] values = map.get(key);
            StringBuffer sb = new StringBuffer();

            for(String value : values) {
                sb.append(value);
            }
            try {
                if(i == keys.size() - 1) {
                    prestr = prestr + key + "=" + URLEncoder.encode(sb.toString(), "UTF-8").replaceAll("\\+", "%20");
                } else {
                    prestr = prestr + key + "=" + URLEncoder.encode(sb.toString(), "UTF-8").replaceAll("\\+", "%20") + "&";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return prestr;
     }

     private String byte2Hex(byte[] b) {
        if(b == null) {
            return "";
        }
        StringBuffer tmp = new StringBuffer();
        int len = b.length;
        for(int i = 0; i < len; i++) {
            String s = Integer.toHexString(b[i] & 0xff);
            if(s.length() < 2) {
                tmp.append('0');
            }
            tmp.append(s);
        }
        while (tmp.length() < 16) {
            tmp.append("00");
        }
        return tmp.toString();
     }

     private boolean getSignVerify(Map<String, String[]> map, String token) {
         String prestr = createLinkString(map);
         byte[] result = null;
         try {
             logger.info("排序后的字符串 : " + prestr);
             MessageDigest messageDigest = MessageDigest.getInstance("MD5");
             messageDigest.update(prestr.getBytes());
             result = messageDigest.digest();

         } catch (Exception e) {
             e.printStackTrace();
         }
         logger.info(" MD5 : " + byte2Hex(result));
         return token.equals(byte2Hex(result));
     }

}
