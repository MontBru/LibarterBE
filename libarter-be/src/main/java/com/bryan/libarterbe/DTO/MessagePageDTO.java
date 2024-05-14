package com.bryan.libarterbe.DTO;

import java.util.List;

public record MessagePageDTO(int totalPageCount,
                             List<MessageDTO> messages) {
}
