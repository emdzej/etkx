package pl.emdzej.etkx.dal.repository.admin;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.emdzej.etkx.dal.dto.admin.TipDto;

/**
 * Repository for information tool data.
 */
@Repository
@RequiredArgsConstructor
public class InfoToolRepository {
    private static final String INSERT_USER_TIPP = """
        insert into w_user_tipps@etk_nutzer (usert_firma_id, usert_id, usert_tipp_id)
        table __TABLE__
        """;

    private static final String DELETE_USER_TIPP = """
        delete from w_user_tipps@etk_nutzer
        where usert_firma_id = :firmaId
          and usert_id = :userId
        """;

    private static final String LOAD_TIPPS_TRICKS = """
        select tipp_id Id,
            tipp_filename Filename,
            tipp_art Art,
            decode(usert_tipp_id, usert_tipp_id, 'J', 'N') Gelesen
        from w_tipp@etk_nutzer
            left join w_user_tipps@etk_nutzer on (
                usert_firma_id = :firmaId
                and usert_id = :userId
                and usert_tipp_id = tipp_id
            )
        where tipp_id > 0
          and tipp_wichtig = :wichtig
        order by tipp_pos
        """;

    private static final String COUNT_TIPPS_TRICKS = """
        select count(*) Anzahl
        from w_tipp@etk_nutzer
        where tipp_id > 0
        """;

    private static final String COUNT_TICKER = """
        select count(*) Anzahl
        from w_tipp@etk_nutzer
        where tipp_id < 0
        """;

    private static final RowMapper<TipDto> TIP_MAPPER = (rs, rowNum) ->
        TipDto.builder()
            .id(rs.getString("Id"))
            .filename(rs.getString("Filename"))
            .art(rs.getString("Art"))
            .gelesen(rs.getString("Gelesen"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Inserts user tip entries using the provided table name.
     *
     * @param tableName table name to insert from
     * @param params parameters for the insert
     * @return number of rows affected
     */
    public int insertUserTip(String tableName, Map<String, Object> params) {
        String sql = INSERT_USER_TIPP.replace("__TABLE__", tableName);
        return jdbc.update(sql, params);
    }

    /**
     * Deletes user tips for a given user.
     *
     * @param firmaId company identifier
     * @param userId user identifier
     * @return number of rows affected
     */
    public int deleteUserTip(String firmaId, String userId) {
        return jdbc.update(DELETE_USER_TIPP, Map.of("firmaId", firmaId, "userId", userId));
    }

    /**
     * Loads tips and tricks for a user.
     *
     * @param firmaId company identifier
     * @param userId user identifier
     * @param wichtig importance flag
     * @return list of tips
     */
    public List<TipDto> loadTips(String firmaId, String userId, String wichtig) {
        return jdbc.query(LOAD_TIPPS_TRICKS, Map.of("firmaId", firmaId, "userId", userId, "wichtig", wichtig),
            TIP_MAPPER);
    }

    /**
     * Counts the number of tips and tricks.
     *
     * @return count of tips
     */
    public Integer countTips() {
        return jdbc.queryForObject(COUNT_TIPPS_TRICKS, Map.of(), Integer.class);
    }

    /**
     * Counts the number of ticker entries.
     *
     * @return count of ticker entries
     */
    public Integer countTicker() {
        return jdbc.queryForObject(COUNT_TICKER, Map.of(), Integer.class);
    }
}
