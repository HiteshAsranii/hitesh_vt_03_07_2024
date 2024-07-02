package com.urlshortner.shortify.DTOs;

import lombok.Data;

@Data
public class UpdateUrlRequest {
    String shortUrl;
    String longUrl;   
}
