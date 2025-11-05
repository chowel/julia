package com.julia.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.config.PlayerToken;
import com.julia.entity.MinaPlayerEntity;
import com.julia.mapper.MinaPlayerMapper;
import com.julia.model.dto.LoginDto;
import com.julia.service.IMinaPlayerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.julia.tool.JuliaException;
import org.springframework.stereotype.Service;
import com.julia.model.vo.MinaPlayerEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import org.springframework.util.ObjectUtils;

import java.util.stream.Collectors;
/**
* <p>
    * 玩家 服务实现类
    * </p>
*
* @author chowel
* @since 2025-10-17
*/
@Service
public class MinaPlayerServiceImpl extends ServiceImpl<MinaPlayerMapper, MinaPlayerEntity> implements IMinaPlayerService {
    @Override
    public Page<MinaPlayerEntityVO> findForPage(QueryPagement queryPagement) {
        Page<MinaPlayerEntity> p = new LambdaQueryChainWrapper<MinaPlayerEntity>(getBaseMapper()).page(new Page<MinaPlayerEntity>(queryPagement.getStartPage(),
        queryPagement.getPageSize()));
        Page<MinaPlayerEntityVO> page = JuliaUtils.convertTo(new Page<MinaPlayerEntityVO>(), p);
                page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new MinaPlayerEntityVO(), e)).collect(Collectors.toList()));
                return page;
    }

    @Override
    public MinaPlayerEntityVO findOneById(Long id) {
            MinaPlayerEntity entity = getById(id);
            return JuliaUtils.convertTo(new MinaPlayerEntityVO(), entity);
    }

    @Override
    public Boolean saveMinaPlayerEntity(MinaPlayerEntityVO vo) {
            return save(JuliaUtils.convertTo(new MinaPlayerEntity(), vo));
    }

    @Override
    public Boolean alter(MinaPlayerEntityVO vo) {
            return updateById(JuliaUtils.convertTo(new MinaPlayerEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
            return removeById(id);
    }

    @Override
    public Boolean updateCoinSpend(Long playerId, Long coin,Long spend) {
        return this.lambdaUpdate()
                .eq(MinaPlayerEntity::getUserId,playerId)
                .set(MinaPlayerEntity::getCoin,coin)
                .set(MinaPlayerEntity::getSpend,spend).update();
    }

    @Override
    public MinaPlayerEntityVO login(LoginDto dto) {
        MinaPlayerEntity player = lambdaQuery().eq(MinaPlayerEntity::getLoginName,dto.getName()).one();
        if (ObjectUtils.isEmpty(player)) {
            throw new JuliaException("用户不存在");
        }

        if (player.getStatus() == 0) {
            throw new JuliaException("用户禁用");
        }

        if (!BCrypt.checkpw(dto.getPassword(), player.getPassword())) {
            throw new JuliaException("密码错误");
        }
        MinaPlayerEntityVO vo = JuliaUtils.convertTo(new MinaPlayerEntityVO(),player);
        PlayerToken.login(vo.getUserId());
        vo.setToken(PlayerToken.getTokenValue());
        vo.setPassword("******");
        return vo;
    }
}

