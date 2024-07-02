package com.urlshortner.shortify.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.urlshortner.shortify.DTOs.ShortUrlDTO;
import com.urlshortner.shortify.DTOs.UpdateExpiryDateDTO;
import com.urlshortner.shortify.DTOs.UpdateUrlRequest;
import com.urlshortner.shortify.Models.UrlMappings;
import com.urlshortner.shortify.Service.ShortenerService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/shortener")
public class ShortenerController {

    private final ShortenerService shortenerService;

    public ShortenerController(ShortenerService shortenerService) {
        super();
        this.shortenerService = shortenerService;
    }

    @PostMapping("generateShortUrl")
    public ResponseEntity<ShortUrlDTO> getShortUrl(@RequestBody String LongUrl) {
        UrlMappings url = shortenerService.shortenUrl(LongUrl);
        ShortUrlDTO s = new ShortUrlDTO();
        s.setId(url.getUrlId());
        s.setShortUrl(url.getShortUrl());
        return new ResponseEntity<ShortUrlDTO>(s, HttpStatus.OK);
    }

    @PostMapping("updateMapping")
    public ResponseEntity<Boolean> updateShortUrl(@RequestBody UpdateUrlRequest u) {
        boolean ans = shortenerService.updateUrl(u.getShortUrl(), u.getLongUrl());
        if (ans)
            return new ResponseEntity<>(ans, HttpStatus.OK);
        else
            return new ResponseEntity<>(ans, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("getDestinationUrl")
    public ResponseEntity<String> getDestinationUrl(@RequestParam String shortUrl) {
        String s = shortenerService.getDestinationUrl(shortUrl);
        if (s.equals("Some error occurred"))
            return new ResponseEntity<>("Some error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        else
            return new ResponseEntity<>(s, HttpStatus.OK);
    }

    @PostMapping("updateExpiry")
    public ResponseEntity<Boolean> updateExpiryDate(@RequestBody UpdateExpiryDateDTO u) {
        if (shortenerService.updateExpiry(u.getShortUrl(), u.getNoOfDays()))
            return new ResponseEntity<>(true, HttpStatus.OK);
        else
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{shortUrl}")
    public void redirectToOriginalUrl(@PathVariable String shortUrl, HttpServletResponse response) {
        String fullShortUrl = "http://localhost:8080/" + shortUrl; 
        String longUrl = shortenerService.getDestinationUrl(fullShortUrl);
        if (longUrl != null && !longUrl.isEmpty()) {
            try {
                response.sendRedirect(longUrl);
            } catch (Exception e) {
                // Handle exception if redirect fails
                e.printStackTrace();
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        } else {
            // Handle case where short URL doesn't map to any long URL
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }

}
