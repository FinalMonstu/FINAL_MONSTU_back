package com.icetea.MonStu.entity.link;

import com.icetea.MonStu.entity.Image;
import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.entity.Nouncement;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"nouncement","image"})
@Entity
@Table(name="nouncement_image")
public class NouncementImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "nouncement_id")
    private Nouncement nouncement;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "image_id")
    private Image image;

}
