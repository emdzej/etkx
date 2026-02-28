package pl.emdzej.etkx.dal.repository.search;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pl.emdzej.etkx.dal.dto.search.ChangePointDto;
import pl.emdzej.etkx.dal.dto.search.GroupDesignationDto;
import pl.emdzej.etkx.dal.dto.search.GroupThumbnailDto;
import pl.emdzej.etkx.dal.dto.search.PartSearchLineDto;
import pl.emdzej.etkx.dal.dto.search.PlateGraphicSearchResultDto;
import pl.emdzej.etkx.dal.dto.search.PlateSearchResultDto;

/**
 * Repository for vehicle-specific part search operations.
 */
@Repository
@RequiredArgsConstructor
public class PartSearchVehicleRepository {
    private static final String RETRIEVE_HGS = """
        select distinct hgfg_hg Hauptgruppe, ben_text Benennung, hgthb_grafikid GrafikId, grafik_moddate ModStamp
        from w_hgfg_mosp
        inner join w_hgfg on (
            hgfgm_hg = hgfg_hg
            and hgfg_fg = '00'
            and hgfg_produktart = hgfgm_produktart
            and hgfg_bereich = hgfgm_bereich
        )
        inner join w_ben_gk on (
            hgfg_textcode = ben_textcode
            and ben_iso = :iso
            and ben_regiso = :regiso
        )
        left join w_hg_thumbnail on (
            hgfg_hg = hgthb_hg
            and hgthb_marke_tps = :marke
            and hgthb_produktart = hgfg_produktart
            and hgthb_bereich = hgfg_bereich
        )
        left join w_grafik on (hgthb_grafikid = grafik_grafikid and grafik_art = 'T')
        where hgfgm_mospid = :mosp
        order by Hauptgruppe
        """;

    private static final String RETRIEVE_HGS_GRAF = """
        select distinct hgfg_hg Hauptgruppe, ben_text Benennung, hgthb_grafikid GrafikId, grafik_moddate ModStamp, grafik_blob Grafik
        from w_hgfg_mosp
        inner join w_hgfg on (
            hgfgm_hg = hgfg_hg
            and hgfg_fg = '00'
            and hgfg_produktart = hgfgm_produktart
            and hgfg_bereich = hgfgm_bereich
        )
        inner join w_ben_gk on (
            hgfg_textcode = ben_textcode
            and ben_iso = :iso
            and ben_regiso = :regiso
        )
        left join w_hg_thumbnail on (
            hgfg_hg = hgthb_hg
            and hgthb_marke_tps = :marke
            and hgthb_produktart = hgfg_produktart
            and hgthb_bereich = hgfg_bereich
        )
        left join w_grafik on (hgthb_grafikid = grafik_grafikid and grafik_art = 'T')
        where hgfgm_mospid = :mosp
        order by Hauptgruppe
        """;

    private static final String RETRIEVE_FGS_GRAF = """
        select distinct hgfg_fg Funktionsgruppe, ben_text Benennung, fgthb_grafikid GrafikId, grafik_moddate ModStamp
        from w_hgfg
        inner join w_ben_gk on (
            hgfg_textcode = ben_textcode
            and ben_iso = :iso
            and ben_regiso = :regiso
        )
        left join w_fg_thumbnail on (
            hgfg_hg = fgthb_hg
            and hgfg_fg = fgthb_fg
            and fgthb_marke_tps = :marke
            and fgthb_produktart = hgfg_produktart
            and fgthb_bereich = hgfg_bereich
        )
        left join w_grafik on (fgthb_grafikid = grafik_grafikid and grafik_art = 'T')
        inner join w_bildtaf on (bildtaf_hg = hgfg_hg and hgfg_fg = bildtaf_fg)
        where hgfg_hg = :hg
          and hgfg_produktart = :prodart
        order by Funktionsgruppe
        """;

    private static final String RETRIEVE_FGS_GRAF_MOSP = """
        select distinct hgfg_fg Funktionsgruppe, ben_text Benennung, fgthb_grafikid GrafikId, grafik_moddate ModStamp
        from w_hgfg_mosp
        inner join w_hgfg on (
            hgfgm_hg = hgfg_hg
            and hgfg_fg = '00'
            and hgfg_produktart = hgfgm_produktart
            and hgfg_bereich = hgfgm_bereich
        )
        inner join w_ben_gk on (
            hgfg_textcode = ben_textcode
            and ben_iso = :iso
            and ben_regiso = :regiso
        )
        left join w_fg_thumbnail on (
            hgfg_hg = fgthb_hg
            and hgfg_fg = fgthb_fg
            and fgthb_marke_tps = :marke
            and fgthb_produktart = hgfg_produktart
            and fgthb_bereich = hgfg_bereich
        )
        left join w_grafik on (fgthb_grafikid = grafik_grafikid and grafik_art = 'T')
        inner join w_bildtaf on (bildtaf_hg = hgfgm_hg and hgfgm_fg = bildtaf_fg)
        where hgfgm_mospid = :mosp
          and hgfgm_hg = :hg
        order by Funktionsgruppe
        """;

    private static final String RETRIEVE_FGS_GRAF_MIT_GRAFIKEN = """
        select distinct hgfg_fg Funktionsgruppe, ben_text Benennung, fgthb_grafikid GrafikId, grafik_moddate ModStamp, grafik_blob Grafik
        from w_hgfg
        inner join w_ben_gk on (
            hgfg_textcode = ben_textcode
            and ben_iso = :iso
            and ben_regiso = :regiso
        )
        left join w_fg_thumbnail on (
            hgfg_hg = fgthb_hg
            and hgfg_fg = fgthb_fg
            and fgthb_marke_tps = :marke
            and fgthb_produktart = hgfg_produktart
            and fgthb_bereich = hgfg_bereich
        )
        left join w_grafik on (fgthb_grafikid = grafik_grafikid and grafik_art = 'T')
        inner join w_bildtaf on (bildtaf_hg = hgfg_hg and hgfg_fg = bildtaf_fg)
        where hgfg_hg = :hg
          and hgfg_produktart = :prodart
        order by Funktionsgruppe
        """;

    private static final String RETRIEVE_FGS_GRAF_MOSP_MIT_GRAFIKEN = """
        select distinct hgfg_fg Funktionsgruppe, ben_text Benennung, fgthb_grafikid GrafikId, grafik_moddate ModStamp, grafik_blob Grafik
        from w_hgfg_mosp
        inner join w_hgfg on (
            hgfgm_hg = hgfg_hg
            and hgfg_fg = '00'
            and hgfg_produktart = hgfgm_produktart
            and hgfg_bereich = hgfgm_bereich
        )
        inner join w_ben_gk on (
            hgfg_textcode = ben_textcode
            and ben_iso = :iso
            and ben_regiso = :regiso
        )
        left join w_fg_thumbnail on (
            hgfg_hg = fgthb_hg
            and hgfg_fg = fgthb_fg
            and fgthb_marke_tps = :marke
            and fgthb_produktart = hgfg_produktart
            and fgthb_bereich = hgfg_bereich
        )
        left join w_grafik on (fgthb_grafikid = grafik_grafikid and grafik_art = 'T')
        inner join w_bildtaf on (bildtaf_hg = hgfgm_hg and hgfgm_fg = bildtaf_fg)
        where hgfgm_mospid = :mosp
          and hgfgm_hg = :hg
        order by Funktionsgruppe
        """;

    private static final String RETRIEVE_HGFGS = """
        select distinct hgfg_hg Hauptgruppe, hgfg_fg Funktionsgruppe, ben_text Benennung
        from w_hgfg_mosp, w_hgfg, w_ben_gk
        where hgfgm_mospid = :mosp
          and hgfgm_hg = :hg
          and hgfgm_hg = hgfg_hg
          and hgfgm_fg = hgfg_fg
          and hgfgm_produktart = hgfg_produktart
          and hgfgm_bereich = hgfg_bereich
          and hgfg_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by Hauptgruppe, Funktionsgruppe
        """;

    private static final String RETRIEVE_ALL_HGFGS = """
        select hgfg_hg Hauptgruppe, hgfg_fg Funktionsgruppe, ben_text Benennung
        from w_hgfg_mosp, w_hgfg, w_ben_gk
        where hgfgm_mospid = :mosp
          and hgfgm_hg = hgfg_hg
          and hgfg_fg = '00'
          and hgfgm_produktart = hgfg_produktart
          and hgfgm_bereich = hgfg_bereich
          and hgfg_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        union
        select hgfg_hg Hauptgruppe, hgfg_fg Funktionsgruppe, ben_text Benennung
        from w_hgfg_mosp, w_hgfg, w_ben_gk
        where hgfgm_mospid = :mosp
          and hgfgm_hg = hgfg_hg
          and hgfgm_fg = hgfg_fg
          and hgfgm_produktart = hgfg_produktart
          and hgfgm_bereich = hgfg_bereich
          and hgfg_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by 1, 2
        """;

    private static final String SEARCH_BT_BENENNUNG = """
        select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_pos Pos,
            bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso
        from w_ben_gk, w_bildtaf_suche, w_bildtaf
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where ben_iso = :iso
          and ben_regiso = :regiso
          and ben_text like INSENSITIVE :suchstring
          and ben_textcode = bildtaf_textc
          and bildtaf_btnr = bildtafs_btnr
          and bildtafs_mospid = :mosp
        """;

    private static final String SEARCH_BT_BENENNUNG_SONDERLOCKE = """
        select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_pos Pos,
            bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso
        from w_ben_gk, w_bildtaf_suche, w_bildtaf
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where ben_iso = :iso
          and ben_regiso = :regiso
          and (ben_text like INSENSITIVE :suchstring1 or ben_text like INSENSITIVE :suchstring2)
          and ben_textcode = bildtaf_textc
          and bildtaf_btnr = bildtafs_btnr
          and bildtafs_mospid = :mosp
        """;

    private static final String SEARCH_BT_BENENNUNG_TR = """
        select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_pos Pos,
            bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso
        from w_ben_gk, w_bildtaf_suche, w_bildtaf
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where ben_iso = :iso
          and ben_regiso = :regiso
          and upper(replace('i' by  '\u0130' in replace('\u0131' by 'I' in ben_text))) like :suchstring
          and ben_textcode = bildtaf_textc
          and bildtaf_btnr = bildtafs_btnr
          and bildtafs_mospid = :mosp
        """;

    private static final String SEARCH_BT_BEGRIFF = """
        select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung,
            bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos,
            marktetk_isokz MarktIso
        from w_ben_gk, w_bildtaf_suche, w_bildtaf
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where ben_iso = :iso
          and ben_regiso = :regiso
          and ben_textcode = bildtaf_textc
          and bildtaf_btnr = bildtafs_btnr
          and bildtafs_mospid = :mosp
        """;

    private static final String SEARCH_BT_BEGRIFF_NEU = """
        select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung,
            bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos,
            marktetk_isokz MarktIso
        from w_ben_gk, w_bildtaf_suche, w_bildtaf
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where ben_iso = :iso
          and ben_regiso = :regiso
          and ben_textcode = bildtaf_textc
          and bildtaf_btnr = bildtafs_btnr
          and bildtafs_mospid = :mosp
        """;

    private static final String SEARCH_BT_BEGRIFF_NEU_KOMM = """
        select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_bte.ben_text Benennung,
            bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos,
            marktetk_isokz MarktIso
        from w_ben_gk ben_komm, w_komm_help, w_komm, w_bildtaf_suche, w_ben_gk ben_bte, w_bildtaf
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where ben_komm.ben_iso = :iso
          and ben_komm.ben_regiso = :regiso
          and komm_textcode = ben_komm.ben_textcode
          and kommh_id = komm_id
          and kommh_mospid = :mosp
          and kommh_btnr = bildtaf_btnr
          and ben_bte.ben_iso = :iso
          and ben_bte.ben_regiso = :regiso
          and ben_bte.ben_textcode = bildtaf_textc
          and bildtaf_btnr = bildtafs_btnr
          and bildtafs_mospid = kommh_mospid
        """;

    private static final String SEARCH_SNR_BENENNUNG = """
        select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer,
            ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,
            btzeilenv_alter_kz BTZeilenAlter,
            if btzeilenv_alter_kz is not null THEN btzeilenv_pos ELSE NULL FI Pos,
            if btzeilenv_alter_kz is not null THEN btzeilenv_btnr ELSE NULL FI BTNummer,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_btzeilen_verbauung
        inner join w_btzeilen on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)
        inner join w_bildtaf on (btzeilen_btnr = bildtaf_btnr)
        inner join w_teil on (teil_sachnr = btzeilen_sachnr)
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
        where btzeilenv_mospid = :mosp
          and ben_teil.ben_text like INSENSITIVE :suchstring
        """;

    private static final String SEARCH_SNR_BENENNUNG_SONDERLOCKE = """
        select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer,
            ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,
            btzeilenv_alter_kz BTZeilenAlter,
            if btzeilenv_alter_kz is not null THEN btzeilenv_pos ELSE NULL FI Pos,
            if btzeilenv_alter_kz is not null THEN btzeilenv_btnr ELSE NULL FI BTNummer,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_btzeilen_verbauung
        inner join w_btzeilen on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)
        inner join w_bildtaf on (btzeilen_btnr = bildtaf_btnr)
        inner join w_teil on (teil_sachnr = btzeilen_sachnr)
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
        where btzeilenv_mospid = :mosp
          and (ben_teil.ben_text like INSENSITIVE :suchstring1 or ben_teil.ben_text like INSENSITIVE :suchstring2)
        """;

    private static final String SEARCH_SNR_BENENNUNG_TR = """
        select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer,
            ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,
            btzeilenv_alter_kz BTZeilenAlter,
            if btzeilenv_alter_kz is not null THEN btzeilenv_pos ELSE NULL FI Pos,
            if btzeilenv_alter_kz is not null THEN btzeilenv_btnr ELSE NULL BTNummer,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_btzeilen_verbauung
        inner join w_btzeilen on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)
        inner join w_bildtaf on (btzeilen_btnr = bildtaf_btnr)
        inner join w_teil on (teil_sachnr = btzeilen_sachnr)
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
        where btzeilenv_mospid = :mosp
          and upper(replace('i' by '\u0130' in replace('\u0131' by 'I' in ben_teil.ben_text))) like :suchstring
        """;

    private static final String SEARCH_SNR_BEGRIFF = """
        select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer,
            ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,
            btzeilenv_alter_kz BTZeilenAlter,
            if btzeilenv_alter_kz is not null THEN btzeilenv_pos ELSE NULL FI Pos,
            if btzeilenv_alter_kz is not null THEN btzeilenv_btnr ELSE NULL FI BTNummer,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_btzeilen_verbauung
        inner join w_btzeilen on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)
        inner join w_bildtaf on (btzeilen_btnr = bildtaf_btnr)
        inner join w_teil on (teil_sachnr = btzeilen_sachnr)
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
        where btzeilenv_mospid = :mosp
        """;

    private static final String SEARCH_BT_SACHNUMMER_COMPL = """
        select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung,
            bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos,
            marktetk_isokz MarktIso
        from w_btzeilen_verbauung, w_ben_gk, w_btzeilen, w_bildtaf
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where btzeilenv_mospid = :mosp
          and btzeilenv_sachnr = :sachnummer
          and btzeilenv_btnr = btzeilen_btnr
          and btzeilenv_pos = btzeilen_pos
          and btzeilen_btnr = bildtaf_btnr
          and bildtaf_produktart = :produktart
          and bildtaf_textc = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        """;

    private static final String SEARCH_SNR_SACHNUMMER_INCOMPL = """
        select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer,
            ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_btzeilen_verbauung
        inner join w_btzeilen on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)
        inner join w_bildtaf on (btzeilen_btnr = bildtaf_btnr)
        inner join w_teil on (teil_sachnr = btzeilen_sachnr)
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
        where btzeilenv_mospid = :mosp
          and btzeilenv_sachnr like :sachnummer
        order by Benennung, Hauptgruppe, Untergruppe, Sachnummer
        """;

    private static final String SEARCH_SNR_FREMDNR = """
        select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer,
            ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,
            teil_ist_diebstahlrelevant Teil_Diebstahlrelevant
        from w_btzeilen_verbauung
        inner join w_btzeilen on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)
        inner join w_bildtaf on (btzeilen_btnr = bildtaf_btnr)
        inner join w_teil on (teil_sachnr = btzeilen_sachnr)
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
        where btzeilenv_mospid = :mosp
          and btzeilenv_sachnr IN (:fremdnummern)
        order by Benennung, Hauptgruppe, Untergruppe, Sachnummer
        """;

    private static final String SEARCH_BT_SACHNUMMERN = """
        select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung,
            bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos,
            marktetk_isokz MarktIso
        from w_btzeilen_verbauung, w_ben_gk, w_btzeilen, w_bildtaf
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where btzeilenv_mospid = :mosp
          and btzeilenv_sachnr IN (:sachnummern)
          and btzeilenv_btnr = btzeilen_btnr
          and btzeilenv_pos = btzeilen_pos
          and btzeilen_btnr = bildtaf_btnr
          and bildtaf_textc = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by Pos
        """;

    private static final String SEARCH_BT_HGFG = """
        select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung,
            bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos,
            marktetk_isokz MarktIso
        from w_bildtaf_suche, w_ben_gk, w_bildtaf
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where bildtafs_hg = :hg
          and bildtafs_mospid = :mosp
          and bildtafs_btnr = bildtaf_btnr
          and bildtaf_textc = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by Pos
        """;

    private static final String SEARCH_BT_HG_GRAFISCH_MIT_GRAFIKEN = """
        select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, BB.ben_text Benennung, bildtaf_pos Pos,
            bildtaf_grafikid GrafikId, bildtaf_bedkez BedingungKZ, grafik_blob Grafik, grafik_moddate ModStamp,
            marktetk_isokz MarktIso
        from w_btzeilen_verbauung
        inner join w_btzeilen on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)
        inner join w_bildtaf on (btzeilen_btnr = bildtaf_btnr)
        left join w_grafik on (bildtaf_grafikid = grafik_grafikid and grafik_art = 'T')
        inner join w_ben_gk BB on (bildtaf_textc = BB.ben_textcode and BB.ben_iso = :iso and BB.ben_regiso = :regiso)
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where btzeilenv_mospid = :mosp
          and bildtaf_hg = :hg
        order by Pos
        """;

    private static final String SEARCH_BT_HG_FG_GRAFISCH_MIT_GRAFIKEN = """
        select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, BB.ben_text Benennung, bildtaf_pos Pos,
            bildtaf_grafikid GrafikId, bildtaf_bedkez BedingungKZ, grafik_blob Grafik, grafik_moddate ModStamp,
            marktetk_isokz MarktIso
        from w_btzeilen_verbauung
        inner join w_btzeilen on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)
        inner join w_bildtaf on (btzeilen_btnr = bildtaf_btnr)
        left join w_grafik on (bildtaf_grafikid = grafik_grafikid and grafik_art = 'T')
        inner join w_ben_gk BB on (bildtaf_textc = BB.ben_textcode and BB.ben_iso = :iso and BB.ben_regiso = :regiso)
        left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)
        where btzeilenv_mospid = :mosp
          and bildtaf_hg = :hg
          and bildtaf_fg = :fg
        order by Pos
        """;

    private static final String CHECK_BT_HG_GRAFISCH = """
        select count(bildtaf_btnr) countBte
        from w_btzeilen_verbauung
        inner join w_btzeilen on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)
        inner join w_bildtaf on (btzeilen_btnr = bildtaf_btnr)
        left join w_grafik on (bildtaf_grafikid = grafik_grafikid and grafik_art = 'T')
        where btzeilenv_mospid = :mosp
          and bildtaf_hg = :hg
        """;

    private static final String SEARCH_KABELBAUM_CHANGEPOINTS = """
        select distinct btzeilenc_pos Pos, btzeilenc_typschl Typ, btzeilenc_werk Werk, btzeilenc_art Art,
            btzeilenc_datum Datum, btzeilenc_vin Vin, btzeilenc_vin_proddatum VinProddatum, btzeilenc_vin_min VinMin,
            btzeilenc_vin_max VinMax, btzeilenc_nart ArtNummer, btzeilenc_nummer Nummer, btzeilenc_alter CPAlter
        from w_btzeilen_cp
        where btzeilenc_mospid = :mospid
          and btzeilenc_typschl = :typschluessel
          and btzeilenc_werk = :werk
          and btzeilenc_pos = :position
          and btzeilenc_btnr = :btnr
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

    private static final RowMapper<ChangePointDto> CHANGE_POINT_MAPPER = (rs, rowNum) ->
        ChangePointDto.builder()
            .pos(getInteger(rs, "Pos"))
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

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Retrieves main groups for a vehicle model series.
     */
    public List<GroupThumbnailDto> findMainGroups(String mosp, String iso, String regiso, String marke) {
        return jdbc.query(RETRIEVE_HGS, Map.of("mosp", mosp, "iso", iso, "regiso", regiso, "marke", marke),
            GROUP_THUMBNAIL_MAPPER);
    }

    /**
     * Retrieves main groups with thumbnail graphics for a vehicle model series.
     */
    public List<GroupThumbnailDto> findMainGroupsWithGraphics(String mosp, String iso, String regiso, String marke) {
        return jdbc.query(RETRIEVE_HGS_GRAF, Map.of("mosp", mosp, "iso", iso, "regiso", regiso, "marke", marke),
            GROUP_THUMBNAIL_MAPPER);
    }

    /**
     * Retrieves function groups with thumbnails for a main group.
     */
    public List<GroupThumbnailDto> findFunctionGroups(String hg, String prodart, String iso, String regiso, String marke) {
        return jdbc.query(RETRIEVE_FGS_GRAF,
            Map.of("hg", hg, "prodart", prodart, "iso", iso, "regiso", regiso, "marke", marke),
            GROUP_THUMBNAIL_MAPPER);
    }

    /**
     * Retrieves function groups with thumbnails for a main group using MOSP filters.
     */
    public List<GroupThumbnailDto> findFunctionGroupsForMosp(String mosp, String hg, String iso, String regiso, String marke) {
        return jdbc.query(RETRIEVE_FGS_GRAF_MOSP,
            Map.of("mosp", mosp, "hg", hg, "iso", iso, "regiso", regiso, "marke", marke),
            GROUP_THUMBNAIL_MAPPER);
    }

    /**
     * Retrieves function groups with thumbnails and graphics for a main group.
     */
    public List<GroupThumbnailDto> findFunctionGroupsWithGraphics(
        String hg,
        String prodart,
        String iso,
        String regiso,
        String marke
    ) {
        return jdbc.query(RETRIEVE_FGS_GRAF_MIT_GRAFIKEN,
            Map.of("hg", hg, "prodart", prodart, "iso", iso, "regiso", regiso, "marke", marke),
            GROUP_THUMBNAIL_MAPPER);
    }

    /**
     * Retrieves function groups with thumbnails and graphics using MOSP filters.
     */
    public List<GroupThumbnailDto> findFunctionGroupsWithGraphicsForMosp(
        String mosp,
        String hg,
        String iso,
        String regiso,
        String marke
    ) {
        return jdbc.query(RETRIEVE_FGS_GRAF_MOSP_MIT_GRAFIKEN,
            Map.of("mosp", mosp, "hg", hg, "iso", iso, "regiso", regiso, "marke", marke),
            GROUP_THUMBNAIL_MAPPER);
    }

    /**
     * Retrieves main and function group combinations for a MOSP.
     */
    public List<GroupDesignationDto> findMainAndFunctionGroups(String mosp, String hg, String iso, String regiso) {
        return jdbc.query(RETRIEVE_HGFGS, Map.of("mosp", mosp, "hg", hg, "iso", iso, "regiso", regiso),
            GROUP_DESIGNATION_MAPPER);
    }

    /**
     * Retrieves all main and function groups for a MOSP.
     */
    public List<GroupDesignationDto> findAllMainAndFunctionGroups(String mosp, String iso, String regiso) {
        return jdbc.query(RETRIEVE_ALL_HGFGS, Map.of("mosp", mosp, "iso", iso, "regiso", regiso),
            GROUP_DESIGNATION_MAPPER);
    }

    /**
     * Searches illustration plates by designation.
     */
    public List<PlateSearchResultDto> searchPlatesByDesignation(
        String mosp,
        String iso,
        String regiso,
        String searchString,
        String additionalFilters,
        String unionSql
    ) {
        String sql = appendUnion(appendFilters(SEARCH_BT_BENENNUNG, additionalFilters), unionSql) + " order by Pos";
        return jdbc.query(sql, Map.of("mosp", mosp, "iso", iso, "regiso", regiso, "suchstring", searchString),
            PLATE_SEARCH_MAPPER);
    }

    /**
     * Searches illustration plates by designation using special case patterns.
     */
    public List<PlateSearchResultDto> searchPlatesByDesignationSpecialCase(
        String mosp,
        String iso,
        String regiso,
        String searchString1,
        String searchString2,
        String additionalFilters,
        String unionSql
    ) {
        String sql = appendUnion(appendFilters(SEARCH_BT_BENENNUNG_SONDERLOCKE, additionalFilters), unionSql)
            + " order by Pos";
        return jdbc.query(sql,
            Map.of("mosp", mosp, "iso", iso, "regiso", regiso, "suchstring1", searchString1, "suchstring2", searchString2),
            PLATE_SEARCH_MAPPER);
    }

    /**
     * Searches illustration plates by designation using Turkish locale replacements.
     */
    public List<PlateSearchResultDto> searchPlatesByDesignationTurkish(
        String mosp,
        String iso,
        String regiso,
        String searchString,
        String additionalFilters,
        String unionSql
    ) {
        String sql = appendUnion(appendFilters(SEARCH_BT_BENENNUNG_TR, additionalFilters), unionSql) + " order by Pos";
        return jdbc.query(sql, Map.of("mosp", mosp, "iso", iso, "regiso", regiso, "suchstring", searchString),
            PLATE_SEARCH_MAPPER);
    }

    /**
     * Searches illustration plates by term clauses.
     */
    public List<PlateSearchResultDto> searchPlatesByTerm(
        String mosp,
        String iso,
        String regiso,
        String termClause,
        String additionalFilters,
        String unionSql
    ) {
        String sql = appendUnion(appendFilters(SEARCH_BT_BEGRIFF + " and " + termClause, additionalFilters), unionSql)
            + " order by Pos";
        return jdbc.query(sql, Map.of("mosp", mosp, "iso", iso, "regiso", regiso), PLATE_SEARCH_MAPPER);
    }

    /**
     * Searches illustration plates by term clauses including comment terms.
     */
    public List<PlateSearchResultDto> searchPlatesByTermIncludingComments(
        String mosp,
        String iso,
        String regiso,
        String termClause,
        String commentTermClause,
        String additionalFilters,
        String unionSql
    ) {
        String first = appendFilters(SEARCH_BT_BEGRIFF_NEU + " and " + termClause, additionalFilters);
        String second = appendFilters(SEARCH_BT_BEGRIFF_NEU_KOMM + " and " + commentTermClause, additionalFilters);
        String sql = appendUnion(first, unionSql) + " union " + second + " order by Pos";
        return jdbc.query(sql, Map.of("mosp", mosp, "iso", iso, "regiso", regiso), PLATE_SEARCH_MAPPER);
    }

    /**
     * Searches part lines by designation.
     */
    public List<PartSearchLineDto> searchPartsByDesignation(
        String mosp,
        String iso,
        String regiso,
        String searchString,
        String additionalFilters,
        String unionSql
    ) {
        String sql = appendUnion(appendFilters(SEARCH_SNR_BENENNUNG, additionalFilters), unionSql)
            + " order by Benennung, Hauptgruppe, Untergruppe, Sachnummer";
        return jdbc.query(sql, Map.of("mosp", mosp, "iso", iso, "regiso", regiso, "suchstring", searchString),
            PART_SEARCH_LINE_MAPPER);
    }

    /**
     * Searches part lines by designation using special case patterns.
     */
    public List<PartSearchLineDto> searchPartsByDesignationSpecialCase(
        String mosp,
        String iso,
        String regiso,
        String searchString1,
        String searchString2,
        String additionalFilters,
        String unionSql
    ) {
        String sql = appendUnion(appendFilters(SEARCH_SNR_BENENNUNG_SONDERLOCKE, additionalFilters), unionSql)
            + " order by Benennung, Hauptgruppe, Untergruppe, Sachnummer";
        return jdbc.query(sql,
            Map.of("mosp", mosp, "iso", iso, "regiso", regiso, "suchstring1", searchString1, "suchstring2", searchString2),
            PART_SEARCH_LINE_MAPPER);
    }

    /**
     * Searches part lines by designation using Turkish locale replacements.
     */
    public List<PartSearchLineDto> searchPartsByDesignationTurkish(
        String mosp,
        String iso,
        String regiso,
        String searchString,
        String additionalFilters,
        String unionSql
    ) {
        String sql = appendUnion(appendFilters(SEARCH_SNR_BENENNUNG_TR, additionalFilters), unionSql)
            + " order by Benennung, Hauptgruppe, Untergruppe, Sachnummer";
        return jdbc.query(sql, Map.of("mosp", mosp, "iso", iso, "regiso", regiso, "suchstring", searchString),
            PART_SEARCH_LINE_MAPPER);
    }

    /**
     * Searches part lines by term clauses.
     */
    public List<PartSearchLineDto> searchPartsByTerm(
        String mosp,
        String iso,
        String regiso,
        String termClause,
        String commentTermClause,
        String additionalFilters,
        String unionSql
    ) {
        String first = appendFilters(SEARCH_SNR_BEGRIFF + " and " + termClause, additionalFilters);
        String second = appendFilters(SEARCH_SNR_BEGRIFF + " and " + commentTermClause, additionalFilters);
        String sql = appendUnion(first, unionSql) + " union " + second
            + " order by Benennung, Hauptgruppe, Untergruppe, Sachnummer";
        return jdbc.query(sql, Map.of("mosp", mosp, "iso", iso, "regiso", regiso), PART_SEARCH_LINE_MAPPER);
    }

    /**
     * Searches illustration plates by a complete part number.
     */
    public List<PlateSearchResultDto> searchPlatesByCompletePartNumber(
        String mosp,
        String produktart,
        String iso,
        String regiso,
        String sachnummer,
        String additionalFilters,
        String unionSql
    ) {
        String sql = appendUnion(appendFilters(SEARCH_BT_SACHNUMMER_COMPL, additionalFilters), unionSql)
            + " order by Pos";
        return jdbc.query(sql,
            Map.of("mosp", mosp, "produktart", produktart, "iso", iso, "regiso", regiso, "sachnummer", sachnummer),
            PLATE_SEARCH_MAPPER);
    }

    /**
     * Searches part lines by an incomplete part number.
     */
    public List<PartSearchLineDto> searchPartsByIncompletePartNumber(
        String mosp,
        String iso,
        String regiso,
        String sachnummer,
        String additionalFilters
    ) {
        String sql = appendFilters(SEARCH_SNR_SACHNUMMER_INCOMPL, additionalFilters);
        return jdbc.query(sql, Map.of("mosp", mosp, "iso", iso, "regiso", regiso, "sachnummer", sachnummer),
            PART_SEARCH_LINE_MAPPER);
    }

    /**
     * Searches part lines by a list of external numbers.
     */
    public List<PartSearchLineDto> searchPartsByExternalNumbers(
        String mosp,
        String iso,
        String regiso,
        List<String> externalNumbers,
        String additionalFilters
    ) {
        String sql = appendFilters(SEARCH_SNR_FREMDNR, additionalFilters);
        return jdbc.query(sql,
            Map.of("mosp", mosp, "iso", iso, "regiso", regiso, "fremdnummern", externalNumbers),
            PART_SEARCH_LINE_MAPPER);
    }

    /**
     * Searches illustration plates by a list of part numbers.
     */
    public List<PlateSearchResultDto> searchPlatesByPartNumbers(
        String mosp,
        String iso,
        String regiso,
        List<String> sachnummern,
        String additionalFilters,
        String unionSql
    ) {
        String sql = appendUnion(appendFilters(SEARCH_BT_SACHNUMMERN, additionalFilters), unionSql);
        return jdbc.query(sql, Map.of("mosp", mosp, "iso", iso, "regiso", regiso, "sachnummern", sachnummern),
            PLATE_SEARCH_MAPPER);
    }

    /**
     * Searches illustration plates by main group.
     */
    public List<PlateSearchResultDto> searchPlatesByMainGroup(
        String mosp,
        String iso,
        String regiso,
        String hg,
        String additionalFilters
    ) {
        String sql = appendFilters(SEARCH_BT_HGFG, additionalFilters);
        return jdbc.query(sql, Map.of("mosp", mosp, "iso", iso, "regiso", regiso, "hg", hg), PLATE_SEARCH_MAPPER);
    }

    /**
     * Searches illustration plates with graphics by main group.
     */
    public List<PlateGraphicSearchResultDto> searchPlatesByMainGroupWithGraphics(
        String mosp,
        String iso,
        String regiso,
        String hg,
        String additionalFilters
    ) {
        String sql = appendFilters(SEARCH_BT_HG_GRAFISCH_MIT_GRAFIKEN, additionalFilters);
        return jdbc.query(sql, Map.of("mosp", mosp, "iso", iso, "regiso", regiso, "hg", hg), PLATE_GRAPHIC_MAPPER);
    }

    /**
     * Searches illustration plates with graphics by main and function group.
     */
    public List<PlateGraphicSearchResultDto> searchPlatesByMainAndFunctionGroupWithGraphics(
        String mosp,
        String iso,
        String regiso,
        String hg,
        String fg,
        String additionalFilters
    ) {
        String sql = appendFilters(SEARCH_BT_HG_FG_GRAFISCH_MIT_GRAFIKEN, additionalFilters);
        return jdbc.query(sql,
            Map.of("mosp", mosp, "iso", iso, "regiso", regiso, "hg", hg, "fg", fg),
            PLATE_GRAPHIC_MAPPER);
    }

    /**
     * Counts illustration plates that have graphics for a main group.
     */
    public long countPlatesWithGraphicsForMainGroup(String mosp, String hg, String additionalFilters) {
        String sql = appendFilters(CHECK_BT_HG_GRAFISCH, additionalFilters);
        return jdbc.queryForObject(sql, Map.of("mosp", mosp, "hg", hg), Long.class);
    }

    /**
     * Retrieves change points for cable harness positions.
     */
    public List<ChangePointDto> findCableHarnessChangePoints(
        String mospId,
        String typeKey,
        String werk,
        Integer position,
        String btnr
    ) {
        return jdbc.query(SEARCH_KABELBAUM_CHANGEPOINTS,
            Map.of("mospid", mospId, "typschluessel", typeKey, "werk", werk, "position", position, "btnr", btnr),
            CHANGE_POINT_MAPPER);
    }

    private static Integer getInteger(java.sql.ResultSet rs, String column) {
        try {
            int value = rs.getInt(column);
            return rs.wasNull() ? null : value;
        } catch (java.sql.SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private static String appendFilters(String base, String additionalFilters) {
        if (!StringUtils.hasText(additionalFilters)) {
            return base;
        }
        return base + " " + additionalFilters;
    }

    private static String appendUnion(String base, String unionSql) {
        if (!StringUtils.hasText(unionSql)) {
            return base;
        }
        return base + " union " + unionSql;
    }
}
