package com.fjh.commity.entity;


public class LoginTicket {

  private Integer id;
  private Integer userId;
  private String ticket;
  private Integer status;
  private java.util.Date expired;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }


  public String getTicket() {
    return ticket;
  }

  public void setTicket(String ticket) {
    this.ticket = ticket;
  }


  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }


  public java.util.Date getExpired() {
    return expired;
  }

  public void setExpired(java.util.Date expired) {
    this.expired = expired;
  }

  @Override
  public String toString() {
    return "LoginTicket{" +
            "id=" + id +
            ", userId=" + userId +
            ", ticket='" + ticket + '\'' +
            ", status=" + status +
            ", expired=" + expired +
            '}';
  }
}
