package pl.etkx.dal.repository.reference;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.etkx.dal.dto.reference.UpholsteryCodeDto;

/**
 * Repository for upholstery code reference data.
 */
@Repository
@RequiredArgsConstructor
public class UpholsteryCodeRepository {
    private static final String LOAD_POLSTERCODE = """
        select bedaflpc_art Art,
            bedaflpc_code Code,
            ben_text Benennung,
            bedaflpc_pcode PCode,
            bedaflpc_gilt_v GueltigVon,
            bedaflpc_gilt_b GueltigBis,
            bedaflpc_pos Pos
        from w_bed_aflpc, w_ben_gk
        where ben_textcode = bedaflpc_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by Pos
        """;

    private static final RowMapper<UpholsteryCodeDto> UPHOLSTERY_CODE_MAPPER = (rs, rowNum) ->
        UpholsteryCodeDto.builder()
            .art(rs.getString("Art"))
            .code(rs.getString("Code"))
            .benennung(rs.getString("Benennung"))
            .pcode(rs.getString("PCode"))
            .gueltigVon(rs.getString("GueltigVon"))
            .gueltigBis(rs.getString("GueltigBis"))
            .pos(getInteger(rs, "Pos"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Loads upholstery codes for a given language.
     *
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return list of upholstery codes
     */
    public List<UpholsteryCodeDto> loadUpholsteryCodes(String iso, String regiso) {
        return jdbc.query(LOAD_POLSTERCODE, Map.of("iso", iso, "regiso", regiso), UPHOLSTERY_CODE_MAPPER);
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
