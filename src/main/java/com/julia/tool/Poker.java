package com.julia.tool;

public class Poker {

    public Integer id;
    public Integer value;
    public String suit;
    public String rand;

    public Integer positionId;

    public Poker() {
        // 构造函数体
    }

    public Poker(int id, int value, String suit, String rand) {
        this.id = id;
        this.value = value;
        this.suit = suit;
        this.rand = rand;
        this.positionId = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getRand() {
        return rand;
    }

    public void setRand(String rand) {
        this.rand = rand;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }
}
