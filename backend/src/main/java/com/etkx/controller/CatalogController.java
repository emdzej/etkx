package com.etkx.controller;

import com.etkx.domain.Graphic;
import com.etkx.dto.DiagramDto;
import com.etkx.dto.MainGroupDto;
import com.etkx.dto.SubGroupDto;
import com.etkx.service.CatalogService;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("/groups")
    public List<MainGroupDto> getMainGroups(@RequestParam(name = "produktart", required = false) String produktart) {
        return catalogService.getMainGroups(produktart);
    }

    @GetMapping("/groups/{hg}/subgroups")
    public List<SubGroupDto> getSubGroups(
            @PathVariable String hg,
            @RequestParam(name = "produktart", required = false) String produktart
    ) {
        return catalogService.getSubGroups(hg, produktart);
    }

    @GetMapping("/diagrams/{btnr}")
    public DiagramDto getDiagram(@PathVariable String btnr) {
        return catalogService.getDiagram(btnr)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Diagram not found"));
    }

    @GetMapping("/diagrams/{btnr}/image")
    public ResponseEntity<byte[]> getDiagramImage(@PathVariable String btnr) {
        Integer graphicId = catalogService.getDiagramGraphicId(btnr)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Diagram not found"));
        return catalogService.getDiagramImage(graphicId)
                .map(this::buildGraphicResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));
    }

    @GetMapping("/images/{grafikid}")
    public ResponseEntity<byte[]> getImageByGraphicId(@PathVariable Integer grafikid) {
        return catalogService.getDiagramImage(grafikid)
                .map(this::buildGraphicResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));
    }

    private ResponseEntity<byte[]> buildGraphicResponse(Graphic graphic) {
        MediaType mediaType = resolveMediaType(graphic.getFormat());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setCacheControl(CacheControl.maxAge(Duration.ofDays(30)).cachePublic());
        byte[] blob = graphic.getBlob();
        return new ResponseEntity<>(blob, headers, HttpStatus.OK);
    }

    private MediaType resolveMediaType(String format) {
        if (format == null) {
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
