package com.etkx.controller;

import com.etkx.dto.PartDetailsDto;
import com.etkx.dto.PartSearchResultDto;
import com.etkx.dto.PartUsageDto;
import com.etkx.service.PartService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/parts")
@RequiredArgsConstructor
public class PartController {

    private final PartService partService;

    @GetMapping("/search")
    public List<PartSearchResultDto> searchParts(
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "hg", required = false) String hg,
            @RequestParam(name = "fg", required = false) String fg
    ) {
        return partService.searchParts(query, hg, fg);
    }

    @GetMapping("/{sachnr}")
    public PartDetailsDto getPartDetails(@PathVariable String sachnr) {
        return partService.getPartDetails(sachnr)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Part not found"));
    }

    @GetMapping("/{sachnr}/usage")
    public List<PartUsageDto> getPartUsage(@PathVariable String sachnr) {
        return partService.getPartUsage(sachnr);
    }
}
