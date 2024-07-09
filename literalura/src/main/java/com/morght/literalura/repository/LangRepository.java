package com.morght.literalura.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.morght.literalura.models.Language;

public interface LangRepository extends JpaRepository<Language, Long> {

    Optional<Language> findByName(String name);

}
