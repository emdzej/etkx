package pl.etkx.dal.repository.admin;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.etkx.dal.dto.admin.AbbreviationDto;
import pl.etkx.dal.dto.admin.MeaningDto;
import pl.etkx.dal.dto.admin.SalaCodeDto;
import pl.etkx.dal.dto.admin.VersionInfoDto;

/**
 * Repository for help system content.
 */
@Repository
@RequiredArgsConstructor
public class HelpRepository {
    private static final String RETRIEVE_ABKUERZUNGEN = """
        select abk_kuerzel ABKUERZUNG,
            abk_bedeutung BEDEUTUNG,
            ben_text UEBERSETZUNG
        from w_abk, w_ben_gk
        where abk_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by ABKUERZUNG
        """;

    private static final String RETRIEVE_VERSIONSINFO = """
        select verwaltung_info Info,
            verwaltung_wert Wert
        from w_verwaltung
        """;

    private static final String RETRIEVE_BEDEUTUNGEN = """
        select bedeutung_wert WERT,
            ben_text BEDEUTUNG,
            komm_pos POS
        from w_bedeutung, w_ben_gk, w_komm
        where bedeutung_art = :art
          and bedeutung_kommid = komm_id
          and komm_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by WERT, POS
        """;

    private static final String RETRIEVE_SALAPAS = """
        select bedsala_art || bedsala_pnr || bedsala_hz Code,
            ben_text Benennung
        from w_bed_sala, w_ben_gk, w_bed
        where bedsala_art in (:arten)
          and bedsala_id = bed_elemid
          and bed_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by Code
        """;

    private static final RowMapper<AbbreviationDto> ABBREVIATION_MAPPER = (rs, rowNum) ->
        AbbreviationDto.builder()
            .abkuerzung(rs.getString("ABKUERZUNG"))
            .bedeutung(rs.getString("BEDEUTUNG"))
            .uebersetzung(rs.getString("UEBERSETZUNG"))
            .build();

    private static final RowMapper<VersionInfoDto> VERSION_INFO_MAPPER = (rs, rowNum) ->
        VersionInfoDto.builder()
            .info(rs.getString("Info"))
            .wert(rs.getString("Wert"))
            .build();

    private static final RowMapper<MeaningDto> MEANING_MAPPER = (rs, rowNum) ->
        MeaningDto.builder()
            .wert(rs.getString("WERT"))
            .bedeutung(rs.getString("BEDEUTUNG"))
            .pos(rs.getString("POS"))
            .build();

    private static final RowMapper<SalaCodeDto> SALA_MAPPER = (rs, rowNum) ->
        SalaCodeDto.builder()
            .code(rs.getString("Code"))
            .benennung(rs.getString("Benennung"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Loads abbreviation entries for a language.
     *
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return list of abbreviations
     */
    public List<AbbreviationDto> loadAbbreviations(String iso, String regiso) {
        return jdbc.query(RETRIEVE_ABKUERZUNGEN, Map.of("iso", iso, "regiso", regiso), ABBREVIATION_MAPPER);
    }

    /**
     * Loads version information from the database.
     *
     * @return list of version info entries
     */
    public List<VersionInfoDto> loadVersionInfo() {
        return jdbc.query(RETRIEVE_VERSIONSINFO, VERSION_INFO_MAPPER);
    }

    /**
     * Loads meanings for the given code type.
     *
     * @param art meaning type
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return list of meaning entries
     */
    public List<MeaningDto> loadMeanings(String art, String iso, String regiso) {
        return jdbc.query(RETRIEVE_BEDEUTUNGEN, Map.of("art", art, "iso", iso, "regiso", regiso), MEANING_MAPPER);
    }

    /**
     * Loads SALA codes for the given language and types.
     *
     * @param arten list of SALA types
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return list of SALA code entries
     */
    public List<SalaCodeDto> loadSalaCodes(List<String> arten, String iso, String regiso) {
        return jdbc.query(RETRIEVE_SALAPAS, Map.of("arten", arten, "iso", iso, "regiso", regiso), SALA_MAPPER);
    }
}
