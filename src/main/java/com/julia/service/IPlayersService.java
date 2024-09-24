package com.julia.service;

import com.julia.entity.PlayersEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
import com.julia.model.vo.PlayersEntityVO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author chowel
 * @since 2024-09-24
 */

public interface IPlayersService extends IService<PlayersEntity> {
    /**
    * @Description: 分页查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-09-24
    */
    Page<PlayersEntityVO> findForPage(QueryPagement queryPagement);

    /**
    * @Description: 根据id查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-09-24
    */
    PlayersEntityVO findOneById(Long id);

    /**
    * @Description: 添加
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-09-24
    */
    Boolean savePlayersEntity(PlayersEntityVO vo);

    /**
    * @Description: 修改
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-09-24
    */
    Boolean alter(PlayersEntityVO vo);

    /**
    * @Description: 删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-09-24
    */
    Boolean remove(Long id);
}

