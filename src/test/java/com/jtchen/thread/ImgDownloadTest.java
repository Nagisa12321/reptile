package com.jtchen.thread;

import org.junit.Test;

import java.net.MalformedURLException;

public class ImgDownloadTest {

    @Test
    public void run() throws MalformedURLException {
        String tail = "\\__local\\6\\4E\\5D\\E5BE7CB3D29C31228E3C7BCB185_171F3FCE_31419.jpg";
        System.out.println(tail.substring(0, 17));
        ImgDownload test = new ImgDownload
                ("http://news.gzhu.edu.cn/__local/1/2B/8D/1AF47C8B558517C7329608F09CA_409D7466_2DC5F.jpg"
                        , "",
                        "/test.png");
        test.run();
    }
}