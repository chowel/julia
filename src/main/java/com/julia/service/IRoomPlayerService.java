package com.julia.service;

import com.julia.entity.RoomPlayerEntity;
import com.julia.model.vo.RoomPlayerEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;

/**
 * <p>
 * 房间玩家 服务类
 * </p>
 *
 * @author chowel
 * @since 2024-09-30
 */

public interface IRoomPlayerService extends IService<RoomPlayerEntity> {
    /**
     * @Description: 分页查找
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: 2024-09-30
     */
    Page<RoomPlayerEntityVO> findForPage(QueryPagement queryPagement);

    /**
     * @Description: 根据id查找
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: 2024-09-30
     */
    RoomPlayerEntityVO findOneById(Long id);

    /**
     * @Description: 添加
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: 2024-09-30
     */
    Boolean saveRoomPlayerEntity(RoomPlayerEntityVO vo);

    /**
     * @Description: 修改
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: 2024-09-30
     */
    Boolean alter(RoomPlayerEntityVO vo);

    /**
     * @Description: 玩家离开房间
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    Boolean playerSetOnline(Long playerId,int onLine,String roomFlag);

    /**
     * @Description: 删除
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: 2024-09-30
     */
    Boolean remove(Long id);
}

