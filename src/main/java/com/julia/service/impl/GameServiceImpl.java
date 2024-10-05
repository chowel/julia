package com.julia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.GameEntity;
import com.julia.mapper.GameMapper;
import com.julia.service.IGameService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.julia.model.vo.GameEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;

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
}

