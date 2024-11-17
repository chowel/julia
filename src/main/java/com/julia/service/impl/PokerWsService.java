package com.julia.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.julia.entity.GameEntity;
import com.julia.entity.PlayersEntity;
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
import org.springframework.util.StringUtils;

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

    @Resource
    IRoomPlayerService roomPlayerService;

    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public void handleChannelMsg(String requestMsg, Channel channel) {
        WebSocketMsgBO bo = mapper.readValue(requestMsg, WebSocketMsgBO.class);
        // 心跳
        if ("PING".equals(bo.getSub())) {
            String userId = ChannelPond.findUserIdByChannel(channel);
            String heart = (String) redisUtils.get(RedisKeyEnum.HEARTBEAT.getKey() + userId);
            if (!StringUtils.hasLength(heart)) {
                redisUtils.set(RedisKeyEnum.HEARTBEAT.getKey() + userId, "1", 3600);
            }
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
            String bData = (String) bo.getData();
            String[] datas = bData.split("_");

            int gameType = Integer.parseInt(datas[0]);
            // 13水
            if (gameType == 1) {

                AliveGameRo alive = (AliveGameRo) redisUtils.get(RedisKeyEnum.ALIVEGAME.getKey() + userId);
                //  断线重连
                if (!ObjectUtils.isEmpty(alive) && datas[1].equals(alive.getRoomIde())) {
                    PlayerGameRo playerGame =
                            (PlayerGameRo) redisUtils.get(RedisKeyEnum.PLAYERPOKERS.getKey() + userId + ":" + alive.getGameIde());
                    if (!ObjectUtils.isEmpty(playerGame)) {
                        WebSocketMsgBO sendMsg = new WebSocketMsgBO();
                        sendMsg.setSub("DISPOKERS");
                        sendMsg.setData(playerGame);
                        channel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(sendMsg)));
                    }
                } else {
                    // 添加AliveGame
                    AliveGameRo ro = new AliveGameRo();
                    ro.setPlayId(Long.valueOf(userId));
                    ro.setRoomIde(datas[1]);
                    ro.setGameType(Integer.parseInt(datas[0]));
                    ro.setLoginName(player.getLoginName());
                    ro.setNickName(player.getNickName());
                    redisUtils.set(RedisKeyEnum.ALIVEGAME.getKey() + userId, ro);
                }

                String ROOMPLAYERKEY = RedisKeyEnum.ROOMPLAYERS.getKey() + datas[0] + ":" + datas[1];
                Set<Object> players = redisUtils.sGet(ROOMPLAYERKEY);
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


            }
            // 拉霸
            if (gameType == 2) {
                WebSocketMsgBO msg = new WebSocketMsgBO();
                msg.setSub("LABAROOM");
                msg.setData(player.getCoin());
                channel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(msg)));
            }
        }

        if ("READYPLAY".equals(bo.getSub())) {
            String userId = ChannelPond.findUserIdByChannel(channel);
            long pId = Long.parseLong(userId);
            String bData = (String) bo.getData();
            String[] datas = bData.split("_");
//          datas[0]  gameType datas[1] roomflag
            GameEntity game = gameService.findGameByRoom(Integer.parseInt(datas[0]), datas[1]);
            PlayersEntityVO player = playersService.findOneById(pId);
            if (!ObjectUtils.isEmpty(game)) {
                String NOTSENDPOKERKEY =
                        RedisKeyEnum.NOTSENDPOKER.getKey() + datas[0] + ":" + datas[1] + ":" + game.getGameNo();

                List<Poker> pokers = (List<Poker>) redisUtils.getLeftRemove(NOTSENDPOKERKEY);

                PlayerGameRo playerGame = new PlayerGameRo();
                playerGame.setPrePokers(pokers);
                playerGame.setGameType(Integer.parseInt(datas[0]));
                playerGame.setRoomIde(datas[1]);
                playerGame.setPlayerId(Long.valueOf(userId));
                playerGame.setGameNo(game.getGameNo());

                String PLAYERPOKERSKEY = RedisKeyEnum.PLAYERPOKERS.getKey() + userId + ":" + game.getGameNo();

                redisUtils.set(PLAYERPOKERSKEY, playerGame);
                // 修改 aliveGame
                AliveGameRo alive = (AliveGameRo) redisUtils.get(RedisKeyEnum.ALIVEGAME.getKey() + userId);
                if (!ObjectUtils.isEmpty(alive)) {
                    alive.setGameIde(game.getGameNo());
                    redisUtils.set(RedisKeyEnum.ALIVEGAME.getKey() + userId, alive);
                }
//                AliveGameRo ro = new AliveGameRo();
//                ro.setPlayId(Long.valueOf(userId));
//                ro.setGameIde(game.getGameNo());
//                ro.setRoomIde(datas[1]);
//                ro.setGameType(Integer.parseInt(datas[0]));
//                ro.setLoginName(player.getLoginName());
//                ro.setNickName(player.getNickName());


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
            redisUtils.set(RedisKeyEnum.SELFQUIT.getKey() + ":" + userId, "1", 30);
            PlayersEntityVO player = playersService.findOneById(Long.valueOf(userId));

            log.info("退出房间->userId: " + userId);
            log.info((String) bo.getData());

            String wsData = (String) bo.getData();
            String[] wsdatas = wsData.split("_");
            // wsdatas[0]->gameType wsdatas[1]->roomIde
            int gameType = Integer.parseInt(wsdatas[0]);
            if (gameType == 1) {
                redisUtils.del(RedisKeyEnum.ALIVEGAME.getKey() + userId);

                String ROOMPLAYERKEY = RedisKeyEnum.ROOMPLAYERS.getKey() + wsdatas[0] + ":" + wsdatas[1];

                Set<Object> players = redisUtils.sGet(ROOMPLAYERKEY);
                int playerNum = players.size();
                for (Object element : players) {
                    PlayerRo t = (PlayerRo) element;
                    Long playerId = Long.valueOf(userId);
                    if (t.getPlayId().equals(playerId)) {
                        roomPlayerService.playerSetOnline(playerId, 0, wsdatas[1]);
                        if ((playerNum - 1) == 0) {
                            roomService.close(wsdatas[1], gameType);
                            gameService.overGameByRoomFlag(wsdatas[1], gameType);
                        }
                        redisUtils.setRemove(ROOMPLAYERKEY, t);
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
            if (gameType == 2) {
                roomService.close(wsdatas[1], gameType);
                gameService.overGameByRoomFlag(wsdatas[1], gameType);
            }
        }

        if ("GALE".equals(bo.getSub())) {
            String bData = (String) bo.getData();
            String[] datas = bData.split("_");
            // datas[0]->gameNo datas[1]->roomIde
            String RECEIVESKEY = RedisKeyEnum.RECEIVES.getKey() + datas[0];
            redisUtils.incr(RECEIVESKEY, 1);
            int gameReceive = (int) redisUtils.get(RECEIVESKEY);
            GameRoomEntityVO room = roomService.findOneByFlag(datas[1]);
            if (gameReceive == room.getPlayers()) {
                redisUtils.del(RECEIVESKEY);
//                redisUtils.del(RedisKeyEnum.NOTSENDPOKER.getKey() + ":1:" + datas[1] + ":" + datas[0]);
                gameService.countScore(datas[0], datas[1]);
            }
        }

        if ("LABACLEANCOIN".equals(bo.getSub())) {
            String userId = ChannelPond.findUserIdByChannel(channel);
            String bData = (String) bo.getData();
            String[] datas = bData.split("_");
            // datas[0]->gameNo datas[1]-> 0 输 1赢 datas[2] 金币数
            gameService.computeCoinsByLaba(Long.valueOf(userId),
                    "0".equals(datas[1]) ? Integer.parseInt(datas[2]) : Integer.parseInt(datas[2]) * -1,
                    datas[0]);
            PlayersEntityVO player = playersService.findOneById(Long.valueOf(userId));
            WebSocketMsgBO msg = new WebSocketMsgBO();
            msg.setSub("LABAROOM");
            msg.setData(player.getCoin());
            channel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(msg)));
        }

    }

    @SneakyThrows
    public void delByUserId(String removeId) {
        String selfQuit = (String) redisUtils.get(RedisKeyEnum.SELFQUIT.getKey() + ":" + removeId);
        // 主动退出不存在,就是被动断线处理
        if (!StringUtils.hasLength(selfQuit)) {
            AliveGameRo ro = (AliveGameRo) redisUtils.get(RedisKeyEnum.ALIVEGAME.getKey() + removeId);
//            PlayersEntityVO rPlayer = playersService.findOneById(Long.valueOf(removeId));
            String ROOMPLAYERKEY = RedisKeyEnum.ROOMPLAYERS.getKey() + ro.getGameType() + ":" + ro.getRoomIde();
            PlayerRo removePlayer = new PlayerRo();
            removePlayer.setPlayId(Long.valueOf(ro.getPlayId()));
            removePlayer.setCoin(0);
            removePlayer.setLoginName(ro.getLoginName());
            removePlayer.setNickName(ro.getNickName());
            Set<Object> players = redisUtils.sGet(ROOMPLAYERKEY);
            for (Object element : players) {
                PlayerRo t = (PlayerRo) element;
                Channel userChannel = ChannelPond.findChannel(String.valueOf(t.getPlayId()));
                if (!ObjectUtils.isEmpty(userChannel)) {
                    WebSocketMsgBO myMsg = new WebSocketMsgBO();
                    myMsg.setSub("OFFLINE");
                    myMsg.setData(removePlayer);
                    userChannel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(myMsg)));
                } else {
                    log.info("断线用户" + t.getNickName());
                    roomPlayerService.playerSetOnline(t.getPlayId(), 0, ro.getRoomIde());
                    if ((players.size() - 1) == 0) {
                        log.info("关闭房间结束游戏");
                        roomService.close(ro.getRoomIde(), ro.getGameType());
                        gameService.overGameByRoomFlag(ro.getRoomIde(), ro.getGameType());
                    }
                    redisUtils.setRemove(ROOMPLAYERKEY, t);
                }
            }


        }
    }
}
