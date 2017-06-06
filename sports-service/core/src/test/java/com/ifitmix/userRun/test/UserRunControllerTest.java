//package com.ifitmix.userRun.test;
//
//import com.ifitmix.common.spring.BaseConfiguration;
//import com.ifitmix.common.spring.ServiceClientConfiguration;
//import com.ifitmix.common.spring.WebApplication;
//import com.ifitmix.common.spring.WebMvcConfiguration;
//import com.ifitmix.sports.api.SportsUrl;
//import com.ifitmix.sports.api.dtos.UserRunDto;
//import com.ifitmix.sports.context.SportsApplication;
//import com.ifitmix.sports.controller.UserRunController;
//import net.sf.json.JSONObject;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.map.ObjectWriter;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
//import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.boot.test.SpringApplicationContextLoader;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.http.*;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.http.converter.FormHttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.net.URI;
//import java.nio.charset.Charset;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * Created by zhangtao on 2017/4/10.
// */
//@RunWith(SpringRunner.class)
//public class UserRunControllerTest {
////
////    private MockMvc mockMvc;
////
////    @Before
////    public void setUp() {
////        mockMvc = MockMvcBuilders.standaloneSetup(new UserRunController()).build();
////    }
//
//    @Test
//    public void addUserRun() throws Exception {
//        RestTemplate template = new RestTemplate();
//        FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
//        formHttpMessageConverter.setCharset(Charset.forName("UTF8"));
//        template.getMessageConverters().add( formHttpMessageConverter );
//        template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//        template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
//        UserRunDto userRunDto = new UserRunDto();
//        userRunDto.setDistance(1000L);
//        //设置值
//        ObjectMapper mapper = new ObjectMapper();
//        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//        String requestJson = ow.writeValueAsString(userRunDto);
//        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
//        parts.add("userRunZip", new FileSystemResource("/Users/zhangtao/Downloads/6623e182c38b46198132f630dc84cfa3.zip"));
//        parts.add("distance", 1000L);
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<MultiValueMap<String, Object>> imageEntity = new HttpEntity<MultiValueMap<String, Object>>(parts, httpHeaders);
//        ResponseEntity e = template.exchange("http://10.10.8.238:23111/v1/sports/run/1", HttpMethod.POST, imageEntity, String.class);
//        System.out.println(e.toString());
////        String responseString = this.mockMvc.perform( post("/v1/sports/run/1").contentType(MediaType.APPLICATION_JSON).content(requestJson))
////                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
////        System.out.println(responseString);
//    }
//
//}
