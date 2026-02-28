package pl.emdzej.etkx.dal.repository.search;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pl.emdzej.etkx.dal.dto.search.GroupDesignationDto;
import pl.emdzej.etkx.dal.dto.search.GroupThumbnailDto;
import pl.emdzej.etkx.dal.dto.search.PartSearchLineDto;
import pl.emdzej.etkx.dal.dto.search.PlateGraphicSearchResultDto;
import pl.emdzej.etkx.dal.dto.search.PlateSearchResultDto;

/**
 * Repository for assembly-based part search operations.
 */
@Repository
@RequiredArgsConstructor
public class PartSearchAssemblyRepository {
    private static final String RETRIEVE_HGS = """
        select distinct hgfg_hg Hauptgruppe, ben_text Benennung, hgthb_grafikid GrafikId, grafik_moddate ModStamp
        from w_btzeilenugb_verbauung
        inner join w_bildtaf on (
            btzeilenuv_btnr = bildtaf_btnr
            and bildtaf_produktart = :produktart
            and bildtaf_vbereich in ('BE', :katalogumfang)
        )
        inner join w_hgfg on (
            bildtaf_hg = hgfg_hg
            and hgfg_fg = '00'
            and bildtaf_produktart = hgfg_produktart
            and bildtaf_bereich = hgfg_bereich
        )
        inner join w_ben_gk on (
            hgfg_textcode = ben_textcode
            and ben_iso = :iso
            and ben_regiso = :regiso
        )
        left join w_hg_thumbnail on (
            hgfg_hg = hgthb_hg
            and hgthb_marke_tps = :marke
            and hgfg_produktart = hgthb_produktart
            and hgfg_bereich = hgthb_bereich
        )
        left join w_grafik on (hgthb_grafikid = grafik_grafikid and grafik_art = 'T')
        where btzeilenuv_marke_tps = :marke
        order by Hauptgruppe
        """;

    private static final String RETRIEVE_HGS_GRAF = """
        select distinct hgfg_hg Hauptgruppe, ben_text Benennung, hgthb_grafikid GrafikId, grafik_moddate ModStamp, grafik_blob Grafik
        from w_btzeilenugb_verbauung
        inner join w_bildtaf on (
            btzeilenuv_btnr = bildtaf_btnr
            and bildtaf_produktart = :produktart
            and bildtaf_vbereich in ('BE', :katalogumfang)
        )
        inner join w_hgfg on (
            bildtaf_hg = hgfg_hg
            and hgfg_fg = '00'
            and bildtaf_produktart = hgfg_produktart
            and bildtaf_bereich = hgfg_bereich
        )
        inner join w_ben_gk on (
            hgfg_textcode = ben_textcode
            and ben_iso = :iso
            and ben_regiso = :regiso
        )
        left join w_hg_thumbnail on (
            hgfg_hg = hgthb_hg
            and hgthb_marke_tps = :marke
            and hgfg_produktart = hgthb_produktart
            and hgfg_bereich = hgthb_bereich
        )
        left join w_grafik on (hgthb_grafikid = grafik_grafikid and grafik_art = 'T')
        where btzeilenuv_marke_tps = :marke
        order by Hauptgruppe
        """;

    private static final String RETRIEVE_HGFGS = """
        select distinct hgfg_hg Hauptgruppe, hgfg_fg Funktionsgruppe, ben_text Benennung
        from w_btzeilenugb_verbauung, w_hgfg, w_ben_gk, w_bildtaf
        where btzeilenuv_marke_tps = :marke
          and btzeilenuv_btnr = bildtaf_btnr
          and bildtaf_produktart = :produktart
          and bildtaf_vbereich in ('BE', :katalogumfang)
          and bildtaf_hg = :hg
          and bildtaf_hg = hgfg_hg
          and bildtaf_fg = hgfg_fg
          and bildtaf_produktart = hgfg_produktart
          and bildtaf_bereich = hgfg_bereich
          and hgfg_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by Hauptgruppe, Funktionsgruppe
        """;

    private static final String RETRIEVE_ALL_HGFGS = """
        select hgfg_hg Hauptgruppe, hgfg_fg Funktionsgruppe, ben_text Benennung
        from w_btzeilenugb_verbauung, w_hgfg, w_ben_gk, w_bildtaf
        where btzeilenuv_marke_tps = :marke
          and btzeilenuv_btnr = bildtaf_btnr
          and bildtaf_produktart = :produktart
          and bildtaf_vbereich in ('BE', :katalogumfang)
          and bildtaf_hg = hgfg_hg
          and hgfg_fg = '00'
          and bildtaf_produktart = hgfg_produktart
          and bildtaf_bereich = hgfg_bereich
          and hgfg_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        union
        select hgfg_hg Hauptgruppe, hgfg_fg Funktionsgruppe, ben_text Benennung
        from w_btzeilenugb_verbauung, w_hgfg, w_ben_gk, w_bildtaf
        where btzeilenuv_marke_tps = :marke
          and btzeilenuv_btnr = bildtaf_btnr
          and bildtaf_produktart = :produktart
          and bildtaf_vbereich in ('BE', :katalogumfang)
          and bildtaf_hg = hgfg_hg
          and bildtaf_fg = hgfg_fg
          and bildtaf_produktart = hgfg_produktart
          and bildtaf_bereich = hgfg_bereich
          and hgfg_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by 1, 2
        """;

    private static final String SEARCH_BT_BENENNUNG = """
        select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_pos Pos,
            bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso
        from w_btzeilenugb_verbauung, w_ben_gk, w_bildtaf
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where btzeilenuv_marke_tps = :marke
          and btzeilenuv_btnr = bildtaf_btnr
          and bildtaf_produktart = :produktart
          and bildtaf_vbereich in ('BE', :katalogumfang)
          and bildtaf_textc = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
          and ben_text like INSENSITIVE :suchstring
        """;

    private static final String SEARCH_BT_BENENNUNG_SONDERLOCKE = """
        select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_pos Pos,
            bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso
        from w_btzeilenugb_verbauung, w_ben_gk, w_bildtaf
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where btzeilenuv_marke_tps = :marke
          and btzeilenuv_btnr = bildtaf_btnr
          and bildtaf_produktart = :produktart
          and bildtaf_vbereich in ('BE', :katalogumfang)
          and bildtaf_textc = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
          and (ben_text like INSENSITIVE :suchstring1 or ben_text like INSENSITIVE :suchstring2)
        """;

    private static final String SEARCH_BT_BENENNUNG_TR = """
        select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_pos Pos,
            bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso
        from w_btzeilenugb_verbauung, w_ben_gk, w_bildtaf
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where btzeilenuv_marke_tps = :marke
          and btzeilenuv_btnr = bildtaf_btnr
          and bildtaf_produktart = :produktart
          and bildtaf_vbereich in ('BE', :katalogumfang)
          and bildtaf_textc = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
          and upper(replace('i' by '\u0130' in replace('\u0131' by 'I' in ben_text))) like :suchstring
        """;

    private static final String SEARCH_BT_BEGRIFF = """
        select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung,
            bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos,
            marktetk_isokz MarktIso
        from w_btzeilenugb_verbauung, w_ben_gk, w_bildtaf
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where btzeilenuv_marke_tps = :marke
          and btzeilenuv_btnr = bildtaf_btnr
          and bildtaf_produktart = :produktart
          and bildtaf_vbereich in ('BE', :katalogumfang)
          and bildtaf_textc = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        """;

    private static final String SEARCH_SNR_BENENNUNG = """
        select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer,
            ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,
            NULL BTZeilenAlter, NULL Pos, NULL BTNummer, teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_btzeilenugb_verbauung
        inner join w_btzeilenugb on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos)
        inner join w_bildtaf on (btzeilenuv_btnr = bildtaf_btnr)
        inner join w_teil on (btzeilenu_sachnr = teil_sachnr)
        inner join w_ben_gk ben_teil on (
            teil_textcode = ben_teil.ben_textcode
            and ben_teil.ben_iso = :iso
            and ben_teil.ben_regiso = :regiso
        )
        left join w_ben_gk ben_komm on (
            teil_textcode_kom = ben_komm.ben_textcode
            and ben_komm.ben_iso = :iso
            and ben_komm.ben_regiso = :regiso
        )
        where btzeilenuv_marke_tps = :marke
          and bildtaf_produktart = :produktart
          and bildtaf_vbereich in ('BE', :katalogumfang)
          and ben_teil.ben_text like INSENSITIVE :suchstring
        """;

    private static final String SEARCH_SNR_BENENNUNG_SONDERLOCKE = """
        select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer,
            ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,
            NULL BTZeilenAlter, NULL Pos, NULL BTNummer, teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_btzeilenugb_verbauung
        inner join w_btzeilenugb on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos)
        inner join w_bildtaf on (btzeilenuv_btnr = bildtaf_btnr)
        inner join w_teil on (btzeilenu_sachnr = teil_sachnr)
        inner join w_ben_gk ben_teil on (
            teil_textcode = ben_teil.ben_textcode
            and ben_teil.ben_iso = :iso
            and ben_teil.ben_regiso = :regiso
        )
        left join w_ben_gk ben_komm on (
            teil_textcode_kom = ben_komm.ben_textcode
            and ben_komm.ben_iso = :iso
            and ben_komm.ben_regiso = :regiso
        )
        where btzeilenuv_marke_tps = :marke
          and bildtaf_produktart = :produktart
          and bildtaf_vbereich in ('BE', :katalogumfang)
          and (ben_teil.ben_text like INSENSITIVE :suchstring1 or ben_teil.ben_text like INSENSITIVE :suchstring2)
        """;

    private static final String SEARCH_SNR_BENENNUNG_TR = """
        select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer,
            ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,
            NULL BTZeilenAlter, NULL Pos, NULL BTNummer, teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_btzeilenugb_verbauung
        inner join w_btzeilenugb on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos)
        inner join w_bildtaf on (btzeilenuv_btnr = bildtaf_btnr)
        inner join w_teil on (btzeilenu_sachnr = teil_sachnr)
        inner join w_ben_gk ben_teil on (
            teil_textcode = ben_teil.ben_textcode
            and ben_teil.ben_iso = :iso
            and ben_teil.ben_regiso = :regiso
        )
        left join w_ben_gk ben_komm on (
            teil_textcode_kom = ben_komm.ben_textcode
            and ben_komm.ben_iso = :iso
            and ben_komm.ben_regiso = :regiso
        )
        where btzeilenuv_marke_tps = :marke
          and bildtaf_produktart = :produktart
          and bildtaf_vbereich in ('BE', :katalogumfang)
          and upper(replace('i' by '\u0130' in replace('\u0131' by 'I' in ben_teil.ben_text))) like :suchstring
        """;

    private static final String SEARCH_SNR_BEGRIFF = """
        select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer,
            ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,
            NULL BTZeilenAlter, NULL Pos, NULL BTNummer, teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_btzeilenugb_verbauung
        inner join w_btzeilenugb on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos)
        inner join w_bildtaf on (btzeilenuv_btnr = bildtaf_btnr)
        inner join w_teil on (btzeilenu_sachnr = teil_sachnr)
        inner join w_ben_gk ben_teil on (
            teil_textcode = ben_teil.ben_textcode
            and ben_teil.ben_iso = :iso
            and ben_teil.ben_regiso = :regiso
        )
        left join w_ben_gk ben_komm on (
            teil_textcode_kom = ben_komm.ben_textcode
            and ben_komm.ben_iso = :iso
            and ben_komm.ben_regiso = :regiso
        )
        where btzeilenuv_marke_tps = :marke
          and bildtaf_produktart = :produktart
          and bildtaf_vbereich in ('BE', :katalogumfang)
        """;

    private static final String SEARCH_BT_SACHNUMMER_COMPL = """
        select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung,
            bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos,
            marktetk_isokz MarktIso
        from w_btzeilenugb_verbauung, w_ben_gk, w_btzeilenugb, w_bildtaf
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where btzeilenuv_marke_tps = :marke
          and btzeilenuv_btnr = btzeilenu_btnr
          and btzeilenuv_pos = btzeilenu_pos
          and btzeilenu_sachnr = :sachnummer
          and btzeilenu_btnr = bildtaf_btnr
          and bildtaf_produktart = :produktart
          and bildtaf_vbereich in ('BE', :katalogumfang)
          and bildtaf_textc = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        """;

    private static final String SEARCH_SNR_SACHNUMMER_INCOMPL = """
        select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer,
            ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_btzeilenugb_verbauung
        inner join w_btzeilenugb on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos)
        inner join w_bildtaf on (btzeilenu_btnr = bildtaf_btnr)
        inner join w_teil on (btzeilenu_sachnr = teil_sachnr)
        inner join w_ben_gk ben_teil on (
            teil_textcode = ben_teil.ben_textcode
            and ben_teil.ben_iso = :iso
            and ben_teil.ben_regiso = :regiso
        )
        left join w_ben_gk ben_komm on (
            teil_textcode_kom = ben_komm.ben_textcode
            and ben_komm.ben_iso = :iso
            and ben_komm.ben_regiso = :regiso
        )
        where btzeilenuv_marke_tps = :marke
          and btzeilenu_sachnr like :sachnummer
          and bildtaf_produktart = :produktart
          and bildtaf_vbereich in ('BE', :katalogumfang)
        order by Benennung, Hauptgruppe, Untergruppe, Sachnummer
        """;

    private static final String SEARCH_SNR_FREMDNR = """
        select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer,
            ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_btzeilenugb_verbauung
        inner join w_btzeilenugb on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos)
        inner join w_bildtaf on (btzeilenu_btnr = bildtaf_btnr)
        inner join w_teil on (btzeilenu_sachnr = teil_sachnr)
        inner join w_ben_gk ben_teil on (
            teil_textcode = ben_teil.ben_textcode
            and ben_teil.ben_iso = :iso
            and ben_teil.ben_regiso = :regiso
        )
        left join w_ben_gk ben_komm on (
            teil_textcode_kom = ben_komm.ben_textcode
            and ben_komm.ben_iso = :iso
            and ben_komm.ben_regiso = :regiso
        )
        where btzeilenuv_marke_tps = :marke
          and btzeilenu_sachnr IN (:fremdnummern)
          and bildtaf_produktart = :produktart
          and bildtaf_vbereich in ('BE', :katalogumfang)
        order by Benennung, Hauptgruppe, Untergruppe, Sachnummer
        """;

    private static final String SEARCH_BT_SACHNUMMERN = """
        select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_kommbt Kommentar,
            bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos, marktetk_isokz MarktIso
        from w_btzeilenugb_verbauung, w_ben_gk, w_btzeilenugb, w_bildtaf
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where btzeilenuv_marke_tps = :marke
          and btzeilenuv_btnr = btzeilenu_btnr
          and btzeilenuv_pos = btzeilenu_pos
          and btzeilenu_sachnr IN (:sachnummern)
          and btzeilenu_btnr = bildtaf_btnr
          and bildtaf_produktart = :produktart
          and bildtaf_vbereich in ('BE', :katalogumfang)
          and bildtaf_textc = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        """;

    private static final String SEARCH_BT_HGFG = """
        select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung,
            bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos,
            marktetk_isokz MarktIso
        from w_btzeilenugb_verbauung, w_ben_gk, w_btzeilenugb, w_bildtaf
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where btzeilenuv_marke_tps = :marke
          and btzeilenuv_btnr = btzeilenu_btnr
          and btzeilenuv_pos = btzeilenu_pos
          and btzeilenu_btnr = bildtaf_btnr
          and bildtaf_produktart = :produktart
          and bildtaf_vbereich in ('BE', :katalogumfang)
          and bildtaf_textc = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        """;

    private static final String SEARCH_BT_HG_GRAFISCH_MIT_GRAFIKEN = """
        select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, BB.ben_text Benennung, bildtaf_pos Pos,
            bildtaf_grafikid GrafikId, bildtaf_bedkez BedingungKZ, grafik_blob Grafik, grafik_moddate ModStamp,
            marktetk_isokz MarktIso
        from w_btzeilenugb_verbauung
        inner join w_btzeilenugb on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos)
        left join w_bildtaf on (btzeilenu_btnr = bildtaf_btnr)
        left join w_grafik on (bildtaf_grafikid = grafik_grafikid and grafik_art = 'T')
        left join w_ben_gk BB on (bildtaf_textc = ben_textcode and ben_iso = :iso and ben_regiso = :regiso)
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where btzeilenuv_marke_tps = :marke
          and bildtaf_produktart = :produktart
          and bildtaf_vbereich in ('BE', :katalogumfang)
          and bildtaf_hg = :hg
        order by Pos
        """;

    private static final String SEARCH_BT_HG_FG_GRAFISCH_MIT_GRAFIKEN = """
        select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, BB.ben_text Benennung, bildtaf_pos Pos,
            bildtaf_grafikid GrafikId, grafik_blob Grafik, bildtaf_bedkez BedingungKZ, grafik_moddate ModStamp,
            marktetk_isokz MarktIso
        from w_btzeilenugb_verbauung
        inner join w_btzeilenugb on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos)
        left join w_bildtaf on (btzeilenu_btnr = bildtaf_btnr)
        left join w_grafik on (bildtaf_grafikid = grafik_grafikid and grafik_art = 'T')
        left join w_ben_gk BB on (bildtaf_textc = ben_textcode and ben_iso = :iso and ben_regiso = :regiso)
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where btzeilenuv_marke_tps = :marke
          and bildtaf_produktart = :produktart
          and bildtaf_vbereich in ('BE', :katalogumfang)
          and bildtaf_hg = :hg
          and bildtaf_fg = :fg
        order by Pos
        """;

    private static final String CHECK_BT_HG_GRAFISCH = """
        select count(bildtaf_btnr) countBte
        from w_btzeilenugb_verbauung
        inner join w_btzeilenugb on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos)
        left join w_bildtaf on (btzeilenu_btnr = bildtaf_btnr)
        left join w_grafik on (bildtaf_grafikid = grafik_grafikid and grafik_art = 'T')
        where btzeilenuv_marke_tps = :marke
          and bildtaf_produktart = :produktart
          and bildtaf_vbereich in ('BE', :katalogumfang)
          and bildtaf_hg = :hg
        """;

    private static final RowMapper<GroupThumbnailDto> GROUP_THUMBNAIL_MAPPER = (rs, rowNum) ->
        GroupThumbnailDto.builder()
            .hauptgruppe(rs.getString("Hauptgruppe"))
            .funktionsgruppe(rs.getString("Funktionsgruppe"))
            .benennung(rs.getString("Benennung"))
            .grafikId(rs.getString("GrafikId"))
            .modStamp(rs.getString("ModStamp"))
            .grafik(rs.getBytes("Grafik"))
            .build();

    private static final RowMapper<GroupDesignationDto> GROUP_DESIGNATION_MAPPER = (rs, rowNum) ->
        GroupDesignationDto.builder()
            .hauptgruppe(rs.getString("Hauptgruppe"))
            .funktionsgruppe(rs.getString("Funktionsgruppe"))
            .benennung(rs.getString("Benennung"))
            .build();

    private static final RowMapper<PlateSearchResultDto> PLATE_SEARCH_MAPPER = (rs, rowNum) ->
        PlateSearchResultDto.builder()
            .bildtafelNr(rs.getString("BildtafelNr"))
            .bildtafelArt(rs.getString("BildtafelArt"))
            .benennung(rs.getString("Benennung"))
            .pos(getInteger(rs, "Pos"))
            .kommentar(rs.getString("Kommentar"))
            .cpVorhanden(rs.getString("CPVorhanden"))
            .bedingungKz(rs.getString("BedingungKZ"))
            .marktIso(rs.getString("MarktIso"))
            .build();

    private static final RowMapper<PlateGraphicSearchResultDto> PLATE_GRAPHIC_MAPPER = (rs, rowNum) ->
        PlateGraphicSearchResultDto.builder()
            .bildtafelNr(rs.getString("BildtafelNr"))
            .bildtafelArt(rs.getString("BildtafelArt"))
            .benennung(rs.getString("Benennung"))
            .pos(getInteger(rs, "Pos"))
            .grafikId(rs.getString("GrafikId"))
            .bedingungKz(rs.getString("BedingungKZ"))
            .grafik(rs.getBytes("Grafik"))
            .modStamp(rs.getString("ModStamp"))
            .marktIso(rs.getString("MarktIso"))
            .build();

    private static final RowMapper<PartSearchLineDto> PART_SEARCH_LINE_MAPPER = (rs, rowNum) ->
        PartSearchLineDto.builder()
            .hauptgruppe(rs.getString("Hauptgruppe"))
            .untergruppe(rs.getString("Untergruppe"))
            .sachnummer(rs.getString("Sachnummer"))
            .benennung(rs.getString("Benennung"))
            .zusatz(rs.getString("Zusatz"))
            .benennungKommentar(rs.getString("BenennungKommentar"))
            .btZeilenAlter(rs.getString("BTZeilenAlter"))
            .pos(getInteger(rs, "Pos"))
            .btNummer(rs.getString("BTNummer"))
            .teilDiebstahlrelevant(rs.getString("Teil_Diebstahlrelevant"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Retrieves main groups for assembly search.
     */
    public List<GroupThumbnailDto> findMainGroups(
        String marke,
        String produktart,
        String katalogumfang,
        String iso,
        String regiso
    ) {
        return jdbc.query(RETRIEVE_HGS,
            Map.of("marke", marke, "produktart", produktart, "katalogumfang", katalogumfang, "iso", iso, "regiso", regiso),
            GROUP_THUMBNAIL_MAPPER);
    }

    /**
     * Retrieves main groups with graphics for assembly search.
     */
    public List<GroupThumbnailDto> findMainGroupsWithGraphics(
        String marke,
        String produktart,
        String katalogumfang,
        String iso,
        String regiso
    ) {
        return jdbc.query(RETRIEVE_HGS_GRAF,
            Map.of("marke", marke, "produktart", produktart, "katalogumfang", katalogumfang, "iso", iso, "regiso", regiso),
            GROUP_THUMBNAIL_MAPPER);
    }

    /**
     * Retrieves main and function groups for assembly search.
     */
    public List<GroupDesignationDto> findMainAndFunctionGroups(
        String marke,
        String produktart,
        String katalogumfang,
        String hg,
        String iso,
        String regiso
    ) {
        return jdbc.query(RETRIEVE_HGFGS,
            Map.of("marke", marke, "produktart", produktart, "katalogumfang", katalogumfang, "hg", hg, "iso", iso, "regiso", regiso),
            GROUP_DESIGNATION_MAPPER);
    }

    /**
     * Retrieves all main and function groups for assembly search.
     */
    public List<GroupDesignationDto> findAllMainAndFunctionGroups(
        String marke,
        String produktart,
        String katalogumfang,
        String iso,
        String regiso
    ) {
        return jdbc.query(RETRIEVE_ALL_HGFGS,
            Map.of("marke", marke, "produktart", produktart, "katalogumfang", katalogumfang, "iso", iso, "regiso", regiso),
            GROUP_DESIGNATION_MAPPER);
    }

    /**
     * Searches assembly plates by designation.
     */
    public List<PlateSearchResultDto> searchPlatesByDesignation(
        String marke,
        String produktart,
        String katalogumfang,
        String iso,
        String regiso,
        String searchString,
        String orderByClause
    ) {
        String sql = appendClause(SEARCH_BT_BENENNUNG, orderByClause);
        return jdbc.query(sql,
            Map.of("marke", marke, "produktart", produktart, "katalogumfang", katalogumfang,
                "iso", iso, "regiso", regiso, "suchstring", searchString),
            PLATE_SEARCH_MAPPER);
    }

    /**
     * Searches assembly plates by designation using special case patterns.
     */
    public List<PlateSearchResultDto> searchPlatesByDesignationSpecialCase(
        String marke,
        String produktart,
        String katalogumfang,
        String iso,
        String regiso,
        String searchString1,
        String searchString2,
        String orderByClause
    ) {
        String sql = appendClause(SEARCH_BT_BENENNUNG_SONDERLOCKE, orderByClause);
        return jdbc.query(sql,
            Map.of("marke", marke, "produktart", produktart, "katalogumfang", katalogumfang,
                "iso", iso, "regiso", regiso, "suchstring1", searchString1, "suchstring2", searchString2),
            PLATE_SEARCH_MAPPER);
    }

    /**
     * Searches assembly plates by designation using Turkish locale replacements.
     */
    public List<PlateSearchResultDto> searchPlatesByDesignationTurkish(
        String marke,
        String produktart,
        String katalogumfang,
        String iso,
        String regiso,
        String searchString,
        String orderByClause
    ) {
        String sql = appendClause(SEARCH_BT_BENENNUNG_TR, orderByClause);
        return jdbc.query(sql,
            Map.of("marke", marke, "produktart", produktart, "katalogumfang", katalogumfang,
                "iso", iso, "regiso", regiso, "suchstring", searchString),
            PLATE_SEARCH_MAPPER);
    }

    /**
     * Searches assembly plates by term clauses.
     */
    public List<PlateSearchResultDto> searchPlatesByTerm(
        String marke,
        String produktart,
        String katalogumfang,
        String iso,
        String regiso,
        String termClause,
        String orderByClause
    ) {
        String sql = appendClause(SEARCH_BT_BEGRIFF + " and " + termClause, orderByClause);
        return jdbc.query(sql,
            Map.of("marke", marke, "produktart", produktart, "katalogumfang", katalogumfang, "iso", iso, "regiso", regiso),
            PLATE_SEARCH_MAPPER);
    }

    /**
     * Searches assembly part lines by designation.
     */
    public List<PartSearchLineDto> searchPartsByDesignation(
        String marke,
        String produktart,
        String katalogumfang,
        String iso,
        String regiso,
        String searchString,
        String orderByClause
    ) {
        String sql = appendClause(SEARCH_SNR_BENENNUNG, orderByClause);
        return jdbc.query(sql,
            Map.of("marke", marke, "produktart", produktart, "katalogumfang", katalogumfang,
                "iso", iso, "regiso", regiso, "suchstring", searchString),
            PART_SEARCH_LINE_MAPPER);
    }

    /**
     * Searches assembly part lines by designation using special case patterns.
     */
    public List<PartSearchLineDto> searchPartsByDesignationSpecialCase(
        String marke,
        String produktart,
        String katalogumfang,
        String iso,
        String regiso,
        String searchString1,
        String searchString2,
        String orderByClause
    ) {
        String sql = appendClause(SEARCH_SNR_BENENNUNG_SONDERLOCKE, orderByClause);
        return jdbc.query(sql,
            Map.of("marke", marke, "produktart", produktart, "katalogumfang", katalogumfang,
                "iso", iso, "regiso", regiso, "suchstring1", searchString1, "suchstring2", searchString2),
            PART_SEARCH_LINE_MAPPER);
    }

    /**
     * Searches assembly part lines by designation using Turkish locale replacements.
     */
    public List<PartSearchLineDto> searchPartsByDesignationTurkish(
        String marke,
        String produktart,
        String katalogumfang,
        String iso,
        String regiso,
        String searchString,
        String orderByClause
    ) {
        String sql = appendClause(SEARCH_SNR_BENENNUNG_TR, orderByClause);
        return jdbc.query(sql,
            Map.of("marke", marke, "produktart", produktart, "katalogumfang", katalogumfang,
                "iso", iso, "regiso", regiso, "suchstring", searchString),
            PART_SEARCH_LINE_MAPPER);
    }

    /**
     * Searches assembly part lines by term clauses.
     */
    public List<PartSearchLineDto> searchPartsByTerm(
        String marke,
        String produktart,
        String katalogumfang,
        String iso,
        String regiso,
        String termClause,
        String commentTermClause,
        String orderByClause
    ) {
        String sql = SEARCH_SNR_BEGRIFF + " and " + termClause
            + " union " + SEARCH_SNR_BEGRIFF + " and " + commentTermClause;
        sql = appendClause(sql, orderByClause);
        return jdbc.query(sql,
            Map.of("marke", marke, "produktart", produktart, "katalogumfang", katalogumfang, "iso", iso, "regiso", regiso),
            PART_SEARCH_LINE_MAPPER);
    }

    /**
     * Searches assembly plates by a complete part number.
     */
    public List<PlateSearchResultDto> searchPlatesByCompletePartNumber(
        String marke,
        String produktart,
        String katalogumfang,
        String iso,
        String regiso,
        String sachnummer,
        String orderByClause
    ) {
        String sql = appendClause(SEARCH_BT_SACHNUMMER_COMPL, orderByClause);
        return jdbc.query(sql,
            Map.of("marke", marke, "produktart", produktart, "katalogumfang", katalogumfang,
                "iso", iso, "regiso", regiso, "sachnummer", sachnummer),
            PLATE_SEARCH_MAPPER);
    }

    /**
     * Searches assembly part lines by an incomplete part number.
     */
    public List<PartSearchLineDto> searchPartsByIncompletePartNumber(
        String marke,
        String produktart,
        String katalogumfang,
        String iso,
        String regiso,
        String sachnummer
    ) {
        return jdbc.query(SEARCH_SNR_SACHNUMMER_INCOMPL,
            Map.of("marke", marke, "produktart", produktart, "katalogumfang", katalogumfang,
                "iso", iso, "regiso", regiso, "sachnummer", sachnummer),
            PART_SEARCH_LINE_MAPPER);
    }

    /**
     * Searches assembly part lines by external numbers.
     */
    public List<PartSearchLineDto> searchPartsByExternalNumbers(
        String marke,
        String produktart,
        String katalogumfang,
        String iso,
        String regiso,
        List<String> externalNumbers
    ) {
        return jdbc.query(SEARCH_SNR_FREMDNR,
            Map.of("marke", marke, "produktart", produktart, "katalogumfang", katalogumfang,
                "iso", iso, "regiso", regiso, "fremdnummern", externalNumbers),
            PART_SEARCH_LINE_MAPPER);
    }

    /**
     * Searches assembly plates by a list of part numbers.
     */
    public List<PlateSearchResultDto> searchPlatesByPartNumbers(
        String marke,
        String produktart,
        String katalogumfang,
        String iso,
        String regiso,
        List<String> sachnummern,
        String orderByClause
    ) {
        String sql = appendClause(SEARCH_BT_SACHNUMMERN, orderByClause);
        return jdbc.query(sql,
            Map.of("marke", marke, "produktart", produktart, "katalogumfang", katalogumfang,
                "iso", iso, "regiso", regiso, "sachnummern", sachnummern),
            PLATE_SEARCH_MAPPER);
    }

    /**
     * Searches assembly plates by main or function group.
     */
    public List<PlateSearchResultDto> searchPlatesByMainGroup(
        String marke,
        String produktart,
        String katalogumfang,
        String iso,
        String regiso,
        String groupClause
    ) {
        String sql = appendClause(SEARCH_BT_HGFG + " " + groupClause, "order by Pos");
        return jdbc.query(sql,
            Map.of("marke", marke, "produktart", produktart, "katalogumfang", katalogumfang, "iso", iso, "regiso", regiso),
            PLATE_SEARCH_MAPPER);
    }

    /**
     * Searches assembly plates with graphics by main group.
     */
    public List<PlateGraphicSearchResultDto> searchPlatesByMainGroupWithGraphics(
        String marke,
        String produktart,
        String katalogumfang,
        String iso,
        String regiso,
        String hg
    ) {
        return jdbc.query(SEARCH_BT_HG_GRAFISCH_MIT_GRAFIKEN,
            Map.of("marke", marke, "produktart", produktart, "katalogumfang", katalogumfang, "iso", iso, "regiso", regiso, "hg", hg),
            PLATE_GRAPHIC_MAPPER);
    }

    /**
     * Searches assembly plates with graphics by main and function group.
     */
    public List<PlateGraphicSearchResultDto> searchPlatesByMainAndFunctionGroupWithGraphics(
        String marke,
        String produktart,
        String katalogumfang,
        String iso,
        String regiso,
        String hg,
        String fg
    ) {
        return jdbc.query(SEARCH_BT_HG_FG_GRAFISCH_MIT_GRAFIKEN,
            Map.of("marke", marke, "produktart", produktart, "katalogumfang", katalogumfang,
                "iso", iso, "regiso", regiso, "hg", hg, "fg", fg),
            PLATE_GRAPHIC_MAPPER);
    }

    /**
     * Counts assembly plates that have graphics for a main group.
     */
    public long countPlatesWithGraphicsForMainGroup(
        String marke,
        String produktart,
        String katalogumfang,
        String hg
    ) {
        return jdbc.queryForObject(CHECK_BT_HG_GRAFISCH,
            Map.of("marke", marke, "produktart", produktart, "katalogumfang", katalogumfang, "hg", hg),
            Long.class);
    }

    private static Integer getInteger(java.sql.ResultSet rs, String column) {
        try {
            int value = rs.getInt(column);
            return rs.wasNull() ? null : value;
        } catch (java.sql.SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private static String appendClause(String base, String clause) {
        if (!StringUtils.hasText(clause)) {
            return base;
        }
        return base + " " + clause;
    }
}
