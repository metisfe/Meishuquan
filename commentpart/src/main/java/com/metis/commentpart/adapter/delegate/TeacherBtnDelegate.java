package com.metis.commentpart.adapter.delegate;

import com.metis.commentpart.module.Teacher;

/**
 * Created by Beak on 2015/8/4.
 */
public class TeacherBtnDelegate extends TeacherDelegate {

    public TeacherBtnDelegate(Teacher teacher) {
        super(teacher);
    }

    @Override
    public int getDelegateType() {
        return CommentDelegateType.TYPE_TEACHER_WITH_BTN.getType();
    }
}
