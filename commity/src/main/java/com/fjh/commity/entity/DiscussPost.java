package com.fjh.commity.entity;


public class DiscussPost {

  private Integer id;
  private Integer userId;
  private String title;
  private String content;
  private Integer type;
  private Integer status;
  private java.util.Date createTime;
  private Integer commentCount;
  private double score;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public Integer getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = Integer.parseInt(userId);
  }


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }


  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }


  public java.util.Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }


  public Integer getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(Integer commentCount) {
    this.commentCount = commentCount;
  }


  public double getScore() {
    return score;
  }

  public void setScore(double score) {
    this.score = score;
  }

  @Override
  public String toString() {
    return "DiscussPost{" +
            "id=" + id +
            ", userId='" + userId + '\'' +
            ", title='" + title + '\'' +
            ", content='" + content + '\'' +
            ", type=" + type +
            ", status=" + status +
            ", createTime=" + createTime +
            ", commentCount=" + commentCount +
            ", score=" + score +
            '}';
  }
}
