package com.etkx.repository;

import com.etkx.domain.Baureihe;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BaureiheRepository extends JpaRepository<Baureihe, String> {

    @Query("select distinct b from Baureihe b where b.baureiheProduktart = :produktart")
    List<Baureihe> findDistinctBaureiheByProduktart(@Param("produktart") String produktart);
}
