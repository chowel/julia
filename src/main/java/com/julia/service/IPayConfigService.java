package com.julia.service;

import com.julia.entity.PayConfigEntity;
import com.julia.model.vo.PayConfigEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chowel
 * @since 2025-03-15
 */

public interface IPayConfigService extends IService<PayConfigEntity> {
    /**
    * @Description: 分页查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-03-15
    */
    Page<PayConfigEntityVO> findForPage(QueryPagement queryPagement);

    List<PayConfigEntityVO> allconfig();


    /**
    * @Description: 根据id查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-03-15
    */
    PayConfigEntityVO findOneById(Long id);


    PayConfigEntity findOneByPrice(Long price);

    /**
    * @Description: 添加
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-03-15
    */
    Boolean savePayConfigEntity(PayConfigEntityVO vo);

    /**
    * @Description: 修改
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-03-15
    */
    Boolean alter(PayConfigEntityVO vo);

    /**
    * @Description: 删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-03-15
    */
    Boolean remove(Long id);
}

