package online.afeibaili.chess;

import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.util.regex.Pattern;

import static online.afeibaili.chess.Chess.CHESS;

public class ChessHandle {
    public static void parsingMessage(GroupMessageEvent event) {
        long id = event.getSender().getId();
        Chess chess = isPlayer(id);
        String message = event.getMessage().contentToString().trim().toLowerCase();
        if (chess == null) return;
        if (!Pattern.matches(chess.getRegex(), message)) return;
        if (!(id == chess.getBlack() && chess.isBlack() || id == chess.getWhite() && !chess.isBlack())) {
            event.getGroup().sendMessage("现在还没有轮到你");
            return;
        }
        String[] rowAndColumn = message.split("\\s+");
        int number;
        int letter;
        if (rowAndColumn.length == 1) {
            String letterAndNumber = rowAndColumn[0];
            char charAt = letterAndNumber.charAt(0);
            if (letterAndNumber.length() == 2) {
                if (charAt >= 97) {
                    letter = charAt - 97;
                    number = Integer.parseInt(String.valueOf(letterAndNumber.charAt(1)));
                } else {
                    letter = letterAndNumber.charAt(1) - 97;
                    number = Integer.parseInt(String.valueOf(letterAndNumber.charAt(0)));
                }
            } else if (charAt >= 97) {
                letter = charAt - 97;
                number = Integer.parseInt(String.valueOf(letterAndNumber.charAt(1)) + letterAndNumber.charAt(2));
            } else {
                letter = letterAndNumber.charAt(2) - 97;
                number = Integer.parseInt(String.valueOf(letterAndNumber.charAt(0)) + letterAndNumber.charAt(1));
            }
        } else {
            letter = rowAndColumn[0].charAt(0) - 97;
            try {
                number = Integer.parseInt(rowAndColumn[1]);
            } catch (NumberFormatException e) {
                number = Integer.parseInt(rowAndColumn[0]);
                letter = rowAndColumn[1].toLowerCase().charAt(0) - 97;
            }
        }
        if (chess.getChessMap()[letter][number] != 0) {
            event.getGroup().sendMessage("这里已经没有位置了");
            return;
        }

        if (chess.isProcessing()) {
            event.getGroup().sendMessage("你的操作太快啦！");
            return;
        }
        chess.setProcessing(true);

        chess.getChessMap()[letter][number] = chess.isBlack() ? '#' : '*';
        win(chess, letter, number);
        chess.sendChessImage();
        chess.setBlack(!chess.isBlack());
        chess.setProcessing(false);
    }

    public static void win(Chess chess, int letter, int number) {
        int letterTemp = letter;
        int numberTemp = number;
        int count = 4;
        char chessChar = chess.isBlack() ? '#' : '*';

        int charCount = 0;
        while (++letter < chess.getChessMap().length) {
            if (chess.getChessMap()[letter][number] == chessChar) charCount += 1;
            else break;
        }
        letter = letterTemp;
        while (--letter >= 0) {
            if (chess.getChessMap()[letter][number] == chessChar) charCount += 1;
            else break;
        }
        if (charCount >= count) {
            chess.setWin(true);
            return;
        }

        charCount = 0;
        letter = letterTemp;
        while (++number < chess.getChessMap().length) {
            if (chess.getChessMap()[letter][number] == chessChar) charCount += 1;
            else break;
        }
        number = numberTemp;
        while (--number >= 0) {
            if (chess.getChessMap()[letter][number] == chessChar) charCount += 1;
            else break;
        }
        if (charCount >= count) {
            chess.setWin(true);
            return;
        }

        charCount = 0;
        number = numberTemp;
        while (--letter >= 0 && --number >= 0) {
            if (chess.getChessMap()[letter][number] == chessChar) charCount += 1;
            else break;
        }
        letter = letterTemp;
        number = numberTemp;
        while (++number < chess.getChessMap().length && ++letter < chess.getChessMap().length) {
            if (chess.getChessMap()[letter][number] == chessChar) charCount += 1;
            else break;
        }
        if (charCount >= count) {
            chess.setWin(true);
            return;
        }

        charCount = 0;
        letter = letterTemp;
        number = numberTemp;
        while (++letter < chess.getChessMap().length && --number >= 0) {
            if (chess.getChessMap()[letter][number] == chessChar) charCount += 1;
            else break;
        }
        letter = letterTemp;
        number = numberTemp;
        while (++number < chess.getChessMap().length && --letter >= 0) {
            if (chess.getChessMap()[letter][number] == chessChar) charCount += 1;
            else break;
        }
        if (charCount >= count) {
            chess.setWin(true);
        }
    }

    public static Chess isPlayer(long number) {
        for (Chess chess : CHESS) {
            if (chess.getBlack() == number || chess.getWhite() == number) return chess;
        }
        return null;
    }
}
