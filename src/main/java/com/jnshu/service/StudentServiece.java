package com.jnshu.service;

import com.jnshu.pojo.Page;
import com.jnshu.pojo.Student;

import java.util.List;

/**
 * @ClassName StudentServiece
 * @Description
 * @Author 韦延伦
 * @Date 2020/8/9 14:32
 * @Version 1.0
 */
public interface StudentServiece {
    List<Student> selectAll();
    /**
     * 获取全部学生记录条数
     * @return
     */
    int getCount();

    List<Student> queryByPage(Page page);

}
