package com.julia.mapper;

import com.julia.entity.GameEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 游戏 Mapper 接口
 * </p>
 *
 * @author chowel
 * @since 2024-10-03
 */
@Mapper

public interface GameMapper extends BaseMapper<GameEntity> {

}
