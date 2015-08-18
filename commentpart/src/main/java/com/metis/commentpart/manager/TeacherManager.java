package com.metis.commentpart.manager;

import android.content.Context;

import com.metis.base.manager.AbsManager;
import com.metis.base.manager.CacheManager;
import com.metis.commentpart.adapter.delegate.TeacherCbDelegate;
import com.metis.commentpart.module.Teacher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beak on 2015/7/30.
 */
public class TeacherManager extends AbsManager {

    private static final String DB_NAME_RECENT_TEACHERS = "recent_teachers.db";

    private static final int MAX_SELECTED_COUNT = 3;

    private static TeacherManager sManager = null;

    public synchronized static TeacherManager getInstance (Context context) {
        if (sManager == null) {
            sManager = new TeacherManager(context.getApplicationContext());
        }
        return sManager;
    }

    private List<TeacherCbDelegate> mSelectedTeachers = new ArrayList<TeacherCbDelegate>();

    private List<OnTeachersListener> mTeacherListenerList = new ArrayList<OnTeachersListener> ();

    private TeacherManager(Context context) {
        super(context);
    }

    public List<TeacherCbDelegate> getRecentTeachers () {
        List<Teacher> teacherList = CacheManager.getInstance(getContext()).readUserDataAtDatabase(Teacher.class, DB_NAME_RECENT_TEACHERS);
        if (teacherList == null) {
            return null;
        }
        final int length = teacherList.size();
        List<TeacherCbDelegate> delegates = new ArrayList<TeacherCbDelegate>();
        for (int i = 0; i < length; i++) {
            TeacherCbDelegate delegate = new TeacherCbDelegate(teacherList.get(i));
            delegate.setChecked(hasSelected(delegate));
            delegates.add(delegate);
        }
        return delegates;
    }

    public void commitRecentTeachers () {
        List<Teacher> teacherList = new ArrayList<Teacher>();
        final int length = mSelectedTeachers.size();
        for (int i = 0; i < length; i++) {
            teacherList.add(mSelectedTeachers.get(i).getSource());
        }
        CacheManager.getInstance(getContext()).saveAllUserDataAtDatabase(teacherList, DB_NAME_RECENT_TEACHERS, Teacher.class, true);

        for (OnTeachersListener listener : mTeacherListenerList) {
            listener.onConfirmed(teacherList);
        }
    }

    public List<TeacherCbDelegate> getSelectedTeachers () {
        return mSelectedTeachers;
    }

    public boolean hasSelected (TeacherCbDelegate delegate) {
        return mSelectedTeachers.contains(delegate);
    }

    public void selectTeacher (TeacherCbDelegate delegate) {
        if (mSelectedTeachers.size() >= MAX_SELECTED_COUNT) {
            return;
        }
        if (hasSelected(delegate)) {
            return;
        }
        mSelectedTeachers.add(delegate);
        delegate.setChecked(hasSelected(delegate));
        final int length = mTeacherListenerList.size();
        for (int i = 0; i < length; i++) {
            OnTeachersListener listener = mTeacherListenerList.get(i);
            if (listener != null) {
                listener.onSelected(this, delegate.getSource());
            }
        }
    }

    public void unSelectTeacher (Teacher teacher) {
        /*TeacherCbDelegate delegate = new TeacherCbDelegate(teacher);*/
        TeacherCbDelegate delegate = findDelegateByTeacher(teacher);
        if (delegate != null) {
            unSelectTeacher(delegate);
        }
    }

    public void unSelectTeacher (TeacherCbDelegate delegate) {
        if (!hasSelected(delegate)) {
            return;
        }
        mSelectedTeachers.remove(delegate);
        delegate.setChecked(hasSelected(delegate));
        final int length = mTeacherListenerList.size();
        for (int i = 0; i < length; i++) {
            OnTeachersListener listener = mTeacherListenerList.get(i);
            if (listener != null) {
                listener.onUnSelected(this, delegate.getSource());
            }
        }
    }

    public TeacherCbDelegate findDelegateByTeacher (Teacher teacher) {
        final int length = mSelectedTeachers.size();
        for (int i = 0; i < length; i++) {
            TeacherCbDelegate delegate = mSelectedTeachers.get(i);
            if (delegate.getSource().equals(teacher)) {
                return delegate;
            }
        }
        return null;
    }

    public int getSelectedCount () {
        return mSelectedTeachers.size();
    }

    public int getMaxCount () {
        return MAX_SELECTED_COUNT;
    }

    public void clearSelected () {
        mSelectedTeachers.clear();
    }

    public void registerOnTeachersListener(OnTeachersListener listener) {
        if (mTeacherListenerList.contains(listener)) {
            return;
        }
        mTeacherListenerList.add(listener);
    }

    public void unregisterOnTeachersListener(OnTeachersListener listener) {
        if (mTeacherListenerList.contains(listener)) {
            mTeacherListenerList.remove(listener);
        }
    }

    public static interface OnTeachersListener {
        public void onConfirmed (List<Teacher> teachers);
        public void onSelected(TeacherManager manager, Teacher teacher);
        public void onUnSelected (TeacherManager manager, Teacher teacher);
    }
}
