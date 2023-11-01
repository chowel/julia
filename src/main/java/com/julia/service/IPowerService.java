package com.julia.service;

import com.julia.entity.PowerEntity;
import com.julia.model.vo.PowerEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author chowel
 * @since 2023-11-01
 */

public interface IPowerService extends IService<PowerEntity> {
    /**
    * @Description: 分页查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2023-11-01
    */
    Page<PowerEntityVO> findForPage(QueryPagement queryPagement);

    /**
    * @Description: 根据id查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2023-11-01
    */
    PowerEntityVO findOneById(Long id);

    /**
    * @Description: 添加
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2023-11-01
    */
    Boolean savePowerEntity(PowerEntityVO vo);

    /**
    * @Description: 修改
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2023-11-01
    */
    Boolean alter(PowerEntityVO vo);

    /**
    * @Description: 删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2023-11-01
    */
    Boolean remove(Long id);
}

