package pl.etkx.dal.repository.accessories;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.etkx.dal.dto.accessories.AccessoriesPriceDto;

/**
 * Repository for accessories pricing data.
 */
@Repository
@RequiredArgsConstructor
public class AccessoriesPricingRepository {
    private static final String FIND_PRICES_BY_PARTS = """
        select preise_sachnr PartNumber,
               preise_firma CompanyId,
               preise_evpreis Price,
               preise_rabattschluessel DiscountCode,
               preise_mwst VatRate
        from w_preise
        where preise_firma = :companyId
          and preise_sachnr in (:partNumbers)
        """;

    private static final RowMapper<AccessoriesPriceDto> PRICE_MAPPER = (rs, rowNum) ->
        AccessoriesPriceDto.builder()
            .partNumber(rs.getString("PartNumber"))
            .companyId(rs.getString("CompanyId"))
            .price(rs.getBigDecimal("Price"))
            .discountCode(rs.getString("DiscountCode"))
            .vatRate(rs.getBigDecimal("VatRate"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Loads accessory prices for the given company and part numbers.
     *
     * @param companyId company identifier
     * @param partNumbers list of part numbers
     * @return matching prices
     */
    public List<AccessoriesPriceDto> findPricesByPartNumbers(String companyId, List<String> partNumbers) {
        return jdbc.query(
            FIND_PRICES_BY_PARTS,
            Map.of("companyId", companyId, "partNumbers", partNumbers),
            PRICE_MAPPER
        );
    }
}
