package pl.etkx.dal.repository.search;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.etkx.dal.dto.search.PlateSearchResultDto;

/**
 * Repository for value line specific part searches.
 */
@Repository
@RequiredArgsConstructor
public class PartSearchValueLineRepository {
    private static final String RETRIEVE_BTES_VALUE_LINE = """
        select distinct bildtaf_hg BildtafelHG, bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt,
            ben_text Benennung, bildtaf_pos Pos, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden,
            bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso
        from w_hgfg, w_bildtaf left join w_markt_etk on (marktetk_lkz = bildtaf_lkz), w_ben_gk, w_bildtaf_suche
        where hgfg_ist_valueline = 'J'
          and bildtaf_hg = hgfg_hg
          and bildtaf_fg = hgfg_fg
          and bildtafs_btnr = bildtaf_btnr
          and bildtafs_hg = bildtaf_hg
          and bildtafs_mospid = :mosp
          and ben_textcode = bildtaf_textc
          and ben_iso = :iso
          and ben_regiso = :regiso
        union
        select distinct bildtaf_hg BildtafelHG, bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt,
            ben_text Benennung, bildtaf_pos Pos, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden,
            bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso
        from w_kompl_satz, w_btzeilen_verbauung, w_bildtaf left join w_markt_etk on (marktetk_lkz = bildtaf_lkz),
            w_ben_gk, w_bildtaf_suche
        where ks_ist_valueline = 'J'
          and ks_sachnr_satz = btzeilenv_sachnr
          and ks_hg = bildtaf_hg
          and btzeilenv_btnr = bildtaf_btnr
          and btzeilenv_mospid = bildtafs_mospid
          and bildtafs_btnr = bildtaf_btnr
          and bildtafs_hg = bildtaf_hg
          and bildtafs_mospid = :mosp
          and ben_textcode = bildtaf_textc
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by BildtafelHG, BildtafelNr
        """;

    private static final RowMapper<PlateSearchResultDto> PLATE_SEARCH_MAPPER = (rs, rowNum) ->
        PlateSearchResultDto.builder()
            .bildtafelHg(rs.getString("BildtafelHG"))
            .bildtafelNr(rs.getString("BildtafelNr"))
            .bildtafelArt(rs.getString("BildtafelArt"))
            .benennung(rs.getString("Benennung"))
            .pos(getInteger(rs, "Pos"))
            .kommentar(rs.getString("Kommentar"))
            .cpVorhanden(rs.getString("CPVorhanden"))
            .bedingungKz(rs.getString("BedingungKZ"))
            .marktIso(rs.getString("MarktIso"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Retrieves illustration plates for value line vehicles.
     *
     * @param mosp model series identifier
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return list of value line plates
     */
    public List<PlateSearchResultDto> findValueLinePlates(String mosp, String iso, String regiso) {
        return jdbc.query(RETRIEVE_BTES_VALUE_LINE, Map.of("mosp", mosp, "iso", iso, "regiso", regiso),
            PLATE_SEARCH_MAPPER);
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
