package com.julia.service;

import com.julia.entity.PlayersEntity;
import com.julia.model.vo.PlayersEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author chowel
 * @since 2025-09-28
 */

public interface IPlayersService extends IService<PlayersEntity> {
    /**
    * @Description: 分页查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-09-28
    */
    Page<PlayersEntityVO> findForPage(QueryPagement queryPagement);

    /**
    * @Description: 根据id查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-09-28
    */
    PlayersEntityVO findOneById(Long id);

    /**
    * @Description: 添加
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-09-28
    */
    Boolean savePlayersEntity(PlayersEntityVO vo);

    /**
    * @Description: 修改
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-09-28
    */
    Boolean alter(PlayersEntityVO vo);

    /**
    * @Description: 删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-09-28
    */
    Boolean remove(Long id);
}

