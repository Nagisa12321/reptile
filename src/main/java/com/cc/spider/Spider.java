package com.cc.spider;


import com.jtchen.thread.HtmlDownload;
import com.jtchen.thread.ImgDownload;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/************************************************
 * @author jtchen
 * @date 2020/12/22 23:16
 * @version 1.0
 ************************************************/
@SuppressWarnings({"CommentedOutCode", "SpellCheckingInspection"})
public class Spider {
    private static final ExecutorService pool = Executors.newFixedThreadPool(50);

    public static String spider(String url) {
        try {
            Document doc = Jsoup.connect(url)./*
                    proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 1080))).*/get();
            //根据key为id,value以line_u开头得到元素
            Elements container = doc.getElementsByAttributeValueStarting("id", "line_u");
            //重新生成doc以便dom操作
            Document containerDoc = Jsoup.parse(container.toString());
            //All I want!
            Elements LinksAndTitles = containerDoc.select("a[href]");
            Elements Times = containerDoc.select("span[class]");
            if (LinksAndTitles.size() != Times.size())
                throw new IllegalArgumentException("时间和标题数目不同, 抓包异常! ");

            //遍历爬取到的元素 进行处理
            for (int i = 0; i < Times.size(); i++) {
                //生成地址
                String urlTail = LinksAndTitles.get(i).attr("href");
                String time = Times.get(i).toString().substring(19, 29);
                if (urlTail.indexOf('.') == 0)
                    urlTail = urlTail.substring(urlTail.indexOf('/') + 1);

                String theUrl = "http://news.gzhu.edu.cn/" + urlTail;


                /* 冲到URL里面获取图片链接 */
                Document doc1 = Jsoup.connect(theUrl)./*
                        proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 1080))).*/get();
                //根据key为id,value以line_u开头得到元素
                Elements container1 = doc1.getElementsByAttributeValueStarting("style", "text-align:");

                /*
                container1.removeIf(x ->
                        !x.toString().startsWith("<p style=\"text-align: center;\"><img width=\"500\""));
                */

                //重新生成doc以便dom操作
                Document containerDoc1 = Jsoup.parse(container1.toString());
                //图片链接获取
                Elements pngs = containerDoc1.select("img[width]");

                //得到标题
                String theTitle = LinksAndTitles.get(i).attr("title");
                System.out.println("时间: " + time);
                System.out.println("标题: " + theTitle);
                //下载
//                GetSource.download(theUrl, theTitle);
                theTitle = RemoveSpaces(theTitle);

                //下载图片
                for (Element png : pngs) {
                    String tail = png.attr("src");
                    String pngUrl = "http://news.gzhu.edu.cn" + tail;
                    Runnable imgDl = new ImgDownload(pngUrl, theTitle, tail);
                    pool.submit(imgDl);
                }

                Runnable htmlDl = new HtmlDownload(theUrl, theTitle);
                pool.submit(htmlDl);
            }

            //获取下一页
            Elements nextPage = doc.getElementsByClass("Next");
            String nextTail = nextPage.attr("href");
            String nextP;
            if (nextTail.startsWith("ttgd"))
                nextP = "http://news.gzhu.edu.cn/" + nextTail;
            else
                nextP = "http://news.gzhu.edu.cn/ttgd/" + nextTail;
            return nextP;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

    public static void main(String[] args) {
        String url = "http://news.gzhu.edu.cn/ttgd.htm";

//        do {
//            url = spider(url);
//        } while (url != null);

        for (int i = 0; i < 10; i++) {
            url = spider(url);
        }
        pool.shutdown();
    }

}
