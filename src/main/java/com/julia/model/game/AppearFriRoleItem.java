package com.julia.model.game;

import lombok.Data;

/**
 * @program: julia
 * @description: 游戏结果上报
 * @author: Chowel.Master
 * @create: 2025-10-28 13:12
 **/
@Data
public class AppearFriRoleItem {

    private String roleName;

    private Integer type;

    private Integer score;
}
