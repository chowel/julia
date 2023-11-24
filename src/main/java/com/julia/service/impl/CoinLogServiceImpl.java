package com.julia.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.CoinLogEntity;
import com.julia.mapper.CoinLogMapper;
import com.julia.service.ICoinLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.julia.model.vo.CoinLogEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import java.util.stream.Collectors;
/**
* <p>
    * 车队加进记录 服务实现类
    * </p>
*
* @author chowel
* @since 2023-11-23
*/
@Service
public class CoinLogServiceImpl extends ServiceImpl<CoinLogMapper, CoinLogEntity> implements ICoinLogService {
    @Override
    public Page<CoinLogEntityVO> findForPage(QueryPagement queryPagement) {
        Page<CoinLogEntity> p = new LambdaQueryChainWrapper<CoinLogEntity>(getBaseMapper()).page(new Page<CoinLogEntity>(queryPagement.getStartPage(),
        queryPagement.getPageSize()));
        Page<CoinLogEntityVO> page = JuliaUtils.convertTo(new Page<CoinLogEntityVO>(), p);
                page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new CoinLogEntityVO(), e)).collect(Collectors.toList()));
                return page;
    }

    @Override
    public CoinLogEntityVO findOneById(Long id) {
            CoinLogEntity entity = getById(id);
            return JuliaUtils.convertTo(new CoinLogEntityVO(), entity);
    }

    @Override
    public Boolean saveCoinLogEntity(CoinLogEntityVO vo) {
            return save(JuliaUtils.convertTo(new CoinLogEntity(), vo));
    }

    @Override
    public Boolean alter(CoinLogEntityVO vo) {
            return updateById(JuliaUtils.convertTo(new CoinLogEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
            return removeById(id);
    }
}

