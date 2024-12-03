package com.th.mymap.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Picture extends CreatedAtEntity{
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ipicture;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ilocation")
    private Location location;

    @Column
    private String picture;

    @Column
    private String thumbnail;
}
