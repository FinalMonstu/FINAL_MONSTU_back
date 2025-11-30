package com.icetea.MonStu.nouncement.domain;

import com.icetea.MonStu.image.domain.Image;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"nouncement","image"})
@Entity
@Table(name="nouncement_image")
public class NouncementImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "nouncement_id")
    private Nouncement nouncement;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "image_id")
    private Image image;


    /*---------------------------------------편의 메서드-------------------------------------------*/
    public void setNouncement(Nouncement nouncement) {
        if (this.nouncement != null) {
            this.nouncement.getNouncementImages().remove(this);
        }
        this.nouncement = nouncement;

        if (nouncement != null && !nouncement.getNouncementImages().contains(this)) {
            nouncement.getNouncementImages().add(this);
        }
    }
}
