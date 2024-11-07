package com.julia.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.PlayersEntity;
import com.julia.entity.YaoEntity;
import com.julia.enums.RedisKeyEnum;
import com.julia.mapper.PlayersMapper;
import com.julia.model.dto.LoginDto;
import com.julia.model.vo.YaoEntityVO;
import com.julia.service.IPlayersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.julia.tool.*;
import org.springframework.stereotype.Service;
import com.julia.model.vo.PlayersEntityVO;
import com.julia.model.QueryPagement;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author chowel
 * @since 2024-09-24
 */
@Service
public class PlayersServiceImpl extends ServiceImpl<PlayersMapper, PlayersEntity> implements IPlayersService {


    @Resource
    RedisUtils redisUtils;

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
        PlayersEntity entity = (PlayersEntity) redisUtils.get(RedisKeyEnum.PLAYERCACHE.getKey() + id);
        if (ObjectUtils.isEmpty(entity)) {
            entity = getById(id);
            if (ObjectUtils.isEmpty(entity)) {
                return null;
            }
        }
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
    public Boolean alterOne(PlayersEntity entity) {
        redisUtils.del(RedisKeyEnum.PLAYERCACHE.getKey() + entity.getPlayId());
        return updateById(entity);
    }

    @Override
    public Boolean remove(Long id) {
        return removeById(id);
    }

    @Override
    public PlayersEntityVO login(LoginDto dto) {
        PlayersEntity player = this.getOne(new QueryWrapper<PlayersEntity>().eq("login_name", dto.getName()));

        if (ObjectUtils.isEmpty(player)) {
            throw new JuliaException("用户不存在");
        }

        if (player.getStatus() == 2) {
            throw new JuliaException("用户禁用");
        }

        if (!BCrypt.checkpw(dto.getPassword(), player.getPassword())) {
            throw new JuliaException("密码错误");
        }

        PlayersEntityVO vo = JuliaUtils.convertTo(new PlayersEntityVO(), player);

        PlayerToken.login(player.getPlayId());
        vo.setToken(PlayerToken.getTokenValue());
        vo.setPassword("******");
        redisUtils.set(RedisKeyEnum.PLAYERCACHE.getKey() + player.getPlayId(), player, 24 * 3600);

        return vo;
    }
}

