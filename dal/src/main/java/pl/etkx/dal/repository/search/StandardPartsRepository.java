package pl.etkx.dal.repository.search;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pl.etkx.dal.dto.search.StandardPartDesignationDto;
import pl.etkx.dal.dto.search.StandardPartDetailDto;
import pl.etkx.dal.dto.search.StandardPartGroupGraphicDto;
import pl.etkx.dal.dto.search.StandardPartNumberGraphicDto;

/**
 * Repository for standard parts (norm parts) operations.
 */
@Repository
@RequiredArgsConstructor
public class StandardPartsRepository {
    private static final String RETRIEVE_BENENNUNGEN = """
        select distinct ben_text BENENNUNG, normteilben_textcode TEXTCODE
        from w_normteilben, w_ben_gk
        where normteilben_marke_tps = :marke
          and (normteilben_produktart = :produktart or normteilben_produktart = 'B')
          and normteilben_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by BENENNUNG
        """;

    private static final String RETRIEVE_TEILE_ZU_BENENNUNG = """
        select distinct ben_text BENENNUNG, teil_hauptgr HG, teil_untergrup UG, teil_sachnr SACHNUMMER,
            teil_benennzus ZUSATZ, nn_art NORMART, teil_normnummer NORMNUMMER, teil_produktart PRODUKTART,
            teilm_marke_tps MARKE, teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_teil
        inner join w_ben_gk on (teil_textcode = ben_textcode and ben_iso = :iso and ben_regiso = :regiso)
        inner join w_teil_marken on (teil_sachnr = teilm_sachnr and teilm_marke_tps = :marke)
        left join w_normnummer on (teil_normnummer = nn_nnid and teilm_marke_tps = nn_marke_tps)
        where (teil_produktart = :produktart or teil_produktart = 'B' or teil_produktart IS NULL)
          and (teil_hauptgr = '07' or (teil_hauptgr = '88' and teil_untergrup = '10'))
          and (teil_lkz = '   ' or teil_lkz IS NULL)
          and teil_technik IN ('0', '3', '4', '7')
          and (teil_dispo IN ('0', '2', '3', '4', '5') or (teil_dispo = '6' and teil_entfall_dat > :compareDate))
        """;

    private static final String RETRIEVE_TEILE_ZU_NORMNUMMER = """
        select distinct teil_hauptgr HG, teil_untergrup UG, teil_sachnr SACHNUMMER, ben_text BENENNUNG,
            teil_benennzus ZUSATZ, nn_art NORMART, teil_normnummer NORMNUMMER, teil_produktart PRODUKTART,
            teilm_marke_tps MARKE, teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_teil
        inner join w_ben_gk on (teil_textcode = ben_textcode and ben_iso = :iso and ben_regiso = :regiso)
        inner join w_teil_marken on (teil_sachnr = teilm_sachnr and teilm_marke_tps = :marke)
        left join w_normnummer on (teil_normnummer = nn_nnid and teilm_marke_tps = nn_marke_tps)
        where (teil_produktart = :produktart or teil_produktart = 'B' or teil_produktart IS NULL)
          and upper(teil_normnummer) = upper(:normnummer)
          and (teil_hauptgr = '07' or (teil_hauptgr = '88' and teil_untergrup = '10'))
          and (teil_lkz = '   ' or teil_lkz IS NULL)
          and teil_technik IN ('0', '3', '4', '7')
          and (teil_dispo IN ('0', '2', '3', '4', '5') or (teil_dispo = '6' and teil_entfall_dat > :compareDate))
        order by ZUSATZ, NORMNUMMER
        """;

    private static final String RETRIEVE_NORMNUMMERN_GRUPPEN = """
        select nng_nngid NUMMER, nng_grafikid GRAFIKID, nng_pos POS, grafik_moddate TS
        from w_normnummergruppe, w_grafik
        where nng_marke_tps = :marke
          and (nng_produktart = :produktart or nng_produktart = 'B')
          and nng_grafikid = grafik_grafikid
          and grafik_art = 'T'
        order by POS
        """;

    private static final String RETRIEVE_NORMNUMMERN = """
        select nn_nnid NUMMER, nn_art ART, nn_grafikid GRAFIKID, nn_pos POS, grafik_moddate TS
        from w_normnummer, w_grafik
        where nn_marke_tps = :marke
          and (nn_produktart = :produktart or nn_produktart = 'B')
          and nn_nngid = :normnummerngruppe
          and nn_grafikid = grafik_grafikid
          and grafik_art = 'T'
        order by POS
        """;

    private static final RowMapper<StandardPartDesignationDto> DESIGNATION_MAPPER = (rs, rowNum) ->
        StandardPartDesignationDto.builder()
            .benennung(rs.getString("BENENNUNG"))
            .textcode(rs.getString("TEXTCODE"))
            .build();

    private static final RowMapper<StandardPartDetailDto> STANDARD_PART_DETAIL_MAPPER = (rs, rowNum) ->
        StandardPartDetailDto.builder()
            .benennung(rs.getString("BENENNUNG"))
            .hg(rs.getString("HG"))
            .ug(rs.getString("UG"))
            .sachnummer(rs.getString("SACHNUMMER"))
            .zusatz(rs.getString("ZUSATZ"))
            .normart(rs.getString("NORMART"))
            .normnummer(rs.getString("NORMNUMMER"))
            .produktart(rs.getString("PRODUKTART"))
            .marke(rs.getString("MARKE"))
            .teilDiebstahlrelevant(rs.getString("Teil_Diebstahlrelevant"))
            .build();

    private static final RowMapper<StandardPartGroupGraphicDto> GROUP_GRAPHIC_MAPPER = (rs, rowNum) ->
        StandardPartGroupGraphicDto.builder()
            .nummer(rs.getString("NUMMER"))
            .grafikId(rs.getString("GRAFIKID"))
            .pos(getInteger(rs, "POS"))
            .ts(rs.getString("TS"))
            .build();

    private static final RowMapper<StandardPartNumberGraphicDto> NUMBER_GRAPHIC_MAPPER = (rs, rowNum) ->
        StandardPartNumberGraphicDto.builder()
            .nummer(rs.getString("NUMMER"))
            .art(rs.getString("ART"))
            .grafikId(rs.getString("GRAFIKID"))
            .pos(getInteger(rs, "POS"))
            .ts(rs.getString("TS"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Retrieves standard part designations for a brand and product type.
     */
    public List<StandardPartDesignationDto> findDesignations(
        String marke,
        String produktart,
        String iso,
        String regiso
    ) {
        return jdbc.query(RETRIEVE_BENENNUNGEN, Map.of("marke", marke, "produktart", produktart, "iso", iso, "regiso", regiso),
            DESIGNATION_MAPPER);
    }

    /**
     * Retrieves standard parts for a designation clause.
     */
    public List<StandardPartDetailDto> findPartsByDesignation(
        String marke,
        String produktart,
        String iso,
        String regiso,
        String compareDate,
        String textcodeClause
    ) {
        String sql = appendClause(RETRIEVE_TEILE_ZU_BENENNUNG + " and " + textcodeClause, "order by ZUSATZ, NORMNUMMER");
        return jdbc.query(sql,
            Map.of("marke", marke, "produktart", produktart, "iso", iso, "regiso", regiso, "compareDate", compareDate),
            STANDARD_PART_DETAIL_MAPPER);
    }

    /**
     * Retrieves standard parts for a norm number.
     */
    public List<StandardPartDetailDto> findPartsByNormNumber(
        String marke,
        String produktart,
        String iso,
        String regiso,
        String compareDate,
        String normnummer
    ) {
        return jdbc.query(RETRIEVE_TEILE_ZU_NORMNUMMER,
            Map.of("marke", marke, "produktart", produktart, "iso", iso, "regiso", regiso, "compareDate", compareDate, "normnummer", normnummer),
            STANDARD_PART_DETAIL_MAPPER);
    }

    /**
     * Retrieves norm number groups with their graphics.
     */
    public List<StandardPartGroupGraphicDto> findNormNumberGroups(String marke, String produktart) {
        return jdbc.query(RETRIEVE_NORMNUMMERN_GRUPPEN, Map.of("marke", marke, "produktart", produktart),
            GROUP_GRAPHIC_MAPPER);
    }

    /**
     * Retrieves norm numbers for a group including graphics.
     */
    public List<StandardPartNumberGraphicDto> findNormNumbers(String marke, String produktart, String normGroup) {
        return jdbc.query(RETRIEVE_NORMNUMMERN,
            Map.of("marke", marke, "produktart", produktart, "normnummerngruppe", normGroup),
            NUMBER_GRAPHIC_MAPPER);
    }

    private static Integer getInteger(java.sql.ResultSet rs, String column) {
        try {
            int value = rs.getInt(column);
            return rs.wasNull() ? null : value;
        } catch (java.sql.SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private static String appendClause(String base, String clause) {
        if (!StringUtils.hasText(clause)) {
            return base;
        }
        return base + " " + clause;
    }
}
