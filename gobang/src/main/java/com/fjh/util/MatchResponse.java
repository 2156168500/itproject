package com.fjh.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MatchResponse {
    private boolean ok;
    private String reason;
    private String message;

}
