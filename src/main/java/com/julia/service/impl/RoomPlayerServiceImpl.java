package com.julia.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.RoomPlayerEntity;
import com.julia.mapper.RoomPlayerMapper;
import com.julia.service.IRoomPlayerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.julia.model.vo.RoomPlayerEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import java.util.stream.Collectors;
/**
* <p>
    * 房间玩家 服务实现类
    * </p>
*
* @author chowel
* @since 2024-09-30
*/
@Service
public class RoomPlayerServiceImpl extends ServiceImpl<RoomPlayerMapper, RoomPlayerEntity> implements IRoomPlayerService {
    @Override
    public Page<RoomPlayerEntityVO> findForPage(QueryPagement queryPagement) {
        Page<RoomPlayerEntity> p = new LambdaQueryChainWrapper<RoomPlayerEntity>(getBaseMapper()).page(new Page<RoomPlayerEntity>(queryPagement.getStartPage(),
        queryPagement.getPageSize()));
        Page<RoomPlayerEntityVO> page = JuliaUtils.convertTo(new Page<RoomPlayerEntityVO>(), p);
                page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new RoomPlayerEntityVO(), e)).collect(Collectors.toList()));
                return page;
    }

    @Override
    public RoomPlayerEntityVO findOneById(Long id) {
            RoomPlayerEntity entity = getById(id);
            return JuliaUtils.convertTo(new RoomPlayerEntityVO(), entity);
    }

    @Override
    public Boolean saveRoomPlayerEntity(RoomPlayerEntityVO vo) {
            return save(JuliaUtils.convertTo(new RoomPlayerEntity(), vo));
    }

    @Override
    public Boolean alter(RoomPlayerEntityVO vo) {
            return updateById(JuliaUtils.convertTo(new RoomPlayerEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
            return removeById(id);
    }
}

