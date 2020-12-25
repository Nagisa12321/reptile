package com.cc.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/************************************************
 * @author jtchen
 * @date 2020/12/22 23:16
 * @version 1.0
 ************************************************/
@SuppressWarnings({"SpellCheckingInspection"})
public class Spider implements Runnable {
    private final String url;
    private static final ExecutorService pool = Executors.newFixedThreadPool(100);

    public Spider(String url) {
        this.url = url;
    }

    @Override
    public void run() {
        try {
            Document doc = Jsoup.connect(url)./*
                    proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 1080))).*/get();
            //根据key为id,value以line_u开头得到元素 重新生成doc以便dom操作
            Elements container = doc.getElementsByAttributeValueStarting("id", "line_u");
            Document containerDoc = Jsoup.parse(container.toString());

            //All I want!
            Elements LinksAndTitles = containerDoc.select("a[href]");
            Elements Times = containerDoc.select("span[class]");
            if (LinksAndTitles.size() != Times.size())
                throw new IllegalArgumentException("时间和标题数目不同, 抓包异常! ");

            //遍历爬取到的元素 进行处理
            for (int i = 0; i < Times.size(); i++) {
                //生成子链接地址尾部，并得到时间
                String urlTail = LinksAndTitles.get(i).attr("href");
                String time = Times.get(i).toString().substring(19, 29);
                if (urlTail.indexOf('.') == 0)
                    urlTail = urlTail.substring(urlTail.indexOf('/') + 1);

                //获得子链接
                String theUrl = "http://news.gzhu.edu.cn/" + urlTail;

                //获取图片链接 以Elements的形式保存
                Document doc1 = Jsoup.connect(theUrl)./*
                        proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 1080))).*/get();
                Elements container1 = doc1.getElementsByAttributeValueStarting("style", "text-align:");
                Document containerDoc1 = Jsoup.parse(container1.toString());
                Elements pngs = containerDoc1.select("img[width]");

                //获取标题
                String theTitle = LinksAndTitles.get(i).attr("title");

                //下载相应资源
                System.out.println("时间: " + time);
                System.out.println("标题: " + theTitle);
                Runnable SourceDL = new GetSource(theUrl, RemoveSpaces(theTitle), pngs);
                SourceDL.run();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取所有的页面
    public static List<String> getAllPage() {
        List<String> allPage = new ArrayList<>();
        try {
            String baseUrl = "http://news.gzhu.edu.cn/";
            allPage.add(baseUrl + "ttgd.htm");

            Document doc = Jsoup.connect(baseUrl + "ttgd.htm").get();
            Elements nextP = doc.getElementsByClass("Next");
            String nextTail = nextP.attr("href");
            String nextPage;
            nextPage = "http://news.gzhu.edu.cn/" + nextTail;
            allPage.add(nextPage);

            while (true) {
                doc = Jsoup.connect(nextPage).get();
                nextP = doc.getElementsByClass("Next");
                nextTail = nextP.attr("href");
                if (nextTail.equals(""))
                    break;
                nextPage = "http://news.gzhu.edu.cn/ttgd/" + nextTail;
                allPage.add(nextPage);
            }
            return allPage;
        } catch (IOException e) {
            return allPage;
        }

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

    public static void main(String[] args) throws InterruptedException {
        System.out.println("===============================================正在获取全部页面......===============================================");
        List<String> test = getAllPage();
        System.out.println("一共获取到" + test.size() + "页,请输入您要爬的页数:");
        Scanner in = new Scanner(System.in);
        int page = Integer.parseInt(in.next());
        if (page > test.size()) {
            System.err.println("爬不到那么多页，呵呵");
            return;
        }

        //一个页面一个线程
        System.out.println("===============================================开始爬取O(∩_∩)O.===============================================");
        for (int i = 0; i < page; ++i) {
            Runnable spider = new Spider(test.get(i));
            pool.submit(spider);
        }
        pool.shutdown();
    }

}
