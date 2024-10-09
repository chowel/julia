package com.julia.tool;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Poker poker = (Poker) o;
        return Objects.equals(id, poker.id) &&
                Objects.equals(value, poker.value) &&
                Objects.equals(suit, poker.suit) &&
                Objects.equals(rand, poker.rand) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, suit, rand, positionId);
    }
}
