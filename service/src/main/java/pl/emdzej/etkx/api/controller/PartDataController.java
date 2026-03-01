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
import pl.emdzej.etkx.dal.dto.parts.PartReplacementDto;
import pl.emdzej.etkx.dal.dto.parts.PartUsagePartSummaryDto;
import pl.emdzej.etkx.dal.dto.parts.PartUsageReductionUsageDto;
import pl.emdzej.etkx.dal.dto.parts.PartUsageSeriesDto;
import pl.emdzej.etkx.dal.dto.parts.PartUsageVehiclePartDto;
import pl.emdzej.etkx.dal.dto.parts.SetIndividualPartDto;
import pl.emdzej.etkx.dal.repository.parts.PartReplacementRepository;
import pl.emdzej.etkx.dal.repository.parts.PartUsageDesignationRepository;
import pl.emdzej.etkx.dal.repository.parts.PartUsagePartRepository;
import pl.emdzej.etkx.dal.repository.parts.PartUsageReductionRepository;
import pl.emdzej.etkx.dal.repository.parts.PartUsageVehicleRepository;
import pl.emdzej.etkx.dal.repository.parts.SetIndividualPartsRepository;

@RestController
@RequestMapping("/api/parts")
@Tag(name = "Part Data", description = "Part details, replacements, and usage")
@RequiredArgsConstructor
public class PartDataController {

    private final PartReplacementRepository partReplacementRepository;
    private final PartUsageDesignationRepository partUsageDesignationRepository;
    private final PartUsageVehicleRepository partUsageVehicleRepository;
    private final PartUsageReductionRepository partUsageReductionRepository;
    private final PartUsagePartRepository partUsagePartRepository;
    private final SetIndividualPartsRepository setIndividualPartsRepository;

    /**
     * Loads replacement (supersession) data for the provided part number.
     */
    @GetMapping("/{partNumber}/replacements")
    @Operation(summary = "Load supersession chain for a part number")
    @ApiResponse(responseCode = "200", description = "Replacement data loaded")
    public List<PartReplacementDto> loadReplacements(
        @Parameter(description = "Part number")
        @PathVariable String partNumber,
        @Parameter(description = "MOSP identifiers")
        @RequestParam List<String> mospids,
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso,
        @Parameter(description = "Reference date for performance filters")
        @RequestParam String datum,
        @Parameter(description = "Optional steering filter clause")
        @RequestParam(required = false) String steeringClause,
        @Parameter(description = "Optional data series filter clause")
        @RequestParam(required = false) String dataSeriesClause,
        @Parameter(description = "Optional TC performance clause")
        @RequestParam(required = false) String tcCheckClause
    ) {
        String normalizedIso = normalizeIso(iso);
        String hg = resolveHg(partNumber, normalizedIso, regiso);
        return partReplacementRepository.findPartReplacements(
            hg,
            mospids,
            normalizedIso,
            regiso,
            datum,
            steeringClause,
            dataSeriesClause,
            tcCheckClause,
            null
        );
    }

    /**
     * Retrieves usage data based on part designation for the provided part number.
     */
    @GetMapping("/{partNumber}/usage/designation")
    @Operation(summary = "Load usage by designation for a part number")
    @ApiResponse(responseCode = "200", description = "Designation usage loaded")
    public List<PartUsageSeriesDto> loadDesignationUsage(
        @Parameter(description = "Part number")
        @PathVariable String partNumber,
        @Parameter(description = "Catalog scope")
        @RequestParam String katalogumfang,
        @Parameter(description = "Region identifiers")
        @RequestParam List<String> regionen,
        @Parameter(description = "Brand identifier")
        @RequestParam String marke,
        @Parameter(description = "Product type")
        @RequestParam String produktart,
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso
    ) {
        String normalizedIso = normalizeIso(iso);
        return partUsageDesignationRepository.findModelSeriesByPartNumbers(
            List.of(partNumber),
            katalogumfang,
            regionen,
            marke,
            produktart,
            normalizedIso,
            regiso
        );
    }

    /**
     * Retrieves vehicle usage data for the provided part number.
     */
    @GetMapping("/{partNumber}/usage/vehicle")
    @Operation(summary = "Load usage by vehicle for a part number")
    @ApiResponse(responseCode = "200", description = "Vehicle usage loaded")
    public List<PartUsageVehiclePartDto> loadVehicleUsage(
        @Parameter(description = "Part number")
        @PathVariable String partNumber,
        @Parameter(description = "MOSP identifiers")
        @RequestParam List<String> mospids,
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso,
        @Parameter(description = "Reference date for performance filters")
        @RequestParam String datum,
        @Parameter(description = "Optional steering filter")
        @RequestParam(required = false) String lenkung,
        @Parameter(description = "Optional data series filter clause")
        @RequestParam(required = false) String dataSeriesClause,
        @Parameter(description = "Optional TC performance clause")
        @RequestParam(required = false) String tcCheckClause
    ) {
        String normalizedIso = normalizeIso(iso);
        String hg = resolveHg(partNumber, normalizedIso, regiso);
        if (lenkung != null && !lenkung.isBlank()) {
            return partUsageVehicleRepository.findPartsWithSteering(
                hg,
                lenkung,
                mospids,
                normalizedIso,
                regiso,
                datum,
                dataSeriesClause,
                tcCheckClause,
                null
            );
        }
        return partUsageVehicleRepository.findPartsWithoutSteering(
            hg,
            mospids,
            normalizedIso,
            regiso,
            datum,
            dataSeriesClause,
            tcCheckClause,
            null
        );
    }

    /**
     * Retrieves reduced usage data for the provided part number.
     */
    @GetMapping("/{partNumber}/usage/reduced")
    @Operation(summary = "Load reduced usage for a part number")
    @ApiResponse(responseCode = "200", description = "Reduced usage loaded")
    public List<PartUsageReductionUsageDto> loadReducedUsage(
        @Parameter(description = "Part number")
        @PathVariable String partNumber,
        @Parameter(description = "MOSP identifiers")
        @RequestParam List<String> mospids,
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso
    ) {
        return partUsageReductionRepository.findUsage(partNumber, mospids, normalizeIso(iso), regiso);
    }

    /**
     * Retrieves related part usage summaries for the provided part number.
     */
    @GetMapping("/{partNumber}/usage/parts")
    @Operation(summary = "Load related part usage data")
    @ApiResponse(responseCode = "200", description = "Related part usage loaded")
    public List<PartUsagePartSummaryDto> loadPartUsageSummary(
        @Parameter(description = "Part number")
        @PathVariable String partNumber,
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso
    ) {
        return partUsagePartRepository.findPart(partNumber, normalizeIso(iso), regiso);
    }

    /**
     * Retrieves individual parts for a set/kit part number.
     */
    @GetMapping("/{partNumber}/set-parts")
    @Operation(summary = "Load kit contents for a set part number")
    @ApiResponse(responseCode = "200", description = "Set parts loaded")
    public List<SetIndividualPartDto> loadSetParts(
        @Parameter(description = "Part number")
        @PathVariable String partNumber,
        @Parameter(description = "Brand identifier")
        @RequestParam String marke,
        @Parameter(description = "Product type")
        @RequestParam String produktart,
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso,
        @Parameter(description = "Catalog scope list")
        @RequestParam List<String> katalogumfaenge,
        @Parameter(description = "Reference date for performance filters")
        @RequestParam String datum,
        @Parameter(description = "Optional TC performance clause")
        @RequestParam(required = false) String tcCheckClause
    ) {
        String normalizedIso = normalizeIso(iso);
        return setIndividualPartsRepository.loadIndividualParts(
            partNumber,
            marke,
            produktart,
            normalizedIso,
            regiso,
            katalogumfaenge,
            datum,
            tcCheckClause,
            null
        );
    }

    /**
     * Resolves main group for the given part number when available.
     */
    private String resolveHg(String partNumber, String iso, String regiso) {
        return partUsagePartRepository.findPart(partNumber, iso, regiso)
            .stream()
            .map(PartUsagePartSummaryDto::getHg)
            .filter(hg -> hg != null && !hg.isBlank())
            .findFirst()
            .orElse(partNumber);
    }

    private static String normalizeIso(String iso) {
        return iso == null ? null : iso.toLowerCase(Locale.ROOT);
    }
}
