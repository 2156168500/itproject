package com.fjh.commity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

  private Long id;
  private String username;
  private String password;
  private String salt;
  private String email;
  private Long type;
  private Long status;
  private String activationCode;
  private String headerUrl;
  private Date createTime;
}
