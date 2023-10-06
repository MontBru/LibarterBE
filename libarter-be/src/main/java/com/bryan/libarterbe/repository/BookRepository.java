package com.bryan.libarterbe.repository;

import com.bryan.libarterbe.model.Book;
import com.bryan.libarterbe.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Page<Book> findBooksByNameContainingOrDescriptionContainingIgnoreCaseAndPriceBetween(String searchTerm, String searchTermDescription, double minPrice, double maxPrice, Pageable pageable);

    Page<Book> findBooksByTagsTextContainingIgnoreCaseAndPriceBetween(String searchTerm, double minPrice, double maxPrice, Pageable pageable);
    Page<Book> findBooksByAuthorContainingIgnoreCaseAndPriceBetween(String searchTerm, double minPrice, double maxPrice, Pageable pageable);
}
