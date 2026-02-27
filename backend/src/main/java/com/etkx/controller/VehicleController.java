package com.etkx.controller;

import com.etkx.dto.BodyDto;
import com.etkx.dto.SeriesDto;
import com.etkx.dto.VehicleDto;
import com.etkx.service.VehicleService;
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
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping("/series")
    public List<SeriesDto> getSeries(@RequestParam(name = "produktart", required = false) String produktart) {
        return vehicleService.getSeries(produktart);
    }

    @GetMapping("/series/{series}/bodies")
    public List<BodyDto> getBodies(@PathVariable String series) {
        return vehicleService.getBodies(series);
    }

    @GetMapping("/series/{series}/bodies/{body}/models")
    public List<VehicleDto> getModels(@PathVariable String series, @PathVariable String body) {
        return vehicleService.getModels(series, body);
    }

    @GetMapping("/{mospid}")
    public VehicleDto getVehicleByMospid(@PathVariable Integer mospid) {
        return vehicleService.getVehicleByMospid(mospid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found"));
    }

    @GetMapping("/vin/{vin}")
    public VehicleDto decodeVin(@PathVariable String vin) {
        return vehicleService.decodeVin(vin)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found"));
    }
}
