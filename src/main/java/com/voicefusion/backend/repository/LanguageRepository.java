package com.voicefusion.backend.repository;

import com.voicefusion.backend.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    Language findByName(String name);
}
