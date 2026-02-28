package pl.emdzej.etkx.dal.repository.reference;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.emdzej.etkx.dal.dto.reference.NewsTextDto;

/**
 * Repository for news text reference data.
 */
@Repository
@RequiredArgsConstructor
public class NewsTextsRepository {
    private static final String INSERT_NEWSTEXT = """
        insert into w_news_text (NEWST_MARKE_TPS, NEWST_ISO, NEWST_REGISO, NEWST_TEXT, NEWST_AKTUELL, NEWST_STANDARD)
        values (:marke, :iso, :regiso, :text, :aktuell, :standard)
        """;

    private static final String UPDATE_NEWSTEXT = """
        update w_news_text
        set NEWST_TEXT = :text,
            NEWST_AKTUELL = :aktuell,
            NEWST_STANDARD = :standard
        where NEWST_MARKE_TPS = :marke
          and NEWST_ISO = :iso
          and NEWST_REGISO = :regiso
        """;

    private static final String LOAD_NEWSTEXTE = """
        select NEWST_TEXT Text,
            NEWST_AKTUELL IsAktiviert,
            NEWST_STANDARD IsStandard
        from w_news_text
        where NEWST_MARKE_TPS = :marke
          and NEWST_ISO = :iso
          and NEWST_REGISO = :regiso
        order by NEWST_MARKE_TPS, NEWST_ISO, NEWST_REGISO, NEWST_STANDARD
        """;

    private static final String DELETE_NEWSTEXT = """
        delete from w_news_text
        where NEWST_MARKE_TPS = :marke
          and NEWST_ISO = :iso
          and NEWST_REGISO = :regiso
          and NEWST_TEXT = :text
        """;

    private static final String UPDATE_NEWSTEXT_AKTIVIERUNG = """
        update w_news_text
        set NEWST_AKTUELL = :aktuell
        where NEWST_MARKE_TPS = :marke
          and NEWST_ISO = :iso
          and NEWST_REGISO = :regiso
          and NEWST_TEXT = :text
        """;

    private static final String DELETE_IMAGE = """
        delete from w_news_grafik
        where newsg_marke_tps = :marke
        """;

    private static final String INSERT_IMAGE = """
        insert into w_news_grafik values (:marke, zeroblob(0))
        """;

    private static final String LOAD_IMAGE = """
        select newsg_grafik Grafik
        from w_news_grafik
        where newsg_marke_tps = :marke
        """;

    private static final String UPDATE_IMAGE = """
        select newsg_grafik Grafik
        from w_news_grafik
        where newsg_marke_tps = :marke
        for update
        """;

    private static final RowMapper<NewsTextDto> NEWS_TEXT_MAPPER = (rs, rowNum) ->
        NewsTextDto.builder()
            .text(rs.getString("Text"))
            .isAktiviert(rs.getString("IsAktiviert"))
            .isStandard(rs.getString("IsStandard"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Inserts a news text entry.
     *
     * @param marke brand code
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @param text news text content
     * @param aktuell activation flag
     * @param standard standard flag
     * @return number of rows affected
     */
    public int insertNewsText(String marke, String iso, String regiso, String text, String aktuell, String standard) {
        return jdbc.update(INSERT_NEWSTEXT, Map.of(
            "marke", marke,
            "iso", iso,
            "regiso", regiso,
            "text", text,
            "aktuell", aktuell,
            "standard", standard
        ));
    }

    /**
     * Updates a news text entry.
     *
     * @param marke brand code
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @param text news text content
     * @param aktuell activation flag
     * @param standard standard flag
     * @return number of rows affected
     */
    public int updateNewsText(String marke, String iso, String regiso, String text, String aktuell, String standard) {
        return jdbc.update(UPDATE_NEWSTEXT, Map.of(
            "marke", marke,
            "iso", iso,
            "regiso", regiso,
            "text", text,
            "aktuell", aktuell,
            "standard", standard
        ));
    }

    /**
     * Loads news texts for a given brand and language.
     *
     * @param marke brand code
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @return list of news texts
     */
    public List<NewsTextDto> loadNewsTexts(String marke, String iso, String regiso) {
        return jdbc.query(LOAD_NEWSTEXTE, Map.of("marke", marke, "iso", iso, "regiso", regiso), NEWS_TEXT_MAPPER);
    }

    /**
     * Deletes a news text entry.
     *
     * @param marke brand code
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @param text news text content
     * @return number of rows affected
     */
    public int deleteNewsText(String marke, String iso, String regiso, String text) {
        return jdbc.update(DELETE_NEWSTEXT, Map.of("marke", marke, "iso", iso, "regiso", regiso, "text", text));
    }

    /**
     * Updates activation flag for a news text entry.
     *
     * @param marke brand code
     * @param iso ISO language code
     * @param regiso regional ISO language code
     * @param text news text content
     * @param aktuell activation flag
     * @return number of rows affected
     */
    public int updateNewsTextActivation(String marke, String iso, String regiso, String text, String aktuell) {
        return jdbc.update(UPDATE_NEWSTEXT_AKTIVIERUNG, Map.of(
            "marke", marke,
            "iso", iso,
            "regiso", regiso,
            "text", text,
            "aktuell", aktuell
        ));
    }

    /**
     * Deletes the news image for a brand.
     *
     * @param marke brand code
     * @return number of rows affected
     */
    public int deleteImage(String marke) {
        return jdbc.update(DELETE_IMAGE, Map.of("marke", marke));
    }

    /**
     * Inserts an empty news image entry for a brand.
     *
     * @param marke brand code
     * @return number of rows affected
     */
    public int insertImage(String marke) {
        return jdbc.update(INSERT_IMAGE, Map.of("marke", marke));
    }

    /**
     * Loads the news image for a brand.
     *
     * @param marke brand code
     * @return image data as byte array
     */
    public byte[] loadImage(String marke) {
        return jdbc.queryForObject(LOAD_IMAGE, Map.of("marke", marke), (rs, rowNum) -> rs.getBytes("Grafik"));
    }

    /**
     * Locks and loads the news image for update.
     *
     * @param marke brand code
     * @return image data as byte array
     */
    public byte[] loadImageForUpdate(String marke) {
        return jdbc.queryForObject(UPDATE_IMAGE, Map.of("marke", marke), (rs, rowNum) -> rs.getBytes("Grafik"));
    }
}
