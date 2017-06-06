package com.ifitmix.common.spring.excetion;

import com.ifitmix.base.api.CommonErrorCode;
import com.ifitmix.base.api.Error;
import com.ifitmix.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangtao on 2017/3/19.
 *
 * 统一错误处理
 *
 */
@Controller
@RequestMapping("error")
public class AppErrorController extends AbstractErrorController {

//    private ErrorProperties errorProperties;

    @Autowired
    public AppErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
//        Assert.notNull(errorAttributes, "ErrorProperties must be null");
//        this.errorProperties = errorProperties;
    }


    @Override
    public String getErrorPath() {
        return "error";
    }

    @RequestMapping(produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        CommonErrorCode errorCode = CommonErrorCode.fromHttpStatus(status.value());
        Error error = new Error(errorCode.getCode(), request.getRequestURI(), status.getReasonPhrase());
        ModelAndView modelAndView = new ModelAndView();
        MappingJackson2JsonView view = new MappingJackson2JsonView(JsonUtils.OBJECT_MAPPER);
        view.setAttributesMap(JsonUtils.object2Map(error));
        modelAndView.setView(view);
        return modelAndView;
    }

    @RequestMapping
    @ResponseBody
    public ResponseEntity<String> error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        CommonErrorCode errorCode = CommonErrorCode.fromHttpStatus(status.value());
        Error error = new Error(errorCode.getCode(), request.getRequestURI(), status.getReasonPhrase());
        return new ResponseEntity<>(JsonUtils.object2Json(error), status);
    }

}
