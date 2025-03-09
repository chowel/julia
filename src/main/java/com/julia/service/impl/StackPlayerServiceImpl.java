package com.julia.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.StackPlayerEntity;
import com.julia.mapper.StackPlayerMapper;
import com.julia.model.dto.LoginDto;
import com.julia.service.IStackPlayerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.julia.tool.JuliaException;
import org.springframework.stereotype.Service;
import com.julia.model.vo.StackPlayerEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import org.springframework.util.ObjectUtils;

import java.util.stream.Collectors;
/**
* <p>
    * 玩家 服务实现类
    * </p>
*
* @author chowel
* @since 2025-03-09
*/
@Service
public class StackPlayerServiceImpl extends ServiceImpl<StackPlayerMapper, StackPlayerEntity> implements IStackPlayerService {
    @Override
    public Page<StackPlayerEntityVO> findForPage(QueryPagement queryPagement) {
        Page<StackPlayerEntity> p = new LambdaQueryChainWrapper<StackPlayerEntity>(getBaseMapper()).page(new Page<StackPlayerEntity>(queryPagement.getStartPage(),
        queryPagement.getPageSize()));
        Page<StackPlayerEntityVO> page = JuliaUtils.convertTo(new Page<StackPlayerEntityVO>(), p);
                page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new StackPlayerEntityVO(), e)).collect(Collectors.toList()));
                return page;
    }

    @Override
    public StackPlayerEntityVO findOneById(Long id) {
            StackPlayerEntity entity = getById(id);
            return JuliaUtils.convertTo(new StackPlayerEntityVO(), entity);
    }

    @Override
    public Boolean saveStackPlayerEntity(StackPlayerEntityVO vo) {
            return save(JuliaUtils.convertTo(new StackPlayerEntity(), vo));
    }

    @Override
    public Boolean alter(StackPlayerEntityVO vo) {
            return updateById(JuliaUtils.convertTo(new StackPlayerEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
            return removeById(id);
    }

    @Override
    public StackPlayerEntityVO playerLogin(LoginDto dto) {
        StackPlayerEntity player  = lambdaQuery().eq(StackPlayerEntity::getLoginName,dto.getName()).one();
        if(ObjectUtils.isEmpty(player)){
            throw new JuliaException("玩家不存在");
        }

        return null;
    }
}

