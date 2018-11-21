package com.planet.news.news.rssfetcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class RssBillboardCreator
{
    private static String BILLBOARDS_SUBDIR = "/billboards";
    @Value ("${static.resources.path}")
    private String staticResourcesPath;

    public String createBillboardFromExternalImage(String externalImagePath){

        URL url = null;
        Pattern filenamePattern = Pattern.compile("\\/(?:.(?!\\/))+$");
        Matcher matcher = filenamePattern.matcher(externalImagePath);
        matcher.find();

        try
        {
            url = new URL(externalImagePath);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }


        try(InputStream is = url.openStream(); OutputStream os = new FileOutputStream(staticResourcesPath + BILLBOARDS_SUBDIR + matcher.group(0)))
        {
            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return BILLBOARDS_SUBDIR + matcher.group(0);
    }

    public void clearAllBillboards(){
        try
        {
            FileUtils.cleanDirectory(new File(staticResourcesPath + BILLBOARDS_SUBDIR));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
