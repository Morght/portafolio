package com.morght.foro_hub.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.morght.foro_hub.payload.topic.TopicCreationRequest;
import com.morght.foro_hub.payload.topic.TopicResponse;
import com.morght.foro_hub.payload.topic.TopicUpdateRequest;
import com.morght.foro_hub.service.TopicService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping
    public ResponseEntity<Page<TopicResponse>> getAll(@PageableDefault(size = 2) Pageable pagination) {
        return ResponseEntity.ok(topicService.getAllTopics(pagination));
    }

    @PostMapping
    public ResponseEntity<TopicResponse> create(
            @RequestBody @Valid TopicCreationRequest body,
            UriComponentsBuilder uriBilder,
            HttpServletRequest request) {
        TopicResponse topicResponse = topicService.createTopic(body, request);
        URI url = uriBilder.path("/api/topics/{id}").buildAndExpand(
                topicResponse.id())
                .toUri();
        return ResponseEntity.created(url).body(topicResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(topicService.getById(id));
    }

    @PutMapping
    public ResponseEntity<TopicResponse> update(@RequestBody @Valid TopicUpdateRequest body,
            UriComponentsBuilder uriBilder) {
        TopicResponse topicResponse = topicService.updateTopic(body);
        URI url = uriBilder.path("/api/topics/{id}").buildAndExpand(
                topicResponse.id())
                .toUri();
        return ResponseEntity.created(url).body(topicResponse);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        topicService.deleteTopic(id);
        return "Deleted";

    }
}
