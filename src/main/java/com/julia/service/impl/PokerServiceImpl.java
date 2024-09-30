package com.julia.service.impl;

import com.julia.tool.Poker;
import com.julia.tool.PokerUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2024-09-28 00:22
 **/
@Service
public class PokerServiceImpl {

    public Map<String,List<Poker>> oneHanderThirteen(){
       return PokerUtils.dealThirteen();
    }
}
