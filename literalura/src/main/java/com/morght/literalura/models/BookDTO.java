package com.morght.literalura.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookDTO(
        @JsonAlias("id") Long gutenbergId,
        String title,
        List<AuthorDTO> authors,
        List<String> languages,
        String copyright,
        @JsonAlias("download_count") Long downloadCount) {
}
