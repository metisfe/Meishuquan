package com.metis.base.widget.adapter.delegate;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.metis.base.R;
import com.metis.base.module.User;

/**
 * Created by Beak on 2015/8/25.
 */
public enum RoleDelegate {

    STUDENT (User.USER_ROLE_STUDENT, R.drawable.role_student, R.string.text_role_student),
    TEACHER (User.USER_ROLE_TEACHER, R.drawable.role_teacher, R.string.text_role_teacher),
    STUDIO (User.USER_ROLE_STUDIO, R.drawable.role_studio, R.string.text_role_studio),
    PARENT (User.USER_ROLE_PARENTS, R.drawable.role_parent, R.string.text_role_parent),
    FANS (User.USER_ROLE_FANS, R.drawable.role_fans, R.string.text_role_fans);

    private int roleId;
    private @DrawableRes int drawableId;
    private @StringRes int nameId;

    RoleDelegate (int roleId, @DrawableRes int drawableId, @StringRes int nameId) {
        this.roleId = roleId;
        this.drawableId = drawableId;
        this.nameId = nameId;
    }

    public int getRoleId() {
        return roleId;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public int getNameId() {
        return nameId;
    }
}
