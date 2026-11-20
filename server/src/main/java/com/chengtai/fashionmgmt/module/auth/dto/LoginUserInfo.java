package com.chengtai.fashionmgmt.module.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 登录用户基础信息。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserInfo {
    private Long id;
    private String staffCode;
    private String staffName;
    private String loginName;
    private Long deptId;
    private String deptName;
    private String post;
    private List<String> roles;
    private List<String> roleNames;
}
