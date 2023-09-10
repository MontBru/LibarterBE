package com.bryan.libarterbe.repository;

import com.bryan.libarterbe.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findBookByNameContainingOrDescriptionContaining(String searchTerm, String searchTermDescription, Pageable pageable);
}
