package pl.etkx.dal.repository.vehicle;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.etkx.dal.dto.vehicle.BranchDto;
import pl.etkx.dal.dto.vehicle.BranchInfoDto;
import pl.etkx.dal.dto.vehicle.BranchLanguageDto;
import pl.etkx.dal.dto.vehicle.CompanyBranchUserDto;
import pl.etkx.dal.dto.vehicle.CompanyDto;
import pl.etkx.dal.dto.vehicle.ConfigurationDto;
import pl.etkx.dal.dto.vehicle.FunctionRightDto;
import pl.etkx.dal.dto.vehicle.LanguageDto;
import pl.etkx.dal.dto.vehicle.PermissionDto;
import pl.etkx.dal.dto.vehicle.UserDefaultBranchDto;
import pl.etkx.dal.dto.vehicle.UserDto;
import pl.etkx.dal.dto.vehicle.UserInfoDto;
import pl.etkx.dal.dto.vehicle.UserSummaryDto;

/**
 * Repository for company configuration workflows (Firmenkonfiguration).
 */
@Repository
@RequiredArgsConstructor
public class ConfigurationRepository {
    private static final String RETRIEVE_FIRMEN = """
        select firma_id Id,
            firma_bezeichnung Bezeichnung
        from w_firma@etk_nutzer
        order by Bezeichnung
        """;

    private static final String RETRIEVE_FILIALEN = """
        select filiale_id Id,
            filiale_bezeichnung Bezeichnung
        from w_filiale@etk_nutzer
        where filiale_firma_id = :firma
        order by Bezeichnung
        """;

    private static final String RETRIEVE_COUNT_FILIALEN_IN_FIRMA = """
        select Count(*) countFiliale
        from w_filiale@etk_nutzer
        where filiale_firma_id = :firma
        """;

    private static final String RETRIEVE_INFO_FILIALE = """
        select firma_id FirmaId,
            firma_bezeichnung FirmaBezeichnung,
            filiale_id FilialeId,
            filiale_bezeichnung FilialeBezeichnung,
            filiale_iso SpracheISO,
            filiale_regiso SpracheRegISO
        from w_firma@etk_nutzer, w_filiale@etk_nutzer
        where filiale_firma_id = :firma
          and filiale_id = :filiale
          and firma_id = filiale_firma_id
        """;

    private static final String RETRIEVE_EXIST_USER = """
        select count(user_id) ANZ
        from w_user@etk_nutzer
        where user_firma_id = :firma
          and user_id = :kennung
          and user_passwort = :passwort
        """;

    private static final String RETRIEVE_DMS_VERWENDEN = """
        select konfig_hs_verwenden DMSVerwenden
        from w_konfig@etk_nutzer
        where konfig_firma_id = :firma
          and konfig_filiale_id = :filiale
        """;

    private static final String LOAD_KONFIGURATION = """
        select konfig_hd_firma Firma,
            konfig_hd_zusatz Zusatz,
            konfig_hd_strasse Strasse,
            konfig_hd_plz Plz,
            konfig_hd_ort Ort,
            konfig_hd_telefon Telefon,
            konfig_hdnr_pkw Pkw,
            konfig_hdnr_motorrad Motorrad,
            konfig_mwst_niedrig MwstNiedrig,
            konfig_mwst_hoch MwstHoch,
            konfig_mwst_altteile MwstAltteile,
            konfig_mwst_3 Mwst3,
            konfig_mwst_4 Mwst4,
            konfig_rechnungnr RechnungsNr,
            konfig_mailserver Mailserver,
            konfig_barverkaufnr BarverkaufsNr,
            konfig_auftragnr AuftragsNr,
            konfig_kundennr KundenNr,
            konfig_hs_verwenden Verwenden,
            konfig_abwicklung Abwicklung,
            konfig_bestandfiliale Bestandfiliale,
            konfig_datenabgleich Datenabgleich
        from w_konfig@etk_nutzer
        where konfig_firma_id = :firma
          and konfig_filiale_id = :filiale
        """;

    private static final String DELETE_KONFIGURATION = """
        delete from w_konfig@etk_nutzer
        where konfig_firma_id = :firma
          and konfig_filiale_id = :filiale
        """;

    private static final String DELETE_ZUB_KONFIGURATION = """
        delete from w_zub_konfig@etk_nutzer
        where konfigz_firma_id = :firma
          and konfigz_filiale_id = :filiale
        """;

    private static final String INSERT_KONFIGURATION = """
        insert into w_konfig@etk_nutzer
            (konfig_firma_id, konfig_filiale_id, konfig_hd_firma, konfig_hd_zusatz, konfig_hd_strasse,
             konfig_hd_plz, konfig_hd_ort, konfig_hd_telefon, konfig_hdnr_pkw, konfig_hdnr_motorrad,
             konfig_mwst_niedrig, konfig_mwst_hoch, konfig_mwst_altteile, konfig_mwst_3, konfig_mwst_4,
             konfig_rechnungnr, konfig_mailserver, konfig_barverkaufnr, konfig_auftragnr, konfig_kundennr,
             konfig_hs_verwenden, konfig_abwicklung, konfig_bestandfiliale, konfig_datenabgleich)
        values
            (:firma, :filiale, :firmenname, :zusatz, :strasse, :plz, :ort, :telefon, :pkw, :motorrad,
             :mwstNiedrig, :mwstHoch, :mwstAltteile, :mwst3, :mwst4, :rechnungsNr, :mailserver, :barverkaufsNr,
             :auftragsNr, :kundenNr, :hsVerwenden, :abwicklung, :bestandFiliale, :datenabgleich)
        """;

    private static final String UPDATE_KONFIGURATION = """
        update w_konfig@etk_nutzer
        set konfig_hd_firma = :firmenname,
            konfig_hd_zusatz = :zusatz,
            konfig_hd_strasse = :strasse,
            konfig_hd_plz = :plz,
            konfig_hd_ort = :ort,
            konfig_hd_telefon = :telefon,
            konfig_hdnr_pkw = :pkw,
            konfig_hdnr_motorrad = :motorrad,
            konfig_mwst_niedrig = :mwstNiedrig,
            konfig_mwst_hoch = :mwstHoch,
            konfig_mwst_altteile = :mwstAltteile,
            konfig_mwst_3 = :mwst3,
            konfig_mwst_4 = :mwst4,
            konfig_rechnungnr = :rechnungsNr,
            konfig_mailserver = :mailserver,
            konfig_barverkaufnr = :barverkaufsNr,
            konfig_auftragnr = :auftragsNr,
            konfig_kundennr = :kundenNr,
            konfig_hs_verwenden = :hsVerwenden,
            konfig_abwicklung = :abwicklung,
            konfig_bestandfiliale = :bestandFiliale,
            konfig_datenabgleich = :datenabgleich
        where konfig_firma_id = :firma
          and konfig_filiale_id = :filiale
        """;

    private static final String SELECT_RECHNUNGSNUMMER_FOR_UPDATE = """
        select konfig_rechnungnr RechnungsNr
        from w_konfig@etk_nutzer
        where konfig_firma_id = :firma
          and konfig_filiale_id = :filiale
        for update
        """;

    private static final String UPDATE_RECHNUNGSNUMMER = """
        update w_konfig@etk_nutzer
        set konfig_rechnungnr = :rechnungsNr
        where konfig_firma_id = :firma
          and konfig_filiale_id = :filiale
        """;

    private static final String UPDATE_FIRMENBEZEICHNUNG = """
        update w_firma@etk_nutzer
        set firma_bezeichnung = :bezeichnung
        where firma_id = :id
        """;

    private static final String INSERT_FILIALE = """
        insert into w_filiale@etk_nutzer
            (filiale_firma_id, filiale_id, filiale_bezeichnung, filiale_iso, filiale_regiso)
        values
            (:firmaId, :filialId, :filiale, :iso, :regiso)
        """;

    private static final String DELETE_FILIALE = """
        delete from w_filiale@etk_nutzer
        where filiale_firma_id = :firmaId
          and filiale_id = :filialId
        """;

    private static final String UPDATE_FILIALE = """
        update w_filiale@etk_nutzer
        set filiale_bezeichnung = :filiale,
            filiale_iso = :iso,
            filiale_regiso = :regiso
        where filiale_firma_id = :firmaId
          and filiale_id = :filialId
        """;

    private static final String RETRIEVE_FILIALEN_SPRACHEN = """
        select filiale_id Id,
            filiale_bezeichnung Bezeichnung,
            filiale_iso Iso,
            filiale_regiso RegIso
        from w_filiale@etk_nutzer
        where filiale_firma_id = :firma
        order by filiale_id
        """;

    private static final String RETRIEVE_ANZAHL_NUTZER = """
        select count(user_id) NutzerAnzahl
        from w_user@etk_nutzer
        where user_firma_id = :firma
          and user_id not in (:ignoreUser)
          and (user_id like insensitive :krit or user_name like insensitive :krit)
        """;

    private static final String RETRIEVE_MATCHING_NUTZER = """
        select user_id NutzerId,
            user_name NutzerName
        from w_user@etk_nutzer
        where user_firma_id = :firma
          and user_id not in (:ignoreUser)
          and (user_id like insensitive :krit or user_name like insensitive :krit)
        order by user_name
        """;

    private static final String RETRIEVE_NUTZER = """
        select user_id NutzerId,
            user_name NutzerName,
            user_passwort Passwort,
            user_default_filiale_id DefaultFiliale,
            user_bearbeiternummer BearbeiterNummer
        from w_user@etk_nutzer
        where user_firma_id = :firma
        order by user_id
        """;

    private static final String RETRIEVE_SINGLE_NUTZER = """
        select user_name NutzerName,
            user_passwort Passwort,
            user_default_filiale_id DefaultFiliale,
            user_bearbeiternummer BearbeiterNummer
        from w_user@etk_nutzer
        where user_firma_id = :firma
          and user_id = :nutzerId
        """;

    private static final String RETRIEVE_BERECHTIGUNGEN = """
        select firmab_art Art,
            firmab_wert Wert
        from w_firma_berechtigungen@etk_nutzer
        where firmab_firma_id = :firma
        """;

    private static final String RETRIEVE_SPRACHEN = """
        select publben_bezeichnung Code,
            ben_text Benennung
        from w_publben, w_ben_gk
        where publben_art = 'S'
          and publben_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        """;

    private static final String RETRIEVE_FUNKTIONSRECHTE = """
        select publben_bezeichnung Bezeichnung,
            ben_text Text
        from w_publben, w_ben_gk
        where publben_art = 'R'
          and publben_textcode = ben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by ben_text
        """;

    private static final String RETRIEVE_NUTZER_FUNKTIONSRECHTE = """
        select publben_bezeichnung Bezeichnung,
            ben_text Text
        from w_user_funktionsrechte@etk_nutzer, w_ben_gk, w_publben
        where userf_firma_id = :firmaId
          and userf_id = :userId
          and publben_art = 'R'
          and publben_bezeichnung = userf_recht_id
          and ben_textcode = publben_textcode
          and ben_iso = :iso
          and ben_regiso = :regiso
        order by publben_bezeichnung
        """;

    private static final String RETRIEVE_NUTZER_BERECHTIGUNGEN = """
        select userb_art Art,
            userb_wert Wert
        from w_user_berechtigungen@etk_nutzer
        where userb_id = :user
          and userb_firma_id = :firma
        """;

    private static final String DELETE_NUTZER_BERECHTIGUNGEN = """
        delete from w_user_berechtigungen@etk_nutzer
        where userb_id = :id
          and userb_firma_id = :firma
        """;

    private static final String DELETE_NUTZER_FUNKTIONSRECHTE = """
        delete from w_user_funktionsrechte@etk_nutzer
        where userf_id = :id
          and userf_firma_id = :firma
        """;

    private static final String DELETE_NUTZER_EINSTELLUNGEN = """
        delete from w_user_einstellungen@etk_nutzer
        where user_id = :id
          and user_firma_id = :firma
        """;

    private static final String DELETE_ZUB_NUTZER = """
        delete from w_zub_user@etk_nutzer
        where userz_id = :id
          and userz_firma_id = :firma
        """;

    private static final String DELETE_NUTZER_EINSTELLUNGEN_REGION = """
        delete from w_user_einstellungen_region@etk_nutzer
        where user_id = :id
          and user_firma_id = :firma
        """;

    private static final String DELETE_NUTZER = """
        delete from w_user@etk_nutzer
        where user_id = :id
          and user_firma_id = :firma
        """;

    private static final String DELETE_NUTZER_TEILELISTEPOS = """
        delete from w_teilelistepos@etk_nutzer
        where teilelistepos_user_id = :id
          and teilelistepos_firma_id = :firma
        """;

    private static final String DELETE_NUTZER_TEILELISTE = """
        delete from w_teileliste@etk_nutzer
        where teileliste_user_id = :id
          and teileliste_firma_id = :firma
        """;

    private static final String DELETE_NUTZER_TEILELISTE_SENDEINFO = """
        delete from w_teileliste_sendeinfo@etk_nutzer
        where teilelistesi_user_id = :id
          and teilelistesi_firma_id = :firma
        """;

    private static final String DELETE_NUTZER_TEILEINFO = """
        delete from w_teileinfo@etk_nutzer
        where teileinfo_user_id = :id
          and teileinfo_firma_id = :firma
        """;

    private static final String RETRIEVE_EXIST_USER_ID = """
        select count(user_id) Cnt
        from w_user@etk_nutzer
        where user_firma_id = :firma
          and LOWER(user_id) = :kennung
        """;

    private static final String STORE_NUTZER = """
        insert into w_user@etk_nutzer
            (user_firma_id, user_id, user_name, user_passwort, user_default_filiale_id, user_bearbeiternummer)
        values
            (:firmaId, :userId, :userName, :password, :filiale, :bearbeiterNummer)
        """;

    private static final String STORE_NUTZER_BERECHTIGUNGEN = """
        insert into w_user_berechtigungen@etk_nutzer
            (userb_firma_id, userb_id, userb_art, userb_wert)
        values
            (:firmaId, :userId, :art, :wert)
        """;

    private static final String STORE_NUTZER_FUNKTIONSRECHTE = """
        insert into w_user_funktionsrechte@etk_nutzer
            (userf_firma_id, userf_id, userf_recht_id)
        values
            (:firmaId, :userId, :recht)
        """;

    private static final String UPDATE_NUTZER = """
        update w_user@etk_nutzer
        set user_name = :userName,
            user_passwort = :password,
            user_default_filiale_id = :defaultFiliale,
            user_bearbeiternummer = :bearbeiterNummer
        where user_firma_id = :firmaId
          and user_id = :userId
        """;

    private static final String MOVE_TEILELISTEN = """
        update w_teileliste@etk_nutzer
        set teileliste_filiale_id = :filialIdNeu
        where teileliste_firma_id = :firmaId
          and teileliste_filiale_id = :filialIdAlt
          and teileliste_user_id = :userId
          and teileliste_id in (
            select teileliste_id
            from w_teileliste@etk_nutzer
            where teileliste_auftragsnr is null
              and teileliste_firma_id = :firmaId
              and teileliste_filiale_id = :filialIdAlt
              and teileliste_user_id = :userId
          )
        """;

    private static final String MOVE_TEILELISTENPOS = """
        update w_teilelistepos@etk_nutzer
        set teilelistepos_filiale_id = :filialIdNeu
        where teilelistepos_firma_id = :firmaId
          and teilelistepos_filiale_id = :filialIdAlt
          and teilelistepos_user_id = :userId
          and teilelistepos_teileliste_id in (
            select teileliste_id
            from w_teileliste@etk_nutzer
            where teileliste_auftragsnr is null
              and teileliste_firma_id = :firmaId
              and teileliste_filiale_id = :filialIdAlt
              and teileliste_user_id = :userId
          )
        """;

    private static final String MOVE_TEILELISTENSI = """
        update w_teileliste_sendeinfo@etk_nutzer
        set teilelistesi_filiale_id = :filialIdNeu
        where teilelistesi_firma_id = :firmaId
          and teilelistesi_filiale_id = :filialIdAlt
          and teilelistesi_user_id = :userId
          and teilelistesi_teileliste_id in (
            select teileliste_id
            from w_teileliste@etk_nutzer
            where teileliste_auftragsnr is null
              and teileliste_firma_id = :firmaId
              and teileliste_filiale_id = :filialIdAlt
              and teileliste_user_id = :userId
          )
        """;

    private static final String RETRIEVE_USERINFO = """
        select user_id UserId,
            user_passwort Passwort,
            user_default_filiale_id DefaultFiliale,
            user_bearbeiternummer BearbeiterNummer,
            filiale_bezeichnung FilialBezeichnung,
            filiale_iso Iso,
            filiale_regiso RegIso
        from w_user@etk_nutzer, w_filiale@etk_nutzer
        where user_firma_id = :firma
          and user_id = :user
          and user_default_filiale_id = filiale_id
          and user_firma_id = filiale_firma_id
        """;

    private static final String UPDATE_DEFAULT_FILIALE = """
        update w_user@etk_nutzer
        set user_default_filiale_id = :filialId
        where user_firma_id = :firmaId
          and user_id = :userId
        """;

    private static final String RETRIEVE_EINEFIRMAFILIALENUTZER = """
        select firma_id FirmaId,
            filiale_id FilialId,
            user_id UserId
        from w_firma@etk_nutzer
        left join w_filiale@etk_nutzer on (filiale_firma_id = firma_id)
        left join w_user@etk_nutzer on (user_firma_id = firma_id)
        """;

    private static final String RETRIEVE_USER_BY_DEFAULT_FILIALE = """
        select user_id NutzerId,
            user_name NutzerName,
            user_passwort Passwort,
            user_bearbeiternummer BearbeiterNummer
        from w_user@etk_nutzer
        where user_default_filiale_id = :filialId
          and user_firma_id = :firmaId
        order by user_name
        """;

    private static final String RETRIEVE_BESTELLLISTE_POSITIONEN = """
        select *
        from w_bestelllistepos@etk_nutzer
        where bestelllistepos_firma_id = :firmaId
          and bestelllistepos_filiale_id = :filialId
        """;

    private static final String RETRIEVE_TEILELISTE_POSITIONEN = """
        select *
        from w_teilelistepos@etk_nutzer
        where teilelistepos_firma_id = :firmaId
          and teilelistepos_filiale_id = :filialId
        """;

    private static final String DELETE_ALL_AUFTRAEGE = """
        delete from w_auftrag@etk_nutzer
        where auftrag_firma_id = :firmaId
          and auftrag_filiale_id = :filialId
        """;

    private static final String DELETE_ALL_TEILELISTEN = """
        delete from w_teileliste@etk_nutzer
        where teileliste_firma_id = :firmaId
          and teileliste_filiale_id = :filialId
        """;

    private static final String DELETE_ALL_TEILELISTEN_POSITIONEN = """
        delete from w_teilelistepos@etk_nutzer
        where teilelistepos_firma_id = :firmaId
          and teilelistepos_filiale_id = :filialId
        """;

    private static final String DELETE_ALL_TEILELISTEN_SI = """
        delete from w_teileliste_sendeinfo@etk_nutzer
        where teilelistesi_firma_id = :firmaId
          and teilelistesi_filiale_id = :filialId
        """;

    private static final String DELETE_ALL_BESTELLLISTEN = """
        delete from w_bestellliste@etk_nutzer
        where bestellliste_firma_id = :firmaId
          and bestellliste_filiale_id = :filialId
        """;

    private static final String DELETE_ALL_BESTELLLISTEN_POSITIONEN = """
        delete from w_bestelllistepos@etk_nutzer
        where bestelllistepos_firma_id = :firmaId
          and bestelllistepos_filiale_id = :filialId
        """;

    private static final String EXISTIERT_FILIAL_ZUB = """
        select count(*)
        from w_zub_konfig@etk_nutzer
        where konfigz_firma_id = :firmaId
          and konfigz_filiale_id = :filialId
          and konfigz_default_markt_id is not null
        """;

    private static final String UPDATE_MARKTID_NUTZER_VON_FILIALE = """
        update w_user@etk_nutzer
        set user_marktid = (
            select konfigz_default_markt_id
            from w_zub_konfig@etk_nutzer
            where konfigz_filiale_id = :filialId
              and konfigz_firma_id = user_firma_id
        )
        where user_firma_id = :firmaId
          and user_id = :userId
        """;

    private static final RowMapper<CompanyDto> COMPANY_MAPPER = (rs, rowNum) ->
        CompanyDto.builder()
            .id(rs.getString("Id"))
            .bezeichnung(rs.getString("Bezeichnung"))
            .build();

    private static final RowMapper<BranchDto> BRANCH_MAPPER = (rs, rowNum) ->
        BranchDto.builder()
            .id(rs.getString("Id"))
            .bezeichnung(rs.getString("Bezeichnung"))
            .build();

    private static final RowMapper<BranchInfoDto> BRANCH_INFO_MAPPER = (rs, rowNum) ->
        BranchInfoDto.builder()
            .firmaId(rs.getString("FirmaId"))
            .firmaBezeichnung(rs.getString("FirmaBezeichnung"))
            .filialeId(rs.getString("FilialeId"))
            .filialeBezeichnung(rs.getString("FilialeBezeichnung"))
            .spracheIso(rs.getString("SpracheISO"))
            .spracheRegIso(rs.getString("SpracheRegISO"))
            .build();

    private static final RowMapper<BranchLanguageDto> BRANCH_LANGUAGE_MAPPER = (rs, rowNum) ->
        BranchLanguageDto.builder()
            .id(rs.getString("Id"))
            .bezeichnung(rs.getString("Bezeichnung"))
            .iso(rs.getString("Iso"))
            .regIso(rs.getString("RegIso"))
            .build();

    private static final RowMapper<UserSummaryDto> USER_SUMMARY_MAPPER = (rs, rowNum) ->
        UserSummaryDto.builder()
            .nutzerId(rs.getString("NutzerId"))
            .nutzerName(rs.getString("NutzerName"))
            .build();

    private static final RowMapper<UserDto> USER_MAPPER = (rs, rowNum) ->
        UserDto.builder()
            .nutzerId(rs.getString("NutzerId"))
            .nutzerName(rs.getString("NutzerName"))
            .passwort(rs.getString("Passwort"))
            .defaultFiliale(rs.getString("DefaultFiliale"))
            .bearbeiterNummer(rs.getString("BearbeiterNummer"))
            .build();

    private static final RowMapper<PermissionDto> PERMISSION_MAPPER = (rs, rowNum) ->
        PermissionDto.builder()
            .art(rs.getString("Art"))
            .wert(rs.getString("Wert"))
            .build();

    private static final RowMapper<LanguageDto> LANGUAGE_MAPPER = (rs, rowNum) ->
        LanguageDto.builder()
            .code(rs.getString("Code"))
            .benennung(rs.getString("Benennung"))
            .build();

    private static final RowMapper<FunctionRightDto> FUNCTION_RIGHT_MAPPER = (rs, rowNum) ->
        FunctionRightDto.builder()
            .bezeichnung(rs.getString("Bezeichnung"))
            .text(rs.getString("Text"))
            .build();

    private static final RowMapper<ConfigurationDto> CONFIGURATION_MAPPER = (rs, rowNum) ->
        ConfigurationDto.builder()
            .firma(rs.getString("Firma"))
            .zusatz(rs.getString("Zusatz"))
            .strasse(rs.getString("Strasse"))
            .plz(rs.getString("Plz"))
            .ort(rs.getString("Ort"))
            .telefon(rs.getString("Telefon"))
            .pkw(rs.getString("Pkw"))
            .motorrad(rs.getString("Motorrad"))
            .mwstNiedrig(rs.getString("MwstNiedrig"))
            .mwstHoch(rs.getString("MwstHoch"))
            .mwstAltteile(rs.getString("MwstAltteile"))
            .mwst3(rs.getString("Mwst3"))
            .mwst4(rs.getString("Mwst4"))
            .rechnungsNr(rs.getString("RechnungsNr"))
            .mailserver(rs.getString("Mailserver"))
            .barverkaufsNr(rs.getString("BarverkaufsNr"))
            .auftragsNr(rs.getString("AuftragsNr"))
            .kundenNr(rs.getString("KundenNr"))
            .verwenden(rs.getString("Verwenden"))
            .abwicklung(rs.getString("Abwicklung"))
            .bestandfiliale(rs.getString("Bestandfiliale"))
            .datenabgleich(rs.getString("Datenabgleich"))
            .build();

    private static final RowMapper<UserInfoDto> USER_INFO_MAPPER = (rs, rowNum) ->
        UserInfoDto.builder()
            .userId(rs.getString("UserId"))
            .passwort(rs.getString("Passwort"))
            .defaultFiliale(rs.getString("DefaultFiliale"))
            .bearbeiterNummer(rs.getString("BearbeiterNummer"))
            .filialBezeichnung(rs.getString("FilialBezeichnung"))
            .iso(rs.getString("Iso"))
            .regIso(rs.getString("RegIso"))
            .build();

    private static final RowMapper<CompanyBranchUserDto> COMPANY_BRANCH_USER_MAPPER = (rs, rowNum) ->
        CompanyBranchUserDto.builder()
            .firmaId(rs.getString("FirmaId"))
            .filialeId(rs.getString("FilialId"))
            .userId(rs.getString("UserId"))
            .build();

    private static final RowMapper<UserDefaultBranchDto> USER_DEFAULT_BRANCH_MAPPER = (rs, rowNum) ->
        UserDefaultBranchDto.builder()
            .nutzerId(rs.getString("NutzerId"))
            .nutzerName(rs.getString("NutzerName"))
            .passwort(rs.getString("Passwort"))
            .bearbeiterNummer(rs.getString("BearbeiterNummer"))
            .build();

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Retrieves companies.
     */
    public List<CompanyDto> findCompanies() {
        return jdbc.query(RETRIEVE_FIRMEN, COMPANY_MAPPER);
    }

    /**
     * Retrieves branches for a company.
     */
    public List<BranchDto> findBranches(String firma) {
        return jdbc.query(RETRIEVE_FILIALEN, Map.of("firma", firma), BRANCH_MAPPER);
    }

    /**
     * Counts branches for a company.
     */
    public long countBranches(String firma) {
        return jdbc.queryForObject(RETRIEVE_COUNT_FILIALEN_IN_FIRMA, Map.of("firma", firma), Long.class);
    }

    /**
     * Retrieves branch information.
     */
    public List<BranchInfoDto> findBranchInfo(String firma, String filiale) {
        return jdbc.query(RETRIEVE_INFO_FILIALE, Map.of(
            "firma", firma,
            "filiale", filiale
        ), BRANCH_INFO_MAPPER);
    }

    /**
     * Checks whether a user exists for the given credentials.
     */
    public long countExistingUser(String firma, String kennung, String passwort) {
        return jdbc.queryForObject(RETRIEVE_EXIST_USER, Map.of(
            "firma", firma,
            "kennung", kennung,
            "passwort", passwort
        ), Long.class);
    }

    /**
     * Retrieves DMS usage flag.
     */
    public String findDmsUsage(String firma, String filiale) {
        return jdbc.queryForObject(RETRIEVE_DMS_VERWENDEN, Map.of(
            "firma", firma,
            "filiale", filiale
        ), String.class);
    }

    /**
     * Loads configuration settings for a company and branch.
     */
    public List<ConfigurationDto> loadConfiguration(String firma, String filiale) {
        return jdbc.query(LOAD_KONFIGURATION, Map.of(
            "firma", firma,
            "filiale", filiale
        ), CONFIGURATION_MAPPER);
    }

    /**
     * Deletes configuration settings for a company and branch.
     */
    public int deleteConfiguration(String firma, String filiale) {
        return jdbc.update(DELETE_KONFIGURATION, Map.of(
            "firma", firma,
            "filiale", filiale
        ));
    }

    /**
     * Deletes accessory configuration settings for a company and branch.
     */
    public int deleteAccessoryConfiguration(String firma, String filiale) {
        return jdbc.update(DELETE_ZUB_KONFIGURATION, Map.of(
            "firma", firma,
            "filiale", filiale
        ));
    }

    /**
     * Inserts configuration settings.
     */
    public int insertConfiguration(Map<String, Object> params) {
        return jdbc.update(INSERT_KONFIGURATION, params);
    }

    /**
     * Updates configuration settings.
     */
    public int updateConfiguration(Map<String, Object> params) {
        return jdbc.update(UPDATE_KONFIGURATION, params);
    }

    /**
     * Retrieves the current invoice number with a row lock.
     */
    public String findInvoiceNumberForUpdate(String firma, String filiale) {
        return jdbc.queryForObject(SELECT_RECHNUNGSNUMMER_FOR_UPDATE, Map.of(
            "firma", firma,
            "filiale", filiale
        ), String.class);
    }

    /**
     * Updates the invoice number.
     */
    public int updateInvoiceNumber(String firma, String filiale, String rechnungsNr) {
        return jdbc.update(UPDATE_RECHNUNGSNUMMER, Map.of(
            "firma", firma,
            "filiale", filiale,
            "rechnungsNr", rechnungsNr
        ));
    }

    /**
     * Updates the company name.
     */
    public int updateCompanyName(String id, String bezeichnung) {
        return jdbc.update(UPDATE_FIRMENBEZEICHNUNG, Map.of(
            "id", id,
            "bezeichnung", bezeichnung
        ));
    }

    /**
     * Inserts a branch.
     */
    public int insertBranch(String firmaId, String filialId, String filiale, String iso, String regiso) {
        return jdbc.update(INSERT_FILIALE, Map.of(
            "firmaId", firmaId,
            "filialId", filialId,
            "filiale", filiale,
            "iso", iso,
            "regiso", regiso
        ));
    }

    /**
     * Deletes a branch.
     */
    public int deleteBranch(String firmaId, String filialId) {
        return jdbc.update(DELETE_FILIALE, Map.of(
            "firmaId", firmaId,
            "filialId", filialId
        ));
    }

    /**
     * Updates a branch.
     */
    public int updateBranch(String firmaId, String filialId, String filiale, String iso, String regiso) {
        return jdbc.update(UPDATE_FILIALE, Map.of(
            "firmaId", firmaId,
            "filialId", filialId,
            "filiale", filiale,
            "iso", iso,
            "regiso", regiso
        ));
    }

    /**
     * Retrieves branches with language settings.
     */
    public List<BranchLanguageDto> findBranchLanguages(String firma) {
        return jdbc.query(RETRIEVE_FILIALEN_SPRACHEN, Map.of("firma", firma), BRANCH_LANGUAGE_MAPPER);
    }

    /**
     * Counts matching users with a search criterion.
     */
    public long countMatchingUsers(String firma, List<String> ignoreUser, String krit) {
        return jdbc.queryForObject(RETRIEVE_ANZAHL_NUTZER, Map.of(
            "firma", firma,
            "ignoreUser", ignoreUser,
            "krit", krit
        ), Long.class);
    }

    /**
     * Retrieves matching users with a search criterion.
     */
    public List<UserSummaryDto> findMatchingUsers(String firma, List<String> ignoreUser, String krit) {
        return jdbc.query(RETRIEVE_MATCHING_NUTZER, Map.of(
            "firma", firma,
            "ignoreUser", ignoreUser,
            "krit", krit
        ), USER_SUMMARY_MAPPER);
    }

    /**
     * Retrieves users for a company.
     */
    public List<UserDto> findUsers(String firma) {
        return jdbc.query(RETRIEVE_NUTZER, Map.of("firma", firma), USER_MAPPER);
    }

    /**
     * Retrieves a single user record.
     */
    public List<UserDto> findUser(String firma, String nutzerId) {
        return jdbc.query(RETRIEVE_SINGLE_NUTZER, Map.of(
            "firma", firma,
            "nutzerId", nutzerId
        ), USER_MAPPER);
    }

    /**
     * Retrieves company permissions.
     */
    public List<PermissionDto> findCompanyPermissions(String firma) {
        return jdbc.query(RETRIEVE_BERECHTIGUNGEN, Map.of("firma", firma), PERMISSION_MAPPER);
    }

    /**
     * Retrieves language descriptions.
     */
    public List<LanguageDto> findLanguages(String iso, String regiso) {
        return jdbc.query(RETRIEVE_SPRACHEN, Map.of(
            "iso", iso,
            "regiso", regiso
        ), LANGUAGE_MAPPER);
    }

    /**
     * Retrieves functional rights.
     */
    public List<FunctionRightDto> findFunctionRights(String iso, String regiso) {
        return jdbc.query(RETRIEVE_FUNKTIONSRECHTE, Map.of(
            "iso", iso,
            "regiso", regiso
        ), FUNCTION_RIGHT_MAPPER);
    }

    /**
     * Retrieves functional rights assigned to a user.
     */
    public List<FunctionRightDto> findUserFunctionRights(String firmaId, String userId, String iso, String regiso) {
        return jdbc.query(RETRIEVE_NUTZER_FUNKTIONSRECHTE, Map.of(
            "firmaId", firmaId,
            "userId", userId,
            "iso", iso,
            "regiso", regiso
        ), FUNCTION_RIGHT_MAPPER);
    }

    /**
     * Retrieves user permissions.
     */
    public List<PermissionDto> findUserPermissions(String user, String firma) {
        return jdbc.query(RETRIEVE_NUTZER_BERECHTIGUNGEN, Map.of(
            "user", user,
            "firma", firma
        ), PERMISSION_MAPPER);
    }

    /**
     * Deletes user permissions.
     */
    public int deleteUserPermissions(String id, String firma) {
        return jdbc.update(DELETE_NUTZER_BERECHTIGUNGEN, Map.of(
            "id", id,
            "firma", firma
        ));
    }

    /**
     * Deletes user functional rights.
     */
    public int deleteUserFunctionRights(String id, String firma) {
        return jdbc.update(DELETE_NUTZER_FUNKTIONSRECHTE, Map.of(
            "id", id,
            "firma", firma
        ));
    }

    /**
     * Deletes user settings.
     */
    public int deleteUserSettings(String id, String firma) {
        return jdbc.update(DELETE_NUTZER_EINSTELLUNGEN, Map.of(
            "id", id,
            "firma", firma
        ));
    }

    /**
     * Deletes accessory user settings.
     */
    public int deleteAccessoryUser(String id, String firma) {
        return jdbc.update(DELETE_ZUB_NUTZER, Map.of(
            "id", id,
            "firma", firma
        ));
    }

    /**
     * Deletes user region settings.
     */
    public int deleteUserRegionSettings(String id, String firma) {
        return jdbc.update(DELETE_NUTZER_EINSTELLUNGEN_REGION, Map.of(
            "id", id,
            "firma", firma
        ));
    }

    /**
     * Deletes a user.
     */
    public int deleteUser(String id, String firma) {
        return jdbc.update(DELETE_NUTZER, Map.of(
            "id", id,
            "firma", firma
        ));
    }

    /**
     * Deletes user part list positions.
     */
    public int deleteUserPartListPositions(String id, String firma) {
        return jdbc.update(DELETE_NUTZER_TEILELISTEPOS, Map.of(
            "id", id,
            "firma", firma
        ));
    }

    /**
     * Deletes user part lists.
     */
    public int deleteUserPartLists(String id, String firma) {
        return jdbc.update(DELETE_NUTZER_TEILELISTE, Map.of(
            "id", id,
            "firma", firma
        ));
    }

    /**
     * Deletes user part list send info entries.
     */
    public int deleteUserPartListSendInfo(String id, String firma) {
        return jdbc.update(DELETE_NUTZER_TEILELISTE_SENDEINFO, Map.of(
            "id", id,
            "firma", firma
        ));
    }

    /**
     * Deletes user part info entries.
     */
    public int deleteUserPartInfo(String id, String firma) {
        return jdbc.update(DELETE_NUTZER_TEILEINFO, Map.of(
            "id", id,
            "firma", firma
        ));
    }

    /**
     * Checks whether a user id exists.
     */
    public long countExistingUserId(String firma, String kennung) {
        return jdbc.queryForObject(RETRIEVE_EXIST_USER_ID, Map.of(
            "firma", firma,
            "kennung", kennung
        ), Long.class);
    }

    /**
     * Stores a user.
     */
    public int storeUser(String firmaId, String userId, String userName, String password,
                         String filiale, String bearbeiterNummer) {
        return jdbc.update(STORE_NUTZER, Map.of(
            "firmaId", firmaId,
            "userId", userId,
            "userName", userName,
            "password", password,
            "filiale", filiale,
            "bearbeiterNummer", bearbeiterNummer
        ));
    }

    /**
     * Stores user permissions.
     */
    public int storeUserPermission(String firmaId, String userId, String art, String wert) {
        return jdbc.update(STORE_NUTZER_BERECHTIGUNGEN, Map.of(
            "firmaId", firmaId,
            "userId", userId,
            "art", art,
            "wert", wert
        ));
    }

    /**
     * Stores user functional rights.
     */
    public int storeUserFunctionRight(String firmaId, String userId, String recht) {
        return jdbc.update(STORE_NUTZER_FUNKTIONSRECHTE, Map.of(
            "firmaId", firmaId,
            "userId", userId,
            "recht", recht
        ));
    }

    /**
     * Updates user data.
     */
    public int updateUser(String firmaId, String userId, String userName, String password,
                          String defaultFiliale, String bearbeiterNummer) {
        return jdbc.update(UPDATE_NUTZER, Map.of(
            "firmaId", firmaId,
            "userId", userId,
            "userName", userName,
            "password", password,
            "defaultFiliale", defaultFiliale,
            "bearbeiterNummer", bearbeiterNummer
        ));
    }

    /**
     * Moves part lists to a new branch.
     */
    public int movePartLists(String firmaId, String filialIdAlt, String filialIdNeu, String userId) {
        return jdbc.update(MOVE_TEILELISTEN, Map.of(
            "firmaId", firmaId,
            "filialIdAlt", filialIdAlt,
            "filialIdNeu", filialIdNeu,
            "userId", userId
        ));
    }

    /**
     * Moves part list positions to a new branch.
     */
    public int movePartListPositions(String firmaId, String filialIdAlt, String filialIdNeu, String userId) {
        return jdbc.update(MOVE_TEILELISTENPOS, Map.of(
            "firmaId", firmaId,
            "filialIdAlt", filialIdAlt,
            "filialIdNeu", filialIdNeu,
            "userId", userId
        ));
    }

    /**
     * Moves part list send info entries to a new branch.
     */
    public int movePartListSendInfo(String firmaId, String filialIdAlt, String filialIdNeu, String userId) {
        return jdbc.update(MOVE_TEILELISTENSI, Map.of(
            "firmaId", firmaId,
            "filialIdAlt", filialIdAlt,
            "filialIdNeu", filialIdNeu,
            "userId", userId
        ));
    }

    /**
     * Retrieves detailed user info including branch data.
     */
    public List<UserInfoDto> findUserInfo(String firma, String user) {
        return jdbc.query(RETRIEVE_USERINFO, Map.of(
            "firma", firma,
            "user", user
        ), USER_INFO_MAPPER);
    }

    /**
     * Updates the default branch for a user.
     */
    public int updateDefaultBranch(String firmaId, String userId, String filialId) {
        return jdbc.update(UPDATE_DEFAULT_FILIALE, Map.of(
            "firmaId", firmaId,
            "userId", userId,
            "filialId", filialId
        ));
    }

    /**
     * Retrieves company, branch and user identifiers.
     */
    public List<CompanyBranchUserDto> findCompanyBranchUsers() {
        return jdbc.query(RETRIEVE_EINEFIRMAFILIALENUTZER, COMPANY_BRANCH_USER_MAPPER);
    }

    /**
     * Retrieves users by default branch.
     */
    public List<UserDefaultBranchDto> findUsersByDefaultBranch(String firmaId, String filialId) {
        return jdbc.query(RETRIEVE_USER_BY_DEFAULT_FILIALE, Map.of(
            "firmaId", firmaId,
            "filialId", filialId
        ), USER_DEFAULT_BRANCH_MAPPER);
    }

    /**
     * Retrieves order list positions for a branch.
     */
    public List<Map<String, Object>> findOrderListPositions(String firmaId, String filialId) {
        return jdbc.queryForList(RETRIEVE_BESTELLLISTE_POSITIONEN, Map.of(
            "firmaId", firmaId,
            "filialId", filialId
        ));
    }

    /**
     * Retrieves part list positions for a branch.
     */
    public List<Map<String, Object>> findPartListPositions(String firmaId, String filialId) {
        return jdbc.queryForList(RETRIEVE_TEILELISTE_POSITIONEN, Map.of(
            "firmaId", firmaId,
            "filialId", filialId
        ));
    }

    /**
     * Deletes all orders for a branch.
     */
    public int deleteAllOrders(String firmaId, String filialId) {
        return jdbc.update(DELETE_ALL_AUFTRAEGE, Map.of(
            "firmaId", firmaId,
            "filialId", filialId
        ));
    }

    /**
     * Deletes all part lists for a branch.
     */
    public int deleteAllPartLists(String firmaId, String filialId) {
        return jdbc.update(DELETE_ALL_TEILELISTEN, Map.of(
            "firmaId", firmaId,
            "filialId", filialId
        ));
    }

    /**
     * Deletes all part list positions for a branch.
     */
    public int deleteAllPartListPositions(String firmaId, String filialId) {
        return jdbc.update(DELETE_ALL_TEILELISTEN_POSITIONEN, Map.of(
            "firmaId", firmaId,
            "filialId", filialId
        ));
    }

    /**
     * Deletes all part list send info entries for a branch.
     */
    public int deleteAllPartListSendInfo(String firmaId, String filialId) {
        return jdbc.update(DELETE_ALL_TEILELISTEN_SI, Map.of(
            "firmaId", firmaId,
            "filialId", filialId
        ));
    }

    /**
     * Deletes all order lists for a branch.
     */
    public int deleteAllOrderLists(String firmaId, String filialId) {
        return jdbc.update(DELETE_ALL_BESTELLLISTEN, Map.of(
            "firmaId", firmaId,
            "filialId", filialId
        ));
    }

    /**
     * Deletes all order list positions for a branch.
     */
    public int deleteAllOrderListPositions(String firmaId, String filialId) {
        return jdbc.update(DELETE_ALL_BESTELLLISTEN_POSITIONEN, Map.of(
            "firmaId", firmaId,
            "filialId", filialId
        ));
    }

    /**
     * Checks whether a default market is configured for a branch.
     */
    public long countBranchAccessoryConfig(String firmaId, String filialId) {
        return jdbc.queryForObject(EXISTIERT_FILIAL_ZUB, Map.of(
            "firmaId", firmaId,
            "filialId", filialId
        ), Long.class);
    }

    /**
     * Updates user market id based on branch configuration.
     */
    public int updateUserMarketId(String firmaId, String filialId, String userId) {
        return jdbc.update(UPDATE_MARKTID_NUTZER_VON_FILIALE, Map.of(
            "firmaId", firmaId,
            "filialId", filialId,
            "userId", userId
        ));
    }
}
