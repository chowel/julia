package com.julia.model;

import lombok.Data;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2025-10-05 20:52
 **/

@Data
public class VisitHost {

    private String hostname;

    private Long visitCount;

    private Long visitInputCount;
}
