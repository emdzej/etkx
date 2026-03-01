package pl.emdzej.etkx.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.emdzej.etkx.dal.dto.search.MarketDescriptionDto;
import pl.emdzej.etkx.dal.dto.search.PartByNumberDto;
import pl.emdzej.etkx.dal.dto.search.PartSearchLineDto;
import pl.emdzej.etkx.dal.dto.search.PlateSearchResultDto;
import pl.emdzej.etkx.dal.repository.search.PartSearchAssemblyRepository;
import pl.emdzej.etkx.dal.repository.search.PartSearchGeneralRepository;
import pl.emdzej.etkx.dal.repository.search.PartSearchValueLineRepository;
import pl.emdzej.etkx.dal.repository.search.PartSearchVehicleRepository;

@RestController
@RequestMapping("/api/parts/search")
@Tag(name = "Part Search", description = "Search parts by various criteria")
@RequiredArgsConstructor
public class PartSearchController {

    private final PartSearchGeneralRepository generalRepository;
    private final PartSearchVehicleRepository vehicleRepository;
    private final PartSearchAssemblyRepository assemblyRepository;
    private final PartSearchValueLineRepository valueLineRepository;

    /**
     * Loads market description details for a specific illustration plate.
     */
    @GetMapping
    @Operation(summary = "Load market description for an illustration plate")
    @ApiResponse(responseCode = "200", description = "Market description loaded")
    public List<MarketDescriptionDto> loadMarketDescription(
        @Parameter(description = "Illustration plate number")
        @RequestParam String btnr,
        @Parameter(description = "Product type")
        @RequestParam String produktart,
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso,
        @Parameter(description = "Zero-based page index")
        @RequestParam(required = false) Integer page,
        @Parameter(description = "Page size")
        @RequestParam(required = false) Integer size
    ) {
        List<MarketDescriptionDto> results = generalRepository.loadMarketDescription(btnr, produktart, iso, regiso);
        return paginate(results, page, size);
    }

    /**
     * Searches vehicle-specific parts by designation for a MOSP.
     */
    @GetMapping("/vehicle")
    @Operation(summary = "Search vehicle-specific parts by designation")
    @ApiResponse(responseCode = "200", description = "Vehicle part search results")
    public List<PartSearchLineDto> searchVehiclePartsByDesignation(
        @Parameter(description = "Model series identifier")
        @RequestParam String mosp,
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso,
        @Parameter(description = "Designation search string")
        @RequestParam String query,
        @Parameter(description = "Zero-based page index")
        @RequestParam(required = false) Integer page,
        @Parameter(description = "Page size")
        @RequestParam(required = false) Integer size
    ) {
        List<PartSearchLineDto> results = vehicleRepository.searchPartsByDesignation(
            mosp,
            iso,
            regiso,
            query,
            null,
            null
        );
        return paginate(results, page, size);
    }

    /**
     * Searches parts by part number across the entire catalog.
     */
    @GetMapping("/by-number")
    @Operation(summary = "Search parts by part number (global)")
    @ApiResponse(responseCode = "200", description = "Global part number search results")
    public List<PartByNumberDto> searchByPartNumber(
        @Parameter(description = "Part number or prefix")
        @RequestParam String partNumber,
        @Parameter(description = "ISO language code")
        @RequestParam(defaultValue = "EN") String iso
    ) {
        return generalRepository.searchByPartNumber(partNumber, iso);
    }

    /**
     * Searches assembly parts by designation.
     */
    @GetMapping("/assembly")
    @Operation(summary = "Search assembly parts by designation")
    @ApiResponse(responseCode = "200", description = "Assembly part search results")
    public List<PartSearchLineDto> searchAssemblyPartsByDesignation(
        @Parameter(description = "Brand identifier")
        @RequestParam String marke,
        @Parameter(description = "Product type")
        @RequestParam String produktart,
        @Parameter(description = "Catalog scope")
        @RequestParam String katalogumfang,
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso,
        @Parameter(description = "Designation search string")
        @RequestParam String query,
        @Parameter(description = "Zero-based page index")
        @RequestParam(required = false) Integer page,
        @Parameter(description = "Page size")
        @RequestParam(required = false) Integer size
    ) {
        List<PartSearchLineDto> results = assemblyRepository.searchPartsByDesignation(
            marke,
            produktart,
            katalogumfang,
            iso,
            regiso,
            query,
            null
        );
        return paginate(results, page, size);
    }

    /**
     * Retrieves value line illustration plates for a vehicle model series.
     */
    @GetMapping("/valueline")
    @Operation(summary = "List value line illustration plates")
    @ApiResponse(responseCode = "200", description = "Value line plates loaded")
    public List<PlateSearchResultDto> loadValueLinePlates(
        @Parameter(description = "Model series identifier")
        @RequestParam String mosp,
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso,
        @Parameter(description = "Zero-based page index")
        @RequestParam(required = false) Integer page,
        @Parameter(description = "Page size")
        @RequestParam(required = false) Integer size
    ) {
        List<PlateSearchResultDto> results = valueLineRepository.findValueLinePlates(mosp, iso, regiso);
        return paginate(results, page, size);
    }

    private static <T> List<T> paginate(List<T> results, Integer page, Integer size) {
        if (page == null && size == null) {
            return results;
        }
        int resolvedSize = size == null ? 50 : Math.max(size, 1);
        int resolvedPage = page == null ? 0 : Math.max(page, 0);
        int fromIndex = resolvedPage * resolvedSize;
        if (fromIndex >= results.size()) {
            return List.of();
        }
        int toIndex = Math.min(fromIndex + resolvedSize, results.size());
        return results.subList(fromIndex, toIndex);
    }
}
