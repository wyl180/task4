package com.jnshu.util;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @ClassName TimeTag
 * @Description
 * @Author 韦延伦
 * @Date 2020/8/11 21:06
 * @Version 1.0
 */
public class TimeTag extends SimpleTagSupport {
    @Override
    public void doTag() throws JspException, IOException {
        Calendar calendar = Calendar.getInstance();
        // 设置格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //转换
        String dateStr = df.format(calendar.getTime());
        //输出
        JspWriter out = getJspContext().getOut();
        out.print(dateStr);
    }
}

