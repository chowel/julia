package com.julia.service;

import com.julia.entity.MinaSysPowerEntity;
import com.julia.model.vo.MinaSysPowerEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
/**
 * <p>
 * 系统角色 服务类
 * </p>
 *
 * @author chowel
 * @since 2025-10-17
 */

public interface IMinaSysPowerService extends IService<MinaSysPowerEntity> {
    /**
    * @Description: 分页查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-17
    */
    Page<MinaSysPowerEntityVO> findForPage(QueryPagement queryPagement);

    /**
    * @Description: 根据id查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-17
    */
    MinaSysPowerEntityVO findOneById(Long id);

    /**
    * @Description: 添加
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-17
    */
    Boolean saveMinaSysPowerEntity(MinaSysPowerEntityVO vo);

    /**
    * @Description: 修改
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-17
    */
    Boolean alter(MinaSysPowerEntityVO vo);

    /**
    * @Description: 删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-17
    */
    Boolean remove(Long id);
}

