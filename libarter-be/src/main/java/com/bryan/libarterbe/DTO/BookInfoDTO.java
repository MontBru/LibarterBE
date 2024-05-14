package com.bryan.libarterbe.DTO;

import java.util.List;

public record BookInfoDTO(String name,
                          String author,
                          String description,
                          double price,
                          String publisher,
                          String language,
                          int yearPublished,
                          long isbn) {

}
