package com.urlshortner.shortify.Service;

import org.springframework.stereotype.Service;

import com.urlshortner.shortify.Models.UrlMappings;

@Service
public interface ShortenerService {
    public UrlMappings shortenUrl(String longUrl);

    public Boolean updateUrl(String shortUrl, String longUrl);

    public String getDestinationUrl(String shortUrl);

    public Boolean updateExpiry(String shortUrl, int noOfDays);
}
