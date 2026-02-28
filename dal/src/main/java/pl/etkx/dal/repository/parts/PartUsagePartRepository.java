package pl.etkx.dal.repository.parts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pl.etkx.dal.dto.parts.PartUsageModelColumnDto;
import pl.etkx.dal.dto.parts.PartUsagePartSummaryDto;
import pl.etkx.dal.dto.parts.PartUsageSeriesDto;

/**
 * Repository for part usage by part number (TeilevwdgTeil).
 */
@Repository
@RequiredArgsConstructor
public class PartUsagePartRepository {
    private static final String RETRIEVE_TEIL_ZU_MARKE_PROD = """
        select teil_hauptgr HG,
            teil_untergrup UG,
            :marke MARKE,
            :produktart PRODUKTART,
            ben_text BEN,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_teil, w_teil_marken, w_ben_gk
        where teil_sachnr = :sachnummer
          and (teil_produktart = :produktart or teil_produktart = 'B' or teil_produktart is null)
          and teilm_sachnr = teil_sachnr
          and teilm_marke_tps = :marke
          and teil_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        """;

    private static final String RETRIEVE_TEIL = """
        select teil_hauptgr HG,
            teil_untergrup UG,
            teilm_marke_tps MARKE,
            teil_produktart PRODUKTART,
            ben_text BEN,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_teil, w_teil_marken, w_ben_gk
        where teil_sachnr = :sachnummer
          and teil_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
          and teil_sachnr = teilm_sachnr
        """;

    private static final String RETRIEVE_BAUREIHEN = """
        select distinct baureihe_baureihe BAUREIHE, ben_text EXT_BAUREIHE, baureihe_position POS
        from w_btzeilen_verbauung, w_btzeilen, w_ben_gk, w_baureihe, w_fztyp
        where btzeilenv_sachnr = :sachnummer
          __MODELLSPALTEN__
          and btzeilen_btnr = btzeilenv_btnr
          and btzeilen_pos = btzeilenv_pos
          and btzeilen_bildposnr <> '--'
          and btzeilenv_mospid = fztyp_mospid
          and fztyp_vbereich = :katalogumfang
          and fztyp_sichtschutz = 'N'
          and fztyp_ktlgausf IN (:regionen)
          and fztyp_baureihe = baureihe_baureihe
          and baureihe_marke_tps = :marke
          and baureihe_produktart = :produktart
          and baureihe_vbereich in ('BE', :katalogumfang)
          and baureihe_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
          and btzeilenv_alter_kz is null
        order by POS
        """;

    private static final String RETRIEVE_MODELLSPALTEN = """
        select distinct btzeilenv_vmenge MENGE,
            btzeilenv_btnr BTNR,
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
        from w_fztyp, w_ben_gk B, w_ben_gk K, w_publben, w_baureihe, w_btzeilen, w_btzeilen_verbauung, w_bildtaf
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where fztyp_baureihe = :baureihe
          and fztyp_vbereich = :katalogumfang
          and fztyp_sichtschutz = 'N'
          and fztyp_ktlgausf IN (:regionen)
          and fztyp_mospid = btzeilenv_mospid
          __MODELLSPALTEN__
          and btzeilenv_sachnr = :sachnummer
          and btzeilen_btnr = btzeilenv_btnr
          and btzeilen_pos = btzeilenv_pos
          and btzeilen_bildposnr <> '--'
          and btzeilenv_btnr = bildtaf_btnr
          and fztyp_baureihe = baureihe_baureihe
          and fztyp_karosserie = publben_bezeichnung
          and publben_art = 'K'
          and publben_textcode = K.ben_textcode
          and K.ben_iso = :iso
          and K.ben_regiso = :regiso
          and bildtaf_textc = B.ben_textcode
          and B.ben_iso = :iso
          and B.ben_regiso = :regiso
          and btzeilenv_alter_kz is null
        order by MODELL, BTNR, KAROSSERIE, REGION
        """;

    private static final RowMapper<PartUsagePartSummaryDto> PART_SUMMARY_MAPPER = (rs, rowNum) ->
        PartUsagePartSummaryDto.builder()
            .hg(rs.getString("HG"))
            .ug(rs.getString("UG"))
            .marke(rs.getString("MARKE"))
            .produktart(rs.getString("PRODUKTART"))
            .ben(rs.getString("BEN"))
            .teilDiebstahlrelevant(rs.getString("Teil_Diebstahlrelevant"))
            .build();

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

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Retrieves part details scoped to brand and product type.
     */
    public List<PartUsagePartSummaryDto> findPartForBrandAndProduct(
        String sachnummer,
        String produktart,
        String marke,
        String iso,
        String regiso
    ) {
        return jdbc.query(RETRIEVE_TEIL_ZU_MARKE_PROD,
            Map.of("sachnummer", sachnummer, "produktart", produktart, "marke", marke, "iso", iso, "regiso", regiso),
            PART_SUMMARY_MAPPER);
    }

    /**
     * Retrieves part details with brand and product information.
     */
    public List<PartUsagePartSummaryDto> findPart(String sachnummer, String iso, String regiso) {
        return jdbc.query(RETRIEVE_TEIL, Map.of("sachnummer", sachnummer, "iso", iso, "regiso", regiso),
            PART_SUMMARY_MAPPER);
    }

    /**
     * Retrieves model series for a part number.
     */
    public List<PartUsageSeriesDto> findModelSeries(
        String sachnummer,
        String katalogumfang,
        List<String> regionen,
        String marke,
        String produktart,
        String iso,
        String regiso,
        String modellspaltenClause,
        Map<String, Object> extraParams
    ) {
        String sql = applyClause(RETRIEVE_BAUREIHEN, "__MODELLSPALTEN__", modellspaltenClause);
        Map<String, Object> params = new HashMap<>(Map.of(
            "sachnummer", sachnummer,
            "katalogumfang", katalogumfang,
            "regionen", regionen,
            "marke", marke,
            "produktart", produktart,
            "iso", iso,
            "regiso", regiso
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, SERIES_MAPPER);
    }

    /**
     * Retrieves model columns for a part number within a model series.
     */
    public List<PartUsageModelColumnDto> findModelColumns(
        String baureihe,
        String katalogumfang,
        List<String> regionen,
        String sachnummer,
        String iso,
        String regiso,
        String modellspaltenClause,
        Map<String, Object> extraParams
    ) {
        String sql = applyClause(RETRIEVE_MODELLSPALTEN, "__MODELLSPALTEN__", modellspaltenClause);
        Map<String, Object> params = new HashMap<>(Map.of(
            "baureihe", baureihe,
            "katalogumfang", katalogumfang,
            "regionen", regionen,
            "sachnummer", sachnummer,
            "iso", iso,
            "regiso", regiso
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, MODEL_COLUMN_MAPPER);
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
