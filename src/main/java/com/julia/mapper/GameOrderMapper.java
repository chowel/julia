package com.julia.mapper;

import com.julia.entity.GameOrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 游戏充值订单表 Mapper 接口
 * </p>
 *
 * @author chowel
 * @since 2025-03-19
 */
@Mapper

public interface GameOrderMapper extends BaseMapper<GameOrderEntity> {

}
