package com.example.xrefintegration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // ignore any fields that not defined
public class Published {

    @JsonProperty("date-parts")
    private List<List<Integer>> dateParts;

    public LocalDate getPublishedDate() {
        if (dateParts != null && !dateParts.isEmpty()) {
            List<Integer> parts = dateParts.get(0);
            int year = !parts.isEmpty() ? parts.get(0) : 0;
            int month = parts.size() > 1 ? parts.get(1) : 1;
            int day = parts.size() > 2 ? parts.get(2) : 1;
            return LocalDate.of(year, month, day);
        }
        return null;
    }

}
