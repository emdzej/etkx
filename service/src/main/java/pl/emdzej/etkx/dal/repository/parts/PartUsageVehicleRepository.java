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
import pl.emdzej.etkx.dal.dto.parts.PartUsageVehiclePartDto;
import pl.emdzej.etkx.dal.dto.parts.SimpleUsageDto;

/**
 * Repository for part usage in vehicles (TeilevwdgFzg).
 */
@Repository
@RequiredArgsConstructor
public class PartUsageVehicleRepository {
    private static final String RETRIEVE_HGS = """
        select distinct tvs_hg HG
        from w_teileverwendungfzg_suche
        where tvs_mospid IN (:mospids)
          and tvs_hg >= :hgFrom
          __HGBIS__
          __DATSERIE__
        order by HG
        """;

    private static final String RETRIEVE_TEILE_OHNE_LENKUNG = """
        select distinct NeuesTeil.teil_hauptgr HG,
            NeuesTeil.teil_untergrup UG,
            NeuesTeil.teil_sachnr SACHNR,
            NeuesTeil.teil_alt SACHNRALT,
            NeuesTeil.teil_austausch_alt AT,
            AltesTeil.teil_hauptgr HGALT,
            AltesTeil.teil_untergrup UGALT,
            ben_text BENENNUNG,
            NeuesTeil.teil_benennzus ZUSATZ,
            NeuesTeil.teil_vorhanden_si SI,
            NeuesTeil.teil_lzb LZB,
            NeuesTeil.teil_kom_pi PI,
            NeuesTeil.teil_textcode_kom BENKOMMENTARID,
            NeuesTeil.teil_ist_reach REACH,
            NeuesTeil.teil_ist_aspg ASPG,
            NeuesTeil.teil_ist_stecker STECKER,
            decode(tcp_sachnr, tcp_sachnr, 'C', NULL) TC,
            NeuesTeil.teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_btzeilen_verbauung
        inner join w_teil NeuesTeil on (
            btzeilenv_sachnr = NeuesTeil.teil_sachnr
            and NeuesTeil.teil_hauptgr = :hg
        )
        inner join w_ben_gk on (
            NeuesTeil.teil_textcode = ben_textcode
            and ben_iso = :iso
            and ben_regiso = :regiso
        )
        left join w_teil AltesTeil on (NeuesTeil.teil_alt = AltesTeil.teil_sachnr)
        left join w_tc_performance on (
            tcp_mospid in (:mospids)
            and tcp_sachnr = NeuesTeil.teil_sachnr
            __TC_CHECK__
            and tcp_datum_von <= :datum
            and (tcp_datum_bis is null or tcp_datum_bis >= :datum)
        )
        where btzeilenv_mospid IN (:mospids)
          __DATSERIE__
          and btzeilenv_alter_kz is null
        order by HG, UG, SACHNR
        """;

    private static final String RETRIEVE_TEILE_MIT_LENKUNG = """
        select distinct NeuesTeil.teil_hauptgr HG,
            NeuesTeil.teil_untergrup UG,
            NeuesTeil.teil_sachnr SACHNR,
            NeuesTeil.teil_alt SACHNRALT,
            NeuesTeil.teil_austausch_alt AT,
            AltesTeil.teil_hauptgr HGALT,
            AltesTeil.teil_untergrup UGALT,
            ben_text BENENNUNG,
            NeuesTeil.teil_benennzus ZUSATZ,
            NeuesTeil.teil_vorhanden_si SI,
            NeuesTeil.teil_lzb LZB,
            NeuesTeil.teil_kom_pi PI,
            NeuesTeil.teil_textcode_kom BENKOMMENTARID,
            NeuesTeil.teil_ist_reach REACH,
            NeuesTeil.teil_ist_aspg ASPG,
            NeuesTeil.teil_ist_stecker STECKER,
            decode(tcp_sachnr, tcp_sachnr, 'C', NULL) TC,
            NeuesTeil.teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_btzeilen_verbauung
        inner join w_teil NeuesTeil on (
            btzeilenv_sachnr = NeuesTeil.teil_sachnr
            and NeuesTeil.teil_hauptgr = :hg
        )
        inner join w_btzeilen on (
            btzeilenv_sachnr = btzeilen_sachnr
            and btzeilenv_btnr = btzeilen_btnr
            and btzeilenv_pos = btzeilen_pos
            and (btzeilen_lenkg = :lenkung or btzeilen_lenkg is null)
        )
        inner join w_ben_gk on (
            NeuesTeil.teil_textcode = ben_textcode
            and ben_iso = :iso
            and ben_regiso = :regiso
        )
        left join w_teil AltesTeil on (NeuesTeil.teil_alt = AltesTeil.teil_sachnr)
        left join w_tc_performance on (
            tcp_mospid in (:mospids)
            and tcp_sachnr = NeuesTeil.teil_sachnr
            __TC_CHECK__
            and tcp_datum_von <= :datum
            and (tcp_datum_bis is null or tcp_datum_bis >= :datum)
        )
        where btzeilenv_mospid IN (:mospids)
          __DATSERIE__
          and btzeilenv_alter_kz is null
        order by HG, UG, SACHNR
        """;

    private static final String RETRIEVE_TEIL_ONLY_IN_OHNE_LENKUNG = """
        select count(*) ANZ
        from w_btzeilen_verbauung
        where btzeilenv_sachnr = :sachnr
          and btzeilenv_mospid NOT IN (:mospids)
        """;

    private static final String RETRIEVE_TEIL_ONLY_IN_MIT_LENKUNG = """
        select count(*) ANZ
        from w_btzeilen, w_btzeilen_verbauung
        where btzeilen_sachnr = :sachnr
          and (btzeilen_lenkg = :lenkung or btzeilen_lenkg is null)
          and btzeilen_btnr = btzeilenv_btnr
          and btzeilen_pos = btzeilenv_pos
          and btzeilen_sachnr = btzeilenv_sachnr
          and btzeilenv_mospid NOT IN (:mospids)
        """;

    private static final String RETRIEVE_SIMPLE_USAGE = """
        select distinct BR.ben_text Baureihe,
            fztyp_erwvbez Modell,
            KAR.ben_text Karosserie
        from w_btzeilen_verbauung
        inner join w_fztyp on (btzeilenv_mospid = fztyp_mospid)
        inner join w_baureihe on (fztyp_baureihe = baureihe_baureihe)
        inner join w_ben_gk BR on (baureihe_textcode = BR.ben_textcode and BR.ben_iso = :iso)
        inner join w_publben on (fztyp_karosserie = publben_bezeichnung and publben_art = 'K')
        inner join w_ben_gk KAR on (publben_textcode = KAR.ben_textcode and KAR.ben_iso = :iso)
        where btzeilenv_sachnr = :sachnummer
          and fztyp_sichtschutz = 'N'
          __MOSPID__
        order by Baureihe, Modell, Karosserie
        """;

    private static final RowMapper<MainGroupDto> MAIN_GROUP_MAPPER = (rs, rowNum) ->
        MainGroupDto.builder()
            .hg(rs.getString("HG"))
            .build();

    private static final RowMapper<PartUsageVehiclePartDto> PART_USAGE_PART_MAPPER = (rs, rowNum) ->
        PartUsageVehiclePartDto.builder()
            .hg(rs.getString("HG"))
            .ug(rs.getString("UG"))
            .sachnummer(rs.getString("SACHNR"))
            .sachnummerAlt(rs.getString("SACHNRALT"))
            .at(rs.getString("AT"))
            .hgAlt(rs.getString("HGALT"))
            .ugAlt(rs.getString("UGALT"))
            .benennung(rs.getString("BENENNUNG"))
            .zusatz(rs.getString("ZUSATZ"))
            .si(rs.getString("SI"))
            .lzb(rs.getString("LZB"))
            .pi(rs.getString("PI"))
            .benKommentarId(rs.getString("BENKOMMENTARID"))
            .reach(rs.getString("REACH"))
            .aspg(rs.getString("ASPG"))
            .stecker(rs.getString("STECKER"))
            .tc(rs.getString("TC"))
            .teilDiebstahlrelevant(rs.getString("Teil_Diebstahlrelevant"))
            .build();

    private static final RowMapper<SimpleUsageDto> SIMPLE_USAGE_MAPPER = (rs, rowNum) ->
        SimpleUsageDto.builder()
            .baureihe(rs.getString("Baureihe"))
            .karosserie(rs.getString("Karosserie"))
            .modell(rs.getString("Modell"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Finds main group codes for vehicle part usage within a MOSP list.
     *
     * @param mospids list of MOSP identifiers
     * @param hgFrom minimum main group value
     * @param hgToClause optional SQL clause for upper bound
     * @param dataSeriesClause optional SQL clause for data series filters
     * @param extraParams additional parameters used by optional clauses
     * @return list of main group codes
     */
    public List<MainGroupDto> findMainGroups(
        List<String> mospids,
        String hgFrom,
        String hgToClause,
        String dataSeriesClause,
        Map<String, Object> extraParams
    ) {
        String sql = applyClauses(RETRIEVE_HGS, Map.of(
            "__HGBIS__", hgToClause,
            "__DATSERIE__", dataSeriesClause
        ));
        Map<String, Object> params = new HashMap<>(Map.of("mospids", mospids, "hgFrom", hgFrom));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, MAIN_GROUP_MAPPER);
    }

    /**
     * Retrieves used parts without steering filters.
     */
    public List<PartUsageVehiclePartDto> findPartsWithoutSteering(
        String hg,
        List<String> mospids,
        String iso,
        String regiso,
        String datum,
        String dataSeriesClause,
        String tcCheckClause,
        Map<String, Object> extraParams
    ) {
        String sql = applyClauses(RETRIEVE_TEILE_OHNE_LENKUNG, Map.of(
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
        return jdbc.query(sql, params, PART_USAGE_PART_MAPPER);
    }

    /**
     * Retrieves used parts with steering filters.
     */
    public List<PartUsageVehiclePartDto> findPartsWithSteering(
        String hg,
        String lenkung,
        List<String> mospids,
        String iso,
        String regiso,
        String datum,
        String dataSeriesClause,
        String tcCheckClause,
        Map<String, Object> extraParams
    ) {
        String sql = applyClauses(RETRIEVE_TEILE_MIT_LENKUNG, Map.of(
            "__DATSERIE__", dataSeriesClause,
            "__TC_CHECK__", tcCheckClause
        ));
        Map<String, Object> params = new HashMap<>(Map.of(
            "hg", hg,
            "lenkung", lenkung,
            "mospids", mospids,
            "iso", iso,
            "regiso", regiso,
            "datum", datum
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, PART_USAGE_PART_MAPPER);
    }

    /**
     * Retrieves simplified vehicle usage data for a part.
     */
    public List<SimpleUsageDto> findSimpleUsage(String sachnummer, String mospId, String iso) {
        String mospClause = StringUtils.hasText(mospId) ? "and btzeilenv_mospid = :mospId" : null;
        String sql = applyClauses(RETRIEVE_SIMPLE_USAGE, Map.of("__MOSPID__", mospClause));
        Map<String, Object> params = new HashMap<>(Map.of("sachnummer", sachnummer, "iso", iso));
        if (StringUtils.hasText(mospId)) {
            params.put("mospId", mospId);
        }
        return jdbc.query(sql, params, SIMPLE_USAGE_MAPPER);
    }

    /**
     * Counts part usage entries outside the MOSP list without steering filters.
     */
    public long countPartOnlyInOtherMospWithoutSteering(String sachnr, List<String> mospids) {
        return jdbc.queryForObject(RETRIEVE_TEIL_ONLY_IN_OHNE_LENKUNG, Map.of("sachnr", sachnr, "mospids", mospids),
            Long.class);
    }

    /**
     * Counts part usage entries outside the MOSP list with steering filters.
     */
    public long countPartOnlyInOtherMospWithSteering(String sachnr, String lenkung, List<String> mospids) {
        return jdbc.queryForObject(RETRIEVE_TEIL_ONLY_IN_MIT_LENKUNG,
            Map.of("sachnr", sachnr, "lenkung", lenkung, "mospids", mospids),
            Long.class);
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
