package pl.emdzej.etkx.dal.repository.accessories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesInstallationInfoDto;

/**
 * Repository for accessories technical data (installation information).
 */
@Repository
@RequiredArgsConstructor
public class AccessoriesTechnicalRepository {
    private static final String FIND_INSTALLATION_INFO_BY_DIAGRAM = """
        select bildtafze_btnr DiagramNumber,
               bildtafze_art Type,
               bildtafze_baureihe Series,
               bildtafze_id InstallationId,
               bildtafze_einbauinfoid InstallationInfoId,
               einbauinfo_komplexitaet Complexity,
               einbauinfo_lesart ReadingType,
               einbauinfo_mechanisch Mechanical,
               einbauinfo_elektrisch Electrical,
               einbauinfo_programm Programming,
               einbauinfo_lack Paint,
               einbauinfo_gesamt Total
        from w_bildtafzub_einbauinfo
        left join w_einbauinfo on einbauinfo_id = bildtafze_einbauinfoid
        where bildtafze_btnr = :btnr
        """;

    private static final RowMapper<AccessoriesInstallationInfoDto> INSTALLATION_INFO_MAPPER = (rs, rowNum) ->
        AccessoriesInstallationInfoDto.builder()
            .diagramNumber(rs.getString("DiagramNumber"))
            .type(rs.getString("Type"))
            .series(rs.getString("Series"))
            .installationId(rs.getString("InstallationId"))
            .installationInfoId(getInteger(rs, "InstallationInfoId"))
            .complexity(rs.getString("Complexity"))
            .readingType(rs.getString("ReadingType"))
            .mechanical(rs.getString("Mechanical"))
            .electrical(rs.getString("Electrical"))
            .programming(rs.getString("Programming"))
            .paint(rs.getString("Paint"))
            .total(rs.getString("Total"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Loads installation information for an accessories diagram.
     *
     * @param diagramNumber accessories diagram number
     * @return installation information rows
     */
    public List<AccessoriesInstallationInfoDto> findInstallationInfoByDiagram(String diagramNumber) {
        return jdbc.query(
            FIND_INSTALLATION_INFO_BY_DIAGRAM,
            Map.of("btnr", diagramNumber),
            INSTALLATION_INFO_MAPPER
        );
    }

    private static Integer getInteger(ResultSet rs, String column) {
        try {
            int value = rs.getInt(column);
            return rs.wasNull() ? null : value;
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
