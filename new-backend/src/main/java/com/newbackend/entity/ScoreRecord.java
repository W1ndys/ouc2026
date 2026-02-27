package com.newbackend.entity;

import java.util.Date;

public class ScoreRecord {
    private String qq;
    private String name;
    private String examDirection;
    private String major;
    private int politicsScore;
    private int englishScore;
    private int mathScore;
    private int professionalScore;
    private int totalScore;
    private Date createTime;
    private Date updateTime;
    private int isDeleted;
    private String examNumber;
    private String picture;

    // Getters and Setters
    public String getQqNumber() {
        return qq;
    }

    public void setQqNumber(String qq) {
        this.qq = qq;
    }

    // 前端传递的字段名
    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExamDirection() {
        return examDirection != null ? examDirection : major;
    }

    public void setExamDirection(String examDirection) {
        this.examDirection = examDirection;
    }

    public String getMajor() {
        return major != null ? major : examDirection;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getPoliticsScore() {
        return politicsScore;
    }

    public void setPoliticsScore(int politicsScore) {
        this.politicsScore = politicsScore;
    }

    // 前端传递的字段名
    public int getPolitics() {
        return politicsScore;
    }

    public void setPolitics(int politics) {
        this.politicsScore = politics;
    }

    public int getEnglishScore() {
        return englishScore;
    }

    public void setEnglishScore(int englishScore) {
        this.englishScore = englishScore;
    }

    // 前端传递的字段名
    public int getEnglish() {
        return englishScore;
    }

    public void setEnglish(int english) {
        this.englishScore = english;
    }

    public int getMathScore() {
        return mathScore;
    }

    public void setMathScore(int mathScore) {
        this.mathScore = mathScore;
    }

    // 前端传递的字段名
    public int getMath() {
        return mathScore;
    }

    public void setMath(int math) {
        this.mathScore = math;
    }

    public int getProfessionalScore() {
        return professionalScore;
    }

    public void setProfessionalScore(int professionalScore) {
        this.professionalScore = professionalScore;
    }

    // 前端传递的字段名
    public int getProfessional() {
        return professionalScore;
    }

    public void setProfessional(int professional) {
        this.professionalScore = professional;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    // 前端传递的字段名
    public int getTotal() {
        return totalScore;
    }

    public void setTotal(int total) {
        this.totalScore = total;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    // 前端传递的字段名
    public int getIs_deleted() {
        return isDeleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.isDeleted = is_deleted;
    }

    public String getExamNumber() {
        return examNumber;
    }

    public void setExamNumber(String examNumber) {
        this.examNumber = examNumber;
    }

    // 前端传递的字段名
    public String getExam_number() {
        return examNumber;
    }

    public void setExam_number(String exam_number) {
        this.examNumber = exam_number;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}