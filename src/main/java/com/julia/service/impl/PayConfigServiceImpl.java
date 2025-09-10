package com.julia.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.PayConfigEntity;
import com.julia.mapper.PayConfigMapper;
import com.julia.service.IPayConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.julia.model.vo.PayConfigEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;

import java.util.List;
import java.util.stream.Collectors;
/**
* <p>
    *  服务实现类
    * </p>
*
* @author chowel
* @since 2025-03-15
*/
@Service
public class PayConfigServiceImpl extends ServiceImpl<PayConfigMapper, PayConfigEntity> implements IPayConfigService {
    @Override
    public Page<PayConfigEntityVO> findForPage(QueryPagement queryPagement) {
        Page<PayConfigEntity> p = new LambdaQueryChainWrapper<PayConfigEntity>(getBaseMapper()).page(new Page<PayConfigEntity>(queryPagement.getStartPage(),
        queryPagement.getPageSize()));
        Page<PayConfigEntityVO> page = JuliaUtils.convertTo(new Page<PayConfigEntityVO>(), p);
                page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new PayConfigEntityVO(), e)).collect(Collectors.toList()));
                return page;
    }

    @Override
    public List<PayConfigEntityVO> allconfig() {
        List<PayConfigEntity> payConfigEntityList = this.lambdaQuery().list();
        return payConfigEntityList.stream().map(e -> JuliaUtils.convertTo(new PayConfigEntityVO(), e)).collect(Collectors.toList());

    }

    @Override
    public PayConfigEntityVO findOneById(Long id) {
            PayConfigEntity entity = getById(id);
            return JuliaUtils.convertTo(new PayConfigEntityVO(), entity);
    }

    @Override
    public PayConfigEntity findOneByPrice(Long price) {
        return lambdaQuery().eq(PayConfigEntity::getPrice,price).one();
    }

    @Override
    public Boolean savePayConfigEntity(PayConfigEntityVO vo) {
            return save(JuliaUtils.convertTo(new PayConfigEntity(), vo));
    }

    @Override
    public Boolean alter(PayConfigEntityVO vo) {
            return updateById(JuliaUtils.convertTo(new PayConfigEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
            return removeById(id);
    }
}

