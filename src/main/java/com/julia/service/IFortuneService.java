package com.julia.service;

import com.julia.entity.FortuneEntity;
import com.julia.entity.MetricsFortuneEntity;
import com.julia.model.dto.FortuneDTO;
import com.julia.model.dto.HandOutDTO;
import com.julia.model.vo.FortuneEntityVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.model.QueryPagement;

import java.util.List;

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
     * @Description: 多条件分页查找
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: 2024-02-15
     */
    Page<FortuneEntityVO> queryPage(QueryPagement queryPagement);

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
    String handIn(FortuneDTO dto, int pid);

    /**
     * @Description: 车队分发收款
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    Boolean handOut(HandOutDTO dto, int cid);

    /**
     * @Description: 收款结果
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    Boolean overFortune(FortuneEntityVO dto);

    /**
     * @Description: 重新回调
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    Boolean callBack(FortuneDTO dto, int pid);

    /**
     * @Description: 收单统计
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    List<MetricsFortuneEntity> mertric(QueryPagement queryPagement);

    /**
     * @Description: 车队拒绝财神
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    Boolean refuse(FortuneDTO dto, int carId);
    /**
    * @Description: 主动下线
    * @Param:
    * @return:
    * @Author: chowel
    * @Date:
    */
    Boolean handClose(String carId);
    /**
    * @Description:  获取米
    * @Param:
    * @return:
    * @Author: chowel
    * @Date:
    */
    Integer getCoins(int yaoId);
    /** 
    * @Description: 在线车队 
    * @Param:  
    * @return:  
    * @Author: chowel 
    * @Date:  
    */
    List<String> alives();

    Boolean expiredCallback(FortuneEntity fortuneEntity);
}

