package com.jnshu.pojo;

import java.io.Serializable;

public class Profession implements Serializable {

    private static final long serialVersionUID = 7514540361859911313L;


    private Integer id;

    private String image;

    private String professionName;

    private String devType;

    private String direction;

    private Integer limitCon;

    private Integer difficulty;

    private Integer growthJunior;

    private Integer growthSenior;

    private Integer needCount;

    private Long salaryJunior;

    private Long salarySenior;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public String getProfessionName() {
        return professionName;
    }

    public void setProfessionName(String professionName) {
        this.professionName = professionName == null ? null : professionName.trim();
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction == null ? null : direction.trim();
    }

    public Integer getLimitCon() {
        return limitCon;
    }

    public void setLimitCon(Integer limitCon) {
        this.limitCon = limitCon;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getGrowthJunior() {
        return growthJunior;
    }

    public void setGrowthJunior(Integer growthJunior) {
        this.growthJunior = growthJunior;
    }

    public Integer getGrowthSenior() {
        return growthSenior;
    }

    public void setGrowthSenior(Integer growthSenior) {
        this.growthSenior = growthSenior;
    }

    public Integer getNeedCount() {
        return needCount;
    }

    public void setNeedCount(Integer needCount) {
        this.needCount = needCount;
    }

    public Long getSalaryJunior() {
        return salaryJunior;
    }

    public void setSalaryJunior(Long salaryJunior) {
        this.salaryJunior = salaryJunior;
    }

    public Long getSalarySenior() {
        return salarySenior;
    }

    public void setSalarySenior(Long salarySenior) {
        this.salarySenior = salarySenior;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    @Override
    public String toString() {
        return "Profession{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", professionName='" + professionName + '\'' +
                ", devType='" + devType + '\'' +
                ", direction='" + direction + '\'' +
                ", limitCon=" + limitCon +
                ", difficulty=" + difficulty +
                ", growthJunior=" + growthJunior +
                ", growthSenior=" + growthSenior +
                ", needCount=" + needCount +
                ", salaryJunior=" + salaryJunior +
                ", salarySenior=" + salarySenior +
                '}';
    }
}