package com.etkx.repository;

import com.etkx.domain.BtZeilen;
import com.etkx.domain.BtZeilenId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BtZeilenRepository extends JpaRepository<BtZeilen, BtZeilenId> {

    List<BtZeilen> findByBtzeilenBtnrOrderByBtzeilenPos(String btnr);
}
