package com.bryan.libarterbe.DTO;

public record SearchBooksDTO(boolean isRequest,
                             String searchTerm,
                             int pageNum,
                             double minPrice,
                             double maxPrice) {
}
