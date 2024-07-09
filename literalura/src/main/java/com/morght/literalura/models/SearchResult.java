package com.morght.literalura.models;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SearchResult(
                int count,
                String next,
                String previous,
                ArrayList<BookDTO> results) {
}
