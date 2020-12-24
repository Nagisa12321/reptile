

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
import java.net.URLEncoder;

/**
 * 使用Jsoup解析帖子详情和评论
 *
 * @tag: url:https://algs4.cs.princeton.edu/home/
 * Created by monster on 2015/12/11.
 */
public class test {
    public static String spider(String url){
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
                String urlTail = linkAndTitle.attr("href");
                if(urlTail.indexOf('.')==0)
                    urlTail = urlTail.substring(urlTail.indexOf('/')+1);
                String theUrl = "http://news.gzhu.edu.cn/"+urlTail;
                //得到标题
                String theTitle = linkAndTitle.attr("title");
                System.out.println("标题：" + theTitle);
                //下载
                source.download(theUrl,theTitle+".html");
            }

            //获取下一页
            Elements nextPage = doc.getElementsByClass("Next");
            String nextTail = nextPage.attr("href");
            String nextP;
            if(nextTail.startsWith("ttgd"))
                nextP = "http://news.gzhu.edu.cn/"+nextTail;
            else
                nextP = "http://news.gzhu.edu.cn/ttgd/"+nextTail;
            return nextP;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) {
            String url = "http://news.gzhu.edu.cn/ttgd.htm";
            do{
                url = spider(url);
            }while (url!=null);
    }

}
