package com.icetea.MonStu.entity;

import com.icetea.MonStu.entity.link.PostTag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"postTags"})
@Entity
@Table(name="tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column @NotBlank
    private String value;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PostTag> postTags;
}
