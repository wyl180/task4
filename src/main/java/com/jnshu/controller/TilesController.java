package com.jnshu.controller;

import com.jnshu.pojo.Page;
import com.jnshu.pojo.Profession;
import com.jnshu.pojo.Student;
import com.jnshu.service.ProfessionService;
import com.jnshu.service.StudentServiece;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ProfessionController
 * @Description
 * @Author 韦延伦
 * @Date 2020/8/9 14:34
 * @Version 1.0
 */
@Controller
public class TilesController {
    @Autowired
    StudentServiece studentServiece;
    @Autowired
    ProfessionService professionService;
    @RequestMapping("/student")
    public String student(Map<String,Object> map){
        List<Student> studentList=studentServiece.selectAll();
        map.put("stus",studentList);
        return "student";
    }
    @RequestMapping("/u/profession")
    public String profession(Map<String,Object> map){
        List<Profession> professionList=professionService.selectAll();
        map.put("pro",professionList);
        return "profession";
    }

    /**
     * 分页接口
     * @param curr
     * @param pageSize
     * @return
     */
    @RequestMapping("/page")
    @ResponseBody
    public Map<String,Object> page(@RequestParam(required =true,defaultValue = "1") int curr, @RequestParam(required = false,defaultValue = "5") int pageSize){
        Map<String,Object> map=new HashMap<>();
        //获取总记录数
        int count=studentServiece.getCount();
        //把当前页curr，要显示的记录数，总记录数传进page里面做计算。
        Page page=new Page(curr,pageSize,count);
        //通过page对象的start开始索引字段，和pageSize查询出对应的具体数据
        List<Student> list=studentServiece.queryByPage(page);

        page.setStudentList(list);
        map.put("data",page);
        return map;
    }
}
