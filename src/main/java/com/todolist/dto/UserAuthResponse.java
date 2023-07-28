
package com.todolist.dto;

import lombok.Data;

@Data
public class UserAuthResponse {
    private String message;
    
    private String jwtToken;
    
    private boolean  status;
}
