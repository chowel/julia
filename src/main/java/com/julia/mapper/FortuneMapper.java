package com.julia.mapper;

import com.julia.entity.FortuneEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.julia.entity.MetricsFortuneEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 财神 Mapper 接口
 * </p>
 *
 * @author chowel
 * @since 2024-02-15
 */
@Mapper
public interface FortuneMapper extends BaseMapper<FortuneEntity> {

   List<MetricsFortuneEntity> metricsfortune(String st,String et,int cid,int pid);
}
