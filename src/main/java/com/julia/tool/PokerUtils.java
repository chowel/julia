package com.julia.tool;

import io.swagger.models.auth.In;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2024-09-26 21:29
 **/
public class PokerUtils {

    public static List<Poker> pokers;

    static {
        Poker p1 = new Poker(1, 2, "Hearts", "2");
        Poker p2 = new Poker(2, 3, "Hearts", "3");
        Poker p3 = new Poker(3, 4, "Hearts", "4");
        Poker p4 = new Poker(4, 5, "Hearts", "5");
        Poker p5 = new Poker(5, 6, "Hearts", "6");
        Poker p6 = new Poker(6, 7, "Hearts", "7");
        Poker p7 = new Poker(7, 8, "Hearts", "8");
        Poker p8 = new Poker(8, 9, "Hearts", "9");
        Poker p9 = new Poker(9, 10, "Hearts", "10");
        Poker p10 = new Poker(10, 11, "Hearts", "J");
        Poker p11 = new Poker(11, 12, "Hearts", "Q");
        Poker p12 = new Poker(12, 13, "Hearts", "K");
        Poker p13 = new Poker(13, 14, "Hearts", "A");

        Poker p14 = new Poker(14, 2, "Diamonds", "2");
        Poker p15 = new Poker(15, 3, "Diamonds", "3");
        Poker p16 = new Poker(16, 4, "Diamonds", "4");
        Poker p17 = new Poker(17, 5, "Diamonds", "5");
        Poker p18 = new Poker(18, 6, "Diamonds", "6");
        Poker p19 = new Poker(19, 7, "Diamonds", "7");
        Poker p20 = new Poker(20, 8, "Diamonds", "8");
        Poker p21 = new Poker(21, 9, "Diamonds", "9");
        Poker p22 = new Poker(22, 10, "Diamonds", "10");
        Poker p23 = new Poker(23, 11, "Diamonds", "J");
        Poker p24 = new Poker(24, 12, "Diamonds", "Q");
        Poker p25 = new Poker(25, 13, "Diamonds", "K");
        Poker p26 = new Poker(26, 14, "Diamonds", "A");

        Poker p27 = new Poker(27, 2, "Clubs", "2");
        Poker p28 = new Poker(28, 3, "Clubs", "3");
        Poker p29 = new Poker(29, 4, "Clubs", "4");
        Poker p30 = new Poker(30, 5, "Clubs", "5");
        Poker p31 = new Poker(31, 6, "Clubs", "6");
        Poker p32 = new Poker(32, 7, "Clubs", "7");
        Poker p33 = new Poker(33, 8, "Clubs", "8");
        Poker p34 = new Poker(34, 9, "Clubs", "9");
        Poker p35 = new Poker(35, 10, "Clubs", "10");
        Poker p36 = new Poker(36, 11, "Clubs", "J");
        Poker p37 = new Poker(37, 12, "Clubs", "Q");
        Poker p38 = new Poker(38, 13, "Clubs", "K");
        Poker p39 = new Poker(39, 14, "Clubs", "A");

        Poker p40 = new Poker(40, 2, "Spades", "2");
        Poker p41 = new Poker(41, 3, "Spades", "3");
        Poker p42 = new Poker(42, 4, "Spades", "4");
        Poker p43 = new Poker(43, 5, "Spades", "5");
        Poker p44 = new Poker(44, 6, "Spades", "6");
        Poker p45 = new Poker(45, 7, "Spades", "7");
        Poker p46 = new Poker(46, 8, "Spades", "8");
        Poker p47 = new Poker(47, 9, "Spades", "9");
        Poker p48 = new Poker(48, 10, "Spades", "10");
        Poker p49 = new Poker(49, 11, "Spades", "J");
        Poker p50 = new Poker(50, 12, "Spades", "Q");
        Poker p51 = new Poker(51, 13, "Spades", "K");
        Poker p52 = new Poker(52, 14, "Spades", "A");

        Poker p53 = new Poker(53, 15, "Joker", "S");
        Poker p54 = new Poker(54, 16, "Joker", "B");
        pokers = new ArrayList<>();
        pokers.add(p1);
        pokers.add(p2);
        pokers.add(p3);
        pokers.add(p4);
        pokers.add(p5);
        pokers.add(p6);
        pokers.add(p7);
        pokers.add(p8);
        pokers.add(p9);
        pokers.add(p10);
        pokers.add(p11);
        pokers.add(p12);
        pokers.add(p13);
        pokers.add(p14);
        pokers.add(p15);
        pokers.add(p16);
        pokers.add(p17);
        pokers.add(p18);
        pokers.add(p19);
        pokers.add(p20);
        pokers.add(p21);
        pokers.add(p22);
        pokers.add(p23);
        pokers.add(p24);
        pokers.add(p25);
        pokers.add(p26);
        pokers.add(p27);
        pokers.add(p28);
        pokers.add(p29);
        pokers.add(p30);
        pokers.add(p31);
        pokers.add(p32);
        pokers.add(p33);
        pokers.add(p34);
        pokers.add(p35);
        pokers.add(p36);
        pokers.add(p37);
        pokers.add(p38);
        pokers.add(p39);
        pokers.add(p40);
        pokers.add(p41);
        pokers.add(p42);
        pokers.add(p43);
        pokers.add(p44);
        pokers.add(p45);
        pokers.add(p46);
        pokers.add(p47);
        pokers.add(p48);
        pokers.add(p49);
        pokers.add(p50);
        pokers.add(p51);
        pokers.add(p52);
        pokers.add(p53);
        pokers.add(p54);
    }

    public static Map<String, List<Poker>> dealThirteen() {
        List<Integer> pokerIndexs = new ArrayList<>();
        for (int i = 0; i < 52; i++) {
            pokerIndexs.add(i);
        }
        Collections.shuffle(pokerIndexs);
        List<Poker> onePokers = new ArrayList<>();
        List<Poker> twoPokers = new ArrayList<>();
        List<Poker> threePokers = new ArrayList<>();
        List<Poker> fourPokers = new ArrayList<>();
        for (int j = 0; j < pokerIndexs.size(); j++) {
            if (j < 13) {
                onePokers.add(pokers.get(pokerIndexs.get(j)));
            }
            if (j > 12 && j < 26) {
                twoPokers.add(pokers.get(pokerIndexs.get(j)));
            }
            if (j > 25 && j < 39) {
                threePokers.add(pokers.get(pokerIndexs.get(j)));
            }
            if (j > 38 && j < 52) {
                fourPokers.add(pokers.get(pokerIndexs.get(j)));
            }
        }
        Map<String, List<Poker>> pokerMap = new HashMap<>();
        pokerMap.put("one", onePokers);
        pokerMap.put("two", twoPokers);
        pokerMap.put("three", threePokers);
        pokerMap.put("four", fourPokers);

        return pokerMap;
    }

    public static List<Poker> shufflePoker() {
        List<Poker> shufflePokers = pokers.stream().limit(52).collect(Collectors.toList());
        Collections.shuffle(shufflePokers);
        return shufflePokers;
    }

    /**
     * @Description: 同花
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public static PokerMoldForFive checkFlush(List<Poker> fivePokers) {

        if (fivePokers.size() == 5) {

            Set<String> suits = new HashSet<>();
            fivePokers.forEach(p -> suits.add(p.getSuit()));
            if (suits.size() == 1) {
                fivePokers.sort(Comparator.comparing(Poker::getValue, Comparator.reverseOrder()));
                PokerMoldForFive pm = new PokerMoldForFive();
                pm.setName("flush");
                pm.setCname("同花");
                pm.setScore(1);
                pm.setRange(6);

                pm.setSuit(fivePokers.get(0).getSuit());
                pm.setMax(fivePokers.get(0).getValue());
                pm.setSub(fivePokers.get(1).getValue());
                pm.setMid(fivePokers.get(2).getValue());
                pm.setLittle(fivePokers.get(3).getValue());
                pm.setMinimum(fivePokers.get(4).getValue());
                return pm;
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * @Description: 顺
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public static PokerMoldForFive checkStraight(List<Poker> fivePokers) {
        if (fivePokers.size() == 5) {
            fivePokers.sort(Comparator.comparing(Poker::getValue, Comparator.reverseOrder()));
            boolean IsStraight = true;
            for (int i = 2; i < fivePokers.size(); i++) {
                int before = fivePokers.get(i - 1).getValue();
                int present = fivePokers.get(i).getValue();
                if ((before - present) != 1) {
                    IsStraight = false;
                    break;
                }
            }
            if (IsStraight) {
                int l = fivePokers.get(0).getValue();

                if (l == 14 && fivePokers.get(1).getValue() != 13) {
                    if (l - fivePokers.get(4).getValue() != 12) {
                        return null;
                    }
                } else {
                    if (l - fivePokers.get(1).getValue() != 1) {
                        return null;
                    }
                }

                PokerMoldForFive pm = new PokerMoldForFive();
                pm.setName("Straight");
                pm.setCname("顺");
                pm.setScore(1);
                pm.setSuit("NONE");
                pm.setRange(5);

                pm.setMax(fivePokers.get(0).getValue());
                pm.setSub(fivePokers.get(1).getValue());
//                pm.setMid(fivePokers.get(2).getValue());
//                pm.setLittle(fivePokers.get(3).getValue());
//                pm.setMinimum(fivePokers.get(4).getValue());
                return pm;
            } else {
                return null;
            }
        }
        return null;
    }

    public static PokerMoldForFive generateMold(List<Poker> fivePokers) {
        if (fivePokers.size() == 5) {
            PokerMoldForFive pm = new PokerMoldForFive();
            Map<Integer, Integer> map = new HashMap<>();
            fivePokers.forEach(poker -> {
                Integer count = map.get(poker.getValue());
                if (ObjectUtils.isEmpty(count)) {
                    map.put(poker.getValue(), 1);
                } else {
                    map.put(poker.getValue(), count + 1);
                }
            });

            Set<Integer> keys = map.keySet();
            //  单牌
            if (keys.size() == 5) {
                fivePokers.sort(Comparator.comparing(Poker::getValue, Comparator.reverseOrder()));
                pm.setName("Kicker");
                pm.setCname("单牌");
                pm.setScore(1);
                pm.setSuit("NONE");
                pm.setRange(1);

                pm.setMax(fivePokers.get(0).getValue());
                pm.setSub(fivePokers.get(1).getValue());
                pm.setMid(fivePokers.get(2).getValue());
                pm.setLittle(fivePokers.get(3).getValue());
                pm.setMinimum(fivePokers.get(4).getValue());
            }
            //  一对
            if (keys.size() == 4) {
                pm.setName("Pair");
                pm.setCname("对子");
                pm.setScore(1);
                pm.setSuit("NONE");
                pm.setRange(2);

                List<Integer> sortList = new ArrayList<>();

                for (Integer key : map.keySet()) {
                    Integer value = map.get(key);
                    if (value == 2) {
                        pm.setMax(key);
                    } else {
                        sortList.add(key);
                    }
                }
                sortList.sort(Comparator.reverseOrder());
                pm.setSub(sortList.get(0));
                pm.setMid(sortList.get(1));
                pm.setLittle(sortList.get(2));
            }
            //  三张 两对
            if (keys.size() == 3) {
                List<Integer> sortList = new ArrayList<>();
                int lit = -1;
                boolean isThree = false;
                for (Integer key : map.keySet()) {
                    Integer value = map.get(key);
                    if (value == 3) {
                        pm.setName("Trips");
                        pm.setCname("三条");
                        pm.setScore(1);
                        pm.setSuit("NONE");
                        pm.setRange(4);
                        pm.setMax(key);
                        isThree = true;
                    }
                    if (value == 2) {
                        isThree = false;
                        sortList.add(key);
                    }
                    if (value == 1) {
                        lit = key;
                    }
                }
                if (!isThree) {
                    pm.setName("DoublePair");
                    pm.setCname("两对");
                    pm.setScore(1);
                    pm.setSuit("NONE");
                    pm.setRange(3);
                    sortList.sort(Comparator.reverseOrder());
                    pm.setMax(sortList.get(0));
                    pm.setSub(sortList.get(1));
                    pm.setMid(lit);
                }
            }
            //  炸弹 葫芦
            if (keys.size() == 2) {
                for (Integer key : map.keySet()) {
                    Integer value = map.get(key);
                    if (value == 3) {
                        pm.setName("FullHouse");
                        pm.setCname("葫芦 ");
                        pm.setScore(1);
                        pm.setSuit("NONE");
                        pm.setRange(7);
                        pm.setMax(key);
                    }
                    if (value == 4) {
                        pm.setName("bomb");
                        pm.setCname("炸弹");
                        pm.setScore(4);
                        pm.setSuit("NONE");
                        pm.setRange(8);
                        pm.setMax(key);
                    }
                }
            }
            return pm;
        }
        return null;
    }

    public static PokerMoldForFive headerForThree(List<Poker> threePokers) {
        if (threePokers.size() == 3) {
            PokerMoldForFive pm = new PokerMoldForFive();
            Map<Integer, Integer> map = new HashMap<>();
            threePokers.forEach(poker -> {
                Integer count = map.get(poker.getValue());
                if (ObjectUtils.isEmpty(count)) {
                    map.put(poker.getValue(), 1);
                } else {
                    map.put(poker.getValue(), count + 1);
                }
            });
            Set<Integer> keys = map.keySet();
            if (keys.size() == 3) {
                threePokers.sort(Comparator.comparing(Poker::getValue, Comparator.reverseOrder()));
                pm.setName("Kicker");
                pm.setCname("单牌");
                pm.setScore(1);
                pm.setSuit("NONE");
                pm.setRange(1);

                pm.setMax(threePokers.get(0).getValue());
                pm.setSub(threePokers.get(1).getValue());
                pm.setMid(threePokers.get(2).getValue());

            }
            if (keys.size() == 2) {
                for (Integer key : map.keySet()) {
                    Integer value = map.get(key);
                    if (value == 2) {
                        pm.setMax(key);
                    } else {
                        pm.setSub(key);
                    }
                }
                pm.setName("Pair");
                pm.setCname("对子");
                pm.setScore(1);
                pm.setSuit("NONE");
                pm.setRange(2);
            }

            if (keys.size() == 1) {

                pm.setName("tricuspid");
                pm.setCname("三尖刀");
                pm.setScore(3);
                pm.setSuit("NONE");
                pm.setRange(12);

                pm.setMid(threePokers.get(0).getValue());
            }

            return pm;

        }
        return null;
    }

    public static List<Poker> findPokersByid(String ids) {
        String[] idArray = ids.split(",");
        List<Poker> p = new ArrayList<>();
        for (String s : idArray) {
            int id = Integer.parseInt(s);
            if (id > 0) {
                p.add(pokers.get(id - 1));
            }
        }
        return p;
    }

    ;
}
