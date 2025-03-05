package online.afeibaili.util;

import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.GroupMessageEvent;

public class BotHandle {
    public static String isExit(long qqNumber, GroupMessageEvent event) {
        NormalMember qq = event.getGroup().get(qqNumber);
        if (qq == null) {
            return "已退群";
        }
        return qq.getNameCard();
    }
}
