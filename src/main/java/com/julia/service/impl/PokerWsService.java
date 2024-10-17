package com.julia.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.julia.entity.GameEntity;
import com.julia.enums.RedisKeyEnum;
import com.julia.model.AliveGameRo;
import com.julia.model.PlayerGameRo;
import com.julia.model.PlayerRo;
import com.julia.model.WebSocketMsgBO;
import com.julia.model.vo.GameRoomEntityVO;
import com.julia.model.vo.GameThirteenEntityVO;
import com.julia.model.vo.PlayersEntityVO;
import com.julia.service.*;
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
    IGameRoomService roomService;

    @Resource
    IGameThirteenService thirteenService;

    @Resource
    IPlayersService playersService;

    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public void handleChannelMsg(String requestMsg, Channel channel) {
        WebSocketMsgBO bo = mapper.readValue(requestMsg, WebSocketMsgBO.class);
        // 心跳
        if ("PING".equals(bo.getSub())) {
            String userId = ChannelPond.findUserIdByChannel(channel);
//            log.info("心跳->userId: " + userId);
            WebSocketMsgBO myMsg = new WebSocketMsgBO();
            myMsg.setSub("PONG");
            myMsg.setData("PONG");
            channel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(myMsg)));
        }
        //加入房间
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

                PlayerGameRo playerGame =
                        (PlayerGameRo) redisUtils.get(RedisKeyEnum.PLAYERPOKERS.getKey() + userId + "_" + alive.getGameIde());

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
//          datas[0]  gameType datas[1] roomflag
            GameEntity game = gameService.findGameByRoom(Integer.parseInt(datas[0]), datas[1]);
            if (!ObjectUtils.isEmpty(game)) {
                String notSendPokerKey =
                        RedisKeyEnum.NOTSENDPOKER.getKey() + datas[0] + "_" + datas[1] + "_" + game.getGameNo();

                List<Poker> pokers = (List<Poker>) redisUtils.getLeftRemove(notSendPokerKey);

                PlayerGameRo playerGame = new PlayerGameRo();
                playerGame.setPrePokers(pokers);
                playerGame.setGameType(Integer.parseInt(datas[0]));
                playerGame.setRoomIde(datas[1]);
                playerGame.setPlayerId(Long.valueOf(userId));
                playerGame.setGameNo(game.getGameNo());

                redisUtils.set(RedisKeyEnum.PLAYERPOKERS.getKey() + userId + "_" + game.getGameNo(), playerGame);
                // 添加aliveGame
                AliveGameRo ro = new AliveGameRo();
                ro.setPlayId(Integer.parseInt(userId));
                ro.setGameIde(game.getGameNo());
                ro.setRoomIde(datas[1]);
                ro.setGameType(Integer.parseInt(datas[0]));
                redisUtils.set(RedisKeyEnum.ALIVEGAME.getKey() + userId, ro);

//            gameScoreService.saveGamePlayer(game.getGameNo(), Integer.valueOf(userId), game.getGameId());
                if ("1".equals(datas[0])) {
                    GameThirteenEntityVO vo = new GameThirteenEntityVO();
                    vo.setPlayerId(Long.valueOf(userId));
                    vo.setGameId(game.getGameId());
                    vo.setGameNo(game.getGameNo());

                    thirteenService.saveGameThirteenEntity(vo);
                }
                WebSocketMsgBO sendMsg = new WebSocketMsgBO();
                sendMsg.setSub("DISPOKERS");
                sendMsg.setData(playerGame);

                channel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(sendMsg)));
            }
        }
//        if("REPLAY".equals(bo.getSub())){
//            String userId = ChannelPond.findUserIdByChannel(channel);
//            String bData = (String) bo.getData();
//            String[] datas = bData.split("_");
//            GameEntity game = gameService.findGameByRoom(Integer.parseInt(datas[0]), datas[1]);
//            if(ObjectUtils.isEmpty(game)){
//                gameService.createThirteennGame(Integer.parseInt(datas[0]), datas[1]);
//            }
//        }
        if ("QUITROOM".equals(bo.getSub())) {
            String userId = ChannelPond.findUserIdByChannel(channel);
            PlayersEntityVO player = playersService.findOneById(Long.valueOf(userId));

            log.info("退出房间->userId: " + userId);
            log.info((String) bo.getData());
            String key = RedisKeyEnum.ROOMPLAYERS.getKey() + (String) bo.getData();
            Set<Object> players = redisUtils.sGet(key);
            int playerNum = players.size();
            for (Object element : players) {
                PlayerRo t = (PlayerRo) element;
                if (t.getPlayId().equals(Long.valueOf(userId))) {
                    redisUtils.setRemove(key, t);
                    if ((playerNum - 1) == 0) {
                        String bData = (String) bo.getData();
                        String[] datas = bData.split("_");
                        roomService.close(datas[1], Integer.valueOf(datas[0]));
                    }
                } else {
                    Channel userChannel = ChannelPond.findChannel(String.valueOf(t.getPlayId()));
                    if (!ObjectUtils.isEmpty(userChannel)) {
                        WebSocketMsgBO myMsg = new WebSocketMsgBO();
                        myMsg.setSub("PLAYERQUIT");
                        myMsg.setData(player);
                        userChannel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(myMsg)));

                    }
                }
            }
        }

        if ("GALE".equals(bo.getSub())) {
            String bData = (String) bo.getData();
            String[] datas = bData.split("_");
            // datas[0]->gameNo datas[1]->roomIde
            String RECEIVESKEY = RedisKeyEnum.RECEIVES.getKey() + "_" + datas[0];
            redisUtils.incr(RECEIVESKEY, 1);
            int gameReceive = (int) redisUtils.get(RECEIVESKEY);
            GameRoomEntityVO room = roomService.findOneByFlag(datas[1]);
            if (gameReceive == room.getPlayers()) {
                gameService.countScore(datas[0]);
            }
        }

    }

    public void delByUserId(String removeId) {

    }
}
