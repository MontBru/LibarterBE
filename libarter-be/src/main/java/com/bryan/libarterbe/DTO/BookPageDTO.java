package com.bryan.libarterbe.DTO;

import java.util.List;

public record BookPageDTO(List<BookDTO> books,
                          int totalPageCount) {
}
