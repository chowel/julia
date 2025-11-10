package com.julia.tool;

import com.julia.model.game.CaluRoleBo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @program: julia
 * @description: 游戏工具
 * @author: Chowel.Master
 * @create: 2025-10-19 12:02
 **/
@Slf4j
public class GameUtils {

    private static final String[] FRIGAMEBOX = {"1", "2", "3", "4", "5", "6", "7"};

    private static final Integer[] ROLE_FRI_1 = {0, 1, 2, 3, 4};

    private static final Integer[] ROLE_FRI_2 = {5, 6, 7, 8, 9};

    private static final Integer[] ROLE_FRI_3 = {10, 11, 12, 13, 14};

    private static final Integer[] ROLE_FRI_4 = {0, 6, 12, 8, 4};

    private static final Integer[] ROLE_FRI_5 = {0, 1, 7, 13, 14};

    private static final Integer[] ROLE_FRI_6 = {10, 6, 2, 8, 14};

    private static final Integer[] ROLE_FRI_7 = {0, 6, 2, 8, 4};

    private static final Integer[] ROLE_FRI_8 = {10, 6, 12, 8, 14};

    private static final Integer[] ROLE_FRI_9 = {10, 11, 7, 3, 4};

    private static final String[] STAGAMEBOX = {"1", "2", "3", "4", "5", "6", "7"};



    public static List<CaluRoleBo> calcuFri(String[] gameFri) {
        List<CaluRoleBo> roles = new ArrayList<>();

        List<String[]> resultList = new ArrayList<>();


        String[] role1Result = {gameFri[ROLE_FRI_1[0]], gameFri[ROLE_FRI_1[1]], gameFri[ROLE_FRI_1[2]],
                gameFri[ROLE_FRI_1[3]], gameFri[ROLE_FRI_1[4]]};

        String[] role2Result = {gameFri[ROLE_FRI_2[0]], gameFri[ROLE_FRI_2[1]], gameFri[ROLE_FRI_2[2]],
                gameFri[ROLE_FRI_2[3]], gameFri[ROLE_FRI_2[4]]};

        String[] role3Result = {gameFri[ROLE_FRI_3[0]], gameFri[ROLE_FRI_3[1]], gameFri[ROLE_FRI_3[2]],
                gameFri[ROLE_FRI_3[3]], gameFri[ROLE_FRI_3[4]]};

        String[] role4Result = {gameFri[ROLE_FRI_4[0]], gameFri[ROLE_FRI_4[1]], gameFri[ROLE_FRI_4[2]],
                gameFri[ROLE_FRI_4[3]], gameFri[ROLE_FRI_4[4]]};

        String[] role5Result = {gameFri[ROLE_FRI_5[0]], gameFri[ROLE_FRI_5[1]], gameFri[ROLE_FRI_5[2]],
                gameFri[ROLE_FRI_5[3]], gameFri[ROLE_FRI_5[4]]};

        String[] role6Result = {gameFri[ROLE_FRI_6[0]], gameFri[ROLE_FRI_6[1]], gameFri[ROLE_FRI_6[2]],
                gameFri[ROLE_FRI_6[3]], gameFri[ROLE_FRI_6[4]]};

        String[] role7Result = {gameFri[ROLE_FRI_7[0]], gameFri[ROLE_FRI_7[1]], gameFri[ROLE_FRI_7[2]],
                gameFri[ROLE_FRI_7[3]], gameFri[ROLE_FRI_7[4]]};

        String[] role8Result = {gameFri[ROLE_FRI_8[0]], gameFri[ROLE_FRI_8[1]], gameFri[ROLE_FRI_8[2]],
                gameFri[ROLE_FRI_8[3]], gameFri[ROLE_FRI_8[4]]};

        String[] role9Result = {gameFri[ROLE_FRI_9[0]], gameFri[ROLE_FRI_9[1]], gameFri[ROLE_FRI_9[2]],
                gameFri[ROLE_FRI_9[3]], gameFri[ROLE_FRI_9[4]]};


        resultList.add(role1Result);
        resultList.add(role2Result);
        resultList.add(role3Result);
        resultList.add(role4Result);
        resultList.add(role5Result);
        resultList.add(role6Result);
        resultList.add(role7Result);
        resultList.add(role8Result);
        resultList.add(role9Result);

        for (int i = 0; i < 9; i++) {
            CaluRoleBo t = countConsecutiveFromStart(resultList.get(i), "ROLE_FRI_" + (i + 1));
            roles.add(t);

        }
        return roles;
    }

    /**
     * @Description: 生成Fri游戏
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public static String[] genGameFri(int length) {
        String[] result = new String[length];
        Random random = ThreadLocalRandom.current();

        for (int i = 0; i < length; i++) {
            result[i] = FRIGAMEBOX[random.nextInt(FRIGAMEBOX.length)];
        }

        return result;
    }

    /**
     * @Description: 生成Fri游戏
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public static String[] genGameSta(int length) {
        String[] result = new String[length];
        Random random = ThreadLocalRandom.current();

        for (int i = 0; i < length; i++) {
            result[i] = STAGAMEBOX[random.nextInt(STAGAMEBOX.length)];
        }

        return result;
    }

    /**
     * @Description: 计算规则
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public static CaluRoleBo countConsecutiveFromStart(String[] arr, String name) {

        CaluRoleBo bo = new CaluRoleBo();
        if (arr == null || arr.length == 0) {
            return null;
        }
        String first = arr[0];
        int count = 1;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] != null && arr[i].equals(first)) {
                count++;
            } else {
                break;
            }
        }
        bo.setRoleName(name);
        bo.setBall(count > 1 ? count : 0);
        bo.setType(first);
        bo.setOnEnd(0);

        return bo;
    }


}
