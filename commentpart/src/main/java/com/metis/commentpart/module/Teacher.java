package com.metis.commentpart.module;

import com.metis.base.module.User;

/**
 * Created by Beak on 2015/7/30.
 */
public class Teacher {

    public static final int
            RELATION_TYPE_NONE = 0,
            RELATION_TYPE_I_FOCUS = 1,
            RELATION_TYPE_I_WAS_FOLLOWED = 2,
            RELATION_TYPE_EACH = 3;

    private long id;

    public User user;

    public int commentCount;
    public int supportCount;
    public int relationType;//关注状态 0 无 1我关注他 2他关注我 3 相互关注


    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Teacher)) {
            return false;
        }
        if (user == null) {
            return false;
        }
        return user.equals(((Teacher) o).user);
    }
}
