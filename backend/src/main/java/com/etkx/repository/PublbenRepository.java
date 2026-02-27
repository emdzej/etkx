package com.etkx.repository;

import com.etkx.domain.Publben;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PublbenRepository extends JpaRepository<Publben, Integer> {

    @Query("select p from Publben p where p.publbenTextcode = :textcode")
    Optional<Publben> findByPublbenTextcodeAndPublbenSprache(
            @Param("textcode") Integer textcode,
            @Param("lang") String lang
    );
}
