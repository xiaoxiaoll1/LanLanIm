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
public class LoginDto implements Serializable {

    private String username;

    private String password;

    private String aseKey;
}
