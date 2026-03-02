package pl.emdzej.etkx.dal.repository.catalog;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.emdzej.etkx.dal.dto.catalog.DiagramSummaryDto;
import pl.emdzej.etkx.dal.dto.catalog.MainGroupDto;
import pl.emdzej.etkx.dal.dto.catalog.SubGroupDto;

/**
 * Repository for catalog navigation (main groups and subgroups).
 */
@Repository
@RequiredArgsConstructor
public class CatalogNavigationRepository {
    private static final String RETRIEVE_MAIN_GROUPS = """
        select vg.hg Hg,
            MIN(ben_text) Name,
            MIN(hgthb_grafikid) ThumbnailId
        from v_vehicle_groups vg
        inner join w_ben_gk on (vg.textcode = ben_textcode and ben_iso = :iso)
        inner join v_vehicle_types vt on (vt.mospid = vg.mospid)
        left join w_hg_thumbnail on (
            hgthb_hg = vg.hg 
            AND hgthb_produktart = vg.produktart
            AND hgthb_marke_tps = vt.marke
        )
        where vg.mospid = :mospId
          and vg.fg = '00'
        group by vg.hg
        order by vg.hg
        """;

    private static final String RETRIEVE_SUB_GROUPS = """
        select vg.hg Hg,
            vg.fg Fg,
            MIN(ben_text) Name,
            MIN(vg.grafikId) ThumbnailId,
            MIN(d.btnr) Btnr
        from v_vehicle_groups vg
        inner join w_ben_gk on (vg.textcode = ben_textcode and ben_iso = :iso)
        left join v_diagrams d on (d.hg = vg.hg and d.fg = vg.fg)
        where vg.mospid = :mospId
          and vg.hg = :hg
          and vg.fg != '00'
        group by vg.hg, vg.fg
        order by vg.fg
        """;

    private static final String RETRIEVE_DIAGRAMS = """
        select distinct d.btnr Btnr,
            d.grafikId GrafikId,
            ben_text Name
        from w_btzeilen_verbauung v
        inner join v_diagrams d on (v.btzeilenv_btnr = d.btnr)
        left join w_ben_gk on (d.textcode = ben_textcode and ben_iso = :iso and TRIM(ben_regiso) = '')
        where v.btzeilenv_mospid = :mospId
          and d.hg = :hg
          and d.fg = :fg
        order by d.btnr
        """;

    private static final RowMapper<MainGroupDto> MAIN_GROUP_MAPPER = (rs, rowNum) ->
        MainGroupDto.builder()
            .hg(rs.getString("Hg"))
            .name(rs.getString("Name"))
            .thumbnailId(getInteger(rs, "ThumbnailId"))
            .build();

    private static final RowMapper<SubGroupDto> SUB_GROUP_MAPPER = (rs, rowNum) ->
        SubGroupDto.builder()
            .hg(rs.getString("Hg"))
            .fg(rs.getString("Fg"))
            .name(rs.getString("Name"))
            .thumbnailId(getInteger(rs, "ThumbnailId"))
            .btnr(rs.getString("Btnr"))
            .build();

    private static final RowMapper<DiagramSummaryDto> DIAGRAM_SUMMARY_MAPPER = (rs, rowNum) ->
        DiagramSummaryDto.builder()
            .btnr(rs.getString("Btnr"))
            .grafikId(getInteger(rs, "GrafikId"))
            .name(rs.getString("Name"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Retrieves main groups for a model column (MOSP).
     */
    public List<MainGroupDto> findMainGroups(String mospId, String iso) {
        return jdbc.query(RETRIEVE_MAIN_GROUPS, Map.of("mospId", mospId, "iso", iso), MAIN_GROUP_MAPPER);
    }

    /**
     * Retrieves subgroups for a main group within a model column (MOSP).
     */
    public List<SubGroupDto> findSubGroups(String mospId, String hg, String iso) {
        return jdbc.query(RETRIEVE_SUB_GROUPS, Map.of("mospId", mospId, "hg", hg, "iso", iso), SUB_GROUP_MAPPER);
    }

    /**
     * Retrieves diagram summaries for a subgroup within a model column (MOSP).
     */
    public List<DiagramSummaryDto> findDiagrams(String mospId, String hg, String fg, String iso) {
        return jdbc.query(
            RETRIEVE_DIAGRAMS,
            Map.of("mospId", mospId, "hg", hg, "fg", fg, "iso", iso),
            DIAGRAM_SUMMARY_MAPPER
        );
    }

    private static Integer getInteger(java.sql.ResultSet rs, String column) {
        try {
            int value = rs.getInt(column);
            return rs.wasNull() ? null : value;
        } catch (java.sql.SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
