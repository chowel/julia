package com.julia.mapper;

import com.julia.entity.GameScoreEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 游戏得分 Mapper 接口
 * </p>
 *
 * @author chowel
 * @since 2024-10-03
 */
@Mapper

public interface GameScoreMapper extends BaseMapper<GameScoreEntity> {

}
