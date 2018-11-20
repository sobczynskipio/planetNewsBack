package com.planet.news.news.rssfetcher;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.planet.news.news.model.RssNews;
import com.planet.news.news.rss.configreader.RssConfigReader;

@Configuration
@ComponentScan
public class RssNewsFetcher
{

    static final ClassLoader classLoader = RssNewsFetcher.class.getClassLoader();
    @Value ("${billboards.path}")
    private String billboardsPath;
    private RestTemplate restTemplate = new RestTemplate();
    private DocumentBuilder builder;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    @Autowired
    private RssConfigReader rssConfigReader;

    public RssNewsFetcher() {

    }

    public List<RssNews> getNews(){
        ArrayList naszeMistoNews = new ArrayList<RssNews>();
        naszeMistoNews.add(new RssNews(fetchRssTitleImage(), "", ""));
        return naszeMistoNews;
    }

    private String fetchRssTitleImage() {
        String xml = restTemplate.getForObject(rssConfigReader.getRssNewsConfigs().get(0).getPath(), String.class);

        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xml)));
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("item");
            System.out.println(nList.getLength());

            Element item = (Element) nList.item(0);
            NodeList descrList = item.getElementsByTagName("description");
            System.out.println(descrList.getLength());
            Element descr = (Element)descrList.item(0);
            Node imgCdata = descr.getChildNodes().item(1);
            org.jsoup.nodes.Document imgTag = Jsoup.parseBodyFragment(((CharacterData)imgCdata).getData());

            createPlanetNewsBillboardImage(imgTag.childNode(0).childNode(1).childNode(0).attr("src"));

            return imgTag.childNode(0).childNode(1).childNode(0).attr("src");

        }catch (IOException | SAXException | ParserConfigurationException e){
            System.out.print(e);
        }
        return "";
    }

    private String createPlanetNewsBillboardImage(String imageExternalPath){
        URL url = null;

        try
        {
            url = new URL(imageExternalPath);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }


        try( InputStream is = url.openStream(); OutputStream os = new FileOutputStream(billboardsPath + "image.jpg"))
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

        return billboardsPath + "image.jpg";
    }
}
