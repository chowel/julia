package com.julia.service;

import com.julia.entity.GameLabaEntity;
import com.julia.model.vo.GameLabaEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
/**
 * <p>
 * laba游戏详情 服务类
 * </p>
 *
 * @author chowel
 * @since 2024-10-31
 */

public interface IGameLabaService extends IService<GameLabaEntity> {
    /**
    * @Description: 分页查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-10-31
    */
    Page<GameLabaEntityVO> findForPage(QueryPagement queryPagement);

    /**
    * @Description: 根据id查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-10-31
    */
    GameLabaEntityVO findOneById(Long id);

    /**
    * @Description: 添加
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-10-31
    */
    Boolean saveGameLabaEntity(GameLabaEntityVO vo);

    /**
    * @Description: 修改
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-10-31
    */
    Boolean alter(GameLabaEntityVO vo);

    /**
    * @Description: 删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-10-31
    */
    Boolean remove(Long id);
}

