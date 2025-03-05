package online.afeibaili;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.GroupEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import online.afeibaili.chess.ChessHandle;
import online.afeibaili.util.FileHandle;

import java.util.List;

public class MessageHandle {

    public static final List<Long> GROUPS = FileHandle.getAllGroupNumber();

    public static void load() {
        Commands.load();
        loadListener();
    }

    public static String parsingMessage(String message, GroupMessageEvent e) {
        String[] parma = message.trim().split("@");
        if (Commands.COMMANDS.get(parma[0].trim()) == null) {
            return null;
        }
        return Commands.COMMANDS.get(parma[0].trim()).method(parma, e);
    }

    private static void loadListener() {
        GlobalEventChannel.INSTANCE.filter(event -> {
            if (event instanceof GroupEvent) {
                GroupEvent g = (GroupEvent) event;
                return GROUPS.contains(g.getGroup().getId());
            }
            return false;
        }).subscribeAlways(GroupMessageEvent.class, e -> {
            String message = e.getMessage().contentToString();
            Group send = e.getSubject();
            String parsingMessage = parsingMessage(message, e);
            if (parsingMessage != null) send.sendMessage(parsingMessage);
            ChessHandle.parsingMessage(e);
        });
    }
}