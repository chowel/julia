package com.julia.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.PactEntity;
import com.julia.mapper.PactMapper;
import com.julia.model.QueryPagement;
import com.julia.model.vo.PactEntityVO;
import com.julia.service.IPactService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.julia.tool.JuliaUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
/**
* <p>
    * 菜单/接口权限項 服务实现类
    * </p>
*
* @author chowel
* @since 2023-11-01
*/
@Service
public class PactServiceImpl extends ServiceImpl<PactMapper, PactEntity> implements IPactService {
    @Override
    public Page<PactEntityVO> findForPage(QueryPagement queryPagement) {
        Page<PactEntity> p = new LambdaQueryChainWrapper<PactEntity>(getBaseMapper()).page(new Page<PactEntity>(queryPagement.getStartPage(),
        queryPagement.getPageSize()));
        Page<PactEntityVO> page = JuliaUtils.convertTo(new Page<PactEntityVO>(), p);
                page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new PactEntityVO(), e)).collect(Collectors.toList()));
                return page;
    }

    @Override
    public PactEntityVO findOneById(Long id) {
            PactEntity entity = getById(id);
            return JuliaUtils.convertTo(new PactEntityVO(), entity);
    }

    @Override
    public Boolean savePactEntity(PactEntityVO vo) {
            return save(JuliaUtils.convertTo(new PactEntity(), vo));
    }

    @Override
    public Boolean alter(PactEntityVO vo) {
            return updateById(JuliaUtils.convertTo(new PactEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
            return removeById(id);
    }

    @Override
    public List<PactEntityVO> findPactById(int id) {
        List<PactEntity> list = baseMapper.getPackByPowerId(id);
        return list.stream().map(e->JuliaUtils.convertTo(new PactEntityVO(),e)).collect(Collectors.toList());
    }
}

