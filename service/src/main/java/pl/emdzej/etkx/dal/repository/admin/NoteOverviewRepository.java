package pl.emdzej.etkx.dal.repository.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pl.emdzej.etkx.dal.dto.admin.NoteOverviewDto;

/**
 * Repository for note overview data.
 */
@Repository
@RequiredArgsConstructor
public class NoteOverviewRepository {
    private static final String RETRIEVE_ANZ_NOTIZEN = """
        select count(distinct teileinfo_sachnr) ANZ
        from w_teileinfo@etk_nutzer
        where (teileinfo_user_id = :nutzer or teileinfo_allgemein = 'J')
          and teileinfo_firma_id = :firma
        """;

    private static final String RETRIEVE_ANZ_NOTIZEN_ZU_SACHNUMMER = """
        select count(distinct teileinfo_sachnr) ANZ
        from w_teileinfo@etk_nutzer
        where (teileinfo_user_id = :nutzer or teileinfo_allgemein = 'J')
          and teileinfo_firma_id = :firma
          and teileinfo_sachnr = :sachnummer
        """;

    private static final String RETRIEVE_MIN_HG = """
        select min(teil_hauptgr) HG
        from w_teileinfo@etk_nutzer, w_teil
        where teileinfo_user_id = :nutzer
          and teileinfo_firma_id = :firma
          and teileinfo_sachnr = teil_sachnr
        """;

    private static final String RETRIEVE_NOTIZEN = """
        select teil_hauptgr HG,
            teil_untergrup UG,
            teil_sachnr SACHNR,
            tben.ben_text BENENNUNG,
            mben.ben_text MONAT,
            teileinfo_gueltig_bis_jahr JAHR
        from w_teileinfo@etk_nutzer, w_publben, w_ben_gk mben, w_ben_gk tben, w_teil
        where (teileinfo_user_id = :nutzer or teileinfo_allgemein = 'J')
          and teileinfo_firma_id = :firma
          and teileinfo_sachnr = teil_sachnr
          __HG_STMT__
          and teil_textcode = tben.ben_textcode
          and tben.ben_iso = :iso
          and tben.ben_regiso = :regiso
          and publben_art = 'M'
          and (
              CAST(teileinfo_gueltig_bis_monat AS TEXT) = publben_bezeichnung
              or '0' || CAST(teileinfo_gueltig_bis_monat AS TEXT) = publben_bezeichnung
          )
          and publben_textcode = mben.ben_textcode
          and mben.ben_iso = :iso
          and mben.ben_regiso = :regiso
        order by HG, UG, SACHNR
        """;

    private static final RowMapper<NoteOverviewDto> NOTE_OVERVIEW_MAPPER = (rs, rowNum) ->
        NoteOverviewDto.builder()
            .hg(rs.getString("HG"))
            .ug(rs.getString("UG"))
            .sachnummer(rs.getString("SACHNR"))
            .benennung(rs.getString("BENENNUNG"))
            .monat(rs.getString("MONAT"))
            .jahr(rs.getString("JAHR"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Counts distinct notes for a user.
     *
     * @param nutzer user identifier
     * @param firma company identifier
     * @return count of notes
     */
    public Integer countNotes(String nutzer, String firma) {
        return jdbc.queryForObject(RETRIEVE_ANZ_NOTIZEN, Map.of("nutzer", nutzer, "firma", firma), Integer.class);
    }

    /**
     * Counts distinct notes for a user and part number.
     *
     * @param nutzer user identifier
     * @param firma company identifier
     * @param sachnummer part number
     * @return count of notes
     */
    public Integer countNotesForPart(String nutzer, String firma, String sachnummer) {
        return jdbc.queryForObject(RETRIEVE_ANZ_NOTIZEN_ZU_SACHNUMMER,
            Map.of("nutzer", nutzer, "firma", firma, "sachnummer", sachnummer), Integer.class);
    }

    /**
     * Retrieves the minimum main group for notes.
     *
     * @param nutzer user identifier
     * @param firma company identifier
     * @return minimum main group
     */
    public String loadMinMainGroup(String nutzer, String firma) {
        return jdbc.queryForObject(RETRIEVE_MIN_HG, Map.of("nutzer", nutzer, "firma", firma), String.class);
    }

    /**
     * Loads note overview entries.
     *
     * @param nutzer user identifier
     * @param firma company identifier
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @param hgClause optional main group clause
     * @param extraParams additional parameters for optional clauses
     * @return list of note overview entries
     */
    public List<NoteOverviewDto> loadNotes(
        String nutzer,
        String firma,
        String iso,
        String regiso,
        String hgClause,
        Map<String, Object> extraParams
    ) {
        String sql = applyClauses(RETRIEVE_NOTIZEN, Map.of("__HG_STMT__", hgClause));
        Map<String, Object> params = new HashMap<>(Map.of(
            "nutzer", nutzer,
            "firma", firma,
            "iso", iso,
            "regiso", regiso
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, NOTE_OVERVIEW_MAPPER);
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
