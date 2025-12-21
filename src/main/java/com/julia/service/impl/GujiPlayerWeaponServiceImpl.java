package com.julia.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.GujiPlayerWeaponEntity;
import com.julia.mapper.GujiPlayerWeaponMapper;
import com.julia.service.IGujiPlayerWeaponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.julia.model.vo.GujiPlayerWeaponEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import java.util.stream.Collectors;
/**
* <p>
    * 玩家武器 服务实现类
    * </p>
*
* @author chowel
* @since 2025-12-21
*/
@Service
public class GujiPlayerWeaponServiceImpl extends ServiceImpl<GujiPlayerWeaponMapper, GujiPlayerWeaponEntity> implements IGujiPlayerWeaponService {
    @Override
    public Page<GujiPlayerWeaponEntityVO> findForPage(QueryPagement queryPagement) {
        Page<GujiPlayerWeaponEntity> p = new LambdaQueryChainWrapper<GujiPlayerWeaponEntity>(getBaseMapper()).page(new Page<GujiPlayerWeaponEntity>(queryPagement.getStartPage(),
        queryPagement.getPageSize()));
        Page<GujiPlayerWeaponEntityVO> page = JuliaUtils.convertTo(new Page<GujiPlayerWeaponEntityVO>(), p);
                page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new GujiPlayerWeaponEntityVO(), e)).collect(Collectors.toList()));
                return page;
    }

    @Override
    public GujiPlayerWeaponEntityVO findOneById(Long id) {
            GujiPlayerWeaponEntity entity = getById(id);
            return JuliaUtils.convertTo(new GujiPlayerWeaponEntityVO(), entity);
    }

    @Override
    public Boolean saveGujiPlayerWeaponEntity(GujiPlayerWeaponEntityVO vo) {
            return save(JuliaUtils.convertTo(new GujiPlayerWeaponEntity(), vo));
    }

    @Override
    public Boolean alter(GujiPlayerWeaponEntityVO vo) {
            return updateById(JuliaUtils.convertTo(new GujiPlayerWeaponEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
            return removeById(id);
    }
}

