package com.urlshortner.shortify.DTOs;

import lombok.Data;

@Data
public class UpdateExpiryDateDTO {
    String shortUrl;
    int noOfDays;
}
