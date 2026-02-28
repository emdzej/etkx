package pl.etkx.dal.repository.reference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pl.etkx.dal.dto.reference.InitialStockMainGroupDto;
import pl.etkx.dal.dto.reference.InitialStockPartDto;

/**
 * Repository for initial stock reference data.
 */
@Repository
@RequiredArgsConstructor
public class InitialStockRepository {
    private static final String RETRIEVE_HGS = """
        select distinct ebs_hg HG
        from w_erstbevorratung_suche
        where ebs_mospid IN (:mospids)
          and ebs_hg >= :hgFrom
          __HGBIS__
          __LENKUNG__
        order by HG
        """;

    private static final String RETRIEVE_TEILE = """
        select distinct teil_hauptgr HG,
            teil_untergrup UG,
            teil_sachnr SACHNR,
            ben_text BENENNUNG,
            teil_benennzus ZUSATZ,
            teil_vorhanden_si SI,
            teil_lzb LZB,
            teil_kom_pi PI,
            teil_textcode_kom BENKOMMENTARID,
            teil_ist_reach REACH,
            teil_ist_aspg ASPG,
            decode(tcp_sachnr, tcp_sachnr, 'C', NULL) TC,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_erstbevorratung
        inner join w_teil on (eb_sachnr = teil_sachnr)
        inner join w_ben_gk on (teil_textcode = ben_textcode and ben_iso = :iso and ben_regiso = :regiso)
        left join w_tc_performance on (
            tcp_mospid in (:mospids)
            and tcp_sachnr = eb_sachnr
            __TC_CHECK__
            and tcp_datum_von <= :date
            and (tcp_datum_bis is null or tcp_datum_bis >= :date)
        )
        where eb_hg = :hg
          and eb_mospid IN (:mospids)
          __LENKUNG__
        order by HG, UG, SACHNR
        """;

    private static final RowMapper<InitialStockMainGroupDto> MAIN_GROUP_MAPPER = (rs, rowNum) ->
        InitialStockMainGroupDto.builder()
            .hg(rs.getString("HG"))
            .build();

    private static final RowMapper<InitialStockPartDto> PART_MAPPER = (rs, rowNum) ->
        InitialStockPartDto.builder()
            .hg(rs.getString("HG"))
            .ug(rs.getString("UG"))
            .sachnr(rs.getString("SACHNR"))
            .benennung(rs.getString("BENENNUNG"))
            .zusatz(rs.getString("ZUSATZ"))
            .si(rs.getString("SI"))
            .lzb(rs.getString("LZB"))
            .pi(rs.getString("PI"))
            .benKommentarId(rs.getString("BENKOMMENTARID"))
            .reach(rs.getString("REACH"))
            .aspg(rs.getString("ASPG"))
            .tc(rs.getString("TC"))
            .teilDiebstahlrelevant(rs.getString("Teil_Diebstahlrelevant"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Loads main groups for initial stock reference data.
     *
     * @param mospids list of MOSP identifiers
     * @param hgFrom minimum main group value
     * @param hgToClause optional SQL clause for upper bound
     * @param lenkungClause optional SQL clause for steering filters
     * @param extraParams additional parameters used by optional clauses
     * @return list of main group entries
     */
    public List<InitialStockMainGroupDto> loadMainGroups(
        List<String> mospids,
        String hgFrom,
        String hgToClause,
        String lenkungClause,
        Map<String, Object> extraParams
    ) {
        String sql = applyClauses(RETRIEVE_HGS, Map.of(
            "__HGBIS__", hgToClause,
            "__LENKUNG__", lenkungClause
        ));
        Map<String, Object> params = new HashMap<>(Map.of("mospids", mospids, "hgFrom", hgFrom));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, MAIN_GROUP_MAPPER);
    }

    /**
     * Loads initial stock parts for a given main group.
     *
     * @param hg main group
     * @param mospids list of MOSP identifiers
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @param date date value used for filtering
     * @param lenkungClause optional SQL clause for steering filters
     * @param tcCheckClause optional SQL clause for TC filtering
     * @param extraParams additional parameters used by optional clauses
     * @return list of initial stock part entries
     */
    public List<InitialStockPartDto> loadParts(
        String hg,
        List<String> mospids,
        String iso,
        String regiso,
        String date,
        String lenkungClause,
        String tcCheckClause,
        Map<String, Object> extraParams
    ) {
        String sql = applyClauses(RETRIEVE_TEILE, Map.of(
            "__LENKUNG__", lenkungClause,
            "__TC_CHECK__", tcCheckClause
        ));
        Map<String, Object> params = new HashMap<>(Map.of(
            "hg", hg,
            "mospids", mospids,
            "iso", iso,
            "regiso", regiso,
            "date", date
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
