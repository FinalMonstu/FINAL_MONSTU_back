package com.icetea.MonStu.entity.log;

import com.icetea.MonStu.entity.Image;
import com.icetea.MonStu.entity.Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"post"})
@Table(name="post_log")
public class PostLog {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private Long viewCount;

    @Column
    private LocalDateTime lastViewedAt;

}
