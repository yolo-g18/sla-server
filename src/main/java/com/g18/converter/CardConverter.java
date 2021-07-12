package com.g18.converter;

import com.g18.dto.CardDto;
import com.g18.dto.EventDto;
import com.g18.entity.Card;
import com.g18.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class CardConverter {
    public CardDto toDto(Card card){
        CardDto cardDto = new CardDto();
        cardDto.setStudySetID(card.getStudySet().getId());
        cardDto.setId(card.getId());
        cardDto.setFront(card.getFront());
        cardDto.setBack(card.getBack());
        return cardDto;
    }
}
