package com.julia.service;

import com.julia.entity.MinaSysMenuEntity;
import com.julia.model.vo.MinaSysMenuEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
import com.julia.model.vo.PactEntityVO;

import java.util.List;

/**
 * <p>
 * 系统菜单 服务类
 * </p>
 *
 * @author chowel
 * @since 2025-10-17
 */

public interface IMinaSysMenuService extends IService<MinaSysMenuEntity> {
    /**
    * @Description: 分页查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-17
    */
    Page<MinaSysMenuEntityVO> findForPage(QueryPagement queryPagement);

    /**
    * @Description: 根据id查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-17
    */
    MinaSysMenuEntityVO findOneById(Long id);

    /**
    * @Description: 添加
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-17
    */
    Boolean saveMinaSysMenuEntity(MinaSysMenuEntityVO vo);

    /**
    * @Description: 修改
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-17
    */
    Boolean alter(MinaSysMenuEntityVO vo);

    /**
    * @Description: 删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-17
    */
    Boolean remove(Long id);

    List<MinaSysMenuEntityVO> findMenusById(int id);
}

