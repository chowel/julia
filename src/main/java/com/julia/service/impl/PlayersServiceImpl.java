package com.julia.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.PlayersEntity;
import com.julia.mapper.PlayersMapper;
import com.julia.service.IPlayersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.julia.model.vo.PlayersEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import java.util.stream.Collectors;
/**
* <p>
    * 用户表 服务实现类
    * </p>
*
* @author chowel
* @since 2025-09-28
*/
@Service
public class PlayersServiceImpl extends ServiceImpl<PlayersMapper, PlayersEntity> implements IPlayersService {
    @Override
    public Page<PlayersEntityVO> findForPage(QueryPagement queryPagement) {
        Page<PlayersEntity> p = new LambdaQueryChainWrapper<PlayersEntity>(getBaseMapper()).page(new Page<PlayersEntity>(queryPagement.getStartPage(),
        queryPagement.getPageSize()));
        Page<PlayersEntityVO> page = JuliaUtils.convertTo(new Page<PlayersEntityVO>(), p);
                page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new PlayersEntityVO(), e)).collect(Collectors.toList()));
                return page;
    }

    @Override
    public PlayersEntityVO findOneById(Long id) {
            PlayersEntity entity = getById(id);
            return JuliaUtils.convertTo(new PlayersEntityVO(), entity);
    }

    @Override
    public Boolean savePlayersEntity(PlayersEntityVO vo) {
            return save(JuliaUtils.convertTo(new PlayersEntity(), vo));
    }

    @Override
    public Boolean alter(PlayersEntityVO vo) {
            return updateById(JuliaUtils.convertTo(new PlayersEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
            return removeById(id);
    }
}

