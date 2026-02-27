package com.etkx.repository;

import com.etkx.domain.Diagram;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DiagramRepository extends JpaRepository<Diagram, String> {

    @Query("select d from Diagram d where d.mainGroup = :hg and d.subGroup = :fg")
    List<Diagram> findByBildtafHgAndBildtafFg(@Param("hg") String hg, @Param("fg") String fg);

    @Query("select d from Diagram d where d.diagramNumber = :btnr")
    Optional<Diagram> findByBildtafBtnr(@Param("btnr") String btnr);
}
