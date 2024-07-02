package com.urlshortner.shortify.Service.Implementation;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.urlshortner.shortify.Models.UrlMappings;
import com.urlshortner.shortify.Service.ShortenerService;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class ShortenerServiceImplementation implements ShortenerService {

    private static final String BASE_URL = "http://localhost:8080/";
    private static final int SHORT_URL_LENGTH = 8;
    private static final long EXPIRY_DURATION = 10 * 30L;

    @Autowired
    private EntityManager entityManager;

    public String generateShortUrl() {
        Random random = new Random();
        StringBuilder shortUrl = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            shortUrl.append(characters.charAt(random.nextInt(characters.length())));
        }

        return BASE_URL + shortUrl.toString();
    }

    @Transactional
    @Override
    public UrlMappings shortenUrl(String longUrl) {
        String shortUrl = generateShortUrl();
        UrlMappings urlMappings = new UrlMappings();
        urlMappings.setLongUrl(longUrl);
        urlMappings.setShortUrl(shortUrl);
        urlMappings.setExpiryDate(LocalDateTime.now().plusDays(EXPIRY_DURATION));
        entityManager.persist(urlMappings);
        return urlMappings;
    }

    @Override
    @Transactional
    public Boolean updateUrl(String shortUrl, String longUrl) {
        try {
            UrlMappings urlMappings = entityManager
                    .createQuery("select url from UrlMappings  url where url.shortUrl =:shortUrl", UrlMappings.class)
                    .setParameter("shortUrl", shortUrl).getSingleResult();
            if (urlMappings != null) {
                urlMappings.setLongUrl(longUrl);
                return true;

            } else
                return false;

        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public String getDestinationUrl(String shortUrl) {
        try {
            System.out.println("Getting destination URL for shortUrl: " + shortUrl);
            UrlMappings urlMappings = entityManager
                    .createQuery("select url from UrlMappings url where url.shortUrl =:shortUrl", UrlMappings.class)
                    .setParameter("shortUrl", shortUrl).getSingleResult();
            return urlMappings.getLongUrl();
        } catch (Exception e) {
            return "Some error occurred";
        }
    }

    @Override
    @Transactional
    public Boolean updateExpiry(String shortUrl, int noOfDays) {
        try {
            UrlMappings urlMappings = entityManager
                    .createQuery("select url from UrlMappings  url where url.shortUrl =:shortUrl", UrlMappings.class)
                    .setParameter("shortUrl", shortUrl).getSingleResult();
            urlMappings.setExpiryDate(urlMappings.getExpiryDate().plusDays(noOfDays));
            entityManager.merge(urlMappings);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
