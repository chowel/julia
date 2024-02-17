package com.julia.service;

import com.julia.entity.FortuneEntity;
import com.julia.model.dto.FortuneDTO;
import com.julia.model.vo.FortuneEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;

/**
 * <p>
 * 财神 服务类
 * </p>
 *
 * @author chowel
 * @since 2024-02-15
 */

public interface IFortuneService extends IService<FortuneEntity> {
    /**
     * @Description: 分页查找
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: 2024-02-15
     */
    Page<FortuneEntityVO> findForPage(QueryPagement queryPagement);

    /**
     * @Description: 根据id查找
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: 2024-02-15
     */
    FortuneEntityVO findOneById(Long id);

    /**
     * @Description: 添加
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: 2024-02-15
     */
    Boolean saveFortuneEntity(FortuneEntityVO vo);

    /**
     * @Description: 修改
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: 2024-02-15
     */
    Boolean alter(FortuneEntityVO vo);

    /**
     * @Description: 删除
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: 2024-02-15
     */
    Boolean remove(Long id);

    /**
     * @Description: 输入订单
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    Boolean input(FortuneDTO dto, int pid);
}

