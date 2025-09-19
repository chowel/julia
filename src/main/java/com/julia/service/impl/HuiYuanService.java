package com.julia.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.diagnosis.DiagnosisUtils;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.julia.config.XmlApiHelper;
import com.julia.entity.GameOrderEntity;
import com.julia.entity.PayConfigEntity;
import com.julia.entity.StackPlayerEntity;
import com.julia.entity.YaoEntity;
import com.julia.entity.alipaymodel.AlipayResposeVO;
import com.julia.entity.alipaymodel.PayByAliPay;
import com.julia.enums.RedisKeyEnum;
import com.julia.model.HuiYuan.BillDetailsResponse;
import com.julia.model.HuiYuan.BillTable;
import com.julia.model.HuiYuan.NewDataSet;
import com.julia.model.HuiYuan.ReturnData;
import com.julia.model.alipay.AliPayCreate;
import com.julia.model.dto.DrawerPollDTO;
import com.julia.service.*;
import com.julia.tool.JuliaException;
import com.julia.tool.JuliaUtils;
import com.julia.tool.RedisUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @program: julia
 * @description: 支付宝支付
 * @author: Chowel.Master
 * @create: 2025-03-12 15:21
 **/

@Service
@Slf4j
public class HuiYuanService {


    @Resource
    private IStackPlayerService playerService;

    @Resource
    private IGameOrderService orderService;

    @Resource
    private IYaoService yaoService;

    @Resource
    private IPayConfigService payConfigService;

    @Resource
    private IRocketService rocketService;

    @Resource
    private RedisUtils redisUtils;

    @Value("${huiyuan.agentId}")
    private String agentId;

    @Value("${huiyuan.dealUser}")
    private String dealUser;

    @Value("${huiyuan.md5Key}")
    private String md5Key;

    @Value("${huiyuan.domain}")
    private String domain;

    @Value("${huiyuan.callbackUrl}")
    private String callbackUrl;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private XmlApiHelper xmlApiHelper;

    /**
     * @Description: 查询骏网未处理订单
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    @SneakyThrows
    public void getHuiYuanOrders() {
//        String downTime = getDownTime();
        String downTime = (String) redisUtils.get(RedisKeyEnum.DOWNTIME.getKey());
        log.info("downTime: {}", downTime);
        String sign = genDownBillSign(downTime);
        String url =
                domain + "DownLoad.aspx?agent_id=" + agentId + "&down_time=" + downTime + "&deal_user=" + dealUser + "&sign=" + sign;
//        log.info("URL: {}", url);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_XML));
        HttpEntity<String> request = new HttpEntity<>(headers);

        ReturnData res = xmlApiHelper.postForXml(url, request, ReturnData.class);

        assert res != null;
        log.info("RetCode: {}", res.getRetCode());
        log.info("DT: {}", res.getDownLoadTime());
        if (StringUtils.hasLength(res.getDownLoadTime())) {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime dateTime = LocalDateTime.parse(res.getDownLoadTime(), inputFormatter);
            String result = dateTime.format(outputFormatter);
            redisUtils.set(RedisKeyEnum.DOWNTIME.getKey(), result, 3600 * 48);
        }

        if (!ObjectUtils.isEmpty(res.getNewDataSet())) {
            NewDataSet newDataSet = res.getNewDataSet();
            if (!ObjectUtils.isEmpty(newDataSet.getTable())) {
                List<BillTable> tables = newDataSet.getTable();
                if (tables.size() > 0) {
                    tables.forEach(b -> {
                        handleBill(b);
                    });
                }
            }
        }
    }

    @Async("handleNotificationExecutor")
    public void handleBill(BillTable bill) {
        log.info("BillNo: {}", bill.getBillNo());
        log.info("ProductCode: {}", bill.getProductCode());
        log.info("ParPrice: {}", bill.getParPrice());
        log.info("Account: {}", bill.getChargeAccount());
        log.info("BillStatus: {}", bill.getBillStatus());
        log.info("ProductName: {}", bill.getProductName());
        boolean isGou;
        String account = bill.getChargeAccount();

        String orderNo = "";
        String paylerLoginName = "";

        if (account.length() > 8) {
            isGou = true;
            orderNo = account.substring(account.length() - 8);
            paylerLoginName = account.substring(0, account.length() - 8);
        } else {
            paylerLoginName = account;
            isGou = false;
        }
        log.info("ORG: {}  orderNo: {}  name: {}", account, orderNo, paylerLoginName);
        StackPlayerEntity player = playerService.findPlayerByLoginName(paylerLoginName);
        if (!ObjectUtils.isEmpty(player)) {
            log.info("处理骏网业务");
            setBillStatus(bill.getBillNo(), 0, "签出准备处理");
            setBillStatus(bill.getBillNo(), 1, "成功处理");

            if (isGou) {
                if (StringUtils.hasLength(orderNo)) {
                    GameOrderEntity order = orderService.findOneByOrderNo(orderNo);
                    if (!ObjectUtils.isEmpty(order)) {
//                        order.setOrderNo(JuliaUtils.GeneratorOderNo(Math.toIntExact(player.getUserId())));
                        order.setPlayerId(Math.toIntExact(player.getUserId()));
                        order.setStatus(2);
                        order.setSubject(bill.getProductName());
                        order.setProductCode(bill.getProductCode());
                        Long price = JuliaUtils.stringYuanToCents(bill.getParPrice());
                        order.setTotal(price);
                        order.setTotalAmount(price);
                        order.setTradeStatus("TRADE_SUCCESS");
                        order.setOutOrderNo(bill.getBillNo());
                        if (orderService.updateById(order)) {
                            // 玩家上分
                            PayConfigEntity config = payConfigService.findOneByCode(bill.getProductCode());
                            if (!ObjectUtils.isEmpty(config)) {
                                playerService.addCoin(order.getPlayerId(), config.getGold(), config.getOre());
                            }
                            log.info("回调");
                            Map<String, Object> callBackParams = new HashMap<>(5);
                            String sign = order.getOrderNo() + order.getOutOrderNo();
                            callBackParams.put("orderNo", order.getOrderNo());
                            callBackParams.put("outOrderNo", order.getOutOrderNo());
                            callBackParams.put("totalAmount", String.format("%.2f", price / 100.0));
                            callBackParams.put("tradeStatus", order.getTradeStatus());
                            callBackParams.put("account", paylerLoginName);
                            callBackParams.put("sign", DigestUtils.md5DigestAsHex(sign.getBytes(StandardCharsets.UTF_8)));
//                            rocketService.handleCallBack(callbackUrl, callBackParams);

                            HttpHeaders headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);

                            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(callBackParams, headers);
                            ResponseEntity<String> response = restTemplate.postForEntity(callbackUrl, requestEntity, String.class);
                            if (response.getStatusCode() == HttpStatus.OK) {
                                log.info(response.getBody());
                                if (Objects.requireNonNull(response.getBody()).contains("ok")) {
                                    orderService.lambdaUpdate()
                                            .eq(GameOrderEntity::getOrderId, order.getOrderId())
                                            .set(GameOrderEntity::getUrl, "Yes")
                                            .update();
                                }
                            }

                        }
                    }
                } else {
                    GameOrderEntity newOrder = new GameOrderEntity();
                    newOrder.setOrderNo(JuliaUtils.GeneratorOderNo(Math.toIntExact(player.getUserId())));
                    newOrder.setPlayerId(Math.toIntExact(player.getUserId()));
                    newOrder.setStatus(2);
                    newOrder.setSubject(bill.getProductName());
                    newOrder.setProductCode(bill.getProductCode());
                    Long price = JuliaUtils.stringYuanToCents(bill.getParPrice());
                    newOrder.setTotal(price);
                    newOrder.setTotalAmount(price);
                    newOrder.setTradeStatus("TRADE_SUCCESS");
                    newOrder.setOutOrderNo(bill.getBillNo());
                    newOrder.setPlayerName(paylerLoginName);
                    if (orderService.save(newOrder)) {
                        // 玩家上分
                        PayConfigEntity config = payConfigService.findOneByCode(bill.getProductCode());
                        if (!ObjectUtils.isEmpty(config)) {
                            playerService.addCoin(newOrder.getPlayerId(), config.getGold(), config.getOre());
                        }
                    }
                }
            } else {
                GameOrderEntity order = new GameOrderEntity();
                order.setOrderNo(JuliaUtils.GeneratorOderNo(Math.toIntExact(player.getUserId())));
                order.setPlayerId(Math.toIntExact(player.getUserId()));
                order.setStatus(2);
                order.setSubject(bill.getProductName());
                order.setProductCode(bill.getProductCode());
                Long price = JuliaUtils.stringYuanToCents(bill.getParPrice());
                order.setTotal(price);
                order.setTotalAmount(price);
                order.setTradeStatus("TRADE_SUCCESS");
                order.setOutOrderNo(bill.getBillNo());
                order.setPlayerName(paylerLoginName);
                if (orderService.save(order)) {
                    // 玩家上分
                    PayConfigEntity config = payConfigService.findOneByCode(bill.getProductCode());
                    if (!ObjectUtils.isEmpty(config)) {
                        playerService.addCoin(order.getPlayerId(), config.getGold(), config.getOre());
                    }
                }
            }


        }
    }

    /**
     * @Description: 设置订单状态
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    @SneakyThrows
    public void setBillStatus(String billNo, int status, String dealMsg) {

        String sign = genSetBillSign(billNo, status);

        String url = domain + "Notify.aspx?agent_id=" + agentId +
                "&bill_no=" + billNo +
                "&status=" + status +
                "&deal_user=" + dealUser +
                "&deal_msg=" + dealMsg +
                "&esale_account=" +
                "&sign=" + sign;

        log.info("Set Bill URL: {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_XML));
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                String.class
        );

        String xmlString = response.getBody();
        Map<String, String> params = parse(xmlString);

        BillDetailsResponse resp = new BillDetailsResponse();
        resp.setRetCode(Integer.valueOf(params.get("ret_code")));
        resp.setRetMsg(params.get("ret_msg"));
        resp.setAgentId(params.get("agent_id"));
        resp.setBillNo(params.get("bill_no"));
        resp.setProductCode(params.get("product_code"));
        resp.setProductName(params.get("product_name"));
        resp.setParPrice(new BigDecimal(params.get("par_price")));
        resp.setPurchaseAmt(new BigDecimal(params.get("purchase_amt")));
        resp.setBillStatus(Integer.valueOf(params.get("bill_status")));
        resp.setSign(params.get("sign"));

        log.info("SetStatus:BillNo: {} - Status: {}", resp.getBillNo(), resp.getBillStatus());
    }

    /**
     * @Description: 查询订单详情
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public void getDetailOrder(String billNo) {
        String sign = genDetailBill(billNo);
        String url = domain + "Query.aspx?agent_id=" + agentId +
                "&bill_no=" + billNo +
                "&sign=" + sign;

        log.info("getDetails URL: {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_XML));
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                String.class
        );

        String xmlString = response.getBody();


        Map<String, String> params = parse(xmlString);

        BillDetailsResponse resp = new BillDetailsResponse();
        resp.setRetCode(Integer.valueOf(params.get("ret_code")));
        resp.setRetMsg(params.get("ret_msg"));
        resp.setAgentId(params.get("agent_id"));
        resp.setBillNo(params.get("bill_no"));
        resp.setProductCode(params.get("product_code"));
        resp.setProductName(params.get("product_name"));
        resp.setParPrice(new BigDecimal(params.get("par_price")));
        resp.setPurchaseAmt(new BigDecimal(params.get("purchase_amt")));
        resp.setBillStatus(Integer.valueOf(params.get("bill_status")));
        resp.setSign(params.get("sign"));

        log.info("Details:BillNo: {} - Status: {}", resp.getBillNo(), resp.getBillStatus());

    }


    private String getDownTime() {
        // 获取当前时间  20250916000000
        LocalDateTime now = LocalDateTime.now();
        // 定义格式：4位年+2位月+2位日+2位时+2位分+2位秒
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        // 格式化为字符串
        return now.format(formatter);

    }

    private Map<String, String> parse(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return result;
        }

        for (String param : query.split("&")) {
            String[] entry = param.split("=", 2);
            if (entry.length == 2) {
                result.put(entry[0], decode(entry[1]));
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }


    private String decode(String s) {
        try {
            return java.net.URLDecoder.decode(s, "UTF-8");
        } catch (Exception e) {
            return s; // 解码失败保留原样
        }
    }

    // md5加密
    private String genMd5(String str) {
        try {
            // 获取 MD5 加密算法实例
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 将输入字符串转换为字节数组
            byte[] inputBytes = str.getBytes();

            // 计算 MD5 哈希值
            byte[] hashBytes = md.digest(inputBytes);

            // 将哈希值转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b)); // 使用 %02x 将每个字节表示为两位十六进制数
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // 当算法不可用时，处理异常
            log.info("MD5 算法不可用：" + e.getMessage());
            return null;
        }
    }


    // 下载单据生成签名
    private String genDownBillSign(String dt) {
        String data =
                "agent_id=" + agentId + "&down_time=" + dt + "&deal_user=" + dealUser + "|||" + md5Key;
//        log.info("orgSian: {}", data);
        return genMd5(data);
    }

    // 修改单据生成签名
    private String genSetBillSign(String billNo, int status) {
        // 拼接字符串
        String data =
                "agent_id=" + agentId + "&bill_no=" + billNo + "&status=" + status + "|||" + md5Key;
        log.info("orgSian: {}", data);
        // 生成签名
        return genMd5(data);
    }

    private String genDetailBill(String billNo) {
        // 拼接字符串
        String data =
                "agent_id=" + agentId + "&bill_no=" + billNo + "|||" + md5Key;
        log.info("orgSian: {}", data);
        // 生成签名
        return genMd5(data);
    }

}

