package pl.emdzej.etkx.dal.repository.diagram;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.emdzej.etkx.dal.dto.diagram.DiagramCommentDto;
import pl.emdzej.etkx.dal.dto.diagram.DiagramTitleDto;

/**
 * Repository for diagram metadata operations (BTEInfo).
 */
@Repository
@RequiredArgsConstructor
public class DiagramInfoRepository {
    private static final String RETRIEVE_BTEINFO = """
        select ben.ben_text Ueberschrift
        from w_bildtaf, w_ben_gk ben
        where bildtaf_btnr = :bteNr
          and bildtaf_produktart = :produktart
          and bildtaf_textc = ben.ben_textcode
          and ben.ben_iso = :iso
          and ben.ben_regiso = :regiso
        """;

    private static final String RETRIEVE_BTEKOMMENTAR = """
        select distinct komm_id KommId,
            ben_text Text,
            komm_code Code,
            komm_vz VZ,
            komm_darstellung Darstellung,
            komm_tiefe Tiefe,
            komm_pos Pos
        from w_bildtaf, w_ben_gk, w_komm
        where bildtaf_btnr = :btnr
          and bildtaf_kommbt = komm_id
          and komm_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by KommId, Pos
        """;

    private static final RowMapper<DiagramTitleDto> TITLE_MAPPER = (rs, rowNum) ->
        DiagramTitleDto.builder()
            .ueberschrift(rs.getString("Ueberschrift"))
            .build();

    private static final RowMapper<DiagramCommentDto> COMMENT_MAPPER = (rs, rowNum) ->
        DiagramCommentDto.builder()
            .kommId(rs.getString("KommId"))
            .text(rs.getString("Text"))
            .code(rs.getString("Code"))
            .vz(rs.getString("VZ"))
            .darstellung(rs.getString("Darstellung"))
            .tiefe(rs.getString("Tiefe"))
            .pos(rs.getString("Pos"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Retrieves the title for a diagram plate.
     */
    public Optional<DiagramTitleDto> findDiagramTitle(String bteNr, String produktart, String iso, String regiso) {
        return jdbc.query(RETRIEVE_BTEINFO, Map.of(
            "bteNr", bteNr,
            "produktart", produktart,
            "iso", iso,
            "regiso", regiso
        ), TITLE_MAPPER).stream().findFirst();
    }

    /**
     * Retrieves comments attached to a diagram plate.
     */
    public List<DiagramCommentDto> findDiagramComments(String btnr, String iso, String regiso) {
        return jdbc.query(RETRIEVE_BTEKOMMENTAR, Map.of(
            "btnr", btnr,
            "iso", iso,
            "regiso", regiso
        ), COMMENT_MAPPER);
    }
}
