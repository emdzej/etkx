package com.etkx.service;

import com.etkx.domain.Part;
import com.etkx.dto.PartDetailsDto;
import com.etkx.dto.PartSearchResultDto;
import com.etkx.dto.PartUsageDto;
import com.etkx.dto.SupersessionDto;
import com.etkx.dto.SupersessionItemDto;
import com.etkx.repository.PartRepository;
import com.etkx.repository.PublbenRepository;
import com.etkx.repository.TeileersetzungRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class PartService {

    private static final String DEFAULT_LANGUAGE = "DE";

    private final PartRepository partRepository;
    private final PublbenRepository publbenRepository;
    private final TeileersetzungRepository teileersetzungRepository;
    private final EntityManager entityManager;

    public List<PartSearchResultDto> searchParts(String query, String hg, String fg) {
        String normalizedQuery = normalize(query);
        String normalizedHg = normalize(hg);
        String normalizedFg = normalize(fg);

        List<Part> parts;
        if (StringUtils.hasText(normalizedQuery)) {
            parts = partRepository.findByTeilSachnrContaining(normalizedQuery);
        } else if (StringUtils.hasText(normalizedHg) && StringUtils.hasText(normalizedFg)) {
            parts = partRepository.findByTeilHauptgrAndTeilUntergrup(normalizedHg, normalizedFg);
        } else if (StringUtils.hasText(normalizedHg)) {
            parts = partRepository.findByTeilHauptgr(normalizedHg);
        } else {
            parts = Collections.emptyList();
        }

        if (StringUtils.hasText(normalizedHg)) {
            parts = parts.stream()
                    .filter(part -> normalizedHg.equals(part.getTeilHauptgr()))
                    .toList();
        }
        if (StringUtils.hasText(normalizedFg)) {
            parts = parts.stream()
                    .filter(part -> normalizedFg.equals(part.getTeilUntergrup()))
                    .toList();
        }

        return parts.stream()
                .map(part -> PartSearchResultDto.builder()
                        .sachnr(part.getTeilSachnr())
                        .name(resolveName(part.getTeilTextcode()))
                        .mainGroup(part.getTeilHauptgr())
                        .subGroup(part.getTeilUntergrup())
                        .build())
                .collect(Collectors.toList());
    }

    public Optional<PartDetailsDto> getPartDetails(String sachnr) {
        if (!StringUtils.hasText(sachnr)) {
            return Optional.empty();
        }
        return partRepository.findById(sachnr)
                .map(part -> PartDetailsDto.builder()
                        .sachnr(part.getTeilSachnr())
                        .name(resolveName(part.getTeilTextcode()))
                        .mainGroup(part.getTeilHauptgr())
                        .subGroup(part.getTeilUntergrup())
                        .textCode(part.getTeilTextcode())
                        .descriptionSuffix(part.getTeilBenennzus())
                        .partType(part.getTeilArt())
                        .build());
    }

    public List<PartUsageDto> getPartUsage(String sachnr) {
        if (!StringUtils.hasText(sachnr)) {
            return Collections.emptyList();
        }

        Query query = entityManager.createNativeQuery("""
                select b.btzeilen_btnr as diagramNumber,
                       b.btzeilen_bildposnr as position,
                       d.bildtaf_hg as mainGroup,
                       d.bildtaf_fg as subGroup
                  from w_btzeilen b
                  left join w_bildtaf d on d.bildtaf_btnr = b.btzeilen_btnr
                 where b.btzeilen_sachnr = :sachnr
                 order by b.btzeilen_btnr, b.btzeilen_bildposnr
                """);
        query.setParameter("sachnr", sachnr);

        List<?> rows = query.getResultList();
        return rows.stream()
                .map(row -> (Object[]) row)
                .map(columns -> PartUsageDto.builder()
                        .diagramNumber(stringValue(columns[0]))
                        .position(stringValue(columns[1]))
                        .mainGroup(stringValue(columns[2]))
                        .subGroup(stringValue(columns[3]))
                        .build())
                .collect(Collectors.toList());
    }

    public SupersessionDto getSupersession(String sachnr) {
        if (!StringUtils.hasText(sachnr)) {
            return SupersessionDto.builder()
                    .searched(sachnr)
                    .current(sachnr)
                    .chain(List.of())
                    .build();
        }

        List<SupersessionItemDto> chain = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        buildSupersessionChain(sachnr, chain, visited);

        String current = chain.isEmpty()
                ? sachnr
                : chain.get(chain.size() - 1).getSachnr();

        return SupersessionDto.builder()
                .searched(sachnr)
                .current(current)
                .chain(chain)
                .build();
    }

    private void buildSupersessionChain(String sachnr, List<SupersessionItemDto> chain, Set<String> visited) {
        if (!StringUtils.hasText(sachnr) || !visited.add(sachnr)) {
            return;
        }

        partRepository.findById(sachnr)
                .map(part -> SupersessionItemDto.builder()
                        .nr(chain.size() + 1)
                        .sachnr(part.getTeilSachnr())
                        .description(resolveName(part.getTeilTextcode()))
                        .expiration(toDate(part.getTeilEntfallDat()))
                        .build())
                .ifPresent(chain::add);

        List<String> replacements = teileersetzungRepository.findReplacementSachnrs(sachnr);
        for (String replacement : replacements) {
            buildSupersessionChain(replacement, chain, visited);
        }
    }

    private LocalDate toDate(Integer date) {
        if (date == null) {
            return null;
        }
        String raw = date.toString();
        if (raw.length() != 8) {
            return null;
        }
        return LocalDate.parse(raw, DateTimeFormatter.BASIC_ISO_DATE);
    }

    private String resolveName(Integer textCode) {
        if (textCode == null) {
            return null;
        }
        return publbenRepository.findByPublbenTextcodeAndPublbenSprache(textCode, DEFAULT_LANGUAGE)
                .map(publben -> publben.getPublbenText())
                .orElse(null);
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }

    private String stringValue(Object value) {
        return value == null ? null : value.toString();
    }
}
