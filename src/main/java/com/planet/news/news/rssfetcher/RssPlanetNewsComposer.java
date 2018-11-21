package com.planet.news.news.rssfetcher;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.planet.news.news.model.RssNews;
import com.planet.news.news.model.RssNewsConfig;
import com.planet.news.news.rss.configreader.RssConfigReader;

@Configuration
@ComponentScan
public class RssPlanetNewsComposer
{
    static final ClassLoader classLoader = RssPlanetNewsComposer.class.getClassLoader();


    private RestTemplate restTemplate = new RestTemplate();
    private DocumentBuilder builder;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    @Autowired
    private RssConfigReader rssConfigReader;

    @Autowired
    private RssNewsFromXmlFetcher rssNewsFromXmlFetcher;

    @Autowired
    private RssBillboardCreator rssBillboardCreator;

    public List<RssNews> getNews(){
        rssBillboardCreator.clearAllBillboards();
        ArrayList naszeMistoNews = new ArrayList<RssNews>();
        for(RssNewsConfig rssNewsConfig: rssConfigReader.getRssNewsConfigs()){
            String xml = restTemplate.getForObject(rssNewsConfig.getPath(), String.class);
            String billboardPath = fetchRssTitleImage(xml, rssNewsConfig.getImageLocation());
            naszeMistoNews.add(new RssNews(billboardPath, rssNewsConfig.getLongitude(), rssNewsConfig.getLatitude()));
        }
        return naszeMistoNews;
    }

    private String fetchRssTitleImage(String xml, String imageLocation) {
        return rssBillboardCreator.createBillboardFromExternalImage(
                   rssNewsFromXmlFetcher.fetchTagAttributeValueAtPath(xml, "src", imageLocation)
               );
    }
}
