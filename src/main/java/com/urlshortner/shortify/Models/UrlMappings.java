package com.urlshortner.shortify.Models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="UrlMappings")
@Data
public class UrlMappings {
    @Column(name ="UrlId")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long UrlId;


    @Column(name ="shortUrl", nullable = false, unique = true)
    private String shortUrl;

    @Column(name ="longUrl", nullable = false, unique = true)
    private String longUrl;

    @Column(name = "expiryDate", nullable = false)
    private LocalDateTime expiryDate;

}
