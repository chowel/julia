package com.julia.model.game;

import lombok.Data;

import java.util.List;

/**
 * @program: julia
 * @description: 上报结果
 * @author: Chowel.Master
 * @create: 2025-10-28 13:14
 **/

@Data
public class AppearRole {

    private String gameNo;

    private List<AppearRoleItem> roles;

    private Long gamePoint;

    private Long totalCoin;
}
