package com.julia.service;

import com.julia.entity.PactEntity;
import com.julia.model.QueryPagement;
import com.julia.model.vo.PactEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 菜单/接口权限項 服务类
 * </p>
 *
 * @author chowel
 * @since 2023-11-01
 */

public interface IPactService extends IService<PactEntity> {
    /**
    * @Description: 分页查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2023-11-01
    */
    Page<PactEntityVO> findForPage(QueryPagement queryPagement);

    /**
    * @Description: 根据id查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2023-11-01
    */
    PactEntityVO findOneById(Long id);

    /**
    * @Description: 添加
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2023-11-01
    */
    Boolean savePactEntity(PactEntityVO vo);

    /**
    * @Description: 修改
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2023-11-01
    */
    Boolean alter(PactEntityVO vo);

    /**
    * @Description: 删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2023-11-01
    */
    Boolean remove(Long id);

    List<PactEntityVO> findPactById(int id);
}

