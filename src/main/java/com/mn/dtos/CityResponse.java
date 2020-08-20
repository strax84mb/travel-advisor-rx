package com.mn.dtos;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CityResponse {

    private Long id;
    private String name;
    private String country;
    private List<CommentResponse> comments;
}
