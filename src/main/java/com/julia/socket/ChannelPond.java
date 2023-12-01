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

    private static final ConcurrentHashMap<String, ChannelId> POND = new ConcurrentHashMap();

    public static void addChannel(Channel channel, String userId) {
        ChannelId temp = POND.get(userId);
        if (ObjectUtils.isEmpty(temp)) {
            GLOBAL_GROUP.add(channel);
            POND.putIfAbsent(userId, channel.id());
        } else {
            Channel c = GLOBAL_GROUP.find(temp);
            if(ObjectUtils.isEmpty(c)){
                GLOBAL_GROUP.add(channel);
            }
        }
        ChannelId bemp = POND.get(userId);
        bemp.asShortText();
    }

    public static String removeChannel(Channel channel) {
        String userId = "";
        Iterator<ConcurrentMap.Entry<String, ChannelId>> iterator = POND.entrySet().iterator();
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
        Iterator<ConcurrentMap.Entry<String, ChannelId>> iterator = POND.entrySet().iterator();
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
        ChannelId channelId = POND.get(userId);
        if(ObjectUtils.isEmpty(channelId)){
            return null;
        }
        return GLOBAL_GROUP.find(channelId);
    }

    public static List<String> getAliveCheChe(){
        return new ArrayList<>(POND.keySet());
    }


}
