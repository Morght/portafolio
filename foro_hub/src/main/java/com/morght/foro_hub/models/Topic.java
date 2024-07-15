package com.morght.foro_hub.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.morght.foro_hub.payload.topic.TopicCreationRequest;
import com.morght.foro_hub.payload.topic.TopicUpdateRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "topics")
@Entity
@Getter
@NoArgsConstructor
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String message;
    private LocalDateTime creationDate;
    @Enumerated(EnumType.STRING)
    private State state;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private User owner;
    @OneToMany(mappedBy = "topic", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Answer> answers = new ArrayList<>();

    public Topic(User u, TopicCreationRequest data) {
        this.title = data.title();
        this.message = data.message();
        this.creationDate = LocalDateTime.now();
        this.state = State.OPEN;
        this.owner = u;
    }

    public void update(TopicUpdateRequest body) {
        this.title = body.title();
        this.message = body.message();
    }
}
