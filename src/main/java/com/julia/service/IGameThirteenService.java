package com.julia.service;

import com.julia.entity.GameThirteenEntity;
import com.julia.model.vo.GameThirteenEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;

import java.util.List;

/**
 * <p>
 * 13游戏详情 服务类
 * </p>
 *
 * @author chowel
 * @since 2024-10-10
 */

public interface IGameThirteenService extends IService<GameThirteenEntity> {
    /**
    * @Description: 分页查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-10-10
    */
    Page<GameThirteenEntityVO> findForPage(QueryPagement queryPagement);

    /**
    * @Description: 根据id查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-10-10
    */
    GameThirteenEntityVO findOneById(Long id);

    /**
    * @Description: 添加
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-10-10
    */
    Boolean saveGameThirteenEntity(GameThirteenEntityVO vo);

    /**
    * @Description: 修改
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-10-10
    */
    Boolean alter(GameThirteenEntityVO vo);

    /**
    * @Description: 删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-10-10
    */
    Boolean remove(Long id);

    List<GameThirteenEntity> findByGameNo(String gameNo);

    GameThirteenEntity findOneByGameNoWhitPlayerId(Long playerId,String gameNo);
}

