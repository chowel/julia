package com.julia.service;

import com.julia.entity.YaoClientEntity;
import com.julia.model.vo.YaoClientEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chowel
 * @since 2025-10-04
 */

public interface IYaoClientService extends IService<YaoClientEntity> {
    /**
    * @Description: 分页查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-04
    */
    Page<YaoClientEntityVO> findForPage(QueryPagement queryPagement);

    /**
    * @Description: 根据id查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-04
    */
    YaoClientEntityVO findOneById(Long id);

    /**
    * @Description: 添加
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-04
    */
    Boolean saveYaoClientEntity(YaoClientEntityVO vo);

    /**
    * @Description: 修改
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-04
    */
    Boolean alter(YaoClientEntityVO vo);

    /**
    * @Description: 删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-04
    */
    Boolean remove(Long id);

    Boolean saveFromConnect(String secure,String hostname);
}

