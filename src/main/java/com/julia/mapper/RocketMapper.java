package com.julia.mapper;

import com.julia.entity.RocketEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 火箭业务 Mapper 接口
 * </p>
 *
 * @author chowel
 * @since 2023-11-01
 */
@Mapper

public interface RocketMapper extends BaseMapper<RocketEntity> {

}
