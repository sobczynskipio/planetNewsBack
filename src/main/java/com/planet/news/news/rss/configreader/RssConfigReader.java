package com.planet.news.news.rss.configreader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planet.news.news.model.RssNewsConfig;

@Configuration
@ComponentScan
public class RssConfigReader
{
    ObjectMapper objectMapper = new ObjectMapper();
    private String configFilePath = "classpath:news/rss/naszemiasto/naszemiasto.json";

    private List<RssNewsConfig> rssNewsConfigs;

    public RssConfigReader(){
        rssNewsConfigs = new ArrayList<>();
        try {
            rssNewsConfigs = objectMapper.readValue(readConfigFile(), new TypeReference<List<RssNewsConfig>>(){});
        }catch (IOException e ){
            System.out.println(e);
        }
    }

    public List<RssNewsConfig> getRssNewsConfigs()
    {
        return rssNewsConfigs;
    }

    private String readConfigFile() throws IOException
    {
        File configFile = ResourceUtils.getFile(configFilePath);
        return FileUtils.readFileToString(configFile, "UTF-8");
    }
}
