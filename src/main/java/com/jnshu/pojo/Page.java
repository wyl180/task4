package com.jnshu.pojo;

import java.util.List;

/**
 * @ClassName Page
 * @Description
 * @Author 韦延伦
 * @Date 2020/8/17 19:41
 * @Version 1.0
 */
public class Page {
    /**
     * 当前页
     */
    private int currPage;
    /**
     * 每页显示的记录数
     */
    private int pageSize;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 总条数
     */
    private int totalCount;
    /**
     * sql语句最开始索引值
     */
    private int skips;
    /**
     * 当前页显示的具体数据
     */
    private List<Student> studentList;

    public Page(int curr,int size,int total){
        this.pageSize=size;
        //计算总页数
        this.totalPage=total%size==0?total/size:(total/size)+1;
        //不给当前页码变成0
        this.currPage=curr<1 ? 1:curr;
        //不让当前页码超过总页数
        this.currPage=curr>this.totalPage ?this.totalPage:this.currPage;
        //计算出最初索引值
        skips=(this.currPage-1)*this.pageSize;
        this.totalCount=total;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getSkips() {
        return skips;
    }

    public void setSkips(int skips) {
        this.skips = skips;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }
}
