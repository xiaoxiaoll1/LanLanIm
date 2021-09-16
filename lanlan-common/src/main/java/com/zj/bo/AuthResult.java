package com.zj.bo;

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
public class AuthResult implements Serializable {

	private String code;
	private String codeDesc;
	
}
