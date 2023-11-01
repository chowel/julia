package com.julia.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.PowerEntity;
import com.julia.mapper.PowerMapper;
import com.julia.service.IPowerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.julia.model.vo.PowerEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import java.util.stream.Collectors;
/**
* <p>
    * 权限表 服务实现类
    * </p>
*
* @author chowel
* @since 2023-11-01
*/
@Service
public class PowerServiceImpl extends ServiceImpl<PowerMapper, PowerEntity> implements IPowerService {
    @Override
    public Page<PowerEntityVO> findForPage(QueryPagement queryPagement) {
        Page<PowerEntity> p = new LambdaQueryChainWrapper<PowerEntity>(getBaseMapper()).page(new Page<PowerEntity>(queryPagement.getStartPage(),
        queryPagement.getPageSize()));
        Page<PowerEntityVO> page = JuliaUtils.convertTo(new Page<PowerEntityVO>(), p);
                page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new PowerEntityVO(), e)).collect(Collectors.toList()));
                return page;
    }

    @Override
    public PowerEntityVO findOneById(Long id) {
            PowerEntity entity = getById(id);
            return JuliaUtils.convertTo(new PowerEntityVO(), entity);
    }

    @Override
    public Boolean savePowerEntity(PowerEntityVO vo) {
            return save(JuliaUtils.convertTo(new PowerEntity(), vo));
    }

    @Override
    public Boolean alter(PowerEntityVO vo) {
            return updateById(JuliaUtils.convertTo(new PowerEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
            return removeById(id);
    }
}

