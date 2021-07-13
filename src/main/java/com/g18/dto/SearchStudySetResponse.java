package com.g18.dto;

import com.g18.entity.Card;
import com.g18.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchStudySetResponse {
    private Long id;
    private String creator;
    private String title;
    private List<Card> first4Cards;
    private int numberOfCards;

}
