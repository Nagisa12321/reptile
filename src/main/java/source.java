import java.io.*;
import java.net.*;

/************************************************
 *
 * @author jtchen
 * @date 2020/12/19 1:39
 * @version 1.0
 ************************************************/
public class source {
    public  static void download(String url,String filename){
        try (var out = new PrintStream("./src/main/resources/"+filename)) {
            URLConnection connection = new URL(url).openConnection
                    (new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 1080)));
            Reader r = new InputStreamReader(new BufferedInputStream(connection.getInputStream()));
            int c;
            while ((c = r.read()) != -1) {
                out.print((char) c);
            }
        } catch (MalformedURLException e) {
            System.err.println("is not a URL");
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    public static void main(String[] args) {
        if (args.length > 0) {
            try (var out = new PrintStream("./src/main/resources/source1.html")) {
                URLConnection connection = new URL(args[0]).openConnection
                        (/*new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 1080))*/);
                Reader r = new InputStreamReader(new BufferedInputStream(connection.getInputStream()));
                int c;
                while ((c = r.read()) != -1) {
                    out.print((char) c);
                }
            } catch (MalformedURLException e) {
                System.err.println("is not a URL");
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
}
