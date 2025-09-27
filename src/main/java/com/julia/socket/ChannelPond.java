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
        String userId = "";
        Iterator<ConcurrentMap.Entry<String, ChannelId>> iterator = ADMINPOND.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ChannelId> next = iterator.next();
            ChannelId value = next.getValue();
            if (channel.id().equals(value)) {
                userId = next.getKey();
                iterator.remove();
            }
        }
        return userId;
    }

    public static String findUserIdByChannel(Channel c){
        String userId = "";
        Iterator<ConcurrentMap.Entry<String, ChannelId>> iterator = ADMINPOND.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ChannelId> next = iterator.next();
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

    public static Channel findClientChannel(String secure){
        ChannelId channelId = CLIENTPOND.get(secure);
        if(ObjectUtils.isEmpty(channelId)){
            return null;
        }
        return GLOBAL_GROUP.find(channelId);
    }

    public static List<String> getAliveCheChe(){
        return new ArrayList<>(ADMINPOND.keySet());
    }


}
