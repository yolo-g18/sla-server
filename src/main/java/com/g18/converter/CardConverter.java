package com.g18.converter;

import com.g18.dto.CardSearchResponse;
import com.g18.entity.Card;
import org.springframework.stereotype.Component;

@Component
public class CardConverter {
    public CardSearchResponse toDto(Card card){
        CardSearchResponse cardSearchResponse = new CardSearchResponse();
        cardSearchResponse.setStudySetID(card.getStudySet().getId());
        cardSearchResponse.setId(card.getId());
        cardSearchResponse.setFront(card.getFront());
        cardSearchResponse.setBack(card.getBack());
        return cardSearchResponse;
    }
}
