package pl.emdzej.etkx.dal.repository.vehicle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pl.emdzej.etkx.dal.dto.vehicle.BauartDto;
import pl.emdzej.etkx.dal.dto.vehicle.BaureiheDto;
import pl.emdzej.etkx.dal.dto.vehicle.KarosserieDto;
import pl.emdzej.etkx.dal.dto.vehicle.LenkungDto;
import pl.emdzej.etkx.dal.dto.vehicle.ModellDto;
import pl.emdzej.etkx.dal.dto.vehicle.MospIdDto;
import pl.emdzej.etkx.dal.dto.vehicle.RegionDto;

/**
 * Repository for vehicle scope selection (FzgUmfang).
 */
@Repository
@RequiredArgsConstructor
public class VehicleScopeRepository {
    private static final String RETRIEVE_REGIONEN = """
        select distinct fztyp_ktlgausf Region
        from w_baureihe, w_fztyp
        where baureihe_marke_tps = :marke
          and baureihe_produktart = :produktart
          and baureihe_vbereich in ('BE', :katalogumfang)
          and baureihe_baureihe = fztyp_baureihe
          and fztyp_vbereich = :katalogumfang
          and fztyp_ktlgausf in (:regionen)
          and fztyp_sichtschutz = 'N'
        order by Region
        """;

    private static final String RETRIEVE_LENKUNGEN = """
        select distinct fztyp_lenkung Lenkung,
            ben_text ExtLenkung
        from w_baureihe, w_ben_gk, w_publben, w_fztyp
        where baureihe_marke_tps = :marke
          and baureihe_produktart = 'P'
          and baureihe_vbereich in ('BE', :katalogumfang)
          and baureihe_baureihe = fztyp_baureihe
          and fztyp_vbereich = :katalogumfang
          and fztyp_ktlgausf in (:regionen)
          and fztyp_lenkung = publben_bezeichnung
          and publben_art = 'L'
          and publben_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by Lenkung
        """;

    private static final String RETRIEVE_BAUARTEN = """
        select distinct baureihe_bauart Bauart,
            ben_text ExtBauart,
            bauart_position Pos
        from w_baureihe, w_bauart, w_ben_gk, w_fztyp
        where baureihe_marke_tps = :marke
          and baureihe_produktart = 'M'
          and baureihe_vbereich in ('BE', :katalogumfang)
          and baureihe_baureihe = fztyp_baureihe
          and fztyp_vbereich = :katalogumfang
          and fztyp_ktlgausf in (:regionen)
          and fztyp_sichtschutz = 'N'
          and bauart_bauart = baureihe_bauart
          and ben_textcode = bauart_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by Pos
        """;

    private static final String RETRIEVE_BAUREIHEN = """
        select distinct baureihe_baureihe Baureihe,
            ben_text ExtBaureihe,
            baureihe_position Pos
        from w_baureihe, w_ben_gk, w_fztyp
        where baureihe_marke_tps = :marke
          and baureihe_produktart = :produktart
          and baureihe_vbereich in ('BE', :katalogumfang)
          __BAUART_STMT__
          and baureihe_baureihe = fztyp_baureihe
          and fztyp_vbereich = :katalogumfang
          and fztyp_ktlgausf in (:regionen)
          __LENKUNG_STMT__
          and fztyp_sichtschutz = 'N'
          and baureihe_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by Pos
        """;

    private static final String RETRIEVE_KAROSSERIEN = """
        select distinct fztyp_karosserie Karosserie,
            ben_text ExtKarosserie
        from w_fztyp, w_ben_gk, w_publben
        where fztyp_baureihe IN (:baureihen)
          and fztyp_vbereich = :katalogumfang
          and fztyp_ktlgausf in (:regionen)
          and fztyp_lenkung IN (:lenkungen)
          and fztyp_sichtschutz = 'N'
          and fztyp_karosserie = publben_bezeichnung
          and publben_art = 'K'
          and publben_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by ExtKarosserie
        """;

    private static final String RETRIEVE_MODELLE = """
        select distinct fztyp_erwvbez Modell,
            vbezp_pos Pos
        from w_vbez_pos, w_baureihe, w_fztyp
        where baureihe_baureihe IN (:baureihen)
          and baureihe_baureihe = fztyp_baureihe
          and fztyp_vbereich = :katalogumfang
          __BAUART_STMT__
          __KAROSSERIE_STMT__
          and fztyp_ktlgausf in (:regionen)
          __LENKUNG_STMT__
          and fztyp_sichtschutz = 'N'
          and vbezp_baureihe = baureihe_baureihe
          and vbezp_vbez = fztyp_vbez
        order by Pos, Modell
        """;

    private static final String RETRIEVE_MODELLSPALTEN = """
        select distinct fztyp_mospid MospID
        from w_baureihe, w_fztyp
        where baureihe_baureihe IN (:baureihen)
          and baureihe_baureihe = fztyp_baureihe
          and fztyp_vbereich = :katalogumfang
          __MODELL_STMT__
          __BAUART_STMT__
          __KAROSSERIE_STMT__
          and fztyp_ktlgausf in (:regionen)
          __LENKUNG_STMT__
          and fztyp_sichtschutz = 'N'
        order by MospID
        """;

    private static final RowMapper<RegionDto> REGION_MAPPER = (rs, rowNum) ->
        RegionDto.builder()
            .region(rs.getString("Region"))
            .build();

    private static final RowMapper<LenkungDto> LENKUNG_MAPPER = (rs, rowNum) ->
        LenkungDto.builder()
            .lenkung(rs.getString("Lenkung"))
            .extLenkung(rs.getString("ExtLenkung"))
            .build();

    private static final RowMapper<BauartDto> BAUART_MAPPER = (rs, rowNum) ->
        BauartDto.builder()
            .bauart(rs.getString("Bauart"))
            .extBauart(rs.getString("ExtBauart"))
            .pos(rs.getString("Pos"))
            .build();

    private static final RowMapper<BaureiheDto> BAUREIHE_MAPPER = (rs, rowNum) ->
        BaureiheDto.builder()
            .baureihe(rs.getString("Baureihe"))
            .extBaureihe(rs.getString("ExtBaureihe"))
            .pos(rs.getString("Pos"))
            .build();

    private static final RowMapper<KarosserieDto> KAROSSERIE_MAPPER = (rs, rowNum) ->
        KarosserieDto.builder()
            .karosserie(rs.getString("Karosserie"))
            .extKarosserie(rs.getString("ExtKarosserie"))
            .build();

    private static final RowMapper<ModellDto> MODELL_MAPPER = (rs, rowNum) ->
        ModellDto.builder()
            .modell(rs.getString("Modell"))
            .pos(rs.getString("Pos"))
            .build();

    private static final RowMapper<MospIdDto> MOSP_MAPPER = (rs, rowNum) ->
        MospIdDto.builder()
            .mospId(rs.getString("MospID"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Retrieves regions for a vehicle scope selection.
     */
    public List<RegionDto> findRegions(String marke, String produktart, String katalogumfang,
                                       List<String> regionen) {
        return jdbc.query(RETRIEVE_REGIONEN, Map.of(
            "marke", marke,
            "produktart", produktart,
            "katalogumfang", katalogumfang,
            "regionen", regionen
        ), REGION_MAPPER);
    }

    /**
     * Retrieves steering options for a vehicle scope selection.
     */
    public List<LenkungDto> findSteerings(String marke, String katalogumfang, List<String> regionen,
                                          String iso, String regiso) {
        return jdbc.query(RETRIEVE_LENKUNGEN, Map.of(
            "marke", marke,
            "katalogumfang", katalogumfang,
            "regionen", regionen,
            "iso", iso,
            "regiso", regiso
        ), LENKUNG_MAPPER);
    }

    /**
     * Retrieves construction types for vehicle scope selection.
     */
    public List<BauartDto> findConstructionTypes(String marke, String katalogumfang, List<String> regionen,
                                                 String iso, String regiso) {
        return jdbc.query(RETRIEVE_BAUARTEN, Map.of(
            "marke", marke,
            "katalogumfang", katalogumfang,
            "regionen", regionen,
            "iso", iso,
            "regiso", regiso
        ), BAUART_MAPPER);
    }

    /**
     * Retrieves series for vehicle scope selection.
     */
    public List<BaureiheDto> findSeries(String marke, String produktart, String katalogumfang,
                                        List<String> regionen, String iso, String regiso,
                                        String bauartClause, String lenkungClause,
                                        Map<String, Object> extraParams) {
        String sql = applyClauses(RETRIEVE_BAUREIHEN, Map.of(
            "__BAUART_STMT__", bauartClause,
            "__LENKUNG_STMT__", lenkungClause
        ));
        Map<String, Object> params = new HashMap<>(Map.of(
            "marke", marke,
            "produktart", produktart,
            "katalogumfang", katalogumfang,
            "regionen", regionen,
            "iso", iso,
            "regiso", regiso
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, BAUREIHE_MAPPER);
    }

    /**
     * Retrieves body types for the given selections.
     */
    public List<KarosserieDto> findBodies(List<String> baureihen, String katalogumfang, List<String> regionen,
                                          List<String> lenkungen, String iso, String regiso) {
        return jdbc.query(RETRIEVE_KAROSSERIEN, Map.of(
            "baureihen", baureihen,
            "katalogumfang", katalogumfang,
            "regionen", regionen,
            "lenkungen", lenkungen,
            "iso", iso,
            "regiso", regiso
        ), KAROSSERIE_MAPPER);
    }

    /**
     * Retrieves models for vehicle scope selection.
     */
    public List<ModellDto> findModels(List<String> baureihen, String katalogumfang, List<String> regionen,
                                      String bauartClause, String karosserieClause, String lenkungClause,
                                      Map<String, Object> extraParams) {
        String sql = applyClauses(RETRIEVE_MODELLE, Map.of(
            "__BAUART_STMT__", bauartClause,
            "__KAROSSERIE_STMT__", karosserieClause,
            "__LENKUNG_STMT__", lenkungClause
        ));
        Map<String, Object> params = new HashMap<>(Map.of(
            "baureihen", baureihen,
            "katalogumfang", katalogumfang,
            "regionen", regionen
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, MODELL_MAPPER);
    }

    /**
     * Retrieves model columns for vehicle scope selection.
     */
    public List<MospIdDto> findModelColumns(List<String> baureihen, String katalogumfang, List<String> regionen,
                                            String modellClause, String bauartClause, String karosserieClause,
                                            String lenkungClause, Map<String, Object> extraParams) {
        String sql = applyClauses(RETRIEVE_MODELLSPALTEN, Map.of(
            "__MODELL_STMT__", modellClause,
            "__BAUART_STMT__", bauartClause,
            "__KAROSSERIE_STMT__", karosserieClause,
            "__LENKUNG_STMT__", lenkungClause
        ));
        Map<String, Object> params = new HashMap<>(Map.of(
            "baureihen", baureihen,
            "katalogumfang", katalogumfang,
            "regionen", regionen
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, MOSP_MAPPER);
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
