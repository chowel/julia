package com.julia.model.dto;

import com.julia.tool.Poker;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2024-10-08 16:53
 **/
@Getter
@Setter
@ApiModel(value = "ReceivePokerDto", description = "玩家上交牌")
public class ReceivePokerDto {

    private Integer playId;

    private Integer gameType;

    private String roomIde;

    private String gameIde;

    private List<Poker> pokers;
}
