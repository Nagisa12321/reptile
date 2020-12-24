package com.cc.spider;

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
public class GetSource {
    public static void download(String url, String filename) {
        try (var outputStream = new FileOutputStream("./src/main/resources/" + filename)) {
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
