package com.julia.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.MinaSysMenuEntity;
import com.julia.entity.PactEntity;
import com.julia.mapper.MinaSysMenuMapper;
import com.julia.model.vo.PactEntityVO;
import com.julia.service.IMinaSysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.julia.model.vo.MinaSysMenuEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;

import java.util.List;
import java.util.stream.Collectors;
/**
* <p>
    * 系统菜单 服务实现类
    * </p>
*
* @author chowel
* @since 2025-10-17
*/
@Service
public class MinaSysMenuServiceImpl extends ServiceImpl<MinaSysMenuMapper, MinaSysMenuEntity> implements IMinaSysMenuService {
    @Override
    public Page<MinaSysMenuEntityVO> findForPage(QueryPagement queryPagement) {
        Page<MinaSysMenuEntity> p = new LambdaQueryChainWrapper<MinaSysMenuEntity>(getBaseMapper()).page(new Page<MinaSysMenuEntity>(queryPagement.getStartPage(),
        queryPagement.getPageSize()));
        Page<MinaSysMenuEntityVO> page = JuliaUtils.convertTo(new Page<MinaSysMenuEntityVO>(), p);
                page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new MinaSysMenuEntityVO(), e)).collect(Collectors.toList()));
                return page;
    }

    @Override
    public MinaSysMenuEntityVO findOneById(Long id) {
            MinaSysMenuEntity entity = getById(id);
            return JuliaUtils.convertTo(new MinaSysMenuEntityVO(), entity);
    }

    @Override
    public Boolean saveMinaSysMenuEntity(MinaSysMenuEntityVO vo) {
            return save(JuliaUtils.convertTo(new MinaSysMenuEntity(), vo));
    }

    @Override
    public Boolean alter(MinaSysMenuEntityVO vo) {
            return updateById(JuliaUtils.convertTo(new MinaSysMenuEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
            return removeById(id);
    }

    @Override
    public List<MinaSysMenuEntityVO> findMenusById(int id) {
        List<MinaSysMenuEntity> list = baseMapper.getMenusByPowerId(id);
        return list.stream().map(e->JuliaUtils.convertTo(new MinaSysMenuEntityVO(),e)).collect(Collectors.toList());
    }
}

