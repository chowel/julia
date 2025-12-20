package com.julia.service;

import com.julia.entity.MinaGameEntity;
import com.julia.model.game.MinaGame;
import com.julia.model.vo.MinaGameEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;

import java.util.List;

/**
 * <p>
 * Game 服务类
 * </p>
 *
 * @author chowel
 * @since 2025-10-27
 */

public interface IMinaGameService extends IService<MinaGameEntity> {
    /**
    * @Description: 分页查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-27
    */
    Page<MinaGameEntityVO> findForPage(QueryPagement queryPagement);

    /**
    * @Description: 根据id查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-27
    */
    MinaGameEntityVO findOneById(Long id);

    /**
    * @Description: 添加
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-27
    */
    Boolean saveMinaGameEntity(MinaGameEntityVO vo);

    /**
    * @Description: 修改
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-27
    */
    Boolean alter(MinaGameEntityVO vo);

    /**
    * @Description: 删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-27
    */
    Boolean remove(Long id);
    /**
    * @Description:
    * @Param:  len 生成几局
    * @return:
    * @Author: chowel
    * @Date:
    */
//    List<MinaGame> genGameByPlayerId(Long playerId,int len,int type);

}

