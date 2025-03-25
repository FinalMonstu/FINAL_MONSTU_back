package com.icetea.MonStu.entity.link;

import com.icetea.MonStu.entity.Image;
import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.entity.Nouncement;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"nouncement","image"})
@Table(name="nouncement_image")
public class NouncementImage {

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "nouncement_id")
    private Nouncement nouncement;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "image_id")
    private Image image;


    public void setNouncement(Nouncement nouncement) {
        this.nouncement = nouncement;
        if(!nouncement.getNouncementImages().contains(this)) nouncement.getNouncementImages().add(this);
    }

}
