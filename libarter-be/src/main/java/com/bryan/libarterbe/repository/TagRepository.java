package com.bryan.libarterbe.repository;

import com.bryan.libarterbe.model.Role;
import com.bryan.libarterbe.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Integer> {
    Tag findByText(String text);
}
