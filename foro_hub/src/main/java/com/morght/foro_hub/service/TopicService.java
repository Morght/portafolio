package com.morght.foro_hub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.morght.foro_hub.infra.security.TokenService;
import com.morght.foro_hub.models.Topic;
import com.morght.foro_hub.models.User;
import com.morght.foro_hub.payload.topic.TopicCreationRequest;
import com.morght.foro_hub.payload.topic.TopicResponse;
import com.morght.foro_hub.payload.topic.TopicUpdateRequest;
import com.morght.foro_hub.repository.TopicRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class TopicService {

    @Autowired
    TopicRepository topicRepository;
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;

    public TopicResponse createTopic(TopicCreationRequest data, HttpServletRequest request) {
        // TODO prevent duplicate topic
        User user = userService.getUserByEmail(tokenService.getUserEmail(request));
        return new TopicResponse(
                topicRepository.save(
                        new Topic(user, data)

                ));
    }

    public TopicResponse getById(Long id) {
        // TODO valid existance
        return new TopicResponse(topicRepository.getReferenceById(id));
    }

    public Page<TopicResponse> getAllTopics(Pageable pagination) {
        Page<Topic> topics = topicRepository.findAll(pagination);
        return topics.map(TopicResponse::new);
    }

    @Transactional
    public TopicResponse updateTopic(TopicUpdateRequest body) {
        Topic topic = topicRepository.getReferenceById(body.id());
        topic.update(body);
        return new TopicResponse(topic);
    }

    public void deleteTopic(Long id) {
        Topic topic = topicRepository.getReferenceById(id);
        topicRepository.delete(topic);
    }
}
