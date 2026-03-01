package pl.emdzej.etkx.dal.repository.catalog;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
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
            min(ben_text) Name,
            min(hgthb_grafikid) ThumbnailId
        from w_hgfg_mosp
        inner join w_hgfg on (
            hgfg_hg = hgfgm_hg
            and hgfg_fg = hgfgm_fg
        )
        inner join w_ben_gk on (
            hgfg_textcode = ben_textcode
            and ben_iso = :iso
        )
        left join w_hg_thumbnail on (hgthb_hg = hgfg_hg)
        where hgfgm_mospid = :mospId
        group by hgfgm_hg
        order by hgfgm_hg
        """;

    private static final String RETRIEVE_SUB_GROUPS = """
        select hgfgm_hg Hg,
            hgfgm_fg Fg,
            ben_text Name,
            fgthb_grafikid ThumbnailId,
            bt.btnr Btnr
        from w_hgfg_mosp
        inner join w_hgfg on (
            hgfg_hg = hgfgm_hg
            and hgfg_fg = hgfgm_fg
        )
        inner join w_ben_gk on (
            hgfg_textcode = ben_textcode
            and ben_iso = :iso
        )
        left join w_fg_thumbnail on (
            fgthb_hg = hgfg_hg
            and fgthb_fg = hgfg_fg
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
        order by hgfgm_fg
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

    private static Integer getInteger(java.sql.ResultSet rs, String column) {
        try {
            int value = rs.getInt(column);
            return rs.wasNull() ? null : value;
        } catch (java.sql.SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
