package com.cc.spider;

import com.jtchen.thread.HtmlDownload;
import com.jtchen.thread.ImgDownload;
import org.jsoup.select.Elements;

/************************************************
 *
 * @author jtchen
 * @date 2020/12/19 1:39
 * @version 1.0
 ************************************************/
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

}
