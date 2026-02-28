package pl.emdzej.etkx.dal.repository.parts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pl.emdzej.etkx.dal.dto.parts.SetIndividualPartDto;
import pl.emdzej.etkx.dal.dto.parts.SetMainGroupDto;
import pl.emdzej.etkx.dal.dto.parts.SetPartDto;

/**
 * Repository for set/kit individual parts (SatzEinzelteile).
 */
@Repository
@RequiredArgsConstructor
public class SetIndividualPartsRepository {
    private static final String LOAD_HG = """
        select distinct hgfg_hg HG, ben_text Benennung
        from w_kompl_satz, w_ben_gk, w_hgfg
        where ks_marke_tps = :marke
          and ks_produktart IN ('B', :produktart)
          and hgfg_produktart = :produktart
          and ks_hg = hgfg_hg
          and hgfg_fg = '00'
          and hgfg_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by HG
        """;

    private static final String LOAD_SATZ = """
        select distinct teil_hauptgr HG,
            teil_untergrup UG,
            teil_sachnr Sachnummer,
            ben_text Benennung,
            teil_benennzus Zusatz,
            teil_vorhanden_si vorhandenSI,
            teil_textcode_kom BenKommentarId,
            teil_ist_reach Reach,
            teil_ist_aspg Aspg,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant,
            decode(teil_sachnr, tcp_sachnr, 'C', NULL) TC
        from w_kompl_satz
        inner join w_teil on (ks_sachnr_satz = teil_sachnr)
        inner join w_ben_gk on (
            teil_textcode = ben_textcode
            and ben_iso = :iso
            and ben_regiso = :regiso
        )
        left join w_tc_performance_allg on (
            teil_sachnr = tcp_sachnr
            and tcp_marke_tps = :marke
            and tcp_produktart = :produktart
            and tcp_vbereich in (:katalogumfaenge)
            __TC_CHECK__
            and tcp_datum_von <= :datum
            and (tcp_datum_bis is null or tcp_datum_bis <= :datum)
        )
        where ks_marke_tps = :marke
          and ks_produktart IN ('B', :produktart)
          and ks_hg = :hg
        order by HG, UG, Sachnummer
        """;

    private static final String COUNT_EINZELTEILE = """
        select count(ke_sachnr_einzelteil) Anzahl
        from w_kompl_einzelteil
        where ke_sachnr_satz = :sachnummer
        """;

    private static final String LOAD_EINZELTEIL = """
        select distinct teil_hauptgr HG,
            teil_untergrup UG,
            teil_sachnr Sachnummer,
            ben_text Benennung,
            teil_benennzus Zusatz,
            ke_menge Menge,
            ke_beziehbar istBeziehbar,
            teil_vorhanden_si vorhandenSI,
            teil_textcode_kom BenKommentarId,
            teil_ist_reach Reach,
            teil_ist_aspg Aspg,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant,
            decode(teil_sachnr, tcp_sachnr, 'C', NULL) TC,
            teil_ist_eba istEBA,
            ke_pos Pos
        from w_kompl_einzelteil
        inner join w_teil on (ke_sachnr_einzelteil = teil_sachnr)
        inner join w_ben_gk on (
            teil_textcode = ben_textcode
            and ben_iso = :iso
            and ben_regiso = :regiso
        )
        left join w_tc_performance_allg on (
            teil_sachnr = tcp_sachnr
            and tcp_marke_tps = :marke
            and tcp_produktart = :produktart
            and tcp_vbereich in (:katalogumfaenge)
            __TC_CHECK__
            and tcp_datum_von <= :datum
            and (tcp_datum_bis is null or tcp_datum_bis <= :datum)
        )
        where ke_sachnr_satz = :sachnummer
        order by Pos
        """;

    private static final RowMapper<SetMainGroupDto> MAIN_GROUP_MAPPER = (rs, rowNum) ->
        SetMainGroupDto.builder()
            .hg(rs.getString("HG"))
            .benennung(rs.getString("Benennung"))
            .build();

    private static final RowMapper<SetPartDto> SET_PART_MAPPER = (rs, rowNum) ->
        SetPartDto.builder()
            .hg(rs.getString("HG"))
            .ug(rs.getString("UG"))
            .sachnummer(rs.getString("Sachnummer"))
            .benennung(rs.getString("Benennung"))
            .zusatz(rs.getString("Zusatz"))
            .vorhandenSi(rs.getString("vorhandenSI"))
            .benKommentarId(rs.getString("BenKommentarId"))
            .reach(rs.getString("Reach"))
            .aspg(rs.getString("Aspg"))
            .teilDiebstahlrelevant(rs.getString("Teil_Diebstahlrelevant"))
            .tc(rs.getString("TC"))
            .build();

    private static final RowMapper<SetIndividualPartDto> INDIVIDUAL_PART_MAPPER = (rs, rowNum) ->
        SetIndividualPartDto.builder()
            .hg(rs.getString("HG"))
            .ug(rs.getString("UG"))
            .sachnummer(rs.getString("Sachnummer"))
            .benennung(rs.getString("Benennung"))
            .zusatz(rs.getString("Zusatz"))
            .menge(rs.getString("Menge"))
            .istBeziehbar(rs.getString("istBeziehbar"))
            .vorhandenSi(rs.getString("vorhandenSI"))
            .benKommentarId(rs.getString("BenKommentarId"))
            .reach(rs.getString("Reach"))
            .aspg(rs.getString("Aspg"))
            .teilDiebstahlrelevant(rs.getString("Teil_Diebstahlrelevant"))
            .tc(rs.getString("TC"))
            .istEba(rs.getString("istEBA"))
            .pos(getInteger(rs, "Pos"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Loads available main groups for sets.
     */
    public List<SetMainGroupDto> loadMainGroups(String marke, String produktart, String iso, String regiso) {
        return jdbc.query(LOAD_HG, Map.of("marke", marke, "produktart", produktart, "iso", iso, "regiso", regiso),
            MAIN_GROUP_MAPPER);
    }

    /**
     * Loads set parts for a main group.
     */
    public List<SetPartDto> loadSets(
        String marke,
        String produktart,
        String iso,
        String regiso,
        String hg,
        List<String> katalogumfaenge,
        String datum,
        String tcCheckClause,
        Map<String, Object> extraParams
    ) {
        String sql = applyClause(LOAD_SATZ, "__TC_CHECK__", tcCheckClause);
        Map<String, Object> params = new HashMap<>(Map.of(
            "marke", marke,
            "produktart", produktart,
            "iso", iso,
            "regiso", regiso,
            "hg", hg,
            "katalogumfaenge", katalogumfaenge,
            "datum", datum
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, SET_PART_MAPPER);
    }

    /**
     * Counts individual parts within a set.
     */
    public long countIndividualParts(String sachnummer) {
        return jdbc.queryForObject(COUNT_EINZELTEILE, Map.of("sachnummer", sachnummer), Long.class);
    }

    /**
     * Loads individual parts for a set.
     */
    public List<SetIndividualPartDto> loadIndividualParts(
        String sachnummer,
        String marke,
        String produktart,
        String iso,
        String regiso,
        List<String> katalogumfaenge,
        String datum,
        String tcCheckClause,
        Map<String, Object> extraParams
    ) {
        String sql = applyClause(LOAD_EINZELTEIL, "__TC_CHECK__", tcCheckClause);
        Map<String, Object> params = new HashMap<>(Map.of(
            "sachnummer", sachnummer,
            "marke", marke,
            "produktart", produktart,
            "iso", iso,
            "regiso", regiso,
            "katalogumfaenge", katalogumfaenge,
            "datum", datum
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, INDIVIDUAL_PART_MAPPER);
    }

    private static Integer getInteger(java.sql.ResultSet rs, String column) {
        try {
            int value = rs.getInt(column);
            return rs.wasNull() ? null : value;
        } catch (java.sql.SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private static String applyClause(String sql, String token, String clause) {
        if (!StringUtils.hasText(clause)) {
            return sql.replace(token, " ");
        }
        return sql.replace(token, " " + clause + " ");
    }
}
