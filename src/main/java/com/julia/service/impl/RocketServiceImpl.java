package com.julia.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.RocketEntity;
import com.julia.mapper.RocketMapper;
import com.julia.service.IRocketService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.julia.model.vo.RocketEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import java.util.stream.Collectors;
/**
* <p>
    * 火箭业务 服务实现类
    * </p>
*
* @author chowel
* @since 2023-11-01
*/
@Service
public class RocketServiceImpl extends ServiceImpl<RocketMapper, RocketEntity> implements IRocketService {
    @Override
    public Page<RocketEntityVO> findForPage(QueryPagement queryPagement) {
        Page<RocketEntity> p = new LambdaQueryChainWrapper<RocketEntity>(getBaseMapper()).page(new Page<RocketEntity>(queryPagement.getStartPage(),
        queryPagement.getPageSize()));
        Page<RocketEntityVO> page = JuliaUtils.convertTo(new Page<RocketEntityVO>(), p);
                page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new RocketEntityVO(), e)).collect(Collectors.toList()));
                return page;
    }

    @Override
    public RocketEntityVO findOneById(Long id) {
            RocketEntity entity = getById(id);
            return JuliaUtils.convertTo(new RocketEntityVO(), entity);
    }

    @Override
    public Boolean saveRocketEntity(RocketEntityVO vo) {
            return save(JuliaUtils.convertTo(new RocketEntity(), vo));
    }

    @Override
    public Boolean alter(RocketEntityVO vo) {
            return updateById(JuliaUtils.convertTo(new RocketEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
            return removeById(id);
    }
}

