package com.julia.service;

import com.julia.entity.MinaPlayerEntity;
import com.julia.model.dto.LoginDto;
import com.julia.model.vo.MinaPlayerEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
/**
 * <p>
 * 玩家 服务类
 * </p>
 *
 * @author chowel
 * @since 2025-10-17
 */

public interface IMinaPlayerService extends IService<MinaPlayerEntity> {
    /**
    * @Description: 分页查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-17
    */
    Page<MinaPlayerEntityVO> findForPage(QueryPagement queryPagement);

    /**
    * @Description: 根据id查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-17
    */
    MinaPlayerEntityVO findOneById(Long id);

    /**
    * @Description: 添加
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-17
    */
    Boolean saveMinaPlayerEntity(MinaPlayerEntityVO vo);

    /**
    * @Description: 修改
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-17
    */
    Boolean alter(MinaPlayerEntityVO vo);

    /**
    * @Description: 删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-17
    */
    Boolean remove(Long id);

    MinaPlayerEntityVO login(LoginDto dto);
}

