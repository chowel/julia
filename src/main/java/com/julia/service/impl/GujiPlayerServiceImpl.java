package com.julia.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.config.PlayerToken;
import com.julia.entity.GujiPlayerEntity;
import com.julia.mapper.GujiPlayerMapper;
import com.julia.model.dto.LoginDto;
import com.julia.service.IGujiPlayerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.julia.tool.JuliaException;
import org.springframework.stereotype.Service;
import com.julia.model.vo.GujiPlayerEntityVO;
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
* @since 2025-12-17
*/
@Service
public class GujiPlayerServiceImpl extends ServiceImpl<GujiPlayerMapper, GujiPlayerEntity> implements IGujiPlayerService {
    @Override
    public Page<GujiPlayerEntityVO> findForPage(QueryPagement queryPagement) {
        Page<GujiPlayerEntity> p = new LambdaQueryChainWrapper<GujiPlayerEntity>(getBaseMapper()).page(new Page<GujiPlayerEntity>(queryPagement.getStartPage(),
        queryPagement.getPageSize()));
        Page<GujiPlayerEntityVO> page = JuliaUtils.convertTo(new Page<GujiPlayerEntityVO>(), p);
                page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new GujiPlayerEntityVO(), e)).collect(Collectors.toList()));
                return page;
    }

    @Override
    public GujiPlayerEntityVO findOneById(Long id) {
            GujiPlayerEntity entity = getById(id);
            return JuliaUtils.convertTo(new GujiPlayerEntityVO(), entity);
    }

    @Override
    public Boolean saveGujiPlayerEntity(GujiPlayerEntityVO vo) {
            return save(JuliaUtils.convertTo(new GujiPlayerEntity(), vo));
    }

    @Override
    public Boolean alter(GujiPlayerEntityVO vo) {
            return updateById(JuliaUtils.convertTo(new GujiPlayerEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
            return removeById(id);
    }

    @Override
    public GujiPlayerEntityVO login(LoginDto dto) {
        GujiPlayerEntity player = lambdaQuery().eq(GujiPlayerEntity::getLoginName,dto.getName()).one();
        if (ObjectUtils.isEmpty(player)) {
            throw new JuliaException("用户不存在");
        }

        if (player.getStatus() == 0) {
            throw new JuliaException("用户禁用");
        }

        if (!BCrypt.checkpw(dto.getPassword(), player.getPassword())) {
            throw new JuliaException("密码错误");
        }

        GujiPlayerEntityVO vo = JuliaUtils.convertTo(new GujiPlayerEntityVO(), player);
        PlayerToken.login(vo.getUserId());
        vo.setToken(PlayerToken.getTokenValue());
        vo.setPassword("******");
        return vo;
    }

    @Override
    public GujiPlayerEntityVO register(LoginDto dto) {
        GujiPlayerEntity player = new GujiPlayerEntity();
        player.setLoginName(dto.getName());
        player.setPassword( BCrypt.hashpw(dto.getPassword()));
        if (save(player)) {
            player = lambdaQuery().eq(GujiPlayerEntity::getLoginName,dto.getName()).one();
            GujiPlayerEntityVO vo = JuliaUtils.convertTo(new GujiPlayerEntityVO(), player);
            PlayerToken.login(vo.getUserId());
            vo.setToken(PlayerToken.getTokenValue());
            vo.setPassword("******");
            return vo;
        }
        return null;
    }
}

