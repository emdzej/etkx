package pl.etkx.dal.repository.reference;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.etkx.dal.dto.reference.AspgConnectorDto;
import pl.etkx.dal.dto.reference.AspgPartDto;

/**
 * Repository for ASPG reference data.
 */
@Repository
@RequiredArgsConstructor
public class AspgRepository {
    private static final String LOAD_ASPG_TEILE = """
        select teilaspg_sachnr_pg SachnrPg,
            teilaspg_vmenge VMenge
        from w_teil_aspg
        where teilaspg_sachnr = :sachnr
          and teilaspg_kz_gruppe = :gruppe
        """;

    private static final String LOAD_STECKER = """
        select teil_hauptgr Hauptgr,
            teil_untergrup Untergrup,
            teilaspg_sachnr_pg SachnrPg,
            ben_text BenText,
            teilaspg_vmenge VMenge,
            teil_benennzus BenennZus,
            teil_technik Technik,
            teil_entfall_kez EntfallKez,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_teil_aspg, w_teil, w_ben_gk
        where teilaspg_sachnr = :sachnr
          and teilaspg_kz_gruppe = :gruppe
          and teilaspg_sachnr_pg = teil_sachnr
          and teil_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        """;

    private static final RowMapper<AspgPartDto> ASPG_PART_MAPPER = (rs, rowNum) ->
        AspgPartDto.builder()
            .sachnrPg(rs.getString("SachnrPg"))
            .vmenge(rs.getString("VMenge"))
            .build();

    private static final RowMapper<AspgConnectorDto> ASPG_CONNECTOR_MAPPER = (rs, rowNum) ->
        AspgConnectorDto.builder()
            .hauptgr(rs.getString("Hauptgr"))
            .untergrup(rs.getString("Untergrup"))
            .sachnrPg(rs.getString("SachnrPg"))
            .benText(rs.getString("BenText"))
            .vmenge(rs.getString("VMenge"))
            .benennZus(rs.getString("BenennZus"))
            .technik(rs.getString("Technik"))
            .entfallKez(rs.getString("EntfallKez"))
            .teilDiebstahlrelevant(rs.getString("Teil_Diebstahlrelevant"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Loads ASPG parts for a given part number and group.
     *
     * @param sachnr part number
     * @param gruppe group code
     * @return list of ASPG parts
     */
    public List<AspgPartDto> loadAspgParts(String sachnr, String gruppe) {
        return jdbc.query(LOAD_ASPG_TEILE, Map.of("sachnr", sachnr, "gruppe", gruppe), ASPG_PART_MAPPER);
    }

    /**
     * Loads connector parts (Stecker) for a given ASPG part.
     *
     * @param sachnr part number
     * @param gruppe group code
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return list of connector parts
     */
    public List<AspgConnectorDto> loadConnectors(String sachnr, String gruppe, String iso, String regiso) {
        return jdbc.query(LOAD_STECKER, Map.of(
            "sachnr", sachnr,
            "gruppe", gruppe,
            "iso", iso,
            "regiso", regiso
        ), ASPG_CONNECTOR_MAPPER);
    }
}
