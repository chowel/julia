package com.julia.service;

import com.julia.entity.MinaSysAdminEntity;
import com.julia.model.dto.LoginDto;
import com.julia.model.vo.MinaSysAdminEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;
/**
 * <p>
 * 账号 服务类
 * </p>
 *
 * @author chowel
 * @since 2025-10-17
 */

public interface IMinaSysAdminService extends IService<MinaSysAdminEntity> {
    /**
    * @Description: 分页查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-17
    */
    Page<MinaSysAdminEntityVO> findForPage(QueryPagement queryPagement);

    /**
    * @Description: 根据id查找
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-17
    */
    MinaSysAdminEntityVO findOneById(Long id);

    MinaSysAdminEntityVO login(LoginDto dto);

    /**
    * @Description: 添加
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-17
    */
    Boolean saveMinaSysAdminEntity(MinaSysAdminEntityVO vo);

    /**
    * @Description: 修改
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-17
    */
    Boolean alter(MinaSysAdminEntityVO vo);

    /**
    * @Description: 删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date: 2025-10-17
    */
    Boolean remove(Long id);
}

