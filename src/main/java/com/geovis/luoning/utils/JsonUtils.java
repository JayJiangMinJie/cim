package com.geovis.luoning.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonUtils {

    //格式化json字符串
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }

    /**
     * 添加space
     * @param sb
     * @param indent
     * @author   xuhy
     * @Date   2015-10-14 上午10:38:04
     */
    public static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }
    //直接用字符流写入文本了. str表示已经经过上面方法格式化后的字符串
    public static void writeFile(String str, File file){
        String b=formatJson(str);
        try
        {
            // 建立文件对象
            // 向文件写入对象写入信息
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw=new BufferedWriter(fw);
            // 写文件
            bw.write(b);
            bw.flush();//强制输出下省得 en写入数据不完整
            // 关闭
            bw.close();
            fw.close();
        }
        catch (IOException e)
        {
            //
            e.printStackTrace();
        }
    }

}
