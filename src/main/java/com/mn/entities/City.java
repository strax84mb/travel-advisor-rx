package com.mn.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "city")
@Table(name = "city", indexes = {
        @Index(name = "city_name_country_index", columnList = "name,country", unique = true)
})
public class City {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @OneToMany(/*cascade = CascadeType., orphanRemoval = true, */mappedBy = "city")
    //@OrderBy("created DESC")
    private List<Comment> comments;
}
