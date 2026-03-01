package pl.emdzej.etkx.dal.repository.reference;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.emdzej.etkx.dal.dto.reference.FillQuantityDto;

/**
 * Repository for fill quantity reference data.
 */
@Repository
@RequiredArgsConstructor
public class FillQuantitiesRepository {
    private static final String RETRIEVE_FUELLMENGEN = """
        select fuellmengen_typ Typ,
            fuellmengen_getriebeben Getriebe,
            fuellmengen_motor Motor,
            fuellmengen_fm_getriebe FMGetriebe,
            fuellmengen_fm_motor FMMotor,
            fuellmengen_fm_ha FMHinterachse,
            fuellmengen_fm_km_mitac FMKuehlmittelMitAC,
            fuellmengen_fm_km_ohneac FMKuehlmittelOhneAC,
            fuellmengen_fm_bremse FMBremse,
            fuellmengen_hw___LANG__ Hinweis
        from w_fuellmengen
        where fuellmengen_typ in (:types)
          and cast(substr(CAST(fuellmengen_ab AS TEXT), 1, 6) as integer) <= :productionDate
          and nvl(cast(substr(CAST(fuellmengen_bis AS TEXT), 1, 6) as integer), 999999) >= :productionDate
        order by Typ, Getriebe
        """;

    private static final RowMapper<FillQuantityDto> FILL_QUANTITY_MAPPER = (rs, rowNum) ->
        FillQuantityDto.builder()
            .typ(rs.getString("Typ"))
            .getriebe(rs.getString("Getriebe"))
            .motor(rs.getString("Motor"))
            .fmGetriebe(rs.getString("FMGetriebe"))
            .fmMotor(rs.getString("FMMotor"))
            .fmHinterachse(rs.getString("FMHinterachse"))
            .fmKuehlmittelMitAc(rs.getString("FMKuehlmittelMitAC"))
            .fmKuehlmittelOhneAc(rs.getString("FMKuehlmittelOhneAC"))
            .fmBremse(rs.getString("FMBremse"))
            .hinweis(rs.getString("Hinweis"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Loads fill quantities for given types and production date.
     *
     * @param types list of fill quantity types
     * @param language language suffix used by the hint column
     * @param productionDate production date formatted as YYYYMM
     * @return list of fill quantity entries
     */
    public List<FillQuantityDto> loadFillQuantities(List<String> types, String language, Integer productionDate) {
        String sql = RETRIEVE_FUELLMENGEN.replace("__LANG__", language);
        return jdbc.query(sql, Map.of("types", types, "productionDate", productionDate), FILL_QUANTITY_MAPPER);
    }
}
