package online.afeibaili.util;

import online.afeibaili.Gobang;
import online.afeibaili.exception.FontFileNotFoundException;
import online.afeibaili.exception.PropertiesFileNotFoundException;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FileHandle {

    public static final Properties group = new Properties();

    static {
        load();
    }

    public static void load() {
        File file = new File(System.getProperty("user.dir") + "/config/gobang.properties");
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (parentFile.mkdirs()) {
                Gobang.INSTANCE.getLogger().info("");
            }
            Properties properties = new Properties();
            properties.put("groups", "975709430");
            try (FileWriter fileWriter = new FileWriter(file)) {
                properties.store(fileWriter, "use \"|\" split the group number");
            } catch (IOException e) {
                throw new RuntimeException("找不到" + file.getPath() + "文件异常" + e);
            }
            throw new PropertiesFileNotFoundException("找不到文件，已重新生成文件。请配置群号，配置" + file.getPath() + "文件并重启此插件!");
        }

        try (FileReader fileReader = new FileReader(file)) {
            group.load(fileReader);
        } catch (IOException e) {
            throw new RuntimeException("找不到" + file.getPath() + "文件异常" + e);
        }
    }

    public static List<Long> getAllGroupNumber() {
        String groupString = group.getProperty("groups");

        String[] split = groupString.split(" *\\| *");
        ArrayList<Long> list = new ArrayList<>();
        for (String group : split) {
            try {
                long l = Long.parseLong(group);
                list.add(l);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("无法转换" + group + "这个字符串为数字，请检查配置文件");
            }
        }
        return list;
    }

    public static Font getFontByFile(String fontName) {
        try (InputStream font = FileHandle.class.getClassLoader().getResourceAsStream(fontName)) {
            if (font != null) {
                return Font.createFont(Font.TRUETYPE_FONT, font);
            }
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException("无法加载字体文件" + e);
        }
        throw new FontFileNotFoundException("找不到字体文件");
    }
}
