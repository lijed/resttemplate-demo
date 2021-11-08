/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.springcloud.orderservice.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.me.learn.springcloud.entity.User;
import com.me.learn.springcloud.jackson.Views;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/11/6
 **/

@RestController
@Slf4j
@RequestMapping("order")
public class RestTemplateController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;


    @RequestMapping("/hello")
    public String hello(String name) {

        String requestURI = getUserServiceRootUrl() + "/test/hello";
        System.out.println("request url: " + requestURI);
        String response = restTemplate.getForObject(requestURI, String.class);
        System.out.println("获取了结果：" + response);

        return "获取到了结果：" + response;
    }

    @GetMapping("/user/{userName}")
    public User testGetMapWithPathVariable(@PathVariable String userName) {

        String requestURI = getUserServiceRootUrl() + "/user/{userName}";
        System.out.println("request url：" + requestURI);
        Map<String, Object> paras = new HashMap<>();
        paras.put("userName", userName);
        User user = restTemplate.getForObject(requestURI, User.class, paras);
        return user;
    }

    /**
     * //String result = restTemplate.getForObject(
     * //        "https://example.com/hotels/{hotel}/bookings/{booking}", String.class, "42", "21");
     *
     * @param userName
     * @return
     */
    @GetMapping("/user/index/{userName}")
    public User testGetMappingWithPlaceHolder(@PathVariable String userName) {

        String requestURI = getUserServiceRootUrl() + "/user/{1}";  //占位符，第二个参数{2}
        System.out.println("request url：" + requestURI);
        User user = restTemplate.getForObject(requestURI, User.class, userName);
        return user;
    }

    @GetMapping("/user/responsebody/{userName}")
    public User testGetEntityMappingWithPlaceHolder(@PathVariable String userName) {

        String requestURI = getUserServiceRootUrl() + "/user/{1}";  //占位符，第二个参数{2}
        System.out.println("request url：" + requestURI);
        ResponseEntity<User> entity = restTemplate.getForEntity(requestURI, User.class, userName);
        HttpStatus statusCode = entity.getStatusCode();
        System.out.println("httpstatus: " + statusCode);
        int statusCodeValue = entity.getStatusCodeValue();
        System.out.println("status code: " + statusCodeValue);
        User user = entity.getBody();
        return user;
    }

    /***
     *
     * 加上JsonView注解，在返回的User对象里，只有加油@Views.Public.class的属性才会被序列化
     *
     *
     * */
    @JsonView(Views.Public.class)
    @GetMapping("/user/exchange/{userName}")
    public User testGettingMapExchange(@PathVariable String userName) {
        String requestURI = getUserServiceRootUrl() + "/user/{1}";  //占位符，第二个参数{2}
        URI uri = UriComponentsBuilder.fromUriString(requestURI).build(userName);

        RequestEntity<Void> requestEntity = RequestEntity.get(uri).header("myRequestHeader", "myHeader").build();

        ResponseEntity<User> responseEntity = restTemplate.exchange(requestEntity, User.class);

        String myResponseHeader = responseEntity.getHeaders().getFirst("myResponseHeader");

        System.out.println("the returned myResponseHeader: " + myResponseHeader);

        User user = responseEntity.getBody();
        return user;
    }


    @GetMapping("/user/save/{userName}/{password}")
    public User saveUser(@PathVariable String userName, @PathVariable String password) {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        String saveUserUri = getUserServiceRootUrl() + "/user";
        return restTemplate.postForObject(saveUserUri, user, User.class);
    }

    private String getUserServiceRootUrl() {
        List<ServiceInstance> instances = discoveryClient.getInstances("rest-user-service");
        if (CollectionUtils.isEmpty(instances)) {
            log.info("user-instances is null, {}", instances);
            throw new RuntimeException("No service URL is found");
        } else {
            ServiceInstance userServiceInstance = instances.get(0);
            return "http://" + userServiceInstance.getHost() + ":" + userServiceInstance.getPort();
        }
    }


}
