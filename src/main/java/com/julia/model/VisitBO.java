package com.julia.model;

import lombok.Data;

import java.util.List;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2025-10-05 21:00
 **/
@Data
public class VisitBO {

    private long visits;

    private long inputVisits;

    private long alives;

    private List<VisitHost> hosts;
}
