/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.springcloud.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.me.learn.springcloud.jackson.Views;
import lombok.Data;

import java.util.List;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/11/6
 **/

@Data
public class User {
    private Long userId;
    private String userName;
    @JsonView(Views.Public.class)
    private String password;
    private Integer age;
    private List<String> hobbides;
    private String address1;
    private String address2;
    private String province;
    private String city;
    private String district;
}
