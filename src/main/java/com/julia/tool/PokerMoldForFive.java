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
    private int score;
    private String suit;

    private int range;



    /**
     * 最大
     */
    private int max;
    /**
     * 次大
     */
    private int sub;
    /**
     * 中间
     */
    private int mid;
    /**
     * 次小
     */
    private int little;
    /**
     * 最小
     */
    private int minimum;

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getSub() {
        return sub;
    }

    public void setSub(int sub) {
        this.sub = sub;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getLittle() {
        return little;
    }

    public void setLittle(int little) {
        this.little = little;
    }

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }
}
