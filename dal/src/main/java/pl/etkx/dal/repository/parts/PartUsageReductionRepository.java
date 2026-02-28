package pl.etkx.dal.repository.parts;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.etkx.dal.dto.parts.PartUsageReductionPartDto;
import pl.etkx.dal.dto.parts.PartUsageReductionUsageDto;

/**
 * Repository for reduced part usage lookups.
 */
@Repository
@RequiredArgsConstructor
public class PartUsageReductionRepository {
    private static final String RETRIEVE_TEIL = """
        select teil_hauptgr HG, teil_untergrup UG, teil_sachnr Sachnummer, ben_text Ben, teil_benennzus Zusatz
        from w_teil, w_ben_gk
        where teil_sachnr = :sachnummer
          and teil_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        """;

    private static final String RETRIEVE_VERWENDUNG = """
        select distinct BR.ben_text Baureihe,
            fztyp_erwvbez Modell,
            KAR.ben_text Karosserie,
            fztyp_ktlgausf Region
        from w_btzeilen_verbauung, w_ben_gk BR, w_ben_gk KAR, w_baureihe, w_publben, w_fztyp
        where btzeilenv_sachnr = :sachnummer
          and btzeilenv_mospid IN (:mospids)
          and btzeilenv_mospid = fztyp_mospid
          and fztyp_sichtschutz = 'N'
          and fztyp_baureihe = baureihe_baureihe
          and baureihe_textcode = BR.ben_textcode
          and BR.ben_iso = :iso
          and BR.ben_regiso = :regiso
          and publben_art = 'K'
          and fztyp_karosserie = publben_bezeichnung
          and publben_textcode = KAR.ben_textcode
          and KAR.ben_iso = :iso
          and KAR.ben_regiso = :regiso
        order by Baureihe, Modell, Karosserie, Region
        """;

    private static final RowMapper<PartUsageReductionPartDto> PART_MAPPER = (rs, rowNum) ->
        PartUsageReductionPartDto.builder()
            .hg(rs.getString("HG"))
            .ug(rs.getString("UG"))
            .sachnummer(rs.getString("Sachnummer"))
            .ben(rs.getString("Ben"))
            .zusatz(rs.getString("Zusatz"))
            .build();

    private static final RowMapper<PartUsageReductionUsageDto> USAGE_MAPPER = (rs, rowNum) ->
        PartUsageReductionUsageDto.builder()
            .baureihe(rs.getString("Baureihe"))
            .modell(rs.getString("Modell"))
            .karosserie(rs.getString("Karosserie"))
            .region(rs.getString("Region"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Retrieves a part summary for reduced usage screens.
     */
    public List<PartUsageReductionPartDto> findPart(String sachnummer, String iso, String regiso) {
        return jdbc.query(RETRIEVE_TEIL, Map.of("sachnummer", sachnummer, "iso", iso, "regiso", regiso), PART_MAPPER);
    }

    /**
     * Retrieves reduced usage entries for a part.
     */
    public List<PartUsageReductionUsageDto> findUsage(String sachnummer, List<String> mospids, String iso, String regiso) {
        return jdbc.query(RETRIEVE_VERWENDUNG,
            Map.of("sachnummer", sachnummer, "mospids", mospids, "iso", iso, "regiso", regiso),
            USAGE_MAPPER);
    }
}
