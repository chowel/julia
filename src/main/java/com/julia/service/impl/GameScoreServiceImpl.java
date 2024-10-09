package com.julia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.GameScoreEntity;
import com.julia.mapper.GameScoreMapper;
import com.julia.service.IGameScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.julia.tool.JuliaException;
import org.springframework.stereotype.Service;
import com.julia.model.vo.GameScoreEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import org.springframework.util.ObjectUtils;

import java.util.jar.JarException;
import java.util.stream.Collectors;

/**
 * <p>
 * 游戏得分 服务实现类
 * </p>
 *
 * @author chowel
 * @since 2024-10-03
 */
@Service
public class GameScoreServiceImpl extends ServiceImpl<GameScoreMapper, GameScoreEntity> implements IGameScoreService {
    @Override
    public Page<GameScoreEntityVO> findForPage(QueryPagement queryPagement) {
        Page<GameScoreEntity> p = new LambdaQueryChainWrapper<GameScoreEntity>(getBaseMapper()).page(new Page<GameScoreEntity>(queryPagement.getStartPage(),
                queryPagement.getPageSize()));
        Page<GameScoreEntityVO> page = JuliaUtils.convertTo(new Page<GameScoreEntityVO>(), p);
        page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new GameScoreEntityVO(), e)).collect(Collectors.toList()));
        return page;
    }

    @Override
    public GameScoreEntityVO findOneById(Long id) {
        GameScoreEntity entity = getById(id);
        return JuliaUtils.convertTo(new GameScoreEntityVO(), entity);
    }

    @Override
    public GameScoreEntity findOneByGameNo(String gameNo) {
        return getOne(new QueryWrapper<GameScoreEntity>().eq("game_no", gameNo));
    }

    @Override
    public Boolean saveGameScoreEntity(GameScoreEntityVO vo) {
        return save(JuliaUtils.convertTo(new GameScoreEntity(), vo));
    }

    @Override
    public Boolean alter(GameScoreEntityVO vo) {
        return updateById(JuliaUtils.convertTo(new GameScoreEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
        return removeById(id);
    }

    @Override
    public Boolean saveGamePlayer(String gameNo, Integer userId, Long gameId) {
        GameScoreEntity score = this.getOne(new QueryWrapper<GameScoreEntity>().eq("game_no", gameNo).eq("game_id", gameId));
        if (ObjectUtils.isEmpty(score)) {
            score = new GameScoreEntity();
            score.setGameId(gameId);
            score.setGameNo(gameNo);
            score.setPlayerIId(userId);
            return save(score);
        }
        if(score.getPlayerIiId()==0){
            score.setPlayerIiId(userId);
            return updateById(score);
        }

        if(score.getPlayerIiiId()==0){
            score.setPlayerIiiId(userId);
            return updateById(score);
        }

        if(score.getPlayerIvId()==0){
            score.setPlayerIvId(userId);
            return updateById(score);
        }
        return false;
    }

    @Override
    public Boolean savePlayerPokers(String gameNo, Integer userId, String pokers) {
        GameScoreEntity score = this.getOne(new QueryWrapper<GameScoreEntity>().eq("game_no", gameNo));
        if (ObjectUtils.isEmpty(score)) {
            throw new JuliaException("游戏系统异常");
        }
        if(score.getPlayerIId().equals(userId)){
            score.setPlayerIPoker(pokers);
            return updateById(score);
        }

        if(score.getPlayerIiId().equals(userId)){
            score.setPlayerIiPoker(pokers);
            return updateById(score);
        }

        if(score.getPlayerIiiId().equals(userId)){
            score.setPlayerIiiPoker(pokers);
            return updateById(score);
        }

        if(score.getPlayerIvId().equals(userId)){
            score.setPlayerIvPoker(pokers);
            return updateById(score);
        }

        return null;
    }
}

