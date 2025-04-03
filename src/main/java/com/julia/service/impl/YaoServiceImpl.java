package com.julia.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.CoinLogEntity;
import com.julia.entity.PactEntity;
import com.julia.entity.YaoEntity;
import com.julia.enums.RedisKeyEnum;
import com.julia.mapper.CoinLogMapper;
import com.julia.mapper.PactMapper;
import com.julia.mapper.YaoMapper;
import com.julia.model.dto.LoginDto;
import com.julia.model.dto.MidPasswordDto;
import com.julia.model.vo.PactEntityVO;
import com.julia.service.IYaoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.julia.tool.AdminToken;
import com.julia.tool.JuliaException;
import com.julia.tool.RedisUtils;
import org.springframework.stereotype.Service;
import com.julia.model.vo.YaoEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 账号 服务实现类
 * </p>
 *
 * @author chowel
 * @since 2023-11-01
 */
@Service
public class YaoServiceImpl extends ServiceImpl<YaoMapper, YaoEntity> implements IYaoService {

    @Resource
    CoinLogMapper coinLogMapper;

//    @Resource
//    WebSocketService webSocketService;

    @Resource
    RedisUtils redisUtils;


    @Override
    public Page<YaoEntityVO> findForPage(QueryPagement queryPagement) {
        int roleId = 0;
        Map<String, Object> searchFields = queryPagement.getSearchFields();
        if (searchFields.containsKey("roleId")) {
            roleId = (int) queryPagement.getSearchFields().get("roleId");
        }

        Page<YaoEntity> p = new LambdaQueryChainWrapper<YaoEntity>(getBaseMapper())
                .eq(roleId > 0, YaoEntity::getRoleId, roleId)
                .page(new Page<YaoEntity>(queryPagement.getStartPage(),
                        queryPagement.getPageSize()));
        Page<YaoEntityVO> page = JuliaUtils.convertTo(new Page<YaoEntityVO>(), p);
        page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new YaoEntityVO(), e)).collect(Collectors.toList()));
        return page;
    }

    @Override
    public YaoEntityVO findOneById(Long id) {
        YaoEntity entity = getById(id);
        return JuliaUtils.convertTo(new YaoEntityVO(), entity);
    }

    @Override
    public Boolean saveYaoEntity(YaoEntityVO vo) {
        if (StringUtils.hasLength(vo.getPassword())) {
            vo.setPassword(BCrypt.hashpw(vo.getPassword()));
        }
        return save(JuliaUtils.convertTo(new YaoEntity(), vo));
    }

    @Override
    public Boolean alter(YaoEntityVO vo) {
        // 提人
//        if (vo.getCheDel() != null && vo.getCheDel() == 2) {
//            StpUtil.logout(vo.getYaoId());
//            webSocketService.delByUserid(String.valueOf(vo.getYaoId()));
//        }
//        return updateById(JuliaUtils.convertTo(new YaoEntity(), vo));
        return true;
    }

    @Override
    public Boolean remove(Long id) {
        return removeById(id);
    }

    @Override
    public YaoEntityVO login(LoginDto dto) {
        YaoEntity yao = this.getOne(new QueryWrapper<YaoEntity>().eq("login_name", dto.getName()));

        if (ObjectUtils.isEmpty(yao)) {
            throw new JuliaException("用户不存在");
        }

        if (yao.getCheDel() == 2) {
            throw new JuliaException("用户禁用");
        }

        if (!BCrypt.checkpw(dto.getPassword(), yao.getPassword())) {
            throw new JuliaException("密码错误");
        }

//        List<PactEntity> pactList  =  pactMapper.getPackByPowerId(yao.getRoleId());

        YaoEntityVO vo = JuliaUtils.convertTo(new YaoEntityVO(), yao);

//        vo.setMenus();
        AdminToken.login(yao.getYaoId());
        vo.setToken(AdminToken.getTokenValue());
        vo.setPassword("******");
        return vo;
    }

    @Override
    public YaoEntityVO mySelf(Integer id) {
        YaoEntity entity = getById(id);
        YaoEntityVO vo = JuliaUtils.convertTo(new YaoEntityVO(), entity);
        vo.setToken(StpUtil.getTokenValue());
        return vo;
    }

    @Override
    public String createToken(Integer id) {
        YaoEntity entity = getById(id);
        StpUtil.login(entity.getYaoId());
        return StpUtil.getTokenValue();
    }

    @Override
    public String getTokenByPan(LoginDto dto) {
        YaoEntity yao = this.getOne(new QueryWrapper<YaoEntity>().eq("login_name", dto.getName()));

        if (ObjectUtils.isEmpty(yao)) {
            throw new JuliaException("用户不存在");
        }

        if (yao.getCheDel() == 2) {
            throw new JuliaException("用户禁用");
        }

        if (!BCrypt.checkpw(dto.getPassword(), yao.getPassword())) {
            throw new JuliaException("密码错误");
        }
        StpUtil.login(yao.getYaoId());
        return StpUtil.getTokenValue();
    }

    @Override
    public Boolean alterPassword(MidPasswordDto dto) {
        YaoEntity entity = getById(dto.getYaoId());
        if (!BCrypt.checkpw(dto.getPassword(), entity.getPassword())) {
            throw new JuliaException("旧密码错误");
        }
        entity.setPassword(BCrypt.hashpw(dto.getNewPassword()));
        return updateById(entity);
    }

    @Override
    @Transactional
    public Boolean altercCoin(YaoEntityVO vo, int pId) {
        YaoEntity entity = getById(vo.getYaoId());
        // todo 加锁
        int coin = entity.getCoin() + vo.getCoin();
        if (coin < 0) {
            coin = 0;
        }
        entity.setCoin(coin);
        updateById(entity);

        CoinLogEntity coinLog = new CoinLogEntity();
        coinLog.setCId(vo.getYaoId());
        coinLog.setCoin(vo.getCoin());
        coinLog.setYId(pId);
        coinLogMapper.insert(coinLog);
        return true;
    }

    @Override
    public YaoEntity getCallBackOrKey(String loginName) {
        YaoEntity jh = (YaoEntity)redisUtils.get(RedisKeyEnum.JIAHE.getKey());
        if(ObjectUtils.isEmpty(jh)){
            jh = lambdaQuery().eq(YaoEntity::getLoginName,loginName).one();
            redisUtils.set(RedisKeyEnum.JIAHE.getKey(),jh,3600*24*7);
        }
        return jh;
    }
}

