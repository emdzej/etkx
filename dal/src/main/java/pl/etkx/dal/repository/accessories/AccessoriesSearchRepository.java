package pl.etkx.dal.repository.accessories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.etkx.dal.dto.accessories.AccessoriesDiagramDto;
import pl.etkx.dal.dto.accessories.AccessoriesDiagramSeriesDto;
import pl.etkx.dal.dto.accessories.AccessoriesLineItemDto;

/**
 * Repository for accessory search data (diagrams and line items).
 */
@Repository
@RequiredArgsConstructor
public class AccessoriesSearchRepository {
    private static final String FIND_DIAGRAM_BY_NUMBER = """
        select bildtafz_btnr DiagramNumber,
               bildtafz_salaid SaleId,
               bildtafz_bedkez ConditionCode,
               bildtafz_ipac IpacFlag,
               bildtafz_sa2 Sa2Flag,
               bildtafz_sa3 Sa3Flag,
               bildtafz_zusatz_kommid AdditionalCommentId
        from w_bildtafzub
        where bildtafz_btnr = :btnr
        """;

    private static final String FIND_DIAGRAMS_BY_SERIES = """
        select bildtafzb_btnr DiagramNumber,
               bildtafzb_baureihe Series,
               bildtafzb_ktlgausf CatalogVariant
        from w_bildtafzub_baureihe
        where bildtafzb_baureihe = :series
          and bildtafzb_ktlgausf = :catalogVariant
        order by bildtafzb_btnr
        """;

    private static final String FIND_LINE_ITEMS_BY_DIAGRAM = """
        select btzeilenz_btnr DiagramNumber,
               btzeilenz_pos Position,
               btzeilenz_gehzupos GroupPosition,
               btzeilenz_bildposnr ImagePosition,
               btzeilenz_art Type,
               btzeilenz_verw AdministrationFlag,
               btzeilenz_sachnr PartNumber,
               btzeilenz_elementart ElementType,
               btzeilenz_buendelung Bundling,
               btzeilenz_menge_min QuantityMin,
               btzeilenz_menge_max QuantityMax,
               btzeilenz_kat Category,
               btzeilenz_automatik Automatic,
               btzeilenz_lenkg Steering,
               btzeilenz_eins ValidFrom,
               btzeilenz_auslf ValidTo,
               btzeilenz_bedkez ConditionCode,
               btzeilenz_regelnr RuleNumber,
               btzeilenz_gruppeid GroupId
        from w_btzeilenzub
        where btzeilenz_btnr = :btnr
        order by btzeilenz_pos
        """;

    private static final RowMapper<AccessoriesDiagramDto> DIAGRAM_MAPPER = (rs, rowNum) ->
        AccessoriesDiagramDto.builder()
            .diagramNumber(rs.getString("DiagramNumber"))
            .saleId(getInteger(rs, "SaleId"))
            .conditionCode(rs.getString("ConditionCode"))
            .ipacFlag(rs.getString("IpacFlag"))
            .sa2Flag(rs.getString("Sa2Flag"))
            .sa3Flag(rs.getString("Sa3Flag"))
            .additionalCommentId(getInteger(rs, "AdditionalCommentId"))
            .build();

    private static final RowMapper<AccessoriesDiagramSeriesDto> DIAGRAM_SERIES_MAPPER = (rs, rowNum) ->
        AccessoriesDiagramSeriesDto.builder()
            .diagramNumber(rs.getString("DiagramNumber"))
            .series(rs.getString("Series"))
            .catalogVariant(rs.getString("CatalogVariant"))
            .build();

    private static final RowMapper<AccessoriesLineItemDto> LINE_ITEM_MAPPER = (rs, rowNum) ->
        AccessoriesLineItemDto.builder()
            .diagramNumber(rs.getString("DiagramNumber"))
            .position(getInteger(rs, "Position"))
            .groupPosition(getInteger(rs, "GroupPosition"))
            .imagePosition(rs.getString("ImagePosition"))
            .type(rs.getString("Type"))
            .administrationFlag(getInteger(rs, "AdministrationFlag"))
            .partNumber(rs.getString("PartNumber"))
            .elementType(rs.getString("ElementType"))
            .bundling(rs.getString("Bundling"))
            .quantityMin(rs.getString("QuantityMin"))
            .quantityMax(rs.getString("QuantityMax"))
            .category(rs.getString("Category"))
            .automatic(rs.getString("Automatic"))
            .steering(rs.getString("Steering"))
            .validFrom(getInteger(rs, "ValidFrom"))
            .validTo(getInteger(rs, "ValidTo"))
            .conditionCode(rs.getString("ConditionCode"))
            .ruleNumber(getInteger(rs, "RuleNumber"))
            .groupId(getInteger(rs, "GroupId"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Loads a single accessories diagram entry.
     *
     * @param diagramNumber accessories diagram number
     * @return list with diagram data (empty if not found)
     */
    public List<AccessoriesDiagramDto> findDiagramByNumber(String diagramNumber) {
        return jdbc.query(
            FIND_DIAGRAM_BY_NUMBER,
            Map.of("btnr", diagramNumber),
            DIAGRAM_MAPPER
        );
    }

    /**
     * Loads diagram numbers for a given series and catalog variant.
     *
     * @param series vehicle series
     * @param catalogVariant catalog variant identifier
     * @return matching diagram numbers
     */
    public List<AccessoriesDiagramSeriesDto> findDiagramsBySeries(String series, String catalogVariant) {
        return jdbc.query(
            FIND_DIAGRAMS_BY_SERIES,
            Map.of("series", series, "catalogVariant", catalogVariant),
            DIAGRAM_SERIES_MAPPER
        );
    }

    /**
     * Loads accessories line items for a diagram.
     *
     * @param diagramNumber accessories diagram number
     * @return line items ordered by position
     */
    public List<AccessoriesLineItemDto> findLineItemsByDiagram(String diagramNumber) {
        return jdbc.query(
            FIND_LINE_ITEMS_BY_DIAGRAM,
            Map.of("btnr", diagramNumber),
            LINE_ITEM_MAPPER
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
