package com.cc.spider;

import com.jtchen.thread.HtmlDownload;
import com.jtchen.thread.ImgDownload;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

/************************************************
 *
 * @author jtchen
 * @date 2020/12/19 1:39
 * @version 1.0
 ************************************************/
@Deprecated
public class GetSource implements Runnable{
    private final Elements pngs;
    private final String url;
    private final String rawFileName;
    public GetSource(String url, String rawFileName,Elements pngs){
        this.pngs = pngs;
        this.rawFileName = rawFileName;
        this.url = url;
    }
    @Override
    public void run() {
        ImgDownload imgDL = new ImgDownload(pngs,rawFileName);
        HtmlDownload htmlDL = new HtmlDownload(url,rawFileName);
        imgDL.run();
        htmlDL.run();
    }
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void download(String url, String filename) {
        var fi = new File("./src/main/resources/" + filename);
        fi.mkdirs();
        try (var outputStream = new FileOutputStream(fi.getAbsoluteFile() +  "\\" + filename + ".html")) {
            URLConnection connection = new URL(url).openConnection
                    (/*new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 1080))*/);
            Reader r = new InputStreamReader(new BufferedInputStream(connection.getInputStream()));
            int c;
            while ((c = r.read()) != -1) {
                outputStream.write(c);
                //out.print((char) c);
            }
            r.close();
        } catch (MalformedURLException e) {
            System.err.println("is not a URL");
        } catch (IOException e) {
            System.err.println("下载错误！");
            System.err.println(e.toString());
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            try (var outputStream = new FileOutputStream("./src/main/resources/test.html")) {
                URLConnection connection = new URL(args[0]).openConnection
                        (/*new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 1080))*/);
                Reader r = new InputStreamReader
                        (new BufferedInputStream(connection.getInputStream()), StandardCharsets.ISO_8859_1);
                int c;
                while ((c = r.read()) != -1) {
                    outputStream.write(c);
                    //out.print((char) c);
                }
                r.close();
            } catch (MalformedURLException e) {
                System.err.println("is not a URL");
            } catch (IOException e) {
                System.err.println(e.toString());
            }
        }
    }

}
