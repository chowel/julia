package com.julia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.julia.entity.*;
import com.julia.enums.RedisKeyEnum;
import com.julia.mapper.GameMapper;
import com.julia.model.*;
import com.julia.model.dto.ReceivePokerDto;
import com.julia.model.vo.GameRoomEntityVO;
import com.julia.model.vo.GameThirteenEntityVO;
import com.julia.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.julia.socket.ChannelPond;
import com.julia.tool.*;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.julia.model.vo.GameEntityVO;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 游戏 服务实现类
 * </p>
 *
 * @author chowel
 * @since 2024-10-03
 */
@Service
public class GameServiceImpl extends ServiceImpl<GameMapper, GameEntity> implements IGameService {
    @Resource
    RedisUtils redisUtils;

    @Resource
    IGameThirteenService thirteenService;

    @Resource
    IGameRoomService roomService;

    @Resource
    IPlayersService playersService;


    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Page<GameEntityVO> findForPage(QueryPagement queryPagement) {
        Page<GameEntity> p = new LambdaQueryChainWrapper<GameEntity>(getBaseMapper()).page(new Page<GameEntity>(queryPagement.getStartPage(),
                queryPagement.getPageSize()));
        Page<GameEntityVO> page = JuliaUtils.convertTo(new Page<GameEntityVO>(), p);
        page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new GameEntityVO(), e)).collect(Collectors.toList()));
        return page;
    }

    @Override
    public GameEntityVO findOneById(Long id) {
        GameEntity entity = getById(id);
        return JuliaUtils.convertTo(new GameEntityVO(), entity);
    }

    @Override
    public GameEntity findGameByRoom(int gameType, String roomIde) {
        return getOne(new QueryWrapper<GameEntity>()
                .eq("room_flag", roomIde)
                .eq("game_type", gameType)
                .eq("status", 1)
        );
    }

    @Override
    public GameEntity findGameByNo(String gameNo) {
        return getOne(new QueryWrapper<GameEntity>()
                .eq("game_no", gameNo)
        );
    }

    @Override
    public Boolean saveGameEntity(GameEntityVO vo) {
        return save(JuliaUtils.convertTo(new GameEntity(), vo));
    }

    @Override
    public Boolean alter(GameEntityVO vo) {
        return updateById(JuliaUtils.convertTo(new GameEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
        return removeById(id);
    }

    @SneakyThrows
    @Override
    public List<PokerMoldForFive> receive(ReceivePokerDto dto) {
        // TODO 校验是否下发的牌
        PlayerGameRo playerGame = (PlayerGameRo) redisUtils.get(RedisKeyEnum.PLAYERPOKERS.getKey() + dto.getPlayId() + "_" + dto.getGameIde());
        // 重复提交
        if (!ObjectUtils.isEmpty(playerGame.getMolds()) && playerGame.getMolds().size() > 0) {
            return playerGame.getMolds();
        }
        if (ObjectUtils.isEmpty(playerGame)) {
            throw new JuliaException("游戏系统异常");
        }
        List<Poker> prePokers = playerGame.getPrePokers();
        List<Poker> handPokers = dto.getPokers();

        prePokers.sort(Comparator.comparing(Poker::getId, Comparator.reverseOrder()));
        handPokers.sort(Comparator.comparing(Poker::getId, Comparator.reverseOrder()));

        if (prePokers.size() != handPokers.size()) {
            throw new JuliaException("游戏系统异常");
        }
        for (int i = 0; i < prePokers.size(); i++) {
            if (!prePokers.get(i).equals(handPokers.get(i))) {
                throw new JuliaException("游戏系统异常");
            }
        }
        // todo 定牌型

//        String pokerJson = mapper.writeValueAsString(handPokers);

//        scoreService.savePlayerPokers(dto.getGameIde(), dto.getPlayId(), pokerJson);

        handPokers.sort(Comparator.comparing(Poker::getPositionId));
        List<Poker> headerGear = handPokers.subList(0, 3);
        List<Poker> midGear = handPokers.subList(3, 8);
        List<Poker> floorGear = handPokers.subList(8, 13);

        PokerMoldForFive headerMold = PokerUtils.headerForThree(headerGear);
        PokerMoldForFive midMold = createMold(midGear);
        PokerMoldForFive floorMold = createMold(floorGear);

        List<PokerMoldForFive> molds = new ArrayList<>();
        molds.add(headerMold);
        molds.add(midMold);
        molds.add(floorMold);

        playerGame.setMolds(molds);
        String playerPokersKey = RedisKeyEnum.PLAYERPOKERS.getKey() + playerGame.getPlayerId() + "_" + playerGame.getGameNo();
        redisUtils.set(playerPokersKey, playerGame);


        GameThirteenEntity thirteen = thirteenService.findOneByGameNoWhitPlayerId(playerGame.getPlayerId(), playerGame.getGameNo());
        if (ObjectUtils.isEmpty(thirteen)) {
            throw new JuliaException("游戏系统异常");
        }
        thirteen.setGameNo(playerGame.getGameNo());
        thirteen.setPlayerId(playerGame.getPlayerId());

        assert headerMold != null;
        thirteen.setHeadgear(headerMold.getCname());
        List<String> headerIds =
                headerGear.stream().map((poker) -> String.valueOf(poker.getId())).collect(Collectors.toList());
        thirteen.setHeadgearPokers(String.join(",", headerIds));

        thirteen.setMidgear(midMold.getCname());
        List<String> midIds = midGear.stream().map(poker -> String.valueOf(poker.getId())).collect(Collectors.toList());
        thirteen.setMidgearPoker(String.join(",", midIds));

        thirteen.setBasegear(floorMold.getCname());
        List<String> baseIds =
                floorGear.stream().map(poker -> String.valueOf(poker.getId())).collect(Collectors.toList());
        thirteen.setBasegearPoker(String.join(",", baseIds));

        thirteenService.updateById(thirteen);

//        String RECEIVESKEY = RedisKeyEnum.RECEIVES.getKey() + "_" + playerGame.getGameNo();
//        redisUtils.incr(RECEIVESKEY, 1);
//        int gameReceive = (int) redisUtils.get(RECEIVESKEY);
//        GameRoomEntityVO room = roomService.findOneByFlag(dto.getRoomIde());
//        if (gameReceive == room.getPlayers()) {
//            countScore(dto.getGameIde());
//        }
        return molds;
    }

    @SneakyThrows
    @Override
//    @Async("countScoreExecutor")
    public void countScore(String gameNo, String roomIde) {
//        GameScoreEntity gameScore = scoreService.findOneByGameNo(gameNo);
        List<GameThirteenEntity> games = thirteenService.findByGameNo(gameNo);


        List<Integer> play1Scores = Arrays.asList(0, 0, 0);
        List<Integer> play2Scores = Arrays.asList(0, 0, 0);
        List<Integer> play3Scores = Arrays.asList(0, 0, 0);
        List<Integer> play4Scores = Arrays.asList(0, 0, 0);

        PlayerGameRo playerI = null;
        List<PokerMoldForFive> playerIMolds = null;
        if (!ObjectUtils.isEmpty(games.get(0))) {
            playerI =
                    (PlayerGameRo) redisUtils.get(RedisKeyEnum.PLAYERPOKERS.getKey() + games.get(0).getPlayerId() + "_" + gameNo);
            playerIMolds = playerI.getMolds();

        }
        PlayerGameRo playerII = null;
        List<PokerMoldForFive> playerIIMolds = null;
        if (!ObjectUtils.isEmpty(games.get(1))) {
            playerII =
                    (PlayerGameRo) redisUtils.get(RedisKeyEnum.PLAYERPOKERS.getKey() + games.get(1).getPlayerId() + "_" + gameNo);
            playerIIMolds = playerII.getMolds();
        }
        PlayerGameRo playerIII = null;
        List<PokerMoldForFive> playerIIIMolds = null;
        if (games.size() > 2) {
            playerIII =
                    (PlayerGameRo) redisUtils.get(RedisKeyEnum.PLAYERPOKERS.getKey() + games.get(2).getPlayerId() + "_" + gameNo);
            playerIIIMolds = playerIII.getMolds();
        }
        PlayerGameRo playerIIII = null;
        List<PokerMoldForFive> playerVIMolds = null;
        if (games.size() > 3) {
            playerIIII = (PlayerGameRo) redisUtils.get(RedisKeyEnum.PLAYERPOKERS.getKey() + games.get(2).getPlayerId() + "_" + gameNo);
            playerVIMolds = playerIIII.getMolds();
        }
        // 玩家1 vs 玩家2
        if (!ObjectUtils.isEmpty(playerIMolds) && !ObjectUtils.isEmpty(playerIIMolds)) {
            List<Integer> sc = comparisonFiveMold(playerIMolds, playerIIMolds);
            play1Scores.set(0, sc.get(0));
            play1Scores.set(1, sc.get(1));
            play1Scores.set(2, sc.get(2));
            play2Scores.set(0, sc.get(3));
            play2Scores.set(1, sc.get(4));
            play2Scores.set(2, sc.get(5));
        }

        // 玩家1 vs 玩家3
        if (!ObjectUtils.isEmpty(playerIMolds) && !ObjectUtils.isEmpty(playerIIIMolds)) {
            List<Integer> sc = comparisonFiveMold(playerIMolds, playerIIIMolds);
            play1Scores.set(0, play1Scores.get(0) + sc.get(0));
            play1Scores.set(1, play1Scores.get(1) + sc.get(1));
            play1Scores.set(2, play1Scores.get(2) + sc.get(2));
            play3Scores.set(0, play3Scores.get(0) + sc.get(3));
            play3Scores.set(1, play3Scores.get(1) + sc.get(4));
            play3Scores.set(2, play3Scores.get(2) + sc.get(5));
        }

        // 玩家1 vs 玩家4
        if (!ObjectUtils.isEmpty(playerIMolds) && !ObjectUtils.isEmpty(playerVIMolds)) {
            List<Integer> sc = comparisonFiveMold(playerIMolds, playerVIMolds);
            play1Scores.set(0, play1Scores.get(0) + sc.get(0));
            play1Scores.set(1, play1Scores.get(1) + sc.get(1));
            play1Scores.set(2, play1Scores.get(2) + sc.get(2));
            play4Scores.set(0, play4Scores.get(0) + sc.get(3));
            play4Scores.set(1, play4Scores.get(1) + sc.get(4));
            play4Scores.set(2, play4Scores.get(2) + sc.get(5));
        }

        // 玩家2 vs 玩家3
        if (!ObjectUtils.isEmpty(playerIIMolds) && !ObjectUtils.isEmpty(playerIIIMolds)) {
            List<Integer> sc = comparisonFiveMold(playerIIMolds, playerIIIMolds);
            play2Scores.set(0, play2Scores.get(0) + sc.get(0));
            play2Scores.set(1, play2Scores.get(1) + sc.get(1));
            play2Scores.set(2, play2Scores.get(2) + sc.get(2));
            play3Scores.set(0, play3Scores.get(0) + sc.get(3));
            play3Scores.set(1, play3Scores.get(1) + sc.get(4));
            play3Scores.set(2, play3Scores.get(2) + sc.get(5));
        }

        // 玩家2 vs 玩家4
        if (!ObjectUtils.isEmpty(playerIIMolds) && !ObjectUtils.isEmpty(playerVIMolds)) {
            List<Integer> sc = comparisonFiveMold(playerIIMolds, playerVIMolds);
            play2Scores.set(0, play2Scores.get(0) + sc.get(0));
            play2Scores.set(1, play2Scores.get(1) + sc.get(1));
            play2Scores.set(2, play2Scores.get(2) + sc.get(2));
            play4Scores.set(0, play4Scores.get(0) + sc.get(3));
            play4Scores.set(1, play4Scores.get(1) + sc.get(4));
            play4Scores.set(2, play4Scores.get(2) + sc.get(5));
        }

        // 玩家3 vs 玩家4
        if (!ObjectUtils.isEmpty(playerIIIMolds) && !ObjectUtils.isEmpty(playerVIMolds)) {
            List<Integer> sc = comparisonFiveMold(playerIIIMolds, playerVIMolds);
            play3Scores.set(0, play3Scores.get(0) + sc.get(0));
            play3Scores.set(1, play3Scores.get(1) + sc.get(1));
            play3Scores.set(2, play3Scores.get(2) + sc.get(2));
            play4Scores.set(0, play4Scores.get(0) + sc.get(3));
            play4Scores.set(1, play4Scores.get(1) + sc.get(4));
            play4Scores.set(2, play4Scores.get(2) + sc.get(5));
        }

        GameRoomEntityVO room = roomService.findOneByFlag(roomIde);

        if (!ObjectUtils.isEmpty(games.get(0))) {
            GameThirteenEntity o = games.get(0);
            o.setHeadScore(play1Scores.get(0));
            o.setMidScore(play1Scores.get(1));
            o.setBaseScore(play1Scores.get(2));
            o.setTotal(play1Scores.get(0) + play1Scores.get(1) + play1Scores.get(2));
            thirteenService.updateById(o);
            setPlayerCoin(o.getPlayerId(), o.getTotal() * room.getAgame());
        }

        if (!ObjectUtils.isEmpty(games.get(1))) {
            GameThirteenEntity o = games.get(1);
            o.setHeadScore(play2Scores.get(0));
            o.setMidScore(play2Scores.get(1));
            o.setBaseScore(play2Scores.get(2));
            o.setTotal(play2Scores.get(0) + play2Scores.get(1) + play2Scores.get(2));
            thirteenService.updateById(o);
            setPlayerCoin(o.getPlayerId(), o.getTotal() * room.getAgame());
        }

        if (games.size() > 2) {
            GameThirteenEntity o = games.get(2);
            o.setHeadScore(play3Scores.get(0));
            o.setMidScore(play3Scores.get(1));
            o.setBaseScore(play3Scores.get(2));
            o.setTotal(play3Scores.get(0) + play3Scores.get(1) + play3Scores.get(2));
            thirteenService.updateById(o);
            setPlayerCoin(o.getPlayerId(), o.getTotal() * room.getAgame());
        }

        if (games.size() > 3) {
            GameThirteenEntity o = games.get(3);
            o.setHeadScore(play4Scores.get(0));
            o.setMidScore(play4Scores.get(1));
            o.setBaseScore(play4Scores.get(2));
            o.setTotal(play4Scores.get(0) + play4Scores.get(1) + play4Scores.get(2));
            thirteenService.updateById(o);
            setPlayerCoin(o.getPlayerId(), o.getTotal() * room.getAgame());
        }


        assert playerI != null;
        // 结束当前牌局
        this.lambdaUpdate()
                .eq(GameEntity::getGameNo, playerI.getGameNo())
                .eq(GameEntity::getRoomFlag, playerI.getRoomIde())
                .eq(GameEntity::getGameType, playerI.getGameType())
                .set(GameEntity::getStatus, 0).update();

        String notSendPokerKey =
                RedisKeyEnum.NOTSENDPOKER.getKey() + playerI.getGameType() + "_" + playerI.getRoomIde() + "_" + playerI.getGameNo();

        redisUtils.del(notSendPokerKey);


        sendGameResult(playerI.getGameNo(), playerI.getGameType(), playerI.getRoomIde());

    }

    @SneakyThrows
    @Override
    public void createThirteennGame(int gameType, String roomIde) {
        GameEntity game = new GameEntity();
        game.setGameNo(JuliaUtils.randomGameId());

        List<Poker> pokers = PokerUtils.shufflePoker();
        game.setPokers(mapper.writeValueAsString(pokers));
        game.setRoomFlag(roomIde);
        game.setGameType(gameType);
        game.setStatus(1);
        if (this.save(game)) {
            List<Poker> onePokers = new ArrayList<>();
            List<Poker> twoPokers = new ArrayList<>();
            List<Poker> threePokers = new ArrayList<>();
            List<Poker> fourPokers = new ArrayList<>();

            for (int j = 0; j < pokers.size(); j++) {
                if (j < 13) {
                    onePokers.add(pokers.get(j));
                }
                if (j > 12 && j < 26) {
                    twoPokers.add(pokers.get(j));
                }
                if (j > 25 && j < 39) {
                    threePokers.add(pokers.get(j));
                }
                if (j > 38 && j < 52) {
                    fourPokers.add(pokers.get(j));
                }
            }
            String key = RedisKeyEnum.NOTSENDPOKER.getKey() + gameType + "_" + roomIde + "_" + game.getGameNo();
            redisUtils.lSet(key, onePokers);
            redisUtils.lSet(key, twoPokers);
            redisUtils.lSet(key, threePokers);
            redisUtils.lSet(key, fourPokers);
        }
    }

    @Override
    public Boolean rePlayThirteennGame(int userId, int gameType, String roomIde) {
        GameEntity game = findGameByRoom(gameType, roomIde);
        if (ObjectUtils.isEmpty(game)) {
            createThirteennGame(gameType, roomIde);
        }
        return true;
    }

    private PokerMoldForFive createMold(List<Poker> l) {
        PokerMoldForFive mold = PokerUtils.generateMold(l);
        assert mold != null;
        if ("Kicker".equals(mold.getName())) {
            PokerMoldForFive straight = PokerUtils.checkStraight(l);
            if (ObjectUtils.isEmpty(straight)) {
                PokerMoldForFive flush = PokerUtils.checkFlush(l);
                if (ObjectUtils.isEmpty(flush)) {
                    return mold;
                } else {
                    return flush;
                }
            } else {
                return straight;
            }
        } else {
            return mold;
        }
    }

    private List<Integer> comparisonFiveMold(List<PokerMoldForFive> one, List<PokerMoldForFive> other) {
        int oneHeaderScores = 0;
        int otherHeaderScores = 0;
        int oneMidScores = 0;
        int otherMidScores = 0;
        int oneBaseScores = 0;
        int otherBaseScores = 0;
        for (int i = 0; i < 3; i++) {
            int oneScores = 0;
            int otherScores = 0;
            PokerMoldForFive t1 = one.get(i);
            PokerMoldForFive t2 = other.get(i);
            if (t1.getRange() > t2.getRange()) {
                int t1Score = t1.getScore();
                if (i == 1 && (t1.getRange() == 7 || t1.getRange() == 8 || t1.getRange() == 9)) {
                    t1Score = t1Score * 2;
                }
                oneScores = oneScores + t1Score;
                otherScores = otherScores - t1Score;
            }
            if (t1.getRange() < t2.getRange()) {
                int t2Score = t2.getScore();
                if (i == 1 && (t2.getRange() == 7 || t2.getRange() == 8 || t2.getRange() == 9)) {
                    t2Score = t2Score * 2;
                }
                oneScores = oneScores - t2Score;
                otherScores = otherScores + t2Score;
            }

            if (t1.getRange().equals(t2.getRange())) {
                if (t1.getMax() > t2.getMax()) {
                    int t1Score = t1.getScore();
                    if (i == 1 && (t1.getRange() == 7 || t1.getRange() == 8 || t1.getRange() == 9)) {
                        t1Score = t1Score * 2;
                    }
                    oneScores = oneScores + t1Score;
                    otherScores = otherScores - t1Score;
                }
                if (t1.getMax() < t2.getMax()) {
                    int t2Score = t2.getScore();
                    if (i == 1 && (t2.getRange() == 7 || t2.getRange() == 8 || t2.getRange() == 9)) {
                        t2Score = t2Score * 2;
                    }
                    oneScores = oneScores - t2Score;
                    otherScores = otherScores + t2Score;
                }

                if (t1.getMax().equals(t2.getMax())) {
                    if (t1.getSub() > t2.getSub()) {
                        int t1Score = t1.getScore();
                        if (i == 1 && (t1.getRange() == 7 || t1.getRange() == 8 || t1.getRange() == 9)) {
                            t1Score = t1Score * 2;
                        }
                        oneScores = oneScores + t1Score;
                        otherScores = otherScores - t1Score;
                    }
                    if (t1.getSub() < t2.getSub()) {
                        int t2Score = t2.getScore();
                        if (i == 1 && (t2.getRange() == 7 || t2.getRange() == 8 || t2.getRange() == 9)) {
                            t2Score = t2Score * 2;
                        }
                        oneScores = oneScores - t2Score;
                        otherScores = otherScores + t2Score;
                    }

                    if (t1.getSub().equals(t2.getSub())) {
                        if (t1.getMid() > t2.getMid()) {
                            int t1Score = t1.getScore();
                            if (i == 1 && (t1.getRange() == 7 || t1.getRange() == 8 || t1.getRange() == 9)) {
                                t1Score = t1Score * 2;
                            }
                            oneScores = oneScores + t1Score;
                            otherScores = otherScores - t1Score;
                        }
                        if (t1.getMid() < t2.getMid()) {
                            int t2Score = t2.getScore();
                            if (i == 1 && (t2.getRange() == 7 || t2.getRange() == 8 || t2.getRange() == 9)) {
                                t2Score = t2Score * 2;
                            }
                            oneScores = oneScores - t2Score;
                            otherScores = otherScores + t2Score;
                        }

                        if (t1.getMid().equals(t2.getMid())) {
                            if (t1.getLittle() > t2.getLittle()) {
                                int t1Score = t1.getScore();
                                if (i == 1 && (t1.getRange() == 7 || t1.getRange() == 8 || t1.getRange() == 9)) {
                                    t1Score = t1Score * 2;
                                }
                                oneScores = oneScores + t1Score;
                                otherScores = otherScores - t1Score;
                            }
                            if (t1.getLittle() < t2.getLittle()) {
                                int t2Score = t2.getScore();
                                if (i == 1 && (t2.getRange() == 7 || t2.getRange() == 8 || t2.getRange() == 9)) {
                                    t2Score = t2Score * 2;
                                }
                                oneScores = oneScores - t2Score;
                                otherScores = otherScores + t2Score;
                            }

                            if (t1.getLittle().equals(t2.getLittle())) {
                                if (t1.getMinimum() > t2.getMinimum()) {
                                    int t1Score = t1.getScore();
                                    if (i == 1 && (t1.getRange() == 7 || t1.getRange() == 8 || t1.getRange() == 9)) {
                                        t1Score = t1Score * 2;
                                    }
                                    oneScores = oneScores + t1Score;
                                    otherScores = otherScores - t1Score;
                                }
                                if (t1.getMinimum() < t2.getMinimum()) {
                                    int t2Score = t2.getScore();
                                    if (i == 1 && (t2.getRange() == 7 || t2.getRange() == 8 || t2.getRange() == 9)) {
                                        t2Score = t2Score * 2;
                                    }
                                    oneScores = oneScores - t2Score;
                                    otherScores = otherScores + t2Score;
                                }

                                if (t1.getMinimum().equals(t2.getMinimum())) {
                                    oneScores = 0;
                                    otherScores = 0;
                                }
                            }
                        }
                    }
                }

            }

            if (i == 0) {
                oneHeaderScores = oneScores;
                otherHeaderScores = otherScores;
            }
            if (i == 1) {
                oneMidScores = oneScores;
                otherMidScores = otherScores;
            }

            if (i == 2) {
                oneBaseScores = oneScores;
                otherBaseScores = otherScores;
            }
        }
        return Arrays.asList(oneHeaderScores, oneMidScores, oneBaseScores, otherHeaderScores, otherMidScores, otherBaseScores);
    }

    @SneakyThrows
    private void sendGameResult(String gameNo, int gameType, String roomIde) {
        List<GameThirteenEntity> games = thirteenService.findByGameNo(gameNo);

        List<GameThirteenEntityVO> result = games.stream().map(
                (entity -> {
                    GameThirteenEntityVO t = JuliaUtils.convertTo(new GameThirteenEntityVO(), entity);
                    t.setHeadPokers(PokerUtils.findPokersByid(t.getHeadgearPokers()));
                    t.setMidPokers(PokerUtils.findPokersByid(t.getMidgearPoker()));
                    t.setBasePokers(PokerUtils.findPokersByid(t.getBasegearPoker()));
                    return t;
                })
        ).collect(Collectors.toList());

        String key = RedisKeyEnum.ROOMPLAYERS.getKey() + gameType + "_" + roomIde;
        Set<Object> players = redisUtils.sGet(key);
        for (Object element : players) {
            PlayerRo t = (PlayerRo) element;

//            redisUtils.del(RedisKeyEnum.ALIVEGAME.getKey() + t.getPlayId());
            AliveGameRo aliveGameRo=  (AliveGameRo) redisUtils.get(RedisKeyEnum.ALIVEGAME.getKey() + t.getPlayId());
            if(!ObjectUtils.isEmpty(aliveGameRo)){
                aliveGameRo.setGameIde(null);
                redisUtils.set(RedisKeyEnum.ALIVEGAME.getKey() + t.getPlayId(), aliveGameRo);
            }
            Channel channel = ChannelPond.findChannel(String.valueOf(t.getPlayId()));
            if (!ObjectUtils.isEmpty(channel)) {
                WebSocketMsgBO sendMsg = new WebSocketMsgBO();
                sendMsg.setSub("GAMERESULT");
                sendMsg.setData(result);
                channel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(sendMsg)));
            }
        }
    }

    private void setPlayerCoin(Long playerId, int scoreCoin) {
        PlayersEntity player = playersService.getById(playerId);
        if (!ObjectUtils.isEmpty(player)) {
            player.setCoin(player.getCoin() - scoreCoin);
            playersService.updateById(player);
        }
    }


}

