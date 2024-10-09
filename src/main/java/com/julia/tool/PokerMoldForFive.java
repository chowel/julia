package com.julia.tool;

/**
 * @program: julia
 * @description: 13张牌型
 * @author: Chowel.Master
 * @create: 2024-10-07 16:09
 **/
public class PokerMoldForFive {
    private String name;
    private String cname;
    private Integer score;
    private String suit;

    private Integer range;



    /**
     * 最大
     */
    private Integer max;
    /**
     * 次大
     */
    private Integer sub;
    /**
     * 中间
     */
    private Integer mid;
    /**
     * 次小
     */
    private Integer little;
    /**
     * 最小
     */
    private Integer minimum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getSub() {
        return sub;
    }

    public void setSub(Integer sub) {
        this.sub = sub;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Integer getLittle() {
        return little;
    }

    public void setLittle(Integer little) {
        this.little = little;
    }

    public Integer getMinimum() {
        return minimum;
    }

    public void setMinimum(Integer minimum) {
        this.minimum = minimum;
    }

    public Integer getRange() {
        return range;
    }

    public void setRange(Integer range) {
        this.range = range;
    }
}
