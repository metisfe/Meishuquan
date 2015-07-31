package com.metis.commentpart.module;

import com.metis.base.module.User;

/**
 * Created by Beak on 2015/7/30.
 */
public class Teacher {

    private long id;

    public User user;

    public int commentCount;
    public int supportCount;
    public int relationType;

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
