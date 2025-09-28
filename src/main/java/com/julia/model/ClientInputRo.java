package com.julia.model;

import lombok.Data;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2025-09-28 10:28
 **/
@Data
public class ClientInputRo {

    private String secure;

    private String loginName;

    private String password;

    private Integer status;

    private Integer checkVerify;
}
