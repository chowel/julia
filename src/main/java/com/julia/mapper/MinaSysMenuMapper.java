package com.julia.mapper;

import com.julia.entity.MinaSysMenuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 系统菜单 Mapper 接口
 * </p>
 *
 * @author chowel
 * @since 2025-10-17
 */
@Mapper

public interface MinaSysMenuMapper extends BaseMapper<MinaSysMenuEntity> {

    List<MinaSysMenuEntity> getMenusByPowerId(int powerId);

}
