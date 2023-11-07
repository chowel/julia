package com.julia.service;

import com.julia.entity.RocketEntity;
import com.julia.model.dto.CarOperaDTO;
import com.julia.model.vo.CarOrderVO;
import com.julia.model.vo.RocketEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
/**
 * <p>
 * 火箭业务 服务类
 * </p>
 *
 * @author chowel
 * @since 2023-11-01
 */

public interface IRocketService extends IService<RocketEntity> {
    /**
    * @Description: 分页查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2023-11-01
    */
    Page<CarOrderVO> findForPage(QueryPagement queryPagement);

    /**
    * @Description: 根据id查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2023-11-01
    */
    RocketEntityVO findOneById(Long id);

    /**
    * @Description: 添加
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2023-11-01
    */
    Boolean saveRocketEntity(RocketEntityVO vo);

    /**
    * @Description: 修改
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2023-11-01
    */
    Boolean alter(RocketEntityVO vo);

    /**
    * @Description: 删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2023-11-01
    */
    Boolean remove(Long id);

    /**
    * @Description: 车队收单操作
    * @Param:
    * @return:
    * @Author: chowel
    * @Date:
    */
    Boolean carOpera(CarOperaDTO dto);
}

