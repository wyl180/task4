package com.jnshu.service.impl;

import com.jnshu.dao.UserMapper;
import com.jnshu.pojo.User;
import com.jnshu.service.UserService;
import com.jnshu.util.ImgException;
import com.jnshu.util.OSSClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @ClassName AccountServiceImpl
 * @Description
 * @Author 韦延伦
 * @Date 2020/8/12 19:40
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    OSSClientUtil ossClient;
    @Override
    public int registUser(User user) {
        return userMapper.registUser(user);
    }

    @Override
    public User findUser(String username) {
        return userMapper.findUser(username);
    }

    @Override
    public String updateHead(MultipartFile file, String username) throws ImgException {
        if (file == null || file.getSize() <= 0) {
            throw new ImgException("头像不能为空");
        }
        String name = ossClient.uploadImg2Oss(file);
        String imgUrl = ossClient.getImgUrl(name);
        User user=new User();
        user.setUsername(username);
        user.setImage(imgUrl);
        //上传的同时把图片url更新到数据库中
        userMapper.updateHead(user);
        return imgUrl;
    }

}
