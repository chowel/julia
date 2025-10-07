package com.julia.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.StackPlayerEntity;
import com.julia.enums.RedisKeyEnum;
import com.julia.mapper.StackPlayerMapper;
import com.julia.model.dto.LoginDto;
import com.julia.service.IStackPlayerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.julia.tool.JuliaException;
import com.julia.tool.PlayerToken;
import com.julia.tool.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.julia.model.vo.StackPlayerEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * <p>
 * 玩家 服务实现类
 * </p>
 *
 * @author chowel
 * @since 2025-03-09
 */
@Slf4j
@Service
public class StackPlayerServiceImpl extends ServiceImpl<StackPlayerMapper, StackPlayerEntity> implements IStackPlayerService {

    @Resource
    RedisUtils redisUtils;

    @Override
    public Page<StackPlayerEntityVO> findForPage(QueryPagement queryPagement) {
        Page<StackPlayerEntity> p = new LambdaQueryChainWrapper<StackPlayerEntity>(getBaseMapper()).page(new Page<StackPlayerEntity>(queryPagement.getStartPage(),
                queryPagement.getPageSize()));
        Page<StackPlayerEntityVO> page = JuliaUtils.convertTo(new Page<StackPlayerEntityVO>(), p);
        page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new StackPlayerEntityVO(), e)).collect(Collectors.toList()));
        return page;
    }

    @Override
    public StackPlayerEntityVO findOneById(Long id) {
        StackPlayerEntity entity = getById(id);
        return JuliaUtils.convertTo(new StackPlayerEntityVO(), entity);
    }

    @Override
    public StackPlayerEntityVO saveStackPlayerEntity(LoginDto dto) {
        StackPlayerEntity player = this.lambdaQuery().eq(StackPlayerEntity::getLoginName, dto.getName()).one();
        if (!ObjectUtils.isEmpty(player)) {
            throw new JuliaException("该用户已存在");
        }
        player = new StackPlayerEntity();
        player.setLoginName(dto.getName());
        player.setNickName(dto.getName());
        player.setPassword(BCrypt.hashpw(dto.getPassword()));
        if (save(player)){
            PlayerToken.login(player.getUserId());
            StackPlayerEntityVO vo = JuliaUtils.convertTo(new StackPlayerEntityVO(), player);
            vo.setToken(PlayerToken.getTokenValue());
            vo.setPassword("****");
            return vo;
        }
        return null;
    }

    @Override
    public Boolean alter(StackPlayerEntityVO vo) {
        return updateById(JuliaUtils.convertTo(new StackPlayerEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
        return removeById(id);
    }

    @Override
    public StackPlayerEntityVO playerLogin(LoginDto dto) {
        StackPlayerEntity player = lambdaQuery().eq(StackPlayerEntity::getLoginName, dto.getName()).one();
        if (ObjectUtils.isEmpty(player)) {
            throw new JuliaException("玩家不存在");
        }
        if (!BCrypt.checkpw(dto.getPassword(), player.getPassword())) {
            throw new JuliaException("密码错误");
        }
        if (player.getStatus() == 0) {
            throw new JuliaException("用户封禁");
        }

        PlayerToken.login(player.getUserId());
        StackPlayerEntityVO vo = JuliaUtils.convertTo(new StackPlayerEntityVO(), player);
        vo.setToken(PlayerToken.getTokenValue());
        vo.setPassword("****");
        return vo;
    }

    @Override
    public StackPlayerEntity findPlayerForPay() {

        if (redisUtils.hasKey(RedisKeyEnum.PLAYERSZET.getKey())
                && redisUtils.sGetSetSize(RedisKeyEnum.PLAYERSZET.getKey())>0) {
            return (StackPlayerEntity) redisUtils.randomPlayer(RedisKeyEnum.PLAYERSZET.getKey());
        } else {
            List<StackPlayerEntity> list = lambdaQuery().list();
            list.forEach(player -> {
                redisUtils.sSet(RedisKeyEnum.PLAYERSZET.getKey(), player);
            });
            redisUtils.expire(RedisKeyEnum.PLAYERSZET.getKey(), 3600 * 48);
            return list.get(0);
        }

    }

    @Override
    public StackPlayerEntity findPlayerByLoginName(String loginName) {
        return lambdaQuery().eq(StackPlayerEntity::getLoginName,loginName).one();
    }

    @Override
    public Boolean addCoin(Integer userId, int coin,int mason) {
        StackPlayerEntity player = getById(userId);
        if(!ObjectUtils.isEmpty(player)){
            return lambdaUpdate()
                    .eq(StackPlayerEntity::getUserId,userId)
                    .set(StackPlayerEntity::getCoin,player.getCoin()+coin)
                    .set(StackPlayerEntity::getMason,player.getMason()+mason)
                    .update();
        }
        return false;
    }

    @Override
    public Boolean gameUpData(Integer userId, int coin, int mason) {
        StackPlayerEntity player = getById(userId);
        if(!ObjectUtils.isEmpty(player)){
            return lambdaUpdate()
                    .eq(StackPlayerEntity::getUserId,userId)
                    .set(StackPlayerEntity::getCoin,coin)
                    .set(StackPlayerEntity::getMason,mason)
                    .update();
        }
        return false;
    }
}

