package com.icetea.MonStu.entity;

import com.icetea.MonStu.entity.link.NouncementImage;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"nouncementImages"})
@Entity
@Table(name="nouncement")
public class Nouncement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime modifiedAt;

    @Column(nullable = false)
    private Boolean isPublic;   // 공개여부

    @Column(nullable = false)
    private Boolean isImportant;   // 중요여부

    @Column
    private int viewCount;   // 공개여부


    // 다대다 연관관계
    @OneToMany(mappedBy = "nouncement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NouncementImage> nouncementImages = new ArrayList<>();


    public void addNouncementImage(NouncementImage nouncementImage) {
        this.nouncementImages.add(nouncementImage);
        if (nouncementImage.getNouncement() != this) nouncementImage.setNouncement(this);
    }

    public void removeNouncementImage(NouncementImage nouncementImage){
        nouncementImages.remove(nouncementImage);
        if(nouncementImage.getNouncement()==this) nouncementImage.setNouncement(null);
    }

}
