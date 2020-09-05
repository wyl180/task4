package com.jnshu.service.impl;

import com.jnshu.util.RedisService;
import com.jnshu.dao.StudentMapper;
import com.jnshu.pojo.Page;
import com.jnshu.pojo.Student;
import com.jnshu.service.StudentServiece;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @ClassName StudentServieceImpl
 * @Description
 * @Author 韦延伦
 * @Date 2020/8/11 10:14
 * @Version 1.0
 */
@Service
public class StudentServieceImpl implements StudentServiece {
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    RedisService redisService;
    private static final String KEY = "studentList";

    @Override
    @SuppressWarnings("unchecked")
    public List<Student> selectAll() {
        List<Student> studentList = null;
        if (redisService.hasKey(KEY)) {
            studentList = (List<Student>) redisService.get(KEY);
            return studentList;
        } else {
            studentList = studentMapper.selectAll();
            redisService.set(KEY, studentList);
            return studentList;
        }
    }

    @Override
    public int getCount() {
        return studentMapper.getCount();
    }

    @Override
    public List<Student> queryByPage(Page page) {
        return studentMapper.queryByPage(page);
    }

}

