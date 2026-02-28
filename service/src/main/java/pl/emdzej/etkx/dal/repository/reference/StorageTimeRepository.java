package pl.emdzej.etkx.dal.repository.reference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pl.emdzej.etkx.dal.dto.reference.StorageTimeMainGroupDto;
import pl.emdzej.etkx.dal.dto.reference.StorageTimePartDto;

/**
 * Repository for storage time reference data.
 */
@Repository
@RequiredArgsConstructor
public class StorageTimeRepository {
    private static final String RETRIEVE_HGS = """
        select distinct teil_hauptgr HG
        from w_teil, w_teil_marken
        where teil_lzb = 'J'
          and teil_hauptgr >= :hgFrom
          __HGBIS__
          and teil_produktart IN (:productTypes)
          and teil_sachnr = teilm_sachnr
          and teilm_marke_tps = :marke
        order by HG
        """;

    private static final String RETRIEVE_TEILE = """
        select distinct teil_hauptgr HG,
            teil_untergrup UG,
            teil_sachnr SACHNR,
            ben_text BENENNUNG,
            teil_benennzus ZUSATZ,
            teil_vorhanden_si_ohne_lzb SI,
            teil_kom_pi PI,
            teil_textcode_kom BENKOMMENTARID,
            teil_ist_reach REACH,
            teil_ist_aspg ASPG,
            decode(teil_sachnr, tcp_sachnr, 'C', NULL) TC,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_teil
        inner join w_teil_marken on (teil_sachnr = teilm_sachnr and teilm_marke_tps = :marke)
        inner join w_ben_gk on (teil_textcode = ben_textcode and ben_iso = :iso and ben_regiso = :regiso)
        left join w_tc_performance_allg on (
            teil_sachnr = tcp_sachnr
            and tcp_marke_tps = :marke
            and tcp_produktart = :produktart
            and tcp_vbereich in (:catalogScopes)
            __TC_CHECK__
            and tcp_datum_von <= :date
            and (tcp_datum_bis is null or tcp_datum_bis <= :date)
        )
        where teil_lzb = 'J'
          and teil_hauptgr = :hg
          and teil_produktart IN (:productTypes)
        order by HG, UG, SACHNR
        """;

    private static final RowMapper<StorageTimeMainGroupDto> MAIN_GROUP_MAPPER = (rs, rowNum) ->
        StorageTimeMainGroupDto.builder()
            .hg(rs.getString("HG"))
            .build();

    private static final RowMapper<StorageTimePartDto> PART_MAPPER = (rs, rowNum) ->
        StorageTimePartDto.builder()
            .hg(rs.getString("HG"))
            .ug(rs.getString("UG"))
            .sachnr(rs.getString("SACHNR"))
            .benennung(rs.getString("BENENNUNG"))
            .zusatz(rs.getString("ZUSATZ"))
            .si(rs.getString("SI"))
            .pi(rs.getString("PI"))
            .benKommentarId(rs.getString("BENKOMMENTARID"))
            .reach(rs.getString("REACH"))
            .aspg(rs.getString("ASPG"))
            .tc(rs.getString("TC"))
            .teilDiebstahlrelevant(rs.getString("Teil_Diebstahlrelevant"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Loads main groups for storage time reference data.
     *
     * @param hgFrom minimum main group value
     * @param hgToClause optional SQL clause for upper bound
     * @param marke brand code
     * @param produktart product type
     * @param extraParams additional parameters used by optional clauses
     * @return list of main group entries
     */
    public List<StorageTimeMainGroupDto> loadMainGroups(
        String hgFrom,
        String hgToClause,
        String marke,
        String produktart,
        Map<String, Object> extraParams
    ) {
        String sql = applyClauses(RETRIEVE_HGS, Map.of("__HGBIS__", hgToClause));
        Map<String, Object> params = new HashMap<>(Map.of(
            "hgFrom", hgFrom,
            "marke", marke,
            "productTypes", List.of(produktart, "B")
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, MAIN_GROUP_MAPPER);
    }

    /**
     * Loads storage time parts for a given main group.
     *
     * @param hg main group
     * @param marke brand code
     * @param produktart product type
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @param catalogScopes catalog scope identifiers
     * @param date date value used for filtering
     * @param tcCheckClause optional SQL clause for TC filtering
     * @param extraParams additional parameters used by optional clauses
     * @return list of storage time part entries
     */
    public List<StorageTimePartDto> loadParts(
        String hg,
        String marke,
        String produktart,
        String iso,
        String regiso,
        List<String> catalogScopes,
        String date,
        String tcCheckClause,
        Map<String, Object> extraParams
    ) {
        String sql = applyClauses(RETRIEVE_TEILE, Map.of("__TC_CHECK__", tcCheckClause));
        Map<String, Object> params = new HashMap<>(Map.of(
            "hg", hg,
            "marke", marke,
            "produktart", produktart,
            "iso", iso,
            "regiso", regiso,
            "catalogScopes", catalogScopes,
            "date", date,
            "productTypes", List.of(produktart, "B")
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, PART_MAPPER);
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
