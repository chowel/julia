package com.julia.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.FortuneEntity;
import com.julia.entity.MetricsFortuneEntity;
import com.julia.entity.YaoEntity;
import com.julia.mapper.FortuneMapper;
import com.julia.mapper.YaoMapper;
import com.julia.model.dto.FortuneDTO;
import com.julia.model.dto.HandOutDTO;
import com.julia.service.IFortuneService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.julia.tool.JuliaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import com.julia.model.vo.FortuneEntityVO;
import com.julia.tool.JuliaUtils;
import com.julia.model.QueryPagement;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 财神 服务实现类
 * </p>
 *
 * @author chowel
 * @since 2024-02-15
 */
@Service
public class FortuneServiceImpl extends ServiceImpl<FortuneMapper, FortuneEntity> implements IFortuneService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    WebSocketService webSocketService;

    @Resource
    YaoMapper yaoMapper;

    @Resource
    private RestTemplate restTemplate;

    @Value("${sign.salt}")
    private String SIGNSALT;

    @Override
    public Page<FortuneEntityVO> findForPage(QueryPagement queryPagement) {
        Page<FortuneEntity> p = null;

        Map<String, Object> sf = queryPagement.getSearchFields();

        if (ObjectUtils.isEmpty(sf)) {
            p = new LambdaQueryChainWrapper<FortuneEntity>(getBaseMapper()).page(new Page<FortuneEntity>(queryPagement.getStartPage(),
                    queryPagement.getPageSize()));
        } else {
            p = new LambdaQueryChainWrapper<FortuneEntity>(getBaseMapper())
                    .eq(FortuneEntity::getCId, sf.get("cid"))
                    .page(new Page<FortuneEntity>(queryPagement.getStartPage(),
                            queryPagement.getPageSize()));
        }

        Page<FortuneEntityVO> page = JuliaUtils.convertTo(new Page<FortuneEntityVO>(), p);
        page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new FortuneEntityVO(), e)).collect(Collectors.toList()));
        return page;
    }

    @Override
    public Page<FortuneEntityVO> queryPage(QueryPagement queryPagement) {

        Map<String, Object> sf = queryPagement.getSearchFields();
        int panid = ObjectUtils.isEmpty(sf.get("panid")) ? -1 : (int) sf.get("panid");
        int carid = ObjectUtils.isEmpty(sf.get("carid")) ? -1 : (int) sf.get("carid");
        Page<FortuneEntity> p = new LambdaQueryChainWrapper<FortuneEntity>(getBaseMapper())
                .eq(panid > 0, FortuneEntity::getPId, panid)
                .eq(carid > 0, FortuneEntity::getCId, carid)
                .eq((int) sf.get("status") > -1, FortuneEntity::getStatus, sf.get("status"))
                .eq((int) sf.get("callback") > -1, FortuneEntity::getCheckCallback, sf.get("callback"))
                .eq(StringUtils.hasLength((String) sf.get("drawer")), FortuneEntity::getDrawer, sf.get("drawer"))
                .eq(StringUtils.hasLength((String) sf.get("payid")), FortuneEntity::getPayId, sf.get("payid"))
                .like(StringUtils.hasLength((String) sf.get("orderid")), FortuneEntity::getOrderId, sf.get("orderid"))
                .between(StringUtils.hasLength((String) sf.get("st")), FortuneEntity::getCreateTime, (String) sf.get("st"),
                        (String) sf.get("et"))
                .page(new Page<FortuneEntity>(queryPagement.getStartPage(),
                        queryPagement.getPageSize()));
        Page<FortuneEntityVO> page = JuliaUtils.convertTo(new Page<FortuneEntityVO>(), p);
        page.setRecords(p.getRecords().stream().map(e -> JuliaUtils.convertTo(new FortuneEntityVO(), e)).collect(Collectors.toList()));

        return page;
    }

    @Override
    public FortuneEntityVO findOneById(Long id) {
        FortuneEntity entity = getById(id);
        return JuliaUtils.convertTo(new FortuneEntityVO(), entity);
    }

    @Override
    public Boolean saveFortuneEntity(FortuneEntityVO vo) {
        return save(JuliaUtils.convertTo(new FortuneEntity(), vo));
    }

    @Override
    public Boolean alter(FortuneEntityVO vo) {
        return updateById(JuliaUtils.convertTo(new FortuneEntity(), vo));
    }

    @Override
    public Boolean remove(Long id) {
        return removeById(id);
    }

    @Override
    public Boolean handIn(FortuneDTO dto, int pid) {
        FortuneEntity entity = new FortuneEntity();
        entity.setAmount(dto.getAmount());
        entity.setPId(pid);
        entity.setOrderId(dto.getOrderId());
        entity.setDrawer(dto.getDrawer());

        if (save(entity)) {
            webSocketService.hanldeFortune(entity);
            return true;
        }
        return false;
    }

    @Override
    public Boolean handOut(HandOutDTO dto, int cid) {
        FortuneEntity entity = getById(dto.getFortuneId());
        if (!ObjectUtils.isEmpty(entity)) {
            entity.setCId(cid);
            entity.setPayId(dto.getPid());
            entity.setStatus(1);
            entity.setHandoutTime(System.currentTimeMillis());
            updateById(entity);
            // 处理redis中数据
            webSocketService.handOutRedis(entity);
            return true;
        }
        return false;
    }

    @Override
    public Boolean overFortune(FortuneEntityVO dto) {
        FortuneEntity entity = getById(dto.getFortuneId());
        if (!ObjectUtils.isEmpty(entity)) {
            entity.setStatus(dto.getStatus());
            if (dto.getStatus() == 2) {
                entity.setMsg(dto.getMsg());
            }

//            entity.setDoneTime(System.currentTimeMillis());

            long ct = System.currentTimeMillis();

            YaoEntity pan = yaoMapper.selectById(entity.getPId());

            Map<String, Object> params = new HashMap<>(5);
            String sign = SIGNSALT + entity.getOrderId() + entity.getDoneTime();
            params.put("orderNo", entity.getOrderId());
            params.put("amount", entity.getAmount());
            params.put("orderStatus", entity.getStatus());
            params.put("payTime", ct);
            params.put("sign", DigestUtils.md5DigestAsHex(sign.getBytes(StandardCharsets.UTF_8)));

            String callbackReturn = handleCallBack(pan.getCallback(), params);
            if ("success".equals(callbackReturn)) {
                if (entity.getCheckCallback() != 1) {
                    entity.setCheckCallback(1);
                    entity.setDoneTime(ct);
                }
            } else {
                entity.setCheckCallback(2);
            }

            YaoEntity car = yaoMapper.selectById(entity.getCId());

            car.setCoin(car.getCoin() - entity.getAmount());

            if (car.getCoin() < 0) {
                car.setCoin(0);
            }

            updateById(entity);
            return true;
        }
        return false;
    }

    @Override
    public Boolean callBack(FortuneDTO dto, int pid) {
        YaoEntity pan = yaoMapper.selectById(pid);
        if (ObjectUtils.isEmpty(pan)) {
            throw new JuliaException("盘方不存在");
        }
        FortuneEntity fortune =
                new LambdaQueryChainWrapper<FortuneEntity>(getBaseMapper()).eq(FortuneEntity::getOrderId, dto.getOrderId()).one();
        if (ObjectUtils.isEmpty(fortune)) {
            throw new JuliaException("订单异常");
        }
        if (fortune.getStatus() == 0 || fortune.getStatus() == 1 || fortune.getCheckCallback() != 0) {
            throw new JuliaException("不可发起");
        }


        Map<String, Object> params = new HashMap<>(5);
        String sign = SIGNSALT + fortune.getOrderId() + fortune.getDoneTime();
        params.put("orderNo", fortune.getOrderId());
        params.put("amount", fortune.getAmount());
        params.put("orderStatus", fortune.getStatus());
        params.put("payTime", System.currentTimeMillis());
        params.put("sign", DigestUtils.md5DigestAsHex(sign.getBytes(StandardCharsets.UTF_8)));

        String callbackReturn = handleCallBack(pan.getCallback(), params);

        if ("success".equals(callbackReturn)) {
            if (fortune.getCheckCallback() != 1) {
                fortune.setCheckCallback(1);
                return updateById(fortune);
            }
        }
        return false;

    }

    @Override
    public List<MetricsFortuneEntity> mertric(QueryPagement queryPagement) {
        Map<String, Object> sf = queryPagement.getSearchFields();
        return getBaseMapper().metricsfortune((String) sf.get("st"), (String) sf.get("et"));
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

