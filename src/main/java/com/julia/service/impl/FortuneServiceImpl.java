package com.julia.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.FortuneEntity;
import com.julia.mapper.FortuneMapper;
import com.julia.model.dto.FortuneDTO;
import com.julia.service.IFortuneService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.julia.model.vo.FortuneEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;

import javax.annotation.Resource;
import java.util.stream.Collectors;

/**
 * <p>
 * 财神 服务实现类
 * </p>
 *
 * @author chowel
 * @since 2024-02-15
 */
@Service
public class FortuneServiceImpl extends ServiceImpl<FortuneMapper, FortuneEntity> implements IFortuneService {

    @Resource
    WebSocketService webSocketService;

    @Override
    public Page<FortuneEntityVO> findForPage(QueryPagement queryPagement) {
        Page<FortuneEntity> p = new LambdaQueryChainWrapper<FortuneEntity>(getBaseMapper()).page(new Page<FortuneEntity>(queryPagement.getStartPage(),
                queryPagement.getPageSize()));
        Page<FortuneEntityVO> page = JuliaUtils.convertTo(new Page<FortuneEntityVO>(), p);
        page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new FortuneEntityVO(), e)).collect(Collectors.toList()));
        return page;
    }

    @Override
    public FortuneEntityVO findOneById(Long id) {
        FortuneEntity entity = getById(id);
        return JuliaUtils.convertTo(new FortuneEntityVO(), entity);
    }

    @Override
    public Boolean saveFortuneEntity(FortuneEntityVO vo) {
        return save(JuliaUtils.convertTo(new FortuneEntity(), vo));
    }

    @Override
    public Boolean alter(FortuneEntityVO vo) {
        return updateById(JuliaUtils.convertTo(new FortuneEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
        return removeById(id);
    }

    @Override
    public Boolean input(FortuneDTO dto, int pid) {
        FortuneEntity entity = new FortuneEntity();
        entity.setAmount(dto.getAmount());
        entity.setPId(pid);
        entity.setOrderId(dto.getOrderId());

        if (save(entity)) {
            webSocketService.hanldeFortune(entity);
        }
        return false;
    }


}

