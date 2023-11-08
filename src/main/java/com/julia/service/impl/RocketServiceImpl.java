package com.julia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.RocketEntity;
import com.julia.entity.YaoEntity;
import com.julia.mapper.RocketMapper;
import com.julia.mapper.YaoMapper;
import com.julia.model.dto.CarOperaDTO;
import com.julia.model.dto.InputRocketDTO;
import com.julia.model.dto.InputRocketListDTO;
import com.julia.model.vo.CarOrderVO;
import com.julia.service.IRocketService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.julia.tool.JuliaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import com.julia.model.vo.RocketEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 火箭业务 服务实现类
 * </p>
 *
 * @author chowel
 * @since 2023-11-01
 */
@Service
public class RocketServiceImpl extends ServiceImpl<RocketMapper, RocketEntity> implements IRocketService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    YaoMapper yaoMapper;

    @Resource
    private RestTemplate restTemplate;

    @Value("${sign.salt}")
    private String SIGNSALT;

    @Override
    public Page<CarOrderVO> findForPage(QueryPagement queryPagement) {
        int status = -1;
        int cId = 0;
        int pId = 0;

        Map<String, Object> searchFields = queryPagement.getSearchFields();
        if (searchFields.containsKey("status")) {
            status = (int) queryPagement.getSearchFields().get("status");
        }
        if (searchFields.containsKey("cId")) {
            cId = (int) queryPagement.getSearchFields().get("cId");
        }
        if (searchFields.containsKey("pId")) {
            pId = (int) queryPagement.getSearchFields().get("pId");
        }

        Page<RocketEntity> p = new LambdaQueryChainWrapper<RocketEntity>(getBaseMapper())
                .eq(status > -1, RocketEntity::getStatus, status)
                .eq(cId > 0, RocketEntity::getCId, cId)
                .eq(pId > 0, RocketEntity::getPId, pId)
                .page(new Page<RocketEntity>(queryPagement.getStartPage(), queryPagement.getPageSize()));
        Page<CarOrderVO> page = JuliaUtils.convertTo(new Page<CarOrderVO>(), p);
        page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new CarOrderVO(), e)).collect(Collectors.toList()));
        return page;
    }

    @Override
    public RocketEntityVO findOneById(Long id) {
        RocketEntity entity = getById(id);
        return JuliaUtils.convertTo(new RocketEntityVO(), entity);
    }

    @Override
    public Boolean saveRocketEntity(RocketEntityVO vo) {
        return save(JuliaUtils.convertTo(new RocketEntity(), vo));
    }

    @Override
    public Boolean alter(RocketEntityVO vo) {
        return updateById(JuliaUtils.convertTo(new RocketEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
        return removeById(id);
    }

    @Override
    public Boolean carOpera(CarOperaDTO dto) {
        RocketEntity entity = getById(dto.getRocketId());
        if (ObjectUtils.isEmpty(entity)) {
            throw new JuliaException("操作异常");
        }
        entity.setRealPay(dto.getRealPay());
        entity.setCId(dto.getCId());

        YaoEntity pYao = yaoMapper.selectById(entity.getPId());
//        String callBack_url = pYao.getCallback();
        //  回调逻辑
        Map<String, Object> params = new HashMap<>(5);
        String sign = SIGNSALT + entity.getOrderId() + entity.getDoneTime();
        params.put("orderNo", entity.getOrderId());
        params.put("amount", entity.getAmount());
        params.put("orderStatus", entity.getStatus());
        params.put("payTime", entity.getDoneTime());
        params.put("sign", DigestUtils.md5DigestAsHex(sign.getBytes(StandardCharsets.UTF_8)));

        handleCallBack(pYao.getCallback(),params);
        entity.setStatus(1);
        entity.setDoneTime(System.currentTimeMillis());
        return updateById(entity);
    }

    @Override
    public Boolean inputRocketBatch(InputRocketListDTO inputRocketListDTO, int pId) {
        List<RocketEntity> rockets = inputRocketListDTO.getList().stream()
                .map(e -> {
                    RocketEntity entity = JuliaUtils.convertTo(new RocketEntity(), e);
                    entity.setPId(pId);
                    return entity;
                })
                .collect(Collectors.toList());
        return saveBatch(rockets, 100);
    }

    @Override
    public Boolean inputRocket(InputRocketDTO dto, int pId) {
        RocketEntity entity = JuliaUtils.convertTo(new RocketEntity(), dto);
        entity.setPId(pId);
        return save(entity);
    }

    @Override
    public CarOrderVO queryRocketByOrderId(String orderId, int pId) {
        RocketEntity entity = getOne(new QueryWrapper<RocketEntity>().eq("p_id", pId).eq("order_id", orderId));
        if (ObjectUtils.isEmpty(entity)) {
            throw new JuliaException("订单不存在");
        }
        return JuliaUtils.convertTo(new CarOrderVO(), entity);
    }

    @Override
    public Boolean noticeRocketByOrderId(String orderId, int pId) {
        RocketEntity entity = getOne(new QueryWrapper<RocketEntity>().eq("p_id", pId).eq("order_id", orderId));
        if (ObjectUtils.isEmpty(entity)) {
            throw new JuliaException("订单不存在");
        }
        YaoEntity yao = yaoMapper.selectById(entity.getPId());

        Map<String, Object> params = new HashMap<>(5);
        String sign = SIGNSALT + entity.getOrderId() + entity.getDoneTime();
        params.put("orderNo", entity.getOrderId());
        params.put("amount", entity.getAmount());
        params.put("orderStatus", entity.getStatus());
        params.put("payTime", entity.getDoneTime());
        params.put("sign", DigestUtils.md5DigestAsHex(sign.getBytes(StandardCharsets.UTF_8)));

        handleCallBack(yao.getCallback(),params);
        return true;
    }

    protected String handleCallBack(String url,Map<String, Object> params){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            logger.info("回调友商成功!");
        }
        return "";
    }


}

