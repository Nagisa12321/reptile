

/************************************************
 *
 * @author jtchen
 * @date 2020/12/22 23:16
 * @version 1.0
 ************************************************/

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * 使用Jsoup解析帖子详情和评论
 *
 * @tag: url:https://algs4.cs.princeton.edu/home/
 * Created by monster on 2015/12/11.
 */
public class test {

    public static void main(String[] args) {
        final String url = "http://news.gzhu.edu.cn/ttgd.htm";
        try {

            Document doc = Jsoup.connect(url).
                    /*proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 1080))).*/get();

            Elements container = doc.getElementsByAttribute("title");

            Document containerDoc = Jsoup.parse(container.toString());

            String articleTitle = containerDoc.getElementsByAttribute("title").text();
//            String authorName = containerDoc.getElementById("authorName").text();
//            String time = containerDoc.select("span").first().text();
//            String imgphotoUrl=containerDoc.select("img").get(1).attr("src");
            System.out.println("标题：" + articleTitle); //标题
//            System.out.println("作者："+authorName); //作者
//            System.out.println("发布时间："+time); //发布时间
//            System.out.println("作者头像的url："+imgphotoUrl); //发布时间

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
