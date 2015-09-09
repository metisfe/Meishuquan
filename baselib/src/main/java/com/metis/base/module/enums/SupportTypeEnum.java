package com.metis.base.module.enums;

/**
 * support or favorite type
 * Created by wangjin on 15/4/13.
 */
public enum SupportTypeEnum {
    Assess(1), AssessComment(2), News(3), NewsComment(4), Course(5), CourseComment(6), Circle(7), CircleComment(8),
    ActivityStudio(9), ActivityStudent(10), Activity(11),CircleActivity(12);


    private final int val;

    private SupportTypeEnum(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
