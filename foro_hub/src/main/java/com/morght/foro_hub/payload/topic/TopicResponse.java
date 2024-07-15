package com.morght.foro_hub.payload.topic;

import java.time.LocalDateTime;
import java.util.List;

import com.morght.foro_hub.models.Answer;
import com.morght.foro_hub.models.Topic;

public record TopicResponse(
        Long id,
        String title,
        String message,
        LocalDateTime creationDate,
        Long ownerId,
        List<Answer> answers) {

    public TopicResponse(Topic t) {
        this(t.getId(), t.getTitle(), t.getMessage(), t.getCreationDate(), t.getOwner().getId(), t.getAnswers());
    }
}
