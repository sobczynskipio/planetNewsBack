package com.planet.news.news.rssfetcher;

import java.io.IOException;
import java.io.StringReader;
import java.util.EmptyStackException;
import java.util.Stack;

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

import com.planet.news.news.utils.PlanetNewsUtils;

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

        Stack<String> pathStack = PlanetNewsUtils.getStackOfArray(path.split("/"), true);

        NodeList bottomElementList = getBottomElementListAtPath(doc.getElementsByTagName(pathStack.pop()), pathStack);
        return getCdataHtmlTagAttribute((Element)bottomElementList.item(0), attribute);
    }

    private NodeList getBottomElementListAtPath(NodeList nodeList, Stack<String> pathComponents){
        String currentComponent = "";
        try
        {
            currentComponent = pathComponents.pop();
            if (!currentComponent.contains("CDATA"))
            {
                return getBottomElementListAtPath(((Element)nodeList.item(0)).getElementsByTagName(currentComponent), pathComponents);
            }
        }catch (EmptyStackException | ClassCastException e){
            System.out.println("Exception in fetching xml " + e);
            return nodeList;
        }finally
        {
            return ((Element)nodeList.item(0)).getElementsByTagName(currentComponent);
        }
    }

    private String getCdataHtmlTagAttribute(Element element, String attribute){
        Node imgCdata = element.getChildNodes().item(1);
        org.jsoup.nodes.Document imgTag = Jsoup.parseBodyFragment(((CharacterData)imgCdata).getData());
        return imgTag.childNode(0).childNode(1).childNode(0).attr(attribute);
    }

}
