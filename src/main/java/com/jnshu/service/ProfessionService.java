package com.jnshu.service;

import com.jnshu.pojo.Profession;

import java.util.List;

/**
 * @ClassName professionService
 * @Description
 * @Author 韦延伦
 * @Date 2020/8/9 14:35
 * @Version 1.0
 */
public interface ProfessionService {
    List<Profession> selectAll();
}
