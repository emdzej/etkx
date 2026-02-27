package com.etkx.repository;

import com.etkx.domain.HgFg;
import com.etkx.domain.HgFgId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HgFgRepository extends JpaRepository<HgFg, HgFgId> {

    @Query("select h from HgFg h where h.hgfgProduktart = :produktart and h.hgfgFg = '00'")
    List<HgFg> findDistinctHgByProduktart(@Param("produktart") String produktart);

    List<HgFg> findByHgfgHgAndHgfgProduktart(String hg, String produktart);
}
