package com.th.mymap.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"uid", "email"}
        )
})
public class User extends BaseEntity{
    @Id
    @Column(columnDefinition = "BIGINT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iuser;

    @NotNull
    @Column(length = 100)
    private String uid;

    @NotNull
    @Column(length = 2100)
    private String upw;

    @NotNull
    @Column(length = 20)
    private String nm;

    @NotNull
    @Column(length = 50)
    private String email;

    @Column
    private Double lat;

    @Column
    private Double lng;
}
