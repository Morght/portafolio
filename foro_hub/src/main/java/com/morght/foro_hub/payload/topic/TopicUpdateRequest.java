package com.morght.foro_hub.payload.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record TopicUpdateRequest(
        Long id,
        @NotEmpty @NotBlank String title,
        @NotEmpty @NotBlank String message) {

}
