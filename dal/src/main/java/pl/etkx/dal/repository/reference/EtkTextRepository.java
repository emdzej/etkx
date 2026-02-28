package pl.etkx.dal.repository.reference;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.etkx.dal.dto.reference.EtkTextCommentDto;
import pl.etkx.dal.dto.reference.EtkTextEntryDto;

/**
 * Repository for ETK text reference data.
 */
@Repository
@RequiredArgsConstructor
public class EtkTextRepository {
    private static final String RETRIEVE_ETKTEXTE = """
        select bedetkt_elemid ElemId,
            bedetkt_hg HG,
            bedetkt_fg FG,
            bedetkt_produktart Produktart,
            bedetkt_kommid KommId
        from w_bed_etktext
        order by Produktart, HG, FG
        """;

    private static final String RETRIEVE_ETKTEXTE_KOMMENTARE = """
        select distinct bedetkt_kommid KommId,
            ben_text Text,
            komm_pos KommPos
        from w_bed_etktext, w_komm, w_ben_gk
        where komm_id = bedetkt_kommid
          and ben_textcode = komm_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by KommId, KommPos
        """;

    private static final RowMapper<EtkTextEntryDto> ETK_TEXT_ENTRY_MAPPER = (rs, rowNum) ->
        EtkTextEntryDto.builder()
            .elemId(rs.getString("ElemId"))
            .hg(rs.getString("HG"))
            .fg(rs.getString("FG"))
            .produktart(rs.getString("Produktart"))
            .kommId(rs.getString("KommId"))
            .build();

    private static final RowMapper<EtkTextCommentDto> ETK_TEXT_COMMENT_MAPPER = (rs, rowNum) ->
        EtkTextCommentDto.builder()
            .kommId(rs.getString("KommId"))
            .text(rs.getString("Text"))
            .kommPos(getInteger(rs, "KommPos"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Loads all ETK text entries.
     *
     * @return list of ETK text entries
     */
    public List<EtkTextEntryDto> loadEtkTexts() {
        return jdbc.query(RETRIEVE_ETKTEXTE, Map.of(), ETK_TEXT_ENTRY_MAPPER);
    }

    /**
     * Loads ETK text comments for a specific language.
     *
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return list of ETK text comments
     */
    public List<EtkTextCommentDto> loadEtkTextComments(String iso, String regiso) {
        return jdbc.query(RETRIEVE_ETKTEXTE_KOMMENTARE, Map.of("iso", iso, "regiso", regiso),
            ETK_TEXT_COMMENT_MAPPER);
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
