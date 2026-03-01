package pl.emdzej.etkx.dal.repository.diagram;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pl.emdzej.etkx.dal.dto.admin.GraphicDto;
import pl.emdzej.etkx.dal.dto.diagram.DiagramAtbDto;
import pl.emdzej.etkx.dal.dto.diagram.DiagramCommentDto;
import pl.emdzej.etkx.dal.dto.diagram.DiagramCommentShortDto;
import pl.emdzej.etkx.dal.dto.diagram.DiagramConditionDetailDto;
import pl.emdzej.etkx.dal.dto.diagram.DiagramConditionDto;
import pl.emdzej.etkx.dal.dto.diagram.DiagramCpLineDto;
import pl.emdzej.etkx.dal.dto.diagram.DiagramHotspotDto;
import pl.emdzej.etkx.dal.dto.diagram.DiagramLineFzgDto;
import pl.emdzej.etkx.dal.dto.diagram.DiagramLineUgbDto;
import pl.emdzej.etkx.dal.dto.diagram.DiagramOverConditionDto;
import pl.emdzej.etkx.dal.dto.diagram.DiagramReferenceDto;
import pl.emdzej.etkx.dal.dto.diagram.DiagramYesNoTextDto;

/**
 * Repository for diagram display workflows (BteAnzeige).
 */
@Repository
@RequiredArgsConstructor
public class DiagramDisplayRepository {
    private static final String LOAD_HOTSPOTS = """
        select grafikhs_bildposnr Bildnummer,
            grafikhs_topleft_x TopLeft_x,
            grafikhs_topleft_y TopLeft_y,
            grafikhs_bottomright_x BottomRight_x,
            grafikhs_bottomright_y BottomRight_y
        from w_grafik_hs
        where grafikhs_grafikid = :grafikId
          and grafikhs_art = :art
        """;

    private static final String LOAD_DIAGRAM_GRAFIK = """
        select grafik_blob Grafik,
            grafik_format Format,
            grafik_moddate ModStamp
        from w_bildtaf
        inner join w_grafik on (bildtaf_grafikid = grafik_grafikid)
        where bildtaf_btnr = :btnr
          and grafik_art = :art
        """;

    private static final String LOAD_GRAFIK_BY_ID = """
        select grafik_blob Grafik,
            grafik_format Format,
            grafik_moddate ModStamp
        from w_grafik
        where grafik_grafikid = :grafikId
          and grafik_art = :art
        """;

    private static final String LOAD_BTZEILEN_FZG = """
        select distinct btzeilen_bildposnr Bildnummer,
            teil_hauptgr Teil_HG,
            teil_untergrup Teil_UG,
            teil_sachnr Teil_Sachnummer,
            tben.ben_text Teil_Benennung,
            teil_benennzus Teil_Zusatz,
            teil_entfall_kez Teil_Entfall,
            teil_textcode_kom Teil_Kommentar_Id,
            tkben.ben_text Teil_Kommentar,
            teil_kom_pi Teil_Komm_PI,
            teil_vorhanden_si Teil_SI,
            teil_ist_reach Teil_Reach,
            teil_ist_aspg Teil_Aspg,
            teil_ist_stecker Teil_Stecker,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant,
            si_dokart SI_DokArt,
            decode(tcp_sachnr, tcp_sachnr, 'C', NULL) Teil_TC,
            COALESCE(tcp_proddat_rel, 'N') Teil_TC_ProdDatRelevant,
            grpinfo_leitaw_pa GRP_PA,
            grpinfo_leitaw_hg GRP_HG,
            grpinfo_leitaw_ug GRP_UG,
            grpinfo_leitaw_nr GRP_lfdNr,
            btzeilenv_vmenge Menge,
            btzeilen_kat Kat_KZ,
            btzeilen_automatik Getriebe_KZ,
            btzeilen_lenkg Lenkung_KZ,
            btzeilen_eins Einsatz,
            btzeilen_auslf Auslauf,
            btzeilen_bedkez || nvl(CAST(btzeilen_regelnr AS TEXT), '') Bedingung_KZ,
            btzeilen_kommbt KommBT,
            btzeilen_kommvor KommVor,
            btzeilen_kommnach KommNach,
            ks_sachnr_satz Satz_Sachnummer,
            btzeilen_gruppeid GruppeId,
            btzeilen_blocknr BlockNr,
            bnbben.ben_text BnbBenText,
            btzeilen_pos Pos,
            btzeilenv_alter_kz BtZAlter,
            btzeilen_bedkez_pg Teil_BedkezPG,
            btzeilenv_bed_art BedingungArt,
            btzeilenv_bed_alter BedingungAlter
        from w_btzeilen_verbauung
        inner join w_btzeilen on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)
        inner join w_teil on (btzeilen_sachnr = teil_sachnr)
        inner join w_ben_gk tben on (
            teil_textcode = tben.ben_textcode
            and tben.ben_iso = :iso
            and tben.ben_regiso = :regiso
        )
        left join w_kompl_satz on (
            btzeilen_sachnr = ks_sachnr_satz
            and ks_marke_tps = :marke
        )
        left join w_tc_performance on (
            tcp_mospid = :mosp
            and tcp_sachnr = btzeilen_sachnr
            __TC_CHECK_LANDKUERZEL__
            and tcp_datum_von <= :datum
            and (tcp_datum_bis is null or tcp_datum_bis >= :datum)
        )
        left join w_grp_information on (
            btzeilenv_mospid = grpinfo_mospid
            and grpinfo_sachnr = btzeilen_sachnr
            and grpinfo_typ = :typ
        )
        left join w_ben_gk tkben on (
            teil_textcode_kom = tkben.ben_textcode
            and tkben.ben_iso = :iso
            and tkben.ben_regiso = :regiso
        )
        left join w_si on (si_sachnr = teil_sachnr)
        left join w_bildtaf_bnbben on (
            bildtafb_btnr = btzeilenv_btnr
            and bildtafb_bildposnr = btzeilen_bildposnr
        )
        left join w_ben_gk bnbben on (
            bildtafb_textcode = bnbben.ben_textcode
            and bnbben.ben_iso = :iso
            and bnbben.ben_regiso = :regiso
        )
        where btzeilenv_mospid = :mosp
          and btzeilenv_btnr = :btnr
        order by Pos, GRP_PA, GRP_HG, GRP_UG, GRP_lfdNr, SI_DokArt
        """;

    private static final String LOAD_BTZEILEN_CP_FZG = """
        select distinct btzeilenc_pos Pos,
            btzeilenc_typschl Typ,
            btzeilenc_werk Werk,
            btzeilenc_art Art,
            btzeilenc_datum Datum,
            btzeilenc_vin Vin,
            btzeilenc_vin_proddatum VinProddatum,
            btzeilenc_vin_min VinMin,
            btzeilenc_vin_max VinMax,
            btzeilenc_nart ArtNummer,
            btzeilenc_nummer Nummer,
            btzeilenc_alter CPAlter
        from w_btzeilen_cp
        where btzeilenc_mospid = :mosp
          and btzeilenc_btnr = :btnr
          __CP_FZG_TYP_WERK__
        order by Pos
        """;

    private static final String LOAD_BTZEILEN_UGB = """
        select distinct btzeilenu_bildposnr Bildnummer,
            teil_hauptgr Teil_HG,
            teil_untergrup Teil_UG,
            teil_sachnr Teil_Sachnummer,
            tben.ben_text Teil_Benennung,
            teil_benennzus Teil_Zusatz,
            teil_entfall_kez Teil_Entfall,
            teil_textcode_kom Teil_Kommentar_Id,
            tkben.ben_text Teil_Kommentar,
            teil_kom_pi Teil_Komm_PI,
            teil_vorhanden_si Teil_SI,
            teil_ist_reach Teil_Reach,
            teil_ist_aspg Teil_Aspg,
            teil_ist_stecker Teil_Stecker,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant,
            si_dokart SI_DokArt,
            decode(teil_sachnr, tcp_sachnr, 'C', NULL) Teil_TC,
            'N' Teil_TC_ProdDatRelevant,
            btzeilenu_mmg MMG,
            btzeilenu_emg EMG,
            btzeilenu_eins Einsatz,
            btzeilenu_ausl Auslauf,
            btzeilenu_kommbt KommBT,
            btzeilenu_kommvor KommVor,
            btzeilenu_kommnach KommNach,
            ks_sachnr_satz Satz_Sachnummer,
            0 GruppeId,
            0 BlockNr,
            bnbben.ben_text BnbBenText,
            btzeilenu_pos Pos
        from w_btzeilenugb_verbauung
        inner join w_btzeilenugb on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos)
        inner join w_teil on (btzeilenu_sachnr = teil_sachnr)
        inner join w_ben_gk tben on (
            teil_textcode = tben.ben_textcode
            and tben.ben_iso = :iso
            and tben.ben_regiso = :regiso
        )
        left join w_kompl_satz on (
            btzeilenu_sachnr = ks_sachnr_satz
            and ks_marke_tps = :marke
        )
        left join w_tc_performance_allg on (
            btzeilenu_sachnr = tcp_sachnr
            and tcp_marke_tps = :marke
            and tcp_produktart = :produktart
            and tcp_vbereich = :katalogumfang
            __TC_CHECK_LANDKUERZEL__
            and tcp_datum_von <= :datum
            and (tcp_datum_bis is null or tcp_datum_bis <= :datum)
        )
        left join w_ben_gk tkben on (
            teil_textcode_kom = tkben.ben_textcode
            and tkben.ben_iso = :iso
            and tkben.ben_regiso = :regiso
        )
        left join w_si on (si_sachnr = teil_sachnr)
        left join w_bildtaf_bnbben on (
            bildtafb_btnr = btzeilenu_btnr
            and bildtafb_bildposnr = btzeilenu_bildposnr
        )
        left join w_ben_gk bnbben on (
            bildtafb_textcode = bnbben.ben_textcode
            and bnbben.ben_iso = :iso
            and bnbben.ben_regiso = :regiso
        )
        where btzeilenuv_marke_tps = :marke
          and btzeilenuv_btnr = :btnr
        order by Pos
        """;

    private static final String LOAD_KOMMENTARE_FZG = """
        select distinct komm_id KommId,
            ben_text Text,
            komm_code Code,
            komm_vz VZ,
            komm_darstellung Darstellung,
            komm_tiefe Tiefe,
            komm_pos Pos
        from w_komm_help, w_ben_gk, w_komm
        where kommh_mospid = :mosp
          and kommh_btnr = :btnr
          and kommh_id = komm_id
          and komm_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by KommId, Pos
        """;

    private static final String LOAD_KOMMENTARE_UGB = """
        select distinct komm_id KommId,
            ben_text Text,
            komm_pos Pos
        from w_kommugb_help, w_ben_gk, w_komm
        where kommuh_marke_tps = :marke
          and kommuh_btnr = :btnr
          and kommuh_id = komm_id
          and komm_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by KommId, Pos
        """;

    private static final String LOAD_BEDINGUNGEN_FZG = """
        select distinct btebg_kez Kuerzel,
            btebg_vz GesamttermVZ,
            btebg_gesamtterm Gesamtterm,
            btebo_ogid OG,
            btebo_vart VArt,
            btebo_fzeile FZeile,
            btebe_vz ElementVZ,
            btebe_elemid ElementId,
            btebe_pos Pos
        from w_bte_bedkurz, w_bte_bedelem, w_bte_bedog, w_bte_bedgesamt
        where btebk_btnr = :btnr
          and btebk_mospid = :mosp
          and btebk_btnr = btebg_btnr
          and btebk_kez = btebg_kez
          and btebg_btnr = btebo_btnr
          and btebg_kez = btebo_kez
          and btebo_btnr = btebe_btnr
          and btebo_kez = btebe_kez
          and btebo_ogid = btebe_ogid
        union
        select distinct btebg_kez Kuerzel,
            btebg_vz GesamttermVZ,
            btebg_gesamtterm Gesamtterm,
            btebo_ogid OG,
            btebo_vart VArt,
            btebo_fzeile FZeile,
            btebe_vz ElementVZ,
            btebe_elemid ElementId,
            btebe_pos Pos
        from w_bildtaf, w_bte_bedelem, w_bte_bedog, w_bte_bedgesamt
        where bildtaf_btnr = :btnr
          and bildtaf_bedkez is not null
          and bildtaf_btnr = btebg_btnr
          and bildtaf_bedkez = btebg_kez
          and btebg_btnr = btebo_btnr
          and btebg_kez = btebo_kez
          and btebo_btnr = btebe_btnr
          and btebo_kez = btebe_kez
          and btebo_ogid = btebe_ogid
        order by Kuerzel, OG, Pos
        """;

    private static final String LOAD_BTE_BEDINGUNGEN_FZG = """
        select distinct btebg_vz GesamttermVZ,
            btebg_gesamtterm Gesamtterm,
            btebo_ogid OG,
            btebo_vart VArt,
            btebo_fzeile FZeile,
            btebe_vz ElementVZ,
            btebe_elemid ElementId,
            btebe_pos Pos
        from w_bte_bedelem, w_bte_bedog, w_bte_bedgesamt
        where btebg_btnr = :btnr
          and btebg_kez = :bedkez
          and btebo_btnr = btebg_btnr
          and btebo_kez = btebg_kez
          and btebe_btnr = btebg_btnr
          and btebe_kez = btebg_kez
          and btebe_ogid = btebo_ogid
        order by OG, Pos
        """;

    private static final String LOAD_UEBERBEDINGUNGEN_FZG = """
        select distinct btebu_kez Kuerzel,
            btebu_kezueber KuerzelUeber
        from w_bte_bedkurz, w_bte_bedueber
        where btebk_btnr = :btnr
          and btebk_mospid = :mosp
          and btebu_btnr = btebk_btnr
          and btebu_kez = btebk_kez
        union
        select distinct btebu_kez Kuerzel,
            btebu_kezueber KuerzelUeber
        from w_bte_bedkurz, w_bte_bedueber
        where btebk_btnr = :btnr
          and btebk_mospid = :mosp
          and btebu_btnr = btebk_btnr
          and btebu_kezueber = btebk_kez
        order by Kuerzel, KuerzelUeber
        """;

    private static final String LOAD_BTVERWEISE_FZG = """
        select distinct bv_btnr_nach Bildtafelnummer,
            bt.ben_text Ueberschrift,
            bv.ben_text Text,
            komm_pos Pos
        from w_bildtaf_verweis, w_ben_gk bt, w_ben_gk bv, w_komm, w_bildtaf, w_bildtaf_suche
        where bv_btnr_von = :btnr
          and bildtafs_hg = substr(bv_btnr_nach, 1, 2)
          and bildtafs_mospid = :mosp
          and bildtafs_btnr = bv_btnr_nach
          and bv_kommid = komm_id
          and komm_textcode = bv.ben_textcode
          and bv.ben_iso = :iso
          and bv.ben_regiso = :regiso
          and bv_btnr_nach = bildtaf_btnr
          and bildtaf_textc = bt.ben_textcode
          and bt.ben_iso = :iso
          and bt.ben_regiso = :regiso
        order by Bildtafelnummer, Pos
        """;

    private static final String LOAD_BTVERWEISE_UGB = """
        select distinct bv_btnr_nach Bildtafelnummer,
            bt.ben_text Ueberschrift,
            bv.ben_text Text,
            komm_pos Pos
        from w_bildtaf_verweis, w_ben_gk bt, w_ben_gk bv, w_komm, w_bildtaf, w_btzeilenugb_verbauung
        where bv_btnr_von = :btnr
          and bv_btnr_nach = btzeilenuv_btnr
          and btzeilenuv_marke_tps = :marke
          and bv_kommid = komm_id
          and komm_textcode = bv.ben_textcode
          and bv.ben_iso = :iso
          and bv.ben_regiso = :regiso
          and bv_btnr_nach = bildtaf_btnr
          and bildtaf_produktart = :produktart
          and bildtaf_textc = bt.ben_textcode
          and bt.ben_iso = :iso
          and bt.ben_regiso = :regiso
        order by Bildtafelnummer, Pos
        """;

    private static final String LOAD_JA_NEIN_TEXT = """
        select distinct publben_bezeichnung Bezeichnung,
            ben_text Benennung
        from w_publben, w_ben_gk
        where publben_art = 'V'
          and publben_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by Bezeichnung
        """;

    private static final String LOAD_AZEICHEN = """
        select distinct teilatb_kennz ATB,
            teilatb_bap BAP
        from w_teil_atb
        where teilatb_sachnr_alt = :snr1
          and teilatb_sachnr_neu = :snr2
        """;

    private static final String LOAD_ANZAHL_REL_KAMPAGNEN = """
        select Count(*) Anzahl
        from w_tc_sachnummer, w_tc_kampagne_proddatum, w_tc_kampagne
        where tckp_mospid = :mospid
          and tckp_proddatum_von <= :proddatumMax
          and nvl(tckp_proddatum_bis, 99999999) >= :proddatumMin
          and tcs_id = tckp_id
          and tcs_sachnr = :sachnummer
          and tck_id = tcs_id
          __TC_CHECK_LANDKUERZEL_KAMPAGNE__
        """;

    private static final RowMapper<DiagramHotspotDto> HOTSPOT_MAPPER = (rs, rowNum) ->
        DiagramHotspotDto.builder()
            .bildnummer(rs.getString("Bildnummer"))
            .topLeftX(rs.getString("TopLeft_x"))
            .topLeftY(rs.getString("TopLeft_y"))
            .bottomRightX(rs.getString("BottomRight_x"))
            .bottomRightY(rs.getString("BottomRight_y"))
            .build();

    private static final RowMapper<DiagramLineFzgDto> BTZEILEN_FZG_MAPPER = (rs, rowNum) ->
        DiagramLineFzgDto.builder()
            .bildnummer(rs.getString("Bildnummer"))
            .teilHg(rs.getString("Teil_HG"))
            .teilUg(rs.getString("Teil_UG"))
            .teilSachnummer(rs.getString("Teil_Sachnummer"))
            .teilBenennung(rs.getString("Teil_Benennung"))
            .teilZusatz(rs.getString("Teil_Zusatz"))
            .teilEntfall(rs.getString("Teil_Entfall"))
            .teilKommentarId(rs.getString("Teil_Kommentar_Id"))
            .teilKommentar(rs.getString("Teil_Kommentar"))
            .teilKommPi(rs.getString("Teil_Komm_PI"))
            .teilSi(rs.getString("Teil_SI"))
            .teilReach(rs.getString("Teil_Reach"))
            .teilAspg(rs.getString("Teil_Aspg"))
            .teilStecker(rs.getString("Teil_Stecker"))
            .teilDiebstahlrelevant(rs.getString("Teil_Diebstahlrelevant"))
            .siDokArt(rs.getString("SI_DokArt"))
            .teilTc(rs.getString("Teil_TC"))
            .teilTcProdDatRelevant(rs.getString("Teil_TC_ProdDatRelevant"))
            .grpPa(rs.getString("GRP_PA"))
            .grpHg(rs.getString("GRP_HG"))
            .grpUg(rs.getString("GRP_UG"))
            .grpLfdNr(rs.getString("GRP_lfdNr"))
            .menge(rs.getString("Menge"))
            .katKz(rs.getString("Kat_KZ"))
            .getriebeKz(rs.getString("Getriebe_KZ"))
            .lenkungKz(rs.getString("Lenkung_KZ"))
            .einsatz(rs.getString("Einsatz"))
            .auslauf(rs.getString("Auslauf"))
            .bedingungKz(rs.getString("Bedingung_KZ"))
            .kommBt(rs.getString("KommBT"))
            .kommVor(rs.getString("KommVor"))
            .kommNach(rs.getString("KommNach"))
            .satzSachnummer(rs.getString("Satz_Sachnummer"))
            .gruppeId(rs.getString("GruppeId"))
            .blockNr(rs.getString("BlockNr"))
            .bnbBenText(rs.getString("BnbBenText"))
            .pos(rs.getString("Pos"))
            .btzAlter(rs.getString("BtZAlter"))
            .teilBedkezPg(rs.getString("Teil_BedkezPG"))
            .bedingungArt(rs.getString("BedingungArt"))
            .bedingungAlter(rs.getString("BedingungAlter"))
            .build();

    private static final RowMapper<DiagramCpLineDto> BTZEILEN_CP_MAPPER = (rs, rowNum) ->
        DiagramCpLineDto.builder()
            .pos(rs.getString("Pos"))
            .typ(rs.getString("Typ"))
            .werk(rs.getString("Werk"))
            .art(rs.getString("Art"))
            .datum(rs.getString("Datum"))
            .vin(rs.getString("Vin"))
            .vinProddatum(rs.getString("VinProddatum"))
            .vinMin(rs.getString("VinMin"))
            .vinMax(rs.getString("VinMax"))
            .artNummer(rs.getString("ArtNummer"))
            .nummer(rs.getString("Nummer"))
            .cpAlter(rs.getString("CPAlter"))
            .build();

    private static final RowMapper<DiagramLineUgbDto> BTZEILEN_UGB_MAPPER = (rs, rowNum) ->
        DiagramLineUgbDto.builder()
            .bildnummer(rs.getString("Bildnummer"))
            .teilHg(rs.getString("Teil_HG"))
            .teilUg(rs.getString("Teil_UG"))
            .teilSachnummer(rs.getString("Teil_Sachnummer"))
            .teilBenennung(rs.getString("Teil_Benennung"))
            .teilZusatz(rs.getString("Teil_Zusatz"))
            .teilEntfall(rs.getString("Teil_Entfall"))
            .teilKommentarId(rs.getString("Teil_Kommentar_Id"))
            .teilKommentar(rs.getString("Teil_Kommentar"))
            .teilKommPi(rs.getString("Teil_Komm_PI"))
            .teilSi(rs.getString("Teil_SI"))
            .teilReach(rs.getString("Teil_Reach"))
            .teilAspg(rs.getString("Teil_Aspg"))
            .teilStecker(rs.getString("Teil_Stecker"))
            .teilDiebstahlrelevant(rs.getString("Teil_Diebstahlrelevant"))
            .siDokArt(rs.getString("SI_DokArt"))
            .teilTc(rs.getString("Teil_TC"))
            .teilTcProdDatRelevant(rs.getString("Teil_TC_ProdDatRelevant"))
            .mmg(rs.getString("MMG"))
            .emg(rs.getString("EMG"))
            .einsatz(rs.getString("Einsatz"))
            .auslauf(rs.getString("Auslauf"))
            .kommBt(rs.getString("KommBT"))
            .kommVor(rs.getString("KommVor"))
            .kommNach(rs.getString("KommNach"))
            .satzSachnummer(rs.getString("Satz_Sachnummer"))
            .gruppeId(rs.getString("GruppeId"))
            .blockNr(rs.getString("BlockNr"))
            .bnbBenText(rs.getString("BnbBenText"))
            .pos(rs.getString("Pos"))
            .build();

    private static final RowMapper<DiagramCommentDto> COMMENT_MAPPER = (rs, rowNum) ->
        DiagramCommentDto.builder()
            .kommId(rs.getString("KommId"))
            .text(rs.getString("Text"))
            .code(rs.getString("Code"))
            .vz(rs.getString("VZ"))
            .darstellung(rs.getString("Darstellung"))
            .tiefe(rs.getString("Tiefe"))
            .pos(rs.getString("Pos"))
            .build();

    private static final RowMapper<DiagramCommentShortDto> COMMENT_SHORT_MAPPER = (rs, rowNum) ->
        DiagramCommentShortDto.builder()
            .kommId(rs.getString("KommId"))
            .text(rs.getString("Text"))
            .pos(rs.getString("Pos"))
            .build();

    private static final RowMapper<DiagramConditionDto> CONDITION_MAPPER = (rs, rowNum) ->
        DiagramConditionDto.builder()
            .kuerzel(rs.getString("Kuerzel"))
            .gesamttermVz(rs.getString("GesamttermVZ"))
            .gesamtterm(rs.getString("Gesamtterm"))
            .og(rs.getString("OG"))
            .vArt(rs.getString("VArt"))
            .fZeile(rs.getString("FZeile"))
            .elementVz(rs.getString("ElementVZ"))
            .elementId(rs.getString("ElementId"))
            .pos(rs.getString("Pos"))
            .build();

    private static final RowMapper<DiagramConditionDetailDto> CONDITION_DETAIL_MAPPER = (rs, rowNum) ->
        DiagramConditionDetailDto.builder()
            .gesamttermVz(rs.getString("GesamttermVZ"))
            .gesamtterm(rs.getString("Gesamtterm"))
            .og(rs.getString("OG"))
            .vArt(rs.getString("VArt"))
            .fZeile(rs.getString("FZeile"))
            .elementVz(rs.getString("ElementVZ"))
            .elementId(rs.getString("ElementId"))
            .pos(rs.getString("Pos"))
            .build();

    private static final RowMapper<DiagramOverConditionDto> OVER_CONDITION_MAPPER = (rs, rowNum) ->
        DiagramOverConditionDto.builder()
            .kuerzel(rs.getString("Kuerzel"))
            .kuerzelUeber(rs.getString("KuerzelUeber"))
            .build();

    private static final RowMapper<DiagramReferenceDto> REFERENCE_MAPPER = (rs, rowNum) ->
        DiagramReferenceDto.builder()
            .bildtafelnummer(rs.getString("Bildtafelnummer"))
            .ueberschrift(rs.getString("Ueberschrift"))
            .text(rs.getString("Text"))
            .pos(rs.getString("Pos"))
            .build();

    private static final RowMapper<DiagramYesNoTextDto> YES_NO_MAPPER = (rs, rowNum) ->
        DiagramYesNoTextDto.builder()
            .bezeichnung(rs.getString("Bezeichnung"))
            .benennung(rs.getString("Benennung"))
            .build();

    private static final RowMapper<DiagramAtbDto> ATB_MAPPER = (rs, rowNum) ->
        DiagramAtbDto.builder()
            .atb(rs.getString("ATB"))
            .bap(rs.getString("BAP"))
            .build();

    private static final RowMapper<GraphicDto> GRAPHIC_MAPPER = (rs, rowNum) ->
        GraphicDto.builder()
            .grafik(rs.getBytes("Grafik"))
            .format(rs.getString("Format"))
            .modStamp(rs.getString("ModStamp"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Loads the diagram graphic for a plate number.
     */
    public GraphicDto loadDiagramGraphic(String btnr, String art) {
        return jdbc.queryForObject(LOAD_DIAGRAM_GRAFIK, Map.of("btnr", btnr, "art", art), GRAPHIC_MAPPER);
    }

    /**
     * Loads the graphic for a grafik identifier.
     */
    public GraphicDto loadGraphicById(Long grafikId, String art) {
        return jdbc.queryForObject(LOAD_GRAFIK_BY_ID, Map.of("grafikId", grafikId, "art", art), GRAPHIC_MAPPER);
    }

    /**
     * Retrieves graphic hotspots for a diagram.
     */
    public List<DiagramHotspotDto> findHotspots(long grafikId, String art) {
        return jdbc.query(LOAD_HOTSPOTS, Map.of("grafikId", grafikId, "art", art), HOTSPOT_MAPPER);
    }

    /**
     * Retrieves diagram lines for vehicle-specific diagrams.
     */
    public List<DiagramLineFzgDto> findVehicleDiagramLines(long mosp, String btnr, String marke,
                                                           String iso, String regiso,
                                                           String typ, long datum,
                                                           String landkuerzel) {
        String sql = applyClauses(LOAD_BTZEILEN_FZG, Map.of(
            "__TC_CHECK_LANDKUERZEL__",
            StringUtils.hasText(landkuerzel) ? "and tcp_landkuerzel = :landkuerzel" : ""
        ));
        Map<String, Object> params = new HashMap<>(Map.of(
            "mosp", mosp,
            "btnr", btnr,
            "marke", marke,
            "iso", iso,
            "regiso", regiso,
            "typ", typ,
            "datum", datum
        ));
        if (StringUtils.hasText(landkuerzel)) {
            params.put("landkuerzel", landkuerzel);
        }
        return jdbc.query(sql, params, BTZEILEN_FZG_MAPPER);
    }

    /**
     * Retrieves CP-specific lines for vehicle diagrams.
     */
    public List<DiagramCpLineDto> findVehicleCpLines(long mosp, String btnr, String typ, String werk) {
        String clause = StringUtils.hasText(typ) && StringUtils.hasText(werk)
            ? "and btzeilenc_typschl = :typ and btzeilenc_werk = :werk"
            : "";
        String sql = applyClauses(LOAD_BTZEILEN_CP_FZG, Map.of("__CP_FZG_TYP_WERK__", clause));
        Map<String, Object> params = new HashMap<>(Map.of("mosp", mosp, "btnr", btnr));
        if (StringUtils.hasText(typ)) {
            params.put("typ", typ);
        }
        if (StringUtils.hasText(werk)) {
            params.put("werk", werk);
        }
        return jdbc.query(sql, params, BTZEILEN_CP_MAPPER);
    }

    /**
     * Retrieves diagram lines for UGB diagrams.
     */
    public List<DiagramLineUgbDto> findUgbDiagramLines(String marke, String btnr, String produktart,
                                                       String katalogumfang, String iso, String regiso,
                                                       long datum, String landkuerzel) {
        String sql = applyClauses(LOAD_BTZEILEN_UGB, Map.of(
            "__TC_CHECK_LANDKUERZEL__",
            StringUtils.hasText(landkuerzel) ? "and tcp_landkuerzel = :landkuerzel" : ""
        ));
        Map<String, Object> params = new HashMap<>(Map.of(
            "marke", marke,
            "btnr", btnr,
            "produktart", produktart,
            "katalogumfang", katalogumfang,
            "iso", iso,
            "regiso", regiso,
            "datum", datum
        ));
        if (StringUtils.hasText(landkuerzel)) {
            params.put("landkuerzel", landkuerzel);
        }
        return jdbc.query(sql, params, BTZEILEN_UGB_MAPPER);
    }

    /**
     * Retrieves comments for vehicle diagrams.
     */
    public List<DiagramCommentDto> findVehicleComments(long mosp, String btnr, String iso, String regiso) {
        return jdbc.query(LOAD_KOMMENTARE_FZG, Map.of(
            "mosp", mosp,
            "btnr", btnr,
            "iso", iso,
            "regiso", regiso
        ), COMMENT_MAPPER);
    }

    /**
     * Retrieves comments for UGB diagrams.
     */
    public List<DiagramCommentShortDto> findUgbComments(String marke, String btnr, String iso, String regiso) {
        return jdbc.query(LOAD_KOMMENTARE_UGB, Map.of(
            "marke", marke,
            "btnr", btnr,
            "iso", iso,
            "regiso", regiso
        ), COMMENT_SHORT_MAPPER);
    }

    /**
     * Retrieves condition definitions for vehicle diagrams.
     */
    public List<DiagramConditionDto> findVehicleConditions(String btnr, long mosp) {
        return jdbc.query(LOAD_BEDINGUNGEN_FZG, Map.of("btnr", btnr, "mosp", mosp), CONDITION_MAPPER);
    }

    /**
     * Retrieves detailed condition definitions for vehicle diagrams.
     */
    public List<DiagramConditionDetailDto> findVehicleConditionDetails(String btnr, String bedkez) {
        return jdbc.query(LOAD_BTE_BEDINGUNGEN_FZG, Map.of("btnr", btnr, "bedkez", bedkez),
            CONDITION_DETAIL_MAPPER);
    }

    /**
     * Retrieves over-conditions for vehicle diagrams.
     */
    public List<DiagramOverConditionDto> findVehicleOverConditions(String btnr, long mosp) {
        return jdbc.query(LOAD_UEBERBEDINGUNGEN_FZG, Map.of("btnr", btnr, "mosp", mosp),
            OVER_CONDITION_MAPPER);
    }

    /**
     * Retrieves diagram references for vehicle diagrams.
     */
    public List<DiagramReferenceDto> findVehicleReferences(String btnr, long mosp, String iso, String regiso) {
        return jdbc.query(LOAD_BTVERWEISE_FZG, Map.of(
            "btnr", btnr,
            "mosp", mosp,
            "iso", iso,
            "regiso", regiso
        ), REFERENCE_MAPPER);
    }

    /**
     * Retrieves diagram references for UGB diagrams.
     */
    public List<DiagramReferenceDto> findUgbReferences(String btnr, String marke, String produktart,
                                                       String iso, String regiso) {
        return jdbc.query(LOAD_BTVERWEISE_UGB, Map.of(
            "btnr", btnr,
            "marke", marke,
            "produktart", produktart,
            "iso", iso,
            "regiso", regiso
        ), REFERENCE_MAPPER);
    }

    /**
     * Retrieves yes/no text designations.
     */
    public List<DiagramYesNoTextDto> findYesNoTexts(String iso, String regiso) {
        return jdbc.query(LOAD_JA_NEIN_TEXT, Map.of("iso", iso, "regiso", regiso), YES_NO_MAPPER);
    }

    /**
     * Retrieves ATB information for part replacements.
     */
    public List<DiagramAtbDto> findAtbSigns(String snr1, String snr2) {
        return jdbc.query(LOAD_AZEICHEN, Map.of("snr1", snr1, "snr2", snr2), ATB_MAPPER);
    }

    /**
     * Counts related technical campaigns for a part number.
     */
    public int countRelatedCampaigns(long mospid, long proddatumMin, long proddatumMax,
                                     String sachnummer, String landkuerzel) {
        String sql = applyClauses(LOAD_ANZAHL_REL_KAMPAGNEN, Map.of(
            "__TC_CHECK_LANDKUERZEL_KAMPAGNE__",
            StringUtils.hasText(landkuerzel) ? "and tck_landkuerzel = :landkuerzel" : ""
        ));
        Map<String, Object> params = new HashMap<>(Map.of(
            "mospid", mospid,
            "proddatumMin", proddatumMin,
            "proddatumMax", proddatumMax,
            "sachnummer", sachnummer
        ));
        if (StringUtils.hasText(landkuerzel)) {
            params.put("landkuerzel", landkuerzel);
        }
        Integer count = jdbc.queryForObject(sql, params, Integer.class);
        return count == null ? 0 : count;
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
