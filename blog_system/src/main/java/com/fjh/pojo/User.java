package com.fjh.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements Serializable {
    private int userId;
    private String username;
    private String password;
}
