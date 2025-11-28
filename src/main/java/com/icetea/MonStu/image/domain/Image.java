package com.icetea.MonStu.image.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column @NotBlank
    private String name;

    @Column @NotBlank
    private String url;

    @Column @NotBlank
    private String type;

    @Column
    private Long byteSize;

    @Column
    private Integer height;

    @Column
    private Integer width;

}
