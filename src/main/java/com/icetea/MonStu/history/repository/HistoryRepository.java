package com.icetea.MonStu.history.repository;

import com.icetea.MonStu.history.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HistoryRepository extends JpaRepository<History,Long>, HistoryRepositoryCustom {


}
