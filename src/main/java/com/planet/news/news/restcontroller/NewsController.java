package com.planet.news.news.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planet.news.news.model.RssNews;
import com.planet.news.news.rssfetcher.RssPlanetNewsComposer;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private RssPlanetNewsComposer naszeMiastoFetcher;

    @GetMapping("/rss")
    public List<RssNews> getNews(){
        return naszeMiastoFetcher.getNews();
    }
}
