package com.etkx.service;

import com.etkx.domain.Baureihe;
import com.etkx.domain.VehicleType;
import com.etkx.dto.BodyDto;
import com.etkx.dto.SeriesDto;
import com.etkx.dto.VehicleDto;
import com.etkx.repository.BaureiheRepository;
import com.etkx.repository.PublbenRepository;
import com.etkx.repository.VehicleTypeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private static final String DEFAULT_LANGUAGE = "DE";

    private final BaureiheRepository baureiheRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final PublbenRepository publbenRepository;
    private final EntityManager entityManager;

    public List<SeriesDto> getSeries(String produktart) {
        if (!StringUtils.hasText(produktart)) {
            return Collections.emptyList();
        }
        String normalized = produktart.trim();
        return baureiheRepository.findDistinctBaureiheByProduktart(normalized).stream()
                .map(this::toSeriesDto)
                .collect(Collectors.toList());
    }

    public List<BodyDto> getBodies(String series) {
        if (!StringUtils.hasText(series)) {
            return Collections.emptyList();
        }
        String normalized = series.trim();
        return vehicleTypeRepository.findByFztypBaureihe(normalized).stream()
                .map(VehicleType::getFztypKarosserie)
                .filter(StringUtils::hasText)
                .distinct()
                .map(body -> BodyDto.builder().body(body).build())
                .collect(Collectors.toList());
    }

    public List<VehicleDto> getModels(String series, String body) {
        if (!StringUtils.hasText(series) || !StringUtils.hasText(body)) {
            return Collections.emptyList();
        }
        String normalizedSeries = series.trim();
        String normalizedBody = body.trim();
        return vehicleTypeRepository.findByFztypBaureiheAndFztypKarosserie(normalizedSeries, normalizedBody).stream()
                .map(this::toVehicleDto)
                .collect(Collectors.toList());
    }

    public Optional<VehicleDto> getVehicleByMospid(Integer mospid) {
        if (mospid == null) {
            return Optional.empty();
        }
        return vehicleTypeRepository.findByFztypMospid(mospid)
                .map(this::toVehicleDto);
    }

    public Optional<VehicleDto> decodeVin(String vin) {
        if (!StringUtils.hasText(vin)) {
            return Optional.empty();
        }
        String normalized = vin.trim().toUpperCase();
        if (normalized.length() < 7) {
            return Optional.empty();
        }

        String lastSeven = normalized.substring(normalized.length() - 7);
        String anf = lastSeven.substring(0, 2);
        String numericPart = lastSeven.substring(2);
        int serial;
        try {
            serial = Integer.parseInt(numericPart);
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }

        Query query = entityManager.createNativeQuery("""
                select fgstnr_mospid
                  from w_fgstnr
                 where fgstnr_anf = :anf
                   and cast(fgstnr_von as integer) <= :serial
                   and cast(fgstnr_bis as integer) >= :serial
                 limit 1
                """);
        query.setParameter("anf", anf);
        query.setParameter("serial", serial);

        List<?> result = query.getResultList();
        if (result.isEmpty()) {
            return Optional.empty();
        }
        Integer mospid = ((Number) result.get(0)).intValue();
        return getVehicleByMospid(mospid);
    }

    private SeriesDto toSeriesDto(Baureihe baureihe) {
        return SeriesDto.builder()
                .series(baureihe.getBaureiheBaureihe())
                .name(resolveName(baureihe.getBaureiheTextcode()))
                .build();
    }

    private VehicleDto toVehicleDto(VehicleType vehicleType) {
        return VehicleDto.builder()
                .mospid(vehicleType.getFztypMospid())
                .series(vehicleType.getFztypBaureihe())
                .body(vehicleType.getFztypKarosserie())
                .engine(vehicleType.getFztypMotor())
                .steering(vehicleType.getFztypLenkung())
                .transmission(vehicleType.getFztypGetriebe())
                .build();
    }

    private String resolveName(Integer textCode) {
        if (textCode == null) {
            return null;
        }
        return publbenRepository.findByPublbenTextcodeAndPublbenSprache(textCode, DEFAULT_LANGUAGE)
                .map(publben -> publben.getPublbenText())
                .orElse(null);
    }
}
