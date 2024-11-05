package com.th.mymap.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Location extends BaseEntity{
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ilocation;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "iuser")
    private User user;

    @Column
    private Double lat;

    @Column
    private Double lng;

    @Column
    private String location;

    @Column
    private LocalDateTime date;
}
