package pl.etkx.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.etkx.dal.dto.reference.AspgConnectorDto;
import pl.etkx.dal.dto.reference.AspgPartDto;
import pl.etkx.dal.dto.reference.EtkTextCommentDto;
import pl.etkx.dal.dto.reference.EtkTextEntryDto;
import pl.etkx.dal.dto.reference.FillQuantityDto;
import pl.etkx.dal.dto.reference.InitialStockMainGroupDto;
import pl.etkx.dal.dto.reference.InitialStockPartDto;
import pl.etkx.dal.dto.reference.NewsTextDto;
import pl.etkx.dal.dto.reference.StorageTimeMainGroupDto;
import pl.etkx.dal.dto.reference.StorageTimePartDto;
import pl.etkx.dal.dto.reference.UpholsteryCodeDto;
import pl.etkx.dal.dto.reference.ValueLinePartNumberDto;
import pl.etkx.dal.repository.reference.AspgRepository;
import pl.etkx.dal.repository.reference.EtkTextRepository;
import pl.etkx.dal.repository.reference.FillQuantitiesRepository;
import pl.etkx.dal.repository.reference.InitialStockRepository;
import pl.etkx.dal.repository.reference.NewsTextsRepository;
import pl.etkx.dal.repository.reference.StorageTimeRepository;
import pl.etkx.dal.repository.reference.UpholsteryCodeRepository;
import pl.etkx.dal.repository.reference.ValueLineRepository;

@RestController
@RequestMapping("/api/reference")
@Tag(name = "Reference Data", description = "System texts, codes, and reference tables")
@RequiredArgsConstructor
public class ReferenceDataController {

    private final EtkTextRepository etkTextRepository;
    private final UpholsteryCodeRepository upholsteryCodeRepository;
    private final FillQuantitiesRepository fillQuantitiesRepository;
    private final StorageTimeRepository storageTimeRepository;
    private final InitialStockRepository initialStockRepository;
    private final ValueLineRepository valueLineRepository;
    private final AspgRepository aspgRepository;
    private final NewsTextsRepository newsTextsRepository;

    /**
     * Loads ETK text entries.
     */
    @GetMapping(value = "/texts", params = {"!iso", "!regiso"})
    @Operation(summary = "Load ETK text entries")
    @ApiResponse(responseCode = "200", description = "ETK text entries loaded")
    public List<EtkTextEntryDto> loadEtkTexts() {
        return etkTextRepository.loadEtkTexts();
    }

    /**
     * Loads ETK text comments for the provided language.
     */
    @GetMapping(value = "/texts", params = {"iso", "regiso"})
    @Operation(summary = "Load ETK text comments")
    @ApiResponse(responseCode = "200", description = "ETK text comments loaded")
    public List<EtkTextCommentDto> loadEtkTextComments(
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso
    ) {
        return etkTextRepository.loadEtkTextComments(iso, regiso);
    }

    /**
     * Loads upholstery codes for the provided language.
     */
    @GetMapping("/upholstery")
    @Operation(summary = "Load upholstery codes")
    @ApiResponse(responseCode = "200", description = "Upholstery codes loaded")
    public List<UpholsteryCodeDto> loadUpholsteryCodes(
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso
    ) {
        return upholsteryCodeRepository.loadUpholsteryCodes(iso, regiso);
    }

    /**
     * Loads fill quantity reference data.
     */
    @GetMapping("/fill-quantities")
    @Operation(summary = "Load fill quantities")
    @ApiResponse(responseCode = "200", description = "Fill quantities loaded")
    public List<FillQuantityDto> loadFillQuantities(
        @Parameter(description = "Fill quantity types")
        @RequestParam List<String> types,
        @Parameter(description = "Language suffix for hint columns")
        @RequestParam String language,
        @Parameter(description = "Production date (YYYYMM)")
        @RequestParam Integer productionDate
    ) {
        return fillQuantitiesRepository.loadFillQuantities(types, language, productionDate);
    }

    /**
     * Loads available storage time main groups.
     */
    @GetMapping(value = "/storage-time", params = "!hg")
    @Operation(summary = "Load storage time main groups")
    @ApiResponse(responseCode = "200", description = "Storage time main groups loaded")
    public List<StorageTimeMainGroupDto> loadStorageTimeMainGroups(
        @Parameter(description = "Minimum main group value")
        @RequestParam(defaultValue = "00") String hgFrom,
        @Parameter(description = "Optional maximum main group value")
        @RequestParam(required = false) String hgTo,
        @Parameter(description = "Brand identifier")
        @RequestParam String marke,
        @Parameter(description = "Product type")
        @RequestParam String produktart
    ) {
        Map<String, Object> extraParams = new HashMap<>();
        String hgToClause = optionalClause("and teil_hauptgr <= :hgTo", hgTo, "hgTo", extraParams);
        return storageTimeRepository.loadMainGroups(hgFrom, hgToClause, marke, produktart, extraParams);
    }

    /**
     * Loads storage time parts for the provided main group.
     */
    @GetMapping(value = "/storage-time", params = "hg")
    @Operation(summary = "Load storage time parts")
    @ApiResponse(responseCode = "200", description = "Storage time parts loaded")
    public List<StorageTimePartDto> loadStorageTimeParts(
        @Parameter(description = "Main group identifier")
        @RequestParam String hg,
        @Parameter(description = "Brand identifier")
        @RequestParam String marke,
        @Parameter(description = "Product type")
        @RequestParam String produktart,
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso,
        @Parameter(description = "Catalog scope identifiers")
        @RequestParam List<String> katalogumfaenge,
        @Parameter(description = "Reference date for performance filters")
        @RequestParam String datum,
        @Parameter(description = "Optional TC performance clause")
        @RequestParam(required = false) String tcCheckClause
    ) {
        return storageTimeRepository.loadParts(
            hg,
            marke,
            produktart,
            iso,
            regiso,
            katalogumfaenge,
            datum,
            tcCheckClause,
            null
        );
    }

    /**
     * Loads available initial stock main groups.
     */
    @GetMapping(value = "/initial-stock", params = "!hg")
    @Operation(summary = "Load initial stock main groups")
    @ApiResponse(responseCode = "200", description = "Initial stock main groups loaded")
    public List<InitialStockMainGroupDto> loadInitialStockMainGroups(
        @Parameter(description = "MOSP identifiers")
        @RequestParam List<String> mospids,
        @Parameter(description = "Minimum main group value")
        @RequestParam(defaultValue = "00") String hgFrom,
        @Parameter(description = "Optional maximum main group value")
        @RequestParam(required = false) String hgTo,
        @Parameter(description = "Optional steering filter")
        @RequestParam(required = false) String lenkung
    ) {
        Map<String, Object> extraParams = new HashMap<>();
        String hgToClause = optionalClause("and ebs_hg <= :hgTo", hgTo, "hgTo", extraParams);
        String lenkungClause = optionalClause("and ebs_lenkung = :lenkung", lenkung, "lenkung", extraParams);
        return initialStockRepository.loadMainGroups(mospids, hgFrom, hgToClause, lenkungClause, extraParams);
    }

    /**
     * Loads initial stock parts for the provided main group.
     */
    @GetMapping(value = "/initial-stock", params = "hg")
    @Operation(summary = "Load initial stock parts")
    @ApiResponse(responseCode = "200", description = "Initial stock parts loaded")
    public List<InitialStockPartDto> loadInitialStockParts(
        @Parameter(description = "Main group identifier")
        @RequestParam String hg,
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
        @Parameter(description = "Optional TC performance clause")
        @RequestParam(required = false) String tcCheckClause
    ) {
        Map<String, Object> extraParams = new HashMap<>();
        String lenkungClause = optionalClause("and eb_lenkung = :lenkung", lenkung, "lenkung", extraParams);
        return initialStockRepository.loadParts(
            hg,
            mospids,
            iso,
            regiso,
            datum,
            lenkungClause,
            tcCheckClause,
            extraParams
        );
    }

    /**
     * Loads ValueLine part numbers.
     */
    @GetMapping("/valueline")
    @Operation(summary = "Load ValueLine part numbers")
    @ApiResponse(responseCode = "200", description = "ValueLine part numbers loaded")
    public List<ValueLinePartNumberDto> loadValueLineParts() {
        return valueLineRepository.loadPartNumbers();
    }

    /**
     * Loads ASPG parts for the provided part number and group.
     */
    @GetMapping(value = "/aspg", params = {"sachnr", "gruppe", "!iso", "!regiso"})
    @Operation(summary = "Load ASPG parts")
    @ApiResponse(responseCode = "200", description = "ASPG parts loaded")
    public List<AspgPartDto> loadAspgParts(
        @Parameter(description = "Part number")
        @RequestParam String sachnr,
        @Parameter(description = "Group code")
        @RequestParam String gruppe
    ) {
        return aspgRepository.loadAspgParts(sachnr, gruppe);
    }

    /**
     * Loads ASPG connector parts (Stecker) for the provided part number and group.
     */
    @GetMapping(value = "/aspg", params = {"sachnr", "gruppe", "iso", "regiso"})
    @Operation(summary = "Load ASPG connectors")
    @ApiResponse(responseCode = "200", description = "ASPG connector parts loaded")
    public List<AspgConnectorDto> loadAspgConnectors(
        @Parameter(description = "Part number")
        @RequestParam String sachnr,
        @Parameter(description = "Group code")
        @RequestParam String gruppe,
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso
    ) {
        return aspgRepository.loadConnectors(sachnr, gruppe, iso, regiso);
    }

    /**
     * Loads news texts for the provided brand and language.
     */
    @GetMapping("/news")
    @Operation(summary = "Load news texts")
    @ApiResponse(responseCode = "200", description = "News texts loaded")
    public List<NewsTextDto> loadNewsTexts(
        @Parameter(description = "Brand identifier")
        @RequestParam String marke,
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso
    ) {
        return newsTextsRepository.loadNewsTexts(marke, iso, regiso);
    }

    private static String optionalClause(String clause, String value, String paramName, Map<String, Object> params) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        params.put(paramName, value);
        return clause;
    }
}
