package com.bryan.libarterbe.DTO;

import com.bryan.libarterbe.model.ApplicationUser;
import com.bryan.libarterbe.model.Book;
import com.bryan.libarterbe.model.Tag;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public record BookDTO(int id,
                      boolean isRequest,
                      String name,
                      String author,
                      String description,
                      List<String> photos,
                      boolean acceptsTrade,
                      boolean isNew,
                      double price,
                      int userId,
                      long isbn,
                      List<String> tags,
                      String publisher,
                      String language,
                      int yearPublished) {

}
