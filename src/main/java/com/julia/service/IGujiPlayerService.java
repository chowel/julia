package com.julia.service;

import com.julia.entity.GujiPlayerEntity;
import com.julia.model.dto.LoginDto;
import com.julia.model.vo.GujiPlayerEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
/**
 * <p>
 * 玩家 服务类
 * </p>
 *
 * @author chowel
 * @since 2025-12-17
 */

public interface IGujiPlayerService extends IService<GujiPlayerEntity> {
    /**
    * @Description: 分页查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-12-17
    */
    Page<GujiPlayerEntityVO> findForPage(QueryPagement queryPagement);

    /**
    * @Description: 根据id查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-12-17
    */
    GujiPlayerEntityVO findOneById(Long id);

    /**
    * @Description: 添加
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-12-17
    */
    Boolean saveGujiPlayerEntity(GujiPlayerEntityVO vo);

    /**
    * @Description: 修改
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-12-17
    */
    Boolean alter(GujiPlayerEntityVO vo);

    /**
    * @Description: 删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-12-17
    */
    Boolean remove(Long id);

    GujiPlayerEntityVO login(LoginDto dto);
}

