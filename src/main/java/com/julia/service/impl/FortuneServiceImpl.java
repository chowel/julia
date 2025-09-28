package com.julia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.julia.entity.*;
import com.julia.mapper.CoinLogMapper;
import com.julia.mapper.FortuneMapper;
import com.julia.mapper.ObtainMapper;
import com.julia.mapper.YaoMapper;
import com.julia.model.dto.*;
import com.julia.model.vo.CreateFortuneVO;
import com.julia.model.vo.FortuneApiVO;
import com.julia.model.vo.YaoEntityVO;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
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
    ObtainMapper obtainMapper;

    @Resource
    RestTemplate restTemplate;

    @Resource
    CoinLogMapper coinLogMapper;

    @Value("${sign.salt}")
    private String SIGNSALT;

    @Value("${deposit.url}")
    private String PAYURL;

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
                .like(StringUtils.hasLength((String) sf.get("fortuneno")), FortuneEntity::getFortuneNo, sf.get("fortuneno"))
                .like(StringUtils.hasLength((String) sf.get("transactno")), FortuneEntity::getTransactNo, sf.get(
                        "transactno"))
                .between(StringUtils.hasLength((String) sf.get("st")), FortuneEntity::getCreateTime, (String) sf.get("st"),
                        (String) sf.get("et"))
                .orderByDesc(FortuneEntity::getFortuneId)
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

//    @Override
//    public String handIn(FortuneDTO dto, int pid) {
//        FortuneEntity entity = new FortuneEntity();
//
//        // 生成本平台订单号；
//        String fortuneNo = GeneratorFortuneNo(pid);
//
//        entity.setFortuneNo(fortuneNo);
//        entity.setAmount(dto.getAmount());
//        entity.setPId(pid);
//        entity.setOrderId(dto.getOrderId());
//        entity.setDrawer(dto.getDrawer());
//        FortuneRedis fortuneRedis = JuliaUtils.convertTo(new FortuneRedis(), entity);
//        if (webSocketService.hanldeFortune(fortuneRedis)) {
//            save(entity);
//            return fortuneNo;
//        } else {
//            throw new JuliaException("暂无车队");
//        }
//    }

    @Override
    public Boolean dispenseCar(DrawerPollDTO dto) {
        return false;
    }

    @Override
    public CreateFortuneVO addFortune(NewFortuneDTO dto, int pid) {
        FortuneEntity entity = new FortuneEntity();
        // 生成本平台订单号；
        String fortuneNo = GeneratorFortuneNo(pid);

        entity.setFortuneNo(fortuneNo);
        entity.setAmount(dto.getAmount());
        entity.setPId(pid);
        entity.setOrderId(dto.getOrderId());
        entity.setNoticeUrl(dto.getNoticeUrl());
        // 回调地址

//        FortuneRedis fortuneRedis = JuliaUtils.convertTo(new FortuneRedis(), entity);
        List<String> alives = new ArrayList<>();
        if (alives.size() > 0) {
//            创建订单
            entity.setStatus(0);
            save(entity);
            CreateFortuneVO vo = new CreateFortuneVO();
            vo.setAmount(dto.getAmount());
            vo.setFortuneNo(fortuneNo);
            vo.setOrderId(dto.getOrderId());
            vo.setPayUrl(PAYURL + fortuneNo);
            return vo;
        } else {
            throw new JuliaException("暂无车队");
        }
    }

    @Override
    public Boolean handOut(HandOutDTO dto, int cid) {

        return false;
    }

    @Override
    @Transactional
    public Boolean overFortune(FortuneEntityVO dto) {
        FortuneEntity entity = getById(dto.getFortuneId());
        if (!ObjectUtils.isEmpty(entity)) {
            if (entity.getStatus() != 5) {
                throw new JuliaException("该单已处理");
            }
            YaoEntity car = yaoMapper.selectById(entity.getCId());

            if (entity.getAmount() > car.getCoin()) {
//                钱不够失败
                entity.setStatus(3);
                String callbackReturn = handleCallBack(entity);
                if (StringUtils.hasLength(callbackReturn)) {

                    entity.setCheckCallback(1);

                } else {
                    entity.setCheckCallback(2);
                }
                updateById(entity);
                throw new JuliaException("米不够");
            }

            // 失败
            if (dto.getStatus() == 0) {
                entity.setStatus(3);
                entity.setMsg(dto.getMsg());
            }
            // 成功
            if (dto.getStatus() == 1) {
                car.setCoin(car.getCoin() - entity.getAmount());
                int coinLose = -entity.getAmount();
//                webSocketService.incrementScore(String.valueOf(car.getYaoId()), coinLose);
                entity.setStatus(4);
                if (StringUtils.hasLength(dto.getTransactNo())) {
                    entity.setTransactNo(dto.getTransactNo());
                }
                addCoinLog(entity);
            }
            entity.setDoneTime(System.currentTimeMillis());

            yaoMapper.updateById(car);

            if(updateById(entity)) {
                String callbackReturn = handleCallBack(entity);
                if (StringUtils.hasLength(callbackReturn)) {
                    entity.setCheckCallback(1);
                } else {
                    entity.setCheckCallback(2);
                }

                ObtainEntity obtain = obtainMapper.selectOne(new QueryWrapper<ObtainEntity>()
                        .eq("yao_id", entity.getCId())
                        .eq("name", entity.getPayId()));

                if (!ObjectUtils.isEmpty(obtain)) {
                    obtain.setCout(obtain.getCout() + 1);
                    obtainMapper.updateById(obtain);
                }
                updateById(entity);
                return true;
            }

        }
        return false;
    }

    @Override
    public Boolean callBack(FortuneDTO dto) {

        FortuneEntity fortune = getOneByFortuneNo(dto.getFortuneNo());
        if (ObjectUtils.isEmpty(fortune)) {
            throw new JuliaException("订单异常");
        }


        String callbackReturn = handleCallBack(fortune);
        
        if (StringUtils.hasLength(callbackReturn)) {
            fortune.setCheckCallback(1);
            return updateById(fortune);
        } else {
            fortune.setCheckCallback(2);
        }
        updateById(fortune);
        return false;

    }

    @Override
    public List<MetricsFortuneEntity> mertric(QueryPagement queryPagement) {
        Map<String, Object> sf = queryPagement.getSearchFields();
        int panid = ObjectUtils.isEmpty(sf.get("panid")) ? -1 : (int) sf.get("panid");
        int carid = ObjectUtils.isEmpty(sf.get("carid")) ? -1 : (int) sf.get("carid");
        return getBaseMapper().metricsfortune((String) sf.get("st"), (String) sf.get("et"), carid, panid);
    }

    @Override
    public Boolean refuse(FortuneDTO dto, int carId) {
        logger.info("分配给其他人: ");

        return false;
    }

    @Override
    public Boolean handClose(String carId) {

        return true;
    }

    @Override
    public Integer getCoins(int yaoId) {
        YaoEntity yaoEntity = yaoMapper.selectById(yaoId);
        YaoEntityVO vo = JuliaUtils.convertTo(new YaoEntityVO(), yaoEntity);
        return vo.getCoin();
    }

    @Override
    public List<String> alives() {

        return null;
    }

    @Override
    public Boolean expiredCallback(FortuneEntity fortuneEntity) {
        return false;
    }

    @Override
    public FortuneApiVO findOneByNo(String fortuneNo) {
        FortuneEntity fortune = getOneByFortuneNo(fortuneNo);
        return JuliaUtils.convertTo(new FortuneApiVO(), fortune);
    }

    @Override
    public Boolean forceFortune(String fortuneNo) {
        FortuneEntity fortune = getOneByFortuneNo(fortuneNo);
        if (ObjectUtils.isEmpty(fortune)) {
            throw new JuliaException("该单异常");
        }

        fortune.setStatus(4);
        String callbackReturn = handleCallBack(fortune);
        if (StringUtils.hasLength(callbackReturn)) {
            fortune.setCheckCallback(1);
        } else {
            fortune.setCheckCallback(2);
        }
        updateById(fortune);
        addCoinLog(fortune);
        return true;

    }

    @Override
    public FortuneApiVO getOneByNo(String fortuneNo) {
        webSocketService.depositsInput(fortuneNo);
        FortuneEntity fortune = getOneByFortuneNo(fortuneNo);
        return JuliaUtils.convertTo(new FortuneApiVO(), fortune);
    }

    protected String handleCallBack(FortuneEntity fortune) {
        logger.info("FortuneStatus:{}", fortune.getStatus());
        try {
            Map<String, Object> params = new HashMap<>(6);
            String sign = fortune.getOrderId() + fortune.getFortuneNo();
            params.put("orderNo", fortune.getOrderId());
            params.put("fortuneNo", fortune.getFortuneNo());
            params.put("amount", fortune.getAmount());
            params.put("orderStatus", fortune.getStatus());
            params.put("payTime", fortune.getDoneTime());
            String signMd5 = DigestUtils.md5DigestAsHex(sign.getBytes(StandardCharsets.UTF_8));
            logger.info("SIGN-MD5 :{}", signMd5);
            params.put("sign", signMd5);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(fortune.getNoticeUrl(), requestEntity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("回调接口返回值 :{}", response.getBody());
                return response.getBody();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;

    }

    protected String GeneratorFortuneNo(int pid) {
        long ct = System.currentTimeMillis();
        Random random = new Random();
        StringBuilder randomLetters = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            // 生成一个随机的小写字母
            char randomLetter = (char) ('A' + random.nextInt(26));
            // 将随机字母添加到字符串构建器中
            randomLetters.append(randomLetter);
        }
        return "YN" + ct + pid + randomLetters;
    }

    public FortuneEntity getOneByFortuneNo(String No) {
        return new LambdaQueryChainWrapper<>(getBaseMapper()).eq(FortuneEntity::getFortuneNo, No).one();
    }

    protected void addCoinLog(FortuneEntity fortune) {

    }
}

