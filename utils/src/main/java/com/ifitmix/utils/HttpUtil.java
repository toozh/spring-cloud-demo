package com.ifitmix.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * Http工具类
 */
public class HttpUtil {

    /**
     * 内容类型 - UTF-8
     */
    public static final ContentType TEXT_PLAIN_UTF_8 = ContentType.create("text/plain", Consts.UTF_8);

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * HttpClient单例持有
     */
    private static class HttpClientHolder {
        private static final CloseableHttpClient INSTANCE = HttpClients.custom()
//                .disableAutomaticRetries()
                .setMaxConnTotal(10240 * 30)
                .setMaxConnPerRoute(800000 * 20)
                .setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout((int) (30 * DateUtil.SECOND_LONG)).setSocketTimeout((int) (30 * DateUtil.SECOND_LONG)).build())
                .setUserAgent("")
                .build();
    }

    /**
     * 获取Cookie方法
     *
     * @param request request对象
     * @param name    Cookie名称
     * @return 值
     */
    public static String getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 添加Cookie方法
     *
     * @param response response对象
     * @param name     Cookie名称
     * @param value    值
     */
    public static void addCookie(HttpServletResponse response, String name, String value) {
        addCookie(response, name, value, (int) DateUtil.YEAR_LONG);
    }

    /**
     * 添加Cookie方法
     *
     * @param response response对象
     * @param name     Cookie名称
     * @param value    值
     * @param maxAge   最长存活时间（秒）
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        addCookie(response, name, value, maxAge, "/");
    }

    /**
     * 添加Cookie方法
     *
     * @param response response对象
     * @param name     Cookie名称
     * @param value    值
     * @param maxAge   最长存活时间（秒）
     * @param path     存放路径
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        response.addCookie(cookie);
    }

    /**
     * 移除Cookie方法
     *
     * @param response response对象
     * @param name     Cookie名称
     */
    public static void removeCookie(HttpServletResponse response, String name) {
        addCookie(response, name, null, -1);
    }

    /**
     * @return HttpClient单例
     */
    public static CloseableHttpClient getHttpClient() {
        return HttpClientHolder.INSTANCE;
    }

    /**
     * 安静的关闭，即使抛出异常
     *
     * @param response 响应
     */
    public static void closeQuietly(CloseableHttpResponse response) {
        if (response == null) {
            return;
        }
        try {
            response.close();
        } catch (IOException ignored) {
        }
    }

    /**
     * @param request request对象
     * @return ip
     */
    public static String getIP(HttpServletRequest request) {
        String ip = request.getHeader("clientip"); // for UC browser
        if (ip == null) {
            ip = request.getHeader("X-Real-IP");
            if (ip == null) {
                ip = request.getRemoteAddr();
            }
        }
        return ip;
    }

    /**
     * 通过IP地址获取MAC地址
     * @param ip String,127.0.0.1格式
     * @return mac String
     * @throws Exception
     */
    public static String getMACAddress(String ip){
        String str = "";
        String macAddress = "";
        try {
            Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
            InputStreamReader ir = new InputStreamReader(p.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (int i = 1; i < 100; i++) {
                str = input.readLine();
                if (str != null) {
                    if (str.indexOf("MAC Address") > 1) {
                        macAddress = str.substring(str.indexOf("MAC Address") + 14, str.length());
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        return macAddress;
    }

    /**
     * @param request 请求
     * @return ua
     */
    public static String getUA(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        return ua != null ? ua : "";
    }

    public static String getUA(ServletRequest request) {
        return getUA((HttpServletRequest) request);
    }

    /**
     * @param request 请求
     * @return ua
     */
    public static String getReferer(HttpServletRequest request) {
        return request.getHeader("Referer");
    }

    /**
     * 根据map建立queryString。主要用于生成Http的Get请求的参数
     *
     * @param params 参数集
     * @return queryString
     */
    public static String buildQueryString(Map<String, String> params) {
        if (params.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    /**
     * url参数编码，按照UTF-8来
     *
     * @param value 参数
     * @return 编码后的参数
     */
    public static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, Charset.forName("UTF-8").name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据request拼接queryString
     *
     * @return queryString
     */
    public static String buildQueryString(HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        Enumeration<String> es = request.getParameterNames();
        if (!es.hasMoreElements()) {
            return "";
        }
        String parameterName, parameterValue;
        StringBuilder params = new StringBuilder();
        while (es.hasMoreElements()) {
            parameterName = es.nextElement();
            parameterValue = request.getParameter(parameterName);
            params.append(parameterName).append("=").append(parameterValue).append("&");
        }
        return params.deleteCharAt(params.length() - 1).toString();
    }

    public static byte[] getImageBytes(String url) {
//        try (InputStream is = new URL(url).openConnection().getInputStream()) {
//            return IOUtils.toByteArray(is);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        CloseableHttpResponse response = null;
        try {
            HttpGet get = new HttpGet(url);
            response = HttpUtil.getHttpClient().execute(get);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                return EntityUtils.toByteArray(response.getEntity());
            } else {
                throw new IOException("返回结果：" + statusCode);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            HttpUtil.closeQuietly(response);
        }
    }

    public static List<NameValuePair> getParams(Map<String, ?> params) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for (Map.Entry<String, ?> param : params.entrySet()) {
            if (param.getValue() == null) {
                continue;
            }
            nvps.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
        }
        return nvps;
    }

    public static String request(String uri, String method, Map<String, Object> params, Map<String, String> headers,
                                 RequestConfig config, HttpClientContext context, String charset) {
        HttpRequestBase req;
        CloseableHttpResponse response = null;
        try {
            if (method.equalsIgnoreCase("post")) {
                req = new HttpPost(uri);
                HttpPost post = (HttpPost) req;
                List<NameValuePair> nvps = getParams(params);
                post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            } else if (method.equalsIgnoreCase("get")) {
                if (params.isEmpty()) {
                    req = new HttpGet(uri);
                } else {
                    URIBuilder builder = new URIBuilder();
                    builder.setPath(uri);
                    for (Map.Entry<String, Object> entry : params.entrySet()) {
                        builder.addParameter(entry.getKey(), String.valueOf(entry.getValue()));
                    }
                    req = new HttpGet(builder.toString());
                }
            } else if (method.equals("post_with_file")) {
                req = new HttpPost(uri);
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if (entry.getValue() instanceof Object[]) {
                        Object[] values = (Object[]) entry.getValue();
                        if ("binary".equals(values[0])) {
                            builder.addBinaryBody(entry.getKey(), (byte[]) values[1], ContentType.APPLICATION_OCTET_STREAM, (String) values[2]);
                        }
                    } else {
                        builder.addTextBody(entry.getKey(), String.valueOf(entry.getValue()), HttpUtil.TEXT_PLAIN_UTF_8);
                    }
                }
                ((HttpPost) req).setEntity(builder.build());
            } else {
                throw new IllegalArgumentException(method);
            }

            if (!CollectionUtils.isEmpty(headers)) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    req.addHeader(header.getKey(), header.getValue());
                }
            }

            if (config != null) {
                req.setConfig(config);
            }

            if (context == null) {
                context = HttpClientContext.create();
                context.setCookieStore(new BasicCookieStore()); // 防止其他请求的cookie影响
            }
            response = HttpUtil.getHttpClient().execute(req, context);
            return EntityUtils.toString(response.getEntity(), charset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            HttpUtil.closeQuietly(response);
        }
    }

    public static String request(String uri, String method, Map<String, Object> params, Map<String, String> headers,
                                 RequestConfig config, HttpClientContext context) {
        return request(uri, method, params, headers, config, context, "utf-8");
    }

    public static String request(String uri, String method, Map<String, Object> params, Map<String, String> headers,
                                 RequestConfig config) {
        return request(uri, method, params, headers, config, null);
    }

    /**
     * 获得Int类型的参数。首先从request参数中拿，若不存在，则从Cookie中获得。
     *
     * @param request 请求
     * @param key     参数名
     * @return 参数值
     */
    public static Integer getIntParameter(HttpServletRequest request, String key) {
        String value = getParameter(request, key);
        return Integer.valueOf(value);
    }

    /**
     * 获得String类型的参数。首先从request参数中拿，若不存在，则从Cookie中获得。
     *
     * @param request 请求
     * @param key     参数名
     * @return 参数值
     */
    public static String  getParameter(HttpServletRequest request, String key) {
        String value = request.getParameter(key);
        if (value == null) {
            value = HttpUtil.getCookie(request, genCookieKey(key));
        }
        return value;
    }

    /**
     * 获得String类型的参数。首先从request参数中拿，若不存在，则从Cookie中获得。
     *
     * @param request 请求
     * @param key     参数名
     * @return 参数值
     */
    public static String  getParameter(HttpServletRequest request, MultipartHttpServletRequest multipartHttpServletRequest, String key) {
        String value = request.getParameter(key);
        if (value == null) {
            value = HttpUtil.getCookie(request, genCookieKey(key));
        }

        if(value == null && multipartHttpServletRequest != null) {
            value = multipartHttpServletRequest.getParameter(key);
        }

        return value;
    }


    /**
     * @param key 键
     * @return 新键。防止和其他应用cookie冲突
     */
    public static String genCookieKey(String key) {
        return "apps_server_" + key;
    }

//    /**
//     * 获取ip区域
//     * @param ip ip
//     * @return 大致区域
//     */
//    public static TaoBaoIp ipArea(String ip) {
//        TaoBaoIp taoBaoIp = null;
//        if (StringUtils.isEmpty(ip)) {
//            return taoBaoIp;
//        }
//        CloseableHttpResponse response = null;
//        try {
//            //TODO 考虑把这个淘宝ip记录下来 这个地方访问频繁
//            HttpGet httpGet = new HttpGet("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip);
//            response = HttpUtil.getHttpClient().execute(httpGet);
//            if (response.getStatusLine().getStatusCode() == org.apache.commons.httpclient.HttpStatus.SC_OK) {
//                String result = EntityUtils.toString(response.getEntity(), "GBK");
//                JSONObject jsonObject = JSONObject.parseObject(result);
//                String taoBaoIpJson = JSONObject.toJSONString(jsonObject.get("data"));
//                taoBaoIp = JSON.parseObject(taoBaoIpJson, TaoBaoIp.class);
//            }
//        } catch (Exception e) {
//            // skip
//        } finally {
//            HttpUtil.closeQuietly(response);
//        }
//        return taoBaoIp;
//    }

    public static String get(String url) {
        CloseableHttpResponse response = null;
        String responseText = "";
        try {
            HttpGet httpGet = new HttpGet(url);
            response = HttpUtil.getHttpClient().execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                responseText = EntityUtils.toString(response.getEntity(), "UTF-8");
            } else {
                throw new IOException("返回结果：" + statusCode);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            HttpUtil.closeQuietly(response);
        }
        return responseText;
    }

    public static String getUrl(String url) {
        CloseableHttpResponse response = null;
        String responseText = "";
        try {
            HttpGet httpGet = new HttpGet(url);
            response = HttpClients.createDefault().execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            responseText = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            HttpUtil.closeQuietly(response);
        }
        return responseText;
    }

    public static String post(String url, StringEntity stringEntity) {
        CloseableHttpResponse response = null;
        String responseText = "";
        try {
            // 请求
            HttpPost post = new HttpPost(url);
            post.addHeader("User-Agent", "IOS");
            post.setEntity(stringEntity);
            response = HttpUtil.getHttpClient().execute(post);
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                responseText = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.err.println(responseText);
            } else {
                System.err.println("错误的结果：" + status);
                System.err.println(responseText);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            HttpUtil.closeQuietly(response);
        }
        return responseText;
    }

    public static String post(String url, MultipartEntityBuilder entityBuilder) {
        CloseableHttpResponse response = null;
        String responseText = "";
        try {
            // 请求
            HttpPost post = new HttpPost(url);
            post.addHeader("User-Agent", "IOS");
            post.setEntity(entityBuilder.build());
            response = HttpUtil.getHttpClient().execute(post);
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                responseText = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.err.println(responseText);
            } else {
                System.err.println("错误的结果：" + status);
                System.err.println(responseText);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            HttpUtil.closeQuietly(response);
        }
        return responseText;
    }

    /**
     * 获取服务器地址
     * @param request
     * @return
     */
    public static String getServerPath(HttpServletRequest request) {
        String path = request.getContextPath();
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    }



    public static void main (String[] args) {


//        CloseableHttpResponse response = null;
//        try {
//            HttpGet httpGet = new HttpGet("http://www.yy6080.org/scripts/tree_items.js");
////            HttpGet httpGet = new HttpGet("http://so.csdn.net/so/search/s.do?q=JS+%E9%97%AE%E9%A2%98&t=null&o=null&s=null");
//            response = HttpUtil.getHttpClient().execute(httpGet);
//            int statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode == 200) {
//                System.out.println(EntityUtils.toByteArray(response.getEntity()).length);
//            } else {
//                throw new IOException("返回结果：" + statusCode);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        } finally {
//            HttpUtil.closeQuietly(response);
//        }
    }
}
