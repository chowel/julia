package com.julia.service;

import com.julia.entity.GameRoomEntity;
import com.julia.model.AliveGameRo;
import com.julia.model.vo.GameRoomEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
/**
 * <p>
 * 游戏房间 服务类
 * </p>
 *
 * @author chowel
 * @since 2024-09-28
 */

public interface IGameRoomService extends IService<GameRoomEntity> {
    /**
    * @Description: 分页查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-09-28
    */
    Page<GameRoomEntityVO> findForPage(QueryPagement queryPagement);

    /**
    * @Description: 根据id查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-09-28
    */
    GameRoomEntityVO findOneById(Long id);

    GameRoomEntityVO findOneByFlag(String flag);

    /**
    * @Description: 添加
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-09-28
    */
    Boolean saveGameRoomEntity(GameRoomEntityVO vo);


    /**
     * @Description: 添加
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: 2024-09-28
     */
    Boolean joinGameRoomEntity(GameRoomEntityVO vo);

    /**
    * @Description: 修改
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-09-28
    */
    Boolean alter(GameRoomEntityVO vo);

    /**
    * @Description: 删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-09-28
    */
    Boolean remove(Long id);

    AliveGameRo findAliveByUserId(Integer userId);
}

