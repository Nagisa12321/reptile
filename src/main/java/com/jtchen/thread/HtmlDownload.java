package com.jtchen.thread;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

/************************************************
 *
 * @author jtchen
 * @date 2020/12/24 21:37
 * @version 1.0
 ************************************************/
public class HtmlDownload implements Runnable {
    private final String url;
    private final String filename;

    public HtmlDownload(String url, String filename) {
        this.filename = (filename);
        this.url = url;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void run() {
        var fi = new File("./src/main/resources/" + filename);
        fi.mkdirs();
        try (var out = new FileOutputStream("./src/main/resources/" + filename + "/" + filename + ".html")) {
            URLConnection connection = new URL(url).openConnection
                    (/*new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 1080))*/);
            Reader r = new InputStreamReader
                    (new BufferedInputStream(connection.getInputStream()), StandardCharsets.ISO_8859_1);
            int c;

            String aims = "src=\""; //目标匹配字符串
            int index = 0;

            while ((c = r.read()) != -1) {
                out.write((char) c);

                if (c == aims.charAt(index)) index++;//下标加一
                else index = 0;                      //下标归零

                /* 若下标匹配到aims长度说明完全匹配, 添加‘.’! */

                if (index == aims.length()) {
                    out.write('.');
                    index = 0;
                }
            }
            out.flush();
            r.close();

        } catch (MalformedURLException e) {
            System.err.println("is not a URL");
        } catch (IOException e) {
            System.err.println("下载错误！");
            System.err.println(e.toString());
        }
    }
}
