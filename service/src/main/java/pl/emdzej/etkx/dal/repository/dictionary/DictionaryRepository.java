package pl.emdzej.etkx.dal.repository.dictionary;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import pl.emdzej.etkx.dal.dto.dictionary.BodyStyleDto;
import pl.emdzej.etkx.dal.dto.dictionary.BodyTypeDto;
import pl.emdzej.etkx.dal.dto.dictionary.LanguageDto;
import pl.emdzej.etkx.dal.dto.dictionary.MarketDto;
import pl.emdzej.etkx.dal.entity.BodyType;

/**
 * Repository for dictionary/reference lookup tables.
 */
public interface DictionaryRepository extends Repository<BodyType, String> {

    @Query(value = """
        select b.bauart_bauart as code,
            t.ben_text as name
        from w_bauart b
        join w_ben_gk t on t.ben_textcode = b.bauart_textcode
        where t.ben_iso = :lang
          and t.ben_regiso = :regIso
        order by b.bauart_bauart
        """, nativeQuery = true)
    List<BodyTypeDto> findAllBodyTypes(@Param("lang") String lang, @Param("regIso") String regIso);

    @Query(value = """
        select m.marktetk_mkz as code,
            m.marktetk_lkz as countryCode,
            m.marktetk_isokz as isoCode,
            t.ben_text as name
        from w_markt_etk m
        join w_ben_gk t on t.ben_textcode = m.marktetk_textcode
        where t.ben_iso = :lang
          and t.ben_regiso = :regIso
        order by m.marktetk_mkz
        """, nativeQuery = true)
    List<MarketDto> findAllMarkets(@Param("lang") String lang, @Param("regIso") String regIso);

    @Query(value = """
        select s.sprache_iso as isoCode,
            t.ben_text as name
        from w_tl_sprache_bnb s
        join w_ben_gk t on t.ben_textcode = s.sprache_textcode
        where t.ben_iso = :lang
          and t.ben_regiso = :regIso
        order by s.sprache_iso
        """, nativeQuery = true)
    List<LanguageDto> findAllLanguages(@Param("lang") String lang, @Param("regIso") String regIso);

    @Query(value = """
        select b.baureihekar_baureihe as seriesCode,
            b.baureihekar_karosserie as bodyCode,
            b.baureihekar_grafikid as graphicId
        from w_baureihe_kar_thb b
        where b.baureihekar_baureihe = :seriesCode
        order by b.baureihekar_karosserie
        """, nativeQuery = true)
    List<BodyStyleDto> findBodyStylesBySeries(@Param("seriesCode") String seriesCode);
}
