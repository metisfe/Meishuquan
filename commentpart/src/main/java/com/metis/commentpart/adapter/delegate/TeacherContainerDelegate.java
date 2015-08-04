package com.metis.commentpart.adapter.delegate;

import com.metis.base.widget.adapter.delegate.BaseDelegate;
import com.metis.commentpart.module.Teacher;

import java.util.List;

/**
 * Created by Beak on 2015/8/4.
 */
public class TeacherContainerDelegate extends BaseDelegate<List<Teacher>> {

    public TeacherContainerDelegate(List<Teacher> teachers) {
        super(teachers);
    }

    @Override
    public int getDelegateType() {
        return CommentDelegateType.TYPE_TEACHER_CONTAINER.getType();
    }
}
