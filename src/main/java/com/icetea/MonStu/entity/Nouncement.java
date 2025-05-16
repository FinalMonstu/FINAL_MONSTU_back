package com.icetea.MonStu.entity;

import com.icetea.MonStu.entity.link.NouncementImage;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
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

    @Column @NotBlank
    private String title;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private Date createdAt;

    @Column
    private Date modifiedAt;

    @Column(nullable = false)
    private Boolean isPublic;   // 공개 여부

    @Column(nullable = false)
    private Boolean isImportant;   // 중요 여부

    @Column
    private int viewCount;


    // 다대다 연관관계
    @Builder.Default
    @OneToMany(mappedBy = "nouncement", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
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
