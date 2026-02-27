package com.etkx.repository;

import com.etkx.domain.Part;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, String> {

    List<Part> findByTeilSachnrContaining(String sachnr);

    List<Part> findByTeilHauptgr(String hg);

    List<Part> findByTeilHauptgrAndTeilUntergrup(String hg, String fg);

    List<Part> findByTeilAlt(String sachnr);
}
