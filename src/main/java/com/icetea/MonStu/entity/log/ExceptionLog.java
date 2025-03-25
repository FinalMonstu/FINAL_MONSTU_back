package com.icetea.MonStu.entity.log;

import com.icetea.MonStu.enums.Level;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "exception_log")
public class ExceptionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private Level level;

    @Column(nullable = false)
    private String service;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private String methodName;

    @Column(nullable = false)
    private int lineNum;

    @Column(nullable = false)
    private String exceptionType;

    @Lob
    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String requestUrl;

    @Column(nullable = false)
    private String httpStatus;






}
