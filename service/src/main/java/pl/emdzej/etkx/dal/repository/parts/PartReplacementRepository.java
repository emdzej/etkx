package pl.emdzej.etkx.dal.repository.parts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pl.emdzej.etkx.dal.dto.parts.MainGroupDto;
import pl.emdzej.etkx.dal.dto.parts.PartReplacementDto;
import pl.emdzej.etkx.dal.dto.parts.SimpleReplacementDto;

/**
 * Repository for part replacement (supersession) data.
 */
@Repository
@RequiredArgsConstructor
public class PartReplacementRepository {
    private static final String RETRIEVE_HGS = """
        select distinct tss_hg HG
        from w_teileersetzung_suche
        where tss_mospid IN (:mospids)
          and tss_hg >= :hgFrom
          __HGBIS__
          __LENKUNG__
          __DATSERIE__
        order by HG
        """;

    private static final String FIND_HG = """
        select distinct teil_hauptgr HG
        from w_teil
        where teil_hauptgr = :hg
        """;

    private static final String RETRIEVE_TEILE = """
        select distinct NEU.teil_hauptgr HG,
            NEU.teil_untergrup UG,
            ts_sachnr SACHNR,
            NEU.teil_alt SACHNRALT,
            ALT.teil_hauptgr HGALT,
            ALT.teil_untergrup UGALT,
            NEU.teil_austausch_alt AT,
            ben_text BENENNUNG,
            NEU.teil_benennzus ZUSATZ,
            NEU.teil_vorhanden_si SI,
            NEU.teil_lzb LZB,
            NEU.teil_kom_pi PI,
            NEU.teil_textcode_kom BENKOMMENTARID,
            NEU.teil_ist_reach REACH,
            NEU.teil_ist_aspg ASPG,
            decode(tcp_sachnr, tcp_sachnr, 'C', NULL) TC,
            NEU.teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_teileersetzung
        inner join w_teil NEU on (ts_sachnr = NEU.teil_sachnr)
        inner join w_teil ALT on (NEU.teil_alt = ALT.teil_sachnr)
        inner join w_ben_gk on (
            NEU.teil_textcode = ben_textcode
            and ben_iso = :iso
            and ben_regiso = :regiso
        )
        left join w_tc_performance on (
            tcp_mospid in (:mospids)
            and tcp_sachnr = ts_sachnr
            __TC_CHECK__
            and tcp_datum_von <= :datum
            and (tcp_datum_bis is null or tcp_datum_bis >= :datum)
        )
        where ts_hg = :hg
          and ts_mospid IN (:mospids)
          __LENKUNG__
          __DATSERIE__
        order by HGALT, UGALT, SACHNRALT, HG, UG, SACHNR
        """;

    private static final String RETRIEVE_SUPERSESSION_CHAIN = """
        select e.ersetzung_sachnr as sachnummer,
            e.ersetzung_sachnr_alt as sachnummerAlt,
            b.ben_text as benennung,
            t.teil_benennzus as zusatz,
            e.ersetzung_ersatzkez as ersatzKez
        from w_teileersetzung e
        join w_teil t
            on t.teil_hauptgr || t.teil_untergrup || t.teil_sachnr = e.ersetzung_sachnr
        left join w_ben_gk b
            on t.teil_textcode = b.ben_textcode
            and lower(b.ben_iso) = :iso
        where e.ersetzung_sachnr_alt = :partNumber
           or e.ersetzung_sachnr = :partNumber
        """;

    private static final RowMapper<MainGroupDto> MAIN_GROUP_MAPPER = (rs, rowNum) ->
        MainGroupDto.builder()
            .hg(rs.getString("HG"))
            .build();

    private static final RowMapper<PartReplacementDto> PART_REPLACEMENT_MAPPER = (rs, rowNum) ->
        PartReplacementDto.builder()
            .hg(rs.getString("HG"))
            .ug(rs.getString("UG"))
            .sachnummer(rs.getString("SACHNR"))
            .sachnummerAlt(rs.getString("SACHNRALT"))
            .hgAlt(rs.getString("HGALT"))
            .ugAlt(rs.getString("UGALT"))
            .at(rs.getString("AT"))
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

    private static final RowMapper<SimpleReplacementDto> SIMPLE_REPLACEMENT_MAPPER = (rs, rowNum) ->
        SimpleReplacementDto.builder()
            .sachnummer(rs.getString("sachnummer"))
            .sachnummerAlt(rs.getString("sachnummerAlt"))
            .benennung(rs.getString("benennung"))
            .zusatz(rs.getString("zusatz"))
            .ersatzKez(rs.getString("ersatzKez"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Finds main group codes for part replacements within a MOSP list.
     *
     * @param mospids list of MOSP identifiers
     * @param hgFrom minimum main group value
     * @param hgToClause optional SQL clause for upper bound (e.g., "and tss_hg &lt;= :hgTo")
     * @param steeringClause optional SQL clause for steering filters
     * @param dataSeriesClause optional SQL clause for data series filters
     * @param extraParams additional parameters used by optional clauses
     * @return list of main group codes
     */
    public List<MainGroupDto> findMainGroups(
        List<String> mospids,
        String hgFrom,
        String hgToClause,
        String steeringClause,
        String dataSeriesClause,
        Map<String, Object> extraParams
    ) {
        String sql = applyClauses(RETRIEVE_HGS, Map.of(
            "__HGBIS__", hgToClause,
            "__LENKUNG__", steeringClause,
            "__DATSERIE__", dataSeriesClause
        ));
        Map<String, Object> params = new HashMap<>(Map.of("mospids", mospids, "hgFrom", hgFrom));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, MAIN_GROUP_MAPPER);
    }

    /**
     * Checks whether a main group exists for a specific code.
     *
     * @param hg main group code
     * @return list containing the matching main group (empty if not found)
     */
    public List<MainGroupDto> findMainGroup(String hg) {
        return jdbc.query(FIND_HG, Map.of("hg", hg), MAIN_GROUP_MAPPER);
    }

    /**
     * Retrieves part replacements for a main group.
     *
     * @param hg main group code
     * @param mospids list of MOSP identifiers
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @param datum reference date for performance filters
     * @param steeringClause optional SQL clause for steering filters
     * @param dataSeriesClause optional SQL clause for data series filters
     * @param tcCheckClause optional SQL clause for land code checks
     * @param extraParams additional parameters used by optional clauses
     * @return list of part replacement entries
     */
    public List<PartReplacementDto> findPartReplacements(
        String hg,
        List<String> mospids,
        String iso,
        String regiso,
        String datum,
        String steeringClause,
        String dataSeriesClause,
        String tcCheckClause,
        Map<String, Object> extraParams
    ) {
        String sql = applyClauses(RETRIEVE_TEILE, Map.of(
            "__LENKUNG__", steeringClause,
            "__DATSERIE__", dataSeriesClause,
            "__TC_CHECK__", tcCheckClause
        ));
        Map<String, Object> params = new HashMap<>(Map.of(
            "hg", hg,
            "mospids", mospids,
            "iso", iso,
            "regiso", regiso,
            "datum", datum
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, PART_REPLACEMENT_MAPPER);
    }

    /**
     * Retrieves the supersession chain for a part number without vehicle context.
     *
     * @param partNumber part number
     * @param iso ISO language code (lowercase)
     * @return list of supersession entries
     */
    public List<SimpleReplacementDto> findSupersessionChain(String partNumber, String iso) {
        return jdbc.query(
            RETRIEVE_SUPERSESSION_CHAIN,
            Map.of("partNumber", partNumber, "iso", iso),
            SIMPLE_REPLACEMENT_MAPPER
        );
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
