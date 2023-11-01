package com.julia.mapper;

import com.julia.entity.PactEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 菜单/接口权限項 Mapper 接口
 * </p>
 *
 * @author chowel
 * @since 2023-11-01
 */
@Mapper

public interface PactMapper extends BaseMapper<PactEntity> {

    List<PactEntity> getPackByPowerId(int powerId);

}
