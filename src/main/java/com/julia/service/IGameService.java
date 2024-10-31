package com.julia.service;

import com.julia.entity.GameEntity;
import com.julia.model.dto.ReceivePokerDto;
import com.julia.model.vo.GameEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
import com.julia.tool.PokerMoldForFive;

import java.util.List;

/**
 * <p>
 * 游戏 服务类
 * </p>
 *
 * @author chowel
 * @since 2024-10-03
 */

public interface IGameService extends IService<GameEntity> {
    /**
    * @Description: 分页查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-10-03
    */
    Page<GameEntityVO> findForPage(QueryPagement queryPagement);

    /**
    * @Description: 根据id查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-10-03
    */
    GameEntityVO findOneById(Long id);

    GameEntity findGameByRoom(int gameType,String roomIde);

    GameEntity findGameByNo(String gameNo);

    /**
    * @Description: 添加
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-10-03
    */
    Boolean saveGameEntity(GameEntityVO vo);

    /**
    * @Description: 修改
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-10-03
    */
    Boolean alter(GameEntityVO vo);

    /**
    * @Description: 删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2024-10-03
    */
    Boolean remove(Long id);

    List<PokerMoldForFive> receive(ReceivePokerDto dto);

    void countScore(String gameNo,String roomIde);

    void createThirteennGame(int gameType, String roomIde);

    Boolean rePlayThirteennGame(int userId,int gameType, String roomIde);
}

