package com.julia.model.game;

import lombok.Data;

import java.util.List;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2025-11-03 20:13
 **/
@Data
public class PlayerBo {

    private Long spend;

    private Long coin;

    private Integer level;

    private List<MinaGame> games;

}
