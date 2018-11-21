package com.planet.news.news.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RssNews
{
    private String imageLink;
    private Double longitude;
    private Double latitude;
}
