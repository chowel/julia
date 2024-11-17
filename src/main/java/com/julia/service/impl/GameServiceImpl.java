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
import com.julia.model.vo.PlayersEntityVO;
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
    public Boolean overGameByRoomFlag(String flag, int gameType) {
        GameEntity game = this.getOne(new QueryWrapper<GameEntity>().eq("room_flag", flag).eq("status", 1));
        if (!ObjectUtils.isEmpty(game)) {
            game.setStatus(0);
            boolean updateRes = updateById(game);
            if (updateRes) {
                redisUtils.del(RedisKeyEnum.RECEIVES.getKey() + game.getGameNo());
                redisUtils.del(RedisKeyEnum.NOTSENDPOKER.getKey() + game.getGameType() + ":" + game.getRoomFlag() +
                        ":" + game.getGameNo());

                return updateRes;
            }
        }
        return false;
//        return lambdaUpdate().eq(GameEntity::getRoomFlag, flag).set(GameEntity::getStatus, 0).update();
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
        PlayerGameRo playerGame =
                (PlayerGameRo) redisUtils.get(RedisKeyEnum.PLAYERPOKERS.getKey() + dto.getPlayId() + ":" + dto.getGameIde());
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

        // 倒水I
        assert headerMold != null;
        if (headerMold.getRange() > midMold.getRange() || midMold.getRange() > floorMold.getRange()) {
            PokerMoldForFive daoBD = new PokerMoldForFive();
            daoBD.setName("Bad");
            daoBD.setCname("倒水");
            daoBD.setScore(0);
            daoBD.setSuit("NONE");
            daoBD.setRange(0);

            daoBD.setMax(5);
            daoBD.setSub(4);
            daoBD.setMid(3);
            daoBD.setLittle(2);
            daoBD.setMinimum(1);

            headerMold = daoBD;
            midMold = daoBD;
            floorMold = daoBD;
        }
        // 倒水II
        if (midMold.getRange() == floorMold.getRange() && midMold.getRange() > 0) {
            boolean checkDao = false;
            List<Integer> res = new ArrayList<>();
            List<Integer> mid = new ArrayList<>(List.of(midMold.getMax(), midMold.getSub(), midMold.getMid(),
                    midMold.getLittle(), midMold.getMinimum()));
            List<Integer> floor = new ArrayList<>(List.of(floorMold.getMax(), floorMold.getSub(), floorMold.getMid(),
                    floorMold.getLittle(), floorMold.getMinimum()));
            for (int i = 0; i < mid.size(); i++) {
                if (mid.get(i) > floor.get(i)) {
                    res.add(1);
                }
                if (mid.get(i).equals(floor.get(i))) {
                    res.add(0);
                }
                if (mid.get(i) < floor.get(i)) {
                    res.add(-1);
                }
            }

            for (int i = 0; i < res.size(); i++) {
                if (res.get(i) == -1) {
                    break;
                }
                if (res.get(i) == 1) {
                    checkDao = true;
                    break;
                }
            }

            if (checkDao) {
                PokerMoldForFive daoBD = new PokerMoldForFive();
                daoBD.setName("Bad");
                daoBD.setCname("倒水");
                daoBD.setScore(0);
                daoBD.setSuit("NONE");
                daoBD.setRange(0);

                daoBD.setMax(5);
                daoBD.setSub(4);
                daoBD.setMid(3);
                daoBD.setLittle(2);
                daoBD.setMinimum(1);

                headerMold = daoBD;
                midMold = daoBD;
                floorMold = daoBD;
            }
        }
        // 倒水II
        if (midMold.getRange() > 0
                && headerMold.getRange() == midMold.getRange()) {
            boolean checkDao = false;
            List<Integer> res = new ArrayList<>();
            List<Integer> head = new ArrayList<>(List.of(headerMold.getMax(), headerMold.getSub(), headerMold.getMid()));
            List<Integer> mid = new ArrayList<>(List.of(midMold.getMax(), midMold.getSub(), midMold.getMid()));
//            List<Integer> floor = new ArrayList<>(List.of(floorMold.getMax(), floorMold.getSub(), floorMold.getMid()));
            for (int i = 0; i < mid.size(); i++) {
                if (head.get(i) > mid.get(i)) {
                    res.add( 1);
                }
                if (head.get(i).equals(mid.get(i))) {
                    res.add( 0);
                }
                if (head.get(i) < mid.get(i)) {
                    res.add(-1);
                }
            }

            for (int i = 0; i < res.size(); i++) {
                if (res.get(i) == -1) {
                    break;
                }
                if (res.get(i) == 1) {
                    checkDao = true;
                    break;
                }
            }

            if (checkDao) {
                PokerMoldForFive daoBD = new PokerMoldForFive();
                daoBD.setName("Bad");
                daoBD.setCname("倒水");
                daoBD.setScore(0);
                daoBD.setSuit("NONE");
                daoBD.setRange(0);

                daoBD.setMax(5);
                daoBD.setSub(4);
                daoBD.setMid(3);
                daoBD.setLittle(2);
                daoBD.setMinimum(1);

                headerMold = daoBD;
                midMold = daoBD;
                floorMold = daoBD;
            }
        }


        // 六対半
        if (midMold.getRange() == 3 && floorMold.getRange() == 3) {
            Set<Integer> vals = new HashSet<>();
            headerGear.forEach(p -> vals.add(p.getValue()));
            midGear.forEach(p -> vals.add(p.getValue()));
            floorGear.forEach(p -> vals.add(p.getValue()));

            if (vals.size() == 7) {
                PokerMoldForFive shBD = new PokerMoldForFive();
                shBD.setName("SixPair");
                shBD.setCname("六対半");
                shBD.setScore(1);
                shBD.setSuit("NONE");
                shBD.setRange(10);

                shBD.setMax(14);
                shBD.setSub(13);
                shBD.setMid(12);
                shBD.setLittle(11);
                shBD.setMinimum(10);

                headerMold = shBD;
                midMold = shBD;
                floorMold = shBD;
            }
        }

        // 三顺
        if (midMold.getRange() == 5 && floorMold.getRange() == 5) {
            headerGear.sort(Comparator.comparing(Poker::getValue, Comparator.reverseOrder()));
            boolean IsStraight = false;


            int before = headerGear.get(1).getValue();
            int present = headerGear.get(2).getValue();

            if ((before - present) == 1) {
                if (headerGear.get(0).getValue() - headerGear.get(1).getValue() == 1) {
                    IsStraight = true;
                }
                if (headerGear.get(1).getValue() == 3 && headerGear.get(0).getValue() == 14) {
                    IsStraight = true;
                }
            }

            if (IsStraight) {
                PokerMoldForFive tsBD = new PokerMoldForFive();
                tsBD.setName("ThreeStraight");
                tsBD.setCname("三顺");
                tsBD.setScore(1);
                tsBD.setSuit("NONE");
                tsBD.setRange(10);

                tsBD.setMax(14);
                tsBD.setSub(13);
                tsBD.setMid(12);
                tsBD.setLittle(11);
                tsBD.setMinimum(10);

                headerMold = tsBD;
                midMold = tsBD;
                floorMold = tsBD;
                // 13顺
                Set<Integer> vals = new HashSet<>();
                headerGear.forEach(p -> vals.add(p.getValue()));
                if (vals.size() == 13) {
                    PokerMoldForFive ttfBDh = new PokerMoldForFive();
                    ttfBDh.setName("ThirteenStraight");
                    ttfBDh.setCname("十三顺");
                    ttfBDh.setScore(3);
                    ttfBDh.setRange(11);
                    ttfBDh.setSuit("NONE");

                    ttfBDh.setMax(14);
                    ttfBDh.setSub(13);
                    ttfBDh.setMid(12);
                    ttfBDh.setLittle(11);
                    ttfBDh.setMinimum(10);
                    headerMold = ttfBDh;


                    PokerMoldForFive ttfBD = new PokerMoldForFive();
                    ttfBD.setName("ThirteenStraight");
                    ttfBD.setCname("十三顺");
                    ttfBD.setScore(5);
                    ttfBD.setRange(11);
                    ttfBD.setSuit("NONE");

                    ttfBD.setMax(14);
                    ttfBD.setSub(13);
                    ttfBD.setMid(12);
                    ttfBD.setLittle(11);
                    ttfBD.setMinimum(10);

                    midMold = ttfBD;
                    floorMold = ttfBD;
                }

            }
        }

        // 三花
        if (midMold.getRange() == 6 && floorMold.getRange() == 6) {
            Set<String> suits = new HashSet<>();
            headerGear.forEach(p -> suits.add(p.getSuit()));
            if (suits.size() == 1) {
                PokerMoldForFive tfBD = new PokerMoldForFive();
                tfBD.setName("ThreeFlush");
                tfBD.setCname("三同花");
                tfBD.setScore(1);
                tfBD.setRange(10);

                tfBD.setMax(14);
                tfBD.setSub(13);
                tfBD.setMid(12);
                tfBD.setLittle(11);
                tfBD.setMinimum(10);

                headerMold = tfBD;
                midMold = tfBD;
                floorMold = tfBD;
            }
        }


        List<PokerMoldForFive> molds = new ArrayList<>();
        molds.add(headerMold);
        molds.add(midMold);
        molds.add(floorMold);

        playerGame.setMolds(molds);
        String PLAYERPOKERSKEY =
                RedisKeyEnum.PLAYERPOKERS.getKey() + playerGame.getPlayerId() + ":" + playerGame.getGameNo();
        redisUtils.set(PLAYERPOKERSKEY, playerGame);


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
                    (PlayerGameRo) redisUtils.get(RedisKeyEnum.PLAYERPOKERS.getKey() + games.get(0).getPlayerId() +
                            ":" + gameNo);
            playerIMolds = playerI.getMolds();

        }
        PlayerGameRo playerII = null;
        List<PokerMoldForFive> playerIIMolds = null;
        if (!ObjectUtils.isEmpty(games.get(1))) {
            playerII =
                    (PlayerGameRo) redisUtils.get(RedisKeyEnum.PLAYERPOKERS.getKey() + games.get(1).getPlayerId() +
                            ":" + gameNo);
            playerIIMolds = playerII.getMolds();
        }
        PlayerGameRo playerIII = null;
        List<PokerMoldForFive> playerIIIMolds = null;
        if (games.size() > 2) {
            playerIII =
                    (PlayerGameRo) redisUtils.get(RedisKeyEnum.PLAYERPOKERS.getKey() + games.get(2).getPlayerId() +
                            ":" + gameNo);
            playerIIIMolds = playerIII.getMolds();
        }
        PlayerGameRo playerIIII = null;
        List<PokerMoldForFive> playerVIMolds = null;
        if (games.size() > 3) {
            playerIIII =
                    (PlayerGameRo) redisUtils.get(RedisKeyEnum.PLAYERPOKERS.getKey() + games.get(2).getPlayerId() + ":" + gameNo);
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

        String NOTSENDPOKERKEY =
                RedisKeyEnum.NOTSENDPOKER.getKey() + playerI.getGameType() + ":" + playerI.getRoomIde() + ":" + playerI.getGameNo();

        redisUtils.del(NOTSENDPOKERKEY);

        // todo alive

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
            String NOTSENDPOKERKEY =
                    RedisKeyEnum.NOTSENDPOKER.getKey() + gameType + ":" + roomIde + ":" + game.getGameNo();
            redisUtils.lSet(NOTSENDPOKERKEY, onePokers);
            redisUtils.lSet(NOTSENDPOKERKEY, twoPokers);
            redisUtils.lSet(NOTSENDPOKERKEY, threePokers);
            redisUtils.lSet(NOTSENDPOKERKEY, fourPokers);
        }
    }

    @Override
    public PlayersEntityVO rePlayThirteennGame(int userId, int gameType, String roomIde) {
        PlayersEntityVO player = playersService.findOneById(Long.valueOf(userId));
        GameRoomEntityVO room = roomService.findOneByFlag(roomIde);
        if (room.getLeast() > player.getCoin()) {
            throw new JuliaException("房间最少金额不足");
        }
        GameEntity game = findGameByRoom(gameType, roomIde);
        if (ObjectUtils.isEmpty(game)) {
            createThirteennGame(gameType, roomIde);
        }
        player.setPassword("****");
        return player;
    }

    @SneakyThrows
    @Override
    public LabaGameRes startLabaGame(String roomIde) {
        GameEntity game = new GameEntity();
        game.setGameNo(JuliaUtils.randomGameId());
        game.setRoomFlag(roomIde);
        String LABAGAMEKEY = RedisKeyEnum.LABAGAME.getKey() + "_" + roomIde;
        // todo
        List<Integer> res = (List<Integer>) redisUtils.getLeftRemove(LABAGAMEKEY);
        if (ObjectUtils.isEmpty(res)) {
            res = PokerUtils.generaLabaRes();
            for (int i = 0; i < 10; i++) {
                redisUtils.lSet(LABAGAMEKEY, PokerUtils.generaLabaRes());
            }
            redisUtils.expire(LABAGAMEKEY, 3600 * 3);
        }

        game.setLabaRes(mapper.writeValueAsString(res));
        game.setStatus(1);
        if (this.save(game)) {
            LabaGameRes labaGameRes = new LabaGameRes();
            labaGameRes.setGameNo(game.getGameNo());
            labaGameRes.setRoomFlag(roomIde);
            labaGameRes.setRes(res);
            return labaGameRes;
        }
        return null;
    }

    @Override
    public Boolean computeCoinsByLaba(Long userId, int coins, String gameNo) {
        boolean updateGame = lambdaUpdate()
                .eq(GameEntity::getGameNo, gameNo)
                .set(GameEntity::getStatus, 0).update();
        if (updateGame) {
            setPlayerCoin(userId, coins);
            return true;
        }
        return false;
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

            if (t1.getRange() == t2.getRange()) {
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

                if (t1.getMax() == t2.getMax()) {
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

                    if (t1.getSub() == t2.getSub()) {
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

                        if (t1.getMid() == t2.getMid()) {
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

                            if (t1.getLittle() == t2.getLittle()) {
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

                                if (t1.getMinimum() == t2.getMinimum()) {
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

                    redisUtils.del(RedisKeyEnum.PLAYERPOKERS.getKey() + entity.getPlayerId() + ":" + entity.getGameNo());

                    GameThirteenEntityVO t = JuliaUtils.convertTo(new GameThirteenEntityVO(), entity);
                    t.setHeadPokers(PokerUtils.findPokersByid(t.getHeadgearPokers()));
                    t.setMidPokers(PokerUtils.findPokersByid(t.getMidgearPoker()));
                    t.setBasePokers(PokerUtils.findPokersByid(t.getBasegearPoker()));
                    return t;
                })
        ).collect(Collectors.toList());


        String ROOMPLAYERKEY = RedisKeyEnum.ROOMPLAYERS.getKey() + gameType + ":" + roomIde;
        Set<Object> players = redisUtils.sGet(ROOMPLAYERKEY);
        for (Object element : players) {
            PlayerRo t = (PlayerRo) element;

//            redisUtils.del(RedisKeyEnum.ALIVEGAME.getKey() + t.getPlayId());
            AliveGameRo aliveGameRo = (AliveGameRo) redisUtils.get(RedisKeyEnum.ALIVEGAME.getKey() + t.getPlayId());
            if (!ObjectUtils.isEmpty(aliveGameRo)) {
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
            playersService.alterOne(player);
        }
    }


}

