package pl.emdzej.etkx.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.emdzej.etkx.dal.dto.catalog.DiagramSummaryDto;
import pl.emdzej.etkx.dal.dto.catalog.MainGroupDto;
import pl.emdzej.etkx.dal.dto.catalog.SubGroupDto;
import pl.emdzej.etkx.dal.repository.catalog.CatalogNavigationRepository;

@RestController
@RequestMapping("/api/catalog")
@Tag(name = "Catalog", description = "Catalog navigation (groups and subgroups)")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogNavigationRepository catalogNavigationRepository;

    /**
     * Loads main groups for a model column.
     */
    @GetMapping("/groups")
    @Operation(summary = "Load main groups for a model column")
    @ApiResponse(responseCode = "200", description = "Main groups loaded")
    public List<MainGroupDto> getMainGroups(
        @Parameter(description = "Model column identifier (MOSP)")
        @RequestParam String mospId,
        @Parameter(description = "ISO language code")
        @RequestParam(defaultValue = "en") String iso
    ) {
        return catalogNavigationRepository.findMainGroups(mospId, normalizeIso(iso));
    }

    /**
     * Loads subgroups for a main group.
     */
    @GetMapping("/groups/{hg}/subgroups")
    @Operation(summary = "Load subgroups for a main group")
    @ApiResponse(responseCode = "200", description = "Subgroups loaded")
    public List<SubGroupDto> getSubGroups(
        @Parameter(description = "Model column identifier (MOSP)")
        @RequestParam String mospId,
        @Parameter(description = "Main group identifier")
        @PathVariable String hg,
        @Parameter(description = "ISO language code")
        @RequestParam(defaultValue = "en") String iso
    ) {
        return catalogNavigationRepository.findSubGroups(mospId, hg, normalizeIso(iso));
    }

    /**
     * Loads diagrams for a subgroup.
     */
    @GetMapping("/diagrams")
    @Operation(summary = "Load diagrams for a subgroup")
    @ApiResponse(responseCode = "200", description = "Diagrams loaded")
    public List<DiagramSummaryDto> getDiagrams(
        @Parameter(description = "Model column identifier (MOSP)")
        @RequestParam String mospId,
        @Parameter(description = "Main group identifier")
        @RequestParam String hg,
        @Parameter(description = "Subgroup identifier")
        @RequestParam String fg,
        @Parameter(description = "ISO language code")
        @RequestParam(defaultValue = "en") String iso
    ) {
        return catalogNavigationRepository.findDiagrams(mospId, hg, fg, normalizeIso(iso));
    }

    private static String normalizeIso(String iso) {
        return iso == null ? null : iso.toLowerCase(Locale.ROOT);
    }
}
