package com.jtchen.thread;

import java.io.*;
import java.net.*;

/************************************************
 *
 * @author jtchen
 * @date 2020/12/25 0:28
 * @version 1.0
 ************************************************/
@SuppressWarnings({"CommentedOutCode", "ResultOfMethodCallIgnored"})
public class ImgDownload implements Runnable{

    private final URL url;
    private final String filename;
    private final String tail;

    public ImgDownload(String url, String filename, String imgTail) throws MalformedURLException {
        this.url = new URL(url);
        this.filename = filename;
        this.tail = imgTail;
    }

    @Override
    public void run() {
        /*var fi = new File("./src/main/resources/" + filename);
        fi.mkdirs();*/
        try {
            saveBinaryFile(url);
        } catch (IOException e) {
            System.err.println(e.toString() + "保存文件错误!");
        }
    }

    private void saveBinaryFile(URL u) throws IOException {
        URLConnection uc =
                u.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 1080)));
        String contentType = uc.getContentType();
        int contentLength = uc.getContentLength();
        if (contentType.startsWith("text/") || contentLength == -1) {
            throw new IOException("This is not a binary file.");
        }

        try (InputStream raw = uc.getInputStream()) {
            InputStream in = new BufferedInputStream(raw);
            byte[] data = new byte[contentLength];
            int offset = 0;
            while (offset < contentLength) {
                int bytesRead = in.read(data, offset, data.length - offset);
                if (bytesRead == -1) break;
                offset += bytesRead;
            }

            if (offset != contentLength) {
                throw new IOException("Only read " + offset
                        + " bytes; Expected " + contentLength + " bytes");
            }
            String tmp = tail.substring(0, 17);
//            var fi = new File("./src/main/resources/" + filename + tmp);
            var fi = new File("D:\\" + tmp);
            fi.mkdirs();

//            try (FileOutputStream fout = new FileOutputStream("./src/main/resources/" + filename + tail)) {
            try (FileOutputStream fout = new FileOutputStream("D:\\" + tail)) {
                fout.write(data);
                fout.flush();
            }
        }
    }
}
