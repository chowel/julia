package com.julia.socket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @program: skychain
 * @description:
 * @author: Chowel.Master
 * @create: 2023-01-10 18:07
 **/
public class ChannelPond {

    private static final ChannelGroup GLOBAL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static final ConcurrentHashMap<String, ChannelId> ADMINPOND = new ConcurrentHashMap();

    private static final ConcurrentHashMap<String, ChannelId> CLIENTPOND = new ConcurrentHashMap();

    /**
    * @Description: 客户端 Channel 加入
    * @Param:
    * @return:
    * @Author: chowel
    * @Date:
    */
    public static void addClientChannel(Channel channel, String secure){
        ChannelId temp = CLIENTPOND.get(secure);
        if (ObjectUtils.isEmpty(temp)) {
            GLOBAL_GROUP.add(channel);
            CLIENTPOND.putIfAbsent(secure, channel.id());
        } else {
            Channel c = GLOBAL_GROUP.find(temp);
            if(ObjectUtils.isEmpty(c)){
                GLOBAL_GROUP.add(channel);
            }
        }
        ChannelId bemp = CLIENTPOND.get(secure);
        bemp.asShortText();
    }

    public static void addChannel(Channel channel, String userId) {
        ChannelId temp = ADMINPOND.get(userId);
        if (ObjectUtils.isEmpty(temp)) {
            GLOBAL_GROUP.add(channel);
            ADMINPOND.putIfAbsent(userId, channel.id());
        } else {
            Channel c = GLOBAL_GROUP.find(temp);
            if(ObjectUtils.isEmpty(c)){
                GLOBAL_GROUP.add(channel);
            }
        }
        ChannelId bemp = ADMINPOND.get(userId);
        bemp.asShortText();
    }

    public static String removeChannel(Channel channel) {
        String result = "";
        boolean adminRemove = true;
        Iterator<ConcurrentMap.Entry<String, ChannelId>> adminIterator = ADMINPOND.entrySet().iterator();
        while (adminIterator.hasNext()) {
            Map.Entry<String, ChannelId> next = adminIterator.next();
            ChannelId value = next.getValue();
            if (channel.id().equals(value)) {
                result = next.getKey();
                adminIterator.remove();
                adminRemove = false;
            }
        }

        if(adminRemove){
            Iterator<ConcurrentMap.Entry<String, ChannelId>> clientIterator = CLIENTPOND.entrySet().iterator();
            while (clientIterator.hasNext()) {
                Map.Entry<String, ChannelId> next = clientIterator.next();
                ChannelId value = next.getValue();
                if (channel.id().equals(value)) {
                    result = next.getKey();
                    clientIterator.remove();
                }
            }
        }

        return result;
    }

    public static String findClientByChannel(Channel c){
        String secure = "";
        for (Map.Entry<String, ChannelId> next : CLIENTPOND.entrySet()) {
            ChannelId value = next.getValue();
            if (c.id() == value) {
                secure = next.getKey();
            }
        }
        return secure;
    }

    public static String findAdminByChannel(Channel c){
        String userId = "";
        for (Map.Entry<String, ChannelId> next : ADMINPOND.entrySet()) {
            ChannelId value = next.getValue();
            if (c.id() == value) {
                userId = next.getKey();
            }
        }
        return userId;
    }

    public static Channel findChannel(String userId) {
        ChannelId channelId = ADMINPOND.get(userId);
        if(ObjectUtils.isEmpty(channelId)){
            return null;
        }
        return GLOBAL_GROUP.find(channelId);
    }

    public static Channel findAdminChannelByChannelId(ChannelId channelId) {
        return GLOBAL_GROUP.find(channelId);
    }

    public static Channel findClientChannel(String secure){
        ChannelId channelId = CLIENTPOND.get(secure);
        if(ObjectUtils.isEmpty(channelId)){
            return null;
        }
        return GLOBAL_GROUP.find(channelId);
    }

    public static List<String> getAliveClient(){
        return new ArrayList<>(CLIENTPOND.keySet());
    }

    public static ConcurrentHashMap<String, ChannelId> getAllAdmin(){
        return ADMINPOND;
    }
}
