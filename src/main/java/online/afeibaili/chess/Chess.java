package online.afeibaili.chess;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import online.afeibaili.util.BotHandle;
import online.afeibaili.util.FileHandle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Chess {
    public static final HashMap<Long, PlayChess> CHESS_ROOM = new HashMap<>();
    public static final List<Chess> CHESS = new ArrayList<>();
    public static final Font ZHOU_ZI_FANG_TI = FileHandle.getFontByFile("fonts/舟方日明.otf");
    public final Color chessboardBackgroundColor = Color.decode("#2b2b2b");
    public final Color chessboardBorderColor = Color.decode("#87939A");
    public final Color fontColor = Color.decode("#AFB1B3");
    public final Color winFontColor = Color.decode("#C6CFD4");
    public final Color mikuColor = Color.decode("#39C5BB");
    private final Integer height = 700;
    private final Integer width = 700;
    private final Integer margin = 30;
    private final Integer border1 = 7;
    private final Integer borderSpace = 3;
    private final Integer border2 = 3;
    private final Integer columnAndRow = 14;
    private final Integer columnAndRowWeight = 2;
    private final Integer winFontSize = 100;
    private final Long black;
    private final Long white;
    private final GroupMessageEvent event;
    private final BufferedImage chessboard;
    private final char[][] chessMap;
    private final Long startTime;
    private final String startTimeFormat;
    private final String regex;
    private final Integer chesspieceSize = 30;
    public Color blackColor = Color.decode("#000000");
    public Color whiteColor = Color.decode("#ffffff");
    private boolean isBlack = true;
    private boolean isWin = false;
    private boolean isProcessing = false;
    private String blackName;
    private String whiteName;

    private Chess(PlayChess playChess) {
        this.black = playChess.getPlay1();
        this.white = playChess.getPlay2();
        this.event = playChess.getEvent();
        this.startTime = Instant.now().toEpochMilli();
        this.startTimeFormat = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 / HH:mm:ss"));
        this.chessboard = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.chessMap = new char[this.columnAndRow + 1][this.columnAndRow + 1];
        String regex1 = "(1[0-" + columnAndRow % 10 + "]|[0-9])";
        String regex2 = "[a-" + (char) ('a' + this.columnAndRow) + "]";
        this.regex = regex1 + "\\s*" + regex2 + "|" + regex2 + "\\s*" + regex1;
        sendChessImage();
        this.blackName = BotHandle.isExit(black, event);
        this.whiteName = BotHandle.isExit(white, event);

        //miku彩蛋
        if (playChess.getPlay1() == 1405748998) blackColor = mikuColor;
        else if (playChess.getPlay2() == 1405748998) whiteColor = mikuColor;
    }

    /**
     * 创建一个房间
     *
     * @param playChess 邀请阶段时传入的PlayChess类
     */
    public static void createRoom(PlayChess playChess) {
        CHESS.add(new Chess(playChess));
    }

    void sendChessImage() {
        Graphics2D g = getChessboard().createGraphics();
        g.setColor(chessboardBackgroundColor);
        g.fillRect(0, 0, width, height);
        g.setColor(chessboardBorderColor);
        g.fillRect(margin, margin, width - (margin << 1), height - (margin << 1));
        g.setColor(chessboardBackgroundColor);
        g.fillRect(margin + border1, margin + border1, width - (margin << 1) - (border1 << 1), height - (margin << 1) - (border1 << 1));
        g.setColor(chessboardBorderColor);
        g.fillRect(margin + border1 + borderSpace, margin + border1 + borderSpace, width - (margin << 1) - (border1 << 1) - (borderSpace << 1), height - (margin << 1) - (border1 << 1) - (borderSpace << 1));
        g.setColor(chessboardBackgroundColor);
        g.fillRect(margin + border1 + borderSpace + border2, margin + border1 + borderSpace + border2, width - (margin << 1) - (border1 << 1) - (borderSpace << 1) - (borderSpace << 1), height - (margin << 1) - (border1 << 1) - (borderSpace << 1) - (borderSpace << 1));
        g.setFont(ZHOU_ZI_FANG_TI.deriveFont(20f));
        g.setColor(fontColor);
        g.drawString("对局日期：" + startTimeFormat, 30, 25);

        int x = margin + border1 + borderSpace + border2;
        int y = margin + border1 + borderSpace + border2;
        int xWidth = width - (x * 2);
        int yHeight = height - (y * 2);
        BufferedImage bufferedImage = new BufferedImage(xWidth, yHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D chess = bufferedImage.createGraphics();
        chess.setColor(chessboardBackgroundColor);
        chess.fillRect(0, 0, xWidth, yHeight);
        chess.setColor(chessboardBorderColor);
        int xSpace = (xWidth + columnAndRow) / columnAndRow;
        int ySpace = (yHeight + columnAndRow) / columnAndRow;
        for (int i = 1; i < columnAndRow; i++) {
            chess.fillRect(xSpace * i, 0, columnAndRowWeight, yHeight);
        }
        for (int i = 1; i < columnAndRow; i++) {
            chess.fillRect(0, ySpace * i, xWidth, columnAndRowWeight);
        }
        for (int i = 0; i < columnAndRow + 1; i++) {
            g.drawString(String.valueOf((char) ('A' + i)), xSpace * i + x - (columnAndRowWeight << 1), height - (x >> 2));
        }
        for (int i = 0; i < columnAndRow + 1; i++) {
            g.drawString(String.valueOf(i), 7, ySpace * i + y + (columnAndRowWeight << 1));
        }
        g.drawImage(bufferedImage, x, y, xWidth, yHeight, null);
        for (int i = 0; i < chessMap.length; i++) {
            for (int j = 0; j < chessMap[i].length; j++) {
                switch (chessMap[i][j]) {
                    case '#':
                        g.setColor(blackColor);
                        break;
                    case '*':
                        g.setColor(whiteColor);
                        break;
                    default:
                        continue;
                }
                g.fillArc(x + xSpace * i - (chesspieceSize >> 1),
                        y + ySpace * j - (chesspieceSize >> 1),
                        chesspieceSize, chesspieceSize, 0, 360);
            }
        }

        if (isWin()) {
            event.getGroup().sendMessage((isBlack() ? "黑棋" : "白棋") + "胜利！@" + (isBlack() ? black : white));
            g.setFont(ZHOU_ZI_FANG_TI.deriveFont(Font.BOLD | Font.ITALIC, winFontSize));
            g.setColor(winFontColor);
            g.drawString((isBlack() ? blackName : whiteName) + "赢", 0, (height >> 1) + (winFontSize >> 1));
            CHESS_ROOM.remove(white);
            CHESS.remove(this);
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(chessboard, "png", outputStream);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            Contact.sendImage(event.getGroup(), inputStream);
            inputStream.close();
        } catch (IOException e) {
            event.getGroup().sendMessage("无法发送图片，可能是图片找不到");
        }
    }

    public boolean isProcessing() {
        return isProcessing;
    }

    public void setProcessing(boolean processing) {
        isProcessing = processing;
    }

    public String getBlackName() {
        return blackName;
    }

    public void setBlackName(String blackName) {
        this.blackName = blackName;
    }

    public String getWhiteName() {
        return whiteName;
    }

    public void setWhiteName(String whiteName) {
        this.whiteName = whiteName;
    }

    public Integer getWinFontSize() {
        return winFontSize;
    }

    private boolean isWin() {
        return isWin;
    }

    public void setWin(boolean win) {
        isWin = win;
    }

    public Integer getChesspieceSize() {
        return chesspieceSize;
    }

    public boolean isBlack() {
        return isBlack;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getMargin() {
        return margin;
    }

    public Integer getBorder1() {
        return border1;
    }

    public Integer getBorderSpace() {
        return borderSpace;
    }

    public Integer getBorder2() {
        return border2;
    }

    public Integer getColumnAndRow() {
        return columnAndRow;
    }

    public Integer getColumnAndRowWeight() {
        return columnAndRowWeight;
    }

    public Long getBlack() {
        return black;
    }

    public void setBlack(boolean black) {
        isBlack = black;
    }

    public Long getWhite() {
        return white;
    }

    public GroupMessageEvent getEvent() {
        return event;
    }

    public BufferedImage getChessboard() {
        return chessboard;
    }

    public char[][] getChessMap() {
        return chessMap;
    }

    public Long getStartTime() {
        return startTime;
    }

    public String getStartTimeFormat() {
        return startTimeFormat;
    }

    public String getRegex() {
        return regex;
    }
}
