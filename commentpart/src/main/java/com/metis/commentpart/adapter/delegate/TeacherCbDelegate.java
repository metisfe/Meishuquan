package com.metis.commentpart.adapter.delegate;

import android.widget.Checkable;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.commentpart.module.Teacher;

/**
 * Created by Beak on 2015/7/30.
 */
public class TeacherCbDelegate extends TeacherDelegate implements Checkable{

    private boolean isChecked = false;

    public TeacherCbDelegate(Teacher teacher) {
        super(teacher);
    }

    @Override
    public int getDelegateType() {
        return CommentDelegateType.TYPE_TEACHER_WITH_CHECK_BOX.getType();
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        isChecked = !isChecked;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof TeacherCbDelegate)) {
            return false;
        }
        return getSource().equals(((TeacherCbDelegate) o).getSource());
    }
}
