package pl.emdzej.etkx.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.emdzej.etkx.dal.dto.dictionary.BodyStyleDto;
import pl.emdzej.etkx.dal.dto.dictionary.BodyTypeDto;
import pl.emdzej.etkx.dal.dto.dictionary.LanguageDto;
import pl.emdzej.etkx.dal.dto.dictionary.MarketDto;
import pl.emdzej.etkx.dal.repository.dictionary.DictionaryRepository;

@RestController
@RequestMapping("/api/dictionary")
@Tag(name = "Dictionary", description = "Reference data for dropdowns and filters")
@RequiredArgsConstructor
public class DictionaryController {

    private final DictionaryRepository dictionaryRepository;

    @GetMapping("/body-types")
    @Operation(summary = "List all body types")
    public List<BodyTypeDto> getBodyTypes(
        @Parameter(description = "ISO language code")
        @RequestParam(defaultValue = "en") String lang,
        @Parameter(description = "Regional ISO language code")
        @RequestParam(defaultValue = "  ") String regIso
    ) {
        return dictionaryRepository.findAllBodyTypes(lang, regIso);
    }

    @GetMapping("/markets")
    @Operation(summary = "List all markets")
    public List<MarketDto> getMarkets(
        @Parameter(description = "ISO language code")
        @RequestParam(defaultValue = "en") String lang,
        @Parameter(description = "Regional ISO language code")
        @RequestParam(defaultValue = "  ") String regIso
    ) {
        return dictionaryRepository.findAllMarkets(lang, regIso);
    }

    @GetMapping("/languages")
    @Operation(summary = "List all languages")
    public List<LanguageDto> getLanguages(
        @Parameter(description = "ISO language code")
        @RequestParam(defaultValue = "en") String lang,
        @Parameter(description = "Regional ISO language code")
        @RequestParam(defaultValue = "  ") String regIso
    ) {
        return dictionaryRepository.findAllLanguages(lang, regIso);
    }

    @GetMapping("/body-styles/{seriesCode}")
    @Operation(summary = "Body styles for a series")
    public List<BodyStyleDto> getBodyStyles(
        @Parameter(description = "Series code")
        @PathVariable String seriesCode,
        @Parameter(description = "ISO language code")
        @RequestParam(defaultValue = "en") String lang,
        @Parameter(description = "Regional ISO language code")
        @RequestParam(defaultValue = "  ") String regIso
    ) {
        return dictionaryRepository.findBodyStylesBySeries(seriesCode);
    }
}
