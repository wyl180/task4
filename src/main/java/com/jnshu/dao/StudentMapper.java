package com.jnshu.dao;

import com.jnshu.pojo.Page;
import com.jnshu.pojo.Student;

import java.util.List;

public interface StudentMapper {


    /**
     * 查找全部学生
     * @return
     */
    List<Student> selectAll();

    /**
     * 获取全部学生记录条数
     * @return
     */
    int getCount();

    List<Student> queryByPage(Page page);

    List<Student> queryAll();
}