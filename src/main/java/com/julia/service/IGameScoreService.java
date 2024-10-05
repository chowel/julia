package com.julia.service;

import com.julia.entity.GameScoreEntity;
import com.julia.model.vo.GameScoreEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
/**
 * <p>
 * 游戏得分 服务类
 * </p>
 *
 * @author chowel
 * @since 2024-10-03
 */

public interface IGameScoreService extends IService<GameScoreEntity> {
    /**
    * @Description: 分页查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-10-03
    */
    Page<GameScoreEntityVO> findForPage(QueryPagement queryPagement);

    /**
    * @Description: 根据id查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-10-03
    */
    GameScoreEntityVO findOneById(Long id);

    /**
    * @Description: 添加
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-10-03
    */
    Boolean saveGameScoreEntity(GameScoreEntityVO vo);

    /**
    * @Description: 修改
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-10-03
    */
    Boolean alter(GameScoreEntityVO vo);

    /**
    * @Description: 删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-10-03
    */
    Boolean remove(Long id);

    Boolean saveGamePlayer(String gameNo,Integer userId, Long gameId);
}

