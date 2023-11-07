package com.julia.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.RocketEntity;
import com.julia.entity.YaoEntity;
import com.julia.mapper.RocketMapper;
import com.julia.mapper.YaoMapper;
import com.julia.model.dto.CarOperaDTO;
import com.julia.model.vo.CarOrderVO;
import com.julia.service.IRocketService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.julia.tool.JuliaException;
import org.springframework.stereotype.Service;
import com.julia.model.vo.RocketEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
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

    @Resource
    YaoMapper yaoMapper;
    @Override
    public Page<CarOrderVO> findForPage(QueryPagement queryPagement) {
        int status = (int) queryPagement.getSearchFields().get("status");
        Page<RocketEntity> p = new LambdaQueryChainWrapper<RocketEntity>(getBaseMapper())
                .eq(status>-1, RocketEntity::getStatus,status)
                .page(new Page<RocketEntity>(queryPagement.getStartPage(), queryPagement.getPageSize()));
        Page<CarOrderVO> page = JuliaUtils.convertTo(new Page<CarOrderVO>(), p);
                page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new CarOrderVO(), e)).collect(Collectors.toList()));
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

    @Override
    public Boolean carOpera(CarOperaDTO dto) {
        RocketEntity entity = getById(dto.getRocketId());
        if(ObjectUtils.isEmpty(entity)){
            throw new JuliaException("操作异常");
        }
        entity.setRealPay(dto.getRealPay());
        entity.setCId(dto.getCId());

        YaoEntity pYao  = yaoMapper.selectById(entity.getPId());
        String callBack_url = pYao.getCallback();
        // todo 回调逻辑
        entity.setStatus(1);
        return updateById(entity);
    }
}

