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

    private static final ChannelGroup GlobalGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static ConcurrentMap<String, ChannelId> POND = new ConcurrentHashMap();

    public static void addChannel(Channel channel, String userId) {
        ChannelId temp = POND.get(userId);
        if (ObjectUtils.isEmpty(temp)) {
            GlobalGroup.add(channel);
            POND.put(userId, channel.id());
        } else {
            Channel c = GlobalGroup.find(temp);

            if(ObjectUtils.isEmpty(c)){
                GlobalGroup.add(channel);
            }
        }
    }

    public static String removeChannel(Channel channel) {
        GlobalGroup.remove(channel);
        String UserId = "";
        Iterator<ConcurrentMap.Entry<String, ChannelId>> iterator = POND.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ChannelId> next = iterator.next();
            UserId = next.getKey();
            ChannelId value = next.getValue();
            if (channel.id() == value) {
                iterator.remove();
            }
        }

        return UserId;
    }

    public static Channel findChannel(String userId) {
        return GlobalGroup.find(POND.get(userId));
    }

    public static List<String> getAliveCheChe(){
        return new ArrayList<>(POND.keySet());
    }


}
