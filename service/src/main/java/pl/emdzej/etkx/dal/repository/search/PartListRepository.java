package pl.emdzej.etkx.dal.repository.search;

import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.emdzej.etkx.dal.dto.search.PartDetailsDto;

/**
 * Repository for part list operations.
 */
@Repository
@RequiredArgsConstructor
public class PartListRepository {
    private static final String RETRIEVE_PART = """
        select teil_hauptgr Hg, teil_untergrup Ug, ben_text Benennung, teil_benennzus Zusatz, teilm_marke_tps Marke,
            teil_art Teileart, teil_produktkl ProduktKlasse, teil_mam MAM, teil_mengeeinh Mengeneinheit,
            COALESCE(teil_vorverpac, 0) VVM, COALESCE(teil_lagerverp, 0) LVM, COALESCE(teil_beh_verp, 0) BVM,
            teil_fertigungshinweis FH, teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_teil, w_teil_marken, w_ben_gk
        where teil_sachnr = :sachnr
          and teil_textcode = ben_textcode
          and ben_regiso = :regiso
          and ben_iso = :iso
          and teil_sachnr = teilm_sachnr
        """;

    private static final RowMapper<PartDetailsDto> PART_DETAILS_MAPPER = (rs, rowNum) ->
        PartDetailsDto.builder()
            .hg(rs.getString("Hg"))
            .ug(rs.getString("Ug"))
            .benennung(rs.getString("Benennung"))
            .zusatz(rs.getString("Zusatz"))
            .marke(rs.getString("Marke"))
            .teileart(rs.getString("Teileart"))
            .produktKlasse(rs.getString("ProduktKlasse"))
            .mam(rs.getString("MAM"))
            .mengeneinheit(rs.getString("Mengeneinheit"))
            .vvm(getInteger(rs, "VVM"))
            .lvm(getInteger(rs, "LVM"))
            .bvm(getInteger(rs, "BVM"))
            .fh(rs.getString("FH"))
            .teilDiebstahlrelevant(rs.getString("Teil_Diebstahlrelevant"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Retrieves part details for a given part number.
     *
     * @param sachnr part number
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return optional part details
     */
    public Optional<PartDetailsDto> findPartDetails(String sachnr, String iso, String regiso) {
        return jdbc.query(RETRIEVE_PART, Map.of("sachnr", sachnr, "iso", iso, "regiso", regiso), PART_DETAILS_MAPPER)
            .stream()
            .findFirst();
    }

    private static Integer getInteger(java.sql.ResultSet rs, String column) {
        try {
            int value = rs.getInt(column);
            return rs.wasNull() ? null : value;
        } catch (java.sql.SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
