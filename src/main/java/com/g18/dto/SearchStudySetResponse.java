package com.g18.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchStudySetResponse {
    private Long id;
    private String creator;
    private String creatorAvatar;
    private String title;
    private String description;
    private List<CardSearchResponse> first4Cards;
    private String tag;
    private int numberOfCards;
    private Instant createdDate;



}
