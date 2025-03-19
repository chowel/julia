package com.julia.service;

import com.julia.entity.OrderEntity;
import com.julia.model.dto.DrawerPollDTO;
import com.julia.model.vo.OrderEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chowel
 * @since 2025-03-19
 */

public interface IOrderService extends IService<OrderEntity> {
    /**
    * @Description: 分页查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-03-19
    */
    Page<OrderEntityVO> findForPage(QueryPagement queryPagement);

    /**
    * @Description: 根据id查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-03-19
    */
    OrderEntityVO findOneById(Long id);

    /**
    * @Description: 添加
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-03-19
    */
    DrawerPollDTO saveOrderEntity(OrderEntityVO vo);

    /**
    * @Description: 修改
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-03-19
    */
    Boolean alter(OrderEntityVO vo);

    /**
    * @Description: 删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-03-19
    */
    Boolean remove(Long id);
}

