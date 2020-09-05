package com.jnshu.service.impl;

import com.jnshu.dao.ProfessionMapper;
import com.jnshu.pojo.Profession;
import com.jnshu.service.ProfessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName professionServiceImpl
 * @Description
 * @Author 韦延伦
 * @Date 2020/8/11 17:47
 * @Version 1.0
 */
@Service

public class ProfessionServiceImpl implements ProfessionService {
    @Autowired
    ProfessionMapper professionMapper;
    @Override
    public List<Profession> selectAll() {
        return professionMapper.selectAll();
    }
}
