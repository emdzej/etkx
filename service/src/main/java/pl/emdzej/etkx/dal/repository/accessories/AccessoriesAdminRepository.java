package pl.emdzej.etkx.dal.repository.accessories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesDiagramVariantDto;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesLineVariantDto;

/**
 * Repository for accessories administration data (variants and configuration).
 */
@Repository
@RequiredArgsConstructor
public class AccessoriesAdminRepository {
    private static final String FIND_DIAGRAM_VARIANTS = """
        select bildtafzv_btnr DiagramNumber,
               bildtafzv_varianteid VariantId
        from w_bildtafzub_variante
        where bildtafzv_btnr = :btnr
        order by bildtafzv_varianteid
        """;

    private static final String FIND_LINE_VARIANTS = """
        select btzeilenzva_btnr DiagramNumber,
               btzeilenzva_pos Position,
               btzeilenzva_varianteid VariantId
        from w_btzeilenzub_variante
        where btzeilenzva_btnr = :btnr
          and btzeilenzva_pos = :pos
        order by btzeilenzva_varianteid
        """;

    private static final RowMapper<AccessoriesDiagramVariantDto> DIAGRAM_VARIANT_MAPPER = (rs, rowNum) ->
        AccessoriesDiagramVariantDto.builder()
            .diagramNumber(rs.getString("DiagramNumber"))
            .variantId(getInteger(rs, "VariantId"))
            .build();

    private static final RowMapper<AccessoriesLineVariantDto> LINE_VARIANT_MAPPER = (rs, rowNum) ->
        AccessoriesLineVariantDto.builder()
            .diagramNumber(rs.getString("DiagramNumber"))
            .position(getInteger(rs, "Position"))
            .variantId(getInteger(rs, "VariantId"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Loads available variants for a diagram.
     *
     * @param diagramNumber accessories diagram number
     * @return diagram variants
     */
    public List<AccessoriesDiagramVariantDto> findDiagramVariants(String diagramNumber) {
        return jdbc.query(
            FIND_DIAGRAM_VARIANTS,
            Map.of("btnr", diagramNumber),
            DIAGRAM_VARIANT_MAPPER
        );
    }

    /**
     * Loads available variants for a diagram line position.
     *
     * @param diagramNumber accessories diagram number
     * @param position line position
     * @return line variants
     */
    public List<AccessoriesLineVariantDto> findLineVariants(String diagramNumber, Integer position) {
        return jdbc.query(
            FIND_LINE_VARIANTS,
            Map.of("btnr", diagramNumber, "pos", position),
            LINE_VARIANT_MAPPER
        );
    }

    private static Integer getInteger(ResultSet rs, String column) {
        try {
            int value = rs.getInt(column);
            return rs.wasNull() ? null : value;
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
