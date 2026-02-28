package pl.emdzej.etkx.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesAdminInfoDto;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesDetailsDto;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesDiagramDto;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesDiagramMarketingDto;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesDiagramSeriesDto;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesInstallationInfoDto;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesLineItemDto;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesMarketingGraphicDto;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesMarketingInfoDto;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesMarketingKeywordDto;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesMarketingProductDto;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesMarketingRelatedDto;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesMarketingTextDto;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesPriceDto;
import pl.emdzej.etkx.dal.dto.accessories.AccessoriesVariantMarketingDto;
import pl.emdzej.etkx.dal.repository.accessories.AccessoriesAdminRepository;
import pl.emdzej.etkx.dal.repository.accessories.AccessoriesMarketingRepository;
import pl.emdzej.etkx.dal.repository.accessories.AccessoriesPricingRepository;
import pl.emdzej.etkx.dal.repository.accessories.AccessoriesSearchRepository;
import pl.emdzej.etkx.dal.repository.accessories.AccessoriesTechnicalRepository;

@RestController
@RequestMapping("/api/accessories")
@Tag(name = "Accessories", description = "BMW accessories and add-ons")
@RequiredArgsConstructor
public class AccessoriesController {

    private final AccessoriesSearchRepository accessoriesSearchRepository;
    private final AccessoriesMarketingRepository accessoriesMarketingRepository;
    private final AccessoriesPricingRepository accessoriesPricingRepository;
    private final AccessoriesTechnicalRepository accessoriesTechnicalRepository;
    private final AccessoriesAdminRepository accessoriesAdminRepository;

    /**
     * Searches accessories diagrams for a series and catalog variant.
     */
    @GetMapping("/search")
    @Operation(summary = "Search accessories")
    @ApiResponse(responseCode = "200", description = "Accessories search results loaded")
    public List<AccessoriesDiagramSeriesDto> searchAccessories(
        @Parameter(description = "Vehicle series identifier")
        @RequestParam String series,
        @Parameter(description = "Catalog variant identifier")
        @RequestParam String catalogVariant
    ) {
        return accessoriesSearchRepository.findDiagramsBySeries(series, catalogVariant);
    }

    /**
     * Loads accessory diagram details and line items.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Load accessory details")
    @ApiResponse(responseCode = "200", description = "Accessory details loaded")
    public AccessoriesDetailsDto loadAccessoryDetails(
        @Parameter(description = "Accessory diagram number")
        @PathVariable("id") String diagramNumber
    ) {
        AccessoriesDiagramDto diagram = accessoriesSearchRepository.findDiagramByNumber(diagramNumber)
            .stream()
            .findFirst()
            .orElse(null);
        return AccessoriesDetailsDto.builder()
            .diagram(diagram)
            .lineItems(accessoriesSearchRepository.findLineItemsByDiagram(diagramNumber))
            .build();
    }

    /**
     * Loads marketing information for an accessories diagram.
     */
    @GetMapping("/{id}/marketing")
    @Operation(summary = "Load accessories marketing information")
    @ApiResponse(responseCode = "200", description = "Accessories marketing data loaded")
    public AccessoriesMarketingInfoDto loadMarketingInfo(
        @Parameter(description = "Accessory diagram number")
        @PathVariable("id") String diagramNumber,
        @Parameter(description = "ISO language code")
        @RequestParam String iso,
        @Parameter(description = "Regional ISO language code")
        @RequestParam String regiso,
        @Parameter(description = "Optional variant identifier")
        @RequestParam(required = false) Integer variantId
    ) {
        List<AccessoriesDiagramMarketingDto> diagramMarketing =
            accessoriesMarketingRepository.findDiagramMarketingLinks(diagramNumber);
        List<AccessoriesVariantMarketingDto> variantMarketing = variantId == null
            ? List.of()
            : accessoriesMarketingRepository.findVariantMarketingLinks(diagramNumber, variantId);

        Set<Integer> productIds = new HashSet<>();
        Set<ProductMarketKey> productMarkets = new HashSet<>();

        collectProductMarkets(diagramMarketing, productIds, productMarkets);
        collectVariantProductMarkets(variantMarketing, productIds, productMarkets);

        List<AccessoriesMarketingProductDto> products = productIds.isEmpty()
            ? List.of()
            : accessoriesMarketingRepository.findMarketingProducts(List.copyOf(productIds));

        List<AccessoriesMarketingTextDto> texts = new ArrayList<>();
        List<AccessoriesMarketingKeywordDto> keywords = new ArrayList<>();
        List<AccessoriesMarketingGraphicDto> graphics = new ArrayList<>();
        List<AccessoriesMarketingRelatedDto> relatedProducts = new ArrayList<>();

        for (ProductMarketKey key : productMarkets) {
            texts.addAll(accessoriesMarketingRepository.findMarketingTexts(key.productId(), key.marketId(), iso, regiso));
            keywords.addAll(accessoriesMarketingRepository.findMarketingKeywords(key.productId(), key.marketId(), iso, regiso));
            graphics.addAll(accessoriesMarketingRepository.findMarketingGraphics(key.productId(), key.marketId()));
            relatedProducts.addAll(accessoriesMarketingRepository.findRelatedMarketingProducts(key.productId(), key.marketId()));
        }

        return AccessoriesMarketingInfoDto.builder()
            .diagramMarketing(diagramMarketing)
            .variantMarketing(variantMarketing)
            .products(products)
            .texts(texts)
            .keywords(keywords)
            .graphics(graphics)
            .relatedProducts(relatedProducts)
            .build();
    }

    /**
     * Loads pricing information for accessories in a diagram.
     */
    @GetMapping("/{id}/pricing")
    @Operation(summary = "Load accessories pricing")
    @ApiResponse(responseCode = "200", description = "Accessories pricing loaded")
    public List<AccessoriesPriceDto> loadPricing(
        @Parameter(description = "Accessory diagram number")
        @PathVariable("id") String diagramNumber,
        @Parameter(description = "Company identifier")
        @RequestParam String companyId
    ) {
        List<String> partNumbers = accessoriesSearchRepository.findLineItemsByDiagram(diagramNumber)
            .stream()
            .map(AccessoriesLineItemDto::getPartNumber)
            .filter(StringUtils::hasText)
            .distinct()
            .toList();
        if (partNumbers.isEmpty()) {
            return List.of();
        }
        return accessoriesPricingRepository.findPricesByPartNumbers(companyId, partNumbers);
    }

    /**
     * Loads installation and technical information for a diagram.
     */
    @GetMapping("/{id}/technical")
    @Operation(summary = "Load accessories technical information")
    @ApiResponse(responseCode = "200", description = "Accessories technical data loaded")
    public List<AccessoriesInstallationInfoDto> loadTechnicalInfo(
        @Parameter(description = "Accessory diagram number")
        @PathVariable("id") String diagramNumber
    ) {
        return accessoriesTechnicalRepository.findInstallationInfoByDiagram(diagramNumber);
    }

    /**
     * Loads accessories administration data for variants.
     */
    @GetMapping("/admin")
    @Operation(summary = "Load accessories administration data")
    @ApiResponse(responseCode = "200", description = "Accessories administration data loaded")
    public AccessoriesAdminInfoDto loadAdminData(
        @Parameter(description = "Accessory diagram number")
        @RequestParam String diagramNumber,
        @Parameter(description = "Optional line position for variants")
        @RequestParam(required = false) Integer position
    ) {
        return AccessoriesAdminInfoDto.builder()
            .diagramVariants(accessoriesAdminRepository.findDiagramVariants(diagramNumber))
            .lineVariants(position == null
                ? List.of()
                : accessoriesAdminRepository.findLineVariants(diagramNumber, position))
            .build();
    }

    private static void collectProductMarkets(
        List<AccessoriesDiagramMarketingDto> links,
        Set<Integer> productIds,
        Set<ProductMarketKey> productMarkets
    ) {
        for (AccessoriesDiagramMarketingDto link : links) {
            addProductMarket(link.getProductId(), link.getMarketId(), productIds, productMarkets);
        }
    }

    private static void collectVariantProductMarkets(
        List<AccessoriesVariantMarketingDto> links,
        Set<Integer> productIds,
        Set<ProductMarketKey> productMarkets
    ) {
        for (AccessoriesVariantMarketingDto link : links) {
            addProductMarket(link.getProductId(), link.getMarketId(), productIds, productMarkets);
        }
    }

    private static void addProductMarket(
        Integer productId,
        Integer marketId,
        Set<Integer> productIds,
        Set<ProductMarketKey> productMarkets
    ) {
        if (productId != null) {
            productIds.add(productId);
            if (marketId != null) {
                productMarkets.add(new ProductMarketKey(productId, marketId));
            }
        }
    }

    private record ProductMarketKey(Integer productId, Integer marketId) {
    }
}
