package com.metis.commentpart.module;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Beak on 2015/8/5.
 */
public class PushCommentParams implements Serializable {

    private long assessId, replyCid, userId;
    private String content;
    private int score;
    private String points;
    private List<String> image;
    private String voice;
    private int commentType;
    private int commentSource;// 评论来源 0 老师评论 1 学生评论

    public long getAssessId() {
        return assessId;
    }

    public void setAssessId(long assessId) {
        this.assessId = assessId;
    }

    public long getReplyCid() {
        return replyCid;
    }

    public void setReplyCid(long replyCid) {
        this.replyCid = replyCid;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

    public int getCommentSource() {
        return commentSource;
    }

    public void setCommentSource(int commentSource) {
        this.commentSource = commentSource;
    }
}
