package com.zj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xiaozj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto implements Serializable {

    private String username;

    private String password;
}
