package pl.emdzej.etkx.dal.repository.accessories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesDiagramMarketingDto;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesMarketingGraphicDto;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesMarketingKeywordDto;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesMarketingProductDto;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesMarketingRelatedDto;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesMarketingTextDto;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesVariantMarketingDto;

/**
 * Repository for accessories marketing data.
 */
@Repository
@RequiredArgsConstructor
public class AccessoriesMarketingRepository {
    private static final String FIND_DIAGRAM_MARKETING_LINKS = """
        select bildtafzm_btnr DiagramNumber,
               bildtafzm_produktid ProductId,
               bildtafzm_marktid MarketId
        from w_bildtafzub_marketing
        where bildtafzm_btnr = :btnr
        """;

    private static final String FIND_VARIANT_MARKETING_LINKS = """
        select bildtafzvm_btnr DiagramNumber,
               bildtafzvm_varianteid VariantId,
               bildtafzvm_produktid ProductId,
               bildtafzvm_marktid MarketId
        from w_bildtafzub_var_marketing
        where bildtafzvm_btnr = :btnr
          and bildtafzvm_varianteid = :variantId
        """;

    private static final String FIND_MARKETING_PRODUCTS = """
        select mprhk_produktid ProductId,
               mprhk_herkunft Origin
        from w_marketingprodukt
        where mprhk_produktid in (:productIds)
        """;

    private static final String FIND_MARKETING_TEXTS = """
        select mprod_produktid ProductId,
               mprod_marktid MarketId,
               mprod_spriso LanguageIso,
               mprod_sprregiso LanguageRegionIso,
               mprod_art Type,
               mprod_text Text,
               mprod_text_anfang TextStart,
               mprod_text_laenge TextLength
        from w_marketingprodukt_text
        where mprod_produktid = :productId
          and mprod_marktid = :marketId
          and mprod_spriso = :iso
          and mprod_sprregiso = :regiso
        """;

    private static final String FIND_MARKETING_KEYWORDS = """
        select mkeyw_produktid ProductId,
               mkeyw_marktid MarketId,
               mkeyw_spriso LanguageIso,
               mkeyw_sprregiso LanguageRegionIso,
               mkeyw_keyword Keyword
        from w_marketingprodukt_keyword
        where mkeyw_produktid = :productId
          and mkeyw_marktid = :marketId
          and mkeyw_spriso = :iso
          and mkeyw_sprregiso = :regiso
        """;

    private static final String FIND_MARKETING_GRAPHICS = """
        select mgraf_produktid ProductId,
               mgraf_marktid MarketId,
               mgraf_grafikid GraphicId,
               mgraf_pos Position,
               mgraf_art Type
        from w_marketingprodukt_grafik
        where mgraf_produktid = :productId
          and mgraf_marktid = :marketId
        order by mgraf_pos
        """;

    private static final String FIND_RELATED_PRODUCTS = """
        select mrellink_produktid ProductId,
               mrellink_marktid MarketId,
               mrellink_related_productid RelatedProductId
        from w_marketingprodukt_related
        where mrellink_produktid = :productId
          and mrellink_marktid = :marketId
        """;

    private static final RowMapper<AccessoriesDiagramMarketingDto> DIAGRAM_MARKETING_MAPPER = (rs, rowNum) ->
        AccessoriesDiagramMarketingDto.builder()
            .diagramNumber(rs.getString("DiagramNumber"))
            .productId(getInteger(rs, "ProductId"))
            .marketId(getInteger(rs, "MarketId"))
            .build();

    private static final RowMapper<AccessoriesVariantMarketingDto> VARIANT_MARKETING_MAPPER = (rs, rowNum) ->
        AccessoriesVariantMarketingDto.builder()
            .diagramNumber(rs.getString("DiagramNumber"))
            .variantId(getInteger(rs, "VariantId"))
            .productId(getInteger(rs, "ProductId"))
            .marketId(getInteger(rs, "MarketId"))
            .build();

    private static final RowMapper<AccessoriesMarketingProductDto> MARKETING_PRODUCT_MAPPER = (rs, rowNum) ->
        AccessoriesMarketingProductDto.builder()
            .productId(getInteger(rs, "ProductId"))
            .origin(rs.getString("Origin"))
            .build();

    private static final RowMapper<AccessoriesMarketingTextDto> MARKETING_TEXT_MAPPER = (rs, rowNum) ->
        AccessoriesMarketingTextDto.builder()
            .productId(getInteger(rs, "ProductId"))
            .marketId(getInteger(rs, "MarketId"))
            .languageIso(rs.getString("LanguageIso"))
            .languageRegionIso(rs.getString("LanguageRegionIso"))
            .type(rs.getString("Type"))
            .text(rs.getString("Text"))
            .textStart(rs.getString("TextStart"))
            .textLength(getInteger(rs, "TextLength"))
            .build();

    private static final RowMapper<AccessoriesMarketingKeywordDto> MARKETING_KEYWORD_MAPPER = (rs, rowNum) ->
        AccessoriesMarketingKeywordDto.builder()
            .productId(getInteger(rs, "ProductId"))
            .marketId(getInteger(rs, "MarketId"))
            .languageIso(rs.getString("LanguageIso"))
            .languageRegionIso(rs.getString("LanguageRegionIso"))
            .keyword(rs.getString("Keyword"))
            .build();

    private static final RowMapper<AccessoriesMarketingGraphicDto> MARKETING_GRAPHIC_MAPPER = (rs, rowNum) ->
        AccessoriesMarketingGraphicDto.builder()
            .productId(getInteger(rs, "ProductId"))
            .marketId(getInteger(rs, "MarketId"))
            .graphicId(getInteger(rs, "GraphicId"))
            .position(getInteger(rs, "Position"))
            .type(rs.getString("Type"))
            .build();

    private static final RowMapper<AccessoriesMarketingRelatedDto> MARKETING_RELATED_MAPPER = (rs, rowNum) ->
        AccessoriesMarketingRelatedDto.builder()
            .productId(getInteger(rs, "ProductId"))
            .marketId(getInteger(rs, "MarketId"))
            .relatedProductId(getInteger(rs, "RelatedProductId"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Loads marketing product references for an accessories diagram.
     *
     * @param diagramNumber accessories diagram number
     * @return marketing product references
     */
    public List<AccessoriesDiagramMarketingDto> findDiagramMarketingLinks(String diagramNumber) {
        return jdbc.query(
            FIND_DIAGRAM_MARKETING_LINKS,
            Map.of("btnr", diagramNumber),
            DIAGRAM_MARKETING_MAPPER
        );
    }

    /**
     * Loads marketing product references for a diagram variant.
     *
     * @param diagramNumber accessories diagram number
     * @param variantId variant identifier
     * @return marketing product references
     */
    public List<AccessoriesVariantMarketingDto> findVariantMarketingLinks(String diagramNumber, Integer variantId) {
        return jdbc.query(
            FIND_VARIANT_MARKETING_LINKS,
            Map.of("btnr", diagramNumber, "variantId", variantId),
            VARIANT_MARKETING_MAPPER
        );
    }

    /**
     * Loads marketing products by product identifiers.
     *
     * @param productIds marketing product identifiers
     * @return marketing product metadata
     */
    public List<AccessoriesMarketingProductDto> findMarketingProducts(List<Integer> productIds) {
        return jdbc.query(
            FIND_MARKETING_PRODUCTS,
            Map.of("productIds", productIds),
            MARKETING_PRODUCT_MAPPER
        );
    }

    /**
     * Loads marketing texts for a product in a specific market and language.
     *
     * @param productId product identifier
     * @param marketId market identifier
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return marketing text entries
     */
    public List<AccessoriesMarketingTextDto> findMarketingTexts(
        Integer productId,
        Integer marketId,
        String iso,
        String regiso
    ) {
        return jdbc.query(
            FIND_MARKETING_TEXTS,
            Map.of(
                "productId", productId,
                "marketId", marketId,
                "iso", iso,
                "regiso", regiso
            ),
            MARKETING_TEXT_MAPPER
        );
    }

    /**
     * Loads marketing keywords for a product in a specific market and language.
     *
     * @param productId product identifier
     * @param marketId market identifier
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return marketing keywords
     */
    public List<AccessoriesMarketingKeywordDto> findMarketingKeywords(
        Integer productId,
        Integer marketId,
        String iso,
        String regiso
    ) {
        return jdbc.query(
            FIND_MARKETING_KEYWORDS,
            Map.of(
                "productId", productId,
                "marketId", marketId,
                "iso", iso,
                "regiso", regiso
            ),
            MARKETING_KEYWORD_MAPPER
        );
    }

    /**
     * Loads marketing graphics metadata for a product.
     *
     * @param productId product identifier
     * @param marketId market identifier
     * @return marketing graphics metadata
     */
    public List<AccessoriesMarketingGraphicDto> findMarketingGraphics(Integer productId, Integer marketId) {
        return jdbc.query(
            FIND_MARKETING_GRAPHICS,
            Map.of("productId", productId, "marketId", marketId),
            MARKETING_GRAPHIC_MAPPER
        );
    }

    /**
     * Loads related marketing products for a product.
     *
     * @param productId product identifier
     * @param marketId market identifier
     * @return related marketing products
     */
    public List<AccessoriesMarketingRelatedDto> findRelatedMarketingProducts(Integer productId, Integer marketId) {
        return jdbc.query(
            FIND_RELATED_PRODUCTS,
            Map.of("productId", productId, "marketId", marketId),
            MARKETING_RELATED_MAPPER
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
