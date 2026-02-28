package pl.emdzej.etkx.dal.repository.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pl.emdzej.etkx.dal.dto.admin.TechnicalLiteratureEntryDto;
import pl.emdzej.etkx.dal.dto.admin.TechnicalLiteratureGroupDto;
import pl.emdzej.etkx.dal.dto.admin.TechnicalLiteratureLanguageDto;

/**
 * Repository for technical literature data.
 */
@Repository
@RequiredArgsConstructor
public class TechnicalLiteratureRepository {
    private static final String RETRIEVE_FGS = """
        select hgfg_hg Hauptgruppe,
            hgfg_fg Funktionsgruppe,
            ben_text Benennung
        from w_hgfg_mosp, w_hgfg, w_ben_gk
        where hgfgm_mospid = :mosp
          and hgfgm_hg = '01'
          and hgfgm_hg = hgfg_hg
          and hgfgm_fg = hgfg_fg
          and hgfgm_produktart = hgfg_produktart
          and hgfgm_bereich = hgfg_bereich
          and hgfg_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        union
        select hgfg_hg Hauptgruppe,
            hgfg_fg Funktionsgruppe,
            ben_text Benennung
        from w_bildtaf, w_hgfg, w_ben_gk, w_btzeilenugb_verbauung
        where bildtaf_bteart = 'U'
          and bildtaf_hg = '01'
          and bildtaf_produktart = :produktart
          and bildtaf_vbereich in ('BE', :katalogumfang)
          and bildtaf_btnr = btzeilenuv_btnr
          and btzeilenuv_marke_tps = :marke
          and bildtaf_hg = hgfg_hg
          and bildtaf_fg = hgfg_fg
          and bildtaf_produktart = hgfg_produktart
          and hgfg_bereich = 'KONZERN'
          and hgfg_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by 1, 2
        """;

    private static final String RETRIEVE_SPRACHEN = """
        select substr(publben_bezeichnung, 1, 2) SpracheISO,
            substr(publben_bezeichnung, 3, 2) SpracheRegISO,
            ben_text Benennung
        from w_publben, w_ben_gk
        where publben_art = 'T'
          and publben_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by Benennung
        """;

    private static final String LOAD_TECHNISCHE_LITERATUR = """
        select teil_hauptgr HG,
            teil_untergrup UG,
            teil_sachnr Sachnummer,
            teil_ist_eba istEBA,
            benennung.ben_text Benennung,
            kommentar.ben_text Kommentar,
            teil_benennzus Zusatz,
            btzeilen_eins Einsatz,
            btzeilen_auslf Auslauf,
            teil_mam MAM,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_bildtaf
        inner join w_btzeilen on (bildtaf_btnr = btzeilen_btnr and btzeilen_bildposnr <> '--')
        inner join w_teil on (btzeilen_sachnr = teil_sachnr)
        inner join w_ben_gk benennung on (
            teil_textcode = benennung.ben_textcode
            and benennung.ben_iso = :iso
            and benennung.ben_regiso = :regiso
        )
        left join w_ben_gk kommentar on (
            teil_textcode_kom = kommentar.ben_textcode
            and kommentar.ben_iso = :iso
            and kommentar.ben_regiso = :regiso
        )
        inner join w_btzeilen_verbauung on (
            btzeilen_btnr = btzeilenv_btnr
            and btzeilen_pos = btzeilenv_pos
            and btzeilenv_mospid = :mosp
        )
        where bildtaf_hg = '01'
          and bildtaf_fg = :fg
          and bildtaf_bteart = 'G'
          and bildtaf_produktart = :produktart
          and bildtaf_vbereich in ('BE', :katalogumfang)
          __TLSPRACHE_STMT_GEB__
        union
        select teil_hauptgr HG,
            teil_untergrup UG,
            teil_sachnr Sachnummer,
            teil_ist_eba istEBA,
            benennung.ben_text Benennung,
            kommentar.ben_text Kommentar,
            teil_benennzus Zusatz,
            btzeilenu_eins Einsatz,
            btzeilenu_ausl Auslauf,
            teil_mam MAM,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_bildtaf
        inner join w_btzeilenugb on (bildtaf_btnr = btzeilenu_btnr and btzeilenu_bildposnr <> '--')
        inner join w_teil on (btzeilenu_sachnr = teil_sachnr)
        inner join w_ben_gk benennung on (
            teil_textcode = benennung.ben_textcode
            and benennung.ben_iso = :iso
            and benennung.ben_regiso = :regiso
        )
        left join w_ben_gk kommentar on (
            teil_textcode_kom = kommentar.ben_textcode
            and kommentar.ben_iso = :iso
            and kommentar.ben_regiso = :regiso
        )
        inner join w_btzeilenugb_verbauung on (
            btzeilenu_btnr = btzeilenuv_btnr
            and btzeilenu_pos = btzeilenuv_pos
            and btzeilenuv_marke_tps = :marke
        )
        where bildtaf_hg = '01'
          and bildtaf_fg = :fg
          and bildtaf_bteart = 'U'
          and bildtaf_produktart = :produktart
          and bildtaf_vbereich in ('BE', :katalogumfang)
          __TLSPRACHE_STMT_UGB__
        order by Benennung asc, Kommentar asc, Einsatz desc
        """;

    private static final String TL_SPRACHE_STMT_GEB = """
        and btzeilen_bildposnr = (
            select tlsb_bildnummer
            from w_tl_sprache_bnb
            where tlsb_iso = :isoTl
              and tlsb_regiso = :regisoTl
        )
        """;

    private static final String TL_SPRACHE_STMT_UGB = """
        and btzeilenu_bildposnr = (
            select tlsb_bildnummer
            from w_tl_sprache_bnb
            where tlsb_iso = :isoTl
              and tlsb_regiso = :regisoTl
        )
        """;

    private static final RowMapper<TechnicalLiteratureGroupDto> GROUP_MAPPER = (rs, rowNum) ->
        TechnicalLiteratureGroupDto.builder()
            .hauptgruppe(rs.getString("Hauptgruppe"))
            .funktionsgruppe(rs.getString("Funktionsgruppe"))
            .benennung(rs.getString("Benennung"))
            .build();

    private static final RowMapper<TechnicalLiteratureLanguageDto> LANGUAGE_MAPPER = (rs, rowNum) ->
        TechnicalLiteratureLanguageDto.builder()
            .spracheIso(rs.getString("SpracheISO"))
            .spracheRegiso(rs.getString("SpracheRegISO"))
            .benennung(rs.getString("Benennung"))
            .build();

    private static final RowMapper<TechnicalLiteratureEntryDto> ENTRY_MAPPER = (rs, rowNum) ->
        TechnicalLiteratureEntryDto.builder()
            .hg(rs.getString("HG"))
            .ug(rs.getString("UG"))
            .sachnummer(rs.getString("Sachnummer"))
            .istEba(rs.getString("istEBA"))
            .benennung(rs.getString("Benennung"))
            .kommentar(rs.getString("Kommentar"))
            .zusatz(rs.getString("Zusatz"))
            .einsatz(rs.getString("Einsatz"))
            .auslauf(rs.getString("Auslauf"))
            .mam(rs.getString("MAM"))
            .teilDiebstahlrelevant(rs.getString("Teil_Diebstahlrelevant"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Loads technical literature functional groups.
     *
     * @param mosp MOSP identifier
     * @param produktart product type
     * @param katalogumfang catalog scope
     * @param marke brand code
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return list of functional groups
     */
    public List<TechnicalLiteratureGroupDto> loadFunctionalGroups(
        String mosp,
        String produktart,
        String katalogumfang,
        String marke,
        String iso,
        String regiso
    ) {
        return jdbc.query(RETRIEVE_FGS, Map.of(
            "mosp", mosp,
            "produktart", produktart,
            "katalogumfang", katalogumfang,
            "marke", marke,
            "iso", iso,
            "regiso", regiso
        ), GROUP_MAPPER);
    }

    /**
     * Loads available technical literature languages.
     *
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return list of language entries
     */
    public List<TechnicalLiteratureLanguageDto> loadLanguages(String iso, String regiso) {
        return jdbc.query(RETRIEVE_SPRACHEN, Map.of("iso", iso, "regiso", regiso), LANGUAGE_MAPPER);
    }

    /**
     * Loads technical literature entries.
     *
     * @param fg function group
     * @param produktart product type
     * @param katalogumfang catalog scope
     * @param marke brand code
     * @param mosp MOSP identifier
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @param isoTl optional technical literature ISO code
     * @param regisoTl optional technical literature regional ISO code
     * @return list of literature entries
     */
    public List<TechnicalLiteratureEntryDto> loadTechnicalLiterature(
        String fg,
        String produktart,
        String katalogumfang,
        String marke,
        String mosp,
        String iso,
        String regiso,
        String isoTl,
        String regisoTl
    ) {
        String tlClauseGeb = StringUtils.hasText(isoTl) && StringUtils.hasText(regisoTl) ? TL_SPRACHE_STMT_GEB : null;
        String tlClauseUgb = StringUtils.hasText(isoTl) && StringUtils.hasText(regisoTl) ? TL_SPRACHE_STMT_UGB : null;
        String sql = applyClauses(LOAD_TECHNISCHE_LITERATUR, Map.of(
            "__TLSPRACHE_STMT_GEB__", tlClauseGeb,
            "__TLSPRACHE_STMT_UGB__", tlClauseUgb
        ));
        Map<String, Object> params = new HashMap<>(Map.of(
            "fg", fg,
            "produktart", produktart,
            "katalogumfang", katalogumfang,
            "marke", marke,
            "mosp", mosp,
            "iso", iso,
            "regiso", regiso
        ));
        if (StringUtils.hasText(isoTl) && StringUtils.hasText(regisoTl)) {
            params.put("isoTl", isoTl);
            params.put("regisoTl", regisoTl);
        }
        return jdbc.query(sql, params, ENTRY_MAPPER);
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
