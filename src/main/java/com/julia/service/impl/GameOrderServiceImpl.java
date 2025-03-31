package com.julia.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.GameOrderEntity;
import com.julia.mapper.GameOrderMapper;
import com.julia.model.alipay.AliPayCreate;
import com.julia.model.dto.DrawerPollDTO;
import com.julia.service.IGameOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.julia.model.vo.GameOrderEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.stream.Collectors;
/**
* <p>
    * 游戏充值订单表 服务实现类
    * </p>
*
* @author chowel
* @since 2025-03-19
*/
@Service
public class GameOrderServiceImpl extends ServiceImpl<GameOrderMapper, GameOrderEntity> implements IGameOrderService {

    @Resource
    AlipayService alipayService;

    @Override
    public Page<GameOrderEntityVO> findForPage(QueryPagement queryPagement) {
        Page<GameOrderEntity> p = new LambdaQueryChainWrapper<GameOrderEntity>(getBaseMapper()).page(new Page<GameOrderEntity>(queryPagement.getStartPage(),
        queryPagement.getPageSize()));
        Page<GameOrderEntityVO> page = JuliaUtils.convertTo(new Page<GameOrderEntityVO>(), p);
                page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new GameOrderEntityVO(), e)).collect(Collectors.toList()));
                return page;
    }

    @Override
    public GameOrderEntityVO findOneById(Long id) {
            GameOrderEntity entity = getById(id);
            return JuliaUtils.convertTo(new GameOrderEntityVO(), entity);
    }

    @Override
    public DrawerPollDTO saveGameOrderEntity(GameOrderEntityVO vo) {
//        GameOrderEntity order = JuliaUtils.convertTo(new GameOrderEntity(), vo);
//        order.setOrderNo(JuliaUtils.GeneratorOderNo(vo.getPlayerId()));
//        if (save(order)) {
//            // todo 去支付宝
//            AliPayCreate aliPayCreate = new AliPayCreate();
//            aliPayCreate.setOutTradeNo(order.getOrderNo());
//            aliPayCreate.setSubject(order.getSubject());
//            Long price =order.getTotal();
//
//            aliPayCreate.setTotalAmount(String.format("%.2f",price/100.0));
//
//            String payUrl =  alipayService.createPay(aliPayCreate);
//            if(StringUtils.hasLength(payUrl)){
//                DrawerPollDTO dto = new DrawerPollDTO();
//                dto.setOrderNo(order.getOrderNo());
//                dto.setPayUrl(payUrl);
//                return dto;
//            }
//
//        }
        return null;
    }

    @Override
    public Boolean alter(GameOrderEntityVO vo) {
            return updateById(JuliaUtils.convertTo(new GameOrderEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
            return removeById(id);
    }
}

