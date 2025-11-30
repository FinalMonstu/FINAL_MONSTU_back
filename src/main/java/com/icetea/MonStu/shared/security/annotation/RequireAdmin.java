package com.icetea.MonStu.shared.security.annotation;

import org.springframework.security.access.prepost.PreAuthorize;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE}) // 메서드와 클래스 둘 다 붙일 수 있게
@Retention(RetentionPolicy.RUNTIME)             // 런타임까지 유지되도록
@Inherited                                      // 상속받은 클래스에도 적용되도록
@PreAuthorize("hasRole(T(com.icetea.MonStu.member.enums.MemberRole).ADMIN.name())")
public @interface RequireAdmin {
}
