package com.etkx.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Repository
@RequiredArgsConstructor
public class TeileersetzungRepository {

    private final EntityManager entityManager;

    public List<String> findReplacementSachnrs(String sachnr) {
        Query query = entityManager.createNativeQuery("""
                select distinct neu.teil_sachnr
                  from w_teileersetzung t
                  join w_teil neu on t.ts_sachnr = neu.teil_sachnr
                 where neu.teil_alt = :sachnr
                 order by neu.teil_sachnr
                """);
        query.setParameter("sachnr", sachnr);
        List<?> rows = query.getResultList();
        return rows.stream().map(Object::toString).toList();
    }
}
