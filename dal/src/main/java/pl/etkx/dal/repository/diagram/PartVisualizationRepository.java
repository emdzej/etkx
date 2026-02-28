package pl.etkx.dal.repository.diagram;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.etkx.dal.dto.diagram.PartVisualizationDto;

/**
 * Repository for part visualization data (VisualisierungTeil).
 */
@Repository
@RequiredArgsConstructor
public class PartVisualizationRepository {
    private static final String RETRIEVE_VISUALISIERUNGSINFO_GEB = """
        select teil_hauptgr HG,
            teil_untergrup UG,
            teil_sachnr Sachnummer,
            t.ben_text Teilebenennung,
            teil_benennzus TeilebenennungZusatz,
            bildtaf_btnr BildtafelNummer,
            bt.ben_text BildtafelUeberschrift,
            btzeilen_bildposnr Bildnummer,
            bildtaf_grafikid GrafikId,
            grafik_format GrafikFormat,
            grafik_moddate GrafikTimestamp
        from w_btzeilen_verbauung, w_grafik, w_baureihe, w_fztyp, w_ben_gk t, w_ben_gk bt, w_teil, w_bildtaf, w_btzeilen
        where btzeilenv_sachnr = :sachnummer
          and btzeilenv_mospid = fztyp_mospid
          and fztyp_baureihe = baureihe_baureihe
          and baureihe_marke_tps in (:marken)
          and btzeilenv_btnr = bildtaf_btnr
          and bildtaf_produktart in (:produktarten)
          and btzeilenv_sachnr = teil_sachnr
          and btzeilenv_btnr = btzeilen_btnr
          and btzeilenv_pos = btzeilen_pos
          and teil_textcode = t.ben_textcode
          and t.ben_iso = :iso
          and t.ben_regiso = :regiso
          and bildtaf_textc = bt.ben_textcode
          and bt.ben_iso = :iso
          and bt.ben_regiso = :regiso
          and bildtaf_grafikid = grafik_grafikid
          and grafik_art = 'Z'
        """;

    private static final String RETRIEVE_VISUALISIERUNGSINFO_UGB = """
        select teil_hauptgr HG,
            teil_untergrup UG,
            teil_sachnr Sachnummer,
            t.ben_text Teilebenennung,
            teil_benennzus TeilebenennungZusatz,
            bildtaf_btnr BildtafelNummer,
            bt.ben_text BildtafelUeberschrift,
            btzeilenu_bildposnr Bildnummer,
            bildtaf_grafikid GrafikId,
            grafik_format GrafikFormat,
            grafik_moddate GrafikTimestamp
        from w_btzeilenugb, w_grafik, w_ben_gk t, w_ben_gk bt, w_teil, w_bildtaf, w_btzeilenugb_verbauung
        where btzeilenu_sachnr = :sachnummer
          and btzeilenu_btnr = btzeilenuv_btnr
          and btzeilenu_pos = btzeilenuv_pos
          and btzeilenuv_marke_tps in (:marken)
          and btzeilenu_btnr = bildtaf_btnr
          and bildtaf_produktart in (:produktarten)
          and btzeilenu_sachnr = teil_sachnr
          and teil_textcode = t.ben_textcode
          and t.ben_iso = :iso
          and t.ben_regiso = :regiso
          and bildtaf_textc = bt.ben_textcode
          and bt.ben_iso = :iso
          and bt.ben_regiso = :regiso
          and bildtaf_grafikid = grafik_grafikid
          and grafik_art = 'Z'
        """;

    private static final RowMapper<PartVisualizationDto> VISUALIZATION_MAPPER = (rs, rowNum) ->
        PartVisualizationDto.builder()
            .hg(rs.getString("HG"))
            .ug(rs.getString("UG"))
            .sachnummer(rs.getString("Sachnummer"))
            .teilebenennung(rs.getString("Teilebenennung"))
            .teilebenennungZusatz(rs.getString("TeilebenennungZusatz"))
            .bildtafelNummer(rs.getString("BildtafelNummer"))
            .bildtafelUeberschrift(rs.getString("BildtafelUeberschrift"))
            .bildnummer(rs.getString("Bildnummer"))
            .grafikId(rs.getString("GrafikId"))
            .grafikFormat(rs.getString("GrafikFormat"))
            .grafikTimestamp(rs.getString("GrafikTimestamp"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Retrieves visualization data for a part in vehicle context.
     */
    public List<PartVisualizationDto> findVehicleVisualizations(String sachnummer, List<String> marken,
                                                                List<String> produktarten,
                                                                String iso, String regiso) {
        return jdbc.query(RETRIEVE_VISUALISIERUNGSINFO_GEB, Map.of(
            "sachnummer", sachnummer,
            "marken", marken,
            "produktarten", produktarten,
            "iso", iso,
            "regiso", regiso
        ), VISUALIZATION_MAPPER);
    }

    /**
     * Retrieves visualization data for a part in UGB context.
     */
    public List<PartVisualizationDto> findUgbVisualizations(String sachnummer, List<String> marken,
                                                            List<String> produktarten,
                                                            String iso, String regiso) {
        return jdbc.query(RETRIEVE_VISUALISIERUNGSINFO_UGB, Map.of(
            "sachnummer", sachnummer,
            "marken", marken,
            "produktarten", produktarten,
            "iso", iso,
            "regiso", regiso
        ), VISUALIZATION_MAPPER);
    }
}
