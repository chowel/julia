package com.julia.service;

import com.julia.entity.YaoEntity;
import com.julia.model.dto.LoginDto;
import com.julia.model.dto.MidPasswordDto;
import com.julia.model.vo.YaoEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;

/**
 * <p>
 * 账号 服务类
 * </p>
 *
 * @author chowel
 * @since 2023-11-01
 */

public interface IYaoService extends IService<YaoEntity> {
    /**
     * @Description: 分页查找
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: 2023-11-01
     */
    Page<YaoEntityVO> findForPage(QueryPagement queryPagement);

    /**
     * @Description: 根据id查找
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: 2023-11-01
     */
    YaoEntityVO findOneById(Long id);

    /**
     * @Description: 添加
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: 2023-11-01
     */
    Boolean saveYaoEntity(YaoEntityVO vo);

    /**
     * @Description: 修改
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: 2023-11-01
     */
    Boolean alter(YaoEntityVO vo);

    /**
     * @Description: 删除
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: 2023-11-01
     */
    Boolean remove(Long id);

    /**
     * @Description: login
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    YaoEntityVO login(LoginDto dto);

    /**
     * @Description: 个人信息
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    YaoEntityVO mySelf(Integer id);

    /**
     * @Description: 更新token
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    String createToken(Integer id);


    Boolean alterPassword(MidPasswordDto dto);

}

