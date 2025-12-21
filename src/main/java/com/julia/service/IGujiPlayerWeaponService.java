package com.julia.service;

import com.julia.entity.GujiPlayerWeaponEntity;
import com.julia.model.vo.GujiPlayerWeaponEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
/**
 * <p>
 * 玩家武器 服务类
 * </p>
 *
 * @author chowel
 * @since 2025-12-21
 */

public interface IGujiPlayerWeaponService extends IService<GujiPlayerWeaponEntity> {
    /**
    * @Description: 分页查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-12-21
    */
    Page<GujiPlayerWeaponEntityVO> findForPage(QueryPagement queryPagement);

    /**
    * @Description: 根据id查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-12-21
    */
    GujiPlayerWeaponEntityVO findOneById(Long id);

    /**
    * @Description: 添加
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-12-21
    */
    Boolean saveGujiPlayerWeaponEntity(GujiPlayerWeaponEntityVO vo);

    /**
    * @Description: 修改
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-12-21
    */
    Boolean alter(GujiPlayerWeaponEntityVO vo);

    /**
    * @Description: 删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-12-21
    */
    Boolean remove(Long id);
}

