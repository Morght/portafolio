package com.morght.foro_hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.morght.foro_hub.models.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long> {

}
