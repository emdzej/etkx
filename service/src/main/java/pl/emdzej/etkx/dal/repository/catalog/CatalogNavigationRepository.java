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
        select hgfgm_hg Hg,
            MIN(ben_text) Name,
            MIN(hgthb_grafikid) ThumbnailId
        from w_hgfg_mosp m
        inner join w_hgfg on (
            hgfg_hg = hgfgm_hg
            and hgfg_fg = '00'
        )
        inner join w_ben_gk on (
            hgfg_textcode = ben_textcode
            and ben_iso = :iso and ben_regiso = ''
        )
        inner join w_fztyp f on (f.fztyp_mospid = m.hgfgm_mospid)
        inner join w_baureihe b on (f.fztyp_baureihe = b.baureihe_baureihe)
        left join w_hg_thumbnail on (
            hgthb_hg = hgfgm_hg 
            AND hgthb_produktart = hgfgm_produktart
            AND hgthb_marke_tps = b.baureihe_marke_tps
        )
        where m.hgfgm_mospid = :mospId
        group by hgfgm_hg
        order by hgfgm_hg
        """;

    private static final String RETRIEVE_SUB_GROUPS = """
        select hgfgm_hg Hg,
            hgfgm_fg Fg,
            MIN(ben_text) Name,
            (
                select MIN(bildtaf_grafikid)
                from w_bildtaf
                where bildtaf_hg = hgfgm_hg
                  and bildtaf_fg = hgfgm_fg
            ) ThumbnailId,
            bt.btnr Btnr
        from w_hgfg_mosp
        inner join w_hgfg on (
            hgfg_hg = hgfgm_hg
            and hgfg_fg = hgfgm_fg
        )
        inner join w_ben_gk on (
            hgfg_textcode = ben_textcode
            and ben_iso = :iso and ben_regiso = ''
        )
        left join (
            select bildtaf_hg, bildtaf_fg, min(bildtaf_btnr) as btnr
            from w_bildtaf
            group by bildtaf_hg, bildtaf_fg
        ) bt on (
            bt.bildtaf_hg = hgfgm_hg
            and bt.bildtaf_fg = hgfgm_fg
        )
        where hgfgm_mospid = :mospId
          and hgfgm_hg = :hg
        group by hgfgm_hg, hgfgm_fg
        order by hgfgm_fg
        """;

    private static final String RETRIEVE_DIAGRAMS = """
        select distinct b.bildtaf_btnr Btnr,
            b.bildtaf_grafikid GrafikId,
            ben_text Name
        from w_btzeilen_verbauung v
        inner join w_bildtaf b on (v.btzeilenv_btnr = b.bildtaf_btnr)
        left join w_ben_gk on (
            b.bildtaf_textc = ben_textcode
            and ben_iso = :iso and ben_regiso = ''
        )
        where v.btzeilenv_mospid = :mospId
          and b.bildtaf_hg = :hg
          and b.bildtaf_fg = :fg
        order by b.bildtaf_btnr
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
