package com.julia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.GameRoomEntity;
import com.julia.entity.GameThirteenEntity;
import com.julia.mapper.GameThirteenMapper;
import com.julia.service.IGameThirteenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.julia.model.vo.GameThirteenEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 13游戏详情 服务实现类
 * </p>
 *
 * @author chowel
 * @since 2024-10-10
 */
@Service
public class GameThirteenServiceImpl extends ServiceImpl<GameThirteenMapper, GameThirteenEntity> implements IGameThirteenService {
    @Override
    public Page<GameThirteenEntityVO> findForPage(QueryPagement queryPagement) {
        Page<GameThirteenEntity> p = new LambdaQueryChainWrapper<GameThirteenEntity>(getBaseMapper()).page(new Page<GameThirteenEntity>(queryPagement.getStartPage(),
                queryPagement.getPageSize()));
        Page<GameThirteenEntityVO> page = JuliaUtils.convertTo(new Page<GameThirteenEntityVO>(), p);
        page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new GameThirteenEntityVO(), e)).collect(Collectors.toList()));
        return page;
    }

    @Override
    public GameThirteenEntityVO findOneById(Long id) {
        GameThirteenEntity entity = getById(id);
        return JuliaUtils.convertTo(new GameThirteenEntityVO(), entity);
    }

    @Override
    public Boolean saveGameThirteenEntity(GameThirteenEntityVO vo) {
        GameThirteenEntity entity = getOne(new QueryWrapper<GameThirteenEntity>()
                .eq("player_id", vo.getPlayerId())
                .eq("game_id", vo.getGameId()));
        if (!ObjectUtils.isEmpty(entity)) {
            return true;
        }
        return save(JuliaUtils.convertTo(new GameThirteenEntity(), vo));
    }

    @Override
    public Boolean alter(GameThirteenEntityVO vo) {
        return updateById(JuliaUtils.convertTo(new GameThirteenEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
        return removeById(id);
    }

    @Override
    public List<GameThirteenEntity> findByGameNo(String gameNo) {
        return this.list(new QueryWrapper<GameThirteenEntity>().eq("game_no",gameNo));
    }

    @Override
    public GameThirteenEntity findOneByGameNoWhitPlayerId(Long playerId, String gameNo) {
        return getOne(new QueryWrapper<GameThirteenEntity>().eq("game_no",gameNo).eq("player_id",playerId));
    }
}

