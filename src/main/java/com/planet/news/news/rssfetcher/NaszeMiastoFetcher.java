package com.planet.news.news.rssfetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planet.news.news.model.NaszeMiastoNews;
import com.planet.news.news.model.NaszeMiastoNewsConfig;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;

@Configuration
@ComponentScan
public class NaszeMiastoFetcher {

    static final ClassLoader classLoader = NaszeMiastoFetcher.class.getClassLoader();
    private NaszeMiastoNewsConfig naszeMiastoNewsConfig;
    private String configFilePath = "classpath:news/naszemiasto/naszemiasto.json";
    private RestTemplate restTemplate = new RestTemplate();
    ObjectMapper objectMapper = new ObjectMapper();
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;

    public NaszeMiastoFetcher() {
        try {
            naszeMiastoNewsConfig = objectMapper.readValue(readConfigFile(), NaszeMiastoNewsConfig.class);
            builder = factory.newDocumentBuilder();
        }catch (IOException e ){
            System.out.println(e);
        }catch(ParserConfigurationException e){
            System.out.println(e);
        }
    }

    public List<NaszeMiastoNews> getNews(){
        ArrayList naszeMistoNews = new ArrayList<NaszeMiastoNews>();
        naszeMistoNews.add(new NaszeMiastoNews(fetchRssTitleImage(), "", ""));
        return naszeMistoNews;
    }

    private String fetchRssTitleImage() {
        String xml = restTemplate.getForObject(naszeMiastoNewsConfig.getPath(), String.class);

        try {
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
            return imgTag.childNode(0).childNode(1).childNode(0).attr("src");

        }catch (IOException | SAXException e){
            System.out.print(e);
        }
        return "";
    }

    private String readConfigFile() throws IOException{
        File configFile = ResourceUtils.getFile(configFilePath);
        return FileUtils.readFileToString(configFile, "UTF-8");
    }
}
