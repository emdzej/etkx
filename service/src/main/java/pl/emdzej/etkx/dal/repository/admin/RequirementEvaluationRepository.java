package pl.emdzej.etkx.dal.repository.admin;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.emdzej.etkx.dal.dto.admin.PlateMasterCpDto;
import pl.emdzej.etkx.dal.dto.admin.PlateMasterDto;
import pl.emdzej.etkx.dal.dto.admin.RequirementAflDto;
import pl.emdzej.etkx.dal.dto.admin.RequirementSalaDto;
import pl.emdzej.etkx.dal.dto.admin.RequirementTextDto;

/**
 * Repository for requirement evaluation data.
 */
@Repository
@RequiredArgsConstructor
public class RequirementEvaluationRepository {
    private static final String LOAD_BT_STAMM = """
        select distinct bildtaf_hg HG,
            bildtaf_fg FG,
            bildtaf_btnr BildtafelNr,
            bildtaf_bteart BildtafelArt,
            ben_text Benennung,
            bildtaf_bedkez BedKuerzel,
            bildtaf_vorh_cp VorhandenCP,
            bildtaf_lkz Lkz,
            bildtaf_grafikid GrafikId,
            grafik_moddate ModStamp,
            bildtafz_btnr ZubBtnr
        from w_bildtaf
        left join w_grafik on (bildtaf_grafikid = grafik_grafikid)
        left join w_bildtafzub on (bildtaf_btnr = bildtafz_btnr)
        inner join w_ben_gk on (bildtaf_textc = ben_textcode and ben_iso = :iso and ben_regiso = :regiso)
        where bildtaf_btnr = :btnr
        """;

    private static final String LOAD_BT_STAMM_CP = """
        select distinct bildtafc_art Art,
            bildtafc_datum Datum
        from w_bildtaf_cp
        where bildtafc_btnr = :btnr
        order by Art, Datum
        """;

    private static final String LOAD_BT_BEDINGUNGEN_SALA = """
        select distinct btebe_elemid ElementId,
            bedsala_art || bedsala_pnr || bedsala_hz Code,
            ben_text Benennung,
            bed_egid EGruppenId,
            eg_exklusiv Exklusiv,
            bedsala_saz SAZ,
            eg_pos EGruppenPosition,
            bedsala_pnr PrimaNr
        from w_bte_bedkurz, w_ben_gk, w_bed_sala, w_bed, w_eg, w_bte_bedelem
        where btebk_btnr = :btnr
          and btebk_mospid = :mosp
          and btebk_btnr = btebe_btnr
          and btebk_kez = btebe_kez
          and btebe_elemid = bed_elemid
          and bed_elemid = bedsala_id
          and bed_egid = eg_id
          and bed_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        union
        select distinct btebe_elemid ElementId,
            bedsala_art || bedsala_pnr || bedsala_hz Code,
            ben_text Benennung,
            bed_egid EGruppenId,
            eg_exklusiv Exklusiv,
            bedsala_saz SAZ,
            eg_pos EGruppenPosition,
            bedsala_pnr PrimaNr
        from w_bildtaf, w_ben_gk, w_bed_sala, w_bed, w_eg, w_bte_bedelem
        where bildtaf_btnr = :btnr
          and bildtaf_bedkez is not null
          and bildtaf_btnr = btebe_btnr
          and bildtaf_bedkez = btebe_kez
          and btebe_elemid = bed_elemid
          and bed_elemid = bedsala_id
          and bed_egid = eg_id
          and bed_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by EGruppenPosition, PrimaNr
        """;

    private static final String LOAD_BT_BEDINGUNGEN_AFL = """
        select distinct btebe_elemid ElementId,
            bedafl_code Code,
            ben_text Benennung,
            bed_egid EGruppenId,
            eg_exklusiv Exklusiv,
            eg_pos EGruppenPosition
        from w_bte_bedkurz, w_ben_gk, w_bed_afl, w_bed, w_eg, w_bte_bedelem
        where btebk_btnr = :btnr
          and btebk_mospid = :mosp
          and btebk_btnr = btebe_btnr
          and btebk_kez = btebe_kez
          and btebe_elemid = bed_elemid
          and bed_elemid = bedafl_id
          and bed_egid = eg_id
          and bed_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        union
        select distinct btebe_elemid ElementId,
            bedafl_code Code,
            ben_text Benennung,
            bed_egid EGruppenId,
            eg_exklusiv Exklusiv,
            eg_pos EGruppenPosition
        from w_bildtaf, w_ben_gk, w_bed_afl, w_bed, w_eg, w_bte_bedelem
        where bildtaf_btnr = :btnr
          and bildtaf_bedkez is not null
          and bildtaf_btnr = btebe_btnr
          and bildtaf_bedkez = btebe_kez
          and btebe_elemid = bed_elemid
          and bed_elemid = bedafl_id
          and bed_egid = eg_id
          and bed_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by EGruppenPosition, Code
        """;

    private static final String LOAD_BT_BEDINGUNGEN_TEXT = """
        select distinct btebe_elemid ElementId,
            ben_text Benennung,
            bed_egid EGruppenId,
            eg_exklusiv Exklusiv,
            eg_pos EGruppenPosition
        from w_bte_bedkurz, w_ben_gk, w_bed, w_eg, w_bte_bedelem
        where btebk_btnr = :btnr
          and btebk_mospid = :mosp
          and btebk_btnr = btebe_btnr
          and btebk_kez = btebe_kez
          and btebe_elemid = bed_elemid
          and bed_ogid = 'TEXT'
          and bed_egid = eg_id
          and bed_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        union
        select distinct btebe_elemid ElementId,
            ben_text Benennung,
            bed_egid EGruppenId,
            eg_exklusiv Exklusiv,
            eg_pos EGruppenPosition
        from w_bildtaf, w_ben_gk, w_bed, w_eg, w_bte_bedelem
        where bildtaf_btnr = :btnr
          and bildtaf_bedkez is not null
          and bildtaf_btnr = btebe_btnr
          and bildtaf_bedkez = btebe_kez
          and btebe_elemid = bed_elemid
          and bed_ogid = 'TEXT'
          and bed_egid = eg_id
          and bed_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by EGruppenPosition, Benennung
        """;

    private static final RowMapper<PlateMasterDto> PLATE_MASTER_MAPPER = (rs, rowNum) ->
        PlateMasterDto.builder()
            .hg(rs.getString("HG"))
            .fg(rs.getString("FG"))
            .bildtafelNr(rs.getString("BildtafelNr"))
            .bildtafelArt(rs.getString("BildtafelArt"))
            .benennung(rs.getString("Benennung"))
            .bedKuerzel(rs.getString("BedKuerzel"))
            .vorhandenCp(rs.getString("VorhandenCP"))
            .lkz(rs.getString("Lkz"))
            .grafikId(rs.getString("GrafikId"))
            .modStamp(rs.getString("ModStamp"))
            .zubBtnr(rs.getString("ZubBtnr"))
            .build();

    private static final RowMapper<PlateMasterCpDto> PLATE_MASTER_CP_MAPPER = (rs, rowNum) ->
        PlateMasterCpDto.builder()
            .art(rs.getString("Art"))
            .datum(rs.getString("Datum"))
            .build();

    private static final RowMapper<RequirementSalaDto> REQUIREMENT_SALA_MAPPER = (rs, rowNum) ->
        RequirementSalaDto.builder()
            .elementId(rs.getString("ElementId"))
            .code(rs.getString("Code"))
            .benennung(rs.getString("Benennung"))
            .eGruppenId(rs.getString("EGruppenId"))
            .exklusiv(rs.getString("Exklusiv"))
            .saz(rs.getString("SAZ"))
            .eGruppenPosition(rs.getString("EGruppenPosition"))
            .primaNr(rs.getString("PrimaNr"))
            .build();

    private static final RowMapper<RequirementAflDto> REQUIREMENT_AFL_MAPPER = (rs, rowNum) ->
        RequirementAflDto.builder()
            .elementId(rs.getString("ElementId"))
            .code(rs.getString("Code"))
            .benennung(rs.getString("Benennung"))
            .eGruppenId(rs.getString("EGruppenId"))
            .exklusiv(rs.getString("Exklusiv"))
            .eGruppenPosition(rs.getString("EGruppenPosition"))
            .build();

    private static final RowMapper<RequirementTextDto> REQUIREMENT_TEXT_MAPPER = (rs, rowNum) ->
        RequirementTextDto.builder()
            .elementId(rs.getString("ElementId"))
            .benennung(rs.getString("Benennung"))
            .eGruppenId(rs.getString("EGruppenId"))
            .exklusiv(rs.getString("Exklusiv"))
            .eGruppenPosition(rs.getString("EGruppenPosition"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Loads plate master data for a plate number.
     *
     * @param btnr plate number
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return list of plate master entries
     */
    public List<PlateMasterDto> loadPlateMaster(String btnr, String iso, String regiso) {
        return jdbc.query(LOAD_BT_STAMM, Map.of("btnr", btnr, "iso", iso, "regiso", regiso), PLATE_MASTER_MAPPER);
    }

    /**
     * Loads change points for a plate number.
     *
     * @param btnr plate number
     * @return list of change points
     */
    public List<PlateMasterCpDto> loadPlateChangePoints(String btnr) {
        return jdbc.query(LOAD_BT_STAMM_CP, Map.of("btnr", btnr), PLATE_MASTER_CP_MAPPER);
    }

    /**
     * Loads SALA requirements for a plate.
     *
     * @param btnr plate number
     * @param mosp MOSP identifier
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return list of SALA requirements
     */
    public List<RequirementSalaDto> loadSalaRequirements(String btnr, String mosp, String iso, String regiso) {
        return jdbc.query(LOAD_BT_BEDINGUNGEN_SALA,
            Map.of("btnr", btnr, "mosp", mosp, "iso", iso, "regiso", regiso), REQUIREMENT_SALA_MAPPER);
    }

    /**
     * Loads AFL requirements for a plate.
     *
     * @param btnr plate number
     * @param mosp MOSP identifier
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return list of AFL requirements
     */
    public List<RequirementAflDto> loadAflRequirements(String btnr, String mosp, String iso, String regiso) {
        return jdbc.query(LOAD_BT_BEDINGUNGEN_AFL,
            Map.of("btnr", btnr, "mosp", mosp, "iso", iso, "regiso", regiso), REQUIREMENT_AFL_MAPPER);
    }

    /**
     * Loads text requirements for a plate.
     *
     * @param btnr plate number
     * @param mosp MOSP identifier
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return list of text requirements
     */
    public List<RequirementTextDto> loadTextRequirements(String btnr, String mosp, String iso, String regiso) {
        return jdbc.query(LOAD_BT_BEDINGUNGEN_TEXT,
            Map.of("btnr", btnr, "mosp", mosp, "iso", iso, "regiso", regiso), REQUIREMENT_TEXT_MAPPER);
    }
}
