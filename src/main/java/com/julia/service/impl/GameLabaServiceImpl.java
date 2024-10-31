package com.julia.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.GameLabaEntity;
import com.julia.mapper.GameLabaMapper;
import com.julia.service.IGameLabaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.julia.model.vo.GameLabaEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import java.util.stream.Collectors;
/**
* <p>
    * laba游戏详情 服务实现类
    * </p>
*
* @author chowel
* @since 2024-10-31
*/
@Service
public class GameLabaServiceImpl extends ServiceImpl<GameLabaMapper, GameLabaEntity> implements IGameLabaService {
    @Override
    public Page<GameLabaEntityVO> findForPage(QueryPagement queryPagement) {
        Page<GameLabaEntity> p = new LambdaQueryChainWrapper<GameLabaEntity>(getBaseMapper()).page(new Page<GameLabaEntity>(queryPagement.getStartPage(),
        queryPagement.getPageSize()));
        Page<GameLabaEntityVO> page = JuliaUtils.convertTo(new Page<GameLabaEntityVO>(), p);
                page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new GameLabaEntityVO(), e)).collect(Collectors.toList()));
                return page;
    }

    @Override
    public GameLabaEntityVO findOneById(Long id) {
            GameLabaEntity entity = getById(id);
            return JuliaUtils.convertTo(new GameLabaEntityVO(), entity);
    }

    @Override
    public Boolean saveGameLabaEntity(GameLabaEntityVO vo) {
            return save(JuliaUtils.convertTo(new GameLabaEntity(), vo));
    }

    @Override
    public Boolean alter(GameLabaEntityVO vo) {
            return updateById(JuliaUtils.convertTo(new GameLabaEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
            return removeById(id);
    }
}

