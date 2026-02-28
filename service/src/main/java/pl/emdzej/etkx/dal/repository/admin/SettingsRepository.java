package pl.emdzej.etkx.dal.repository.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pl.emdzej.etkx.dal.dto.admin.IpacMarketDto;
import pl.emdzej.etkx.dal.dto.admin.LanguageDto;
import pl.emdzej.etkx.dal.dto.admin.MarketDto;
import pl.emdzej.etkx.dal.dto.admin.MarketIdDto;
import pl.emdzej.etkx.dal.dto.admin.RegionDto;
import pl.emdzej.etkx.dal.dto.admin.UserMarketDto;
import pl.emdzej.etkx.dal.dto.admin.UserPermissionDto;
import pl.emdzej.etkx.dal.dto.admin.UserRegionDto;
import pl.emdzej.etkx.dal.dto.admin.UserRightDto;
import pl.emdzej.etkx.dal.dto.admin.UserSettingsDto;

/**
 * Repository for user and dealer settings data.
 */
@Repository
@RequiredArgsConstructor
public class SettingsRepository {
    private static final String RETRIEVE_EINSTELLUNGEN = """
        select user_marke UserMarke,
            user_produktart UserProduktart,
            user_lenkung UserLenkung,
            user_katalogumfang UserKatalogumfang,
            user_iso UserIso,
            user_regiso UserRegiso,
            user_expand_bnb UserExpandBnb,
            user_short_searchpath UserShortSearchpath,
            user_request_saz UserRequestSaz,
            user_show_proddate UserShowProddate,
            user_dft_verbaumenge UserDftVerbaumenge
        from w_user_einstellungen
        where user_id = :id
        """;

    private static final String RETRIEVE_EINSTELLUNGEN_JAVA = """
        select user_marke UserMarke,
            user_produktart UserProduktart,
            user_lenkung UserLenkung,
            user_katalogumfang UserKatalogumfang,
            user_iso UserIso,
            user_regiso UserRegiso,
            user_expand_bnb UserExpandBnb,
            user_short_searchpath UserShortSearchpath,
            user_request_saz UserRequestSaz,
            user_show_proddate UserShowProddate,
            user_suchraum UserSuchraum,
            user_show_preise UserShowPreise,
            user_show_tipps UserShowTipps,
            user_primaermarkt_id UserPrimaermarktId,
            user_tablestretch UserTablestretch,
            user_fontsize UserFontsize,
            user_dft_verbaumenge UserDftVerbaumenge,
            user_aufbewahrung UserAufbewahrung
        from w_user_einstellungen@etk_nutzer
        where user_firma_id = :firmaId
          and user_id = :id
        """;

    private static final String RETRIEVE_EINSTELLUNGEN_MARKTID_JAVA = """
        select user_marktid Id,
            marktipac_kuerzel Kuerzel,
            marktipac_lkz Lkz,
            marktipac_relevanz_pa Produktart
        from w_user@etk_nutzer, w_markt_ipac@etk_publ
        where user_id = :id
          and user_firma_id = :firmaId
          and marktipac_id = user_marktid
        """;

    private static final String RETRIEVE_IPAC_MARKT = """
        select marktipac_kuerzel Kuerzel,
            marktipac_lkz Lkz
        from w_markt_ipac@etk_publ
        where marktipac_id = :id
        """;

    private static final String UPDATE_MARKTID = """
        update w_user
        set user_marktid = :marktId
        where user_firma_id = coalesce(:firmaId, user_firma_id)
          and user_id = coalesce(:id, user_id)
        """;

    private static final String RETRIEVE_EINSTELLUNGEN_REGIONEN = """
        select user_region Region
        from w_user_einstellungen_region@etk_nutzer
        where user_id = :id
        """;

    private static final String RETRIEVE_EINSTELLUNGEN_REGIONEN_JAVA = """
        select user_region Region
        from w_user_einstellungen_region@etk_nutzer
        where user_firma_id = :firmaId
          and user_id = :id
        """;

    private static final String DELETE_EINSTELLUNGEN = """
        delete from w_user_einstellungen@etk_nutzer
        where user_id = :id
        """;

    private static final String DELETE_EINSTELLUNGEN_JAVA = """
        delete from w_user_einstellungen@etk_nutzer
        where user_firma_id = :firmaId
          and user_id = :id
        """;

    private static final String INSERT_EINSTELLUNGEN = """
        insert into w_user_einstellungen@etk_nutzer (
            user_id,
            user_marke,
            user_produktart,
            user_lenkung,
            user_katalogumfang,
            user_iso,
            user_regiso,
            user_expand_bnb,
            user_short_searchpath,
            user_request_saz,
            user_show_proddate,
            user_dft_verbaumenge
        ) values (
            :id,
            :marke,
            :produktart,
            :lenkung,
            :katalogumfang,
            :iso,
            :regiso,
            :expandBnb,
            :shortSearchpath,
            :requestSaz,
            :showProddate,
            :verbaumenge
        )
        """;

    private static final String INSERT_EINSTELLUNGEN_JAVA = """
        insert into w_user_einstellungen@etk_nutzer (
            user_firma_id,
            user_id,
            user_marke,
            user_produktart,
            user_lenkung,
            user_katalogumfang,
            user_iso,
            user_regiso,
            user_expand_bnb,
            user_short_searchpath,
            user_request_saz,
            user_show_proddate,
            user_suchraum,
            user_show_preise,
            user_show_tipps,
            user_primaermarkt_id,
            user_tablestretch,
            user_fontsize,
            user_dft_verbaumenge,
            user_aufbewahrung
        ) values (
            :firmaId,
            :id,
            :marke,
            :produktart,
            :lenkung,
            :katalogumfang,
            :iso,
            :regiso,
            :expandBnb,
            :shortSearchpath,
            :requestSaz,
            :showProddate,
            :suchraum,
            :showPreise,
            :showTipps,
            :primaerMarktId,
            :tableStretch,
            :fontSize,
            :verbaumenge,
            :aufbewahrung
        )
        """;

    private static final String RETRIEVE_SPRACHEN = """
        select ben_iso ISO,
            ben_regiso RegISO,
            ben_text Benennung
        from w_ben_gk, w_publben
        where publben_art = 'S'
          and ben_textcode = publben_textcode
          and ben_iso = substr(publben_bezeichnung, 1, 2)
          and ben_regiso = substr(publben_bezeichnung, 3, 2)
        order by ben_iso, ben_regiso
        """;

    private static final String DELETE_EINSTELLUNGEN_REGIONEN = """
        delete from w_user_einstellungen_region@etk_nutzer
        where user_id = :id
        """;

    private static final String DELETE_EINSTELLUNGEN_REGIONEN_JAVA = """
        delete from w_user_einstellungen_region@etk_nutzer
        where user_firma_id = :firmaId
          and user_id = :id
        """;

    private static final String INSERT_EINSTELLUNGEN_REGIONEN = """
        insert into w_user_einstellungen_region@etk_nutzer (user_id, user_region)
        values (:id, :region)
        """;

    private static final String INSERT_EINSTELLUNGEN_REGIONEN_JAVA = """
        insert into w_user_einstellungen_region@etk_nutzer (user_firma_id, user_id, user_region)
        values (:firmaId, :id, :region)
        """;

    private static final String RETRIEVE_REGIONEN = """
        select distinct fztyp_ktlgausf REGIONEN
        from w_fztyp
        """;

    private static final String RETRIEVE_COUNT_MODSPALTEN = """
        select count(distinct fztyp_mospid) Count
        from w_baureihe, w_fztyp
        where baureihe_marke_tps = :marke
          and baureihe_produktart = :produktart
          and baureihe_baureihe = fztyp_baureihe
          __LENKUNG_STMT__
          and fztyp_vbereich = :katalogumfang
          and fztyp_ktlgausf in (:regionen)
        """;

    private static final String RETRIEVE_RECHTE_JAVA = """
        select userf_recht_id RechtId
        from w_user_funktionsrechte@etk_nutzer
        where userf_id = :id
          and userf_firma_id = :firmaId
        """;

    private static final String RETRIEVE_RECHTE = """
        select userf_recht_id RechtId
        from w_user_funktionsrechte
        where userf_id = :id
        """;

    private static final String RETRIEVE_BERECHTIGUNGEN = """
        select userb_art Art,
            userb_wert Wert
        from w_user_berechtigungen@etk_nutzer
        where userb_firma_id = :firmaId
          and userb_id = :id
        order by Art
        """;

    private static final String DELETE_TEILENOTIZEN_ABGELAUFEN = """
        delete from w_teileinfo@etk_nutzer
        where teileinfo_firma_id = :firmaId
          and teileinfo_user_id = :id
          and (
              (teileinfo_gueltig_bis_jahr <= :jahr
                and teileinfo_gueltig_bis_monat < :monat
                and teileinfo_gueltig_bis_monat is not null)
              or (teileinfo_gueltig_bis_jahr < :jahr
                and teileinfo_gueltig_bis_monat is null)
          )
        """;

    private static final String RETRIEVE_MAERKTE_ETK_LOKALE_PRODUKTE = """
        select marktetk_id id,
            ben_text name,
            marktetk_isokz kuerzel,
            marktetk_lkz lkz
        from w_markt_etk, w_ben_gk
        where marktetk_anzlokbt > 0
          and ben_textcode = marktetk_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by ben_text
        """;

    private static final String RETRIEVE_ETK_MARKT = """
        select ben_text name,
            marktetk_isokz kuerzel,
            marktetk_lkz lkz
        from w_markt_etk, w_ben_gk
        where marktetk_id = :id
          and ben_textcode = marktetk_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        """;

    private static final String RETRIEVE_WEITERE_MAERKTE = """
        select user_markt_id userMarktId,
            ben_text name,
            marktetk_isokz kuerzel,
            marktetk_lkz lkz
        from w_user_einstellungen_wmaerkte@etk_nutzer, w_markt_etk, w_ben_gk
        where user_firma_id = :firmaId
          and user_id = :id
          and marktetk_id = user_markt_id
          and ben_textcode = marktetk_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by ben_text
        """;

    private static final String DELETE_WEITERE_MAERKTE = """
        delete from w_user_einstellungen_wmaerkte@etk_nutzer
        where user_firma_id = :firmaId
          and user_id = :userId
        """;

    private static final String INSERT_WEITERE_MAERKTE = """
        insert into w_user_einstellungen_wmaerkte@etk_nutzer (user_firma_id, user_id, user_markt_id)
        values (:firmaId, :userId, :marktId)
        """;

    private static final RowMapper<UserSettingsDto> USER_SETTINGS_MAPPER = (rs, rowNum) ->
        UserSettingsDto.builder()
            .userMarke(rs.getString("UserMarke"))
            .userProduktart(rs.getString("UserProduktart"))
            .userLenkung(rs.getString("UserLenkung"))
            .userKatalogumfang(rs.getString("UserKatalogumfang"))
            .userIso(rs.getString("UserIso"))
            .userRegiso(rs.getString("UserRegiso"))
            .userExpandBnb(rs.getString("UserExpandBnb"))
            .userShortSearchpath(rs.getString("UserShortSearchpath"))
            .userRequestSaz(rs.getString("UserRequestSaz"))
            .userShowProddate(rs.getString("UserShowProddate"))
            .userSuchraum(rs.getString("UserSuchraum"))
            .userShowPreise(rs.getString("UserShowPreise"))
            .userShowTipps(rs.getString("UserShowTipps"))
            .userPrimaermarktId(rs.getString("UserPrimaermarktId"))
            .userTablestretch(rs.getString("UserTablestretch"))
            .userFontsize(rs.getString("UserFontsize"))
            .userDftVerbaumenge(rs.getString("UserDftVerbaumenge"))
            .userAufbewahrung(rs.getString("UserAufbewahrung"))
            .build();

    private static final RowMapper<MarketIdDto> MARKET_ID_MAPPER = (rs, rowNum) ->
        MarketIdDto.builder()
            .id(rs.getString("Id"))
            .kuerzel(rs.getString("Kuerzel"))
            .lkz(rs.getString("Lkz"))
            .produktart(rs.getString("Produktart"))
            .build();

    private static final RowMapper<IpacMarketDto> IPAC_MARKET_MAPPER = (rs, rowNum) ->
        IpacMarketDto.builder()
            .kuerzel(rs.getString("Kuerzel"))
            .lkz(rs.getString("Lkz"))
            .build();

    private static final RowMapper<UserRegionDto> USER_REGION_MAPPER = (rs, rowNum) ->
        UserRegionDto.builder()
            .region(rs.getString("Region"))
            .build();

    private static final RowMapper<LanguageDto> LANGUAGE_MAPPER = (rs, rowNum) ->
        LanguageDto.builder()
            .iso(rs.getString("ISO"))
            .regiso(rs.getString("RegISO"))
            .benennung(rs.getString("Benennung"))
            .build();

    private static final RowMapper<RegionDto> REGION_MAPPER = (rs, rowNum) ->
        RegionDto.builder()
            .region(rs.getString("REGIONEN"))
            .build();

    private static final RowMapper<UserRightDto> USER_RIGHT_MAPPER = (rs, rowNum) ->
        UserRightDto.builder()
            .rechtId(rs.getString("RechtId"))
            .build();

    private static final RowMapper<UserPermissionDto> USER_PERMISSION_MAPPER = (rs, rowNum) ->
        UserPermissionDto.builder()
            .art(rs.getString("Art"))
            .wert(rs.getString("Wert"))
            .build();

    private static final RowMapper<MarketDto> MARKET_MAPPER = (rs, rowNum) ->
        MarketDto.builder()
            .id(rs.getString("id"))
            .name(rs.getString("name"))
            .kuerzel(rs.getString("kuerzel"))
            .lkz(rs.getString("lkz"))
            .build();

    private static final RowMapper<MarketDto> MARKET_NO_ID_MAPPER = (rs, rowNum) ->
        MarketDto.builder()
            .name(rs.getString("name"))
            .kuerzel(rs.getString("kuerzel"))
            .lkz(rs.getString("lkz"))
            .build();

    private static final RowMapper<UserMarketDto> USER_MARKET_MAPPER = (rs, rowNum) ->
        UserMarketDto.builder()
            .userMarktId(rs.getString("userMarktId"))
            .name(rs.getString("name"))
            .kuerzel(rs.getString("kuerzel"))
            .lkz(rs.getString("lkz"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Loads user settings for a user ID.
     *
     * @param id user identifier
     * @return user settings entry
     */
    public UserSettingsDto loadSettings(String id) {
        return jdbc.queryForObject(RETRIEVE_EINSTELLUNGEN, Map.of("id", id), USER_SETTINGS_MAPPER);
    }

    /**
     * Loads user settings for a specific company and user.
     *
     * @param firmaId company identifier
     * @param id user identifier
     * @return user settings entry
     */
    public UserSettingsDto loadSettingsForCompany(String firmaId, String id) {
        return jdbc.queryForObject(RETRIEVE_EINSTELLUNGEN_JAVA, Map.of("firmaId", firmaId, "id", id),
            USER_SETTINGS_MAPPER);
    }

    /**
     * Loads the IPAC market definition for a user.
     *
     * @param firmaId company identifier
     * @param id user identifier
     * @return market entry
     */
    public MarketIdDto loadUserMarket(String firmaId, String id) {
        return jdbc.queryForObject(RETRIEVE_EINSTELLUNGEN_MARKTID_JAVA, Map.of("firmaId", firmaId, "id", id),
            MARKET_ID_MAPPER);
    }

    /**
     * Loads IPAC market information by ID.
     *
     * @param id market identifier
     * @return IPAC market entry
     */
    public IpacMarketDto loadIpacMarket(String id) {
        return jdbc.queryForObject(RETRIEVE_IPAC_MARKT, Map.of("id", id), IPAC_MARKET_MAPPER);
    }

    /**
     * Updates the market identifier for a user.
     *
     * @param marktId market identifier
     * @param firmaId optional company identifier
     * @param id optional user identifier
     * @return number of rows affected
     */
    public int updateMarketId(String marktId, String firmaId, String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("marktId", marktId);
        params.put("firmaId", firmaId);
        params.put("id", id);
        return jdbc.update(UPDATE_MARKTID, params);
    }

    /**
     * Loads regions configured for a user.
     *
     * @param id user identifier
     * @return list of regions
     */
    public List<UserRegionDto> loadUserRegions(String id) {
        return jdbc.query(RETRIEVE_EINSTELLUNGEN_REGIONEN, Map.of("id", id), USER_REGION_MAPPER);
    }

    /**
     * Loads regions configured for a user within a company.
     *
     * @param firmaId company identifier
     * @param id user identifier
     * @return list of regions
     */
    public List<UserRegionDto> loadUserRegionsForCompany(String firmaId, String id) {
        return jdbc.query(RETRIEVE_EINSTELLUNGEN_REGIONEN_JAVA, Map.of("firmaId", firmaId, "id", id),
            USER_REGION_MAPPER);
    }

    /**
     * Deletes user settings by user identifier.
     *
     * @param id user identifier
     * @return number of rows affected
     */
    public int deleteSettings(String id) {
        return jdbc.update(DELETE_EINSTELLUNGEN, Map.of("id", id));
    }

    /**
     * Deletes user settings for a specific company and user.
     *
     * @param firmaId company identifier
     * @param id user identifier
     * @return number of rows affected
     */
    public int deleteSettingsForCompany(String firmaId, String id) {
        return jdbc.update(DELETE_EINSTELLUNGEN_JAVA, Map.of("firmaId", firmaId, "id", id));
    }

    /**
     * Inserts user settings for a user.
     *
     * @param params parameter map describing settings values
     * @return number of rows affected
     */
    public int insertSettings(Map<String, Object> params) {
        return jdbc.update(INSERT_EINSTELLUNGEN, params);
    }

    /**
     * Inserts user settings for a specific company and user.
     *
     * @param params parameter map describing settings values
     * @return number of rows affected
     */
    public int insertSettingsForCompany(Map<String, Object> params) {
        return jdbc.update(INSERT_EINSTELLUNGEN_JAVA, params);
    }

    /**
     * Loads available languages for settings.
     *
     * @return list of language entries
     */
    public List<LanguageDto> loadLanguages() {
        return jdbc.query(RETRIEVE_SPRACHEN, LANGUAGE_MAPPER);
    }

    /**
     * Deletes user region settings by user identifier.
     *
     * @param id user identifier
     * @return number of rows affected
     */
    public int deleteUserRegions(String id) {
        return jdbc.update(DELETE_EINSTELLUNGEN_REGIONEN, Map.of("id", id));
    }

    /**
     * Deletes user region settings for a specific company and user.
     *
     * @param firmaId company identifier
     * @param id user identifier
     * @return number of rows affected
     */
    public int deleteUserRegionsForCompany(String firmaId, String id) {
        return jdbc.update(DELETE_EINSTELLUNGEN_REGIONEN_JAVA, Map.of("firmaId", firmaId, "id", id));
    }

    /**
     * Inserts a user region setting.
     *
     * @param id user identifier
     * @param region region code
     * @return number of rows affected
     */
    public int insertUserRegion(String id, String region) {
        return jdbc.update(INSERT_EINSTELLUNGEN_REGIONEN, Map.of("id", id, "region", region));
    }

    /**
     * Inserts a user region setting for a specific company and user.
     *
     * @param firmaId company identifier
     * @param id user identifier
     * @param region region code
     * @return number of rows affected
     */
    public int insertUserRegionForCompany(String firmaId, String id, String region) {
        return jdbc.update(INSERT_EINSTELLUNGEN_REGIONEN_JAVA,
            Map.of("firmaId", firmaId, "id", id, "region", region));
    }

    /**
     * Loads distinct regions from vehicle types.
     *
     * @return list of regions
     */
    public List<RegionDto> loadRegions() {
        return jdbc.query(RETRIEVE_REGIONEN, REGION_MAPPER);
    }

    /**
     * Counts modification columns for a given scope.
     *
     * @param marke brand code
     * @param produktart product type
     * @param katalogumfang catalog scope
     * @param regionen list of regions
     * @param lenkungClause optional steering clause
     * @param extraParams additional parameters used by optional clauses
     * @return count of distinct MOSP identifiers
     */
    public Integer countModificationColumns(
        String marke,
        String produktart,
        String katalogumfang,
        List<String> regionen,
        String lenkungClause,
        Map<String, Object> extraParams
    ) {
        String sql = applyClauses(RETRIEVE_COUNT_MODSPALTEN, Map.of("__LENKUNG_STMT__", lenkungClause));
        Map<String, Object> params = new HashMap<>(Map.of(
            "marke", marke,
            "produktart", produktart,
            "katalogumfang", katalogumfang,
            "regionen", regionen
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.queryForObject(sql, params, Integer.class);
    }

    /**
     * Loads user functional rights for a company.
     *
     * @param firmaId company identifier
     * @param id user identifier
     * @return list of functional rights
     */
    public List<UserRightDto> loadUserRightsForCompany(String firmaId, String id) {
        return jdbc.query(RETRIEVE_RECHTE_JAVA, Map.of("firmaId", firmaId, "id", id), USER_RIGHT_MAPPER);
    }

    /**
     * Loads user functional rights.
     *
     * @param id user identifier
     * @return list of functional rights
     */
    public List<UserRightDto> loadUserRights(String id) {
        return jdbc.query(RETRIEVE_RECHTE, Map.of("id", id), USER_RIGHT_MAPPER);
    }

    /**
     * Loads user permissions for a company.
     *
     * @param firmaId company identifier
     * @param id user identifier
     * @return list of permissions
     */
    public List<UserPermissionDto> loadUserPermissions(String firmaId, String id) {
        return jdbc.query(RETRIEVE_BERECHTIGUNGEN, Map.of("firmaId", firmaId, "id", id), USER_PERMISSION_MAPPER);
    }

    /**
     * Deletes expired part notes for a user.
     *
     * @param firmaId company identifier
     * @param id user identifier
     * @param jahr year threshold
     * @param monat month threshold
     * @return number of rows affected
     */
    public int deleteExpiredPartNotes(String firmaId, String id, int jahr, int monat) {
        return jdbc.update(DELETE_TEILENOTIZEN_ABGELAUFEN,
            Map.of("firmaId", firmaId, "id", id, "jahr", jahr, "monat", monat));
    }

    /**
     * Loads local ETK markets for a language.
     *
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return list of markets
     */
    public List<MarketDto> loadLocalEtkMarkets(String iso, String regiso) {
        return jdbc.query(RETRIEVE_MAERKTE_ETK_LOKALE_PRODUKTE, Map.of("iso", iso, "regiso", regiso),
            MARKET_MAPPER);
    }

    /**
     * Loads a specific ETK market by ID.
     *
     * @param id market identifier
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return market entry
     */
    public MarketDto loadEtkMarket(String id, String iso, String regiso) {
        return jdbc.queryForObject(RETRIEVE_ETK_MARKT, Map.of("id", id, "iso", iso, "regiso", regiso),
            MARKET_NO_ID_MAPPER);
    }

    /**
     * Loads additional markets assigned to a user.
     *
     * @param firmaId company identifier
     * @param id user identifier
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return list of user markets
     */
    public List<UserMarketDto> loadAdditionalMarkets(String firmaId, String id, String iso, String regiso) {
        return jdbc.query(RETRIEVE_WEITERE_MAERKTE,
            Map.of("firmaId", firmaId, "id", id, "iso", iso, "regiso", regiso), USER_MARKET_MAPPER);
    }

    /**
     * Deletes additional markets for a user.
     *
     * @param firmaId company identifier
     * @param userId user identifier
     * @return number of rows affected
     */
    public int deleteAdditionalMarkets(String firmaId, String userId) {
        return jdbc.update(DELETE_WEITERE_MAERKTE, Map.of("firmaId", firmaId, "userId", userId));
    }

    /**
     * Inserts an additional market for a user.
     *
     * @param firmaId company identifier
     * @param userId user identifier
     * @param marktId market identifier
     * @return number of rows affected
     */
    public int insertAdditionalMarket(String firmaId, String userId, String marktId) {
        return jdbc.update(INSERT_WEITERE_MAERKTE, Map.of("firmaId", firmaId, "userId", userId, "marktId", marktId));
    }

    private static String applyClauses(String sql, Map<String, String> replacements) {
        String result = sql;
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            String clause = entry.getValue();
            result = result.replace(entry.getKey(), StringUtils.hasText(clause) ? " " + clause + " " : " ");
        }
        return result;
    }
}
