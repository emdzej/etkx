package pl.emdzej.etkx.dal.repository.diagram;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pl.emdzej.etkx.dal.dto.diagram.SpringTableAspgKitDto;
import pl.emdzej.etkx.dal.dto.diagram.SpringTablePointsSalaDto;
import pl.emdzej.etkx.dal.dto.diagram.SpringTablePointsTypeDto;
import pl.emdzej.etkx.dal.dto.diagram.SpringTableSalaDto;
import pl.emdzej.etkx.dal.dto.diagram.SpringTableSpringDto;

/**
 * Repository for spring table data (Federtabelle).
 */
@Repository
@RequiredArgsConstructor
public class SpringTableRepository {
    private static final String LOAD_SALAS = """
        select distinct sftsala_salaid Id,
            bedsala_art || bedsala_pnr || bedsala_hz Code,
            ben_text Benennung
        from w_sfttyp, w_ben_gk, w_bed_sala, w_bed, w_sftsala, w_sft
        where sfttyp_typ in (:typen)
          and sfttyp_sftid = sft_sftid
          __PRODDATUM_STMT__
          and sft_sftid = sftsala_sftid
          and sftsala_salaid = bedsala_id
          and bedsala_id = bed_elemid
          and bed_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by Code, Benennung
        """;

    private static final String LOAD_PUNKTE_TYP = """
        select distinct sfttyp_sftid FTId,
            sfttyp_typ Typ,
            sfttyp_punkte_va_l GrundpunkteVA_links,
            sfttyp_punkte_va_r GrundpunkteVA_rechts,
            sfttyp_punkte_ha_l GrundpunkteHA_links,
            sfttyp_punkte_ha_r GrundpunkteHA_rechts,
            sftid_ist_aspg Sftid_Ist_Aspg
        from w_sfttyp, w_sft
        where sfttyp_typ in (:typen)
          and sfttyp_sftid = sft_sftid
          __PRODDATUM_STMT__
        order by FTId, Typ
        """;

    private static final String LOAD_PUNKTE_SALA = """
        select distinct sftsala_sftid FTId,
            sftsala_salaid SalaId,
            sftsala_va_punkte_l PunkteVA_links,
            sftsala_va_punkte_r PunkteVA_rechts,
            sftsala_ha_punkte_l PunkteHA_links,
            sftsala_ha_punkte_r PunkteHA_rechts
        from w_sfttyp, w_sftsala, w_sft
        where sfttyp_typ in (:typen)
          and sfttyp_sftid = sft_sftid
          __PRODDATUM_STMT__
          and sft_sftid = sftsala_sftid
          and sftsala_salaid in (:salaIds)
        order by FTId, SalaId
        """;

    private static final String LOAD_FEDERN_LINKS = """
        select distinct teil_hauptgr || teil_untergrup || sftfeder_sachnr_l Teilenummer,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_sftfeder, w_teil
        where sftfeder_sftid = :ftId
          and sftfeder_kz_vh = :art
          and sftfeder_punkte_von <= :punkte
          and sftfeder_punkte_bis >= :punkte
          and teil_sachnr = sftfeder_sachnr_l
        """;

    private static final String LOAD_FEDERN_RECHTS = """
        select distinct teil_hauptgr || teil_untergrup || sftfeder_sachnr_r Teilenummer,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_sftfeder, w_teil
        where sftfeder_sftid = :ftId
          and sftfeder_kz_vh = :art
          and sftfeder_punkte_von <= :punkte
          and sftfeder_punkte_bis >= :punkte
          and teil_sachnr = sftfeder_sachnr_r
        """;

    private static final String LOAD_ASPG_KIT = """
        select teil_hauptgr || teil_untergrup || sftaspg_sachnr_pg Teilenummer,
            sftaspg_btnr BTNr,
            sftaspg_mospid MospId,
            sftaspg_achse Achse,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_sft_aspg, w_teil
        where sftaspg_sftid = :ftId
          and teil_sachnr = sftaspg_sachnr_pg
        """;

    private static final RowMapper<SpringTableSalaDto> SALA_MAPPER = (rs, rowNum) ->
        SpringTableSalaDto.builder()
            .id(rs.getString("Id"))
            .code(rs.getString("Code"))
            .benennung(rs.getString("Benennung"))
            .build();

    private static final RowMapper<SpringTablePointsTypeDto> POINTS_TYPE_MAPPER = (rs, rowNum) ->
        SpringTablePointsTypeDto.builder()
            .ftId(rs.getString("FTId"))
            .typ(rs.getString("Typ"))
            .grundpunkteVaLinks(rs.getString("GrundpunkteVA_links"))
            .grundpunkteVaRechts(rs.getString("GrundpunkteVA_rechts"))
            .grundpunkteHaLinks(rs.getString("GrundpunkteHA_links"))
            .grundpunkteHaRechts(rs.getString("GrundpunkteHA_rechts"))
            .sftidIstAspg(rs.getString("Sftid_Ist_Aspg"))
            .build();

    private static final RowMapper<SpringTablePointsSalaDto> POINTS_SALA_MAPPER = (rs, rowNum) ->
        SpringTablePointsSalaDto.builder()
            .ftId(rs.getString("FTId"))
            .salaId(rs.getString("SalaId"))
            .punkteVaLinks(rs.getString("PunkteVA_links"))
            .punkteVaRechts(rs.getString("PunkteVA_rechts"))
            .punkteHaLinks(rs.getString("PunkteHA_links"))
            .punkteHaRechts(rs.getString("PunkteHA_rechts"))
            .build();

    private static final RowMapper<SpringTableSpringDto> SPRING_MAPPER = (rs, rowNum) ->
        SpringTableSpringDto.builder()
            .teilenummer(rs.getString("Teilenummer"))
            .teilDiebstahlrelevant(rs.getString("Teil_Diebstahlrelevant"))
            .build();

    private static final RowMapper<SpringTableAspgKitDto> ASPG_KIT_MAPPER = (rs, rowNum) ->
        SpringTableAspgKitDto.builder()
            .teilenummer(rs.getString("Teilenummer"))
            .btnr(rs.getString("BTNr"))
            .mospId(rs.getString("MospId"))
            .achse(rs.getString("Achse"))
            .teilDiebstahlrelevant(rs.getString("Teil_Diebstahlrelevant"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Retrieves available SALA items for spring tables.
     */
    public List<SpringTableSalaDto> findSalas(List<String> typen, String iso, String regiso,
                                              String productionDateClause, Map<String, Object> extraParams) {
        String sql = applyClauses(LOAD_SALAS, Map.of("__PRODDATUM_STMT__", productionDateClause));
        Map<String, Object> params = new HashMap<>(Map.of(
            "typen", typen,
            "iso", iso,
            "regiso", regiso
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, SALA_MAPPER);
    }

    /**
     * Retrieves base points per type for spring tables.
     */
    public List<SpringTablePointsTypeDto> findPointsByType(List<String> typen,
                                                           String productionDateClause,
                                                           Map<String, Object> extraParams) {
        String sql = applyClauses(LOAD_PUNKTE_TYP, Map.of("__PRODDATUM_STMT__", productionDateClause));
        Map<String, Object> params = new HashMap<>(Map.of("typen", typen));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, POINTS_TYPE_MAPPER);
    }

    /**
     * Retrieves SALA point adjustments for spring tables.
     */
    public List<SpringTablePointsSalaDto> findPointsBySala(List<String> typen, List<String> salaIds,
                                                           String productionDateClause,
                                                           Map<String, Object> extraParams) {
        String sql = applyClauses(LOAD_PUNKTE_SALA, Map.of("__PRODDATUM_STMT__", productionDateClause));
        Map<String, Object> params = new HashMap<>(Map.of(
            "typen", typen,
            "salaIds", salaIds
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, POINTS_SALA_MAPPER);
    }

    /**
     * Retrieves left-side springs for a points configuration.
     */
    public List<SpringTableSpringDto> findLeftSprings(long ftId, String art, long punkte) {
        return jdbc.query(LOAD_FEDERN_LINKS, Map.of(
            "ftId", ftId,
            "art", art,
            "punkte", punkte
        ), SPRING_MAPPER);
    }

    /**
     * Retrieves right-side springs for a points configuration.
     */
    public List<SpringTableSpringDto> findRightSprings(long ftId, String art, long punkte) {
        return jdbc.query(LOAD_FEDERN_RECHTS, Map.of(
            "ftId", ftId,
            "art", art,
            "punkte", punkte
        ), SPRING_MAPPER);
    }

    /**
     * Retrieves ASPG kits for a spring table entry.
     */
    public List<SpringTableAspgKitDto> findAspgKits(long ftId) {
        return jdbc.query(LOAD_ASPG_KIT, Map.of("ftId", ftId), ASPG_KIT_MAPPER);
    }

    private static String applyClauses(String sql, Map<String, String> replacements) {
        String result = sql;
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            String clause = entry.getValue();
            result = result.replace(entry.getKey(), StringUtils.hasText(clause) ? " " + clause + " " : " ");
        }
        return result;
    }
}
