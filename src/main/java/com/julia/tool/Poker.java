package com.julia.tool;

public class Poker {

    public int id;
    public int value;
    public String suit;
    public String rand;

    public Poker(int id, int value, String suit, String rand) {
        this.id = id;
        this.value = value;
        this.suit = suit;
        this.rand = rand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
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
}
