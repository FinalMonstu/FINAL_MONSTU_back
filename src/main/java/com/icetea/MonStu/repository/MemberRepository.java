package com.icetea.MonStu.repository;

import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.enums.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long>, QuerydslPredicateExecutor<Member>{

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByPhoneNumberAndNickName(String phoneNumber, String nickName);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Member m SET m.status = :status WHERE m.email = :email")
    int updateStatusByEmail(@Param("email") String email,
                            @Param("status") MemberStatus status);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Member m SET m.password = :password WHERE m.email = :email")
    int updatePasswordByEmail(@Param("email") String email,
                              @Param("password") String password);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE Member m WHERE m.status = 'DELETED'")
    void deleteAllByStatus();


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Member m SET m.status = :status WHERE m.id IN :ids")
    void updateStatusById( @Param("ids") List<Long> ids,
                           @Param("status") MemberStatus status);

}
