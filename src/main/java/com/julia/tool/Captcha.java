package com.julia.tool;

import com.julia.model.CaptchaVo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-14 14:52
 **/
public class Captcha {
    private static final char[] CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'
    };

    private static Random rnd = new Random();

    private static String generateText() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(CHARS[rnd.nextInt(CHARS.length)]);
        }
        return sb.toString();
    }

    private static Color generateColor() {
        return new Color(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public static CaptchaVo createCode() throws IOException {
        int width = 120;
        int height = 40;

        // 创建 BufferedImage 对象
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 获取图片的 Graphics2D 对象
        Graphics2D g = image.createGraphics();

        // 绘制背景
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 绘制文本
        String text = generateText();
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(generateColor());
//        g.rotate(Math.toRadians(30), width / 2, height / 2);
        g.drawString(text, 10, 30);

        // 绘制噪点
//        for (int i = 0; i < 100; i++) {
//            Shape shape = new Ellipse2D.Double(rnd.nextDouble() * width, rnd.nextDouble() * height, rnd.nextDouble() * 5 + 1, rnd.nextDouble() * 5 + 1);
//            g.setColor(generateColor());
//            g.fill(shape);
//        }

        // 绘制干扰线
        for (int i = 0; i < 5; i++) {
            g.setColor(generateColor());
            g.drawLine(rnd.nextInt(width), rnd.nextInt(height), rnd.nextInt(width), rnd.nextInt(height));
        }

        // 释放 Graphics2D 对象
        g.dispose();

        // 输出图片
        // ImageIO.write(image, "PNG", new File("captcha.png"));
        // 返回base64
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        byte[] imageBytes = outputStream.toByteArray();
//        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        CaptchaVo vo =  new CaptchaVo();
        vo.setBase(Base64.getEncoder().encodeToString(imageBytes));
        vo.setCaptchaRes(text);
        vo.setCaptchaId(String.valueOf(System.currentTimeMillis()));
        return vo;
    }
}
