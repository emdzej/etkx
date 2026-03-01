package pl.emdzej.etkx.dal.repository.search;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.emdzej.etkx.dal.dto.search.MarketDescriptionDto;
import pl.emdzej.etkx.dal.dto.search.PartByNumberDto;

/**
 * Repository for general part search operations.
 */
@Repository
@RequiredArgsConstructor
public class PartSearchGeneralRepository {
    private static final String LOAD_MARKET_DESCRIPTION = """
        select lj11.ben_text MarktBen
        from w_bildtaf t1
        left join w_markt_etk lj1 on (lj1.marktetk_lkz = t1.bildtaf_lkz)
        left join w_ben_gk lj11 on (
            lj1.marktetk_textcode = lj11.ben_textcode
            and lj11.ben_iso = :iso
            and lj11.ben_regiso = :regiso
        )
        where t1.bildtaf_btnr = :btnr
          and t1.bildtaf_produktart = :produktart
        """;

    private static final String SEARCH_PARTS_BY_NUMBER = """
        select t1.teil_hauptgr || t1.teil_untergrup || t1.teil_sachnr as sachnr,
               t2.ben_text as benennung,
               t1.teil_benennzus as zusatz,
               t1.teil_hauptgr as hauptgr,
               t1.teil_untergrup as untergrup
        from w_teil t1
        left join w_ben_gk t2 on (t1.teil_textcode = t2.ben_textcode and t2.ben_iso = :iso)
        where t1.teil_hauptgr || t1.teil_untergrup || t1.teil_sachnr like :pattern
           or t1.teil_sachnr like :pattern
        order by t1.teil_hauptgr, t1.teil_untergrup, t1.teil_sachnr
        limit 100
        """;

    private static final RowMapper<MarketDescriptionDto> MARKET_DESCRIPTION_MAPPER = (rs, rowNum) ->
        MarketDescriptionDto.builder()
            .marketDescription(rs.getString("MarktBen"))
            .build();

    private static final RowMapper<PartByNumberDto> PART_BY_NUMBER_MAPPER = (rs, rowNum) ->
        PartByNumberDto.builder()
            .sachnr(rs.getString("sachnr"))
            .benennung(rs.getString("benennung"))
            .zusatz(rs.getString("zusatz"))
            .hauptgr(rs.getString("hauptgr"))
            .untergrup(rs.getString("untergrup"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Loads the market description for a specific illustration plate.
     *
     * @param btnr illustration plate number
     * @param produktart product type
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return list containing the market description (empty if not found)
     */
    public List<MarketDescriptionDto> loadMarketDescription(String btnr, String produktart, String iso, String regiso) {
        return jdbc.query(
            LOAD_MARKET_DESCRIPTION,
            Map.of(
                "btnr", btnr,
                "produktart", produktart,
                "iso", iso,
                "regiso", regiso
            ),
            MARKET_DESCRIPTION_MAPPER
        );
    }

    /**
     * Searches parts by part number across the entire catalog.
     *
     * @param partNumber part number prefix or full number
     * @param iso ISO language code
     * @return list of matching parts
     */
    public List<PartByNumberDto> searchByPartNumber(String partNumber, String iso) {
        return jdbc.query(
            SEARCH_PARTS_BY_NUMBER,
            Map.of(
                "pattern", partNumber + "%",
                "iso", iso
            ),
            PART_BY_NUMBER_MAPPER
        );
    }
}
