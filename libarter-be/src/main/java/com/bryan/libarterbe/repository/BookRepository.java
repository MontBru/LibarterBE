package com.bryan.libarterbe.repository;

import com.bryan.libarterbe.model.Book;
import com.bryan.libarterbe.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Page<Book> findBooksByNameContainingIgnoreCaseAndPriceBetweenAndIsRequestIsFalseOrDescriptionContainingIgnoreCaseAndPriceBetweenAndIsRequestIsFalse(String searchTerm, double minPrice, double maxPrice, String searchTermDescription, double minPrice2, double maxPrice2, Pageable pageable);
    Page<Book> findBooksByTagsTextContainingIgnoreCaseAndPriceBetweenAndIsRequestIsFalse(String searchTerm, double minPrice, double maxPrice, Pageable pageable);
    Page<Book> findBooksByAuthorContainingIgnoreCaseAndPriceBetweenAndIsRequestIsFalse(String searchTerm, double minPrice, double maxPrice, Pageable pageable);

    Page<Book> findBooksByNameContainingIgnoreCaseAndPriceBetweenAndIsRequestIsTrueOrDescriptionContainingIgnoreCaseAndPriceBetweenAndIsRequestIsTrue(String searchTerm, double minPrice, double maxPrice, String searchTermDescription, double minPrice2, double maxPrice2, Pageable pageable);
    Page<Book> findBooksByTagsTextContainingIgnoreCaseAndPriceBetweenAndIsRequestIsTrue(String searchTerm, double minPrice, double maxPrice, Pageable pageable);
    Page<Book> findBooksByAuthorContainingIgnoreCaseAndPriceBetweenAndIsRequestIsTrue(String searchTerm, double minPrice, double maxPrice, Pageable pageable);

    Page<Book> findBooksByNameContainingAndAuthorContainingAndLanguageContainingAndIsRequestIs(String name, String author, String language, boolean isRequest, Pageable pageable);
    Page<Book> findBooksByNameContainingAndAuthorContainingAndLanguageContainingAndIsNewIsAndIsRequestIs(String name, String author, String language, boolean isNew, boolean isRequest, Pageable pageable);
}
