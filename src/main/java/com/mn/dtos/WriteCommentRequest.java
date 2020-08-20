package com.mn.dtos;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class WriteCommentRequest {

    private Long cityId;
    private String message;
}
