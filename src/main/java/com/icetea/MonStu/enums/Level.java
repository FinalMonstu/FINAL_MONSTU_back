package com.icetea.MonStu.enums;

public enum Level {
    TRACE(10),   // 개발 중 가장 세세한 단계 (매우 상세한 디버깅 정보)
    DEBUG(20),   // 디버깅에 필요한 정보 (성능 이슈, 내부 흐름 점검용)
    INFO(30),    // 일반 운영 정보 (애플리케이션 상태, 주요 이벤트)
    WARN(40),    // 잠재적 문제 경고 (주의가 필요한 상황)
    ERROR(50),   // 예외 발생 등 오류 상황
    FATAL(60);    // 시스템 치명적 장애 (즉시 대응/복구가 필요한 상황)

    private final int priority;

    Level(int priority) { this.priority = priority; }

    public int getPriority() { return priority;}
}
