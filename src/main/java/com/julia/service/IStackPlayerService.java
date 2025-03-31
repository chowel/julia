package com.julia.service;

import com.julia.entity.StackPlayerEntity;
import com.julia.model.dto.LoginDto;
import com.julia.model.vo.StackPlayerEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;

/**
 * <p>
 * 玩家 服务类
 * </p>
 *
 * @author chowel
 * @since 2025-03-09
 */

public interface IStackPlayerService extends IService<StackPlayerEntity> {
    /**
     * @Description: 分页查找
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: 2025-03-09
     */
    Page<StackPlayerEntityVO> findForPage(QueryPagement queryPagement);

    /**
     * @Description: 根据id查找
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: 2025-03-09
     */
    StackPlayerEntityVO findOneById(Long id);

    /**
     * @Description: 添加
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: 2025-03-09
     */
    Boolean saveStackPlayerEntity(LoginDto dto);

    /**
     * @Description: 修改
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: 2025-03-09
     */
    Boolean alter(StackPlayerEntityVO vo);

    /**
     * @Description: 删除
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: 2025-03-09
     */
    Boolean remove(Long id);

    /**
     * @Description: 玩家登录
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    StackPlayerEntityVO playerLogin(LoginDto dto);


    StackPlayerEntity findPlayerForPay();
}

