package com.julia.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.PactEntity;
import com.julia.entity.YaoEntity;
import com.julia.mapper.PactMapper;
import com.julia.mapper.YaoMapper;
import com.julia.model.dto.LoginDto;
import com.julia.model.vo.PactEntityVO;
import com.julia.service.IYaoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.julia.tool.JuliaException;
import org.springframework.stereotype.Service;
import com.julia.model.vo.YaoEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
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
    PactMapper pactMapper;

    @Override
    public Page<YaoEntityVO> findForPage(QueryPagement queryPagement) {
        Page<YaoEntity> p = new LambdaQueryChainWrapper<YaoEntity>(getBaseMapper()).page(new Page<YaoEntity>(queryPagement.getStartPage(),
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
        return save(JuliaUtils.convertTo(new YaoEntity(), vo));
    }

    @Override
    public Boolean alter(YaoEntityVO vo) {
        return updateById(JuliaUtils.convertTo(new YaoEntity(), vo));
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

        if (yao.getCheDel() == 0) {
            throw new JuliaException("用户禁用");
        }

        if (!BCrypt.checkpw(dto.getPassword(),yao.getPassword())) {
            throw new JuliaException("密码错误");
        }

//        List<PactEntity> pactList  =  pactMapper.getPackByPowerId(yao.getRoleId());

        YaoEntityVO vo = JuliaUtils.convertTo(new YaoEntityVO(),yao);

//        vo.setMenus();
        StpUtil.login(yao.getYaoId());
        vo.setToken(StpUtil.getTokenValue());
        vo.setPassword("******");
        return vo;
    }
}

