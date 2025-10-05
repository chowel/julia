package com.julia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.YaoClientEntity;
import com.julia.mapper.YaoClientMapper;
import com.julia.service.IYaoClientService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.julia.model.vo.YaoClientEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import org.springframework.util.ObjectUtils;

import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chowel
 * @since 2025-10-04
 */
@Service
public class YaoClientServiceImpl extends ServiceImpl<YaoClientMapper, YaoClientEntity> implements IYaoClientService {
    @Override
    public Page<YaoClientEntityVO> findForPage(QueryPagement queryPagement) {
        Page<YaoClientEntity> p = new LambdaQueryChainWrapper<YaoClientEntity>(getBaseMapper()).page(new Page<YaoClientEntity>(queryPagement.getStartPage(),
                queryPagement.getPageSize()));
        Page<YaoClientEntityVO> page = JuliaUtils.convertTo(new Page<YaoClientEntityVO>(), p);
        page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new YaoClientEntityVO(), e)).collect(Collectors.toList()));
        return page;
    }

    @Override
    public YaoClientEntityVO findOneById(Long id) {
        YaoClientEntity entity = getById(id);
        return JuliaUtils.convertTo(new YaoClientEntityVO(), entity);
    }

    @Override
    public Boolean saveYaoClientEntity(YaoClientEntityVO vo) {
        YaoClientEntity client = lambdaQuery().eq(YaoClientEntity::getSecure, vo.getSecure()).one();
        if(ObjectUtils.isEmpty(client)){
            return save(JuliaUtils.convertTo(new YaoClientEntity(), vo));
        }else{
            YaoClientEntity upDateClient = JuliaUtils.convertTo(new YaoClientEntity(), vo);
            upDateClient.setId(client.getId());
            return updateById(upDateClient);
        }

    }

    @Override
    public Boolean alter(YaoClientEntityVO vo) {
        return updateById(JuliaUtils.convertTo(new YaoClientEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
        return removeById(id);
    }

    @Override
    public Boolean saveFromConnect(String secure, String hostname) {
        YaoClientEntity client = lambdaQuery().eq(YaoClientEntity::getSecure, secure).one();
        if (ObjectUtils.isEmpty(client)) {
            client = new YaoClientEntity();
            client.setSecure(secure);
            client.setHostname(hostname);
            return save(client);
        }
        return false;
    }
}

