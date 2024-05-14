package com.bryan.libarterbe.DTO;

import java.time.LocalDateTime;

public record MessageDTO(String body,
                         boolean you,
                         LocalDateTime time,
                         String username,
                         int id
                         ) {
}
