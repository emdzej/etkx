package pl.emdzej.etkx.api.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.emdzej.etkx.dal.dto.vehicle.BaureiheDto;
import pl.emdzej.etkx.dal.dto.vehicle.ConfigurationDto;
import pl.emdzej.etkx.dal.dto.vehicle.InterpretationDetailDto;
import pl.emdzej.etkx.dal.dto.vehicle.MospIdDto;
import pl.emdzej.etkx.dal.dto.vehicle.TypDto;
import pl.emdzej.etkx.dal.dto.vehicle.VehicleIdentificationDto;
import pl.emdzej.etkx.dal.repository.vehicle.ConfigurationRepository;
import pl.emdzej.etkx.dal.repository.vehicle.InterpretationRepository;
import pl.emdzej.etkx.dal.repository.vehicle.VehicleIdentificationRepository;
import pl.emdzej.etkx.dal.repository.vehicle.VehicleScopeRepository;

@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "Vehicles", description = "Vehicle identification and configuration")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleIdentificationRepository vehicleIdentificationRepository;
    private final VehicleScopeRepository vehicleScopeRepository;
    private final InterpretationRepository interpretationRepository;
    private final ConfigurationRepository configurationRepository;

    /**
     * Lists model series filtered by brand, product type, and catalog scope.
     */
    @GetMapping("/series")
    @Operation(summary = "List model series")
    @ApiResponse(responseCode = "200", description = "Model series loaded")
    public List<BaureiheDto> listSeries(
        @Parameter(description = "Brand identifier")
        @RequestParam String marke,
        @Parameter(description = "Product type")
        @RequestParam String produktart,
        @Parameter(description = "Catalog scope")
        @RequestParam String katalogumfang,
        @Parameter(description = "Region identifiers")
        @RequestParam List<String> regionen,
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso,
        @Parameter(description = "Optional construction type filter")
        @RequestParam(required = false) String bauart,
        @Parameter(description = "Optional steering filter")
        @RequestParam(required = false) String lenkung
    ) {
        Map<String, Object> extraParams = new HashMap<>();
        String bauartClause = optionalClause("and baureihe_bauart = :bauart", bauart, "bauart", extraParams);
        String lenkungClause = optionalClause("and fztyp_lenkung = :lenkung", lenkung, "lenkung", extraParams);
        return vehicleIdentificationRepository.findSeries(
            marke,
            produktart,
            katalogumfang,
            regionen,
            iso,
            regiso,
            bauartClause,
            lenkungClause,
            extraParams
        );
    }

    /**
     * Lists vehicle types for the provided model column.
     */
    @GetMapping("/types")
    @Operation(summary = "List vehicle types")
    @ApiResponse(responseCode = "200", description = "Vehicle types loaded")
    public List<TypDto> listTypes(
        @Parameter(description = "Model column identifier")
        @RequestParam String mospId,
        @Parameter(description = "Production date (YYYYMMDD)")
        @RequestParam String prodDatum,
        @Parameter(description = "Product type; use M for motorcycles")
        @RequestParam(required = false) String produktart,
        @Parameter(description = "Steering option (required for passenger cars)")
        @RequestParam(required = false) String lenkung,
        @Parameter(description = "Transmission option (required for passenger cars)")
        @RequestParam(required = false) String getriebe
    ) {
        if ("M".equalsIgnoreCase(produktart)) {
            return vehicleIdentificationRepository.findTypesForMotorcycles(mospId, prodDatum);
        }
        return vehicleIdentificationRepository.findTypesForPassengerCars(mospId, lenkung, getriebe, prodDatum);
    }

    /**
     * Identifies vehicles based on VIN.
     */
    @GetMapping("/identify")
    @Operation(summary = "Identify vehicle by VIN")
    @ApiResponse(responseCode = "200", description = "Vehicle identification loaded")
    public List<VehicleIdentificationDto> identifyVehicle(
        @Parameter(description = "Vehicle identification number")
        @RequestParam String vin,
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso
    ) {
        String prefix = vin.length() >= 2 ? vin.substring(0, 2) : vin;
        return vehicleIdentificationRepository.findByVin(vin, prefix, iso, regiso);
    }

    /**
     * Loads available model columns for the provided scope filters.
     */
    @GetMapping("/{mospId}/scope")
    @Operation(summary = "Load vehicle scope selection")
    @ApiResponse(responseCode = "200", description = "Vehicle scope loaded")
    public List<MospIdDto> loadVehicleScope(
        @Parameter(description = "Model column identifier")
        @PathVariable String mospId,
        @Parameter(description = "Model series identifiers")
        @RequestParam List<String> baureihen,
        @Parameter(description = "Catalog scope")
        @RequestParam String katalogumfang,
        @Parameter(description = "Region identifiers")
        @RequestParam List<String> regionen,
        @Parameter(description = "Optional model filter")
        @RequestParam(required = false) String modell,
        @Parameter(description = "Optional construction type filter")
        @RequestParam(required = false) String bauart,
        @Parameter(description = "Optional body type filter")
        @RequestParam(required = false) String karosserie,
        @Parameter(description = "Optional steering filter")
        @RequestParam(required = false) String lenkung
    ) {
        Map<String, Object> extraParams = new HashMap<>();
        String modellClause = buildClause(
            List.of(
                optionalClause("and fztyp_mospid = :mospid", mospId, "mospid", extraParams),
                optionalClause("and fztyp_erwvbez = :modell", modell, "modell", extraParams)
            )
        );
        String bauartClause = optionalClause("and baureihe_bauart = :bauart", bauart, "bauart", extraParams);
        String karosserieClause = optionalClause("and fztyp_karosserie = :karosserie", karosserie, "karosserie", extraParams);
        String lenkungClause = optionalClause("and fztyp_lenkung = :lenkung", lenkung, "lenkung", extraParams);
        return vehicleScopeRepository.findModelColumns(
            baureihen,
            katalogumfang,
            regionen,
            modellClause,
            bauartClause,
            karosserieClause,
            lenkungClause,
            extraParams
        );
    }

    /**
     * Decodes interpretation details for a part number.
     */
    @GetMapping("/interpret")
    @Operation(summary = "Decode interpretation codes")
    @ApiResponse(responseCode = "200", description = "Interpretation details loaded")
    public List<InterpretationDetailDto> interpretCodes(
        @Parameter(description = "Part number to decode")
        @RequestParam String sachnummer,
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso,
        @Parameter(description = "Optional brand filters")
        @RequestParam(required = false) List<String> marken
    ) {
        if (marken != null && !marken.isEmpty()) {
            if (interpretationRepository.findPartBrands(sachnummer, marken).isEmpty()) {
                return List.of();
            }
        }
        return interpretationRepository.loadInterpretationDetails(sachnummer, iso, regiso);
    }

    /**
     * Loads configuration settings for a company branch.
     */
    @GetMapping("/config")
    @Operation(summary = "Load system configuration")
    @ApiResponse(responseCode = "200", description = "Configuration loaded")
    public List<ConfigurationDto> loadConfiguration(
        @Parameter(description = "Company identifier")
        @RequestParam String firma,
        @Parameter(description = "Branch identifier")
        @RequestParam String filiale
    ) {
        return configurationRepository.loadConfiguration(firma, filiale);
    }

    private static String optionalClause(String clause, String value, String paramName, Map<String, Object> params) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        params.put(paramName, value);
        return clause;
    }

    private static String buildClause(List<String> clauses) {
        return clauses.stream()
            .filter(StringUtils::hasText)
            .reduce("", (left, right) -> left + " " + right)
            .trim();
    }
}
