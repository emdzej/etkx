package pl.etkx.dal.repository.admin;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.etkx.dal.dto.admin.CompanyDto;
import pl.etkx.dal.dto.admin.LanguageDto;
import pl.etkx.dal.dto.admin.PriceCompanyDto;
import pl.etkx.dal.dto.admin.UserSequenceDto;
import pl.etkx.dal.dto.admin.UserTableDto;
import pl.etkx.dal.dto.admin.VersionInfoDto;

/**
 * Repository for admin tool (system administration) data.
 */
@Repository
@RequiredArgsConstructor
public class AdminToolRepository {
    private static final String LOAD_FIRMEN = """
        select firma_id Id
        from w_firma
        order by firma_bezeichnung
        """;

    private static final String DELETE_PRICES = """
        delete from w_preise
        """;

    private static final String DELETE_PRICES_BY_FIRMA = """
        delete from w_preise
        where preise_firma = :firma
        """;

    private static final String ERMITTLE_NUTZERTABELLEN = """
        select tname tabelle
        from systable
        where tname not like 'sys%'
          and tname not like '%id_seq%'
        """;

    private static final String ERMITTLE_NUTZERSEQUENZEN = """
        select tname sequenz
        from systable
        where tname not like 'sys%'
          and tname like '%id_seq%'
        """;

    private static final String GET_COUNT_FIRMA_PREISE = """
        select count(preise_firma) cnt
        from w_preise
        where preise_firma = :firma
        """;

    private static final String GET_DISTINCT_FIRMA_PREISE = """
        select distinct preise_firma FirmaId
        from w_preise
        """;

    private static final String INSERT_PRICES = """
        insert into w_preise (
            preise_firma,
            preise_sachnr,
            preise_evpreis,
            preise_nachbelastung,
            preise_rabattschluessel,
            preise_preisaenderung,
            preise_preis_kz,
            preise_sonderpreis,
            preise_sonderpreis_kz,
            preise_mwst,
            preise_mwst_code,
            preise_zolltarifnr,
            preise_nettopreis
        ) values (
            :preiseFirma,
            :preiseSachnr,
            :preiseEvpreis,
            :preiseNachbelastung,
            :preiseRabattschluessel,
            :preisePreisaenderung,
            :preisePreisKz,
            :preiseSonderpreis,
            :preiseSonderpreisKz,
            :preiseMwst,
            :preiseMwstCode,
            :preiseZolltarifnr,
            :preiseNettopreis
        )
        """;

    private static final String UPDATE_PRICES = """
        update w_preise
        set preise_preis_kz = :preisePreisKz,
            preise_evpreis = :preiseEvpreis,
            preise_nachbelastung = :preiseNachbelastung,
            preise_rabattschluessel = :preiseRabattschluessel,
            preise_preisaenderung = :preisePreisaenderung,
            preise_sonderpreis = :preiseSonderpreis,
            preise_sonderpreis_kz = :preiseSonderpreisKz,
            preise_mwst = :preiseMwst,
            preise_mwst_code = :preiseMwstCode,
            preise_zolltarifnr = :preiseZolltarifnr,
            preise_nettopreis = :preiseNettopreis
        where preise_firma = :preiseFirma
          and preise_sachnr = :preiseSachnr
        """;

    private static final String LOAD_SPRACHEN = """
        select ben_iso ISO,
            ben_regiso RegISO,
            ben_text Benennung
        from w_ben_gk@etk_publ, w_publben@etk_publ
        where publben_art = 'S'
          and ben_textcode = publben_textcode
          and ben_iso = substr(publben_bezeichnung, 1, 2)
          and ben_regiso = substr(publben_bezeichnung, 3, 2)
        """;

    private static final String LOAD_DBVERSIONSINFO = """
        select verwaltung_info Info,
            verwaltung_wert Wert
        from w_verwaltung
        """;

    private static final RowMapper<CompanyDto> COMPANY_MAPPER = (rs, rowNum) ->
        CompanyDto.builder()
            .id(rs.getString("Id"))
            .build();

    private static final RowMapper<PriceCompanyDto> PRICE_COMPANY_MAPPER = (rs, rowNum) ->
        PriceCompanyDto.builder()
            .firmaId(rs.getString("FirmaId"))
            .build();

    private static final RowMapper<LanguageDto> LANGUAGE_MAPPER = (rs, rowNum) ->
        LanguageDto.builder()
            .iso(rs.getString("ISO"))
            .regiso(rs.getString("RegISO"))
            .benennung(rs.getString("Benennung"))
            .build();

    private static final RowMapper<VersionInfoDto> VERSION_INFO_MAPPER = (rs, rowNum) ->
        VersionInfoDto.builder()
            .info(rs.getString("Info"))
            .wert(rs.getString("Wert"))
            .build();

    private static final RowMapper<UserTableDto> USER_TABLE_MAPPER = (rs, rowNum) ->
        UserTableDto.builder()
            .tabelle(rs.getString("tabelle"))
            .build();

    private static final RowMapper<UserSequenceDto> USER_SEQUENCE_MAPPER = (rs, rowNum) ->
        UserSequenceDto.builder()
            .sequenz(rs.getString("sequenz"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Loads all company identifiers.
     *
     * @return list of companies
     */
    public List<CompanyDto> loadCompanies() {
        return jdbc.query(LOAD_FIRMEN, COMPANY_MAPPER);
    }

    /**
     * Deletes all price records.
     *
     * @return number of rows affected
     */
    public int deletePrices() {
        return jdbc.update(DELETE_PRICES, Map.of());
    }

    /**
     * Deletes price records for a specific company.
     *
     * @param firma company identifier
     * @return number of rows affected
     */
    public int deletePricesByCompany(String firma) {
        return jdbc.update(DELETE_PRICES_BY_FIRMA, Map.of("firma", firma));
    }

    /**
     * Loads user table names from the database metadata.
     *
     * @return list of user tables
     */
    public List<UserTableDto> loadUserTables() {
        return jdbc.query(ERMITTLE_NUTZERTABELLEN, USER_TABLE_MAPPER);
    }

    /**
     * Loads user sequence names from the database metadata.
     *
     * @return list of user sequences
     */
    public List<UserSequenceDto> loadUserSequences() {
        return jdbc.query(ERMITTLE_NUTZERSEQUENZEN, USER_SEQUENCE_MAPPER);
    }

    /**
     * Counts price entries for a specific company.
     *
     * @param firma company identifier
     * @return number of price entries
     */
    public Integer getPriceCountByCompany(String firma) {
        return jdbc.queryForObject(GET_COUNT_FIRMA_PREISE, Map.of("firma", firma), Integer.class);
    }

    /**
     * Loads distinct company identifiers from price records.
     *
     * @return list of companies that have price entries
     */
    public List<PriceCompanyDto> loadDistinctPriceCompanies() {
        return jdbc.query(GET_DISTINCT_FIRMA_PREISE, PRICE_COMPANY_MAPPER);
    }

    /**
     * Inserts a price entry.
     *
     * @param params parameters describing the price entry
     * @return number of rows affected
     */
    public int insertPrice(Map<String, Object> params) {
        return jdbc.update(INSERT_PRICES, params);
    }

    /**
     * Updates a price entry.
     *
     * @param params parameters describing the price entry
     * @return number of rows affected
     */
    public int updatePrice(Map<String, Object> params) {
        return jdbc.update(UPDATE_PRICES, params);
    }

    /**
     * Loads available languages for admin tool texts.
     *
     * @return list of language entries
     */
    public List<LanguageDto> loadLanguages() {
        return jdbc.query(LOAD_SPRACHEN, LANGUAGE_MAPPER);
    }

    /**
     * Loads database version information.
     *
     * @return list of version info entries
     */
    public List<VersionInfoDto> loadDatabaseVersionsInfo() {
        return jdbc.query(LOAD_DBVERSIONSINFO, VERSION_INFO_MAPPER);
    }
}
