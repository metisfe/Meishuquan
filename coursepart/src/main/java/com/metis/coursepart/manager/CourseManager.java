package com.metis.coursepart.manager;

import android.content.Context;

import com.metis.base.framework.NetProxy;
import com.metis.base.manager.AbsManager;

/**
 * Created by Beak on 2015/7/3.
 */
public class CourseManager extends AbsManager {

    public CourseManager(Context context) {
        super(context);
    }

    public void getCourseList () {
        //NetProxy.getInstance(getContext()).doGetRequest();
    }
}
