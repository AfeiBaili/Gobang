package online.afeibaili.chess;

import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.util.Timer;
import java.util.TimerTask;

import static online.afeibaili.chess.Chess.CHESS_ROOM;


public class PlayChess {
    private Long play1;
    private Long play2;

    private Timer timer;
    private GroupMessageEvent event;

    public PlayChess(Long play1, Long play2, GroupMessageEvent e) {
        this.play1 = play1;
        this.play2 = play2;
        this.timer = new Timer();
        this.event = e;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                e.getGroup().sendMessage("对方并没有接受您的邀请🌹\n@" + play1);
                CHESS_ROOM.remove(play2);
            }
        }, 60000);
    }

    public void agreed() {
        timer.cancel();
        Chess.createRoom(this);
    }

    public void refused() {
        timer.cancel();
        CHESS_ROOM.remove(play2);
    }

    public GroupMessageEvent getEvent() {
        return event;
    }

    public void setEvent(GroupMessageEvent event) {
        this.event = event;
    }

    public Long getPlay1() {
        return play1;
    }

    public void setPlay1(Long play1) {
        this.play1 = play1;
    }

    public Long getPlay2() {
        return play2;
    }

    public void setPlay2(Long play2) {
        this.play2 = play2;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
}
