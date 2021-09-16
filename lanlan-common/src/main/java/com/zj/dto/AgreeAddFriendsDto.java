package com.zj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xiaozj
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgreeAddFriendsDto {

    private String username;

    private List<String> friends;


}
