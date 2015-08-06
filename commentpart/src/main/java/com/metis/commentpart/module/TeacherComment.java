package com.metis.commentpart.module;

import java.util.List;

/**
 * Created by Beak on 2015/8/5.
 */
public class TeacherComment {
    public Comment mainComment;
    public List<Comment> subCommentList;

    public boolean hasSubCommentList () {
        return subCommentList != null && !subCommentList.isEmpty();
    }
}
