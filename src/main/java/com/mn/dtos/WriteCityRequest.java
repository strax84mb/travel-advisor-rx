package com.mn.dtos;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class WriteCityRequest {

    private String name;
    private String country;
}
