package com.icetea.MonStu.entity.link;

import com.icetea.MonStu.entity.Post;
import com.icetea.MonStu.entity.Tag;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"post","tag"})
@Entity
@Table(name= "post_tag")
public class PostTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "tag_id")
    private Tag tag;

}
