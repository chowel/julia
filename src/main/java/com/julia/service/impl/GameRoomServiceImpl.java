package com.julia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.GameRoomEntity;
import com.julia.mapper.GameRoomMapper;
import com.julia.service.IGameRoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.julia.model.vo.GameRoomEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import java.util.stream.Collectors;
/**
* <p>
    * 游戏房间 服务实现类
    * </p>
*
* @author chowel
* @since 2024-09-28
*/
@Service
public class GameRoomServiceImpl extends ServiceImpl<GameRoomMapper, GameRoomEntity> implements IGameRoomService {
    @Override
    public Page<GameRoomEntityVO> findForPage(QueryPagement queryPagement) {
        Page<GameRoomEntity> p = new LambdaQueryChainWrapper<GameRoomEntity>(getBaseMapper()).page(new Page<GameRoomEntity>(queryPagement.getStartPage(),
        queryPagement.getPageSize()));
        Page<GameRoomEntityVO> page = JuliaUtils.convertTo(new Page<GameRoomEntityVO>(), p);
                page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new GameRoomEntityVO(), e)).collect(Collectors.toList()));
                return page;
    }

    @Override
    public GameRoomEntityVO findOneById(Long id) {
            GameRoomEntity entity = getById(id);
            return JuliaUtils.convertTo(new GameRoomEntityVO(), entity);
    }

    @Override
    public GameRoomEntityVO findOneByFlag(String flag) {
        GameRoomEntity entity =  this.getOne(new QueryWrapper<GameRoomEntity>().eq("flag",flag));
        return JuliaUtils.convertTo(new GameRoomEntityVO(), entity);
    }

    @Override
    public Boolean saveGameRoomEntity(GameRoomEntityVO vo) {
            return save(JuliaUtils.convertTo(new GameRoomEntity(), vo));
    }

    @Override
    public Boolean alter(GameRoomEntityVO vo) {
            return updateById(JuliaUtils.convertTo(new GameRoomEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
            return removeById(id);
    }
}

