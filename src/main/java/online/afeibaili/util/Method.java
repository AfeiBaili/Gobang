package online.afeibaili.util;

import net.mamoe.mirai.event.events.GroupMessageEvent;

@FunctionalInterface
public interface Method {
    String method(String[] parma,GroupMessageEvent e);
}
