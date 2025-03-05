package online.afeibaili;

import online.afeibaili.chess.Chess;
import online.afeibaili.chess.ChessHandle;
import online.afeibaili.chess.PlayChess;
import online.afeibaili.util.BotHandle;
import online.afeibaili.util.Method;

import java.util.HashMap;

import static online.afeibaili.chess.Chess.CHESS_ROOM;

public class Commands {
    public static final HashMap<String, Method> COMMANDS = new HashMap<>();

    public static void load() {
        COMMANDS.put("菜单", ((parma, e) -> COMMANDS.keySet().toString()));
        COMMANDS.put("下棋", (parma, e) -> {
            if (parma.length != 2) {
                return "下棋@QQ 或者 下棋 @QQ";
            }
            if (parma[1].charAt(0) == '@') {
                parma[1] = parma[1].substring(1);
            }
            long play2QQ;
            try {
                play2QQ = Long.parseLong(parma[1]);
            } catch (NumberFormatException ex) {
                return "请使用@QQ而不是一个@符跟上其他字符";
            }
            if (CHESS_ROOM.get(play2QQ) != null) {
                return BotHandle.isExit(play2QQ, e) + "已经在邀请或者游戏中";
            }

            for (PlayChess playChess : CHESS_ROOM.values()) {
                if (playChess.getPlay1() == e.getSender().getId()) return "你不能同时发出多个邀请";
                if (playChess.getPlay2() == e.getSender().getId()) return "有人已经邀请你了";
            }

            PlayChess playChess = new PlayChess(e.getSender().getId(), play2QQ, e);
            CHESS_ROOM.put(play2QQ, playChess);
            return "请" + BotHandle.isExit(play2QQ, e) + "一分钟内，发送 “接受” 以同意下棋";
        });

        COMMANDS.put("接受", (parma, e) -> {
            long play2 = e.getSender().getId();
            PlayChess playChess = CHESS_ROOM.get(play2);
            if (playChess == null) return "没有邀请你，不能接受喵";
            if (ChessHandle.isPlayer(play2) != null) return "你已经在游戏中了";
            playChess.agreed();
            return "邀请者为黑子，被邀请者为白子，黑子先下";
        });
        COMMANDS.put("拒绝", (parma, e) -> {
            long play2 = e.getSender().getId();
            PlayChess playChess = CHESS_ROOM.get(play2);
            if (playChess == null) return "没有邀请你，不能拒绝喵";
            if (ChessHandle.isPlayer(play2) != null) return "你已经在游戏中啦";
            playChess.refused();
            return "已拒绝" + playChess.getPlay1() + "的邀请";
        });
        COMMANDS.put("认输", (parma, e) -> {
            long play1 = e.getSender().getId();
            Chess chess = ChessHandle.isPlayer(play1);
            if (chess == null) {
                return "你并没有在游戏中";
            }
            if (CHESS_ROOM.get(chess.getWhite()) != null) {
                CHESS_ROOM.get(chess.getWhite()).refused();
            }
            Chess.CHESS.remove(chess);
            return "认输了🤣👉💻" + "@" + BotHandle.isExit(play1, e);
        });
        COMMANDS.put("取消", (parma, e) -> {
            long play1 = e.getSender().getId();

            for (PlayChess playChess : CHESS_ROOM.values()) {
                if (playChess.getPlay1() == play1) {
                    CHESS_ROOM.remove(playChess.getPlay2());
                    playChess.refused();
                    return "已取消给" + BotHandle.isExit(playChess.getPlay2(), e) + "发送的请求";
                }
            }
            return "你并没有发送邀请";
        });
        COMMANDS.put("查看群", (parma, e) -> MessageHandle.GROUPS.toString());
    }
}
