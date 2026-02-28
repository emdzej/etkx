package pl.etkx.dal.repository.parts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pl.etkx.dal.dto.parts.PartUsageDesignationLineDto;
import pl.etkx.dal.dto.parts.PartUsageModelColumnDto;
import pl.etkx.dal.dto.parts.PartUsagePlateDto;
import pl.etkx.dal.dto.parts.PartUsageSeriesDto;

/**
 * Repository for part usage by designation (TeilevwdgBen).
 */
@Repository
@RequiredArgsConstructor
public class PartUsageDesignationRepository {
    private static final String RETRIEVE_BAUREIHEN = """
        select distinct baureihe_baureihe BAUREIHE, benbr.ben_text EXT_BAUREIHE, baureihe_position POS
        from w_baureihe, w_fztyp, w_bildtaf_suche, w_bildtaf, w_ben_gk H, w_ben_gk benbr
        where __SUCHSTRING__
          and H.ben_iso = :iso
          and H.ben_regiso = :regiso
          and bildtaf_textc = H.ben_textcode
          and bildtafs_btnr = bildtaf_btnr
          and bildtafs_hg = bildtaf_hg
          and fztyp_mospid = bildtafs_mospid
          and fztyp_vbereich = :katalogumfang
          and fztyp_sichtschutz = 'N'
          and fztyp_ktlgausf IN (:regionen)
          and baureihe_baureihe = fztyp_baureihe
          and baureihe_marke_tps = :marke
          and baureihe_produktart = :produktart
          and benbr.ben_textcode = baureihe_textcode
          and benbr.ben_iso = :iso
          and benbr.ben_regiso = :regiso
        order by POS
        """;

    private static final String RETRIEVE_BAUREIHEN_SONDERLOCKE = """
        select distinct baureihe_baureihe BAUREIHE, benbr.ben_text EXT_BAUREIHE, baureihe_position POS
        from w_baureihe, w_fztyp, w_bildtaf_suche, w_bildtaf, w_ben_gk H, w_ben_gk benbr
        where (__SUCHSTRING1__ or __SUCHSTRING2__)
          and H.ben_iso = :iso
          and H.ben_regiso = :regiso
          and bildtaf_textc = H.ben_textcode
          and bildtafs_btnr = bildtaf_btnr
          and bildtafs_hg = bildtaf_hg
          and fztyp_mospid = bildtafs_mospid
          and fztyp_vbereich = :katalogumfang
          and fztyp_sichtschutz = 'N'
          and fztyp_ktlgausf IN (:regionen)
          and baureihe_baureihe = fztyp_baureihe
          and baureihe_marke_tps = :marke
          and baureihe_produktart = :produktart
          and benbr.ben_textcode = baureihe_textcode
          and benbr.ben_iso = :iso
          and benbr.ben_regiso = :regiso
        order by POS
        """;

    private static final String RETRIEVE_BAUREIHEN_TR = """
        select distinct baureihe_baureihe BAUREIHE, ben_text EXT_BAUREIHE, baureihe_position POS
        from w_baureihe
        inner join w_ben_gk on (ben_textcode = baureihe_textcode and ben_iso = :iso and ben_regiso = :regiso)
        where baureihe_baureihe IN (
            select distinct fztyp_baureihe
            from w_ben_gk H
            inner join w_bildtaf on (
                bildtaf_textc = ben_textcode
                and bildtaf_vbereich = :katalogumfang
                and bildtaf_produktart = :produktart
            )
            inner join w_bildtaf_marke on (
                bildtafm_btnr = bildtaf_btnr
                and bildtafm_marke_tps = :marke
            )
            inner join w_bildtaf_suche on (bildtafs_hg = bildtaf_hg and bildtafs_btnr = bildtaf_btnr)
            inner join w_fztyp on (
                fztyp_mospid = bildtafs_mospid
                and fztyp_vbereich = :katalogumfang
                and fztyp_sichtschutz = 'N'
                and fztyp_ktlgausf IN (:regionen)
            )
            where ben_iso = :iso
              and ben_regiso = :regiso
              and __SUCHSTRING__
        )
        order by POS
        """;

    private static final String RETRIEVE_MODELLSPALTEN = """
        select distinct NULL MENGE,
            bildtaf_btnr BTNR,
            B.ben_text BTUEBERSCHRIFT,
            fztyp_erwvbez MODELL,
            K.ben_text KAROSSERIE,
            fztyp_karosserie KAROSSERIE_ID,
            baureihe_bauart BAUART,
            fztyp_ktlgausf REGION,
            bildtaf_kommbt Kommentar,
            bildtaf_vorh_cp CPVorhanden,
            bildtaf_bedkez BedingungKZ,
            marktetk_isokz MarktIso
        from w_ben_gk H, w_bildtaf_marke, w_bildtaf_suche,
            w_fztyp, w_baureihe, w_ben_gk K, w_publben, w_ben_gk B, w_bildtaf
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where H.ben_iso = :iso
          and H.ben_regiso = :regiso
          and __SUCHSTRING__
          and H.ben_textcode = bildtaf_textc
          and bildtaf_vbereich = :katalogumfang
          and bildtaf_produktart = :produktart
          and bildtafm_btnr = bildtaf_btnr
          and bildtafm_marke_tps = :marke
          and bildtaf_hg = bildtafs_hg
          and bildtaf_btnr = bildtafs_btnr
          and bildtafs_mospid = fztyp_mospid
          and fztyp_baureihe = :baureihe
          and fztyp_vbereich = :katalogumfang
          and fztyp_sichtschutz = 'N'
          and fztyp_ktlgausf IN (:regionen)
          and fztyp_baureihe = baureihe_baureihe
          and fztyp_karosserie = publben_bezeichnung
          and publben_art = 'K'
          and publben_textcode = K.ben_textcode
          and K.ben_iso = :iso
          and K.ben_regiso = :regiso
          and bildtaf_textc = B.ben_textcode
          and B.ben_iso = :iso
          and B.ben_regiso = :regiso
          __MODELLSPALTEN__
        order by MODELL, BTNR, KAROSSERIE, REGION
        """;

    private static final String RETRIEVE_MODELLSPALTEN_SONDERLOCKE = """
        select distinct NULL MENGE,
            bildtaf_btnr BTNR,
            B.ben_text BTUEBERSCHRIFT,
            fztyp_erwvbez MODELL,
            K.ben_text KAROSSERIE,
            fztyp_karosserie KAROSSERIE_ID,
            baureihe_bauart BAUART,
            fztyp_ktlgausf REGION,
            bildtaf_kommbt Kommentar,
            bildtaf_vorh_cp CPVorhanden,
            bildtaf_bedkez BedingungKZ,
            marktetk_isokz MarktIso
        from w_ben_gk H, w_bildtaf_suche, w_bildtaf_marke,
            w_fztyp, w_baureihe, w_ben_gk K, w_publben, w_ben_gk B, w_bildtaf
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where H.ben_iso = :iso
          and H.ben_regiso = :regiso
          and (__SUCHSTRING1__ or __SUCHSTRING2__)
          and H.ben_textcode = bildtaf_textc
          and bildtaf_vbereich = :katalogumfang
          and bildtaf_produktart = :produktart
          and bildtafm_btnr = bildtaf_btnr
          and bildtafm_marke_tps = :marke
          and bildtaf_hg = bildtafs_hg
          and bildtaf_btnr = bildtafs_btnr
          and bildtafs_mospid = fztyp_mospid
          and fztyp_baureihe = :baureihe
          and fztyp_vbereich = :katalogumfang
          and fztyp_sichtschutz = 'N'
          and fztyp_ktlgausf IN (:regionen)
          and fztyp_baureihe = baureihe_baureihe
          and fztyp_karosserie = publben_bezeichnung
          and publben_art = 'K'
          and publben_textcode = K.ben_textcode
          and K.ben_iso = :iso
          and K.ben_regiso = :regiso
          and bildtaf_textc = B.ben_textcode
          and B.ben_iso = :iso
          and B.ben_regiso = :regiso
          __MODELLSPALTEN__
        order by MODELL, BTNR, KAROSSERIE, REGION
        """;

    private static final String RETRIEVE_BAUREIHEN_TNR = """
        select distinct baureihe_baureihe BAUREIHE, benbr.ben_text EXT_BAUREIHE, baureihe_position POS
        from w_baureihe, w_fztyp, w_ben_gk benbr, w_btzeilen_verbauung
        where btzeilenv_sachnr IN (:sachnummern)
          and fztyp_mospid = btzeilenv_mospid
          and fztyp_vbereich = :katalogumfang
          and fztyp_sichtschutz = 'N'
          and fztyp_ktlgausf IN (:regionen)
          and baureihe_baureihe = fztyp_baureihe
          and baureihe_marke_tps = :marke
          and baureihe_produktart = :produktart
          and benbr.ben_textcode = baureihe_textcode
          and benbr.ben_iso = :iso
          and benbr.ben_regiso = :regiso
          and btzeilenv_alter_kz is null
        order by POS
        """;

    private static final String RETRIEVE_MODELLSPALTEN_TNR = """
        select distinct btzeilenv_vmenge MENGE,
            bildtaf_btnr BTNR,
            B.ben_text BTUEBERSCHRIFT,
            fztyp_erwvbez MODELL,
            K.ben_text KAROSSERIE,
            fztyp_karosserie KAROSSERIE_ID,
            baureihe_bauart BAUART,
            fztyp_ktlgausf REGION,
            bildtaf_kommbt Kommentar,
            bildtaf_vorh_cp CPVorhanden,
            bildtaf_bedkez BedingungKZ,
            marktetk_isokz MarktIso
        from w_bildtaf_marke, w_bildtaf_suche, w_fztyp,
            w_baureihe, w_ben_gk K, w_publben, w_ben_gk B, w_btzeilen_verbauung, w_bildtaf
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where btzeilenv_sachnr IN (:sachnummern)
          and btzeilenv_btnr = bildtaf_btnr
          and bildtaf_vbereich = :katalogumfang
          and bildtaf_produktart = :produktart
          and bildtafm_btnr = bildtaf_btnr
          and bildtafm_marke_tps = :marke
          and bildtaf_hg = bildtafs_hg
          and bildtaf_btnr = bildtafs_btnr
          and bildtafs_mospid = fztyp_mospid
          and fztyp_baureihe = :baureihe
          and fztyp_vbereich = :katalogumfang
          and fztyp_sichtschutz = 'N'
          and fztyp_ktlgausf IN (:regionen)
          and fztyp_baureihe = baureihe_baureihe
          and fztyp_karosserie = publben_bezeichnung
          and publben_art = 'K'
          and publben_textcode = K.ben_textcode
          and K.ben_iso = :iso
          and K.ben_regiso = :regiso
          and bildtaf_textc = B.ben_textcode
          and B.ben_iso = :iso
          and B.ben_regiso = :regiso
          and bildtaf_btnr = btzeilenv_btnr
          and bildtafs_mospid = btzeilenv_mospid
          and btzeilenv_alter_kz is null
        order by MODELL, BTNR, KAROSSERIE, REGION
        """;

    private static final String SEARCH_SNR_TVBENENNUNG = """
        select distinct teil_hauptgr Hauptgruppe,
            teil_untergrup Untergruppe,
            teil_sachnr Sachnummer,
            ben_teil.ben_text Benennung,
            teil_benennzus Zusatz,
            ben_komm.ben_text BenennungKommentar,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_btzeilen_verbauung, w_teil_marken, w_teil
        inner join w_ben_gk ben_teil on (
            ben_textcode = teil_textcode
            and ben_teil.ben_iso = :iso
            and ben_teil.ben_regiso = :regiso
            and __SUCHSTRING__
        )
        left join w_ben_gk ben_komm on (
            ben_komm.ben_textcode = teil_textcode_kom
            and ben_komm.ben_iso = :iso
            and ben_komm.ben_regiso = :regiso
        )
        where teil_sachnr = teilm_sachnr
          and teilm_marke_tps = :marke
          and teil_verbaubar = 'J'
          and teil_produktart in (:produktart, 'B')
          and btzeilenv_sachnr = teil_sachnr
          and btzeilenv_alter_kz is null
        union
        select distinct teil_hauptgr Hauptgruppe,
            teil_untergrup Untergruppe,
            teil_sachnr Sachnummer,
            ben_teil.ben_text Benennung,
            teil_benennzus Zusatz,
            ben_komm.ben_text BenennungKommentar,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_btzeilenugb, w_teil_marken, w_teil
        inner join w_ben_gk ben_teil on (
            ben_textcode = teil_textcode
            and ben_teil.ben_iso = :iso
            and ben_teil.ben_regiso = :regiso
            and __SUCHSTRING__
        )
        left join w_ben_gk ben_komm on (
            ben_komm.ben_textcode = teil_textcode_kom
            and ben_komm.ben_iso = :iso
            and ben_komm.ben_regiso = :regiso
        )
        where teil_sachnr = teilm_sachnr
          and teilm_marke_tps = :marke
          and teil_verbaubar = 'J'
          and teil_produktart in (:produktart, 'B')
          and btzeilenu_sachnr = teil_sachnr
        order by Benennung, Hauptgruppe, Untergruppe, Sachnummer
        """;

    private static final String SEARCH_SNR_TVBENENNUNG_SONDERLOCKE = """
        select distinct teil_hauptgr Hauptgruppe,
            teil_untergrup Untergruppe,
            teil_sachnr Sachnummer,
            ben_teil.ben_text Benennung,
            teil_benennzus Zusatz,
            ben_komm.ben_text BenennungKommentar,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_btzeilen_verbauung, w_teil_marken, w_teil
        inner join w_ben_gk ben_teil on (
            ben_textcode = teil_textcode
            and ben_teil.ben_iso = :iso
            and ben_teil.ben_regiso = :regiso
            and (__SUCHSTRING1__ or __SUCHSTRING2__)
        )
        left join w_ben_gk ben_komm on (
            ben_komm.ben_textcode = teil_textcode_kom
            and ben_komm.ben_iso = :iso
            and ben_komm.ben_regiso = :regiso
        )
        where teil_sachnr = teilm_sachnr
          and teilm_marke_tps = :marke
          and teil_verbaubar = 'J'
          and teil_produktart in (:produktart, 'B')
          and btzeilenv_sachnr = teil_sachnr
          and btzeilenv_alter_kz is null
        union
        select distinct teil_hauptgr Hauptgruppe,
            teil_untergrup Untergruppe,
            teil_sachnr Sachnummer,
            ben_teil.ben_text Benennung,
            teil_benennzus Zusatz,
            ben_komm.ben_text BenennungKommentar,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_btzeilenugb, w_teil_marken, w_teil
        inner join w_ben_gk ben_teil on (
            ben_textcode = teil_textcode
            and ben_teil.ben_iso = :iso
            and ben_teil.ben_regiso = :regiso
            and (__SUCHSTRING1__ or __SUCHSTRING2__)
        )
        left join w_ben_gk ben_komm on (
            ben_komm.ben_textcode = teil_textcode_kom
            and ben_komm.ben_iso = :iso
            and ben_komm.ben_regiso = :regiso
        )
        where teil_sachnr = teilm_sachnr
          and teilm_marke_tps = :marke
          and teil_verbaubar = 'J'
          and teil_produktart in (:produktart, 'B')
          and btzeilenu_sachnr = teil_sachnr
        order by Benennung, Hauptgruppe, Untergruppe, Sachnummer
        """;

    private static final String SEARCH_BT_SACHNUMMERN_TVBEN = """
        select distinct bildtaf_btnr BildtafelNr,
            bildtaf_bteart BildtafelArt,
            ben_text Benennung,
            bildtaf_kommbt Kommentar,
            bildtaf_vorh_cp CPVorhanden,
            bildtaf_bedkez BedingungKZ,
            bildtaf_pos Pos,
            marktetk_isokz MarktIso
        from w_ben_gk, w_bildtaf, w_btzeilen, w_btzeilen_verbauung, w_fztyp, w_baureihe
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where btzeilenv_mospid = fztyp_mospid
          and fztyp_vbereich = :katalogumfang
          and fztyp_sichtschutz = 'N'
          and fztyp_ktlgausf IN (:regionen)
          and fztyp_baureihe = baureihe_baureihe
          and baureihe_marke_tps = :marke
          and baureihe_produktart = :produktart
          and baureihe_vbereich in ('BE', :katalogumfang)
          and btzeilenv_sachnr IN (:sachnummern)
          and btzeilenv_btnr = btzeilen_btnr
          and btzeilenv_pos = btzeilen_pos
          and btzeilen_btnr = bildtaf_btnr
          and bildtaf_textc = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        """;

    private static final RowMapper<PartUsageSeriesDto> SERIES_MAPPER = (rs, rowNum) ->
        PartUsageSeriesDto.builder()
            .baureihe(rs.getString("BAUREIHE"))
            .extBaureihe(rs.getString("EXT_BAUREIHE"))
            .pos(getInteger(rs, "POS"))
            .build();

    private static final RowMapper<PartUsageModelColumnDto> MODEL_COLUMN_MAPPER = (rs, rowNum) ->
        PartUsageModelColumnDto.builder()
            .menge(rs.getString("MENGE"))
            .btnr(rs.getString("BTNR"))
            .btUeberschrift(rs.getString("BTUEBERSCHRIFT"))
            .modell(rs.getString("MODELL"))
            .karosserie(rs.getString("KAROSSERIE"))
            .karosserieId(rs.getString("KAROSSERIE_ID"))
            .bauart(rs.getString("BAUART"))
            .region(rs.getString("REGION"))
            .kommentar(rs.getString("Kommentar"))
            .cpVorhanden(rs.getString("CPVorhanden"))
            .bedingungKz(rs.getString("BedingungKZ"))
            .marktIso(rs.getString("MarktIso"))
            .build();

    private static final RowMapper<PartUsageDesignationLineDto> DESIGNATION_LINE_MAPPER = (rs, rowNum) ->
        PartUsageDesignationLineDto.builder()
            .hauptgruppe(rs.getString("Hauptgruppe"))
            .untergruppe(rs.getString("Untergruppe"))
            .sachnummer(rs.getString("Sachnummer"))
            .benennung(rs.getString("Benennung"))
            .zusatz(rs.getString("Zusatz"))
            .benennungKommentar(rs.getString("BenennungKommentar"))
            .teilDiebstahlrelevant(rs.getString("Teil_Diebstahlrelevant"))
            .build();

    private static final RowMapper<PartUsagePlateDto> PLATE_MAPPER = (rs, rowNum) ->
        PartUsagePlateDto.builder()
            .bildtafelNr(rs.getString("BildtafelNr"))
            .bildtafelArt(rs.getString("BildtafelArt"))
            .benennung(rs.getString("Benennung"))
            .kommentar(rs.getString("Kommentar"))
            .cpVorhanden(rs.getString("CPVorhanden"))
            .bedingungKz(rs.getString("BedingungKZ"))
            .pos(getInteger(rs, "Pos"))
            .marktIso(rs.getString("MarktIso"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Retrieves model series for designation search.
     */
    public List<PartUsageSeriesDto> findModelSeries(
        String searchClause,
        String iso,
        String regiso,
        String katalogumfang,
        List<String> regionen,
        String marke,
        String produktart,
        Map<String, Object> extraParams
    ) {
        String sql = applyClause(RETRIEVE_BAUREIHEN, "__SUCHSTRING__", searchClause);
        Map<String, Object> params = new HashMap<>(Map.of(
            "iso", iso,
            "regiso", regiso,
            "katalogumfang", katalogumfang,
            "regionen", regionen,
            "marke", marke,
            "produktart", produktart
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, SERIES_MAPPER);
    }

    /**
     * Retrieves model series for designation search using special case patterns.
     */
    public List<PartUsageSeriesDto> findModelSeriesSpecialCase(
        String searchClause1,
        String searchClause2,
        String iso,
        String regiso,
        String katalogumfang,
        List<String> regionen,
        String marke,
        String produktart,
        Map<String, Object> extraParams
    ) {
        String sql = applyClauses(RETRIEVE_BAUREIHEN_SONDERLOCKE, Map.of(
            "__SUCHSTRING1__", searchClause1,
            "__SUCHSTRING2__", searchClause2
        ));
        Map<String, Object> params = new HashMap<>(Map.of(
            "iso", iso,
            "regiso", regiso,
            "katalogumfang", katalogumfang,
            "regionen", regionen,
            "marke", marke,
            "produktart", produktart
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, SERIES_MAPPER);
    }

    /**
     * Retrieves model series for Turkish locale search.
     */
    public List<PartUsageSeriesDto> findModelSeriesTurkish(
        String searchClause,
        String iso,
        String regiso,
        String katalogumfang,
        List<String> regionen,
        String marke,
        String produktart,
        Map<String, Object> extraParams
    ) {
        String sql = applyClause(RETRIEVE_BAUREIHEN_TR, "__SUCHSTRING__", searchClause);
        Map<String, Object> params = new HashMap<>(Map.of(
            "iso", iso,
            "regiso", regiso,
            "katalogumfang", katalogumfang,
            "regionen", regionen,
            "marke", marke,
            "produktart", produktart
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, SERIES_MAPPER);
    }

    /**
     * Retrieves model columns for designation search.
     */
    public List<PartUsageModelColumnDto> findModelColumns(
        String searchClause,
        String baureihe,
        String katalogumfang,
        List<String> regionen,
        String marke,
        String produktart,
        String iso,
        String regiso,
        String modellspaltenClause,
        Map<String, Object> extraParams
    ) {
        String sql = applyClauses(RETRIEVE_MODELLSPALTEN, Map.of(
            "__SUCHSTRING__", searchClause,
            "__MODELLSPALTEN__", modellspaltenClause
        ));
        Map<String, Object> params = new HashMap<>(Map.of(
            "iso", iso,
            "regiso", regiso,
            "baureihe", baureihe,
            "katalogumfang", katalogumfang,
            "regionen", regionen,
            "marke", marke,
            "produktart", produktart
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, MODEL_COLUMN_MAPPER);
    }

    /**
     * Retrieves model columns for designation search using special case patterns.
     */
    public List<PartUsageModelColumnDto> findModelColumnsSpecialCase(
        String searchClause1,
        String searchClause2,
        String baureihe,
        String katalogumfang,
        List<String> regionen,
        String marke,
        String produktart,
        String iso,
        String regiso,
        String modellspaltenClause,
        Map<String, Object> extraParams
    ) {
        String sql = applyClauses(RETRIEVE_MODELLSPALTEN_SONDERLOCKE, Map.of(
            "__SUCHSTRING1__", searchClause1,
            "__SUCHSTRING2__", searchClause2,
            "__MODELLSPALTEN__", modellspaltenClause
        ));
        Map<String, Object> params = new HashMap<>(Map.of(
            "iso", iso,
            "regiso", regiso,
            "baureihe", baureihe,
            "katalogumfang", katalogumfang,
            "regionen", regionen,
            "marke", marke,
            "produktart", produktart
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, MODEL_COLUMN_MAPPER);
    }

    /**
     * Retrieves model series for designation search by part numbers.
     */
    public List<PartUsageSeriesDto> findModelSeriesByPartNumbers(
        List<String> sachnummern,
        String katalogumfang,
        List<String> regionen,
        String marke,
        String produktart,
        String iso,
        String regiso
    ) {
        return jdbc.query(RETRIEVE_BAUREIHEN_TNR,
            Map.of(
                "sachnummern", sachnummern,
                "katalogumfang", katalogumfang,
                "regionen", regionen,
                "marke", marke,
                "produktart", produktart,
                "iso", iso,
                "regiso", regiso
            ),
            SERIES_MAPPER);
    }

    /**
     * Retrieves model columns for designation search by part numbers.
     */
    public List<PartUsageModelColumnDto> findModelColumnsByPartNumbers(
        List<String> sachnummern,
        String baureihe,
        String katalogumfang,
        List<String> regionen,
        String marke,
        String produktart,
        String iso,
        String regiso
    ) {
        return jdbc.query(RETRIEVE_MODELLSPALTEN_TNR,
            Map.of(
                "sachnummern", sachnummern,
                "baureihe", baureihe,
                "katalogumfang", katalogumfang,
                "regionen", regionen,
                "marke", marke,
                "produktart", produktart,
                "iso", iso,
                "regiso", regiso
            ),
            MODEL_COLUMN_MAPPER);
    }

    /**
     * Searches parts by designation within usage data.
     */
    public List<PartUsageDesignationLineDto> searchPartsByDesignation(
        String searchClause,
        String iso,
        String regiso,
        String marke,
        String produktart,
        Map<String, Object> extraParams
    ) {
        String sql = applyClause(SEARCH_SNR_TVBENENNUNG, "__SUCHSTRING__", searchClause);
        Map<String, Object> params = new HashMap<>(Map.of(
            "iso", iso,
            "regiso", regiso,
            "marke", marke,
            "produktart", produktart
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, DESIGNATION_LINE_MAPPER);
    }

    /**
     * Searches parts by designation using special case patterns.
     */
    public List<PartUsageDesignationLineDto> searchPartsByDesignationSpecialCase(
        String searchClause1,
        String searchClause2,
        String iso,
        String regiso,
        String marke,
        String produktart,
        Map<String, Object> extraParams
    ) {
        String sql = applyClauses(SEARCH_SNR_TVBENENNUNG_SONDERLOCKE, Map.of(
            "__SUCHSTRING1__", searchClause1,
            "__SUCHSTRING2__", searchClause2
        ));
        Map<String, Object> params = new HashMap<>(Map.of(
            "iso", iso,
            "regiso", regiso,
            "marke", marke,
            "produktart", produktart
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, DESIGNATION_LINE_MAPPER);
    }

    /**
     * Searches illustration plates for the provided part numbers.
     */
    public List<PartUsagePlateDto> searchPlatesByPartNumbers(
        List<String> sachnummern,
        String katalogumfang,
        List<String> regionen,
        String marke,
        String produktart,
        String iso,
        String regiso,
        String unionSql
    ) {
        String sql = appendUnion(SEARCH_BT_SACHNUMMERN_TVBEN, unionSql) + " order by Pos";
        return jdbc.query(sql,
            Map.of(
                "sachnummern", sachnummern,
                "katalogumfang", katalogumfang,
                "regionen", regionen,
                "marke", marke,
                "produktart", produktart,
                "iso", iso,
                "regiso", regiso
            ),
            PLATE_MAPPER);
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

    private static String applyClauses(String sql, Map<String, String> replacements) {
        String result = sql;
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            result = applyClause(result, entry.getKey(), entry.getValue());
        }
        return result;
    }

    private static String appendUnion(String base, String unionSql) {
        if (!StringUtils.hasText(unionSql)) {
            return base;
        }
        return base + " union " + unionSql;
    }
}
