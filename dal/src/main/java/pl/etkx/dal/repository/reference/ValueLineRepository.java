package pl.etkx.dal.repository.reference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pl.etkx.dal.dto.reference.ValueLineMainGroupDto;
import pl.etkx.dal.dto.reference.ValueLinePartNumberDto;
import pl.etkx.dal.dto.reference.ValueLinePlateDto;
import pl.etkx.dal.dto.reference.ValueLineSeriesDto;
import pl.etkx.dal.dto.reference.ValueLineSetPartDto;

/**
 * Repository for ValueLine reference data.
 */
@Repository
@RequiredArgsConstructor
public class ValueLineRepository {
    private static final String LOAD_HGS = """
        select distinct hgfg_hg HG, ben_text Benennung
        from w_kompl_satz, w_ben_gk, w_hgfg
        where ks_marke_tps = :marke
          and ks_ist_valueline = 'J'
          and ks_produktart IN ('B', :produktart)
          and hgfg_produktart = :produktart
          and ks_hg = hgfg_hg
          and hgfg_fg = '00'
          and hgfg_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by HG
        """;

    private static final String LOAD_SAETZE = """
        select distinct teil_hauptgr HG,
            teil_untergrup UG,
            teil_sachnr Sachnummer,
            ben_text Benennung,
            teil_benennzus Zusatz,
            teil_vorhanden_si vorhandenSI,
            teil_textcode_kom BenKommentarId,
            teil_ist_reach Reach,
            teil_ist_aspg Aspg,
            decode(teil_sachnr, tcp_sachnr, 'C', NULL) TC,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_kompl_satz
        inner join w_teil on (ks_sachnr_satz = teil_sachnr)
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
        where ks_marke_tps = :marke
          and ks_produktart IN ('B', :produktart)
          and ks_hg = :hg
          and ks_ist_valueline = 'J'
        order by HG, UG, Sachnummer
        """;

    private static final String LOAD_BTE_BAUREIHEN = """
        select distinct baureihe_baureihe BAUREIHE,
            ben_text EXT_BAUREIHE,
            baureihe_position POS
        from w_hgfg, w_bildtaf, w_ben_gk, w_bildtaf_suche, w_baureihe, w_fztyp
        where hgfg_bereich = 'KONZERN'
          and hgfg_produktart = :produktart
          and hgfg_ist_valueline = 'J'
          and bildtaf_hg = hgfg_hg
          and bildtaf_fg = hgfg_fg
          and bildtafs_btnr = bildtaf_btnr
          and bildtafs_hg = bildtaf_hg
          and fztyp_mospid = bildtafs_mospid
          and fztyp_vbereich = :catalogScope
          and fztyp_sichtschutz = 'N'
          and fztyp_ktlgausf IN (:regions)
          and baureihe_baureihe = fztyp_baureihe
          and baureihe_marke_tps = :marke
          and baureihe_produktart = :produktart
          and ben_textcode = baureihe_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by POS
        """;

    private static final String LOAD_BILDTAFELN = """
        select distinct bildtaf_btnr BildtafelNr,
            B.ben_text Benennung,
            fztyp_erwvbez Modell,
            K.ben_text Karosserie,
            fztyp_karosserie KarosserieId,
            baureihe_bauart Bauart,
            fztyp_ktlgausf Region,
            bildtaf_kommbt Kommentar,
            bildtaf_vorh_cp CPVorhanden,
            bildtaf_bedkez BedingungKZ,
            bildtaf_pos Pos
        from w_hgfg, w_bildtaf, w_ben_gk B, w_publben, w_ben_gk K, w_bildtaf_suche, w_baureihe, w_fztyp
        where hgfg_ist_valueline = 'J'
          and bildtaf_hg = hgfg_hg
          and bildtaf_fg = hgfg_fg
          and bildtafs_btnr = bildtaf_btnr
          and bildtafs_hg = bildtaf_hg
          and fztyp_mospid = bildtafs_mospid
          and fztyp_vbereich = :catalogScope
          and fztyp_sichtschutz = 'N'
          and fztyp_ktlgausf IN (:regions)
          and baureihe_baureihe = :baureihe
          and baureihe_baureihe = fztyp_baureihe
          and baureihe_marke_tps = :marke
          and baureihe_produktart = :produktart
          and B.ben_textcode = bildtaf_textc
          and B.ben_iso = :iso
          and B.ben_regiso = :regiso
          and publben_bezeichnung = fztyp_karosserie
          and publben_art = 'K'
          and K.ben_textcode = publben_textcode
          and K.ben_iso = :iso
          and K.ben_regiso = :regiso
        order by Pos
        """;

    private static final String LOAD_TEILENUMMERN = """
        select distinct teil_hauptgr Hauptgruppe,
            teil_untergrup Untergruppe,
            teil_sachnr Sachnummer,
            ben_teil.ben_text Benennung,
            teil_benennzus Zusatz,
            ben_komm.ben_text Kommentar,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_teil
        inner join w_teil_marken on (teil_sachnr = teilm_sachnr and teilm_marke_tps = 'BMW')
        inner join w_ben_gk ben_teil on (ben_textcode = teil_textcode and ben_teil.ben_iso = 'de' and ben_teil.ben_regiso = '  ')
        left join w_ben_gk ben_komm on (ben_komm.ben_textcode = teil_textcode_kom and ben_komm.ben_iso = '  ' and ben_komm.ben_regiso = '  ')
        where teil_ist_valueline = 'J'
          and teil_sachnr = teilm_sachnr
          and teilm_marke_tps = 'BMW'
          and teil_verbaubar = 'J'
          and teil_produktart in ('P', 'B')
        order by Benennung, Hauptgruppe, Untergruppe, Sachnummer
        """;

    private static final RowMapper<ValueLineMainGroupDto> MAIN_GROUP_MAPPER = (rs, rowNum) ->
        ValueLineMainGroupDto.builder()
            .hg(rs.getString("HG"))
            .benennung(rs.getString("Benennung"))
            .build();

    private static final RowMapper<ValueLineSetPartDto> SET_PART_MAPPER = (rs, rowNum) ->
        ValueLineSetPartDto.builder()
            .hg(rs.getString("HG"))
            .ug(rs.getString("UG"))
            .sachnummer(rs.getString("Sachnummer"))
            .benennung(rs.getString("Benennung"))
            .zusatz(rs.getString("Zusatz"))
            .vorhandenSi(rs.getString("vorhandenSI"))
            .benKommentarId(rs.getString("BenKommentarId"))
            .reach(rs.getString("Reach"))
            .aspg(rs.getString("Aspg"))
            .tc(rs.getString("TC"))
            .teilDiebstahlrelevant(rs.getString("Teil_Diebstahlrelevant"))
            .build();

    private static final RowMapper<ValueLineSeriesDto> SERIES_MAPPER = (rs, rowNum) ->
        ValueLineSeriesDto.builder()
            .baureihe(rs.getString("BAUREIHE"))
            .extBaureihe(rs.getString("EXT_BAUREIHE"))
            .pos(getInteger(rs, "POS"))
            .build();

    private static final RowMapper<ValueLinePlateDto> PLATE_MAPPER = (rs, rowNum) ->
        ValueLinePlateDto.builder()
            .bildtafelNr(rs.getString("BildtafelNr"))
            .benennung(rs.getString("Benennung"))
            .modell(rs.getString("Modell"))
            .karosserie(rs.getString("Karosserie"))
            .karosserieId(rs.getString("KarosserieId"))
            .bauart(rs.getString("Bauart"))
            .region(rs.getString("Region"))
            .kommentar(rs.getString("Kommentar"))
            .cpVorhanden(rs.getString("CPVorhanden"))
            .bedingungKz(rs.getString("BedingungKZ"))
            .pos(getInteger(rs, "Pos"))
            .build();

    private static final RowMapper<ValueLinePartNumberDto> PART_NUMBER_MAPPER = (rs, rowNum) ->
        ValueLinePartNumberDto.builder()
            .hauptgruppe(rs.getString("Hauptgruppe"))
            .untergruppe(rs.getString("Untergruppe"))
            .sachnummer(rs.getString("Sachnummer"))
            .benennung(rs.getString("Benennung"))
            .zusatz(rs.getString("Zusatz"))
            .kommentar(rs.getString("Kommentar"))
            .teilDiebstahlrelevant(rs.getString("Teil_Diebstahlrelevant"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Loads ValueLine main groups.
     *
     * @param marke brand code
     * @param produktart product type
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return list of main group entries
     */
    public List<ValueLineMainGroupDto> loadMainGroups(String marke, String produktart, String iso, String regiso) {
        return jdbc.query(LOAD_HGS, Map.of(
            "marke", marke,
            "produktart", produktart,
            "iso", iso,
            "regiso", regiso
        ), MAIN_GROUP_MAPPER);
    }

    /**
     * Loads ValueLine sets for a given main group.
     *
     * @param marke brand code
     * @param produktart product type
     * @param hg main group
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @param catalogScopes catalog scopes
     * @param date date value used for filtering
     * @param tcCheckClause optional SQL clause for TC filtering
     * @param extraParams additional parameters used by optional clauses
     * @return list of set part entries
     */
    public List<ValueLineSetPartDto> loadSets(
        String marke,
        String produktart,
        String hg,
        String iso,
        String regiso,
        List<String> catalogScopes,
        String date,
        String tcCheckClause,
        Map<String, Object> extraParams
    ) {
        String sql = applyClauses(LOAD_SAETZE, Map.of("__TC_CHECK__", tcCheckClause));
        Map<String, Object> params = new HashMap<>(Map.of(
            "marke", marke,
            "produktart", produktart,
            "hg", hg,
            "iso", iso,
            "regiso", regiso,
            "catalogScopes", catalogScopes,
            "date", date
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, SET_PART_MAPPER);
    }

    /**
     * Loads ValueLine series (BTE) entries.
     *
     * @param produktart product type
     * @param catalogScope catalog scope identifier
     * @param regions region identifiers
     * @param marke brand code
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return list of series entries
     */
    public List<ValueLineSeriesDto> loadSeries(
        String produktart,
        String catalogScope,
        List<String> regions,
        String marke,
        String iso,
        String regiso
    ) {
        return jdbc.query(LOAD_BTE_BAUREIHEN, Map.of(
            "produktart", produktart,
            "catalogScope", catalogScope,
            "regions", regions,
            "marke", marke,
            "iso", iso,
            "regiso", regiso
        ), SERIES_MAPPER);
    }

    /**
     * Loads ValueLine illustration plates.
     *
     * @param catalogScope catalog scope identifier
     * @param regions region identifiers
     * @param baureihe series identifier
     * @param marke brand code
     * @param produktart product type
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return list of plate entries
     */
    public List<ValueLinePlateDto> loadPlates(
        String catalogScope,
        List<String> regions,
        String baureihe,
        String marke,
        String produktart,
        String iso,
        String regiso
    ) {
        return jdbc.query(LOAD_BILDTAFELN, Map.of(
            "catalogScope", catalogScope,
            "regions", regions,
            "baureihe", baureihe,
            "marke", marke,
            "produktart", produktart,
            "iso", iso,
            "regiso", regiso
        ), PLATE_MAPPER);
    }

    /**
     * Loads ValueLine part numbers.
     *
     * @return list of part number entries
     */
    public List<ValueLinePartNumberDto> loadPartNumbers() {
        return jdbc.query(LOAD_TEILENUMMERN, Map.of(), PART_NUMBER_MAPPER);
    }

    private static Integer getInteger(java.sql.ResultSet rs, String column) {
        try {
            int value = rs.getInt(column);
            return rs.wasNull() ? null : value;
        } catch (java.sql.SQLException ex) {
            throw new IllegalStateException(ex);
        }
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
