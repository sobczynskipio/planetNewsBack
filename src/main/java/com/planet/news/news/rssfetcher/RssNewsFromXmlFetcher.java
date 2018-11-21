package com.planet.news.news.rssfetcher;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Configuration
@ComponentScan
public class RssNewsFromXmlFetcher
{

    private DocumentBuilder builder;
    private Document doc;

    public String fetchTagAttributeValueAtPath(String xml, String attribute, String path){
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(xml)));
        }
        catch (IOException | SAXException | ParserConfigurationException e)
        {
            e.printStackTrace();
        }

        NodeList nList = doc.getElementsByTagName("item");
        Element item = (Element) nList.item(0);
        NodeList descrList = item.getElementsByTagName("description");
        Element descr = (Element)descrList.item(0);
        Node imgCdata = descr.getChildNodes().item(1);
        org.jsoup.nodes.Document imgTag = Jsoup.parseBodyFragment(((CharacterData)imgCdata).getData());

        return imgTag.childNode(0).childNode(1).childNode(0).attr(attribute);
    }

}
