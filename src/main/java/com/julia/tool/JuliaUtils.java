package com.julia.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;
import java.util.Random;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2023-10-30 15:26
 **/
@Slf4j
public class JuliaUtils {
    /**
     * @Description: bean属性拷贝
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public static <T, E> T convertTo(T t, E e) {
        BeanUtils.copyProperties(e, t);
        return t;
    }

    /**
     * @Description: 电话号码中间4位脱敏
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public static String phoneEnsconce(String phone) {
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 获取HttpServletRequest
     *
     * @return {@link HttpServletRequest}
     */
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * 获取IP地址
     *
     * @param request {@link HttpServletRequest}
     * @return ip地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (!StringUtils.hasText(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.error("获取IP失败", e);
        }
        return ip;
    }

    /**
     * 获取今年的第一天
     *
     * @return {@link LocalDate}
     */
    public static LocalDate getFirstDay() {
        LocalDate now = LocalDate.now();
        return now.with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * 获取今天开始时间
     *
     * @param date {@link LocalDate}
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime getBeginToday(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.MIN);
    }

    /**
     * 获取今天结束时间
     *
     * @param date {@link LocalDate}
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime getEndToday(LocalDate date) {
        return LocalDateTime.of(date, LocalTime.MAX);
    }

    /**
     * 计算两个日期相差天数
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 天数
     */
    public static Long calculateDateDays(LocalDate beginTime, LocalDate endTime) {
        return endTime.toEpochDay() - beginTime.toEpochDay();
    }

    /**
     * String转LocalDateTime
     *
     * @param date      时间
     * @param localTime {@link LocalTime}
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime toLocalDateTime(String date, LocalTime localTime) {
        if (!StringUtils.hasText(date)) {
            return null;
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDateTime.of(LocalDate.parse(date, dtf), localTime);
    }

    /**
     * 获取x位验证码
     *
     * @return code
     */
    public static String randomCode(int size) {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    /**
     * 随机生成用户名
     *
     * @return String
     */
    public static String randomNickName() {
        String millis = String.valueOf(System.currentTimeMillis());
        String code = millis.substring(millis.length() - 4);
        return randomCode(3) + code;
    }

    /**
     * @Description: 金额转换成带两位小数点
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public static BigDecimal intToBigDecimalTwo(int value) {
        double d = value / (double) 100;
        BigDecimal bigDecimal1 = new BigDecimal(d);
        return bigDecimal1.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * @Description: 生成随机游戏ID
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public static String randomGameId() {
        char[] arr = {'a', 'b', 'c', 'd'};
        for (int j = 0; j < 4; j++) {
            Random rom = new Random();
            int a = rom.nextInt(75) + 48;
            int b = 0;
            //防止出现随机数超出的情况
            while ((a > 57 && a < 65) || (a > 90 && a < 97)) {
                b = rom.nextInt(75) + 48;
                a = b;
            }
            arr[j] = (char) a;
        }
        return String.valueOf(arr) + System.currentTimeMillis();
    }

    /**
     * @Description: 获取今天11:59:59时间戳
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public static Long todayTime() {
        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 获取今天的日期时间
        LocalDateTime today = LocalDateTime.now();
        // 设置时间为11:59:59
        LocalDateTime targetTime = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 23, 59,
                59);
        return targetTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();

    }
}
