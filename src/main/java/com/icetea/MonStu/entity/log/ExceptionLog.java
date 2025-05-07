package com.icetea.MonStu.entity.log;

import com.icetea.MonStu.enums.Level;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "exception_log")
public class ExceptionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String message;

    @NotBlank
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Level level;

    @Column
    private String service;

    @Column
    private String className;

    @Column
    private String methodName;

    @Column
    private Integer lineNum;

    @Column
    private String exceptionType;

    @Column
    private String requestUrl;

    @Column
    private String httpStatus;

}
