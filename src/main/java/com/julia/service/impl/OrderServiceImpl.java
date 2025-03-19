package com.julia.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.OrderEntity;
import com.julia.mapper.OrderMapper;
import com.julia.model.dto.DrawerPollDTO;
import com.julia.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.julia.model.vo.OrderEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;

import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chowel
 * @since 2025-03-19
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderEntity> implements IOrderService {
    @Override
    public Page<OrderEntityVO> findForPage(QueryPagement queryPagement) {
        Page<OrderEntity> p = new LambdaQueryChainWrapper<OrderEntity>(getBaseMapper()).page(new Page<OrderEntity>(queryPagement.getStartPage(),
                queryPagement.getPageSize()));
        Page<OrderEntityVO> page = JuliaUtils.convertTo(new Page<OrderEntityVO>(), p);
        page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new OrderEntityVO(), e)).collect(Collectors.toList()));
        return page;
    }

    @Override
    public OrderEntityVO findOneById(Long id) {
        OrderEntity entity = getById(id);
        return JuliaUtils.convertTo(new OrderEntityVO(), entity);
    }

    @Override
    public DrawerPollDTO saveOrderEntity(OrderEntityVO vo) {
        OrderEntity order = JuliaUtils.convertTo(new OrderEntity(), vo);
        order.setOrderNo(JuliaUtils.GeneratorOderNo(vo.getPlayerId()));
        if (save(order)) {
            // todo 去支付宝
            DrawerPollDTO dto = new DrawerPollDTO();
            dto.setOrderNo(order.getOrderNo());
            dto.setPayUrl("www.baidu.com.cn");
        }
        return null;
    }

    @Override
    public Boolean alter(OrderEntityVO vo) {
        return updateById(JuliaUtils.convertTo(new OrderEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
        return removeById(id);
    }
}

