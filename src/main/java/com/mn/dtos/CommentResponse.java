package com.mn.dtos;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentResponse {

    private Long id;
    private String poster;
    private String text;
    private LocalDateTime created;
    private LocalDateTime modified;
}
