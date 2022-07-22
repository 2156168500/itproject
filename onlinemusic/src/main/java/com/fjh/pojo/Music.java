package com.fjh.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Music {

  private long id;
  private String title;
  private String singer;
  private String time;
  private String url;
  private long userid;




}
