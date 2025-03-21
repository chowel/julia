package com.julia.service;

import com.julia.entity.GameOrderEntity;
import com.julia.model.dto.DrawerPollDTO;
import com.julia.model.vo.GameOrderEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
/**
 * <p>
 * 游戏充值订单表 服务类
 * </p>
 *
 * @author chowel
 * @since 2025-03-19
 */

public interface IGameOrderService extends IService<GameOrderEntity> {
    /**
    * @Description: 分页查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-03-19
    */
    Page<GameOrderEntityVO> findForPage(QueryPagement queryPagement);

    /**
    * @Description: 根据id查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-03-19
    */
    GameOrderEntityVO findOneById(Long id);

    /**
    * @Description: 添加
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-03-19
    */
    DrawerPollDTO saveGameOrderEntity(GameOrderEntityVO vo);

    /**
    * @Description: 修改
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-03-19
    */
    Boolean alter(GameOrderEntityVO vo);

    /**
    * @Description: 删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-03-19
    */
    Boolean remove(Long id);
}

