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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
    WebSocketService webSocketService;

    @Resource
    private RestTemplate restTemplate;

    @Value("${sign.salt}")
    private String SIGNSALT;

    @Value("${file.uploadurl}")
    private String uploadPath;


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
    @Transactional
    public Boolean carOpera(CarOperaDTO dto) {

        YaoEntity cYao = yaoMapper.selectById(dto.getCId());
        int cyaoCoin = cYao.getCoin();
        if (cyaoCoin < dto.getRealPay()) {
            throw new JuliaException("车队押金不足");
        }
        cYao.setCoin(cyaoCoin - dto.getRealPay());
        yaoMapper.updateById(cYao);

        RocketEntity entity = getById(dto.getRocketId());
        if (ObjectUtils.isEmpty(entity)) {
            throw new JuliaException("操作异常");
        }
        entity.setRealPay(dto.getRealPay());
        entity.setCId(dto.getCId());
        entity.setStatus(dto.getFlag());
        if (StringUtils.hasLength(dto.getMsg())) {
            entity.setMsg(dto.getMsg());
        }
        entity.setDoneTime(System.currentTimeMillis());

        webSocketService.handleCarOpera(cYao,entity);

        YaoEntity pYao = yaoMapper.selectById(entity.getPId());

        //  回调逻辑
        Map<String, Object> params = new HashMap<>(5);
        String sign = SIGNSALT + entity.getOrderId() + entity.getDoneTime();
        params.put("orderNo", entity.getOrderId());
        params.put("amount", entity.getAmount());
        params.put("realPay", entity.getRealPay());
        params.put("orderStatus", entity.getStatus());
        params.put("msg", dto.getMsg());
        params.put("payTime", entity.getDoneTime());
        params.put("sign", DigestUtils.md5DigestAsHex(sign.getBytes(StandardCharsets.UTF_8)));


        String callbackReturn = handleCallBack(pYao.getCallback(), params);
        logger.info("回调返回: " + callbackReturn);
        if ("success".equals(callbackReturn)) {
            entity.setCheckCallback(1);
        } else {
            entity.setCheckCallback(2);
        }
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

        String callbackReturn = handleCallBack(yao.getCallback(), params);
        if ("success".equals(callbackReturn)) {
            if (entity.getCheckCallback() != 1) {
                entity.setCheckCallback(1);
                entity.setDoneTime(System.currentTimeMillis());
                this.updateById(entity);
            }
        }
        return true;
    }

    @Override
    public Boolean Deposit(MultipartFile file, InputRocketDTO dto) {
        if (file.isEmpty()) {
            throw new JuliaException("请选择文件！");
        }
        String imageUrl;
        String fileLateName;
        try {
            // 获取文件名
            String oldFileName = file.getOriginalFilename();
            fileLateName = oldFileName.substring(oldFileName.lastIndexOf("."));


            String realyFileName = UUID.randomUUID().toString().replace("-", "")
                    + fileLateName;

            logger.info(realyFileName);
            File fileDir = new File(uploadPath + "/");
            if (!fileDir.isDirectory()) {
                //递归生成文件夹
                fileDir.mkdirs();
            }
            file.transferTo(new File(fileDir, realyFileName));

            RocketEntity rocket = new RocketEntity();
            rocket.setPId(dto.getPId());
            rocket.setAmount(dto.getAmount());
            rocket.setOrderId(dto.getOrderId());
            rocket.setFirstName(dto.getFirstName());
            if (StringUtils.hasLength(dto.getLastName())) {
                rocket.setLastName(dto.getLastName());
            }
            rocket.setUrl(realyFileName);
            save(rocket);

            RocketEntity saved = this.getById(rocket.getRocketId());

//            webSocketService.handleDeposit(saved);

        } catch (Exception e) {
            e.printStackTrace();
            throw new JuliaException("文件上传失败！");
        }

        return true;
    }

    protected String handleCallBack(String url, Map<String, Object> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            logger.info(response.getBody());
            return response.getBody();
        }
        return null;
    }


}

