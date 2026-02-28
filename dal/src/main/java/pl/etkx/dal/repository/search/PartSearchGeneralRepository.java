package pl.etkx.dal.repository.search;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.etkx.dal.dto.search.MarketDescriptionDto;

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

    private static final RowMapper<MarketDescriptionDto> MARKET_DESCRIPTION_MAPPER = (rs, rowNum) ->
        MarketDescriptionDto.builder()
            .marketDescription(rs.getString("MarktBen"))
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
}
