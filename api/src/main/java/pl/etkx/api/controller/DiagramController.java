package pl.etkx.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.etkx.dal.dto.admin.GraphicDto;
import pl.etkx.dal.dto.diagram.DiagramDetailsDto;
import pl.etkx.dal.dto.diagram.DiagramInfoDto;
import pl.etkx.dal.dto.diagram.DiagramLinesDto;
import pl.etkx.dal.dto.diagram.PartVisualizationResponseDto;
import pl.etkx.dal.dto.diagram.SpringTableResponseDto;
import pl.etkx.dal.repository.diagram.DiagramDisplayRepository;
import pl.etkx.dal.repository.diagram.DiagramInfoRepository;
import pl.etkx.dal.repository.diagram.PartVisualizationRepository;
import pl.etkx.dal.repository.diagram.SpringTableRepository;

@RestController
@RequestMapping("/api/diagrams")
@Tag(name = "Diagrams", description = "Parts diagrams and visualizations")
@RequiredArgsConstructor
public class DiagramController {

    private final DiagramDisplayRepository diagramDisplayRepository;
    private final DiagramInfoRepository diagramInfoRepository;
    private final PartVisualizationRepository partVisualizationRepository;
    private final SpringTableRepository springTableRepository;

    /**
     * Loads diagram hotspots, comments, references, and condition metadata.
     */
    @GetMapping("/{btnr}")
    @Operation(summary = "Load diagram details")
    @ApiResponse(responseCode = "200", description = "Diagram details loaded")
    public DiagramDetailsDto loadDiagramDetails(
        @Parameter(description = "Diagram number")
        @PathVariable String btnr,
        @Parameter(description = "Graphic identifier")
        @RequestParam(required = false) Long grafikId,
        @Parameter(description = "Graphic type")
        @RequestParam(defaultValue = "Z") String art,
        @Parameter(description = "Model column identifier (vehicle context)")
        @RequestParam(required = false) Long mosp,
        @Parameter(description = "Brand identifier (UGB context)")
        @RequestParam(required = false) String marke,
        @Parameter(description = "Product type (UGB context)")
        @RequestParam(required = false) String produktart,
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso
    ) {
        DiagramDetailsDto.DiagramDetailsDtoBuilder builder = DiagramDetailsDto.builder()
            .hotspots(grafikId != null ? diagramDisplayRepository.findHotspots(grafikId, art) : List.of())
            .yesNoTexts(diagramDisplayRepository.findYesNoTexts(iso, regiso));

        if (mosp != null) {
            builder.vehicleComments(diagramDisplayRepository.findVehicleComments(mosp, btnr, iso, regiso))
                .conditions(diagramDisplayRepository.findVehicleConditions(btnr, mosp))
                .overConditions(diagramDisplayRepository.findVehicleOverConditions(btnr, mosp))
                .references(diagramDisplayRepository.findVehicleReferences(btnr, mosp, iso, regiso))
                .ugbComments(List.of());
        } else if (StringUtils.hasText(marke) && StringUtils.hasText(produktart)) {
            builder.ugbComments(diagramDisplayRepository.findUgbComments(marke, btnr, iso, regiso))
                .references(diagramDisplayRepository.findUgbReferences(btnr, marke, produktart, iso, regiso))
                .vehicleComments(List.of())
                .conditions(List.of())
                .overConditions(List.of());
        } else {
            builder.vehicleComments(List.of())
                .ugbComments(List.of())
                .conditions(List.of())
                .overConditions(List.of())
                .references(List.of());
        }

        return builder.build();
    }

    /**
     * Loads parts list lines for a diagram.
     */
    @GetMapping("/{btnr}/lines")
    @Operation(summary = "Load diagram parts list")
    @ApiResponse(responseCode = "200", description = "Diagram lines loaded")
    public DiagramLinesDto loadDiagramLines(
        @Parameter(description = "Diagram number")
        @PathVariable String btnr,
        @Parameter(description = "Model column identifier (vehicle context)")
        @RequestParam(required = false) Long mosp,
        @Parameter(description = "Brand identifier")
        @RequestParam String marke,
        @Parameter(description = "Product type (UGB context)")
        @RequestParam(required = false) String produktart,
        @Parameter(description = "Catalog scope (UGB context)")
        @RequestParam(required = false) String katalogumfang,
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso,
        @Parameter(description = "Vehicle type (vehicle context)")
        @RequestParam(required = false) String typ,
        @Parameter(description = "Production date (YYYYMMDD)")
        @RequestParam Long datum,
        @Parameter(description = "Assembly plant (vehicle context)")
        @RequestParam(required = false) String werk,
        @Parameter(description = "Optional market code filter")
        @RequestParam(required = false) String landkuerzel
    ) {
        DiagramLinesDto.DiagramLinesDtoBuilder builder = DiagramLinesDto.builder();
        if (mosp != null) {
            if (!StringUtils.hasText(typ)) {
                throw new IllegalArgumentException("Vehicle context requires typ parameter");
            }
            builder.vehicleLines(diagramDisplayRepository.findVehicleDiagramLines(
                    mosp,
                    btnr,
                    marke,
                    iso,
                    regiso,
                    typ,
                    datum,
                    landkuerzel
                ))
                .cpLines(diagramDisplayRepository.findVehicleCpLines(mosp, btnr, typ, werk))
                .ugbLines(List.of());
        } else {
            if (!StringUtils.hasText(produktart) || !StringUtils.hasText(katalogumfang)) {
                throw new IllegalArgumentException("UGB context requires produktart and katalogumfang parameters");
            }
            builder.ugbLines(diagramDisplayRepository.findUgbDiagramLines(
                    marke,
                    btnr,
                    produktart,
                    katalogumfang,
                    iso,
                    regiso,
                    datum,
                    landkuerzel
                ))
                .vehicleLines(List.of())
                .cpLines(List.of());
        }
        return builder.build();
    }

    /**
     * Loads the diagram graphic as a binary payload.
     */
    @GetMapping("/{btnr}/image")
    @Operation(summary = "Load diagram graphic")
    @ApiResponse(responseCode = "200", description = "Diagram graphic loaded")
    public ResponseEntity<byte[]> loadDiagramImage(
        @Parameter(description = "Diagram number")
        @PathVariable String btnr,
        @Parameter(description = "Graphic type")
        @RequestParam(defaultValue = "Z") String art
    ) {
        GraphicDto graphic = diagramDisplayRepository.loadDiagramGraphic(btnr, art);
        return buildGraphicResponse(graphic);
    }

    /**
     * Loads diagram metadata such as titles and comments.
     */
    @GetMapping("/{btnr}/info")
    @Operation(summary = "Load diagram metadata")
    @ApiResponse(responseCode = "200", description = "Diagram metadata loaded")
    public DiagramInfoDto loadDiagramInfo(
        @Parameter(description = "Diagram number")
        @PathVariable String btnr,
        @Parameter(description = "Product type")
        @RequestParam String produktart,
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso
    ) {
        return DiagramInfoDto.builder()
            .title(diagramInfoRepository.findDiagramTitle(btnr, produktart, iso, regiso).orElse(null))
            .comments(diagramInfoRepository.findDiagramComments(btnr, iso, regiso))
            .build();
    }

    /**
     * Loads part visualization references for a part number.
     */
    @GetMapping("/part/{partNumber}")
    @Operation(summary = "Load part visualization references")
    @ApiResponse(responseCode = "200", description = "Part visualization data loaded")
    public PartVisualizationResponseDto loadPartVisualizations(
        @Parameter(description = "Part number")
        @PathVariable String partNumber,
        @Parameter(description = "Brand identifiers")
        @RequestParam List<String> marken,
        @Parameter(description = "Product types")
        @RequestParam List<String> produktarten,
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso
    ) {
        return PartVisualizationResponseDto.builder()
            .vehicleVisualizations(partVisualizationRepository.findVehicleVisualizations(
                partNumber,
                marken,
                produktarten,
                iso,
                regiso
            ))
            .ugbVisualizations(partVisualizationRepository.findUgbVisualizations(
                partNumber,
                marken,
                produktarten,
                iso,
                regiso
            ))
            .build();
    }

    /**
     * Loads spring table data for the provided filters.
     */
    @GetMapping("/springs")
    @Operation(summary = "Load spring table data")
    @ApiResponse(responseCode = "200", description = "Spring table data loaded")
    public SpringTableResponseDto loadSpringTable(
        @Parameter(description = "Vehicle types")
        @RequestParam List<String> typen,
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso,
        @Parameter(description = "Production date (YYYYMMDD)")
        @RequestParam(required = false) Long prodDatum,
        @Parameter(description = "SALA identifiers")
        @RequestParam(required = false) List<String> salaIds,
        @Parameter(description = "Spring table identifier")
        @RequestParam(required = false) Long ftId,
        @Parameter(description = "Spring axis type")
        @RequestParam(required = false) String art,
        @Parameter(description = "Points value")
        @RequestParam(required = false) Long punkte
    ) {
        String productionDateClause = null;
        Map<String, Object> extraParams = new HashMap<>();
        if (prodDatum != null) {
            productionDateClause = "and sft_gilt_v <= :prodDatum and (sft_gilt_b is null or sft_gilt_b >= :prodDatum)";
            extraParams.put("prodDatum", prodDatum);
        }

        List<String> resolvedSalaIds = CollectionUtils.isEmpty(salaIds) ? List.of() : salaIds;

        SpringTableResponseDto.SpringTableResponseDtoBuilder builder = SpringTableResponseDto.builder()
            .salas(springTableRepository.findSalas(typen, iso, regiso, productionDateClause, extraParams))
            .pointsByType(springTableRepository.findPointsByType(typen, productionDateClause, extraParams))
            .pointsBySala(resolvedSalaIds.isEmpty()
                ? List.of()
                : springTableRepository.findPointsBySala(typen, resolvedSalaIds, productionDateClause, extraParams));

        if (ftId != null) {
            builder.aspgKits(springTableRepository.findAspgKits(ftId));
            if (StringUtils.hasText(art) && punkte != null) {
                builder.leftSprings(springTableRepository.findLeftSprings(ftId, art, punkte))
                    .rightSprings(springTableRepository.findRightSprings(ftId, art, punkte));
            } else {
                builder.leftSprings(List.of())
                    .rightSprings(List.of());
            }
        } else {
            builder.aspgKits(List.of())
                .leftSprings(List.of())
                .rightSprings(List.of());
        }

        return builder.build();
    }

    private ResponseEntity<byte[]> buildGraphicResponse(GraphicDto graphic) {
        MediaType mediaType = resolveMediaType(graphic.getFormat());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setCacheControl(CacheControl.maxAge(Duration.ofDays(30)).cachePublic());
        return ResponseEntity.ok()
            .headers(headers)
            .body(graphic.getGrafik());
    }

    private MediaType resolveMediaType(String format) {
        if (!StringUtils.hasText(format)) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
        String normalized = format.trim().toUpperCase();
        return switch (normalized) {
            case "PNG" -> MediaType.IMAGE_PNG;
            case "JPG", "JPEG" -> MediaType.IMAGE_JPEG;
            case "TIF", "TIFF" -> MediaType.valueOf("image/tiff");
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }
}
