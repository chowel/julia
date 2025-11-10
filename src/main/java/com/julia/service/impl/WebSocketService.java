package com.julia.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.julia.entity.MinaPlayerEntity;
import com.julia.enums.RedisKeyEnum;
import com.julia.enums.RedisKeys;
import com.julia.model.*;
import com.julia.model.game.AppearRole;
import com.julia.model.game.MinaGame;
import com.julia.model.game.PlayerBo;
import com.julia.service.IMinaGameService;
import com.julia.service.IMinaPlayerService;
import com.julia.socket.ChannelPond;
import com.julia.tool.GameUtils;
import com.julia.tool.RedisUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: skychain
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-14 16:01
 **/
@Log4j2
@Service
public class WebSocketService {
    @Resource
    RedisUtils redisUtils;

    @Resource
    IMinaGameService minaGameService;

    @Resource
    IMinaPlayerService playerService;


    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public void handleMsg(String requestMsg) {
        WebSocketMsgBO bo = mapper.readValue(requestMsg, WebSocketMsgBO.class);
    }

    @SneakyThrows
    public void handleBinaryMsg(ByteBuffer buffer, Channel channel) {

        // 消息类型 66 心跳
        short msgType = buffer.getShort();

        // 处理心跳 服务端 66 客户端 67
        if (msgType == 66) {
            ByteBuf byteBuf = channel.alloc().buffer(2);
            byteBuf.writeShort(67);
            channel.writeAndFlush(new BinaryWebSocketFrame(byteBuf));
        }

        // 94 StaGame 发牌
//        if (msgType == 94) {
//            String playerId = ChannelPond.findClientIdByChannel(channel);
//            if (StringUtils.hasLength(playerId)) {
//
//            }
//        }

        // 90 进入Sta房间
        if (msgType == 90) {
            String playerId = ChannelPond.findClientIdByChannel(channel);
            if (StringUtils.hasLength(playerId)) {
                MinaPlayerEntity playerEntity = playerService.getById(Long.valueOf(playerId));
                PlayerBo bo = new PlayerBo();
                if (redisUtils.hasKey(RedisKeys.PLAYERCOIN.getKey(playerId))) {
                    bo.setCoin((redisUtils.getPlayerCoin(playerId)));
                } else {
                    redisUtils.calcCoin(playerId, playerEntity.getCoin());
                    bo.setCoin(playerEntity.getCoin());
                }

                // 发牌
                List<MinaGame> games = minaGameService.genGameByPlayerId(Long.valueOf(playerId), 8, 2);
                bo.setGames(games);
                bo.setLevel(playerEntity.getLevel());
                bo.setSpend(playerEntity.getSpend());
                ByteBuf byteBuf = Unpooled.buffer();
                byteBuf.writeShort(91);
                String json = mapper.writeValueAsString(bo);
                byteBuf.writeBytes(json.getBytes(StandardCharsets.UTF_8));
                log.info("StaGame JSON: {}", json);
                channel.writeAndFlush(new BinaryWebSocketFrame(byteBuf));

            }
        }

        // 80 进入Fri房间
        if (msgType == 80) {
            String playerId = ChannelPond.findClientIdByChannel(channel);
            if (StringUtils.hasLength(playerId)) {
                MinaPlayerEntity playerEntity = playerService.getById(Long.valueOf(playerId));
                PlayerBo bo = new PlayerBo();
                if (redisUtils.hasKey(RedisKeys.PLAYERCOIN.getKey(playerId))) {
                    bo.setCoin((redisUtils.getPlayerCoin(playerId)));
                } else {
                    redisUtils.calcCoin(playerId, playerEntity.getCoin());
                    bo.setCoin(playerEntity.getCoin());
                }

                // 发牌
                List<MinaGame> games = minaGameService.genGameByPlayerId(Long.valueOf(playerId), 8, 1);
                bo.setGames(games);
                bo.setLevel(playerEntity.getLevel());
                bo.setSpend(playerEntity.getSpend());
                ByteBuf byteBuf = Unpooled.buffer();
                byteBuf.writeShort(81);
                String json = mapper.writeValueAsString(bo);
                byteBuf.writeBytes(json.getBytes(StandardCharsets.UTF_8));
                log.info("JSON: {}", json);
                channel.writeAndFlush(new BinaryWebSocketFrame(byteBuf));

            }
        }

        // 游戏 发放游戏
        if (msgType == 84 || msgType == 94) {
            String playerId = ChannelPond.findClientIdByChannel(channel);
            if (StringUtils.hasLength(playerId)) {
                List<MinaGame> games = null;
                ByteBuf byteBuf = Unpooled.buffer();

                if( msgType == 84){
                    games = minaGameService.genGameByPlayerId(Long.valueOf(playerId), 8, 1);
                    byteBuf.writeShort(85);
                }

                if( msgType == 94){
                    games = minaGameService.genGameByPlayerId(Long.valueOf(playerId), 8, 2);
                    byteBuf.writeShort(95);
                }

                String jsonGame = mapper.writeValueAsString(games);
                byteBuf.writeBytes(jsonGame.getBytes(StandardCharsets.UTF_8));
                log.info("发牌: {}", byteBuf.readableBytes());
                channel.writeAndFlush(new BinaryWebSocketFrame(byteBuf));
            }
        }
        // 退出friday 房间
        if (msgType == 82) {
            String playerId = ChannelPond.findClientIdByChannel(channel);
            if (StringUtils.hasLength(playerId)) {
                long playerCoin = redisUtils.getPlayerCoin(playerId);
                redisUtils.del(RedisKeys.PLAYERINFO.getKey(playerId));
                redisUtils.del(RedisKeys.PLAYERCOIN.getKey(playerId));
                log.info("log: {}", "更新数据库");
                playerService.updateCoinSpend(Long.valueOf(playerId), playerCoin, 0L);

                // 总数不一致，以服务端下发为准
                PlayerBo bo = new PlayerBo();
                bo.setCoin(playerCoin);
                this.sendInfo(channel, bo);
            }
        }

        // 统一上报 上报游戏结果
        if (msgType == 68) {
            String playerId = ChannelPond.findClientIdByChannel(channel);
            if (StringUtils.hasLength(playerId)) {
                // 获取剩余可读字节
                int remaining = buffer.remaining();
                // 提取剩余字节
                byte[] bytes = new byte[remaining];
                // 将剩余字节读入数组，position 自动移动到末尾
                buffer.get(bytes);
                // 转换为字符串（推荐 UTF-8）
                String text = new String(bytes, StandardCharsets.UTF_8);


                AppearRole appearRole = mapper.readValue(text, AppearRole.class);

                log.info("游戏类型: {}",appearRole.getType());

                long calc_coin = redisUtils.calcCoin(playerId, appearRole.getGamePoint());

                log.info("GameNo: {} -- Coin: {}", appearRole.getGameNo(), calc_coin);

                if (appearRole.getTotalCoin() != calc_coin) {
                    // 总数不一致，以服务端下发为准
                    PlayerBo bo = new PlayerBo();
                    bo.setCoin(calc_coin);
                    this.sendInfo(channel, bo);
                }
            }

        }
    }

    /**
     * @Description: 下线删除
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public void disconnect(Channel c) {
        String res = ChannelPond.removeChannel(c);
        if (res.length() > 9) {
            log.info("客户端退出");
            WebSocketMsgBO adminInform = new WebSocketMsgBO();
            adminInform.setSub("CLIENTOUT");
            adminInform.setData(res);
            adminSendMsg(adminInform);
        }
    }

    @SneakyThrows
    protected void adminSendMsg(WebSocketMsgBO adminInform) {
        ConcurrentHashMap<String, ChannelId> admins = ChannelPond.getAllAdmin();
        for (ChannelId channelId : admins.values()) {
            Channel adminChannel = ChannelPond.findAdminChannelByChannelId(channelId);
            if (!ObjectUtils.isEmpty(adminChannel)) {
                adminChannel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(adminInform)));
            }
        }
    }

    @SneakyThrows
    protected void sendInfo(Channel c, PlayerBo bo) {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeShort(89);
        String jsonGame = mapper.writeValueAsString(bo);
        byteBuf.writeBytes(jsonGame.getBytes(StandardCharsets.UTF_8));
        c.writeAndFlush(new BinaryWebSocketFrame(byteBuf));
    }

}
