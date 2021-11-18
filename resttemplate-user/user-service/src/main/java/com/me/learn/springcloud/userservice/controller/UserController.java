/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.me.learn.springcloud.userservice.controller;

import com.me.learn.springcloud.entity.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/11/6
 **/

@RestController
@RequestMapping("/user")
public class UserController {

    private static ConcurrentHashMap<String, User> USER_RESPOSITORY = new ConcurrentHashMap<>();

    static {
        User user1 = new User();
        user1.setUserName("jed");
        USER_RESPOSITORY.put("jed", user1);

        User user2 = new User();
        user2.setUserName("thomos");
        USER_RESPOSITORY.put("thomos", user2);
    }

    @GetMapping("/{userName}")
    public User getUser(@PathVariable String userName, HttpServletResponse response ) {
        response.setHeader("myResponseHeader", "from jed");
        return USER_RESPOSITORY.get(userName);
    }

    @PostMapping
    public User saveUser(@RequestBody  User user) {
        User compute = USER_RESPOSITORY.compute(user.getUserName(), (key, value) -> {
            if (USER_RESPOSITORY.containsKey(key)) {
                return USER_RESPOSITORY.get(key);
            } else {
                return user;
            }
        });
        return compute;
    }

    private static final String WORK_DIR =  System.getProperty("user.dir");
    private static final String FILE_FOLDER  = WORK_DIR +  "\\resttemplate-user\\user-service\\files";

    @PostMapping(value = "upload")
    public void uploadFile(MultipartFile file) throws IOException {
        Path path = FileSystems.getDefault().getPath(FILE_FOLDER);
        if (Files.notExists(path)) {
            Files.createDirectory(path, null);
        }

        String targetFullFileName = FILE_FOLDER + "\\" + file.getOriginalFilename();
        file.getBytes();

        BufferedOutputStream bis = null;
        try {
            bis = new BufferedOutputStream(new FileOutputStream(targetFullFileName));
            bis.write(file.getBytes());
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                bis.close();
            }
        }
    }
}
