package pl.etkx.dal.repository.admin;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.etkx.dal.dto.admin.FremdSachnummerDto;
import pl.etkx.dal.dto.admin.GraphicDto;
import pl.etkx.dal.dto.admin.NetworkDto;
import pl.etkx.dal.dto.admin.NetworkUrlDto;
import pl.etkx.dal.dto.admin.ProxyDto;
import pl.etkx.dal.dto.admin.UrlDto;

/**
 * Repository for miscellaneous utilities data.
 */
@Repository
@RequiredArgsConstructor
public class MiscellaneousRepository {
    private static final String LOAD_GRAFIK = """
        select grafik_blob Grafik,
            grafik_format Format,
            grafik_moddate ModStamp
        from w_grafik
        where grafik_grafikid = :grafikId
          and grafik_art = :art
        """;

    private static final String RETRIEVE_BMWSACHNUMMER_FOR_FREMDESACHNUMMER = """
        select distinct fremdtl_sachnr Sachnummer
        from w_fremdtl
        where fremdtl_fremdsnr = :sachnummerFremd
        """;

    private static final String RETRIEVE_URLS = """
        select url_url URL
        from w_url
        where upper(url_type) = upper(:type)
          and upper(url_iso) = upper(:iso)
          and upper(url_regiso) = upper(:regiso)
          and upper(url_marke_tps) = upper(:marke)
        """;

    private static final String RETRIEVE_BMW_NETZ = """
        select netz_netz Netz,
            netz_krit Krit
        from w_netz
        """;

    private static final String RETRIEVE_BMW_NETZURL = """
        select netzurl_url_asap AsapUrl,
            netzurl_asaptunnel AsapTunnel,
            netzurl_url_zr CentralURL,
            netzurl_url_dom_basic IGDOMBasicsUrl,
            netzurl_url_dom_options IGDOMOptionsUrl
        from w_netzurl
        where netzurl_netz = :netz
          and netzurl_krit = :krit
        """;

    private static final String RETRIEVE_BMW_PROXY = """
        select proxy_proxyname ProxyName,
            proxy_port Port,
            proxy_nutzername UserName,
            proxy_passwort Passwort,
            proxy_realm Realm,
            proxy_ntdomain NtHost,
            proxy_nthost NtDomain
        from w_proxy
        """;

    private static final RowMapper<GraphicDto> GRAPHIC_MAPPER = (rs, rowNum) ->
        GraphicDto.builder()
            .grafik(rs.getBytes("Grafik"))
            .format(rs.getString("Format"))
            .modStamp(rs.getString("ModStamp"))
            .build();

    private static final RowMapper<FremdSachnummerDto> FREMD_SACHNUMMER_MAPPER = (rs, rowNum) ->
        FremdSachnummerDto.builder()
            .sachnummer(rs.getString("Sachnummer"))
            .build();

    private static final RowMapper<UrlDto> URL_MAPPER = (rs, rowNum) ->
        UrlDto.builder()
            .url(rs.getString("URL"))
            .build();

    private static final RowMapper<NetworkDto> NETWORK_MAPPER = (rs, rowNum) ->
        NetworkDto.builder()
            .netz(rs.getString("Netz"))
            .krit(rs.getString("Krit"))
            .build();

    private static final RowMapper<NetworkUrlDto> NETWORK_URL_MAPPER = (rs, rowNum) ->
        NetworkUrlDto.builder()
            .asapUrl(rs.getString("AsapUrl"))
            .asapTunnel(rs.getString("AsapTunnel"))
            .centralUrl(rs.getString("CentralURL"))
            .igdomBasicsUrl(rs.getString("IGDOMBasicsUrl"))
            .igdomOptionsUrl(rs.getString("IGDOMOptionsUrl"))
            .build();

    private static final RowMapper<ProxyDto> PROXY_MAPPER = (rs, rowNum) ->
        ProxyDto.builder()
            .proxyName(rs.getString("ProxyName"))
            .port(rs.getString("Port"))
            .userName(rs.getString("UserName"))
            .passwort(rs.getString("Passwort"))
            .realm(rs.getString("Realm"))
            .ntHost(rs.getString("NtHost"))
            .ntDomain(rs.getString("NtDomain"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Loads a graphic entry by identifier and type.
     *
     * @param grafikId graphic identifier
     * @param art graphic type
     * @return graphic entry
     */
    public GraphicDto loadGraphic(String grafikId, String art) {
        return jdbc.queryForObject(LOAD_GRAFIK, Map.of("grafikId", grafikId, "art", art), GRAPHIC_MAPPER);
    }

    /**
     * Retrieves BMW part numbers for a foreign part number.
     *
     * @param sachnummerFremd foreign part number
     * @return list of BMW part numbers
     */
    public List<FremdSachnummerDto> loadBmwPartNumbers(String sachnummerFremd) {
        return jdbc.query(RETRIEVE_BMWSACHNUMMER_FOR_FREMDESACHNUMMER,
            Map.of("sachnummerFremd", sachnummerFremd), FREMD_SACHNUMMER_MAPPER);
    }

    /**
     * Loads configured URLs for the given criteria.
     *
     * @param type URL type
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @param marke brand code
     * @return list of URL entries
     */
    public List<UrlDto> loadUrls(String type, String iso, String regiso, String marke) {
        return jdbc.query(RETRIEVE_URLS, Map.of("type", type, "iso", iso, "regiso", regiso, "marke", marke),
            URL_MAPPER);
    }

    /**
     * Loads network definitions.
     *
     * @return list of network entries
     */
    public List<NetworkDto> loadNetworks() {
        return jdbc.query(RETRIEVE_BMW_NETZ, NETWORK_MAPPER);
    }

    /**
     * Loads network URLs for a given network and criteria.
     *
     * @param netz network code
     * @param krit criteria
     * @return network URL entry
     */
    public NetworkUrlDto loadNetworkUrls(String netz, String krit) {
        return jdbc.queryForObject(RETRIEVE_BMW_NETZURL, Map.of("netz", netz, "krit", krit), NETWORK_URL_MAPPER);
    }

    /**
     * Loads proxy configuration.
     *
     * @return list of proxy entries
     */
    public List<ProxyDto> loadProxySettings() {
        return jdbc.query(RETRIEVE_BMW_PROXY, PROXY_MAPPER);
    }
}
