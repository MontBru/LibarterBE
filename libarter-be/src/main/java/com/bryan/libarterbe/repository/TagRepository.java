package com.bryan.libarterbe.repository;

import com.bryan.libarterbe.model.Role;
import com.bryan.libarterbe.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TagRepository extends JpaRepository<Tag,Integer> {
    Tag findByText(String text);

    @Query("SELECT COUNT(book) > 0 FROM Tag tag " +
            "JOIN tag.books book " +
            "WHERE tag.id = :tagId")
    boolean hasBooks(int tagId);
}
