package com.etkx.service;

import com.etkx.domain.BtZeilen;
import com.etkx.domain.Diagram;
import com.etkx.domain.Graphic;
import com.etkx.domain.HgFg;
import com.etkx.domain.Part;
import com.etkx.dto.DiagramDto;
import com.etkx.dto.DiagramPartDto;
import com.etkx.dto.MainGroupDto;
import com.etkx.dto.SubGroupDto;
import com.etkx.repository.BtZeilenRepository;
import com.etkx.repository.DiagramRepository;
import com.etkx.repository.GraphicRepository;
import com.etkx.repository.HgFgRepository;
import com.etkx.repository.PartRepository;
import com.etkx.repository.PublbenRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private static final String DEFAULT_LANGUAGE = "DE";

    private final HgFgRepository hgFgRepository;
    private final DiagramRepository diagramRepository;
    private final BtZeilenRepository btZeilenRepository;
    private final GraphicRepository graphicRepository;
    private final PartRepository partRepository;
    private final PublbenRepository publbenRepository;
    private final EntityManager entityManager;

    public List<MainGroupDto> getMainGroups(String produktart) {
        if (!StringUtils.hasText(produktart)) {
            return Collections.emptyList();
        }
        String normalized = produktart.trim();
        return hgFgRepository.findDistinctHgByProduktart(normalized).stream()
                .map(hgFg -> MainGroupDto.builder()
                        .hg(hgFg.getHgfgHg())
                        .name(resolveName(hgFg.getHgfgTextcode()))
                        .iconUrl(resolveMainGroupIcon(hgFg, normalized))
                        .build())
                .collect(Collectors.toList());
    }

    public List<SubGroupDto> getSubGroups(String hg, String produktart) {
        if (!StringUtils.hasText(hg) || !StringUtils.hasText(produktart)) {
            return Collections.emptyList();
        }
        String normalizedHg = hg.trim();
        String normalizedProduktart = produktart.trim();
        return hgFgRepository.findByHgfgHgAndHgfgProduktart(normalizedHg, normalizedProduktart).stream()
                .filter(hgFg -> !"00".equals(hgFg.getHgfgFg()))
                .map(hgFg -> SubGroupDto.builder()
                        .fg(hgFg.getHgfgFg())
                        .name(resolveName(hgFg.getHgfgTextcode()))
                        .thumbnailUrl(resolveSubGroupThumbnail(hgFg, normalizedProduktart))
                        .diagramNumber(resolveFirstDiagramNumber(hgFg.getHgfgHg(), hgFg.getHgfgFg()))
                        .build())
                .collect(Collectors.toList());
    }

    public Optional<DiagramDto> getDiagram(String btnr) {
        if (!StringUtils.hasText(btnr)) {
            return Optional.empty();
        }
        return diagramRepository.findByBildtafBtnr(btnr.trim())
                .map(diagram -> DiagramDto.builder()
                        .btnr(diagram.getDiagramNumber())
                        .name(resolveName(diagram.getTextCode()))
                        .parts(loadDiagramParts(diagram.getDiagramNumber()))
                        .build());
    }

    public Optional<Integer> getDiagramGraphicId(String btnr) {
        if (!StringUtils.hasText(btnr)) {
            return Optional.empty();
        }
        return diagramRepository.findByBildtafBtnr(btnr.trim())
                .map(Diagram::getGraphicId);
    }

    @Transactional(readOnly = true)
    public Optional<Graphic> getDiagramImage(Integer grafikid) {
        if (grafikid == null) {
            return Optional.empty();
        }
        return graphicRepository.findByGrafikGrafikid(grafikid);
    }

    private List<DiagramPartDto> loadDiagramParts(String btnr) {
        if (!StringUtils.hasText(btnr)) {
            return Collections.emptyList();
        }
        return btZeilenRepository.findByBtzeilenBtnrOrderByBtzeilenPos(btnr).stream()
                .map(this::toDiagramPartDto)
                .collect(Collectors.toList());
    }

    private DiagramPartDto toDiagramPartDto(BtZeilen btZeilen) {
        String sachnr = btZeilen.getBtzeilenSachnr();
        Optional<Part> part = sachnr == null ? Optional.empty() : partRepository.findById(sachnr);
        return DiagramPartDto.builder()
                .pos(resolvePosition(btZeilen))
                .sachnr(sachnr)
                .quantity(btZeilen.getBtzeilenMenge())
                .name(part.map(p -> resolveName(p.getTeilTextcode())).orElse(null))
                .build();
    }

    private String resolvePosition(BtZeilen btZeilen) {
        if (StringUtils.hasText(btZeilen.getBtzeilenBildposnr())) {
            return btZeilen.getBtzeilenBildposnr();
        }
        return btZeilen.getBtzeilenPos() == null ? null : btZeilen.getBtzeilenPos().toString();
    }

    private String resolveName(Integer textCode) {
        if (textCode == null) {
            return null;
        }
        return publbenRepository.findByPublbenTextcodeAndPublbenSprache(textCode, DEFAULT_LANGUAGE)
                .map(publben -> publben.getPublbenText())
                .orElse(null);
    }

    private String resolveMainGroupIcon(HgFg hgFg, String produktart) {
        Integer grafikId = findMainGroupGraphicId(hgFg.getHgfgHg(), produktart)
                .orElse(hgFg.getHgfgGrafikid());
        return grafikId == null ? null : "/api/catalog/images/" + grafikId;
    }

    private String resolveSubGroupThumbnail(HgFg hgFg, String produktart) {
        Integer grafikId = findSubGroupGraphicId(hgFg.getHgfgHg(), hgFg.getHgfgFg(), produktart)
                .orElse(hgFg.getHgfgGrafikid());
        return grafikId == null ? null : "/api/catalog/images/" + grafikId;
    }

    private Optional<Integer> findMainGroupGraphicId(String hg, String produktart) {
        Query query = entityManager.createNativeQuery("""
                select hgthb_grafikid
                  from w_hg_thumbnail
                 where hgthb_hg = :hg
                   and hgthb_produktart = :produktart
                 limit 1
                """);
        query.setParameter("hg", hg);
        query.setParameter("produktart", produktart);
        List<?> results = query.getResultList();
        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(results.get(0)).map(value -> ((Number) value).intValue());
    }

    private Optional<Integer> findSubGroupGraphicId(String hg, String fg, String produktart) {
        Query query = entityManager.createNativeQuery("""
                select fgthb_grafikid
                  from w_fg_thumbnail
                 where fgthb_hg = :hg
                   and fgthb_fg = :fg
                   and fgthb_produktart = :produktart
                 limit 1
                """);
        query.setParameter("hg", hg);
        query.setParameter("fg", fg);
        query.setParameter("produktart", produktart);
        List<?> results = query.getResultList();
        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(results.get(0)).map(value -> ((Number) value).intValue());
    }

    private String resolveFirstDiagramNumber(String hg, String fg) {
        return diagramRepository.findByBildtafHgAndBildtafFg(hg, fg).stream()
                .map(Diagram::getDiagramNumber)
                .sorted(Comparator.naturalOrder())
                .findFirst()
                .orElse(null);
    }
}
