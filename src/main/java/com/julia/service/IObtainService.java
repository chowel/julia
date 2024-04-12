package com.julia.service;

import com.julia.entity.ObtainEntity;
import com.julia.model.vo.ObtainEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;

import java.util.List;

/**
 * <p>
 * 车队下账号 服务类
 * </p>
 *
 * @author chowel
 * @since 2024-02-23
 */

public interface IObtainService extends IService<ObtainEntity> {
    /**
    * @Description: 分页查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-02-23
    */
    Page<ObtainEntityVO> findForPage(QueryPagement queryPagement);

    /**
    * @Description: 根据id查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-02-23
    */
    ObtainEntityVO findOneById(Long id);

    /**
    * @Description: 添加
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-02-23
    */
    Boolean saveObtainEntity(ObtainEntityVO vo);

    /**
    * @Description: 修改
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-02-23
    */
    Boolean alter(ObtainEntityVO vo);

    /**
    * @Description: 删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-02-23
    */
    Boolean remove(Integer id);
    /** 
    * @Description: 查找全部账号byCar 
    * @Param:  
    * @return:  
    * @Author: chowel 
    * @Date:  
    */
    List<ObtainEntityVO> allObtain(int carId);
    /** 
    * @Description: 每日计数清零 
    * @Param:  
    * @return:  
    * @Author: chowel 
    * @Date:  
    */
    Boolean clearCout(int carId);
}

