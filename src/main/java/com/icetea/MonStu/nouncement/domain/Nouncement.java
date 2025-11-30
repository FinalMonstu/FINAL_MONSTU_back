package com.icetea.MonStu.nouncement.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"nouncementImages"})
@Entity
@DynamicUpdate
@Table(name="nouncement")
@EntityListeners(AuditingEntityListener.class)
public class Nouncement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column @NotBlank
    private String title;

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDate modifiedAt;

    @Builder.Default
    @Column(nullable = false)
    private boolean isPublic = true;   // 공개 여부

    @Builder.Default
    @Column(nullable = false)
    private boolean isImportant = false;;   // 중요 여부

    @Column
    private int viewCount;


    /*---------------------------------------연관관계-------------------------------------------*/
    @Builder.Default
    @OneToMany(mappedBy = "nouncement", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<NouncementImage> nouncementImages = new ArrayList<>();


    /*---------------------------------------편의 메서드-------------------------------------------*/
    public void addNouncementImage(NouncementImage nouncementImage) {
        this.nouncementImages.add(nouncementImage);
        if (nouncementImage != null && nouncementImage.getNouncement() != this) nouncementImage.setNouncement(this);
    }

    public void removeNouncementImage(NouncementImage nouncementImage){
        nouncementImages.remove(nouncementImage);
        if(nouncementImage != null && nouncementImage.getNouncement()==this) nouncementImage.setNouncement(null);
    }

}
