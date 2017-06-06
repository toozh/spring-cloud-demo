package com.ifitmix.config;

import com.ifitmix.base.api.CommonErrorCode;
import com.ifitmix.base.api.ResponseEntity;
import com.ifitmix.utils.JsonUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zhangtao on 2017/5/25.
 */
public class ErrorFilter extends ZuulFilter {

    private static Logger logger = LoggerFactory.getLogger(ErrorFilter.class);
    @Override
    public String filterType() {
        return "error";
    }
    @Override
    public int filterOrder() {
        return 10;
    }
    @Override
    public boolean shouldFilter() {
        return true;
    }
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Throwable throwable = ctx.getThrowable();

        logger.error("this is a ErrorFilter : {}", throwable.getCause().getMessage());
        try {
            ctx.setSendZuulResponse(false);
            ctx.getResponse().setCharacterEncoding("utf-8");
            ctx.getResponse().setContentType("application/json");
            ResponseEntity responseEntity = new ResponseEntity(CommonErrorCode.INTERNAL_ERROR.getCode(), CommonErrorCode.INTERNAL_ERROR.getMessage());
            ctx.getResponse().getWriter().print(JsonUtils.object2Json(responseEntity));
            ctx.getResponse().getWriter().flush();
            ctx.getResponse().getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
