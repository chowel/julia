package com.julia.service;

import com.julia.entity.CoinLogEntity;
import com.julia.model.vo.CoinLogEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
/**
 * <p>
 * 车队加进记录 服务类
 * </p>
 *
 * @author chowel
 * @since 2023-11-23
 */

public interface ICoinLogService extends IService<CoinLogEntity> {
    /**
    * @Description: 分页查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2023-11-23
    */
    Page<CoinLogEntityVO> findForPage(QueryPagement queryPagement);

    /**
    * @Description: 根据id查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2023-11-23
    */
    CoinLogEntityVO findOneById(Long id);

    /**
    * @Description: 添加
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2023-11-23
    */
    Boolean saveCoinLogEntity(CoinLogEntityVO vo);

    /**
    * @Description: 修改
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2023-11-23
    */
    Boolean alter(CoinLogEntityVO vo);

    /**
    * @Description: 删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2023-11-23
    */
    Boolean remove(Long id);
}

