package com.etkx.repository;

import com.etkx.domain.Graphic;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GraphicRepository extends JpaRepository<Graphic, Integer> {

    @Query("select g from Graphic g where g.graphicId = :grafikid")
    Optional<Graphic> findByGrafikGrafikid(@Param("grafikid") Integer grafikid);
}
