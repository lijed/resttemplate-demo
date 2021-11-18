/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.springcloud.userservice;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/11/18
 **/
public class Test {
    private static final String WORK_DIR =  System.getProperty("user.dir");
    private static final String FILE_FOLDER  = WORK_DIR +  "\\resttemplate-user\\user-service\\files";
    public static void main(String[] args) throws IOException {


            Path path = FileSystems.getDefault().getPath(FILE_FOLDER);
            if (Files.notExists(path)) {
                Files.createDirectory(path, null);
            }
            if (Files.exists(path)) {
                System.out.println("exist");
            }


    }
}
