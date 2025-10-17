package com.julia.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.MinaSysPowerEntity;
import com.julia.mapper.MinaSysPowerMapper;
import com.julia.service.IMinaSysPowerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.julia.model.vo.MinaSysPowerEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import java.util.stream.Collectors;
/**
* <p>
    * 系统角色 服务实现类
    * </p>
*
* @author chowel
* @since 2025-10-17
*/
@Service
public class MinaSysPowerServiceImpl extends ServiceImpl<MinaSysPowerMapper, MinaSysPowerEntity> implements IMinaSysPowerService {
    @Override
    public Page<MinaSysPowerEntityVO> findForPage(QueryPagement queryPagement) {
        Page<MinaSysPowerEntity> p = new LambdaQueryChainWrapper<MinaSysPowerEntity>(getBaseMapper()).page(new Page<MinaSysPowerEntity>(queryPagement.getStartPage(),
        queryPagement.getPageSize()));
        Page<MinaSysPowerEntityVO> page = JuliaUtils.convertTo(new Page<MinaSysPowerEntityVO>(), p);
                page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new MinaSysPowerEntityVO(), e)).collect(Collectors.toList()));
                return page;
    }

    @Override
    public MinaSysPowerEntityVO findOneById(Long id) {
            MinaSysPowerEntity entity = getById(id);
            return JuliaUtils.convertTo(new MinaSysPowerEntityVO(), entity);
    }

    @Override
    public Boolean saveMinaSysPowerEntity(MinaSysPowerEntityVO vo) {
            return save(JuliaUtils.convertTo(new MinaSysPowerEntity(), vo));
    }

    @Override
    public Boolean alter(MinaSysPowerEntityVO vo) {
            return updateById(JuliaUtils.convertTo(new MinaSysPowerEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
            return removeById(id);
    }
}

