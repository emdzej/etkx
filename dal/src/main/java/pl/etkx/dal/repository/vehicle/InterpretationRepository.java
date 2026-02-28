package pl.etkx.dal.repository.vehicle;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.etkx.dal.dto.vehicle.InterpretationDetailDto;
import pl.etkx.dal.dto.vehicle.InterpretationEntryDto;
import pl.etkx.dal.dto.vehicle.PartBrandDto;

/**
 * Repository for interpretation workflows (Interpretation).
 */
@Repository
@RequiredArgsConstructor
public class InterpretationRepository {
    private static final String EXIST_SACHNUMMER_MARKE = """
        select distinct teilm_marke_tps Marke
        from w_teil_marken
        where teilm_sachnr = :sachnummer
          and teilm_marke_tps IN (:marken)
        """;

    private static final String LOAD_INTERPRETATION_EINSTIEG = """
        select hist_sachnr Sachnummer,
            hist_zweig_nr ZweigNr,
            hist_struktur_nr StrukturNr
        from w_hist
        where hist_sachnr_h = :sachnummer
        """;

    private static final String LOAD_INTERPRETATION = """
        select teil_hauptgr Hg,
            teil_untergrup Ug,
            hist_sachnr_h Sachnummer,
            ben_text Benennung,
            teil_entfall_dat EntfallDatum,
            teil_bestellbar Bestellbar,
            teilm_marke_tps Marke,
            hist_zweig_nr ZweigNr,
            hist_struktur_nr StrukturNr
        from w_hist, w_teil, w_ben_gk, w_teil_marken
        where hist_sachnr = :sachnummer
          and hist_sachnr_h = teil_sachnr
          and teil_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
          and teilm_sachnr = teil_sachnr
        order by ZweigNr, StrukturNr
        """;

    private static final RowMapper<PartBrandDto> PART_BRAND_MAPPER = (rs, rowNum) ->
        PartBrandDto.builder()
            .marke(rs.getString("Marke"))
            .build();

    private static final RowMapper<InterpretationEntryDto> INTERPRETATION_ENTRY_MAPPER = (rs, rowNum) ->
        InterpretationEntryDto.builder()
            .sachnummer(rs.getString("Sachnummer"))
            .zweigNr(rs.getString("ZweigNr"))
            .strukturNr(rs.getString("StrukturNr"))
            .build();

    private static final RowMapper<InterpretationDetailDto> INTERPRETATION_DETAIL_MAPPER = (rs, rowNum) ->
        InterpretationDetailDto.builder()
            .hg(rs.getString("Hg"))
            .ug(rs.getString("Ug"))
            .sachnummer(rs.getString("Sachnummer"))
            .benennung(rs.getString("Benennung"))
            .entfallDatum(rs.getString("EntfallDatum"))
            .bestellbar(rs.getString("Bestellbar"))
            .marke(rs.getString("Marke"))
            .zweigNr(rs.getString("ZweigNr"))
            .strukturNr(rs.getString("StrukturNr"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Finds brands for a part number within the given brand list.
     */
    public List<PartBrandDto> findPartBrands(String sachnummer, List<String> marken) {
        return jdbc.query(EXIST_SACHNUMMER_MARKE, Map.of(
            "sachnummer", sachnummer,
            "marken", marken
        ), PART_BRAND_MAPPER);
    }

    /**
     * Loads entry-level interpretation data for a part number.
     */
    public List<InterpretationEntryDto> loadInterpretationEntry(String sachnummer) {
        return jdbc.query(LOAD_INTERPRETATION_EINSTIEG, Map.of("sachnummer", sachnummer), INTERPRETATION_ENTRY_MAPPER);
    }

    /**
     * Loads full interpretation data for a part number.
     */
    public List<InterpretationDetailDto> loadInterpretationDetails(String sachnummer, String iso, String regiso) {
        return jdbc.query(LOAD_INTERPRETATION, Map.of(
            "sachnummer", sachnummer,
            "iso", iso,
            "regiso", regiso
        ), INTERPRETATION_DETAIL_MAPPER);
    }
}
