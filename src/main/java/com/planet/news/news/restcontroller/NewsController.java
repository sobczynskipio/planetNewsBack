package com.planet.news.news.restcontroller;

import com.planet.news.news.model.NaszeMiastoNews;
import com.planet.news.news.rssfetcher.NaszeMiastoFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NaszeMiastoFetcher naszeMiastoFetcher;

    @GetMapping("/rss")
    public List<NaszeMiastoNews> getNews(){
        return naszeMiastoFetcher.getNews();
    }
}
