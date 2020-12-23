

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
                    proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 1080))).get();

            //根据key为id,value以line_u开头得到元素
            Elements container = doc.getElementsByAttributeValueStarting("id","line_u");
            //重新生成doc以便dom操作
            Document containerDoc = Jsoup.parse(container.toString());
            //AllIwant!
            Elements ALLIwant = containerDoc.select("a[href]");

            //遍历爬取到的元素 进行处理
            for(Element linkAndTitle:ALLIwant){
                //生成地址
                String theUrl = "http://news.gzhu.edu.cn/"+linkAndTitle.attr("href");
                //得到标题
                String theTitle = linkAndTitle.attr("title");
                System.out.println("标题：" + theTitle);
                //下载
                source.download(theUrl,theTitle+".html");
            }



            //String articleTitle = containerDoc.getElementsByAttribute("title").text();
//            String authorName = containerDoc.getElementById("authorName").text();
//            String time = containerDoc.select("span").first().text();
//            String imgphotoUrl=containerDoc.select("img").get(1).attr("src");
            //System.out.println("标题：" + articleTitle); //标题
//            System.out.println("作者："+authorName); //作者
//            System.out.println("发布时间："+time); //发布时间
//            System.out.println("作者头像的url："+imgphotoUrl); //发布时间

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
