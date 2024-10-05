package com.julia.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.julia.entity.GameEntity;
import com.julia.enums.RedisKeyEnum;
import com.julia.model.AliveGameRo;
import com.julia.model.PlayerGameRo;
import com.julia.model.PlayerRo;
import com.julia.model.WebSocketMsgBO;
import com.julia.model.vo.PlayersEntityVO;
import com.julia.service.IGameScoreService;
import com.julia.service.IGameService;
import com.julia.service.IPlayersService;
import com.julia.socket.ChannelPond;
import com.julia.tool.Poker;
import com.julia.tool.RedisUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2024-09-30 10:30
 **/
@Log4j2
@Service
public class PokerWsService {

    @Resource
    RedisUtils redisUtils;

    @Resource
    IGameService gameService;

    @Resource
    IGameScoreService gameScoreService;

    @Resource
    IPlayersService playersService;

    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public void handleChannelMsg(String requestMsg, Channel channel) {
        WebSocketMsgBO bo = mapper.readValue(requestMsg, WebSocketMsgBO.class);

        if ("JOINROOM".equals(bo.getSub())) {
            String userId = ChannelPond.findUserIdByChannel(channel);
            PlayersEntityVO player = playersService.findOneById(Long.valueOf(userId));

            log.info("加入房间->userId: " + userId);
            log.info((String) bo.getData());
            String key = RedisKeyEnum.ROOMPLAYERS.getKey() + (String) bo.getData();
            Set<Object> players = redisUtils.sGet(key);
            for (Object element : players) {
                PlayerRo t = (PlayerRo) element;
                if (!t.getPlayId().equals(Long.valueOf(userId))) {
                    log.info(t.getNickName());
                    Channel userChannel = ChannelPond.findChannel(String.valueOf(t.getPlayId()));
                    if (!ObjectUtils.isEmpty(userChannel)) {
                        WebSocketMsgBO myMsg = new WebSocketMsgBO();
                        myMsg.setSub("JOINPLAYER");
                        myMsg.setData(player);
                        userChannel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(myMsg)));

                        WebSocketMsgBO sendMsg = new WebSocketMsgBO();
                        sendMsg.setSub("JOINPLAYER");
                        sendMsg.setData(t);
                        channel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(sendMsg)));
                    }
                }
            }

            AliveGameRo alive = (AliveGameRo) redisUtils.get(RedisKeyEnum.ALIVEGAME.getKey() + userId);
//            断线重连
            if (!ObjectUtils.isEmpty(alive)) {
                String alivekey =
                        RedisKeyEnum.NOTSENDPOKER.getKey() + alive.getGameType() + "_" + alive.getRoomIde() + "_" + alive.getGameIde();

                List<Poker> pokers = (List<Poker>) redisUtils.getLeftRemove(alivekey);

                PlayerGameRo playerGame = new PlayerGameRo();
                playerGame.setPrePokers(pokers);
                playerGame.setGameType(alive.getGameType());
                playerGame.setRoomIde(alive.getRoomIde());
                playerGame.setPlayerId(Integer.valueOf(userId));
                playerGame.setGameNo(alive.getGameIde());

                WebSocketMsgBO sendMsg = new WebSocketMsgBO();
                sendMsg.setSub("DISPOKERS");
                sendMsg.setData(playerGame);

                channel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(sendMsg)));
            }
        }

        if ("READYPLAY".equals(bo.getSub())) {
            String userId = ChannelPond.findUserIdByChannel(channel);
            String bData = (String) bo.getData();
            String[] datas = bData.split("_");
            GameEntity game = gameService.findGameByRoom(Integer.parseInt(datas[0]), datas[1]);
            String key = RedisKeyEnum.NOTSENDPOKER.getKey() + datas[0] + "_" + datas[1] + "_" + game.getGameNo();

            List<Poker> pokers = (List<Poker>) redisUtils.getLeftRemove(key);

            PlayerGameRo playerGame = new PlayerGameRo();
            playerGame.setPrePokers(pokers);
            playerGame.setGameType(Integer.parseInt(datas[0]));
            playerGame.setRoomIde(datas[1]);
            playerGame.setPlayerId(Integer.valueOf(userId));
            playerGame.setGameNo(game.getGameNo());

            redisUtils.set(RedisKeyEnum.PLAYERPOKERS.getKey() + userId + "_" + game.getGameNo(), playerGame);
            // 添加aliveGame
            AliveGameRo ro = new AliveGameRo();
            ro.setPlayId(Integer.parseInt(userId));
            ro.setGameIde(game.getGameNo());
            ro.setRoomIde(datas[1]);
            ro.setGameType(Integer.parseInt(datas[0]));
            redisUtils.set(RedisKeyEnum.ALIVEGAME.getKey() + userId, ro);

            gameScoreService.saveGamePlayer(game.getGameNo(), Integer.parseInt(datas[0]), game.getGameId());

            WebSocketMsgBO sendMsg = new WebSocketMsgBO();
            sendMsg.setSub("DISPOKERS");
            sendMsg.setData(playerGame);

            channel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(sendMsg)));
        }
    }
}
