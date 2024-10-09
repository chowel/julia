package com.julia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.julia.entity.GameEntity;
import com.julia.entity.GameScoreEntity;
import com.julia.enums.RedisKeyEnum;
import com.julia.mapper.GameMapper;
import com.julia.model.PlayerGameRo;
import com.julia.model.dto.ReceivePokerDto;
import com.julia.model.vo.GameRoomEntityVO;
import com.julia.service.IGameRoomService;
import com.julia.service.IGameScoreService;
import com.julia.service.IGameService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.julia.tool.*;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.julia.model.vo.GameEntityVO;
import com.julia.model.QueryPagement;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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
    IGameScoreService scoreService;

    @Resource
    IGameRoomService roomService;


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

        String pokerJson = mapper.writeValueAsString(handPokers);

        scoreService.savePlayerPokers(dto.getGameIde(), dto.getPlayId(), pokerJson);

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
        String RECEIVESKEY = RedisKeyEnum.RECEIVES.getKey() + "_" + playerGame.getGameNo();
        redisUtils.incr(RECEIVESKEY, 1);
        int gameReceive = (int) redisUtils.get(RECEIVESKEY);
        GameRoomEntityVO room = roomService.findOneByFlag(dto.getRoomIde());
        if (gameReceive == room.getPlayers()) {
            countScore(dto.getGameIde());
        }
        return molds;
    }

    @Override
    @Async("countScoreExecutor")
    public void countScore(String gameNo) {
        GameScoreEntity gameScore = scoreService.findOneByGameNo(gameNo);
        assert gameScore != null;

        List<Integer> play1Scores = Arrays.asList(0, 0, 0);
        List<Integer> play2Scores = Arrays.asList(0, 0, 0);
        List<Integer> play3Scores = Arrays.asList(0, 0, 0);
        List<Integer> play4Scores = Arrays.asList(0, 0, 0);

        PlayerGameRo playerI;
        List<PokerMoldForFive> playerIMolds = null;
        if (gameScore.getPlayerIId() > 0) {
            playerI = (PlayerGameRo) redisUtils.get(RedisKeyEnum.PLAYERPOKERS.getKey() + gameScore.getPlayerIId() + "_" + gameNo);
            playerIMolds = playerI.getMolds();

        }
        PlayerGameRo playerII;
        List<PokerMoldForFive> playerIIMolds = null;
        if (gameScore.getPlayerIiId() > 0) {
            playerII = (PlayerGameRo) redisUtils.get(RedisKeyEnum.PLAYERPOKERS.getKey() + gameScore.getPlayerIiId() + "_" + gameNo);
            playerIIMolds = playerII.getMolds();
        }
        PlayerGameRo playerIII;
        List<PokerMoldForFive> playerIIIMolds = null;
        if (gameScore.getPlayerIiiId() > 0) {
            playerIII = (PlayerGameRo) redisUtils.get(RedisKeyEnum.PLAYERPOKERS.getKey() + gameScore.getPlayerIiiId() + "_" + gameNo);
            playerIIIMolds = playerIII.getMolds();
        }
        PlayerGameRo playerIIII;
        List<PokerMoldForFive> playerVIMolds = null;
        if (gameScore.getPlayerIvId() > 0) {
            playerIIII = (PlayerGameRo) redisUtils.get(RedisKeyEnum.PLAYERPOKERS.getKey() + gameScore.getPlayerIvId() + "_" + gameNo);
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

        if (gameScore.getPlayerIId() > 0) {
            gameScore.setPlayerIScore(play1Scores.get(0) + "$" + play1Scores.get(1) + "$" + play1Scores.get(2));
        }

        if (gameScore.getPlayerIiId() > 0) {
            gameScore.setPlayerIiScore(play2Scores.get(0) + "$" + play2Scores.get(1) + "$" + play2Scores.get(2));
        }

        if (gameScore.getPlayerIiiId() > 0) {
            gameScore.setPlayerIiiScore(play3Scores.get(0) + "$" + play3Scores.get(1) + "$" + play3Scores.get(2));
        }

        if (gameScore.getPlayerIvId() > 0) {
            gameScore.setPlayerIvScore(play4Scores.get(0) + "$" + play4Scores.get(1) + "$" + play4Scores.get(2));
        }

        scoreService.updateById(gameScore);
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
}

