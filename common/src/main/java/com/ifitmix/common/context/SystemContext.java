package com.ifitmix.common.context;

import com.ifitmix.common.constants.AppConstants;
import com.ifitmix.utils.HttpUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangtao on 2017/5/11.
 */
public class SystemContext {

    /**
     * 请求
     */
    private HttpServletRequest request;
    /**
     * 响应
     */
    private HttpServletResponse response;
    /**
     * 语言
     */
    private String language;
    /**
     * 匹配路径
     */
    private String matchedPath;
    /**
     * 设备唯一码
     */
    private String udid;
    /**
     * 终端类型
     */
    private Integer terminal;
    /**
     * 客户端系统
     */
    private String sdk;
    /**
     * ip
     */
    private String ip;
    /**
     * 服务器接收时间
     */
    private Long serverReceiveTime;
    /**
     * 版本
     */
    private Integer version;
    /**
     * 网络
     */
    private String network;
    /**
     * 渠道
     */
    private String channel;

    public SystemContext() {

    }

    public SystemContext(HttpServletRequest request, HttpServletResponse response, String matchedPath) {
        this.request = request;
        this.response = response;
        this.matchedPath = matchedPath;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String getLanguage() {
        if(language == null) {
            language = AppConstants.LANGUAGE_CH;
        }
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMatchedPath() {
        return matchedPath;
    }

    public void setMatchedPath(String matchedPath) {
        this.matchedPath = matchedPath;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public Integer getTerminal() {
        return terminal;
    }

    public void setTerminal(Integer terminal) {
        this.terminal = terminal;
    }

    public String getSdk() {
        return sdk;
    }

    public void setSdk(String sdk) {
        this.sdk = sdk;
    }

    public String getIp() {
        if(this.ip == null) {
            ip = HttpUtil.getIP(request);
        }
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getServerReceiveTime() {
        return serverReceiveTime;
    }

    public void setServerReceiveTime(Long serverReceiveTime) {
        this.serverReceiveTime = serverReceiveTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
