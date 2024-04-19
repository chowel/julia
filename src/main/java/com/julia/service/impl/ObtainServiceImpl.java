package com.julia.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.FortuneEntity;
import com.julia.entity.ObtainEntity;
import com.julia.mapper.ObtainMapper;
import com.julia.service.IObtainService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.julia.tool.JuliaException;
import org.springframework.stereotype.Service;
import com.julia.model.vo.ObtainEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
* <p>
    * 车队下账号 服务实现类
    * </p>
*
* @author chowel
* @since 2024-02-23
*/
@Service
public class ObtainServiceImpl extends ServiceImpl<ObtainMapper, ObtainEntity> implements IObtainService {
    @Override
    public Page<ObtainEntityVO> findForPage(QueryPagement queryPagement) {
        Map<String, Object> sf = queryPagement.getSearchFields();
        Page<ObtainEntity> p = new LambdaQueryChainWrapper<ObtainEntity>(getBaseMapper())
                .eq( ObtainEntity::getYaoId, sf.get("carid"))
                .eq(StringUtils.hasLength((String) sf.get("name")), ObtainEntity::getName, sf.get("name"))
                .page(new Page<ObtainEntity>(queryPagement.getStartPage(), queryPagement.getPageSize()));
        Page<ObtainEntityVO> page = JuliaUtils.convertTo(new Page<ObtainEntityVO>(), p);
                page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new ObtainEntityVO(), e)).collect(Collectors.toList()));
                return page;
    }

    @Override
    public ObtainEntityVO findOneById(Long id) {
            ObtainEntity entity = getById(id);
            return JuliaUtils.convertTo(new ObtainEntityVO(), entity);
    }

    @Override
    public Boolean saveObtainEntity(ObtainEntityVO vo) {
            ObtainEntity obtain = new LambdaQueryChainWrapper<ObtainEntity>(getBaseMapper())
                    .eq(ObtainEntity::getName,vo.getName()).one();
            if(!ObjectUtils.isEmpty(obtain)){
                throw new JuliaException("账号重复");
            }
            return save(JuliaUtils.convertTo(new ObtainEntity(), vo));
    }

    @Override
    public Boolean alter(ObtainEntityVO vo) {
        ObtainEntity obtain = new LambdaQueryChainWrapper<ObtainEntity>(getBaseMapper())
                .eq(ObtainEntity::getName,vo.getName()).one();
        if(!ObjectUtils.isEmpty(obtain)){
            throw new JuliaException("账号重复");
        }
        return updateById(JuliaUtils.convertTo(new ObtainEntity(), vo));
    }

    @Override
    public Boolean remove(Integer id) {
            return removeById(id);
    }

    @Override
    public List<ObtainEntityVO> allObtain(int carId) {
        List<ObtainEntity> list  =
                new LambdaQueryChainWrapper<ObtainEntity>(getBaseMapper())
                        .eq(ObtainEntity::getStatus,1).eq(ObtainEntity::getYaoId,carId).list();
        return list.stream().map(e -> JuliaUtils.convertTo(new ObtainEntityVO(), e)).collect(Collectors.toList());
    }

    @Override
    public Boolean clearCout(int carId) {
        getBaseMapper().coutEmpty(carId);
        return true;
    }

}

