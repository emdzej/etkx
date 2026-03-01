package pl.emdzej.etkx.dal.repository.vehicle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pl.emdzej.etkx.dal.dto.vehicle.BauartDto;
import pl.emdzej.etkx.dal.dto.vehicle.BaujahrDto;
import pl.emdzej.etkx.dal.dto.vehicle.BaureiheDto;
import pl.emdzej.etkx.dal.dto.vehicle.BedConditionDto;
import pl.emdzej.etkx.dal.dto.vehicle.BedZusatzinfoDto;
import pl.emdzej.etkx.dal.dto.vehicle.DesignationDto;
import pl.emdzej.etkx.dal.dto.vehicle.GetriebeDto;
import pl.emdzej.etkx.dal.dto.vehicle.GrafikInfoDto;
import pl.emdzej.etkx.dal.dto.vehicle.KarosserieDto;
import pl.emdzej.etkx.dal.dto.vehicle.LenkungDto;
import pl.emdzej.etkx.dal.dto.vehicle.ModellDto;
import pl.emdzej.etkx.dal.dto.vehicle.MospIdDto;
import pl.emdzej.etkx.dal.dto.vehicle.PaintConditionDto;
import pl.emdzej.etkx.dal.dto.vehicle.RegionDto;
import pl.emdzej.etkx.dal.dto.vehicle.SalaConditionDto;
import pl.emdzej.etkx.dal.dto.vehicle.SalaDto;
import pl.emdzej.etkx.dal.dto.vehicle.TypDto;
import pl.emdzej.etkx.dal.dto.vehicle.VehicleIdentificationDto;
import pl.emdzej.etkx.dal.dto.vehicle.ZulassungsmonatDto;

/**
 * Repository for vehicle identification workflows (FzgIdentifikation).
 */
@Repository
@RequiredArgsConstructor
public class VehicleIdentificationRepository {
    private static final String RETRIEVE_BAUARTEN = """
        select distinct baureihe_bauart Bauart,
            ben_text ExtBauart,
            bauart_position Pos
        from w_baureihe, w_bauart, w_ben_gk, w_fztyp
        where baureihe_marke_tps = :marke
          and baureihe_produktart = :produktart
          and (:katalogumfang = 'BE' OR baureihe_vbereich in ('BE', :katalogumfang))
          and fztyp_baureihe = baureihe_baureihe
          and (:katalogumfang = 'BE' OR fztyp_vbereich = :katalogumfang)
          and fztyp_ktlgausf in (:regionen)
          and fztyp_sichtschutz = 'N'
          and bauart_bauart = baureihe_bauart
          and ben_textcode = bauart_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by Pos
        """;

    private static final String RETRIEVE_BAUREIHEN = """
        select distinct baureihe_baureihe Baureihe,
            ben_text ExtBaureihe,
            baureihe_position Pos
        from w_baureihe, w_ben_gk, w_fztyp
        where baureihe_marke_tps = :marke
          and baureihe_produktart = :produktart
          and (:katalogumfang = 'BE' OR baureihe_vbereich in ('BE', :katalogumfang))
          __BAUART_STMT__
          and baureihe_baureihe = fztyp_baureihe
          and (:katalogumfang = 'BE' OR fztyp_vbereich = :katalogumfang)
          and fztyp_ktlgausf in (:regionen)
          __LENKUNG_STMT__
          and fztyp_sichtschutz = 'N'
          and baureihe_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by Pos
        """;

    private static final String GET_GRAFIKID_FOR_BAUREIHE = """
        select grafik_grafikid GrafikId,
            grafik_moddate ModStamp
        from w_baureihe, w_grafik
        where baureihe_baureihe = :baureihe
          and baureihe_grafikid = grafik_grafikid
          and grafik_art = 'T'
        """;

    private static final String FIND_MARKE_BY_MOSPID = """
        select distinct baureihe_marke_tps Marke
        from w_fztyp
        join w_baureihe on fztyp_baureihe = baureihe_baureihe
        where fztyp_mospid = :mospId
        limit 1
        """;

    private static final String FIND_TYP_BY_MOSPID = """
        select fztyp_typschl Typ
        from w_fztyp
        where fztyp_mospid = :mospId
        limit 1
        """;

    private static final String GET_GRAFIKID_FOR_BAUREIHE_KAROSSERIE = """
        select grafik_grafikid GrafikId,
            grafik_moddate ModStamp
        from w_baureihe_kar_thb, w_grafik
        where baureihekar_baureihe = :baureihe
          and baureihekar_karosserie = :karosserie
          and baureihekar_grafikid = grafik_grafikid
          and grafik_art = 'T'
        """;

    private static final String GET_GRAFIKID_FOR_FIBILD = """
        select grafik_grafikid GrafikId,
            grafik_format Format,
            grafik_moddate ModStamp
        from w_etk_grafiken, w_grafik
        where etkgraf_ablauf = 'FI'
          and etkgraf_marke = :marke
          and etkgraf_produktart = :produktart
          and (:katalogumfang = 'BE' OR etkgraf_vbereich = :katalogumfang)
          and etkgraf_grafikid = grafik_grafikid
          and grafik_art = 'T'
        """;

    private static final String RETRIEVE_KAROSSERIEN = """
        select distinct fztyp_karosserie Karosserie,
            ben_text ExtKarosserie
        from w_fztyp, w_ben_gk, w_publben
        where fztyp_baureihe = :baureihe
          and (:katalogumfang = 'BE' OR fztyp_vbereich = :katalogumfang)
          and fztyp_ktlgausf in (:regionen)
          __LENKUNG_STMT__
          and fztyp_sichtschutz = 'N'
          and publben_art = 'K'
          and fztyp_karosserie = publben_bezeichnung
          and publben_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by ExtKarosserie
        """;

    private static final String RETRIEVE_MODELLE = """
        select distinct fztyp_erwvbez Modell,
            vbezp_pos Pos
        from w_vbez_pos, w_fztyp
        where fztyp_baureihe = :baureihe
          and (:katalogumfang = 'BE' OR fztyp_vbereich = :katalogumfang)
          __KAROSSERIE_STMT__
          and fztyp_ktlgausf in (:regionen)
          __LENKUNG_STMT__
          and fztyp_sichtschutz = 'N'
          and vbezp_baureihe = fztyp_baureihe
          and vbezp_vbez = fztyp_vbez
        order by Pos, Modell
        """;

    private static final String RETRIEVE_REGIONEN = """
        select distinct fztyp_ktlgausf Region
        from w_fztyp
        where fztyp_baureihe = :baureihe
          and (:katalogumfang = 'BE' OR fztyp_vbereich = :katalogumfang)
          __KAROSSERIE_STMT__
          and fztyp_erwvbez = :modell
          and fztyp_ktlgausf in (:regionen)
          __LENKUNG_STMT__
          and fztyp_sichtschutz = 'N'
        order by Region
        """;

    private static final String RETRIEVE_LENKUNGEN = """
        select distinct fztyp_lenkung Lenkung,
            ben_text ExtLenkung
        from w_fztyp, w_ben_gk, w_publben
        where fztyp_baureihe = :baureihe
          and (:katalogumfang = 'BE' OR fztyp_vbereich = :katalogumfang)
          and fztyp_karosserie = :karosserie
          and fztyp_erwvbez = :modell
          and fztyp_ktlgausf = :region
          and fztyp_sichtschutz = 'N'
          and fztyp_lenkung = publben_bezeichnung
          and publben_art = 'L'
          and publben_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by Lenkung
        """;

    private static final String RETRIEVE_GETRIEBEARTEN = """
        select distinct fztyp_getriebe Getriebe,
            ben_text ExtGetriebe
        from w_fztyp, w_ben_gk, w_publben
        where fztyp_baureihe = :baureihe
          and (:katalogumfang = 'BE' OR fztyp_vbereich = :katalogumfang)
          and fztyp_karosserie = :karosserie
          and fztyp_erwvbez = :modell
          and fztyp_ktlgausf = :region
          __LENKUNG_STMT__
          and fztyp_sichtschutz = 'N'
          and fztyp_getriebe = publben_bezeichnung
          and publben_art = 'G'
          and publben_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by Getriebe
        """;

    private static final String RETRIEVE_BAUJAHRE = """
        select distinct SUBSTR(CAST(fgstnr_prod AS TEXT), 1, 4) Baujahr
        from w_fztyp, w_fgstnr
        where fztyp_baureihe = :baureihe
          and (:katalogumfang = 'BE' OR fztyp_vbereich = :katalogumfang)
          __KAROSSERIE_STMT__
          and fztyp_erwvbez = :modell
          and fztyp_ktlgausf = :region
          __LENKUNG_STMT__
          __GETRIEBE_STMT__
          and fztyp_sichtschutz = 'N'
          and fztyp_typschl = fgstnr_typschl
        order by Baujahr
        """;

    private static final String RETRIEVE_ZULASSMONATE = """
        select distinct SUBSTR(CAST(fgstnr_prod AS TEXT), 5, 2) Zulassungsmonat,
            ben_text ExtZulassungsmonat
        from w_fztyp, w_ben_gk, w_publben, w_fgstnr
        where fztyp_baureihe = :baureihe
          and (:katalogumfang = 'BE' OR fztyp_vbereich = :katalogumfang)
          __KAROSSERIE_STMT__
          and fztyp_erwvbez = :modell
          and fztyp_ktlgausf = :region
          __LENKUNG_STMT__
          __GETRIEBE_STMT__
          and fztyp_sichtschutz = 'N'
          and fztyp_typschl = fgstnr_typschl
          and SUBSTR(CAST(fgstnr_prod AS TEXT), 1, 4) = :baujahr
          and SUBSTR(CAST(fgstnr_prod AS TEXT), 5, 2) = publben_bezeichnung
          and publben_art = 'M'
          and publben_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by Zulassungsmonat
        """;

    private static final String RETRIEVE_ZULASSMONATE2 = """
        select distinct SUBSTR(CAST(fgstnr_prod AS TEXT), 5, 2) Zulassungsmonat,
            ben_text ExtZulassungsmonat
        from w_fztyp, w_ben_gk, w_publben, w_fgstnr
        where fztyp_baureihe = :baureihe
          and (:katalogumfang = 'BE' OR fztyp_vbereich = :katalogumfang)
          __KAROSSERIE_STMT__
          and fztyp_erwvbez = :modell
          and fztyp_ktlgausf = :region
          __LENKUNG_STMT__
          __GETRIEBE_STMT__
          and fztyp_sichtschutz = 'N'
          and fztyp_typschl = fgstnr_typschl
          and SUBSTR(CAST(fgstnr_prod AS TEXT), 5, 2) = publben_bezeichnung
          and publben_art = 'M'
          and publben_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by Zulassungsmonat
        """;

    private static final String RETRIEVE_MOSP_BY_ATTRIBUTE_PKW = """
        select distinct fztyp_mospid Modellspalte
        from w_fztyp
        where fztyp_baureihe = :baureihe
          and fztyp_karosserie = :karosserie
          and fztyp_erwvbez = :modell
          and fztyp_ktlgausf = :region
        """;

    private static final String RETRIEVE_MOSP_BY_ATTRIBUTE_KRAD = """
        select distinct fztyp_mospid Modellspalte
        from w_fztyp
        where fztyp_baureihe = :baureihe
          and fztyp_erwvbez = :modell
          and fztyp_ktlgausf = :region
        """;

    private static final String RETRIEVE_MOSP_BY_FGSTNR = """
        select distinct fgstnr_mospid Modellspalte,
            fgstnr_typschl Typ,
            fgstnr_werk Werk,
            baureihe_marke_tps Marke,
            baureihe_produktart Produktart,
            fztyp_vbereich Katalogumfang,
            fztyp_baureihe Baureihe,
            b.ben_text ExtBaureihe,
            baureihe_bauart Bauart,
            bb.ben_text ExtBauart,
            fztyp_karosserie Karosserie,
            bk.ben_text ExtKarosserie,
            fztyp_motor Motor,
            fztyp_erwvbez Modell,
            fztyp_ktlgausf Region,
            fztyp_lenkung Lenkung,
            fztyp_getriebe Getriebe,
            fgstnr_prod Produktionsdatum,
            fztyp_sichtschutz Sichtschutz,
            COALESCE(fztyp_einsatz, 0) Einsatz
        from w_fgstnr
        inner join w_fztyp on (fgstnr_typschl = fztyp_typschl and fgstnr_mospid = fztyp_mospid)
        inner join w_baureihe on (fztyp_baureihe = baureihe_baureihe)
        inner join w_publben pk on (fztyp_karosserie = pk.publben_bezeichnung and pk.publben_art = 'K')
        inner join w_publben pb on (baureihe_bauart = pb.publben_bezeichnung and pb.publben_art = 'B')
        inner join w_ben_gk b on (baureihe_textcode = b.ben_textcode and b.ben_iso = :iso and b.ben_regiso = :regiso)
        inner join w_ben_gk bk on (pk.publben_textcode = bk.ben_textcode and bk.ben_iso = :iso and bk.ben_regiso = :regiso)
        inner join w_ben_gk bb on (pb.publben_textcode = bb.ben_textcode and bb.ben_iso = :iso and bb.ben_regiso = :regiso)
        where fgstnr_von <= :fgstnr
          and fgstnr_bis >= :fgstnr
          and fgstnr_anf = :fgstnrFirst2
        """;

    private static final String RETRIEVE_MOSP_BY_TYP = """
        select distinct fztyp_mospid Modellspalte,
            fztyp_typschl Typ,
            null Werk,
            baureihe_marke_tps Marke,
            baureihe_produktart Produktart,
            fztyp_vbereich Katalogumfang,
            fztyp_baureihe Baureihe,
            b.ben_text ExtBaureihe,
            baureihe_bauart Bauart,
            bb.ben_text ExtBauart,
            fztyp_karosserie Karosserie,
            bk.ben_text ExtKarosserie,
            fztyp_motor Motor,
            fztyp_erwvbez Modell,
            fztyp_ktlgausf Region,
            fztyp_lenkung Lenkung,
            fztyp_getriebe Getriebe,
            :prodDatum Produktionsdatum,
            fztyp_sichtschutz Sichtschutz,
            COALESCE(fztyp_einsatz, 0) Einsatz
        from w_fztyp
        inner join w_baureihe on (fztyp_baureihe = baureihe_baureihe)
        inner join w_publben pk on (fztyp_karosserie = pk.publben_bezeichnung and pk.publben_art = 'K')
        inner join w_ben_gk bk on (pk.publben_textcode = bk.ben_textcode and bk.ben_iso = :iso and bk.ben_regiso = :regiso)
        inner join w_publben pb on (baureihe_bauart = pb.publben_bezeichnung and pb.publben_art = 'B')
        inner join w_ben_gk bb on (pb.publben_textcode = bb.ben_textcode and bb.ben_iso = :iso and bb.ben_regiso = :regiso)
        inner join w_ben_gk b on (baureihe_textcode = b.ben_textcode and b.ben_iso = :iso and b.ben_regiso = :regiso)
        where fztyp_typschl = :typ
        """;

    private static final String RETRIEVE_TYPMENGE_BY_ATTRIBUTE_PKW = """
        select distinct fztyp_typschl Typ
        from w_fztyp, w_fgstnr
        where fztyp_mospid = :mospid
          and fztyp_sichtschutz = 'N'
          and fztyp_lenkung = :lenkung
          and fztyp_getriebe = :getriebe
          and fztyp_mospid = fgstnr_mospid
          and fztyp_typschl = fgstnr_typschl
          and fgstnr_prod = :prodDatum
        """;

    private static final String RETRIEVE_TYPMENGE_BY_ATTRIBUTE_KRAD = """
        select distinct fztyp_typschl Typ
        from w_fztyp, w_fgstnr
        where fztyp_mospid = :mospid
          and fztyp_sichtschutz = 'N'
          and fztyp_mospid = fgstnr_mospid
          and fztyp_typschl = fgstnr_typschl
          and fgstnr_prod = :prodDatum
        """;

    private static final String RETRIEVE_LENKUNG_BEN = """
        select ben_text Name
        from _publben, w_ben_gk
        where publben_art = 'L'
          and publben_bezeichnung = :value
          and publben_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        """;

    private static final String LOAD_SALAS_ZU_FGSTNR = """
        select distinct bed_elemid ElementId,
            bedsala_art || bedsala_pnr || bedsala_hz Code,
            ben_text Benennung,
            bed_egid EGruppenId,
            bedsala_sicher Sicher,
            bedsala_saz SAZ,
            CASE WHEN bedzus_elemid IS NOT NULL THEN 'J' ELSE 'N' END HasBedText,
            fgstnrs_showtext ShowBedText,
            eg_exklusiv Exklusiv,
            eg_pos EGruppenPosition,
            bedsala_pnr PrimaNr
        from w_fgstnr_sala
        inner join w_bed_sala on (fgstnrs_salaid = bedsala_id)
        inner join w_bed on (bedsala_id = bed_elemid)
        inner join w_ben_gk on (bed_textcode = ben_textcode and ben_iso = :iso and ben_regiso = :regiso)
        inner join w_eg on (bed_egid = eg_id)
        left join w_bed_zusatzinfo on (bedzus_elemid = fgstnrs_salaid)
        where fgstnrs_fgstnr = :fgstnr
        """;

    private static final String LOAD_SS_BEDINGUNGEN_AF = """
        select distinct bed_elemid ElementId,
            af.bedafl_code Code,
            ben_text Benennung,
            bed_egid EGruppenId,
            bed_vorhanden_info VorhandenInfo,
            eg_exklusiv Exklusiv,
            af.bedafl_art Bedingungsart,
            eg_pos EGruppenPosition
        from w_bed_afl af, w_ben_gk, w_bed, w_eg
        where af.bedafl_art = 'AF'
          __LOAD_SS_BEDINGUNGEN_AF_DECIDE__
          and af.bedafl_gilt_v <= :prodDatum
          and COALESCE(af.bedafl_gilt_b, 99999999) >= :prodDatum
          and af.bedafl_id = bed_elemid
          and bed_egid = eg_id
          and bed_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        union
        select distinct bed_elemid ElementId,
            f.bedafl_code Code,
            ben_text Benennung,
            bed_egid EGruppenId,
            bed_vorhanden_info VorhandenInfo,
            eg_exklusiv Exklusiv,
            f.bedafl_art Bedingungsart,
            eg_pos EGruppenPosition
        from w_bed_afl af, w_bed_afl f, w_ben_gk, w_bed, w_eg
        where af.bedafl_art = 'AF'
          __LOAD_SS_BEDINGUNGEN_AF_DECIDE__
          and af.bedafl_gilt_v <= :prodDatum
          and COALESCE(af.bedafl_gilt_b, 99999999) >= :prodDatum
          and f.bedafl_code = substr(af.bedafl_code, 3)
          and f.bedafl_art = 'F'
          and f.bedafl_gilt_v <= :prodDatum
          and COALESCE(f.bedafl_gilt_b, 99999999) >= :prodDatum
          and f.bedafl_id = bed_elemid
          and bed_egid = eg_id
          and bed_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        union
        select distinct bed_elemid ElementId,
            a.bedafl_code Code,
            ben_text Benennung,
            bed_egid EGruppenId,
            bed_vorhanden_info VorhandenInfo,
            eg_exklusiv Exklusiv,
            a.bedafl_art Bedingungsart,
            eg_pos EGruppenPosition
        from w_bed_afl af, w_bed_afl a, w_ben_gk, w_bed, w_eg
        where af.bedafl_art= 'AF'
          __LOAD_SS_BEDINGUNGEN_AF_DECIDE__
          and af.bedafl_gilt_v <= :prodDatum
          and COALESCE(af.bedafl_gilt_b, 99999999) >= :prodDatum
          and a.bedafl_code = substr(af.bedafl_code, 1, 2)
          and a.bedafl_art = 'A'
          and a.bedafl_gilt_v <= :prodDatum
          and COALESCE(a.bedafl_gilt_b, 99999999) >= :prodDatum
          and a.bedafl_id = bed_elemid
          and bed_egid = eg_id
          and bed_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        """;

    private static final String LOAD_SS_BEDINGUNGEN_LACK = """
        select distinct bed_elemid ElementId,
            bedafl_code Code,
            ben_text Benennung,
            bed_egid EGruppenId,
            bed_vorhanden_info VorhandenInfo,
            eg_exklusiv Exklusiv,
            eg_pos EGruppenPosition
        from w_bed_afl, w_ben_gk, w_bed, w_eg
        where bedafl_art= 'L'
          __LOAD_SS_BEDINGUNGEN_LACK_DECIDE__
          and bedafl_gilt_v <= :prodDatum
          and COALESCE(bedafl_gilt_b, 99999999) >= :prodDatum
          and bedafl_id = bed_elemid
          and bed_egid = eg_id
          and bed_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        """;

    private static final String LOAD_SS_BEDINGUNGEN_SALAPA = """
        select distinct bed_elemid ElementId,
            bedsala_art || bedsala_pnr || bedsala_hz Code,
            ben_text Benennung,
            bed_egid EGruppenId,
            bed_vorhanden_info VorhandenInfo,
            eg_exklusiv Exklusiv,
            bedsala_sicher Sicher,
            bedsala_saz SAZ,
            eg_pos EGruppenPosition,
            bedsala_pnr PrimaNr
        from w_bed_sala, w_ben_gk, w_bed, w_eg
        where bedsala_produktart = :produktart
          and bedsala_pnr = :primaNummer
          and bedsala_gilt_v <= :prodDatum
          and COALESCE(bedsala_gilt_b, 99999999) >= :prodDatum
          and bedsala_art not in ('N', 'V')
          __LOAD_SS_BEDINGUNGEN_SALAPA_DECIDE_ART__
          __LOAD_SS_BEDINGUNGEN_SALAPA_DECIDE_HZAEHLER__
          and bedsala_id = bed_elemid
          and bed_egid = eg_id
          and bed_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        """;

    private static final String RETRIEVE_BED_ZUSATZINFO = """
        select komm_id KommId,
            ben_text Ben,
            komm_pos Pos
        from w_komm, w_ben_gk
        where komm_id in (
            select bedzus_kommid
            from w_bed_zusatzinfo
            where bedzus_elemid IN (:bedElemIds)
            diff
            select bedzus_kommid
            from w_bed_zusatzinfo
            where bedzus_elemid NOT IN (:bedElemIds)
        )
          and komm_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by KommId, Pos
        """;

    private static final RowMapper<BauartDto> BAUART_MAPPER = (rs, rowNum) ->
        BauartDto.builder()
            .bauart(rs.getString("Bauart"))
            .extBauart(rs.getString("ExtBauart"))
            .pos(rs.getString("Pos"))
            .build();

    private static final RowMapper<BaureiheDto> BAUREIHE_MAPPER = (rs, rowNum) ->
        BaureiheDto.builder()
            .baureihe(rs.getString("Baureihe"))
            .extBaureihe(rs.getString("ExtBaureihe"))
            .pos(rs.getString("Pos"))
            .build();

    private static final RowMapper<GrafikInfoDto> GRAFIK_MAPPER = (rs, rowNum) ->
        GrafikInfoDto.builder()
            .grafikId(rs.getString("GrafikId"))
            .format(rs.getString("Format"))
            .modStamp(rs.getString("ModStamp"))
            .build();

    private static final RowMapper<KarosserieDto> KAROSSERIE_MAPPER = (rs, rowNum) ->
        KarosserieDto.builder()
            .karosserie(rs.getString("Karosserie"))
            .extKarosserie(rs.getString("ExtKarosserie"))
            .build();

    private static final RowMapper<ModellDto> MODELL_MAPPER = (rs, rowNum) ->
        ModellDto.builder()
            .modell(rs.getString("Modell"))
            .pos(rs.getString("Pos"))
            .build();

    private static final RowMapper<RegionDto> REGION_MAPPER = (rs, rowNum) ->
        RegionDto.builder()
            .region(rs.getString("Region"))
            .build();

    private static final RowMapper<LenkungDto> LENKUNG_MAPPER = (rs, rowNum) ->
        LenkungDto.builder()
            .lenkung(rs.getString("Lenkung"))
            .extLenkung(rs.getString("ExtLenkung"))
            .build();

    private static final RowMapper<GetriebeDto> GETRIEBE_MAPPER = (rs, rowNum) ->
        GetriebeDto.builder()
            .getriebe(rs.getString("Getriebe"))
            .extGetriebe(rs.getString("ExtGetriebe"))
            .build();

    private static final RowMapper<BaujahrDto> BAUJAHR_MAPPER = (rs, rowNum) ->
        BaujahrDto.builder()
            .baujahr(rs.getString("Baujahr"))
            .build();

    private static final RowMapper<ZulassungsmonatDto> ZULASSUNGSMONAT_MAPPER = (rs, rowNum) ->
        ZulassungsmonatDto.builder()
            .zulassungsmonat(rs.getString("Zulassungsmonat"))
            .extZulassungsmonat(rs.getString("ExtZulassungsmonat"))
            .build();

    private static final RowMapper<MospIdDto> MOSP_MAPPER = (rs, rowNum) ->
        MospIdDto.builder()
            .mospId(rs.getString("Modellspalte"))
            .build();

    private static final RowMapper<VehicleIdentificationDto> VEHICLE_IDENTIFICATION_MAPPER = (rs, rowNum) ->
        VehicleIdentificationDto.builder()
            .modellspalte(rs.getString("Modellspalte"))
            .typ(rs.getString("Typ"))
            .werk(rs.getString("Werk"))
            .marke(rs.getString("Marke"))
            .produktart(rs.getString("Produktart"))
            .katalogumfang(rs.getString("Katalogumfang"))
            .baureihe(rs.getString("Baureihe"))
            .extBaureihe(rs.getString("ExtBaureihe"))
            .bauart(rs.getString("Bauart"))
            .extBauart(rs.getString("ExtBauart"))
            .karosserie(rs.getString("Karosserie"))
            .extKarosserie(rs.getString("ExtKarosserie"))
            .motor(rs.getString("Motor"))
            .modell(rs.getString("Modell"))
            .region(rs.getString("Region"))
            .lenkung(rs.getString("Lenkung"))
            .getriebe(rs.getString("Getriebe"))
            .produktionsdatum(rs.getString("Produktionsdatum"))
            .sichtschutz(rs.getString("Sichtschutz"))
            .einsatz(rs.getString("Einsatz"))
            .build();

    private static final RowMapper<TypDto> TYP_MAPPER = (rs, rowNum) ->
        TypDto.builder()
            .typ(rs.getString("Typ"))
            .build();

    private static final RowMapper<DesignationDto> DESIGNATION_MAPPER = (rs, rowNum) ->
        DesignationDto.builder()
            .name(rs.getString("Name"))
            .build();

    private static final RowMapper<SalaDto> SALA_MAPPER = (rs, rowNum) ->
        SalaDto.builder()
            .elementId(rs.getString("ElementId"))
            .code(rs.getString("Code"))
            .benennung(rs.getString("Benennung"))
            .eGruppenId(rs.getString("EGruppenId"))
            .sicher(rs.getString("Sicher"))
            .saz(rs.getString("SAZ"))
            .hasBedText(rs.getString("HasBedText"))
            .showBedText(rs.getString("ShowBedText"))
            .exklusiv(rs.getString("Exklusiv"))
            .eGruppenPosition(rs.getString("EGruppenPosition"))
            .primaNr(rs.getString("PrimaNr"))
            .build();

    private static final RowMapper<BedConditionDto> BED_CONDITION_MAPPER = (rs, rowNum) ->
        BedConditionDto.builder()
            .elementId(rs.getString("ElementId"))
            .code(rs.getString("Code"))
            .benennung(rs.getString("Benennung"))
            .eGruppenId(rs.getString("EGruppenId"))
            .vorhandenInfo(rs.getString("VorhandenInfo"))
            .exklusiv(rs.getString("Exklusiv"))
            .bedingungsart(rs.getString("Bedingungsart"))
            .eGruppenPosition(rs.getString("EGruppenPosition"))
            .build();

    private static final RowMapper<PaintConditionDto> PAINT_CONDITION_MAPPER = (rs, rowNum) ->
        PaintConditionDto.builder()
            .elementId(rs.getString("ElementId"))
            .code(rs.getString("Code"))
            .benennung(rs.getString("Benennung"))
            .eGruppenId(rs.getString("EGruppenId"))
            .vorhandenInfo(rs.getString("VorhandenInfo"))
            .exklusiv(rs.getString("Exklusiv"))
            .eGruppenPosition(rs.getString("EGruppenPosition"))
            .build();

    private static final RowMapper<SalaConditionDto> SALA_CONDITION_MAPPER = (rs, rowNum) ->
        SalaConditionDto.builder()
            .elementId(rs.getString("ElementId"))
            .code(rs.getString("Code"))
            .benennung(rs.getString("Benennung"))
            .eGruppenId(rs.getString("EGruppenId"))
            .vorhandenInfo(rs.getString("VorhandenInfo"))
            .exklusiv(rs.getString("Exklusiv"))
            .sicher(rs.getString("Sicher"))
            .saz(rs.getString("SAZ"))
            .eGruppenPosition(rs.getString("EGruppenPosition"))
            .primaNr(rs.getString("PrimaNr"))
            .build();

    private static final RowMapper<BedZusatzinfoDto> BED_ZUSATZINFO_MAPPER = (rs, rowNum) ->
        BedZusatzinfoDto.builder()
            .kommId(rs.getString("KommId"))
            .ben(rs.getString("Ben"))
            .pos(rs.getString("Pos"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Retrieves construction types (Bauarten) for vehicle identification.
     */
    public List<BauartDto> findConstructionTypes(String marke, String produktart, String katalogumfang,
                                                 List<String> regionen, String iso, String regiso) {
        return jdbc.query(RETRIEVE_BAUARTEN, Map.of(
            "marke", marke,
            "produktart", produktart,
            "katalogumfang", katalogumfang,
            "regionen", regionen,
            "iso", iso,
            "regiso", regiso
        ), BAUART_MAPPER);
    }

    /**
     * Retrieves model series (Baureihen) for vehicle identification.
     */
    public List<BaureiheDto> findSeries(String marke, String produktart, String katalogumfang,
                                        List<String> regionen, String iso, String regiso,
                                        String bauartClause, String lenkungClause,
                                        Map<String, Object> extraParams) {
        String sql = applyClauses(RETRIEVE_BAUREIHEN, Map.of(
            "__BAUART_STMT__", bauartClause,
            "__LENKUNG_STMT__", lenkungClause
        ));
        Map<String, Object> params = new HashMap<>(Map.of(
            "marke", marke,
            "produktart", produktart,
            "katalogumfang", katalogumfang,
            "regionen", regionen,
            "iso", iso,
            "regiso", regiso
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, BAUREIHE_MAPPER);
    }

    /**
     * Resolves brand identifier for the given MOSP id.
     */
    public String findMarkeByMospId(long mospId) {
        return jdbc.queryForObject(FIND_MARKE_BY_MOSPID, Map.of("mospId", mospId), String.class);
    }

    /**
     * Resolves vehicle type for the given MOSP id.
     */
    public String findTypByMospId(long mospId) {
        return jdbc.queryForObject(FIND_TYP_BY_MOSPID, Map.of("mospId", mospId), String.class);
    }

    /**
     * Retrieves graphics metadata for a series.
     */
    public List<GrafikInfoDto> findSeriesGraphic(String baureihe) {
        return jdbc.query(GET_GRAFIKID_FOR_BAUREIHE, Map.of("baureihe", baureihe), GRAFIK_MAPPER);
    }

    /**
     * Retrieves graphics metadata for a series and body.
     */
    public List<GrafikInfoDto> findSeriesBodyGraphic(String baureihe, String karosserie) {
        return jdbc.query(GET_GRAFIKID_FOR_BAUREIHE_KAROSSERIE, Map.of(
            "baureihe", baureihe,
            "karosserie", karosserie
        ), GRAFIK_MAPPER);
    }

    /**
     * Retrieves graphics metadata for the FI image.
     */
    public List<GrafikInfoDto> findFibildGraphic(String marke, String produktart, String katalogumfang) {
        return jdbc.query(GET_GRAFIKID_FOR_FIBILD, Map.of(
            "marke", marke,
            "produktart", produktart,
            "katalogumfang", katalogumfang
        ), GRAFIK_MAPPER);
    }

    /**
     * Retrieves body types for a model series.
     */
    public List<KarosserieDto> findBodies(String baureihe, String katalogumfang, List<String> regionen,
                                          String iso, String regiso,
                                          String lenkungClause, Map<String, Object> extraParams) {
        String sql = applyClauses(RETRIEVE_KAROSSERIEN, Map.of("__LENKUNG_STMT__", lenkungClause));
        Map<String, Object> params = new HashMap<>(Map.of(
            "baureihe", baureihe,
            "katalogumfang", katalogumfang,
            "regionen", regionen,
            "iso", iso,
            "regiso", regiso
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, KAROSSERIE_MAPPER);
    }

    /**
     * Retrieves models for a series.
     */
    public List<ModellDto> findModels(String baureihe, String katalogumfang, List<String> regionen,
                                      String karosserieClause, String lenkungClause,
                                      Map<String, Object> extraParams) {
        String sql = applyClauses(RETRIEVE_MODELLE, Map.of(
            "__KAROSSERIE_STMT__", karosserieClause,
            "__LENKUNG_STMT__", lenkungClause
        ));
        Map<String, Object> params = new HashMap<>(Map.of(
            "baureihe", baureihe,
            "katalogumfang", katalogumfang,
            "regionen", regionen
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, MODELL_MAPPER);
    }

    /**
     * Retrieves available regions for model selection.
     */
    public List<RegionDto> findRegions(String baureihe, String katalogumfang, String modell, List<String> regionen,
                                       String karosserieClause, String lenkungClause,
                                       Map<String, Object> extraParams) {
        String sql = applyClauses(RETRIEVE_REGIONEN, Map.of(
            "__KAROSSERIE_STMT__", karosserieClause,
            "__LENKUNG_STMT__", lenkungClause
        ));
        Map<String, Object> params = new HashMap<>(Map.of(
            "baureihe", baureihe,
            "katalogumfang", katalogumfang,
            "modell", modell,
            "regionen", regionen
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, REGION_MAPPER);
    }

    /**
     * Retrieves steering options for a model selection.
     */
    public List<LenkungDto> findSteerings(String baureihe, String katalogumfang, String karosserie,
                                          String modell, String region, String iso, String regiso) {
        return jdbc.query(RETRIEVE_LENKUNGEN, Map.of(
            "baureihe", baureihe,
            "katalogumfang", katalogumfang,
            "karosserie", karosserie,
            "modell", modell,
            "region", region,
            "iso", iso,
            "regiso", regiso
        ), LENKUNG_MAPPER);
    }

    /**
     * Retrieves transmission options for a model selection.
     */
    public List<GetriebeDto> findTransmissions(String baureihe, String katalogumfang, String karosserie,
                                               String modell, String region, String iso, String regiso,
                                               String lenkungClause, Map<String, Object> extraParams) {
        String sql = applyClauses(RETRIEVE_GETRIEBEARTEN, Map.of("__LENKUNG_STMT__", lenkungClause));
        Map<String, Object> params = new HashMap<>(Map.of(
            "baureihe", baureihe,
            "katalogumfang", katalogumfang,
            "karosserie", karosserie,
            "modell", modell,
            "region", region,
            "iso", iso,
            "regiso", regiso
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, GETRIEBE_MAPPER);
    }

    /**
     * Retrieves production years for a vehicle selection.
     */
    public List<BaujahrDto> findProductionYears(String baureihe, String katalogumfang, String modell, String region,
                                                String karosserieClause, String lenkungClause, String getriebeClause,
                                                Map<String, Object> extraParams) {
        String sql = applyClauses(RETRIEVE_BAUJAHRE, Map.of(
            "__KAROSSERIE_STMT__", karosserieClause,
            "__LENKUNG_STMT__", lenkungClause,
            "__GETRIEBE_STMT__", getriebeClause
        ));
        Map<String, Object> params = new HashMap<>(Map.of(
            "baureihe", baureihe,
            "katalogumfang", katalogumfang,
            "modell", modell,
            "region", region
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, BAUJAHR_MAPPER);
    }

    /**
     * Retrieves registration months for a specific production year.
     */
    public List<ZulassungsmonatDto> findRegistrationMonthsForYear(String baureihe, String katalogumfang, String modell,
                                                                  String region, String baujahr,
                                                                  String iso, String regiso,
                                                                  String karosserieClause, String lenkungClause,
                                                                  String getriebeClause, Map<String, Object> extraParams) {
        String sql = applyClauses(RETRIEVE_ZULASSMONATE, Map.of(
            "__KAROSSERIE_STMT__", karosserieClause,
            "__LENKUNG_STMT__", lenkungClause,
            "__GETRIEBE_STMT__", getriebeClause
        ));
        Map<String, Object> params = new HashMap<>(Map.of(
            "baureihe", baureihe,
            "katalogumfang", katalogumfang,
            "modell", modell,
            "region", region,
            "baujahr", baujahr,
            "iso", iso,
            "regiso", regiso
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, ZULASSUNGSMONAT_MAPPER);
    }

    /**
     * Retrieves registration months regardless of production year.
     */
    public List<ZulassungsmonatDto> findRegistrationMonths(String baureihe, String katalogumfang, String modell,
                                                           String region, String iso, String regiso,
                                                           String karosserieClause, String lenkungClause,
                                                           String getriebeClause, Map<String, Object> extraParams) {
        String sql = applyClauses(RETRIEVE_ZULASSMONATE2, Map.of(
            "__KAROSSERIE_STMT__", karosserieClause,
            "__LENKUNG_STMT__", lenkungClause,
            "__GETRIEBE_STMT__", getriebeClause
        ));
        Map<String, Object> params = new HashMap<>(Map.of(
            "baureihe", baureihe,
            "katalogumfang", katalogumfang,
            "modell", modell,
            "region", region,
            "iso", iso,
            "regiso", regiso
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, ZULASSUNGSMONAT_MAPPER);
    }

    /**
     * Retrieves model columns for passenger cars by attributes.
     */
    public List<MospIdDto> findModelColumnsForPassengerCars(String baureihe, String karosserie, String modell,
                                                            String region) {
        return jdbc.query(RETRIEVE_MOSP_BY_ATTRIBUTE_PKW, Map.of(
            "baureihe", baureihe,
            "karosserie", karosserie,
            "modell", modell,
            "region", region
        ), MOSP_MAPPER);
    }

    /**
     * Retrieves model columns for motorcycles by attributes.
     */
    public List<MospIdDto> findModelColumnsForMotorcycles(String baureihe, String modell, String region) {
        return jdbc.query(RETRIEVE_MOSP_BY_ATTRIBUTE_KRAD, Map.of(
            "baureihe", baureihe,
            "modell", modell,
            "region", region
        ), MOSP_MAPPER);
    }

    /**
     * Retrieves vehicle identification data by VIN.
     */
    public List<VehicleIdentificationDto> findByVin(String fgstnr, String fgstnrFirst2, String iso, String regiso) {
        return jdbc.query(RETRIEVE_MOSP_BY_FGSTNR, Map.of(
            "fgstnr", fgstnr,
            "fgstnrFirst2", fgstnrFirst2,
            "iso", iso,
            "regiso", regiso
        ), VEHICLE_IDENTIFICATION_MAPPER);
    }

    /**
     * Retrieves vehicle identification data by type.
     */
    public List<VehicleIdentificationDto> findByType(String typ, String prodDatum, String iso, String regiso) {
        return jdbc.query(RETRIEVE_MOSP_BY_TYP, Map.of(
            "typ", typ,
            "prodDatum", prodDatum,
            "iso", iso,
            "regiso", regiso
        ), VEHICLE_IDENTIFICATION_MAPPER);
    }

    /**
     * Retrieves type list for passenger car attributes.
     */
    public List<TypDto> findTypesForPassengerCars(String mospid, String lenkung, String getriebe, String prodDatum) {
        return jdbc.query(RETRIEVE_TYPMENGE_BY_ATTRIBUTE_PKW, Map.of(
            "mospid", mospid,
            "lenkung", lenkung,
            "getriebe", getriebe,
            "prodDatum", prodDatum
        ), TYP_MAPPER);
    }

    /**
     * Retrieves type list for motorcycles.
     */
    public List<TypDto> findTypesForMotorcycles(String mospid, String prodDatum) {
        return jdbc.query(RETRIEVE_TYPMENGE_BY_ATTRIBUTE_KRAD, Map.of(
            "mospid", mospid,
            "prodDatum", prodDatum
        ), TYP_MAPPER);
    }

    /**
     * Retrieves steering designation text.
     */
    public List<DesignationDto> findSteeringDesignation(String value, String iso, String regiso) {
        return jdbc.query(RETRIEVE_LENKUNG_BEN, Map.of(
            "value", value,
            "iso", iso,
            "regiso", regiso
        ), DESIGNATION_MAPPER);
    }

    /**
     * Loads SALA options for a VIN.
     */
    public List<SalaDto> loadSalaForVin(String fgstnr, String iso, String regiso) {
        return jdbc.query(LOAD_SALAS_ZU_FGSTNR, Map.of(
            "fgstnr", fgstnr,
            "iso", iso,
            "regiso", regiso
        ), SALA_MAPPER);
    }

    /**
     * Loads AF conditions for a production date.
     */
    public List<BedConditionDto> loadAfConditions(String prodDatum, String iso, String regiso,
                                                  String decideClause, Map<String, Object> extraParams) {
        String sql = applyClauses(LOAD_SS_BEDINGUNGEN_AF, Map.of(
            "__LOAD_SS_BEDINGUNGEN_AF_DECIDE__", decideClause
        ));
        Map<String, Object> params = new HashMap<>(Map.of(
            "prodDatum", prodDatum,
            "iso", iso,
            "regiso", regiso
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, BED_CONDITION_MAPPER);
    }

    /**
     * Loads paint conditions for a production date.
     */
    public List<PaintConditionDto> loadPaintConditions(String prodDatum, String iso, String regiso,
                                                       String decideClause, Map<String, Object> extraParams) {
        String sql = applyClauses(LOAD_SS_BEDINGUNGEN_LACK, Map.of(
            "__LOAD_SS_BEDINGUNGEN_LACK_DECIDE__", decideClause
        ));
        Map<String, Object> params = new HashMap<>(Map.of(
            "prodDatum", prodDatum,
            "iso", iso,
            "regiso", regiso
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, PAINT_CONDITION_MAPPER);
    }

    /**
     * Loads SALAPA conditions for a production date.
     */
    public List<SalaConditionDto> loadSalapaConditions(String produktart, String primaNummer, String prodDatum,
                                                       String iso, String regiso,
                                                       String decideArtClause, String decideHzaehlerClause,
                                                       Map<String, Object> extraParams) {
        String sql = applyClauses(LOAD_SS_BEDINGUNGEN_SALAPA, Map.of(
            "__LOAD_SS_BEDINGUNGEN_SALAPA_DECIDE_ART__", decideArtClause,
            "__LOAD_SS_BEDINGUNGEN_SALAPA_DECIDE_HZAEHLER__", decideHzaehlerClause
        ));
        Map<String, Object> params = new HashMap<>(Map.of(
            "produktart", produktart,
            "primaNummer", primaNummer,
            "prodDatum", prodDatum,
            "iso", iso,
            "regiso", regiso
        ));
        if (extraParams != null) {
            params.putAll(extraParams);
        }
        return jdbc.query(sql, params, SALA_CONDITION_MAPPER);
    }

    /**
     * Retrieves additional bed information.
     */
    public List<BedZusatzinfoDto> findAdditionalBedInfo(List<String> bedElemIds, String iso, String regiso) {
        return jdbc.query(RETRIEVE_BED_ZUSATZINFO, Map.of(
            "bedElemIds", bedElemIds,
            "iso", iso,
            "regiso", regiso
        ), BED_ZUSATZINFO_MAPPER);
    }

    private static String applyClauses(String sql, Map<String, String> replacements) {
        String result = sql;
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            String clause = entry.getValue();
            result = result.replace(entry.getKey(), StringUtils.hasText(clause) ? " " + clause + " " : " ");
        }
        return result;
    }
}
