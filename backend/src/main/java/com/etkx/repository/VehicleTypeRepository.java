package com.etkx.repository;

import com.etkx.domain.VehicleType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VehicleTypeRepository extends JpaRepository<VehicleType, Integer> {

    @Query("select v from VehicleType v where v.fztypBaureihe = :baureihe")
    List<VehicleType> findByFztypBaureihe(@Param("baureihe") String baureihe);

    @Query("select v from VehicleType v where v.fztypBaureihe = :baureihe and v.fztypKarosserie = :karosserie")
    List<VehicleType> findByFztypBaureiheAndFztypKarosserie(
            @Param("baureihe") String baureihe,
            @Param("karosserie") String karosserie
    );

    @Query("select v from VehicleType v where v.fztypMospid = :mospid")
    Optional<VehicleType> findByFztypMospid(@Param("mospid") Integer mospid);
}
