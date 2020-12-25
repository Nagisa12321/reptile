package com.jtchen.thread;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;

/************************************************
 *
 * @author jtchen
 * @date 2020/12/25 0:28
 * @version 1.0
 ************************************************/
@SuppressWarnings({"CommentedOutCode", "ResultOfMethodCallIgnored"})
public class ImgDownload implements Runnable {
    private  final Elements  pngs;
    private  final String filename;

    public ImgDownload(Elements pngs,String rawFileName){
        this.pngs = pngs;
        filename = RemoveSpaces(rawFileName);
    }

    @Override
    public void run() {
        try {
            //下载一个页面图片
            for (Element png : pngs) {
                String tail = png.attr("src");
                String pngUrl = "http://news.gzhu.edu.cn" + tail;
                saveBinaryFile(pngUrl,tail);
            }

        } catch (IOException e) {
            System.err.println(e.toString() + "保存文件错误!");
        }
    }

    private void saveBinaryFile(String u,String tail) throws IOException {
            //简单搞搞文件名
            String tmp = tail.substring(0, 17);
            var fi = new File("./src/main/resources/" + filename + tmp);
            fi.mkdirs();

            //下载
            Connection.Response resultImageResponse = Jsoup.connect(u).ignoreContentType(true).execute();
            FileOutputStream out = new FileOutputStream("./src/main/resources/" + filename + tail);
            out.write(resultImageResponse.bodyAsBytes());
            out.close();

    }


    /* 去掉文件名开头、结尾的空格、特殊符号 */
    public static String RemoveSpaces(String s) {
        int idx = 0;
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) == ' ') idx++;
            else break;
        s = idx == 0 ? s : s.substring(idx);
        idx = s.length() - 1;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == ' ') idx--;
            else break;
        }
        s = idx == s.length() - 1 ? s : s.substring(0, idx + 1);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != '"'
                    && s.charAt(i) != '?'
                    && s.charAt(i) != '\\'
                    && s.charAt(i) != '/'
                    && s.charAt(i) != ':')
                builder.append(s.charAt(i));
        }
        return builder.toString();
    }
}
