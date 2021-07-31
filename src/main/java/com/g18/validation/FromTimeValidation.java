package com.g18.validation;

import com.g18.dto.EventDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class FromTimeValidation implements ConstraintValidator<FromTime, EventDto> {


    @Override
    public void initialize(FromTime constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(EventDto eventDto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime ldtFromTime = LocalDateTime.ofInstant(Instant.parse(eventDto.getFromTime()), ZoneOffset.UTC);
        LocalDateTime ldtToTime = LocalDateTime.ofInstant(Instant.parse(eventDto.getToTime()), ZoneOffset.UTC);
        return ldtFromTime.isBefore(ldtToTime);
    }
}
