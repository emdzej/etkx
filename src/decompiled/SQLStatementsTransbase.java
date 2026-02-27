/*
 * Decompiled with CFR 0.152.
 */
package webetk.db;

import java.util.Locale;
import webetk.db.SQLStatements;

public final class SQLStatementsTransbase
extends SQLStatements {

    public static final class ETKTexte
    extends SQLStatements.ETKTexte {
        public final String RETRIEVE_ETKTEXTE() {
            return "select bedetkt_elemid ElemId,      bedetkt_hg HG,      bedetkt_fg FG,      bedetkt_produktart Produktart,      bedetkt_kommid KommId  from w_bed_etktext  order by Produktart, HG, FG";
        }

        public final String RETRIEVE_ETKTEXTE_KOMMENTARE() {
            return "select DISTINCT bedetkt_kommid KommId,               ben_text Text,              komm_pos KommPos from w_bed_etktext, w_komm, w_ben_gk  where komm_id = bedetkt_kommid    and ben_textcode = komm_textcode    and ben_iso = '&ISO&'    and ben_regiso = '&REGISO&'  order by KommId, KommPos";
        }
    }

    public static final class PerformanceLog
    extends SQLStatements.PerformanceLog {
        public final String GET_NEXT_REQUESTID_SEQ_VAL() {
            return "nur fuer oracle";
        }

        public final String LOAD_LOG_PERFORMANCE_FLAG() {
            return "nur fuer oracle";
        }

        public final String WRITE_PERFORMANCE_LOG() {
            return "nur fuer oracle";
        }
    }

    public static final class MailOptions
    extends SQLStatements.MailOptions {
        public final String LOAD_ABSENDER_UND_EMPFAENGER() {
            return "SELECT usermo_krit_art Kriterium, usermo_krit_wert Wert FROM w_user_mailoptions@etk_nutzer WHERE  usermo_firma_id = ? AND  usermo_user_id = ? AND (usermo_krit_art = 'E-Mail-Adresse Empfaenger' OR usermo_krit_art = 'E-Mail-Adresse Absender' OR usermo_krit_art = 'Name Absender') ";
        }

        public final String INSERT_ABSENDER() {
            return "INSERT INTO w_user_mailoptions@etk_nutzer (usermo_firma_id, usermo_user_id, usermo_krit_art, usermo_krit_wert) TABLE ( ( ?, ?, 'E-Mail-Adresse Absender', ? ), ( ?, ?, 'Name Absender', ? ) ) ";
        }

        public final String DELETE_ABSENDER() {
            return "DELETE FROM w_user_mailoptions@etk_nutzer WHERE  usermo_firma_id = ? AND  usermo_user_id = ? AND (usermo_krit_art = 'Name Absender' OR usermo_krit_art = 'E-Mail-Adresse Absender') ";
        }

        public final String INSERT_EMPFAENGER() {
            return "INSERT INTO w_user_mailoptions@etk_nutzer (usermo_firma_id, usermo_user_id, usermo_krit_art, usermo_krit_wert) VALUES ( ?, ?, 'E-Mail-Adresse Empfaenger', ? ) ";
        }

        public final String DELETE_EMPAENGER() {
            return "DELETE FROM w_user_mailoptions@etk_nutzer WHERE  usermo_firma_id = ? AND  usermo_user_id = ? AND  usermo_krit_art = 'E-Mail-Adresse Empfaenger' ";
        }
    }

    public static final class TabellenKonfiguration
    extends SQLStatements.TabellenKonfiguration {
        public final String LOAD_KONFIG() {
            return "SELECT usertk_column_index Spalte,  usertk_column_name Name FROM w_user_tabellenkonfig@etk_nutzer WHERE  usertk_firma_id = ? AND  usertk_user_id = ? AND  usertk_table_name = ? AND  usertk_zusatz = ? ORDER BY usertk_column_index ";
        }

        public final String INSERT_SPALTE() {
            return "INSERT INTO w_user_tabellenkonfig@etk_nutzer (usertk_firma_id, usertk_user_id, usertk_table_name, usertk_zusatz, usertk_column_name, usertk_column_index ) VALUES ( ?, ?, ?, ?, ?, ? )";
        }

        public final String DELETE_KONFIG() {
            return "DELETE FROM w_user_tabellenkonfig@etk_nutzer WHERE  usertk_firma_id = ? AND  usertk_user_id = ? AND  usertk_table_name = ? AND  usertk_zusatz = ? ";
        }
    }

    public static final class LoginInfo
    extends SQLStatements.LoginInfo {
        public final String LOAD_SINGLE_USER_LOGIN_INFO() {
            return "SELECT\tuserlog_eingeloggt Eingeloggt, \t\t\t\tuserlog_lastlogin LastLogin, \t\t\t\tuserlog_anzahl_logins AnzahlLogins FROM \tw_user_log@etk_nutzer WHERE\tuserlog_firma_id = ? AND\t\tuserlog_user_id = ? ";
        }

        public final String INSERT_NEW_LOGIN_INFO() {
            return "INSERT INTO w_user_log@etk_nutzer ( \t\t\t\tuserlog_firma_id, \t\t\t\tuserlog_user_id, \t\t\t\tuserlog_eingeloggt, \t\t\t\tuserlog_lastlogin, \t\t\t\tuserlog_anzahl_logins ) VALUES ( ?, ?, 'J', ?, 1 ) ";
        }

        public final String UPDATE_LOGIN_INFO() {
            return "UPDATE w_user_log@etk_nutzer SET    userlog_eingeloggt = 'J', \t\t\t\tuserlog_lastlogin = ?, \t\t\t\tuserlog_anzahl_logins = userlog_anzahl_logins + 1 WHERE\tuserlog_firma_id = ? AND    userlog_user_id = ? ";
        }

        public final String UPDATE_LOGIN_INFO_LOGOUT() {
            return "UPDATE w_user_log@etk_nutzer SET    userlog_eingeloggt = 'N' WHERE\tuserlog_firma_id = ? AND    userlog_user_id = ? ";
        }

        public final String GET_LOGGEDIN_USERS() {
            return "SELECT userlog_user_id UserId,        user_name UserName, \t      userlog_lastlogin LastLogin, \t      userlog_anzahl_logins AnzahlLogins,       user_default_filiale_id DefaultFiliale FROM   w_user_log@etk_nutzer, w_user@etk_nutzer WHERE  user_firma_id = userlog_firma_id   AND  user_id = userlog_user_id   AND  user_id NOT IN (&IGNORE_USER&)   AND  userlog_firma_id = ?   AND \tuserlog_eingeloggt = 'J' ORDER BY UserId ";
        }

        public final String GET_INACTIVE_USERS() {
            return "SELECT user_id UserId,        user_name UserName, \t\t\t\tuserlog_lastlogin LastLogin, \t\t\t\tuserlog_anzahl_logins AnzahlLogins,        user_default_filiale_id DefaultFiliale FROM \tw_user_log@etk_nutzer \t\t\t\tRIGHT OUTER JOIN w_user@etk_nutzer \t\t\t\tON (user_firma_id = userlog_firma_id \t\t\t\t\tAND user_id = userlog_user_id) WHERE  user_firma_id = ?   AND  user_id NOT IN (&IGNORE_USER&)   AND  (userlog_lastlogin IS NULL    OR   userlog_lastlogin < ?) ORDER BY UserId ";
        }
    }

    public static final class admintool
    extends SQLStatements.admintool {
        public static final PriceField[] PRICE_FIELD = new PriceField[]{new PriceField("&preise_sachnr&", true), new PriceField("&preise_evpreis&", false), new PriceField("&preise_nachbelastung&", false), new PriceField("&preise_rabattschluessel&", true), new PriceField("&preise_preisaenderung&", true), new PriceField("&preise_preis_kz&", true), new PriceField("&preise_sonderpreis&", false), new PriceField("&preise_sonderpreis_kz&", true), new PriceField("&preise_mwst&", false), new PriceField("&preise_mwst_code&", true), new PriceField("&preise_zolltarifnr&", true), new PriceField("&preise_nettopreis&", false)};

        public final String LOAD_FIRMEN() {
            return "select firma_id Id from w_firma order by firma_bezeichnung";
        }

        public final String DELETE_PRICES() {
            return "delete from w_preise";
        }

        public final String DELETE_PRICES_BY_FIRMA() {
            return "delete from w_preise where preise_firma = '&FIRMA&'";
        }

        public final String ERMITTLE_NUTZERTABELLEN() {
            return "SELECT tname tabelle FROM systable WHERE tname NOT LIKE 'sys%' AND tname NOT LIKE '%id_seq%'";
        }

        public final String ERMITTLE_NUTZERSEQUENZEN() {
            return "SELECT tname sequenz FROM systable WHERE tname NOT LIKE 'sys%' AND tname LIKE '%id_seq%'";
        }

        public final String DROP_SEQUENCE() {
            return "drop sequence &SEQUENCE&";
        }

        public final String DROP_TABLE() {
            return "drop table &TABLE&";
        }

        public final String GET_COUNT_FIRMA_PREISE() {
            return "select count(preise_firma) cnt from w_preise where preise_firma = '&FIRMA&'";
        }

        public final String GET_DISTINCT_FIRMA_PREISE() {
            return "select distinct preise_firma FirmaId from w_preise";
        }

        public final String LOCK_TABLE() {
            return "lock &TABLE& &MODE&";
        }

        public final String UNLOCK_TABLE() {
            return "unlock &TABLE&";
        }

        public final String INSERT_PRICES() {
            return "INSERT INTO w_preise (preise_firma,preise_sachnr,preise_evpreis,preise_nachbelastung,preise_rabattschluessel,preise_preisaenderung,preise_preis_kz,preise_sonderpreis,preise_sonderpreis_kz,preise_mwst,preise_mwst_code,preise_zolltarifnr,preise_nettopreis) VALUES (&preise_firma&,&preise_sachnr&,&preise_evpreis&,&preise_nachbelastung&,&preise_rabattschluessel&,&preise_preisaenderung&,&preise_preis_kz&,&preise_sonderpreis&,&preise_sonderpreis_kz&,&preise_mwst&,&preise_mwst_code&,&preise_zolltarifnr&,&preise_nettopreis&)";
        }

        public String PRICE_FIELD_getName(int pInt) {
            return admintool.PRICE_FIELD[pInt].name;
        }

        public boolean PRICE_FIELD_getIsQuoted(int pInt) {
            return admintool.PRICE_FIELD[pInt].isQuoted;
        }

        public final String UPDATE_PRICES() {
            return "UPDATE w_preise SET preise_preis_kz = &preise_preis_kz&,preise_evpreis = &preise_evpreis&,preise_nachbelastung = &preise_nachbelastung&,preise_rabattschluessel = &preise_rabattschluessel&,preise_preisaenderung = &preise_preisaenderung&,preise_sonderpreis = &preise_sonderpreis&,preise_sonderpreis_kz = &preise_sonderpreis_kz&,preise_mwst = &preise_mwst&,preise_mwst_code = &preise_mwst_code&,preise_zolltarifnr = &preise_zolltarifnr&,preise_nettopreis = &preise_nettopreis& WHERE preise_firma = &preise_firma& and preise_sachnr = &preise_sachnr&";
        }

        public final String LOAD_SPRACHEN() {
            return "select ben_iso ISO, ben_regiso RegISO, ben_text Benennung from w_ben_gk@etk_publ, w_publben@etk_publ where publben_art = 'S' and ben_textcode = publben_textcode and ben_iso = substr(publben_bezeichnung, 1, 2)  and ben_regiso = substr(publben_bezeichnung, 3, 2)";
        }

        public final String LOAD_DBVERSIONSINFO() {
            return "select verwaltung_info Info, verwaltung_wert Wert from w_verwaltung";
        }

        public static class PriceField {
            protected String name;
            protected boolean isQuoted;

            protected PriceField(String theName, boolean quote) {
                this.name = theName;
                this.isQuoted = quote;
            }
        }
    }

    public static final class Infotool
    extends SQLStatements.Infotool {
        public final String INSERT_USER_TIPP() {
            return "insert into w_user_tipps@etk_nutzer (usert_firma_id, usert_id, usert_tipp_id) table &TABELLE&";
        }

        public final String DELETE_USER_TIPP() {
            return "delete from w_user_tipps@etk_nutzer where usert_firma_id = ?   and usert_id = ?";
        }

        public final String LOAD_TIPPS_TRICKS() {
            return "select tipp_id Id, tipp_filename Filename, tipp_art Art, DECODE(usert_tipp_id, usert_tipp_id, 'J', 'N') Gelesen  from w_tipp@etk_nutzer     left join w_user_tipps@etk_nutzer on (usert_firma_id = '&FIRMAID&'  and usert_id ='&USERID&' and usert_tipp_id = tipp_id) where tipp_id > 0 and tipp_wichtig = '&WICHTIG&' order by tipp_pos";
        }

        public final String COUNT_TIPPS_TRICKS() {
            return "select Count(*) Anzahl from w_tipp@etk_nutzer where tipp_id > 0";
        }

        public final String COUNT_TICKER() {
            return "select Count(*) Anzahl from w_tipp@etk_nutzer where tipp_id < 0";
        }
    }

    public static final class News
    extends SQLStatements.News {
        public final String INSERT_NEWSTEXT() {
            return "insert into w_news_text (NEWST_MARKE_TPS, NEWST_ISO, NEWST_REGISO, NEWST_TEXT, NEWST_AKTUELL, NEWST_STANDARD) values ('&MARKE&','&ISO&','&REGISO&','&TEXT&','&AKTUELL&','&STANDARD&')";
        }

        public final String UPDATE_NEWSTEXT() {
            return "update w_news_text set NEWST_TEXT = '&TEXT&', NEWST_AKTUELL = '&AKTUELL&', NEWST_STANDARD = '&STANDARD&' where NEWST_MARKE_TPS = '&MARKE&' and NEWST_ISO = '&ISO&' and NEWST_REGISO = '&REGISO&'";
        }

        public final String LOAD_NEWSTEXTE() {
            return "select NEWST_TEXT Text, NEWST_AKTUELL IsAktiviert, NEWST_STANDARD IsStandard from w_news_text where NEWST_MARKE_TPS = '&MARKE&' and NEWST_ISO = '&ISO&' and NEWST_REGISO = '&REGISO&' order by NEWST_MARKE_TPS, NEWST_ISO, NEWST_REGISO, NEWST_STANDARD";
        }

        public final String DELETE_NEWSTEXT() {
            return "delete from w_news_text where NEWST_MARKE_TPS = '&MARKE&' and NEWST_ISO = '&ISO&' and NEWST_REGISO = '&REGISO&' and NEWST_TEXT = '&TEXT&'";
        }

        public final String UPDATE_NEWSTEXT_AKTIVIERUNG() {
            return "update w_news_text set NEWST_AKTUELL = '&AKTUELL&' where NEWST_MARKE_TPS = '&MARKE&' and NEWST_ISO = '&ISO&' and NEWST_REGISO = '&REGISO&' and NEWST_TEXT = '&TEXT&'";
        }

        public final String DELETE_IMAGE() {
            return "delete from w_news_grafik where newsg_marke_tps = '&MARKE&'";
        }

        public final String INSERT_IMAGE() {
            return "insert into w_news_grafik values ( '&MARKE&', EMPTY_BLOB() )";
        }

        public final String LOAD_IMAGE() {
            return "select newsg_grafik Grafik from w_news_grafik where newsg_marke_tps = '&MARKE&'";
        }

        public final String UPDATE_IMAGE() {
            return "select newsg_grafik from w_news_grafik where newsg_marke_tps = '&MARKE&' for update";
        }
    }

    public static final class Firmenkonfiguration
    extends SQLStatements.Firmenkonfiguration {
        public final String RETRIEVE_FIRMEN() {
            return "select firma_id Id, firma_bezeichnung Bezeichnung from w_firma@etk_nutzer order by Bezeichnung";
        }

        public final String RETRIEVE_FILIALEN() {
            return "select filiale_id Id, filiale_bezeichnung Bezeichnung from w_filiale@etk_nutzer where filiale_firma_id = '&FIRMA&' order by Bezeichnung";
        }

        public final String RETRIEVE_COUNT_FILIALEN_IN_FIRMA() {
            return "select Count(*) countFiliale from w_filiale@etk_nutzer where filiale_firma_id = '&FIRMA&'";
        }

        public final String RETRIEVE_INFO_FILIALE() {
            return "select firma_id FirmaId,  firma_bezeichnung FirmaBezeichnung,  filiale_id FilialeId,  filiale_bezeichnung FilialeBezeichnung,  filiale_iso SpracheISO,  filiale_regiso SpracheRegISO from w_firma@etk_nutzer, w_filiale@etk_nutzer where filiale_firma_id = '&FIRMA&' and filiale_id = '&FILIALE&' and firma_id = filiale_firma_id";
        }

        public final String RETRIEVE_EXIST_USER() {
            return "select count(user_id) ANZ from w_user@etk_nutzer where user_firma_id = '&FIRMA&' and user_id = '&KENNUNG&' and user_passwort = '&PASSWORT&'";
        }

        public final String RETRIEVE_DMS_VERWENDEN() {
            return "select konfig_hs_verwenden DMSVerwenden from w_konfig@etk_nutzer where konfig_firma_id = '&FIRMA&' and konfig_filiale_id = '&FILIALE&'";
        }

        public final String LOAD_KONFIGURATION() {
            return "select konfig_hd_firma Firma, konfig_hd_zusatz Zusatz, konfig_hd_strasse Strasse, konfig_hd_plz Plz, konfig_hd_ort Ort, konfig_hd_telefon Telefon, konfig_hdnr_pkw Pkw, konfig_hdnr_motorrad Motorrad, konfig_mwst_niedrig MwstNiedrig, konfig_mwst_hoch MwstHoch, konfig_mwst_altteile MwstAltteile, konfig_mwst_3 Mwst3, konfig_mwst_4 Mwst4, konfig_rechnungnr RechnungsNr, konfig_mailserver Mailserver, konfig_barverkaufnr BarverkaufsNr, konfig_auftragnr AuftragsNr, konfig_kundennr KundenNr, konfig_hs_verwenden Verwenden, konfig_abwicklung Abwicklung, konfig_bestandfiliale Bestandfiliale, konfig_datenabgleich Datenabgleich from w_konfig@etk_nutzer where konfig_firma_id = '&FIRMA&' and konfig_filiale_id = '&FILIALE&'";
        }

        public final String DELETE_KONFIGURATION() {
            return "delete from w_konfig@etk_nutzer where konfig_firma_id = '&FIRMA&' and konfig_filiale_id = '&FILIALE&'";
        }

        public final String DELETE_ZUB_KONFIGURATION() {
            return "delete from w_zub_konfig@etk_nutzer where konfigz_firma_id = '&FIRMA&' and konfigz_filiale_id = '&FILIALE&'";
        }

        public final String INSERT_KONFIGURATION() {
            return "insert into w_konfig@etk_nutzer (konfig_firma_id, konfig_filiale_id, konfig_hd_firma, konfig_hd_zusatz, konfig_hd_strasse, konfig_hd_plz, konfig_hd_ort, konfig_hd_telefon, konfig_hdnr_pkw, konfig_hdnr_motorrad, konfig_mwst_niedrig, konfig_mwst_hoch, konfig_mwst_altteile, konfig_mwst_3, konfig_mwst_4, konfig_rechnungnr, konfig_mailserver, konfig_barverkaufnr, konfig_auftragnr, konfig_kundennr, konfig_hs_verwenden, konfig_abwicklung, konfig_bestandfiliale, konfig_datenabgleich) values ('&FIRMA&', '&FILIALE&', &FIRMENNAME&, &ZUSATZ&, &STRASSE&, &PLZ&, &ORT&, &TELEFON&, &PKW&, &MOTOTRRAD&, &MWST_NIEDRIG&, &MWST_HOCH&, &MWST_ALTTEILE&, &MWST_3&, &MWST_4&, &RECHNUNGNR&, &MAILSERVER&, &BARVERKAUFNR&, &AUFTRAGSNR&, &KUNDENNR&, '&HS_VERWENDEN&', &ABWICKLUNG&, &BESTAND_FILIALE&, &DATENABGLEICH&)";
        }

        public final String UPDATE_KONFIGURATION() {
            return "update w_konfig@etk_nutzer set konfig_hd_firma = &FIRMENNAME&, konfig_hd_zusatz = &ZUSATZ&, konfig_hd_strasse = &STRASSE&, konfig_hd_plz = &PLZ&, konfig_hd_ort = &ORT&, konfig_hd_telefon = &TELEFON&, konfig_hdnr_pkw = &PKW&, konfig_hdnr_motorrad = &MOTOTRRAD&, konfig_mwst_niedrig = &MWST_NIEDRIG&, konfig_mwst_hoch = &MWST_HOCH&, konfig_mwst_altteile = &MWST_ALTTEILE&, konfig_mwst_3 = &MWST_3&, konfig_mwst_4 = &MWST_4&, konfig_rechnungnr = &RECHNUNGNR&, konfig_mailserver = &MAILSERVER&, konfig_barverkaufnr = &BARVERKAUFNR&, konfig_auftragnr = &AUFTRAGSNR&, konfig_kundennr = &KUNDENNR&, konfig_hs_verwenden = '&HS_VERWENDEN&', konfig_abwicklung = &ABWICKLUNG&, konfig_bestandfiliale = &BESTAND_FILIALE&, konfig_datenabgleich = &DATENABGLEICH& where konfig_firma_id = '&FIRMA&' and konfig_filiale_id = '&FILIALE&'";
        }

        public final String SELECT_RECHNUNGSNUMMER_FOR_UPDATE() {
            return "select konfig_rechnungnr RechnungsNr from w_konfig@etk_nutzer where konfig_firma_id = '&FIRMA&' and konfig_filiale_id = '&FILIALE&' for update";
        }

        public final String UPDATE_RECHNUNGSNUMMER() {
            return "update w_konfig@etk_nutzer set konfig_rechnungnr = &RECHNUNGSNR& where konfig_firma_id = '&FIRMA&' and konfig_filiale_id = '&FILIALE&'";
        }

        public final String UPDATE_FIRMENBEZEICHNUNG() {
            return "update w_firma@etk_nutzer set firma_bezeichnung = '&BEZEICHNUNG&' where firma_id = '&ID&'";
        }

        public final String INSERT_FILIALE() {
            return "insert into w_filiale@etk_nutzer (filiale_firma_id, filiale_id, filiale_bezeichnung, filiale_iso, filiale_regiso) values ('&FIRMAID&', '&FILIALID&', '&FILIALE&', '&ISO&', '&REGISO&')";
        }

        public final String DELETE_FILIALE() {
            return "delete from w_filiale@etk_nutzer where filiale_firma_id = '&FIRMAID&' and filiale_id = '&FILIALID&'";
        }

        public final String UPDATE_FILIALE() {
            return "update w_filiale@etk_nutzer set filiale_bezeichnung = '&FILIALE&', filiale_iso = '&ISO&', filiale_regiso = '&REGISO&' where filiale_firma_id = '&FIRMAID&' and filiale_id = '&FILIALID&'";
        }

        public final String RETRIEVE_FILIALEN_SPRACHEN() {
            return "select filiale_id Id, filiale_bezeichnung Bezeichnung, filiale_iso Iso, filiale_regiso RegIso from w_filiale@etk_nutzer where filiale_firma_id = '&FIRMA&' order by filiale_id";
        }

        public final String RETRIEVE_ANZAHL_NUTZER() {
            return "SELECT count(user_id) NutzerAnzahl FROM   w_user@etk_nutzer WHERE  user_firma_id = '&FIRMA&'   AND  user_id NOT IN (&IGNORE_USER&)   AND ( user_id  LIKE INSENSITIVE '&KRIT&'    OR user_name  LIKE INSENSITIVE '&KRIT&' )";
        }

        public final String RETRIEVE_MATCHING_NUTZER() {
            return "SELECT user_id NutzerId,        user_name NutzerName FROM   w_user@etk_nutzer WHERE  user_firma_id = '&FIRMA&'   AND  user_id NOT IN (&IGNORE_USER&)   AND ( user_id  LIKE INSENSITIVE '&KRIT&'    OR user_name  LIKE INSENSITIVE '&KRIT&' ) ORDER BY user_name ";
        }

        public final String RETRIEVE_NUTZER() {
            return "select user_id NutzerId, user_name NutzerName, user_passwort Passwort,  user_default_filiale_id DefaultFiliale,  user_bearbeiternummer BearbeiterNummer from w_user@etk_nutzer where user_firma_id = '&FIRMA&' order by user_id";
        }

        public final String RETRIEVE_SINGLE_NUTZER() {
            return "select user_name NutzerName,  user_passwort Passwort,  user_default_filiale_id DefaultFiliale,  user_bearbeiternummer BearbeiterNummer  from w_user@etk_nutzer where user_firma_id = '&FIRMA&' and user_id = '&NUTZERID&'";
        }

        public final String RETRIEVE_BERECHTIGUNEN() {
            return "select firmab_art Art, firmab_wert Wert from w_firma_berechtigungen@etk_nutzer where firmab_firma_id = '&FIRMA&'";
        }

        public final String RETRIEVE_SPRACHEN() {
            return "select publben_bezeichnung Code, ben_text Benennung from w_publben, w_ben_gk where publben_art = 'S' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'";
        }

        public final String RETRIEVE_FUNKTIONSRECHTE() {
            return "select publben_bezeichnung Bezeichnung, ben_text Text from w_publben, w_ben_gk where publben_art = 'R' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by ben_text";
        }

        public final String RETRIEVE_NUTZER_FUNKTIONSRECHTE() {
            return "select publben_bezeichnung Bezeichnung, ben_text Text from w_user_funktionsrechte@etk_nutzer, w_ben_gk, w_publben  where userf_firma_id = '&FIRMA_ID&' and userf_id = '&USER_ID&' and publben_art = 'R'  and publben_bezeichnung = userf_recht_id  and ben_textcode = publben_textcode  and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by publben_bezeichnung";
        }

        public final String RETRIEVE_NUTZER_BERECHTIGUNGEN() {
            return "select userb_art Art, userb_wert Wert from w_user_berechtigungen@etk_nutzer where userb_id = '&USER&' and userb_firma_id = '&FIRMA&'";
        }

        public final String DELETE_NUTZER_BERECHTIGUNGEN() {
            return "delete from w_user_berechtigungen@etk_nutzer where userb_id = '&ID&' and userb_firma_id = '&FIRMA&'";
        }

        public final String DELETE_NUTZER_FUNKTIONSRECHTE() {
            return "delete from w_user_funktionsrechte@etk_nutzer where userf_id = '&ID&' and userf_firma_id = '&FIRMA&'";
        }

        public final String DELETE_NUTZER_EINSTELLUNGEN() {
            return "delete from w_user_einstellungen@etk_nutzer where user_id = '&ID&' and user_firma_id = '&FIRMA&'";
        }

        public final String DELETE_ZUB_NUTZER() {
            return "delete from w_zub_user@etk_nutzer where userz_id = '&ID&' and userz_firma_id = '&FIRMA&'";
        }

        public final String DELETE_NUTZER_EINSTELLUNGEN_REGION() {
            return "delete from w_user_einstellungen_region@etk_nutzer where user_id = '&ID&' and user_firma_id = '&FIRMA&'";
        }

        public final String DELETE_NUTZER() {
            return "delete from w_user@etk_nutzer where user_id = '&ID&' and user_firma_id= '&FIRMA&'";
        }

        public final String DELETE_NUTZER_TEILELISTEPOS() {
            return "delete from w_teilelistepos@etk_nutzer where teilelistepos_user_id = '&ID&' and teilelistepos_firma_id= '&FIRMA&'";
        }

        public final String DELETE_NUTZER_TEILELISTE() {
            return "delete from w_teileliste@etk_nutzer where teileliste_user_id = '&ID&' and teileliste_firma_id= '&FIRMA&'";
        }

        public final String DELETE_NUTZER_TEILELISTE_SENDEINFO() {
            return "delete from w_teileliste_sendeinfo@etk_nutzer where teilelistesi_user_id = '&ID&' and teilelistesi_firma_id= '&FIRMA&'";
        }

        public final String DELETE_NUTZER_TEILEINFO() {
            return "delete from w_teileinfo@etk_nutzer where teileinfo_user_id = '&ID&' and teileinfo_firma_id= '&FIRMA&'";
        }

        public final String RETRIEVE_EXIST_USER_ID() {
            return "select count(user_id) Cnt from w_user@etk_nutzer where user_firma_id = '&FIRMA&' and LOWER(user_id) = '&KENNUNG&'";
        }

        public final String STORE_NUTZER() {
            return "insert into w_user@etk_nutzer (user_firma_id, user_id, user_name, user_passwort, user_default_filiale_id, user_bearbeiternummer ) values('&FIRMAID&', '&USERID&', '&USERNAME&', '&PASSWORD&', '&FILIALE&', &BEARBEITERNUMMER&)";
        }

        public final String STORE_NUTZER_BERECHTIGUNGEN() {
            return "insert into w_user_berechtigungen@etk_nutzer (userb_firma_id, userb_id, userb_art, userb_wert ) values ('&FIRMAID&', '&USERID&', '&ART&', '&WERT&')";
        }

        public final String STORE_NUTZER_FUNKTIONSRECHTE() {
            return "insert into w_user_funktionsrechte@etk_nutzer (userf_firma_id, userf_id, userf_recht_id) values ('&FIRMAID&', '&USERID&', '&RECHT&')";
        }

        public final String UPDATE_NUTZER() {
            return "UPDATE w_user@etk_nutzer SET    user_name = ?,        user_passwort = ?,        user_default_filiale_id = ?,        user_bearbeiternummer = ? WHERE  user_firma_id = ? AND    user_id = ? ";
        }

        public final String MOVE_TEILELISTEN() {
            return "update w_teileliste@etk_nutzer set teileliste_filiale_id = '&FILIALID_NEU&' where teileliste_firma_id = '&FIRMAID&' and teileliste_filiale_id = '&FILIALID_ALT&' and teileliste_user_id = '&USERID&' and teileliste_id in (select teileliste_id from w_teileliste@etk_nutzer where teileliste_auftragsnr is null and teileliste_firma_id = '&FIRMAID&' and teileliste_filiale_id = '&FILIALID_ALT&' and teileliste_user_id = '&USERID&')";
        }

        public final String MOVE_TEILELISTENPOS() {
            return "update w_teilelistepos@etk_nutzer set teilelistepos_filiale_id = '&FILIALID_NEU&' where teilelistepos_firma_id = '&FIRMAID&' and teilelistepos_filiale_id = '&FILIALID_ALT&' and teilelistepos_user_id = '&USERID&' and teilelistepos_teileliste_id in (select teileliste_id from w_teileliste@etk_nutzer where teileliste_auftragsnr is null and teileliste_firma_id = '&FIRMAID&' and teileliste_filiale_id = '&FILIALID_ALT&' and teileliste_user_id = '&USERID&')";
        }

        public final String MOVE_TEILELISTENSI() {
            return "update w_teileliste_sendeinfo@etk_nutzer set teilelistesi_filiale_id = '&FILIALID_NEU&' where teilelistesi_firma_id = '&FIRMAID&' and teilelistesi_filiale_id = '&FILIALID_ALT&' and teilelistesi_user_id = '&USERID&' and teilelistesi_teileliste_id in (select teileliste_id from w_teileliste@etk_nutzer where teileliste_auftragsnr is null and teileliste_firma_id = '&FIRMAID&' and teileliste_filiale_id = '&FILIALID_ALT&' and teileliste_user_id = '&USERID&')";
        }

        public final String RETRIEVE_USERINFO() {
            return "select user_id UserId, user_passwort Passwort,  user_default_filiale_id DefaultFiliale,  user_bearbeiternummer BearbeiterNummer,  filiale_bezeichnung FilialBezeichnung, filiale_iso Iso,  filiale_regiso RegIso from w_user@etk_nutzer, w_filiale@etk_nutzer where user_firma_id = '&FIRMA&'  and user_id = '&USER&'  and user_default_filiale_id = filiale_id and user_firma_id = filiale_firma_id";
        }

        public final String UPDATE_DEFAULT_FILIALE() {
            return "update w_user@etk_nutzer set user_default_filiale_id='&FILIALID&' where user_firma_id='&FIRMAID&' and user_id='&USERID&'";
        }

        public final String RETRIEVE_EINEFIRMAFILIALENUTZER() {
            return "select firma_id FirmaId, filiale_id FilialId, user_id UserId from w_firma@etk_nutzer left join w_filiale@etk_nutzer on (filiale_firma_id = firma_id) left join w_user@etk_nutzer on (user_firma_id = firma_id)";
        }

        public final String RETRIEVE_USER_BY_DEFAULT_FILIALE() {
            return "select user_id NutzerId, user_name NutzerName, user_passwort Passwort, user_bearbeiternummer BearbeiterNummer from w_user@etk_nutzer where user_default_filiale_id='&FILIALID&' and user_firma_id='&FIRMAID&' order by user_name";
        }

        public final String RETRIEVE_BESTELLLISTE_POSITIONEN() {
            return "select * from w_bestelllistepos@etk_nutzer where bestelllistepos_firma_id='&FIRMAID&' and bestelllistepos_filiale_id='&FILIALID&'";
        }

        public final String RETRIEVE_TEILELISTE_POSITIONEN() {
            return "select * from w_teilelistepos@etk_nutzer where teilelistepos_firma_id='&FIRMAID&' and teilelistepos_filiale_id='&FILIALID&'";
        }

        public final String DELETE_ALL_AUFTRAEGE() {
            return " delete from w_auftrag@etk_nutzer where auftrag_firma_id='&FIRMAID&' and auftrag_filiale_id='&FILIALID&'";
        }

        public final String DELETE_ALL_TEILELISTEN() {
            return " delete from w_teileliste@etk_nutzer where teileliste_firma_id='&FIRMAID&' and teileliste_filiale_id='&FILIALID&'";
        }

        public final String DELETE_ALL_TEILELISTEN_POSITIONEN() {
            return " delete from w_teilelistepos@etk_nutzer where teilelistepos_firma_id='&FIRMAID&' and teilelistepos_filiale_id='&FILIALID&'";
        }

        public final String DELETE_ALL_TEILELISTEN_SI() {
            return " delete from w_teileliste_sendeinfo@etk_nutzer where teilelistesi_firma_id='&FIRMAID&' and teilelistesi_filiale_id='&FILIALID&'";
        }

        public final String DELETE_ALL_BESTELLLISTEN() {
            return " delete from w_bestellliste@etk_nutzer where bestellliste_firma_id='&FIRMAID&' and bestellliste_filiale_id='&FILIALID&'";
        }

        public final String DELETE_ALL_BESTELLLISTEN_POSITIONEN() {
            return " delete from w_bestelllistepos@etk_nutzer where bestelllistepos_firma_id='&FIRMAID&' and bestelllistepos_filiale_id='&FILIALID&'";
        }

        public final String EXISTIERT_FILIAL_ZUB() {
            return " select count(*) from w_zub_konfig@etk_nutzer where konfigz_firma_id = ?  and konfigz_filiale_id = ?  and konfigz_default_markt_id is not null";
        }

        public final String UPDATE_MARKTID_NUTZER_VON_FILIALE() {
            return " update w_user@etk_nutzer set user_marktid = ( select konfigz_default_markt_id from w_zub_konfig@etk_nutzer  where konfigz_filiale_id = ? and konfigz_firma_id = user_firma_id)  where user_firma_id = ? and user_id = ?";
        }
    }

    public static final class Hilfe
    extends SQLStatements.Hilfe {
        public final String RETRIEVE_ABKUERZUNGEN() {
            return "select abk_kuerzel ABKUERZUNG, abk_bedeutung BEDEUTUNG, ben_text UEBERSETZUNG from w_abk, w_ben_gk where abk_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by ABKUERZUNG";
        }

        public final String RETRIEVE_VERSIONSINFO() {
            return "select verwaltung_info Info, verwaltung_wert Wert from w_verwaltung";
        }

        public final String RETRIEVE_BEDEUTUNGEN() {
            return "select bedeutung_wert WERT, ben_text BEDEUTUNG, komm_pos POS from w_bedeutung, w_ben_gk, w_komm where bedeutung_art = '&ART&' and bedeutung_kommid = komm_id and komm_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by WERT, POS";
        }

        public final String RETRIEVE_SALAPAS() {
            return "select bedsala_art || bedsala_pnr || bedsala_hz Code, ben_text Benennung from w_bed_sala, w_ben_gk, w_bed where bedsala_art IN (&ARTEN&) and bedsala_id = bed_elemid and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Code";
        }
    }

    public static final class TeilevwdgBen
    extends SQLStatements.TeilevwdgBen {
        public final String RETRIEVE_BAUREIHEN() {
            return "select distinct baureihe_baureihe BAUREIHE, benbr.ben_text EXT_BAUREIHE, baureihe_position POS from w_baureihe, w_fztyp, w_bildtaf_suche, w_bildtaf, w_ben_gk H, w_ben_gk benbr where &SUCHSTRING& and H.ben_iso = '&ISO&' and H.ben_regiso = '&REGISO&' and bildtaf_textc = H.ben_textcode and bildtafs_btnr = bildtaf_btnr and bildtafs_hg = bildtaf_hg and fztyp_mospid = bildtafs_mospid and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_sichtschutz = 'N' and fztyp_ktlgausf IN (&REGIONEN&) and baureihe_baureihe = fztyp_baureihe and baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and benbr.ben_textcode = baureihe_textcode and benbr.ben_iso = '&ISO&' and benbr.ben_regiso = '&REGISO&' order by POS";
        }

        public final String RETRIEVE_BAUREIHEN_SONDERLOCKE() {
            return "select distinct baureihe_baureihe BAUREIHE, benbr.ben_text EXT_BAUREIHE, baureihe_position POS from w_baureihe, w_fztyp, w_bildtaf_suche, w_bildtaf, w_ben_gk H, w_ben_gk benbr where (&SUCHSTRING1& or &SUCHSTRING2&) and H.ben_iso = '&ISO&' and H.ben_regiso = '&REGISO&' and bildtaf_textc = H.ben_textcode and bildtafs_btnr = bildtaf_btnr and bildtafs_hg = bildtaf_hg and fztyp_mospid = bildtafs_mospid and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_sichtschutz = 'N' and fztyp_ktlgausf IN (&REGIONEN&) and baureihe_baureihe = fztyp_baureihe and baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and benbr.ben_textcode = baureihe_textcode and benbr.ben_iso = '&ISO&' and benbr.ben_regiso = '&REGISO&' order by POS";
        }

        public final String RETRIEVE_BAUREIHEN_TR() {
            return "select distinct baureihe_baureihe BAUREIHE, ben_text EXT_BAUREIHE, baureihe_position POS from w_baureihe inner join w_ben_gk on (ben_textcode = baureihe_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&')\twhere baureihe_baureihe IN (select distinct fztyp_baureihe from w_ben_gk H\tinner join w_bildtaf on (bildtaf_textc = ben_textcode and bildtaf_vbereich = '&KATALOGUMFANG&' and bildtaf_produktart = '&PRODUKTART&')\tinner join w_bildtaf_marke on (bildtafm_btnr = bildtaf_btnr and bildtafm_marke_tps = '&MARKE&') inner join w_bildtaf_suche on (bildtafs_hg = bildtaf_hg and bildtafs_btnr = bildtaf_btnr) inner join w_fztyp on (fztyp_mospid = bildtafs_mospid\tand fztyp_vbereich = '&KATALOGUMFANG&'\tand fztyp_sichtschutz = 'N'\tand fztyp_ktlgausf IN (&REGIONEN&)) where ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and (&SUCHSTRING&) order by POS";
        }

        public final String RETRIEVE_MODELLSPALTEN() {
            return "select distinct bildtaf_btnr BTNR, B.ben_text BTUEBERSCHRIFT, fztyp_erwvbez MODELL, K.ben_text KAROSSERIE, fztyp_karosserie KAROSSERIE_ID, baureihe_bauart BAUART, fztyp_ktlgausf REGION, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso from w_ben_gk H, w_bildtaf_marke, w_bildtaf_suche,      w_fztyp, w_baureihe, w_ben_gk K, w_publben, w_ben_gk B, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where H.ben_iso = '&ISO&' and H.ben_regiso = '&REGISO&' and &SUCHSTRING& and H.ben_textcode = bildtaf_textc and bildtaf_vbereich = '&KATALOGUMFANG&' and bildtaf_produktart = '&PRODUKTART&' and bildtafm_btnr = bildtaf_btnr and bildtafm_marke_tps = '&MARKE&' and bildtaf_hg = bildtafs_hg and bildtaf_btnr = bildtafs_btnr and bildtafs_mospid = fztyp_mospid and fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_sichtschutz = 'N' and fztyp_ktlgausf IN (&REGIONEN&) and fztyp_baureihe = baureihe_baureihe and fztyp_karosserie = publben_bezeichnung and publben_art = 'K' and publben_textcode = K.ben_textcode and K.ben_iso = '&ISO&' and K.ben_regiso = '&REGISO&' and bildtaf_textc = B.ben_textcode and B.ben_iso = '&ISO&' and B.ben_regiso = '&REGISO&' &MODELLSPALTEN_STMT&  order by MODELL, BTNR, KAROSSERIE, REGION";
        }

        public final String RETRIEVE_MODELLSPALTEN_SONDERLOCKE() {
            return "select distinct bildtaf_btnr BTNR, B.ben_text BTUEBERSCHRIFT, fztyp_erwvbez MODELL, K.ben_text KAROSSERIE, fztyp_karosserie KAROSSERIE_ID, baureihe_bauart BAUART, fztyp_ktlgausf REGION, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso from w_ben_gk H, w_bildtaf_suche, w_bildtaf_marke,      w_fztyp, w_baureihe, w_ben_gk K, w_publben, w_ben_gk B, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where H.ben_iso = '&ISO&' and H.ben_regiso = '&REGISO&' and (&SUCHSTRING1& or &SUCHSTRING2&) and H.ben_textcode = bildtaf_textc and bildtaf_vbereich = '&KATALOGUMFANG&' and bildtaf_produktart = '&PRODUKTART&' and bildtafm_btnr = bildtaf_btnr and bildtafm_marke_tps = '&MARKE&' and bildtaf_hg = bildtafs_hg and bildtaf_btnr = bildtafs_btnr and bildtafs_mospid = fztyp_mospid and fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_sichtschutz = 'N' and fztyp_ktlgausf IN (&REGIONEN&) and fztyp_baureihe = baureihe_baureihe and fztyp_karosserie = publben_bezeichnung and publben_art = 'K' and publben_textcode = K.ben_textcode and K.ben_iso = '&ISO&' and K.ben_regiso = '&REGISO&' and bildtaf_textc = B.ben_textcode and B.ben_iso = '&ISO&' and B.ben_regiso = '&REGISO&' &MODELLSPALTEN_STMT&  order by MODELL, BTNR, KAROSSERIE, REGION";
        }

        public final String MODELLSPALTEN_STMT() {
            return " and bildtafs_mospid IN (&MOSPIDS&)";
        }

        public final String RETRIEVE_BAUREIHEN_TNR() {
            return "select distinct baureihe_baureihe BAUREIHE, benbr.ben_text EXT_BAUREIHE, baureihe_position POS from w_baureihe, w_fztyp, w_ben_gk benbr, w_btzeilen_verbauung where btzeilenv_sachnr IN (&SACHNUMMERN&) and fztyp_mospid = btzeilenv_mospid and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_sichtschutz = 'N' and fztyp_ktlgausf IN (&REGIONEN&) and baureihe_baureihe = fztyp_baureihe and baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and benbr.ben_textcode = baureihe_textcode and benbr.ben_iso = '&ISO&' and benbr.ben_regiso = '&REGISO&' and btzeilenv_alter_kz is null order by POS";
        }

        public final String RETRIEVE_MODELLSPALTEN_TNR() {
            return "select distinct btzeilenv_vmenge MENGE, bildtaf_btnr BTNR, B.ben_text BTUEBERSCHRIFT, fztyp_erwvbez MODELL, K.ben_text KAROSSERIE, fztyp_karosserie KAROSSERIE_ID, baureihe_bauart BAUART, fztyp_ktlgausf REGION, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso from w_bildtaf_marke, w_bildtaf_suche, w_fztyp,       w_baureihe, w_ben_gk K, w_publben, w_ben_gk B, w_btzeilen_verbauung, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where btzeilenv_sachnr IN (&SACHNUMMERN&) and btzeilenv_btnr = bildtaf_btnr and bildtaf_vbereich = '&KATALOGUMFANG&' and bildtaf_produktart = '&PRODUKTART&' and bildtafm_btnr = bildtaf_btnr and bildtafm_marke_tps = '&MARKE&' and bildtaf_hg = bildtafs_hg and bildtaf_btnr = bildtafs_btnr and bildtafs_mospid = fztyp_mospid and fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_sichtschutz = 'N' and fztyp_ktlgausf IN (&REGIONEN&) and fztyp_baureihe = baureihe_baureihe and fztyp_karosserie = publben_bezeichnung and publben_art = 'K' and publben_textcode = K.ben_textcode and K.ben_iso = '&ISO&' and K.ben_regiso = '&REGISO&' and bildtaf_textc = B.ben_textcode and B.ben_iso = '&ISO&' and B.ben_regiso = '&REGISO&' and bildtaf_btnr = btzeilenv_btnr and bildtafs_mospid = btzeilenv_mospid and btzeilenv_alter_kz is null order by MODELL, BTNR, KAROSSERIE, REGION";
        }

        public final String SEARCH_SNR_TVBENENNUNG() {
            return "select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilen_verbauung, w_teil_marken, w_teil  inner join  w_ben_gk ben_teil on (ben_textcode = teil_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&' and &SUCHSTRING&) left join w_ben_gk ben_komm on (ben_komm.ben_textcode = teil_textcode_kom  and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where teil_sachnr = teilm_sachnr and teilm_marke_tps = '&MARKE&' and teil_verbaubar = 'J' and teil_produktart in ('&PRODUKTART&', 'B') and btzeilenv_sachnr = teil_sachnr and btzeilenv_alter_kz is null union select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilenugb, w_teil_marken, w_teil inner join  w_ben_gk ben_teil on (ben_textcode = teil_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&' and &SUCHSTRING&) left join w_ben_gk ben_komm on (ben_komm.ben_textcode = teil_textcode_kom and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where teil_sachnr = teilm_sachnr and teilm_marke_tps = '&MARKE&' and teil_verbaubar = 'J' and teil_produktart in ('&PRODUKTART&', 'B') and btzeilenu_sachnr = teil_sachnr order by Benennung, Hauptgruppe, Untergruppe, Sachnummer";
        }

        public final String SEARCH_SNR_TVBENENNUNG_SONDERLOCKE() {
            return "select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz,  ben_komm.ben_text BenennungKommentar,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilen_verbauung, w_teil_marken, w_teil  inner join  w_ben_gk ben_teil on (ben_textcode = teil_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&' and (&SUCHSTRING1& or &SUCHSTRING2&)) left join w_ben_gk ben_komm on (ben_komm.ben_textcode = teil_textcode_kom  and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where teil_sachnr = teilm_sachnr and teilm_marke_tps = '&MARKE&' and teil_verbaubar = 'J' and teil_produktart in ('&PRODUKTART&', 'B') and btzeilenv_sachnr = teil_sachnr and btzeilenv_alter_kz is null  union select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilenugb, w_teil_marken, w_teil inner join  w_ben_gk ben_teil on (ben_textcode = teil_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&' and (&SUCHSTRING1& or &SUCHSTRING2&)) left join w_ben_gk ben_komm on (ben_komm.ben_textcode = teil_textcode_kom and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where teil_sachnr = teilm_sachnr and teilm_marke_tps = '&MARKE&' and teil_verbaubar = 'J' and teil_produktart in ('&PRODUKTART&', 'B') and btzeilenu_sachnr = teil_sachnr order by Benennung, Hauptgruppe, Untergruppe, Sachnummer";
        }

        public final String SEARCH_BT_SACHNUMMERN_TVBEN() {
            return "select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos, marktetk_isokz MarktIso from  w_ben_gk, w_bildtaf, w_btzeilen, w_btzeilen_verbauung, w_fztyp, w_baureihe left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where btzeilenv_mospid =  fztyp_mospid and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_sichtschutz = 'N' and fztyp_ktlgausf IN (&REGIONEN&) and fztyp_baureihe = baureihe_baureihe and baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and baureihe_vbereich in ('BE', '&KATALOGUMFANG&') and btzeilenv_sachnr IN (&SACHNUMMERN&) and btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos and btzeilen_btnr = bildtaf_btnr and bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' &UNION& &SEARCH_BT_SACHNUMMERN_ASS& order by Pos";
        }
    }

    public static final class TeilevwdgTeil
    extends SQLStatements.TeilevwdgTeil {
        public final String RETRIEVE_TEIL_ZU_MARKE_PROD() {
            return "select teil_hauptgr HG, teil_untergrup UG, ben_text BEN    , teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_teil, w_teil_marken, w_ben_gk where teil_sachnr = '&SACHNUMMER&' and (teil_produktart = '&PRODUKTART&' or teil_produktart = 'B' or teil_produktart IS NULL) and teilm_sachnr = teil_sachnr and teilm_marke_tps = '&MARKE&' and teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'";
        }

        public final String RETRIEVE_TEIL() {
            return "select teil_hauptgr HG,  teil_untergrup UG,  teilm_marke_tps MARKE,  teil_produktart PRODUKTART,  ben_text BEN,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_teil, w_teil_marken, w_ben_gk  where teil_sachnr = '&SACHNUMMER&' and  teil_textcode = ben_textcode  and ben_iso = '&ISO&'  and ben_regiso = '&REGISO&' and teil_sachnr = teilm_sachnr";
        }

        public final String RETRIEVE_BAUREIHEN() {
            return "select distinct baureihe_baureihe BAUREIHE, ben_text EXT_BAUREIHE, baureihe_position POS from w_btzeilen_verbauung, w_btzeilen, w_ben_gk, w_baureihe, w_fztyp where btzeilenv_sachnr = '&SACHNUMMER&' &MODELLSPALTEN_STMT&  and btzeilen_btnr = btzeilenv_btnr and btzeilen_pos = btzeilenv_pos and btzeilen_bildposnr <> '--' and btzeilenv_mospid = fztyp_mospid and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_sichtschutz = 'N' and fztyp_ktlgausf IN (&REGIONEN&) and fztyp_baureihe = baureihe_baureihe and baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and baureihe_vbereich in ('BE', '&KATALOGUMFANG&') and baureihe_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and btzeilenv_alter_kz is null order by POS";
        }

        public final String RETRIEVE_MODELLSPALTEN() {
            return "select distinct btzeilenv_vmenge MENGE, btzeilenv_btnr BTNR, B.ben_text BTUEBERSCHRIFT, fztyp_erwvbez MODELL, K.ben_text KAROSSERIE, fztyp_karosserie KAROSSERIE_ID, baureihe_bauart BAUART, fztyp_ktlgausf REGION, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso from w_fztyp, w_ben_gk B, w_ben_gk K, w_publben, w_baureihe, w_btzeilen, w_btzeilen_verbauung, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_sichtschutz = 'N' and fztyp_ktlgausf IN (&REGIONEN&) and fztyp_mospid = btzeilenv_mospid &MODELLSPALTEN_STMT&  and btzeilenv_sachnr = '&SACHNUMMER&' and btzeilen_btnr = btzeilenv_btnr and btzeilen_pos = btzeilenv_pos and btzeilen_bildposnr <> '--' and btzeilenv_btnr = bildtaf_btnr and fztyp_baureihe = baureihe_baureihe and fztyp_karosserie = publben_bezeichnung and publben_art = 'K' and publben_textcode = K.ben_textcode and K.ben_iso = '&ISO&' and K.ben_regiso = '&REGISO&' and bildtaf_textc = B.ben_textcode and B.ben_iso = '&ISO&' and B.ben_regiso = '&REGISO&' and btzeilenv_alter_kz is null order by MODELL, BTNR, KAROSSERIE, REGION";
        }

        public final String MODELLSPALTEN_STMT() {
            return " and btzeilenv_mospid IN (&MOSPIDS&)";
        }
    }

    public static final class Federtabelle
    extends SQLStatements.Federtabelle {
        public final String LOAD_SALAS() {
            return "select distinct sftsala_salaid Id, bedsala_art || bedsala_pnr || bedsala_hz Code, ben_text Benennung from w_sfttyp, w_ben_gk, w_bed_sala, w_bed, w_sftsala, w_sft where sfttyp_typ in (&TYPEN&) and sfttyp_sftid = sft_sftid &PRODDATUM_STMT& and sft_sftid = sftsala_sftid and sftsala_salaid = bedsala_id and bedsala_id = bed_elemid and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Code, Benennung";
        }

        public final String LOAD_PUNKTE_TYP() {
            return "select distinct sfttyp_sftid FTId, sfttyp_typ Typ, sfttyp_punkte_va_l GrundpunkteVA_links, sfttyp_punkte_va_r GrundpunkteVA_rechts, sfttyp_punkte_ha_l GrundpunkteHA_links, sfttyp_punkte_ha_r GrundpunkteHA_rechts, sftid_ist_aspg Sftid_Ist_Aspg from w_sfttyp, w_sft where sfttyp_typ in (&TYPEN&) and sfttyp_sftid = sft_sftid &PRODDATUM_STMT& order by FTId, Typ";
        }

        public final String LOAD_PUNKTE_SALA() {
            return "select distinct sftsala_sftid FTId, sftsala_salaid SalaId, sftsala_va_punkte_l PunkteVA_links, sftsala_va_punkte_r PunkteVA_rechts, sftsala_ha_punkte_l PunkteHA_links, sftsala_ha_punkte_r PunkteHA_rechts from w_sfttyp, w_sftsala, w_sft where sfttyp_typ in (&TYPEN&) and sfttyp_sftid = sft_sftid &PRODDATUM_STMT& and sft_sftid = sftsala_sftid and sftsala_salaid in (&SALAIDS&) order by FTId, SalaId";
        }

        public final String SFT_PRODDATUM_YYYYMM() {
            return " and substr(to_char(sft_gilt_v), 1, 6) CAST INTEGER <= &PRODUKTIONSDATUM& and nvl(substr(to_char(sft_gilt_b), 1, 6) CAST INTEGER, 999999) >= &PRODUKTIONSDATUM&";
        }

        public final String SFT_PRODDATUM_YYYYMMDD() {
            return " and sft_gilt_v <= &PRODUKTIONSDATUM& and nvl(sft_gilt_b, 99999999) >= &PRODUKTIONSDATUM&";
        }

        public final String LOAD_FEDERN_LINKS() {
            return "select distinct teil_hauptgr || teil_untergrup || sftfeder_sachnr_l Teilenummer   ,          teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_sftfeder, w_teil where sftfeder_sftid = &FTID& and sftfeder_kz_vh = '&ART&' and sftfeder_punkte_von <= &PUNKTE& and sftfeder_punkte_bis >= &PUNKTE& and teil_sachnr = sftfeder_sachnr_l";
        }

        public final String LOAD_FEDERN_RECHTS() {
            return "select distinct teil_hauptgr || teil_untergrup || sftfeder_sachnr_r Teilenummer   ,          teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_sftfeder, w_teil where sftfeder_sftid = &FTID& and sftfeder_kz_vh = '&ART&' and sftfeder_punkte_von <= &PUNKTE& and sftfeder_punkte_bis >= &PUNKTE& and teil_sachnr = sftfeder_sachnr_r";
        }

        public final String LOAD_ASPG_KIT() {
            return "select teil_hauptgr || teil_untergrup || sftaspg_sachnr_pg Teilenummer, sftaspg_btnr BTNr, sftaspg_mospid MospId, sftaspg_achse Achse, teil_ist_diebstahlrelevant Teil_Diebstahlrelevant from w_sft_aspg, w_teil where sftaspg_sftid = &FTID& and teil_sachnr = sftaspg_sachnr_pg";
        }
    }

    public static final class Fuellmengen
    extends SQLStatements.Fuellmengen {
        public final String RETRIEVE_FUELLMENGEN() {
            return "select fuellmengen_typ Typ, fuellmengen_getriebeben Getriebe, fuellmengen_motor Motor, fuellmengen_fm_getriebe FMGetriebe, fuellmengen_fm_motor FMMotor, fuellmengen_fm_ha FMHinterachse, fuellmengen_fm_km_mitac FMKuehlmittelMitAC, fuellmengen_fm_km_ohneac FMKuehlmittelOhneAC, fuellmengen_fm_bremse FMBremse, fuellmengen_hw_&SPRACHE& Hinweis from w_fuellmengen where fuellmengen_typ in (&TYPEN&) and substr(to_char(fuellmengen_ab), 1, 6) CAST INTEGER <= &PRODUKTIONSDATUM_YYYYMM& and nvl(substr(to_char(fuellmengen_bis), 1, 6) CAST INTEGER, 999999) >= &PRODUKTIONSDATUM_YYYYMM& order by Typ, Getriebe";
        }
    }

    public static final class Normteile
    extends SQLStatements.Normteile {
        public final String RETRIEVE_BENENNUNGEN() {
            return "select distinct ben_text BENENNUNG, normteilben_textcode TEXTCODE from w_normteilben, w_ben_gk where normteilben_marke_tps = '&MARKE&' and (normteilben_produktart = '&PRODUKTART&' or normteilben_produktart = 'B') and normteilben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by BENENNUNG";
        }

        public final String RETRIEVE_TEILE_ZU_BENENNUNG() {
            return "select distinct ben_text BENENNUNG, teil_hauptgr HG, teil_untergrup UG, teil_sachnr SACHNUMMER, teil_benennzus ZUSATZ, nn_art NORMART, teil_normnummer NORMNUMMER, teil_produktart PRODUKTART, teilm_marke_tps MARKE,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_teil inner join w_ben_gk on (teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') inner join w_teil_marken on (teil_sachnr = teilm_sachnr and teilm_marke_tps = '&MARKE&') left join w_normnummer on (teil_normnummer = nn_nnid and teilm_marke_tps = nn_marke_tps) where (teil_produktart = '&PRODUKTART&' or teil_produktart = 'B' or teil_produktart IS NULL) and (teil_hauptgr = '07' or (teil_hauptgr = '88' and teil_untergrup = '10')) and (teil_lkz = '   ' or teil_lkz IS NULL) and teil_technik IN ('0', '3', '4', '7') and (teil_dispo IN ('0', '2', '3', '4', '5') or (teil_dispo = '6' and teil_entfall_dat > &COMPARE_DATE& )) and &TEXTCODE_STMT& order by ZUSATZ, NORMNUMMER";
        }

        public final String TEXTCODE_GLEICH_STMT() {
            return "teil_textcode = &TEXTCODE&";
        }

        public final String TEXTCODE_IN_STMT() {
            return "teil_textcode IN (&TEXTCODE&)";
        }

        public final String RETRIEVE_TEILE_ZU_NORMNUMMER() {
            return "select distinct teil_hauptgr HG, teil_untergrup UG, teil_sachnr SACHNUMMER, ben_text BENENNUNG, teil_benennzus ZUSATZ, nn_art NORMART, teil_normnummer NORMNUMMER, teil_produktart PRODUKTART, teilm_marke_tps MARKE,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_teil inner join w_ben_gk on (teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') inner join w_teil_marken on (teil_sachnr = teilm_sachnr and teilm_marke_tps = '&MARKE&') left join w_normnummer on (teil_normnummer = nn_nnid and teilm_marke_tps = nn_marke_tps) where (teil_produktart = '&PRODUKTART&' or teil_produktart = 'B' or teil_produktart IS NULL) and UPPER(teil_normnummer) = UPPER('&NORMNUMMER&') and (teil_hauptgr = '07' or (teil_hauptgr = '88' and teil_untergrup = '10')) and (teil_lkz = '   ' or teil_lkz IS NULL) and teil_technik IN ('0', '3', '4', '7') and (teil_dispo IN ('0', '2', '3', '4', '5') or (teil_dispo = '6' and teil_entfall_dat > &COMPARE_DATE& )) order by ZUSATZ, NORMNUMMER";
        }

        public final String RETRIEVE_NORMNUMMERN_GRUPPEN() {
            return "select nng_nngid NUMMER, nng_grafikid GRAFIKID, nng_pos POS, grafik_moddate TS from w_normnummergruppe, w_grafik where nng_marke_tps = '&MARKE&' and (nng_produktart = '&PRODUKTART&' or nng_produktart = 'B') and nng_grafikid = grafik_grafikid and grafik_art = 'T' order by POS";
        }

        public final String RETRIEVE_NORMNUMMERN() {
            return "select nn_nnid NUMMER, nn_art ART, nn_grafikid GRAFIKID, nn_pos POS, grafik_moddate TS from w_normnummer, w_grafik where nn_marke_tps = '&MARKE&' and (nn_produktart = '&PRODUKTART&' or nn_produktart = 'B') and nn_nngid = '&NORMNUMMERNGRUPPE&' and nn_grafikid = grafik_grafikid and grafik_art = 'T' order by POS";
        }
    }

    public static final class NotizuebersichtJAVA
    extends SQLStatements.NotizuebersichtJAVA {
        public final String RETRIEVE_ANZ_NOTIZEN() {
            return "select count (distinct teileinfo_sachnr) ANZ from w_teileinfo@etk_nutzer where (teileinfo_user_id =  '&NUTZER&' OR teileinfo_allgemein = 'J') and teileinfo_firma_id = '&FIRMA&'";
        }

        public final String RETRIEVE_ANZ_NOTIZEN_ZU_SACHNUMMER() {
            return "select count (distinct teileinfo_sachnr) ANZ from w_teileinfo@etk_nutzer where (teileinfo_user_id =  '&NUTZER&' OR teileinfo_allgemein = 'J') and teileinfo_firma_id = '&FIRMA&' and teileinfo_sachnr = '&SACHNUMMER&'";
        }

        public final String RETRIEVE_MIN_HG() {
            return "select min(teil_hauptgr) HG from w_teileinfo@etk_nutzer, w_teil where teileinfo_user_id =  '&NUTZER&' and teileinfo_firma_id = '&FIRMA&' and teileinfo_sachnr = teil_sachnr";
        }

        public final String RETRIEVE_NOTIZEN() {
            return "select teil_hauptgr HG, teil_untergrup UG, teil_sachnr SACHNR, TBEN.ben_text BENENNUNG, MBEN.ben_text MONAT, teileinfo_gueltig_bis_jahr JAHR from w_teileinfo@etk_nutzer, w_publben, w_ben_gk MBEN, w_ben_gk TBEN, w_teil where (teileinfo_user_id =  '&NUTZER&' OR teileinfo_allgemein = 'J') and teileinfo_firma_id = '&FIRMA&' and teileinfo_sachnr = teil_sachnr &HG_STMT& and teil_textcode = TBEN.ben_textcode and TBEN.ben_iso = '&ISO&' and TBEN.ben_regiso = '&REGISO&' and publben_art = 'M' and (to_char(teileinfo_gueltig_bis_monat) = publben_bezeichnung   or '0' || to_char(teileinfo_gueltig_bis_monat) = publben_bezeichnung) and publben_textcode = MBEN.ben_textcode and MBEN.ben_iso = '&ISO&' and MBEN.ben_regiso = '&REGISO&' order by HG, UG, SACHNR";
        }
    }

    public static final class NotizuebersichtHTML
    extends SQLStatements.NotizuebersichtHTML {
        public final String RETRIEVE_ANZ_NOTIZEN() {
            return "select count (*) ANZ from w_teileinfo@etk_nutzer where teileinfo_user_id = '&NUTZER&'";
        }

        public final String RETRIEVE_MIN_HG() {
            return "select min(teil_hauptgr) HG from w_teil, w_teileinfo@etk_nutzer where teileinfo_user_id =  '&NUTZER&' and teil_sachnr = teileinfo_sachnr";
        }

        public final String RETRIEVE_NOTIZEN() {
            return "select teil_hauptgr HG, teil_untergrup UG, teil_sachnr SACHNR, TBEN.ben_text BENENNUNG, MBEN.ben_text MONAT, teileinfo_gueltig_bis_jahr JAHR from w_teileinfo@etk_nutzer, w_ben_gk MBEN, w_publben, w_ben_gk TBEN, w_teil where teileinfo_user_id =  '&NUTZER&' and teileinfo_sachnr = teil_sachnr &HG_STMT& and teil_textcode = TBEN.ben_textcode and TBEN.ben_iso = '&ISO&' and TBEN.ben_regiso = '&REGISO&' and publben_art = 'M' and (to_char(teileinfo_gueltig_bis_monat) = publben_bezeichnung   or '0' || to_char(teileinfo_gueltig_bis_monat) = publben_bezeichnung) and publben_textcode = MBEN.ben_textcode and MBEN.ben_iso = '&ISO&' and MBEN.ben_regiso = '&REGISO&' order by HG, UG, SACHNR";
        }

        public final String HG_STMT() {
            return "and teil_hauptgr = '&HG&'";
        }
    }

    public static final class Notizuebersicht
    extends SQLStatements.Notizuebersicht {
        public final String HG_STMT() {
            return "and teil_hauptgr = '&HG&'";
        }
    }

    public static final class Lagerzeit
    extends SQLStatements.Lagerzeit {
        public final String RETRIEVE_HGS() {
            return "select distinct teil_hauptgr HG from w_teil, w_teil_marken  where teil_lzb = 'J' and teil_hauptgr >= '&HGAB&' &HGBIS_STMT& and teil_produktart IN ('&PRODUKTART&', 'B') and teil_sachnr = teilm_sachnr  and teilm_marke_tps = '&MARKE&'  order by HG";
        }

        public final String RETRIEVE_TEILE() {
            return "select distinct teil_hauptgr HG, teil_untergrup UG, teil_sachnr SACHNR, ben_text BENENNUNG, teil_benennzus ZUSATZ, teil_vorhanden_si_ohne_lzb SI, teil_kom_pi PI, teil_textcode_kom BENKOMMENTARID,  teil_ist_reach REACH,  teil_ist_aspg ASPG,  decode(teil_sachnr, tcp_sachnr, 'C', NULL) TC,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_teil inner join w_teil_marken on (teil_sachnr = teilm_sachnr and teilm_marke_tps = '&MARKE&') inner join w_ben_gk on (teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_tc_performance_allg on (teil_sachnr = tcp_sachnr                                 and tcp_marke_tps = '&MARKE&'                                  and tcp_produktart = '&PRODUKTART&'                                 and tcp_vbereich in (&KATALOGUMFAENGE&)                                 &TC_CHECK_LANDKUERZEL&                                 and tcp_datum_von <= &DATUM&                                 and (tcp_datum_bis is null or tcp_datum_bis <= &DATUM&)) where teil_lzb = 'J' and teil_hauptgr = '&HG&' and teil_produktart IN ('&PRODUKTART&', 'B') order by HG, UG, SACHNR";
        }

        public final String HG_BIS_STMT() {
            return "and teil_hauptgr <= '&HGBIS&'";
        }

        public final String TC_CHECK_LANDKUERZEL() {
            return " and tcp_landkuerzel = '&LANDKUERZEL&'";
        }
    }

    public static final class Erstbevorratung
    extends SQLStatements.Erstbevorratung {
        public final String RETRIEVE_HGS() {
            return "select distinct ebs_hg HG from w_erstbevorratung_suche where ebs_mospid IN (&MOSPIDS&) and ebs_hg >= '&HGAB&' &HGBIS_STMT& &LENKUNG_STMT& order by HG";
        }

        public final String HG_BIS_STMT() {
            return "and ebs_hg <= '&HGBIS&'";
        }

        public final String HG_LENKUNG_STMT() {
            return "and (ebs_lenkung = '&LENKUNG&' OR ebs_lenkung IS NULL)";
        }

        public final String RETRIEVE_TEILE() {
            return "select distinct teil_hauptgr HG, teil_untergrup UG, teil_sachnr SACHNR, ben_text BENENNUNG, teil_benennzus ZUSATZ, teil_vorhanden_si SI, teil_lzb LZB, teil_kom_pi PI, teil_textcode_kom BENKOMMENTARID,  teil_ist_reach REACH,  teil_ist_aspg ASPG,  decode(tcp_sachnr, tcp_sachnr, 'C', NULL) TC,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_erstbevorratung inner join w_teil on (eb_sachnr = teil_sachnr) inner join w_ben_gk on (teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_tc_performance on (tcp_mospid in (&MOSPIDS&) and tcp_sachnr = eb_sachnr &TC_CHECK_LANDKUERZEL& and tcp_datum_von <= &DATUM& and (tcp_datum_bis is null or tcp_datum_bis >= &DATUM&)) where eb_hg = '&HG&' and eb_mospid IN (&MOSPIDS&) &LENKUNG_STMT& order by HG, UG, SACHNR";
        }

        public final String TEIL_LENKUNG_STMT() {
            return "and (eb_lenkung = '&LENKUNG&' OR eb_lenkung IS NULL)";
        }

        public final String TC_CHECK_LANDKUERZEL() {
            return " and tcp_landkuerzel = '&LANDKUERZEL&'";
        }
    }

    public static final class TechnischeLiteratur
    extends SQLStatements.TechnischeLiteratur {
        public final String RETRIEVE_FGS() {
            return "select hgfg_hg Hauptgruppe, hgfg_fg Funktionsgruppe, ben_text Benennung from w_hgfg_mosp, w_hgfg, w_ben_gk where hgfgm_mospid = &MOSP& and hgfgm_hg = '01' and hgfgm_hg = hgfg_hg and hgfgm_fg = hgfg_fg and hgfgm_produktart = hgfg_produktart and hgfgm_bereich = hgfg_bereich and hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' union select hgfg_hg Hauptgruppe, hgfg_fg Funktionsgruppe, ben_text Benennung from w_bildtaf, w_hgfg, w_ben_gk, w_btzeilenugb_verbauung where bildtaf_bteart = 'U' and bildtaf_hg = '01' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_btnr = btzeilenuv_btnr and btzeilenuv_marke_tps = '&MARKE&' and bildtaf_hg = hgfg_hg and bildtaf_fg = hgfg_fg and bildtaf_produktart = hgfg_produktart and hgfg_bereich = 'KONZERN' and hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by 1, 2";
        }

        public final String RETRIEVE_SPRACHEN() {
            return "select substr(publben_bezeichnung, 1, 2) SpracheISO, substr(publben_bezeichnung, 3, 2) SpracheRegISO, ben_text Benennung from w_publben, w_ben_gk where publben_art = 'T' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Benennung";
        }

        public final String LOAD_TECHNISCHE_LITERATUR() {
            return " select teil_hauptgr HG, teil_untergrup UG, teil_sachnr Sachnummer, teil_ist_eba istEBA, benennung.ben_text Benennung, kommentar.ben_text Kommentar, teil_benennzus Zusatz, btzeilen_eins Einsatz, btzeilen_auslf Auslauf, teil_mam MAM,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_bildtaf inner join w_btzeilen on (bildtaf_btnr = btzeilen_btnr and btzeilen_bildposnr <> '--') inner join w_teil on (btzeilen_sachnr = teil_sachnr) inner join w_ben_gk benennung on (teil_textcode = benennung.ben_textcode and benennung.ben_iso = '&ISO&' and benennung.ben_regiso = '&REGISO&') left join w_ben_gk kommentar on (teil_textcode_kom = kommentar.ben_textcode and kommentar.ben_iso = '&ISO&' and kommentar.ben_regiso = '&REGISO&') inner join w_btzeilen_verbauung on (btzeilen_btnr = btzeilenv_btnr and  btzeilen_pos = btzeilenv_pos and btzeilenv_mospid = &MOSP&) where bildtaf_hg = '01' and bildtaf_fg = '&FG&' and bildtaf_bteart = 'G' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') &TLSPRACHE_STMT_GEB& union select teil_hauptgr HG, teil_untergrup UG, teil_sachnr Sachnummer, teil_ist_eba istEBA, benennung.ben_text Benennung, kommentar.ben_text Kommentar, teil_benennzus Zusatz, btzeilenu_eins Einsatz, btzeilenu_ausl Auslauf, teil_mam MAM,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_bildtaf inner join w_btzeilenugb on (bildtaf_btnr = btzeilenu_btnr and btzeilenu_bildposnr <> '--') inner join w_teil on (btzeilenu_sachnr = teil_sachnr) inner join w_ben_gk benennung on (teil_textcode = benennung.ben_textcode and benennung.ben_iso = '&ISO&' and benennung.ben_regiso = '&REGISO&') left join w_ben_gk kommentar on (teil_textcode_kom = kommentar.ben_textcode and kommentar.ben_iso = '&ISO&' and kommentar.ben_regiso = '&REGISO&') inner join w_btzeilenugb_verbauung on (btzeilenu_btnr = btzeilenuv_btnr and  btzeilenu_pos = btzeilenuv_pos and btzeilenuv_marke_tps = '&MARKE&') where bildtaf_hg = '01' and bildtaf_fg = '&FG&' and bildtaf_bteart = 'U' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') &TLSPRACHE_STMT_UGB& order by Benennung asc, Kommentar asc, Einsatz desc";
        }

        public final String TL_SPRACHE_STMT_GEB() {
            return "and btzeilen_bildposnr = (select tlsb_bildnummer from w_tl_sprache_bnb where tlsb_iso = '&ISO_TL&' and tlsb_regiso = '&REGISO_TL&')";
        }

        public final String TL_SPRACHE_STMT_UGB() {
            return "and btzeilenu_bildposnr = (select tlsb_bildnummer from w_tl_sprache_bnb where tlsb_iso = '&ISO_TL&' and tlsb_regiso = '&REGISO_TL&')";
        }
    }

    public static final class TeilevwdgFzg
    extends SQLStatements.TeilevwdgFzg {
        public final String RETRIEVE_HGS() {
            return "select distinct tvs_hg HG from w_teileverwendungfzg_suche where tvs_mospid IN (&MOSPIDS&) and tvs_hg >= '&HGAB&' &HGBIS_STMT& &DATSERIE_STMT& order by HG";
        }

        public final String HG_BIS_STMT() {
            return "and tvs_hg <= '&HGBIS&'";
        }

        public final String HG_DAT_SERIE_STMT() {
            return "and tvs_einsatz_serie_max >= &DATUMAB&";
        }

        public final String HG_LENKUNG_STMT() {
            return "and (tvs_lenkung = '&LENKUNG&' OR tvs_lenkung IS NULL)";
        }

        public final String RETRIEVE_TEILE_OHNE_LENKUNG() {
            return "select distinct NeuesTeil.teil_hauptgr HG, NeuesTeil.teil_untergrup UG, NeuesTeil.teil_sachnr SACHNR, NeuesTeil.teil_alt SACHNRALT, NeuesTeil.teil_austausch_alt AT, AltesTeil.teil_hauptgr HGALT, AltesTeil.teil_untergrup UGALT, ben_text BENENNUNG, NeuesTeil.teil_benennzus ZUSATZ, NeuesTeil.teil_vorhanden_si SI, NeuesTeil.teil_lzb LZB, NeuesTeil.teil_kom_pi PI, NeuesTeil.teil_textcode_kom BENKOMMENTARID, NeuesTeil.teil_ist_reach REACH,  NeuesTeil.teil_ist_aspg ASPG,  NeuesTeil.teil_ist_stecker STECKER,  decode(tcp_sachnr, tcp_sachnr, 'C', NULL) TC,  NeuesTeil.teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilen_verbauung inner join w_teil NeuesTeil on (btzeilenv_sachnr = NeuesTeil.teil_sachnr and NeuesTeil.teil_hauptgr = '&HG&') inner join w_ben_gk on (NeuesTeil.teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_teil AltesTeil on (NeuesTeil.teil_alt = AltesTeil.teil_sachnr) left join w_tc_performance on (tcp_mospid in (&MOSPIDS&) and tcp_sachnr = NeuesTeil.teil_sachnr &TC_CHECK_LANDKUERZEL& and tcp_datum_von <= &DATUM& and (tcp_datum_bis is null or tcp_datum_bis >= &DATUM&)) where btzeilenv_mospid IN (&MOSPIDS&) &DATSERIE_STMT& and btzeilenv_alter_kz is null order by HG, UG, SACHNR";
        }

        public final String RETRIEVE_TEILE_MIT_LENKUNG() {
            return "select distinct NeuesTeil.teil_hauptgr HG, NeuesTeil.teil_untergrup UG, NeuesTeil.teil_sachnr SACHNR, NeuesTeil.teil_alt SACHNRALT, NeuesTeil.teil_austausch_alt AT, AltesTeil.teil_hauptgr HGALT, AltesTeil.teil_untergrup UGALT, ben_text BENENNUNG, NeuesTeil.teil_benennzus ZUSATZ, NeuesTeil.teil_vorhanden_si SI, NeuesTeil.teil_lzb LZB, NeuesTeil.teil_kom_pi PI, NeuesTeil.teil_textcode_kom BENKOMMENTARID, NeuesTeil.teil_ist_reach REACH,  NeuesTeil.teil_ist_aspg ASPG,  NeuesTeil.teil_ist_stecker STECKER,  decode(tcp_sachnr, tcp_sachnr, 'C', NULL) TC,  NeuesTeil.teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilen_verbauung inner join w_teil NeuesTeil on (btzeilenv_sachnr = NeuesTeil.teil_sachnr and NeuesTeil.teil_hauptgr = '&HG&') inner join w_btzeilen on (btzeilenv_sachnr = btzeilen_sachnr and btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos and (btzeilen_lenkg = '&LENKUNG&' OR btzeilen_lenkg IS NULL)) inner join w_ben_gk on (NeuesTeil.teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_teil AltesTeil on (NeuesTeil.teil_alt = AltesTeil.teil_sachnr) left join w_tc_performance on (tcp_mospid in (&MOSPIDS&) and tcp_sachnr = NeuesTeil.teil_sachnr &TC_CHECK_LANDKUERZEL& and tcp_datum_von <= &DATUM& and (tcp_datum_bis is null or tcp_datum_bis >= &DATUM&)) where btzeilenv_mospid IN (&MOSPIDS&) &DATSERIE_STMT& and btzeilenv_alter_kz is null order by HG, UG, SACHNR";
        }

        public final String TEIL_DAT_SERIE_STMT() {
            return "and NeuesTeil.teil_einsatz_serie >= &DATUMAB&";
        }

        public final String RETRIEVE_TEIL_ONLY_IN_OHNE_LENKUNG() {
            return "select count (*) ANZ from w_btzeilen_verbauung where btzeilenv_sachnr = '&SACHNR&' and btzeilenv_mospid NOT IN (&MOSPIDS&)";
        }

        public final String RETRIEVE_TEIL_ONLY_IN_MIT_LENKUNG() {
            return "select count (*) ANZ from w_btzeilen, w_btzeilen_verbauung where btzeilen_sachnr = '&SACHNR&' and (btzeilen_lenkg = '&LENKUNG&' OR btzeilen_lenkg IS NULL) and btzeilen_btnr = btzeilenv_btnr and btzeilen_pos = btzeilenv_pos and btzeilen_sachnr = btzeilenv_sachnr and btzeilenv_mospid NOT IN (&MOSPIDS&)";
        }

        public final String TC_CHECK_LANDKUERZEL() {
            return " and tcp_landkuerzel = '&LANDKUERZEL&'";
        }
    }

    public static final class TeileverwendungReduziert
    extends SQLStatements.TeileverwendungReduziert {
        public final String RETRIEVE_TEIL() {
            return "select teil_hauptgr HG, teil_untergrup UG, teil_sachnr Sachnummer, ben_text Ben, teil_benennzus Zusatz from w_teil, w_ben_gk where teil_sachnr = '&SACHNUMMER&' and teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'";
        }

        public final String RETRIEVE_VERWENDUNG() {
            return "select distinct BR.ben_text Baureihe, fztyp_erwvbez Modell, KAR.ben_text Karosserie, fztyp_ktlgausf Region from w_btzeilen_verbauung, w_ben_gk BR, w_ben_gk KAR, w_baureihe, w_publben, w_fztyp where btzeilenv_sachnr = '&SACHNUMMER&' and btzeilenv_mospid IN (&MOSPIDS&) and btzeilenv_mospid = fztyp_mospid and fztyp_sichtschutz = 'N' and fztyp_baureihe = baureihe_baureihe and baureihe_textcode = BR.ben_textcode and BR.ben_iso = '&ISO&' and BR.ben_regiso = '&REGISO&' and publben_art = 'K' and fztyp_karosserie = publben_bezeichnung and publben_textcode = KAR.ben_textcode and KAR.ben_iso = '&ISO&' and KAR.ben_regiso = '&REGISO&' order by Baureihe, Modell, Karosserie, Region";
        }
    }

    public static final class VisualisierungTeil
    extends SQLStatements.VisualisierungTeil {
        public final String RETRIEVE_VISUALISIERUNGSINFO_GEB() {
            return "select teil_hauptgr HG, teil_untergrup UG, teil_sachnr Sachnummer, t.ben_text Teilebenennung, teil_benennzus TeilebenennungZusatz, bildtaf_btnr BildtafelNummer, bt.ben_text BildtafelUeberschrift, btzeilen_bildposnr Bildnummer, bildtaf_grafikid GrafikId, grafik_format GrafikFormat, grafik_moddate GrafikTimestamp from w_btzeilen_verbauung, w_grafik, w_baureihe, w_fztyp, w_ben_gk t, w_ben_gk bt, w_teil, w_bildtaf, w_btzeilen where btzeilenv_sachnr = '&SACHNUMMER&' and btzeilenv_mospid = fztyp_mospid and fztyp_baureihe = baureihe_baureihe and baureihe_marke_tps in ('&MARKE&') and btzeilenv_btnr = bildtaf_btnr and bildtaf_produktart in ('&PRODUKTART&') and btzeilenv_sachnr = teil_sachnr and btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos  and teil_textcode = t.ben_textcode and t.ben_iso = '&ISO&' and t.ben_regiso = '&REGISO&' and bildtaf_textc = bt.ben_textcode and bt.ben_iso = '&ISO&' and bt.ben_regiso = '&REGISO&' and bildtaf_grafikid = grafik_grafikid and grafik_art = 'Z'";
        }

        public final String RETRIEVE_VISUALISIERUNGSINFO_UGB() {
            return "select teil_hauptgr HG, teil_untergrup UG, teil_sachnr Sachnummer, t.ben_text Teilebenennung, teil_benennzus TeilebenennungZusatz, bildtaf_btnr BildtafelNummer, bt.ben_text BildtafelUeberschrift, btzeilenu_bildposnr Bildnummer, bildtaf_grafikid GrafikId, grafik_format GrafikFormat, grafik_moddate GrafikTimestamp from w_btzeilenugb, w_grafik, w_ben_gk t, w_ben_gk bt, w_teil, w_bildtaf, w_btzeilenugb_verbauung where btzeilenu_sachnr = '&SACHNUMMER&' and btzeilenu_btnr = btzeilenuv_btnr and btzeilenu_pos = btzeilenuv_pos and btzeilenuv_marke_tps in ('&MARKE&') and btzeilenu_btnr = bildtaf_btnr and bildtaf_produktart in ('&PRODUKTART&') and btzeilenu_sachnr = teil_sachnr and teil_textcode = t.ben_textcode and t.ben_iso = '&ISO&' and t.ben_regiso = '&REGISO&' and bildtaf_textc = bt.ben_textcode and bt.ben_iso = '&ISO&' and bt.ben_regiso = '&REGISO&' and bildtaf_grafikid = grafik_grafikid and grafik_art = 'Z'";
        }
    }

    public static final class Teileersetzung
    extends SQLStatements.Teileersetzung {
        public final String RETRIEVE_HGS() {
            return "select distinct tss_hg HG from w_teileersetzung_suche where tss_mospid IN (&MOSPIDS&) and tss_hg >= '&HGAB&' &HGBIS_STMT& &LENKUNG_STMT& &DATSERIE_STMT& order by HG";
        }

        public final String HG_BIS_STMT() {
            return "and tss_hg <= '&HGBIS&'";
        }

        public final String HG_LENKUNG_STMT() {
            return "and (tss_lenkung = '&LENKUNG&' OR tss_lenkung IS NULL)";
        }

        public final String HG_DAT_SERIE_STMT() {
            return "and tss_einsatz_serie_max >= &DATUMAB&";
        }

        public final String FIND_HG() {
            return "select distinct teil_hauptgr HG from w_teil where teil_hauptgr = '&HG&'";
        }

        public final String RETRIEVE_TEILE() {
            return "select distinct NEU.teil_hauptgr HG,  NEU.teil_untergrup UG,  ts_sachnr SACHNR, NEU.teil_alt SACHNRALT, ALT.teil_hauptgr HGALT, ALT.teil_untergrup UGALT, NEU.teil_austausch_alt AT, ben_text BENENNUNG, NEU.teil_benennzus ZUSATZ, NEU.teil_vorhanden_si SI, NEU.teil_lzb LZB, NEU.teil_kom_pi PI, NEU.teil_textcode_kom BENKOMMENTARID, NEU.teil_ist_reach REACH,  NEU.teil_ist_aspg ASPG,  decode(tcp_sachnr, tcp_sachnr, 'C', NULL) TC,  NEU.teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_teileersetzung inner join w_teil NEU on (ts_sachnr = NEU.teil_sachnr)      inner join w_teil ALT on (NEU.teil_alt = ALT.teil_sachnr)      inner join w_ben_gk on (NEU.teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&')      left join w_tc_performance on (tcp_mospid in (&MOSPIDS&) and tcp_sachnr = ts_sachnr &TC_CHECK_LANDKUERZEL& and tcp_datum_von <= &DATUM& and (tcp_datum_bis is null or tcp_datum_bis >= &DATUM&)) where ts_hg = '&HG&' and ts_mospid IN (&MOSPIDS&) &LENKUNG_STMT& &DATSERIE_STMT& order by HGALT, UGALT, SACHNRALT, HG, UG, SACHNR";
        }

        public final String TEIL_DAT_SERIE_STMT() {
            return "and NEU.teil_einsatz_serie >= &DATUMAB&";
        }

        public final String TEIL_LENKUNG_STMT() {
            return "and (ts_lenkung = '&LENKUNG&' OR ts_lenkung IS NULL)";
        }

        public final String TC_CHECK_LANDKUERZEL() {
            return " and tcp_landkuerzel = '&LANDKUERZEL&'";
        }
    }

    public static final class ValueLine
    extends SQLStatements.ValueLine {
        public final String LOAD_HGS() {
            return "select distinct hgfg_hg HG, ben_text Benennung from w_kompl_satz, w_ben_gk, w_hgfg where ks_marke_tps = '&MARKE&' and ks_ist_valueline = 'J' and ks_produktart IN ('B', '&PRODUKTART&') and hgfg_produktart = '&PRODUKTART&' and ks_hg = hgfg_hg and hgfg_fg = '00' and hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by HG";
        }

        public final String LOAD_SAETZE() {
            return "select distinct teil_hauptgr HG, teil_untergrup UG, teil_sachnr Sachnummer, ben_text Benennung, teil_benennzus Zusatz, teil_vorhanden_si vorhandenSI, teil_textcode_kom BenKommentarId, teil_ist_reach Reach,  teil_ist_aspg Aspg,  decode(teil_sachnr, tcp_sachnr, 'C', NULL) TC,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_kompl_satz inner join w_teil on (ks_sachnr_satz = teil_sachnr) inner join w_ben_gk on (teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_tc_performance_allg on (teil_sachnr = tcp_sachnr                                 and tcp_marke_tps = '&MARKE&'                                  and tcp_produktart = '&PRODUKTART&'                                 and tcp_vbereich in (&KATALOGUMFAENGE&)                                 &TC_CHECK_LANDKUERZEL&                                 and tcp_datum_von <= &DATUM&                                 and (tcp_datum_bis is null or tcp_datum_bis <= &DATUM&)) where ks_marke_tps = '&MARKE&' and ks_produktart IN ('B', '&PRODUKTART&') and ks_hg = '&HG&' and ks_ist_valueline = 'J' order by HG, UG, Sachnummer";
        }

        public final String TC_CHECK_LANDKUERZEL() {
            return " and tcp_landkuerzel = '&LANDKUERZEL&'";
        }

        public final String LOAD_BTE_BAUREIHEN() {
            return "select distinct baureihe_baureihe BAUREIHE, ben_text EXT_BAUREIHE, baureihe_position POS from  w_hgfg, w_bildtaf, w_ben_gk, w_bildtaf_suche, w_baureihe, w_fztyp where hgfg_bereich = 'KONZERN' and hgfg_produktart = '&PRODUKTART&' and hgfg_ist_valueline = 'J' and bildtaf_hg = hgfg_hg and bildtaf_fg = hgfg_fg and bildtafs_btnr = bildtaf_btnr and bildtafs_hg = bildtaf_hg and fztyp_mospid = bildtafs_mospid and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_sichtschutz = 'N' and fztyp_ktlgausf IN (&REGIONEN&) and baureihe_baureihe = fztyp_baureihe and baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and ben_textcode = baureihe_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by POS";
        }

        public final String LOAD_BILDTAFELN() {
            return "select distinct bildtaf_btnr BildtafelNr, B.ben_text Benennung, fztyp_erwvbez Modell, K.ben_text Karosserie, fztyp_karosserie KarosserieId, baureihe_bauart Bauart, fztyp_ktlgausf Region, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos from  w_hgfg, w_bildtaf, w_ben_gk B, w_publben, w_ben_gk K, w_bildtaf_suche, w_baureihe, w_fztyp where hgfg_ist_valueline = 'J' and bildtaf_hg = hgfg_hg and bildtaf_fg = hgfg_fg and bildtafs_btnr = bildtaf_btnr and bildtafs_hg = bildtaf_hg and fztyp_mospid = bildtafs_mospid and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_sichtschutz = 'N' and fztyp_ktlgausf IN (&REGIONEN&) and baureihe_baureihe = '&BAUREIHE&' and baureihe_baureihe = fztyp_baureihe and baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and B.ben_textcode = bildtaf_textc and B.ben_iso = '&ISO&' and B.ben_regiso = '&REGISO&' and publben_bezeichnung = fztyp_karosserie and publben_art = 'K' and K.ben_textcode = publben_textcode and K.ben_iso = '&ISO&' and K.ben_regiso = '&REGISO&' order by Pos";
        }

        public final String LOAD_TEILENUMMERN() {
            return " select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text Kommentar,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_teil inner join w_teil_marken on (teil_sachnr = teilm_sachnr and teilm_marke_tps = 'BMW') inner join  w_ben_gk ben_teil on (ben_textcode = teil_textcode and ben_teil.ben_iso = 'de' and ben_teil.ben_regiso = '  ') left join w_ben_gk ben_komm on (ben_komm.ben_textcode = teil_textcode_kom and ben_komm.ben_iso = '  ' and ben_komm.ben_regiso = '  ') where teil_ist_valueline = 'J' and teil_sachnr = teilm_sachnr and teilm_marke_tps = 'BMW' and teil_verbaubar = 'J' and teil_produktart in ('P', 'B') order by Benennung, Hauptgruppe, Untergruppe, Sachnummer";
        }
    }

    public static final class Aspg
    extends SQLStatements.Aspg {
        public final String LOAD_ASPG_TEILE() {
            return "select teilaspg_sachnr_pg, teilaspg_vmenge from w_teil_aspg where teilaspg_sachnr = ? and teilaspg_kz_gruppe= ? ";
        }

        public final String LOAD_STECKER() {
            return "select teil_hauptgr, teil_untergrup,teilaspg_sachnr_pg, ben_text, teilaspg_vmenge, teil_benennzus, teil_technik, teil_entfall_kez, teil_ist_diebstahlrelevant from w_teil_aspg, w_teil, w_ben_gk where teilaspg_sachnr = ? and teilaspg_kz_gruppe= ? and teilaspg_sachnr_pg = teil_sachnr and teil_textcode = ben_textcode and ben_iso = ? and ben_regiso = ?";
        }
    }

    public static final class SatzEinzelteile
    extends SQLStatements.SatzEinzelteile {
        public final String LOAD_HG() {
            return "select distinct hgfg_hg HG, ben_text Benennung from w_kompl_satz, w_ben_gk, w_hgfg where ks_marke_tps = '&MARKE&' and ks_produktart IN ('B', '&PRODUKTART&') and hgfg_produktart = '&PRODUKTART&' and ks_hg = hgfg_hg and hgfg_fg = '00' and hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by HG";
        }

        public final String LOAD_SATZ() {
            return "select distinct teil_hauptgr HG, teil_untergrup UG, teil_sachnr Sachnummer, ben_text Benennung, teil_benennzus Zusatz, teil_vorhanden_si vorhandenSI, teil_textcode_kom BenKommentarId, teil_ist_reach Reach,  teil_ist_aspg Aspg,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant,  decode(teil_sachnr, tcp_sachnr, 'C', NULL) TC from w_kompl_satz inner join w_teil on (ks_sachnr_satz = teil_sachnr) inner join w_ben_gk on (teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_tc_performance_allg on (teil_sachnr = tcp_sachnr                                 and tcp_marke_tps = '&MARKE&'                                  and tcp_produktart = '&PRODUKTART&'                                 and tcp_vbereich in (&KATALOGUMFAENGE&)                                 &TC_CHECK_LANDKUERZEL&                                 and tcp_datum_von <= &DATUM&                                 and (tcp_datum_bis is null or tcp_datum_bis <= &DATUM&)) where ks_marke_tps = '&MARKE&' and ks_produktart IN ('B', '&PRODUKTART&') and ks_hg = '&HG&' order by HG, UG, Sachnummer";
        }

        public final String COUNT_EINZELTEILE() {
            return "select count(ke_sachnr_einzelteil) Anzahl  from w_kompl_einzelteil where ke_sachnr_satz = '&SACHNUMMER&'";
        }

        public final String LOAD_EINZELTEIL() {
            return "select distinct teil_hauptgr HG, teil_untergrup UG, teil_sachnr Sachnummer, ben_text Benennung, teil_benennzus Zusatz, ke_menge Menge, ke_beziehbar istBeziehbar, teil_vorhanden_si vorhandenSI, teil_textcode_kom BenKommentarId,  teil_ist_reach Reach,  teil_ist_aspg Aspg,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant,  decode(teil_sachnr, tcp_sachnr, 'C', NULL) TC, teil_ist_eba istEBA, ke_pos Pos from w_kompl_einzelteil inner join w_teil on (ke_sachnr_einzelteil = teil_sachnr)   inner join w_ben_gk on (teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&')   left join w_tc_performance_allg on (teil_sachnr = tcp_sachnr                                   and tcp_marke_tps = '&MARKE&'                                    and tcp_produktart = '&PRODUKTART&'                                   and tcp_vbereich in (&KATALOGUMFAENGE&)                                   &TC_CHECK_LANDKUERZEL&                                   and tcp_datum_von <= &DATUM&                                   and (tcp_datum_bis is null or tcp_datum_bis <= &DATUM&)) where ke_sachnr_satz = '&SACHNUMMER&' order by Pos";
        }

        public final String TC_CHECK_LANDKUERZEL() {
            return " and tcp_landkuerzel = '&LANDKUERZEL&'";
        }
    }

    public static final class Interpretation
    extends SQLStatements.Interpretation {
        public final String EXIST_SACHNUMMER_MARKE() {
            return "select distinct teilm_marke_tps Marke from w_teil_marken where teilm_sachnr = '&SACHNUMMER&' and teilm_marke_tps IN (&MARKEN&)";
        }

        public final String LOAD_INTERPRETATION_EINSTIEG() {
            return "select hist_sachnr Sachnummer, hist_zweig_nr ZweigNr, hist_struktur_nr StrukturNr from w_hist where hist_sachnr_h = '&SACHNUMMER&'";
        }

        public final String LOAD_INTERPRETATION() {
            return "select teil_hauptgr Hg, teil_untergrup Ug, hist_sachnr_h Sachnummer, ben_text Benennung, teil_entfall_dat EntfallDatum, teil_bestellbar Bestellbar, teilm_marke_tps Marke, hist_zweig_nr ZweigNr, hist_struktur_nr StrukturNr from w_hist, w_teil, w_ben_gk, w_teil_marken where hist_sachnr = '&SACHNUMMER&' and hist_sachnr_h = teil_sachnr and teil_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'  and teilm_sachnr = teil_sachnr order by ZweigNr, StrukturNr";
        }
    }

    public static final class Polstercode
    extends SQLStatements.Polstercode {
        public final String LOAD_POLSTERCODE() {
            return "select bedaflpc_art Art, bedaflpc_code Code, ben_text Benennung, bedaflpc_pcode PCode, bedaflpc_gilt_v GueltigVon, bedaflpc_gilt_b GueltigBis, bedaflpc_pos Pos from w_bed_aflpc, w_ben_gk where ben_textcode = bedaflpc_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'  order by Pos";
        }
    }

    public static final class BTEInfo
    extends SQLStatements.BTEInfo {
        public final String RETRIEVE_BTEINFO() {
            return "select ben.ben_text Ueberschrift from w_bildtaf, w_ben_gk ben where bildtaf_btnr = '&BTENR&' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_textc = ben.ben_textcode and ben.ben_iso = '&ISO&' and  ben.ben_regiso = '&REGISO&'";
        }

        public final String RETRIEVE_BTEKOMMENTAR() {
            return "select distinct komm_id KommId, ben_text Text, komm_code Code, komm_vz VZ,  komm_darstellung Darstellung, komm_tiefe Tiefe,  komm_pos Pos from w_bildtaf, w_ben_gk, w_komm where bildtaf_btnr = '&BTNR&' and bildtaf_kommbt = komm_id and komm_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by KommId, Pos";
        }
    }

    public static final class TeilelisteJAVA_SAP
    extends SQLStatements.TeilelisteJAVA_SAP {
        public final String LOAD_TEILELISTE_HIST() {
            return "select teilelistehist_datum Datum, ben_text Funktion, teilelistehist_abfrage_id AbfragId from w_ben_gk@etk_publ, w_publben@etk_publ, w_teileliste_hist where teilelistehist_firma_id = '&FIRMA&' and teilelistehist_id = '&ID&' and teilelistehist_user_id = '&NUTZER&' and publben_bezeichnung = teilelistehist_funktion and ben_textcode = publben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and publben_art = 'H' ORDER BY teilelistehist_datum DESC";
        }

        public final String DELETE_TEILELISTE_HIST() {
            return "delete from w_teileliste_hist where teilelistehist_firma_id = '&FIRMA&' and teilelistehist_id = '&ID&' and teilelistehist_user_id = '&NUTZER&'";
        }

        public final String INSERT_TEILELISTE_HIST() {
            return "insert into w_teileliste_hist ( teilelistehist_firma_id, teilelistehist_id, teilelistehist_user_id, teilelistehist_datum, teilelistehist_abfrage_id, teilelistehist_funktion) values( '&FIRMA&', '&ID&', '&NUTZER&', SYSDATE, &ABFRAGEID&, '&FUNKTION&')";
        }

        public final String LOAD_NUTZERDATEN() {
            return "select user_rr_name Name, user_rr_telefon Telefon, user_rr_email Email, user_rr_haendlernr Haendlernr from w_user_rr where user_rr_firma_id = '&FIRMA&' and user_rr_id = '&ID&'";
        }

        public final String INSERT_NUTZERDATEN() {
            return "insert into w_user_rr ( user_rr_firma_id, user_rr_id, user_rr_name, user_rr_telefon, user_rr_email, user_rr_haendlernr) values( '&FIRMA&', '&ID&', &NAME&, &TELEFON&, &EMAIL&, '&HAENDLERNR&')";
        }

        public final String UPDATE_NUTZERDATEN() {
            return "update w_user_rr set user_rr_name = &NAME&, user_rr_telefon = &TELEFON&, user_rr_email = &EMAIL&, user_rr_haendlernr = '&HAENDLERNR&' where user_rr_firma_id = '&FIRMA&' and user_rr_id = '&ID&'";
        }

        public final String UPDATE_ID_IN_LISTEPOS() {
            return "update w_teilelistepos set teilelistepos_teileliste_id = '&NEWID&' where teilelistepos_teileliste_id = '&OLDID&'\tand teilelistepos_firma_id = '&FIRMA&'\tand teilelistepos_filiale_id = '&FILIALE&'\tand teilelistepos_user_id = '&USER&'";
        }

        public final String UPDATE_ID_IN_LISTEHIST() {
            return "update w_teileliste_hist set teilelistehist_id = '&NEWID&' where teilelistehist_id = '&OLDID&'\tand teilelistehist_firma_id = '&FIRMA&'\tand teilelistehist_user_id = '&USER&'";
        }

        public final String UPDATE_ID_IN_LISTE() {
            return "update w_teileliste set teileliste_id = '&NEWID&' where teileliste_id = '&OLDID&'\tand teileliste_firma_id = '&FIRMA&'\tand teileliste_filiale_id = '&FILIALE&'\tand teileliste_user_id = '&USER&'";
        }
    }

    public static final class TeilelisteJAVA
    extends SQLStatements.TeilelisteJAVA {
        public final String RETRIEVE_LISTEN_IDS() {
            return "select teileliste_marke Marke,  teileliste_filiale_id Filiale, teileliste_user_id Eigentuemer, teileliste_id ListeId, teileliste_rr_sap_status RrSapStatus from w_teileliste@etk_nutzer where teileliste_firma_id = '&FIRMA&' and teileliste_filiale_id = '&FILIALE&' and teileliste_user_id = '&NUTZER&' and teileliste_marke IN (&MARKEN&) and teileliste_gesperrt IS NULL and (teileliste_gesperrt_von IS NULL or teileliste_gesperrt_von = teileliste_user_id)";
        }

        public final String RETRIEVE_BESTELLLISTEN_IDS() {
            return "select bestellliste_liste_id ListeId from w_bestellliste@etk_nutzer where bestellliste_firma_id = '&FIRMA&' and bestellliste_filiale_id = '&FILIALE&'";
        }

        public final String RETRIEVE_UEBERSICHT() {
            return "select teileliste_id ListeId, teileliste_user_id UserId, user_name UserName, teileliste_filiale_id Filiale, filiale_bezeichnung FilialeBen, teileliste_bemerkung Bemerkung, teileliste_erzeugt Erzeugt, NVL(teileliste_gesperrt, 'N') Gesperrt, NVL(teileliste_privat, 'N') Privat, teileliste_rr_sap_status RrSapStatus, teileliste_auftragsnr Auftragsnr, auftrag_kundennr Kundennr from w_teileliste@etk_nutzer left join w_auftrag@etk_nutzer on (teileliste_firma_id = auftrag_firma_id and teileliste_filiale_id = auftrag_filiale_id and teileliste_auftragsnr = auftrag_auftragsnr), w_user@etk_nutzer, w_filiale@etk_nutzer where teileliste_firma_id = '&FIRMA&' and &FILTER& and user_id = teileliste_user_id and user_firma_id = teileliste_firma_id and filiale_firma_id = teileliste_firma_id and filiale_id = teileliste_filiale_id order by UserId, Gesperrt, Auftragsnr, Erzeugt DESC, ListeId";
        }

        public final String FILTER_NUTZER() {
            return "teileliste_user_id = '&NUTZER&'";
        }

        public final String FILTER_FILIALE() {
            return "teileliste_filiale_id = '&FILIALE&' and teileliste_user_id <> '&NUTZER&' and teileliste_privat IS NULL";
        }

        public final String FILTER_FIRMA() {
            return "(teileliste_user_id <> '&NUTZER&' and teileliste_privat IS NULL)";
        }

        public final String FILTER_FIRMA_MIT_PRIVAT() {
            return "(teileliste_user_id <> '&NUTZER&')";
        }

        public final String RETRIEVE_UEBERSICHT_BESTELLLISTEN() {
            return "select bestellliste_liste_id ListeId, bestellliste_gesperrt_von GesperrtVon, bestellliste_gesperrt_am GesperrtAm from w_bestellliste@etk_nutzer where bestellliste_firma_id = '&FIRMA&' and bestellliste_filiale_id = '&FILIALE&' order by ListeId";
        }

        public final String RETRIEVE_LISTE_ALLG() {
            return "select teileliste_bemerkung Bemerkung, teileliste_erzeugt Erzeugt, teileliste_geaendert Geaendert, teileliste_marke Marke, teileliste_auftragsnr Auftragsnr, teileliste_auftragsnr_lokal AuftragsnrLokal, teileliste_kundennr_lokal KundennrLokal, teileliste_privat Privat, teileliste_gesperrt_von GesperrtVon, teileliste_gesperrt_am GesperrtAm, teileliste_fzgdurchlauf DurchlaufId, teileliste_dringlichkeit Dringlichkeit, teileliste_vin Vin, teileliste_rr_sap_status RrSapStatus, auftrag_kundennr Kundennr, auftrag_kundenname Kundenname, auftrag_fgstnr FgStNr from w_teileliste@etk_nutzer left join w_auftrag@etk_nutzer on (teileliste_firma_id = auftrag_firma_id and teileliste_filiale_id = auftrag_filiale_id and teileliste_auftragsnr = auftrag_auftragsnr) where teileliste_firma_id = '&FIRMA&' and teileliste_filiale_id = '&FILIALE&' and teileliste_id = '&ID&' and teileliste_user_id = '&NUTZER&'";
        }

        public final String RETRIEVE_BESTELLLISTE_ALLG() {
            return "select bestellliste_gesperrt_von GesperrtVon, bestellliste_gesperrt_am GesperrtAm from w_bestellliste@etk_nutzer where bestellliste_firma_id = '&FIRMA&' and bestellliste_filiale_id = '&FILIALE&' and bestellliste_liste_id = '&ID&'";
        }

        public final String RETRIEVE_LISTE_POS() {
            return "select teilelistepos_sachnr SachNr, teilelistepos_hgug HgUg, teilelistepos_position Pos, teilelistepos_job_id JobId, teilelistepos_srp_id SrpId, teilelistepos_menge Menge, teilelistepos_bemerkung Bemerkung, teilelistepos_benennung Benennung, teilelistepos_zusatz Zusatz, teilelistepos_lagerbestand Lagerbestand, teilelistepos_minimalbestand Minimalbestand, teilelistepos_bedarfshinweis Bedarfshinweis, teilelistepos_lagerort Lagerort, teilelistepos_aume AuMe, teilelistepos_preis Preis, teilelistepos_rabatt Rabatt, teilelistepos_split Splitt, teilelistepos_transparenz Transparenz, teilelistepos_suffix Suffix, teilelistepos_dispocode Dispocode, teilelistepos_ruecksendepfl Ruecksendepflicht, teilelistepos_mwst MwSt, teilelistepos_altteil_steuer AltteilSteuer, teilelistepos_lokalteil Lokalteil, teilelistepos_fistring FiString, teilelistepos_status Status, teilelistepos_pruefen Pruefen, teilelistepos_lock lockflag, teilelistepos_typ AspgTyp, teilelistepos_ref AspgRef, teilelistepos_menge_org AspgMenge, teilm_marke_tps Marke, teil_art Teileart, teil_produktkl ProduktKlasse, teil_mam MAM, NVL(teil_fertigungshinweis, '') FH, teil_mengeeinh Mengeneinheit, NVL(teil_vorverpac, 0) VVM, NVL(teil_lagerverp, 0) LVM, NVL(teil_beh_verp, 0) BVM, teileinfo_sachnr SachnrMitNotiz from w_teilelistepos@etk_nutzer left join w_teileinfo@etk_nutzer on (teilelistepos_sachnr = teileinfo_sachnr and (teilelistepos_user_id = teileinfo_user_id OR teileinfo_allgemein = 'J') and teileinfo_firma_id = teilelistepos_firma_id) left join w_teil@etk_publ on (teilelistepos_sachnr = teil_sachnr) inner join w_teil_marken@etk_publ on (teil_sachnr = teilm_sachnr) where teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&FILIALE&' and teilelistepos_teileliste_id = '&ID&' and teilelistepos_user_id = '&NUTZER&' and NVL(teilelistepos_lokalteil, 'N') = 'N' union select teilelistepos_sachnr SachNr, teilelistepos_hgug HgUg, teilelistepos_position Pos, teilelistepos_job_id JobId, teilelistepos_srp_id SrpId, teilelistepos_menge Menge, teilelistepos_bemerkung Bemerkung, teilelistepos_benennung Benennung, teilelistepos_zusatz Zusatz, teilelistepos_lagerbestand Lagerbestand, teilelistepos_minimalbestand Minimalbestand, teilelistepos_bedarfshinweis Bedarfshinweis, teilelistepos_lagerort Lagerort, teilelistepos_aume AuMe, teilelistepos_preis Preis, teilelistepos_rabatt Rabatt, teilelistepos_split Splitt, teilelistepos_transparenz Transparenz, teilelistepos_suffix Suffix, teilelistepos_dispocode Dispocode, teilelistepos_ruecksendepfl Ruecksendepflicht, teilelistepos_mwst MwSt, teilelistepos_altteil_steuer AltteilSteuer, teilelistepos_lokalteil Lokalteil, teilelistepos_fistring FiString, teilelistepos_status Status, teilelistepos_pruefen Pruefen, teilelistepos_lock lockflag, teilelistepos_typ AspgTyp, teilelistepos_ref AspgRef, teilelistepos_menge_org AspgMenge, '' Marke, '' Teileart, null ProduktKlasse, 0  MAM, null FH, '' Mengeneinheit, 0 VVM, 0 LVM, 0 BVM, null SachnrMitNotiz from w_teilelistepos@etk_nutzer where teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&FILIALE&' and teilelistepos_teileliste_id = '&ID&' and teilelistepos_user_id = '&NUTZER&' and teilelistepos_lokalteil = 'J' order by 4, 5, 3";
        }

        public final String RETRIEVE_MAX_POS() {
            return "select max (teilelistepos_position) Pos from w_teilelistepos@etk_nutzer where teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&FILIALE&' and teilelistepos_teileliste_id = '&ID&' and teilelistepos_user_id = '&NUTZER&'";
        }

        public final String RETRIEVE_BESTELLLISTE_POS() {
            return "select bestelllistepos_sachnr SachNr, bestelllistepos_hgug HgUg, bestelllistepos_position Pos, bestelllistepos_menge Menge, bestelllistepos_bemerkung Bemerkung, bestelllistepos_benennung Benennung, bestelllistepos_zusatz Zusatz, bestelllistepos_lagerbestand Lagerbestand, bestelllistepos_minimalbestand Minimalbestand, bestelllistepos_bedarfshinweis Bedarfshinweis, bestelllistepos_lagerort Lagerort, bestelllistepos_aume AuMe, bestelllistepos_auftragsnr AuftragsNr, bestelllistepos_kundennr KundenNr, bestelllistepos_lokalteil Lokalteil from w_bestelllistepos@etk_nutzer where bestelllistepos_firma_id = '&FIRMA&' and bestelllistepos_filiale_id = '&FILIALE&' and bestelllistepos_liste_id = '&ID&' order by Pos";
        }

        public final String RETRIEVE_TEIL_PREISE() {
            return "select NVL(preise_evpreis, 0) EVP, NVL(preise_nachbelastung, 0) Nachbelastung, preise_rabattschluessel RabattSchluessel, preise_preisaenderung Preisanederung, preise_preis_kz PreisKz, NVL(preise_sonderpreis, 0) SNP, preise_sonderpreis_kz SNPKz, preise_mwst MwSt, preise_mwst_code MwStCode, preise_zolltarifnr ZolltarifNr, NVL(preise_nettopreis, 0) NettoPreis from w_preise@etk_preise where preise_sachnr = '&SACHNR&' and preise_firma = '&FIRMA&'";
        }

        public final String RETRIEVE_TEILELISTE_PREISE() {
            return "select teilelistepos_sachnr Sachnummer_TL, teilelistepos_position Pos, preise_sachnr Sachnummer_Preis, NVL(preise_evpreis, 0) EVP, NVL(preise_nachbelastung, 0) Nachbelastung, preise_rabattschluessel RabattSchluessel, preise_preisaenderung Preisanederung, preise_preis_kz PreisKz, NVL(preise_sonderpreis, 0) SNP, preise_sonderpreis_kz SNPKz, preise_mwst MwSt, preise_mwst_code MwStCode, preise_zolltarifnr ZolltarifNr, NVL(preise_nettopreis, 0) NettoPreis from w_teilelistepos@etk_nutzer left join w_preise@etk_preise on (teilelistepos_sachnr = preise_sachnr and teilelistepos_firma_id = preise_firma) where teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&FILIALE&' and teilelistepos_teileliste_id = '&ID&' and teilelistepos_user_id = '&NUTZER&' order by teilelistepos_position";
        }

        public final String RETRIEVE_TEILELISTE_PUBLDATEN() {
            return "select teil_hauptgr Hg, teil_untergrup Ug, teilelistepos_sachnr Sachnummer_TL, ben_text Benennung, teil_benennzus Zusatz, teilelistepos_position Pos, teil_sachnr Sachnummer_Publdaten, teilm_marke_tps Marke, teil_art Teileart, teil_produktkl ProduktKlasse, teil_mam MAM, teil_mengeeinh Mengeneinheit, NVL(teil_vorverpac, 0) VVM, NVL(teil_lagerverp, 0) LVM, NVL(teil_beh_verp, 0) BVM from w_teilelistepos@etk_nutzer left join w_teil@etk_publ on (teilelistepos_sachnr = teil_sachnr) left join w_ben_gk@etk_publ on (teil_textcode = ben_textcode and  ben_iso = '&ISO&' and ben_regiso = '&REGISO&') inner join w_teil_marken@etk_publ on (teil_sachnr = teilm_sachnr) where teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&FILIALE&' and teilelistepos_teileliste_id = '&ID&' and teilelistepos_user_id = '&NUTZER&' order by teilelistepos_position";
        }

        public final String DELETE() {
            return "delete from w_teileliste@etk_nutzer where teileliste_firma_id = '&FIRMA&' and teileliste_filiale_id = '&FILIALE&' and teileliste_id = '&ID&' and teileliste_user_id = '&NUTZER&'";
        }

        public final String DELETE_JOBS() {
            return "delete from w_teileliste_job@etk_nutzer where teilelistejob_firma_id = '&FIRMA&' and teilelistejob_filiale_id = '&FILIALE&' and teilelistejob_teileliste_id = '&ID&' and teilelistejob_user_id = '&NUTZER&'";
        }

        public final String DELETE_SRPS() {
            return "delete from w_teileliste_srp@etk_nutzer where teilelistesrp_firma_id = '&FIRMA&' and teilelistesrp_filiale_id = '&FILIALE&' and teilelistesrp_teileliste_id = '&ID&' and teilelistesrp_user_id = '&NUTZER&'";
        }

        public final String DELETE_POS() {
            return "delete from w_teilelistepos@etk_nutzer where teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&FILIALE&' and teilelistepos_teileliste_id = '&ID&' and teilelistepos_user_id = '&NUTZER&'";
        }

        public final String INSERT_BESTELLLISTE() {
            return "insert into w_bestellliste@etk_nutzer (bestellliste_firma_id, bestellliste_filiale_id, bestellliste_liste_id) values ('&FIRMAID&', '&FILIALID&', '&ID&')";
        }

        public final String UPDATE_POS() {
            return "update w_teilelistepos@etk_nutzer set teilelistepos_position = teilelistepos_position  - 1 where teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&FILIALE&' and teilelistepos_teileliste_id = '&ID&' and teilelistepos_user_id = '&NUTZER&' and teilelistepos_position > &POS&";
        }

        public final String UPDATE_BESTELL_POS() {
            return "update w_bestelllistepos@etk_nutzer set bestelllistepos_position = bestelllistepos_position  - 1 where bestelllistepos_firma_id = '&FIRMA&' and bestelllistepos_filiale_id = '&FILIALE&' and bestelllistepos_liste_id = '&ID&' and bestelllistepos_position > &POS&";
        }

        public final String DELETE_BESTELLLISTE_POS() {
            return "delete from  w_bestelllistepos@etk_nutzer where bestelllistepos_firma_id = '&FIRMA&' and bestelllistepos_filiale_id = '&FILIALE&' and bestelllistepos_liste_id = '&ID&'";
        }

        public final String DELETE_SINGLE_POS() {
            return "delete from w_teilelistepos@etk_nutzer where teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&FILIALE&' and teilelistepos_teileliste_id = '&ID&' and teilelistepos_user_id = '&NUTZER&' and teilelistepos_position = &POS&";
        }

        public final String DELETE_BESTELLLISTE_SINGLE_POS() {
            return "delete from w_bestelllistepos@etk_nutzer where bestelllistepos_firma_id = '&FIRMA&' and bestelllistepos_filiale_id = '&FILIALE&' and bestelllistepos_liste_id = '&ID&' and bestelllistepos_position = &POS&";
        }

        public final String INSERT_LISTE_ALLG() {
            return "insert into w_teileliste@etk_nutzer (teileliste_firma_id, teileliste_filiale_id, teileliste_id, teileliste_user_id, teileliste_bemerkung, teileliste_erzeugt, teileliste_geaendert, teileliste_marke, teileliste_auftragsnr, teileliste_auftragsnr_lokal, teileliste_kundennr_lokal, teileliste_privat, teileliste_gesperrt, teileliste_fzgdurchlauf, teileliste_dringlichkeit, teileliste_vin, teileliste_rr_sap_status) values ('&FIRMA&', '&FILIALE&', '&ID&', '&NUTZER&', '&BEMERKUNG&', &ERZEUGT&, &GEAENDERT&, '&MARKE&', &AUFTRAGSNR&, &AUFTRAGSNRLOKAL&, &KUNDENNRLOKAL&, &PRIVAT&, &GESPERRT&, &FZGDURCHLAUF&, &DRINGLICHKEIT&, &VIN&, &RRSAPSTATUS&)";
        }

        public final String COPY_LISTE_ALLG() {
            return "insert into w_teileliste@etk_nutzer (teileliste_firma_id, teileliste_filiale_id, teileliste_id, teileliste_user_id, teileliste_bemerkung, teileliste_erzeugt, teileliste_geaendert, teileliste_marke, teileliste_auftragsnr, teileliste_auftragsnr_lokal, teileliste_kundennr_lokal, teileliste_privat, teileliste_gesperrt, teileliste_fzgdurchlauf) (select teileliste_firma_id, '&NEWFILIALE&', '&NEWID&', '&NEWNUTZER&', teileliste_bemerkung, teileliste_erzeugt, teileliste_geaendert, teileliste_marke, teileliste_auftragsnr, teileliste_auftragsnr_lokal, teileliste_kundennr_lokal, teileliste_privat, teileliste_gesperrt, teileliste_fzgdurchlauf from w_teileliste@etk_nutzer where teileliste_id = '&OLDID&' and teileliste_user_id = '&OLDNUTZER&' and teileliste_firma_id = '&FIRMA&' and teileliste_filiale_id = '&OLDFILIALE&')";
        }

        public final String UPDATE_LISTE_ALLG() {
            return "update w_teileliste@etk_nutzer set teileliste_bemerkung = '&BEMERKUNG&', teileliste_erzeugt = &ERZEUGT&, teileliste_geaendert = &GEAENDERT&, teileliste_marke = '&MARKE&', teileliste_auftragsnr = &AUFTRAGSNR&, teileliste_auftragsnr_lokal = &AUFTRAGSNRLOKAL&, teileliste_kundennr_lokal = &KUNDENNRLOKAL&, teileliste_privat = &PRIVAT&, teileliste_dringlichkeit = &DRINGLICHKEIT&, teileliste_vin = &VIN&, teileliste_rr_sap_status = &RRSAPSTATUS& where teileliste_id = '&ID&' and teileliste_user_id = '&NUTZER&' and teileliste_firma_id = '&FIRMA&' and teileliste_filiale_id = '&FILIALE&'";
        }

        public final String UPDATE_LISTE_AENDER_DAT() {
            return "update w_teileliste@etk_nutzer set teileliste_geaendert = &GEAENDERT& where teileliste_id = '&ID&' and teileliste_user_id = '&NUTZER&' and teileliste_firma_id = '&FIRMA&' and teileliste_filiale_id = '&FILIALE&'";
        }

        public final String UPDATE_LISTE_SPERRE() {
            return "update w_teileliste@etk_nutzer set teileliste_gesperrt = &GESPERRT& where teileliste_id = '&ID&' and teileliste_user_id = '&NUTZER&' and teileliste_firma_id = '&FIRMA&' and teileliste_filiale_id = '&FILIALE&'";
        }

        public final String RETRIEVE_LISTE_SPERRE() {
            return "select teileliste_gesperrt_von GesperrtVon, teileliste_gesperrt_am GesperrtAm, teileliste_gesperrt Gesperrt from w_teileliste@etk_nutzer where teileliste_id = '&ID&' and teileliste_user_id = '&LISTE_NUTZER&' and teileliste_firma_id = '&FIRMA&' and teileliste_filiale_id = '&FILIALE&'";
        }

        public final String RETRIEVE_LISTE_SPERRE_FOR_UPDATE() {
            return this.RETRIEVE_LISTE_SPERRE() + " for update";
        }

        public final String UPDATE_LISTE_SPERRE_NUTZER() {
            return " update w_teileliste@etk_nutzer set teileliste_gesperrt_von = &NUTZER&, teileliste_gesperrt_am = &SPERRE_DAT& where teileliste_id = '&ID&' and teileliste_user_id = '&LISTE_NUTZER&' and teileliste_firma_id = '&FIRMA&' and teileliste_filiale_id = '&FILIALE&'";
        }

        public final String INSERT_SENDE_INFO() {
            return "insert into w_teileliste_sendeinfo@etk_nutzer (teilelistesi_teileliste_id, teilelistesi_user_id, teilelistesi_firma_id, teilelistesi_filiale_id, teilelistesi_satzart, teilelistesi_auftragsnr, teilelistesi_kundennr, teilelistesi_mitarbeiternr, teilelistesi_greiferschein, teilelistesi_rechnung, teilelistesi_lieferschein, teilelistesi_freitext, teilelistesi_passwort, teilelistesi_sondersteuerung, teilelistesi_gegeben_bar, teilelistesi_gegeben_unbar) values ('&ID&', '&NUTZER&', '&FIRMA&', '&FILIALE&', '&SATZART&', &AUFTRAGSNR&, &KUNDENNR&, &MITARBEITERNR&, &GREIFERSCHEIN&, &RECHNUNG&, &LIEFERSCHEIN&, &FREITEXT&, &PASSWORT&, &SONDERSTEUERUNG&, &BAR&, &UNBAR&)";
        }

        public final String DELETE_SENDE_INFO() {
            return "delete from w_teileliste_sendeinfo@etk_nutzer where teilelistesi_teileliste_id = '&ID&' and teilelistesi_user_id = '&NUTZER&' and teilelistesi_firma_id = '&FIRMA&' and teilelistesi_filiale_id = '&FILIALE&'";
        }

        public final String RETRIEVE_SENDE_INFO() {
            return "select teilelistesi_satzart Satzart, teilelistesi_auftragsnr AuftragsNr, teilelistesi_kundennr KundenNr, teilelistesi_mitarbeiternr MitarbeiterNr, teilelistesi_greiferschein Greiferschein, teilelistesi_rechnung Rechnung, teilelistesi_lieferschein Lieferschein, teilelistesi_freitext Freitext, teilelistesi_passwort Passwort, teilelistesi_sondersteuerung Sondersteuerung, teilelistesi_gegeben_bar Bar, teilelistesi_gegeben_unbar Unbar from w_teileliste_sendeinfo@etk_nutzer where teilelistesi_teileliste_id = '&ID&' and teilelistesi_user_id = '&NUTZER&' and teilelistesi_firma_id = '&FIRMA&' and teilelistesi_filiale_id = '&FILIALE&'";
        }

        public final String RETRIEVE_BESTELLLISTE_SPERRE_FOR_UPDATE() {
            return "select bestellliste_gesperrt_von GesperrtVon, bestellliste_gesperrt_am GesperrtAm from w_bestellliste@etk_nutzer where bestellliste_firma_id = '&FIRMA&' and bestellliste_filiale_id = '&FILIALE&' and bestellliste_liste_id = '&ID&' for update";
        }

        public final String UPDATE_BESTELLLISTE_SPERRE() {
            return " update w_bestellliste@etk_nutzer set bestellliste_gesperrt_von = &NUTZER&, bestellliste_gesperrt_am = &SPERRE_DAT& where bestellliste_liste_id = '&ID&' and bestellliste_firma_id = '&FIRMA&' and bestellliste_filiale_id = '&FILIALE&'";
        }

        public final String INSERT_LISTE_POS() {
            return "insert into w_teilelistepos@etk_nutzer (teilelistepos_firma_id, teilelistepos_filiale_id, teilelistepos_teileliste_id, teilelistepos_user_id, teilelistepos_position, teilelistepos_hgug, teilelistepos_sachnr, teilelistepos_benennung, teilelistepos_zusatz, teilelistepos_menge, teilelistepos_lagerbestand, teilelistepos_minimalbestand, teilelistepos_bedarfshinweis, teilelistepos_lagerort, teilelistepos_aume, teilelistepos_preis, teilelistepos_rabatt, teilelistepos_split, teilelistepos_transparenz, teilelistepos_suffix, teilelistepos_dispocode, teilelistepos_ruecksendepfl, teilelistepos_mwst, teilelistepos_altteil_steuer, teilelistepos_lokalteil, teilelistepos_bemerkung, teilelistepos_fistring, teilelistepos_job_id, teilelistepos_srp_id, teilelistepos_status, teilelistepos_pruefen, teilelistepos_lock,  teilelistepos_typ, teilelistepos_ref, teilelistepos_menge_org) values ('&FIRMA&', '&FILIALE&', '&ID&', '&NUTZER&', &POS&, '&HGUG&', '&SACHNR&', '&BENENNUNG&', '&ZUSATZ&', &MENGE&, &LAGERBESTAND&, &MINIMALBESTAND&, &BEDARFSHINWEIS&, '&LAGERORT&', &AUME&, &PREIS&, &RABATT&, '&SPLIT&', '&TRANSPARENZ&', '&SUFFIX&', '&DISPO&', '&RUECKSENDEPFLICHT&', &MWST&, &ATST&, &LOKALTEIL&, '&BEMERKUNG&', &FISTRING&, &JOBID&, &SRPID&, &STATUS&, &PRUEFEN&, &LOCK&, '&ASPGTYP&', '&ASPGREF&', &ASPGMENGE&)";
        }

        public final String COPY_LISTE_POSITIONEN() {
            return "insert into w_teilelistepos@etk_nutzer (teilelistepos_firma_id, teilelistepos_filiale_id, teilelistepos_teileliste_id, teilelistepos_user_id, teilelistepos_position, teilelistepos_hgug, teilelistepos_sachnr, teilelistepos_benennung, teilelistepos_zusatz, teilelistepos_menge, teilelistepos_lagerbestand, teilelistepos_minimalbestand, teilelistepos_bedarfshinweis, teilelistepos_lagerort, teilelistepos_aume, teilelistepos_preis, teilelistepos_rabatt, teilelistepos_split, teilelistepos_transparenz, teilelistepos_suffix, teilelistepos_dispocode, teilelistepos_ruecksendepfl, teilelistepos_mwst, teilelistepos_altteil_steuer, teilelistepos_lokalteil, teilelistepos_bemerkung, teilelistepos_job_id, teilelistepos_srp_id, teilelistepos_status, teilelistepos_pruefen, teilelistepos_lock) (select teilelistepos_firma_id, '&NEWFILIALE&', '&NEWID&', '&NEWNUTZER&', teilelistepos_position, teilelistepos_hgug, teilelistepos_sachnr, teilelistepos_benennung, teilelistepos_zusatz, teilelistepos_menge, teilelistepos_lagerbestand, teilelistepos_minimalbestand, teilelistepos_bedarfshinweis, teilelistepos_lagerort, teilelistepos_aume, teilelistepos_preis, teilelistepos_rabatt, teilelistepos_split, teilelistepos_transparenz, teilelistepos_suffix, teilelistepos_dispocode, teilelistepos_ruecksendepfl, teilelistepos_mwst, teilelistepos_altteil_steuer, teilelistepos_lokalteil, teilelistepos_bemerkung, teilelistepos_job_id, teilelistepos_srp_id, teilelistepos_status, teilelistepos_pruefen, teilelistepos_lock from w_teilelistepos@etk_nutzer where teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&OLDFILIALE&' and teilelistepos_user_id = '&OLDNUTZER&' and teilelistepos_teileliste_id = '&OLDID&')";
        }

        public final String INSERT_BESTELLLISTE_POS() {
            return "insert into w_bestelllistepos@etk_nutzer (bestelllistepos_firma_id, bestelllistepos_filiale_id, bestelllistepos_liste_id, bestelllistepos_position, bestelllistepos_hgug, bestelllistepos_sachnr, bestelllistepos_benennung, bestelllistepos_zusatz, bestelllistepos_menge, bestelllistepos_lagerbestand, bestelllistepos_minimalbestand, bestelllistepos_bedarfshinweis, bestelllistepos_lagerort, bestelllistepos_aume, bestelllistepos_auftragsnr, bestelllistepos_kundennr, bestelllistepos_lokalteil, bestelllistepos_bemerkung) values ('&FIRMA&', '&FILIALE&', '&ID&', &POS&, '&HGUG&', '&SACHNR&', '&BENENNUNG&', '&ZUSATZ&', &MENGE&, &LAGERBESTAND&, &MINIMALBESTAND&, &BEDARFSHINWEIS&, '&LAGERORT&', &AUME&, &AUFTRAGSNR&, &KUNDENNR&, &LOKALTEIL&, '&BEMERKUNG&')";
        }

        public final String RETRIEVE_COUNT_BESTELLLISTEPOS() {
            return "select count(*) from w_bestelllistepos@etk_nutzer where bestelllistepos_firma_id = '&FIRMA&' and bestelllistepos_filiale_id = '&FILIALE&' and bestelllistepos_liste_id = '&ID&'";
        }

        public final String UPDATE_LISTE_POS() {
            return "update w_teilelistepos@etk_nutzer set teilelistepos_hgug = '&HGUG&', teilelistepos_sachnr = '&SACHNR&', teilelistepos_benennung = '&BENENNUNG&', teilelistepos_zusatz = '&ZUSATZ&', teilelistepos_menge = &MENGE&, teilelistepos_lagerbestand = &LAGERBESTAND&, teilelistepos_minimalbestand = &MINIMALBESTAND&, teilelistepos_bedarfshinweis = &BEDARFSHINWEIS&, teilelistepos_lagerort = '&LAGERORT&', teilelistepos_aume = &AUME&, teilelistepos_preis = &PREIS&, teilelistepos_rabatt = &RABATT&, teilelistepos_split = '&SPLIT&', teilelistepos_transparenz = '&TRANSPARENZ&', teilelistepos_suffix = '&SUFFIX&', teilelistepos_dispocode = '&DISPO&', teilelistepos_ruecksendepfl = '&RUECKSENDEPFLICHT&', teilelistepos_mwst = &MWST&, teilelistepos_altteil_steuer = &ATST&, teilelistepos_lokalteil = &LOKALTEIL&, teilelistepos_fistring = &FISTRING&, teilelistepos_bemerkung = '&BEMERKUNG&', teilelistepos_job_id = &JOBID&, teilelistepos_srp_id = &SRPID&, teilelistepos_status = &STATUS&, teilelistepos_pruefen = &PRUEFEN&, teilelistepos_lock = &LOCK& where teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&FILIALE&' and teilelistepos_user_id = '&NUTZER&' and teilelistepos_teileliste_id = '&ID&' and teilelistepos_position = &POS&";
        }

        public final String UPDATE_SCORELISTE_POS_STATUS() {
            return "update w_teilelistepos@etk_nutzer set teilelistepos_status = &STATUS& where teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&FILIALE&' and teilelistepos_user_id = '&NUTZER&' and teilelistepos_teileliste_id = '&ID&' and teilelistepos_position = &POS&";
        }

        public final String UPDATE_BESTELLLISTE_POS() {
            return "update w_bestelllistepos@etk_nutzer set bestelllistepos_hgug = '&HGUG&', bestelllistepos_sachnr = '&SACHNR&', bestelllistepos_benennung = '&BENENNUNG&', bestelllistepos_zusatz = '&ZUSATZ&', bestelllistepos_menge = &MENGE&, bestelllistepos_lagerbestand = &LAGERBESTAND&, bestelllistepos_minimalbestand = &MINIMALBESTAND&, bestelllistepos_bedarfshinweis = &BEDARFSHINWEIS&, bestelllistepos_lagerort = '&LAGERORT&', bestelllistepos_aume = &AUME&, bestelllistepos_auftragsnr = &AUFTRAGSNR&, bestelllistepos_kundennr = &KUNDENNR&, bestelllistepos_lokalteil = &LOKALTEIL&, bestelllistepos_bemerkung = '&BEMERKUNG&' where bestelllistepos_firma_id = '&FIRMA&' and bestelllistepos_filiale_id = '&FILIALE&' and bestelllistepos_liste_id = '&ID&' and bestelllistepos_position = &POS&";
        }

        public final String GET_TEILELISTE_ZU_AUFTRAG() {
            return "select teileliste_id TeilelisteId, teileliste_user_id EigentuemerId, teileliste_gesperrt Gesperrt, auftrag_kundennr Kundennummer, auftrag_kundenname Kundenname, auftrag_fgstnr Fahrgestellnummer, auftrag_auftragsnr Auftragsnummer from w_teileliste@etk_nutzer left join w_auftrag@etk_nutzer on (teileliste_firma_id = auftrag_firma_id and  teileliste_filiale_id = auftrag_filiale_id and teileliste_auftragsnr = auftrag_auftragsnr) where teileliste_firma_id = '&FIRMA&' and teileliste_filiale_id = '&FILIALE&' and teileliste_id = '&AUFTRAGSNUMMER&'";
        }

        public final String INSERT_AUFTRAG() {
            return "insert into w_auftrag@etk_nutzer (auftrag_firma_id, auftrag_filiale_id, auftrag_auftragsnr, auftrag_kundennr, auftrag_kundenname, auftrag_fgstnr) values ('&FIRMA&', '&FILIALE&', '&AUFTRAGSNUMMER&', &KUNDENNUMMER&, &KUNDENNAME&, &FGSTNR&)";
        }

        public final String UPDATE_AUFTRAG() {
            return "update w_auftrag@etk_nutzer  set auftrag_kundennr = &KUNDENNUMMER&, auftrag_kundenname = &KUNDENNAME&, auftrag_fgstnr = &FGSTNR& where auftrag_firma_id = '&FIRMA&' and auftrag_filiale_id = '&FILIALE&' and auftrag_auftragsnr = '&AUFTRAGSNUMMER&'";
        }

        public final String DELETE_AUFTRAG() {
            return "delete from w_auftrag@etk_nutzer where auftrag_firma_id = '&FIRMA&' and   auftrag_filiale_id = '&FILIALE&' and   auftrag_auftragsnr = '&AUFTRAGSNUMMER&'";
        }

        public final String UPDATE_EIGENTUEMER() {
            return " update w_teileliste@etk_nutzer set teileliste_user_id = '&NEWNUTZER&' where teileliste_id = '&ID&' and teileliste_user_id = '&NUTZER&' and teileliste_firma_id = '&FIRMA&' and teileliste_filiale_id = '&FILIALE&'";
        }

        public final String UPDATE_EIGENTUEMER_POS() {
            return " update w_teilelistepos@etk_nutzer set teilelistepos_user_id = '&NEWNUTZER&' where teilelistepos_teileliste_id = '&ID&' and teilelistepos_user_id = '&NUTZER&' and teilelistepos_firma_id = '&FIRMA&' and teilelistepos_filiale_id = '&FILIALE&'";
        }

        public final String INSERT_JOB() {
            return "insert into w_teileliste_job@etk_nutzer (teilelistejob_firma_id, teilelistejob_filiale_id, teilelistejob_teileliste_id, teilelistejob_user_id, teilelistejob_job_id, teilelistejob_job_ben, teilelistejob_lock) values ('&FIRMA&', '&FILIALE&', '&ID&', '&NUTZER&', '&JOBID&', '&JOBBEN&', '&LOCK&')";
        }

        public final String INSERT_SRP() {
            return "insert into w_teileliste_srp@etk_nutzer (teilelistesrp_firma_id, teilelistesrp_filiale_id, teilelistesrp_teileliste_id, teilelistesrp_user_id, teilelistesrp_srp_id, teilelistesrp_job_id, teilelistesrp_srp_ben, teilelistesrp_lock, teilelistesrp_quelle) values ('&FIRMA&', '&FILIALE&', '&ID&', '&NUTZER&', '&SRPID&', '&JOBID&', '&SRPBEN&', '&LOCK&', &QUELLE&)";
        }

        public final String RETRIEVE_JOBS() {
            return "select teilelistejob_job_id id, teilelistejob_job_ben ben, teilelistejob_lock lockflag from w_teileliste_job@etk_nutzer WHERE teilelistejob_firma_id = '&FIRMA&' and teilelistejob_filiale_id = '&FILIALE&' and teilelistejob_teileliste_id = '&ID&' and teilelistejob_user_id = '&NUTZER&'";
        }

        public final String RETRIEVE_SRPS() {
            return "select teilelistesrp_srp_id id, teilelistesrp_srp_ben ben, teilelistesrp_job_id jobid, teilelistesrp_lock lockflag, teilelistesrp_quelle quelle from w_teileliste_srp@etk_nutzer WHERE teilelistesrp_firma_id = '&FIRMA&' and teilelistesrp_filiale_id = '&FILIALE&' and teilelistesrp_teileliste_id = '&ID&' and teilelistesrp_user_id = '&NUTZER&'";
        }

        public final String GET_NEXT_ID_SEQ_VAL() {
            return " select teileliste_id_seq.nextval from teileliste_id_seq";
        }

        public final String GET_NEXT_SCOREID_SEQ_VAL() {
            return " select teileliste_score_id_seq.nextval from teileliste_score_id_seq";
        }

        public final String GET_NEXT_RRSAPID_SEQ_VAL() {
            return " select teileliste_rrsap_id_seq.nextval from teileliste_rrsap_id_seq";
        }

        public final String GET_RELEASED_SCORE_LISTS() {
            return "select teileliste_fzgdurchlauf fzgDlfID, teileliste_firma_id firmaId, teileliste_filiale_id filialeId, teileliste_id listeId, teileliste_geaendert geaendert, teileliste_auftragsnr auftragsnr from w_teileliste where teileliste_firma_id = '&FIRMA&' and teileliste_fzgdurchlauf is not null and teileliste_user_id = 'score' ORDER BY fzgDlfID, geaendert, listeId";
        }

        public final String LOAD_TC_INFO() {
            return "select distinct tcp_proddat_rel Teil_TC_ProdDatRelevant from w_tc_performance where tcp_mospid = &MOSP&   and tcp_sachnr = '&SACHNR&'   &TC_CHECK_LANDKUERZEL&    and tcp_datum_von <= &DATUM&    and (tcp_datum_bis is null or tcp_datum_bis >= &DATUM&)";
        }

        public final String TC_CHECK_LANDKUERZEL() {
            return " and tcp_landkuerzel = '&LANDKUERZEL&'";
        }
    }

    public static final class TeilelisteHTML_SAP
    extends SQLStatements.TeilelisteHTML_SAP {
        public final String LOAD_TEILELISTE_RR() {
            return "nur fuer oracle";
        }

        public final String LOAD_TEILELISTE_POS_RR() {
            return "nur fuer oracle";
        }

        public final String DELETE_TEILELISTE_RR() {
            return "nur fuer oracle";
        }

        public final String DELETE_TEILELISTEPOS_RR() {
            return "nur fuer oracle";
        }

        public final String INSERT_TEILELISTE_RR() {
            return "nur fuer oracle";
        }

        public final String LOAD_TEILELISTE_HIST() {
            return "nur fuer oracle";
        }

        public final String DELETE_TEILELISTE_HIST() {
            return "nur fuer oracle";
        }

        public final String INSERT_TEILELISTE_HIST() {
            return "nur fuer oracle";
        }

        public final String GET_NEXT_ABFRAGE_SEQ_VAL() {
            return "nur fuer oracle";
        }
    }

    public static final class TeilelisteHTML
    extends SQLStatements.TeilelisteHTML {
        public final String RETRIEVE_LISTEN_IDS() {
            return "select teileliste_marke Marke,  teileliste_filiale_id Filiale, teileliste_user_id Eigentuemer, teileliste_id ListeId  teileliste_user_id = '&NUTZER&' and teileliste_marke IN (&MARKEN&)";
        }

        public final String RETRIEVE_LISTE_ALLG() {
            return "select teileliste_bemerkung Bemerkung, teileliste_erzeugt Erzeugt, teileliste_geaendert Geaendert, teileliste_marke Marke from w_teileliste@etk_nutzer where teileliste_id = '&ID&' and teileliste_user_id = '&NUTZER&'";
        }

        public final String RETRIEVE_LISTE_POS() {
            return "select teilelistepos_sachnr SachNr, teil_hauptgr Hg, teil_untergrup Ug, teilelistepos_position Pos, teilelistepos_menge Menge, teilelistepos_bemerkung Bemerkung, ben_text Benennung, teil_benennzus Zusatz from w_teilelistepos@etk_nutzer, w_ben_gk, w_teil where teilelistepos_teileliste_id = '&ID&' and teilelistepos_user_id = '&NUTZER&' and teilelistepos_sachnr = teil_sachnr and teil_textcode = ben_textcode and ben_iso = '&ISO&' and  ben_regiso = '&REGISO&' order by Pos";
        }

        public final String DELETE() {
            return "delete from w_teileliste@etk_nutzer where teileliste_id = '&ID&' and teileliste_user_id = '&NUTZER&'";
        }

        public final String DELETE_POS() {
            return "delete from w_teilelistepos@etk_nutzer where teilelistepos_teileliste_id = '&ID&' and teilelistepos_user_id = '&NUTZER&'";
        }

        public final String INSERT_LISTE_ALLG() {
            return "insert into w_teileliste@etk_nutzer (teileliste_id, teileliste_user_id, teileliste_bemerkung, teileliste_erzeugt, teileliste_geaendert, teileliste_marke) values ('&ID&', '&NUTZER&', '&BEMERKUNG&', &ERZEUGT&, &GEAENDERT&, '&MARKE&')";
        }

        public final String UPDATE_LISTE_ALLG() {
            return "update w_teileliste@etk_nutzer set teileliste_bemerkung = '&BEMERKUNG&', teileliste_erzeugt = &ERZEUGT&, teileliste_geaendert = &GEAENDERT&, teileliste_marke = '&MARKE&' where teileliste_id = '&ID&' and teileliste_user_id = '&NUTZER&'";
        }

        public final String INSERT_LISTE_POS() {
            return "insert into w_teilelistepos@etk_nutzer (teilelistepos_teileliste_id, teilelistepos_user_id, teilelistepos_sachnr, teilelistepos_position, teilelistepos_menge, teilelistepos_bemerkung) values ('&ID&', '&NUTZER&', '&SACHNR&', &POS&, '&MENGE&', '&BEMERKUNG&')";
        }

        public final String RETRIEVE_MAILADRESSEN() {
            return "select mailadr_mailadresse AnMailAdresse from w_mailadressen where mailadr_marke_tps IN (&MARKEN&)";
        }

        public final String GET_NEXT_SAMMELID_SEQ_VAL() {
            return "nur fuer oracle";
        }

        public String INSERT_BESTELLLISTE() {
            return "nur fuer oracle";
        }

        public String CHECK_TEILELISTE() {
            return "nur fuer oracle";
        }
    }

    public static final class Teileliste
    extends SQLStatements.Teileliste {
        public final String RETRIEVE_TEIL() {
            return "select teil_hauptgr Hg,  teil_untergrup Ug,  ben_text Benennung,  teil_benennzus Zusatz, teilm_marke_tps Marke,  teil_art Teileart,  teil_produktkl ProduktKlasse, teil_mam MAM,  teil_mengeeinh Mengeneinheit, NVL(teil_vorverpac, 0) VVM,  NVL(teil_lagerverp, 0) LVM,  NVL(teil_beh_verp, 0) BVM, teil_fertigungshinweis FH,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_teil, w_teil_marken, w_ben_gk  where teil_sachnr = '&SACHNR&' and teil_textcode = ben_textcode  and ben_regiso = '&REGISO&'  and ben_iso = '&ISO&' and teil_sachnr = teilm_sachnr";
        }
    }

    public static final class BteAnzeige
    extends SQLStatements.BteAnzeige {
        public final String LOAD_HOTSPOTS() {
            return "select grafikhs_bildposnr Bildnummer, grafikhs_topleft_x TopLeft_x, grafikhs_topleft_y TopLeft_y, grafikhs_bottomright_x BottomRight_x, grafikhs_bottomright_y BottomRight_y from w_grafik_hs where grafikhs_grafikid = &GRAFIKID& and grafikhs_art = '&ART&'";
        }

        public final String LOAD_BTZEILEN_FZG() {
            return "select distinct btzeilen_bildposnr Bildnummer, teil_hauptgr Teil_HG, teil_untergrup Teil_UG, teil_sachnr Teil_Sachnummer, tben.ben_text Teil_Benennung, teil_benennzus Teil_Zusatz, teil_entfall_kez Teil_Entfall, teil_textcode_kom Teil_Kommentar_Id, tkben.ben_text Teil_Kommentar, teil_kom_pi Teil_Komm_PI, teil_vorhanden_si Teil_SI, teil_ist_reach Teil_Reach,  teil_ist_aspg Teil_Aspg,  teil_ist_stecker Teil_Stecker,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant,  si_dokart SI_DokArt, decode(tcp_sachnr, tcp_sachnr, 'C', NULL) Teil_TC, NVL(tcp_proddat_rel, 'N') Teil_TC_ProdDatRelevant, grpinfo_leitaw_pa GRP_PA, grpinfo_leitaw_hg GRP_HG, grpinfo_leitaw_ug GRP_UG, grpinfo_leitaw_nr GRP_lfdNr, btzeilenv_vmenge Menge, btzeilen_kat Kat_KZ, btzeilen_automatik Getriebe_KZ, btzeilen_lenkg Lenkung_KZ, btzeilen_eins Einsatz, btzeilen_auslf Auslauf, btzeilen_bedkez || nvl(to_char(btzeilen_regelnr), '') Bedingung_KZ, btzeilen_kommbt KommBT, btzeilen_kommvor KommVor, btzeilen_kommnach KommNach, ks_sachnr_satz Satz_Sachnummer,  btzeilen_gruppeid GruppeId,  btzeilen_blocknr BlockNr,  bnbben.ben_text BnbBenText, btzeilen_pos Pos, btzeilenv_alter_kz BtZAlter, btzeilen_bedkez_pg Teil_BedkezPG, btzeilenv_bed_art BedingungArt, btzeilenv_bed_alter BedingungAlter from w_btzeilen_verbauung inner join w_btzeilen on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos) inner join w_teil on (btzeilen_sachnr = teil_sachnr) inner join w_ben_gk tben on (teil_textcode = tben.ben_textcode and tben.ben_iso = '&ISO&' and tben.ben_regiso = '&REGISO&') left join w_kompl_satz on (btzeilen_sachnr = ks_sachnr_satz and ks_marke_tps = '&MARKE&') left join w_tc_performance on (tcp_mospid = &MOSP& and tcp_sachnr = btzeilen_sachnr &TC_CHECK_LANDKUERZEL& and tcp_datum_von <= &DATUM& and (tcp_datum_bis is null or tcp_datum_bis >= &DATUM&)) left join w_grp_information on (btzeilenv_mospid = grpinfo_mospid and grpinfo_sachnr = btzeilen_sachnr and grpinfo_typ = '&TYP&') left join w_ben_gk tkben on (teil_textcode_kom = tkben.ben_textcode and tkben.ben_iso = '&ISO&' and tkben.ben_regiso = '&REGISO&') left join w_si on (si_sachnr = teil_sachnr) left join w_bildtaf_bnbben on (bildtafb_btnr = btzeilenv_btnr and bildtafb_bildposnr = btzeilen_bildposnr) left join w_ben_gk bnbben on (bildtafb_textcode = bnbben.ben_textcode and bnbben.ben_iso = '&ISO&' and bnbben.ben_regiso = '&REGISO&') where btzeilenv_mospid = &MOSP& and btzeilenv_btnr = '&BTNR&' order by Pos, GRP_PA, GRP_HG, GRP_UG, GRP_lfdNr, SI_DokArt";
        }

        public final String LOAD_BTZEILEN_CP_FZG() {
            return "select distinct btzeilenc_pos Pos, btzeilenc_typschl Typ, btzeilenc_werk Werk, btzeilenc_art Art, btzeilenc_datum Datum, btzeilenc_vin Vin, btzeilenc_vin_proddatum VinProddatum, btzeilenc_vin_min VinMin, btzeilenc_vin_max VinMax, btzeilenc_nart ArtNummer, btzeilenc_nummer Nummer, btzeilenc_alter CPAlter from w_btzeilen_cp where btzeilenc_mospid = &MOSP& and btzeilenc_btnr = '&BTNR&' &CP_FZG_TYP_WERK& order by Pos";
        }

        public final String CP_FZG_TYP_WERK() {
            return "   and btzeilenc_typschl = '&TYP&' and btzeilenc_werk = '&WERK&'";
        }

        public final String LOAD_BTZEILEN_UGB() {
            return "select distinct btzeilenu_bildposnr Bildnummer, teil_hauptgr Teil_HG, teil_untergrup Teil_UG, teil_sachnr Teil_Sachnummer, tben.ben_text Teil_Benennung, teil_benennzus Teil_Zusatz, teil_entfall_kez Teil_Entfall, teil_textcode_kom Teil_Kommentar_Id, tkben.ben_text Teil_Kommentar, teil_kom_pi Teil_Komm_PI, teil_vorhanden_si Teil_SI, teil_ist_reach Teil_Reach,  teil_ist_aspg Teil_Aspg,  teil_ist_stecker Teil_Stecker,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant,  si_dokart SI_DokArt, decode(teil_sachnr, tcp_sachnr, 'C', NULL) Teil_TC, 'N' Teil_TC_ProdDatRelevant, btzeilenu_mmg MMG, btzeilenu_emg EMG, btzeilenu_eins Einsatz, btzeilenu_ausl Auslauf, btzeilenu_kommbt KommBT, btzeilenu_kommvor KommVor, btzeilenu_kommnach KommNach, ks_sachnr_satz Satz_Sachnummer,  0 GruppeId,  0 BlockNr,  bnbben.ben_text BnbBenText, btzeilenu_pos Pos from w_btzeilenugb_verbauung inner join w_btzeilenugb on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos) inner join w_teil on (btzeilenu_sachnr = teil_sachnr) inner join w_ben_gk tben on (teil_textcode = tben.ben_textcode and tben.ben_iso = '&ISO&' and tben.ben_regiso = '&REGISO&') left join w_kompl_satz on (btzeilenu_sachnr = ks_sachnr_satz and ks_marke_tps = '&MARKE&') left join w_tc_performance_allg on (btzeilenu_sachnr = tcp_sachnr                                 and tcp_marke_tps = '&MARKE&'                                  and tcp_produktart = '&PRODUKTART&'                                 and tcp_vbereich = '&KATALOGUMFANG&'                                     &TC_CHECK_LANDKUERZEL&                                 and tcp_datum_von <= &DATUM&                                 and (tcp_datum_bis is null or tcp_datum_bis <= &DATUM&)) left join w_ben_gk tkben on (teil_textcode_kom = tkben.ben_textcode and tkben.ben_iso = '&ISO&' and tkben.ben_regiso = '&REGISO&') left join w_si on (si_sachnr = teil_sachnr) left join w_bildtaf_bnbben on (bildtafb_btnr = btzeilenu_btnr and bildtafb_bildposnr = btzeilenu_bildposnr) left join w_ben_gk bnbben on (bildtafb_textcode = bnbben.ben_textcode and bnbben.ben_iso = '&ISO&' and bnbben.ben_regiso = '&REGISO&') where btzeilenuv_marke_tps = '&MARKE&' and btzeilenuv_btnr = '&BTNR&' order by Pos";
        }

        public final String TC_CHECK_LANDKUERZEL() {
            return " and tcp_landkuerzel = '&LANDKUERZEL&'";
        }

        public final String LOAD_KOMMENTARE_FZG() {
            return "select distinct komm_id KommId, ben_text Text, komm_code Code, komm_vz VZ, komm_darstellung Darstellung, komm_tiefe Tiefe, komm_pos Pos from w_komm_help, w_ben_gk, w_komm where kommh_mospid = &MOSP& and kommh_btnr = '&BTNR&' and kommh_id = komm_id and komm_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by KommId, Pos";
        }

        public final String LOAD_KOMMENTARE_UGB() {
            return "select distinct komm_id KommId, ben_text Text, komm_pos Pos from w_kommugb_help, w_ben_gk, w_komm where kommuh_marke_tps = '&MARKE&' and kommuh_btnr = '&BTNR&' and kommuh_id = komm_id and komm_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by KommId, Pos";
        }

        public final String LOAD_BEDINGUNGEN_FZG() {
            return "select distinct btebg_kez Kuerzel, btebg_vz  GesamttermVZ, btebg_gesamtterm Gesamtterm, btebo_ogid OG, btebo_vart VArt, btebo_fzeile FZeile, btebe_vz ElementVZ, btebe_elemid ElementId, btebe_pos Pos from w_bte_bedkurz, w_bte_bedelem, w_bte_bedog, w_bte_bedgesamt where btebk_btnr = '&BTNR&' and btebk_mospid = &MOSP& and btebk_btnr = btebg_btnr and btebk_kez = btebg_kez  and btebg_btnr = btebo_btnr and btebg_kez = btebo_kez and btebo_btnr = btebe_btnr and btebo_kez = btebe_kez and btebo_ogid = btebe_ogid union select distinct btebg_kez Kuerzel, btebg_vz  GesamttermVZ, btebg_gesamtterm Gesamtterm, btebo_ogid OG, btebo_vart VArt, btebo_fzeile FZeile, btebe_vz ElementVZ, btebe_elemid ElementId, btebe_pos Pos from w_bildtaf, w_bte_bedelem, w_bte_bedog, w_bte_bedgesamt where bildtaf_btnr = '&BTNR&' and bildtaf_bedkez is not null and bildtaf_btnr = btebg_btnr  and bildtaf_bedkez = btebg_kez and btebg_btnr = btebo_btnr and btebg_kez = btebo_kez and btebo_btnr = btebe_btnr and btebo_kez = btebe_kez and btebo_ogid = btebe_ogid order by Kuerzel, OG, Pos";
        }

        public final String LOAD_BTE_BEDINGUNGEN_FZG() {
            return " select distinct btebg_vz  GesamttermVZ, btebg_gesamtterm Gesamtterm, btebo_ogid OG, btebo_vart VArt, btebo_fzeile FZeile, btebe_vz ElementVZ, btebe_elemid ElementId, btebe_pos Pos from w_bte_bedelem, w_bte_bedog, w_bte_bedgesamt where btebg_btnr = '&BTNR&' and btebg_kez = '&BEDKEZ&' and btebo_btnr = btebg_btnr and btebo_kez = btebg_kez  and btebe_btnr = btebg_btnr and btebe_kez = btebg_kez and btebe_ogid = btebo_ogid order by OG, Pos";
        }

        public final String LOAD_UEBERBEDINGUNGEN_FZG() {
            return "select distinct btebu_kez Kuerzel, btebu_kezueber  KuerzelUeber from w_bte_bedkurz, w_bte_bedueber  where btebk_btnr = '&BTNR&' and btebk_mospid = &MOSP& and btebu_btnr = btebk_btnr and btebu_kez =  btebk_kez union select distinct btebu_kez Kuerzel, btebu_kezueber  KuerzelUeber from w_bte_bedkurz, w_bte_bedueber  where btebk_btnr = '&BTNR&' and btebk_mospid = &MOSP& and btebu_btnr = btebk_btnr and btebu_kezueber = btebk_kez order by Kuerzel, KuerzelUeber";
        }

        public final String LOAD_BTVERWEISE_FZG() {
            return "select distinct bv_btnr_nach Bildtafelnummer, bt.ben_text Ueberschrift, bv.ben_text Text, komm_pos Pos from w_bildtaf_verweis, w_ben_gk bt, w_ben_gk bv, w_komm, w_bildtaf, w_bildtaf_suche where bv_btnr_von = '&BTNR&' and bildtafs_hg = substr(bv_btnr_nach, 1, 2) and bildtafs_mospid = &MOSP& and bildtafs_btnr = bv_btnr_nach and bv_kommid = komm_id and komm_textcode = bv.ben_textcode and bv.ben_iso = '&ISO&' and bv.ben_regiso = '&REGISO&' and bv_btnr_nach = bildtaf_btnr and bildtaf_textc = bt.ben_textcode and bt.ben_iso = '&ISO&' and bt.ben_regiso = '&REGISO&' order by Bildtafelnummer, Pos";
        }

        public final String LOAD_BTVERWEISE_UGB() {
            return "select distinct bv_btnr_nach Bildtafelnummer, bt.ben_text Ueberschrift, bv.ben_text Text, komm_pos Pos from w_bildtaf_verweis, w_ben_gk bt, w_ben_gk bv, w_komm, w_bildtaf, w_btzeilenugb_verbauung where bv_btnr_von = '&BTNR&' and bv_btnr_nach = btzeilenuv_btnr and btzeilenuv_marke_tps = '&MARKE&' and bv_kommid = komm_id and komm_textcode = bv.ben_textcode and bv.ben_iso = '&ISO&' and bv.ben_regiso = '&REGISO&' and bv_btnr_nach = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_textc = bt.ben_textcode and bt.ben_iso = '&ISO&' and bt.ben_regiso = '&REGISO&' order by Bildtafelnummer, Pos";
        }

        public final String LOAD_JA_NEIN_TEXT() {
            return "select distinct publben_bezeichnung Bezeichnung, ben_text Benennung from w_publben, w_ben_gk where publben_art = 'V' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Bezeichnung";
        }

        public final String LOAD_AZEICHEN() {
            return "select distinct teilatb_kennz ATB,              teilatb_bap BAP from w_teil_atb where teilatb_sachnr_alt = '&SNR1&' and teilatb_sachnr_neu = '&SNR2&'";
        }

        public final String LOAD_ANZAHL_REL_KAMPAGNEN() {
            return "select Count(*) Anzahl from w_tc_sachnummer, w_tc_kampagne_proddatum, w_tc_kampagne where tckp_mospid = &MOSPID& and tckp_proddatum_von <= &PRODDATUM_MAX& and nvl(tckp_proddatum_bis, 99999999) >= &PRODDATUM_MIN& and tcs_id = tckp_id and tcs_sachnr = '&SACHNUMMER&' and tck_id = tcs_id &TC_CHECK_LANDKUERZEL_KAMPAGNE&";
        }

        public final String TC_CHECK_LANDKUERZEL_KAMPAGNE() {
            return " and tck_landkuerzel = '&LANDKUERZEL&'";
        }
    }

    public static final class BedAuswertung
    extends SQLStatements.BedAuswertung {
        public final String LOAD_BT_STAMM() {
            return "select distinct bildtaf_hg HG, bildtaf_fg FG, bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_bedkez BedKuerzel, bildtaf_vorh_cp VorhandenCP, bildtaf_lkz Lkz, bildtaf_grafikid GrafikId, grafik_moddate ModStamp, bildtafz_btnr ZubBtnr from w_bildtaf left join w_grafik on (bildtaf_grafikid = grafik_grafikid) left join w_bildtafzub on (bildtaf_btnr = bildtafz_btnr) inner join w_ben_gk on (bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') where bildtaf_btnr = '&BTNR&'";
        }

        public final String LOAD_BT_STAMM_CP() {
            return "select distinct bildtafc_art Art, bildtafc_datum Datum from w_bildtaf_cp where bildtafc_btnr = '&BTNR&' order by Art, Datum";
        }

        public final String LOAD_BT_BEDINGUNGEN_SALA() {
            return "select distinct btebe_elemid ElementId, bedsala_art || bedsala_pnr || bedsala_hz Code, ben_text Benennung, bed_egid EGruppenId, eg_exklusiv Exklusiv, bedsala_saz SAZ, eg_pos EGruppenPosition, bedsala_pnr PrimaNr from w_bte_bedkurz, w_ben_gk, w_bed_sala, w_bed, w_eg, w_bte_bedelem where btebk_btnr = '&BTNR&' and btebk_mospid = &MOSP& and btebk_btnr = btebe_btnr and btebk_kez = btebe_kez and btebe_elemid = bed_elemid and bed_elemid = bedsala_id and bed_egid = eg_id and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' union select distinct btebe_elemid ElementId, bedsala_art || bedsala_pnr || bedsala_hz Code, ben_text Benennung, bed_egid EGruppenId, eg_exklusiv Exklusiv, bedsala_saz SAZ, eg_pos EGruppenPosition, bedsala_pnr PrimaNr from w_bildtaf, w_ben_gk, w_bed_sala, w_bed, w_eg, w_bte_bedelem where bildtaf_btnr = '&BTNR&' and bildtaf_bedkez is not null and bildtaf_btnr = btebe_btnr and bildtaf_bedkez = btebe_kez and btebe_elemid = bed_elemid and bed_elemid = bedsala_id and bed_egid = eg_id and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by EGruppenPosition, PrimaNr";
        }

        public final String LOAD_BT_BEDINGUNGEN_AFL() {
            return "select distinct btebe_elemid ElementId, bedafl_code Code, ben_text Benennung, bed_egid EGruppenId, eg_exklusiv Exklusiv, eg_pos EGruppenPosition from w_bte_bedkurz, w_ben_gk, w_bed_afl, w_bed, w_eg, w_bte_bedelem where btebk_btnr = '&BTNR&' and btebk_mospid = &MOSP& and btebk_btnr = btebe_btnr and btebk_kez = btebe_kez and btebe_elemid = bed_elemid and bed_elemid = bedafl_id and bed_egid = eg_id and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' union select distinct btebe_elemid ElementId, bedafl_code Code, ben_text Benennung, bed_egid EGruppenId, eg_exklusiv Exklusiv, eg_pos EGruppenPosition from w_bildtaf, w_ben_gk, w_bed_afl, w_bed, w_eg, w_bte_bedelem where bildtaf_btnr = '&BTNR&' and bildtaf_bedkez is not null and bildtaf_btnr = btebe_btnr and bildtaf_bedkez = btebe_kez and btebe_elemid = bed_elemid and bed_elemid = bedafl_id and bed_egid = eg_id and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by EGruppenPosition, Code";
        }

        public final String LOAD_BT_BEDINGUNGEN_TEXT() {
            return "select distinct btebe_elemid ElementId, ben_text Benennung, bed_egid EGruppenId, eg_exklusiv Exklusiv, eg_pos EGruppenPosition from w_bte_bedkurz, w_ben_gk, w_bed, w_eg, w_bte_bedelem where btebk_btnr = '&BTNR&' and btebk_mospid = &MOSP& and btebk_btnr = btebe_btnr and btebk_kez = btebe_kez and btebe_elemid = bed_elemid and bed_ogid = 'TEXT' and bed_egid = eg_id and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' union select distinct btebe_elemid ElementId, ben_text Benennung, bed_egid EGruppenId, eg_exklusiv Exklusiv, eg_pos EGruppenPosition from w_bildtaf, w_ben_gk, w_bed, w_eg, w_bte_bedelem where bildtaf_btnr = '&BTNR&' and bildtaf_bedkez is not null and bildtaf_btnr = btebe_btnr and bildtaf_bedkez = btebe_kez and btebe_elemid = bed_elemid and bed_ogid = 'TEXT' and bed_egid = eg_id and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by EGruppenPosition, Benennung";
        }
    }

    public static final class TeileinfoJAVA
    extends SQLStatements.TeileinfoJAVA {
        public final String RETRIEVE_TEILEINFO_NOTIZ() {
            return "select teileinfo_notiz Notiz, teileinfo_gueltig_bis_monat BisMonat, teileinfo_gueltig_bis_jahr BisJahr, MBEN.ben_text MONATBEN, teileinfo_allgemein Allgemein from w_teileinfo@etk_nutzer, w_publben, w_ben_gk MBEN where teileinfo_sachnr = '&SACHNR&' and teileinfo_user_id = '&ID&' and teileinfo_firma_id = '&FIRMA&' and publben_art = 'M' and (to_char(teileinfo_gueltig_bis_monat) = publben_bezeichnung   or '0' || to_char(teileinfo_gueltig_bis_monat) = publben_bezeichnung) and publben_textcode = MBEN.ben_textcode and MBEN.ben_iso = '&ISO&' and MBEN.ben_regiso = '&REGISO&'";
        }

        public final String DELETE_TEILEINFO_NOTIZ() {
            return "delete from w_teileinfo@etk_nutzer where teileinfo_sachnr = '&SACHNR&' and teileinfo_user_id = '&ID&' and teileinfo_firma_id = '&FIRMA&'";
        }

        public final String INSERT_TEILEINFO_NOTIZ() {
            return "insert into w_teileinfo@etk_nutzer (teileinfo_sachnr, teileinfo_user_id, teileinfo_firma_id, teileinfo_allgemein, teileinfo_notiz, teileinfo_gueltig_bis_monat, teileinfo_gueltig_bis_jahr) values ('&SACHNR&', '&ID&', '&FIRMA&', '&ALLGEMEIN&', '&NOTIZTEXT&', &BISMONAT&, &BISJAHR&)";
        }

        public final String RETRIEVE_TEILEINFO_NOTIZEN_OTHERS() {
            return "select teileinfo_notiz Notiz, MBEN.ben_text Monat, teileinfo_gueltig_bis_jahr BisJahr from w_teileinfo@etk_nutzer, w_publben, w_ben_gk MBEN where teileinfo_sachnr = '&SACHNR&' and teileinfo_user_id <> '&ID&' and teileinfo_firma_id = '&FIRMA&' and teileinfo_allgemein = 'J' and publben_art = 'M' and (to_char(teileinfo_gueltig_bis_monat) = publben_bezeichnung   or '0' || to_char(teileinfo_gueltig_bis_monat) = publben_bezeichnung) and publben_textcode = MBEN.ben_textcode and MBEN.ben_iso = '&ISO&' and MBEN.ben_regiso = '&REGISO&'";
        }

        public final String RETRIEVE_COUNT_PREISE() {
            return "select count (*) from w_preise@etk_preise where preise_firma = '&FIRMA&'";
        }
    }

    public static final class TeileinfoHTML
    extends SQLStatements.TeileinfoHTML {
        public final String RETRIEVE_TEILEINFO_NOTIZ() {
            return "select teileinfo_notiz Notiz, teileinfo_gueltig_bis_monat BisMonat, teileinfo_gueltig_bis_jahr BisJahr from w_teileinfo@etk_nutzer where teileinfo_sachnr = '&SACHNR&' and teileinfo_user_id = '&ID&'";
        }

        public final String DELETE_TEILEINFO_NOTIZ() {
            return "delete from w_teileinfo@etk_nutzer where teileinfo_sachnr = '&SACHNR&' and teileinfo_user_id = '&ID&'";
        }

        public final String INSERT_TEILEINFO_NOTIZ() {
            return "insert into w_teileinfo@etk_nutzer (teileinfo_sachnr, teileinfo_user_id, teileinfo_notiz, teileinfo_gueltig_bis_monat, teileinfo_gueltig_bis_jahr) values ('&SACHNR&', '&ID&', '&NOTIZTEXT&', &BISMONAT&, &BISJAHR&)";
        }
    }

    public static final class Teileinfo
    extends SQLStatements.Teileinfo {
        public final String RETRIEVE_TEILEINFO() {
            return "select distinct teil.teil_hauptgr Hg, teil.teil_untergrup Ug, teil.teil_sachnr SachNr, teil_marke.teilm_marke_tps Marke,  ben.ben_text Benennung, benkom.ben_text BenKom, teil.teil_benennzus BenZusatz, teil.teil_art Art, teiltausch.teil_hauptgr TauschHg, teiltausch.teil_untergrup TauschUg, teil.teil_tausch AustauschTNr, teilalt.teil_hauptgr AltHg, teilalt.teil_untergrup AltUg, teil.teil_alt TNrAlt, teil.teil_austausch_alt Austauschbar, NULL BenAustausch, teil.teil_technik TST, teil.teil_dispo TSD, teil.teil_mengeeinh Mengeneinheit, NULL BenMengeneinheit, teil.teil_produktkl Produktklasse, teil.teil_rundung RundungsKz, teil.teil_lkz LokalTeilKz, teil.teil_vorverpac VVM, teil.teil_lagerverp LVM, teil.teil_beh_verp BVM, teil.teil_teile_gew Gewicht, nn_art Normart, teil.teil_normnummer DIN, teil.teil_fertigungshinweis Fertigungshinweis, teil.teil_kom_pi ZusatzinfoKomId, teil.teil_recycling_kez RecyclKz, teil.teil_produktart Produktart, teil.teil_verbaubar Verbaubar,  teil.teil_ist_reach Reach,  teil.teil_ist_diebstahlrelevant Teil_Diebstahlrelevant,  w_eu_reifen.reifen_kraftstoff,  w_eu_reifen.reifen_nasshaftung,  w_eu_reifen.reifen_rollgeraeusch_stufe,  w_eu_reifen.reifen_rollgeraeusch_wert  from w_teil teil inner join w_ben_gk ben on (teil.teil_textcode = ben.ben_textcode and ben.ben_iso = '&ISO&' and  ben.ben_regiso = '&REGISO&')  inner join w_teil_marken teil_marke on (teil_marke.teilm_sachnr = teil.teil_sachnr and  teil_marke.teilm_marke_tps IN (&MARKEN&) ) left join w_ben_gk benkom on (teil.teil_textcode_kom = benkom.ben_textcode and benkom.ben_iso = '&ISO&' and  benkom.ben_regiso = '&REGISO&')  left join w_teil teilalt on (teil.teil_alt = teilalt.teil_sachnr) left join w_teil teiltausch on (teil.teil_tausch = teiltausch.teil_sachnr) left join w_normnummer on (teil.teil_normnummer = nn_nnid) left join w_eu_reifen on (teil.teil_sachnr = w_eu_reifen.reifen_sachnr) where teil.teil_sachnr = '&SACHNR&'  &HGUG_STMT&";
        }

        public final String RETRIEVE_TEILEINFO_HGUG() {
            return " and teil.teil_hauptgr = '&HG&' and teil.teil_untergrup = '&UG&'";
        }

        public final String RETRIEVE_TEILEINFO_SERVICEINFO() {
            return "select si_dokart DokArt, si_doknr DokNr from w_si where si_sachnr = '&SACHNR&'";
        }

        public final String RETRIEVE_TEILEINFO_PRODUKTINFO() {
            return "select ben_text Ben, komm_pos Pos from w_komm, w_ben_gk where komm_id = &KOMID& and komm_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Pos";
        }

        public final String RETRIEVE_TEILEINFO_REACHINFO() {
            return "select teilreach_casnr casNr, teilreach_casname casName, teilreach_gewanteil Gewichtsanteil from w_teil_reach, w_teil where teilreach_sachnr = '&SACHNR&'   and teilreach_sachnr=teil_sachnr  order by casNr";
        }

        public final String RETRIEVE_BEN_ZU_KUERZEL() {
            return "select ben_text Ben from w_publben, w_ben_gk where publben_art = '&ART&' and publben_bezeichnung = '&KUERZEL&' and  ben_textcode = publben_textcode and ben_iso = '&ISO&' and  ben_regiso = '&REGISO&'";
        }

        public final String RETRIEVE_TEILEINFO_TEILECLEARING() {
            return "select distinct tck_id Id, tck_landkuerzel Land, tck_marke_tps Marke, tck_produktart Produktart, tck_vbereich VBereich, tck_baureihen Baureihen, tck_motoren Motoren, tck_baureihen_proddat_von DatumVon, tck_baureihen_proddat_bis DatumBis, tck_pos Pos from w_tc_sachnummer, w_tc_kampagne where tcs_sachnr = '&SACHNR&'   and tcs_id = tck_id    and tck_marke_tps in (&MARKEN&)   and tck_produktart in (&PRODUKTARTEN&)   and tck_vbereich in (&KATALOGUMFAENGE&)   and tck_datum_von <= &DATUM&   &TC_CHECK_LANDKUERZEL&   and (tck_datum_bis is null or tck_datum_bis >= &DATUM&) order by Land, Marke, Produktart DESC, VBereich DESC, Id, Pos";
        }

        public final String TC_CHECK_LANDKUERZEL() {
            return " and tck_landkuerzel = '&LANDKUERZEL&'";
        }

        public final String RETRIEVE_HGUG() {
            return "select teil_hauptgr Hg, teil_untergrup Ug from w_teil where teil_sachnr = '&SACHNR&'";
        }

        public final String SEARCH_SNR_FREMDNR() {
            return "select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar, teilm_marke_tps Marke,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_teil inner join w_ben_gk ben_teil on ( teil_textcode = ben_teil.ben_textcode and  ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&') left join w_ben_gk ben_komm on ( teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') inner join w_teil_marken teil_marke on ( teilm_sachnr = teil_sachnr) where teil_sachnr IN &SACHNUMMERN& order by Benennung, Hauptgruppe, Untergruppe, Sachnummer";
        }
    }

    public static final class TeilesucheAss
    extends SQLStatements.TeilesucheAss {
        public final String RETRIEVE_HGS() {
            return "select distinct hgfg_hg Hauptgruppe, ben_text Benennung, hgthb_grafikid GrafikId, grafik_moddate ModStamp from w_btzeilenugb_verbauung inner join w_bildtaf on (btzeilenuv_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&')) inner join w_hgfg on (bildtaf_hg = hgfg_hg and hgfg_fg = '00' and bildtaf_produktart = hgfg_produktart and bildtaf_bereich = hgfg_bereich) inner join w_ben_gk on (hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_hg_thumbnail on (hgfg_hg = hgthb_hg and hgthb_marke_tps = '&MARKE&' and hgfg_produktart = hgthb_produktart and hgfg_bereich = hgthb_bereich) left join w_grafik on (hgthb_grafikid = grafik_grafikid and grafik_art = 'T') where btzeilenuv_marke_tps = '&MARKE&' order by Hauptgruppe";
        }

        public final String RETRIEVE_HGS_GRAF() {
            return "select distinct hgfg_hg Hauptgruppe, ben_text Benennung, hgthb_grafikid GrafikId, grafik_moddate ModStamp, grafik_blob Grafik from w_btzeilenugb_verbauung inner join w_bildtaf on (btzeilenuv_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&')) inner join w_hgfg on (bildtaf_hg = hgfg_hg and hgfg_fg = '00' and bildtaf_produktart = hgfg_produktart and bildtaf_bereich = hgfg_bereich) inner join w_ben_gk on (hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_hg_thumbnail on (hgfg_hg = hgthb_hg and hgthb_marke_tps = '&MARKE&' and hgfg_produktart = hgthb_produktart and hgfg_bereich = hgthb_bereich) left join w_grafik on (hgthb_grafikid = grafik_grafikid and grafik_art = 'T') where btzeilenuv_marke_tps = '&MARKE&' order by Hauptgruppe";
        }

        public final String RETRIEVE_HGFGS() {
            return "select distinct hgfg_hg Hauptgruppe, hgfg_fg Funktionsgruppe, ben_text Benennung from w_btzeilenugb_verbauung, w_hgfg, w_ben_gk, w_bildtaf where btzeilenuv_marke_tps = '&MARKE&' and btzeilenuv_btnr = bildtaf_btnr  and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_hg = '&HG&' and bildtaf_hg = hgfg_hg and bildtaf_fg = hgfg_fg and bildtaf_produktart = hgfg_produktart and bildtaf_bereich = hgfg_bereich and hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Hauptgruppe, Funktionsgruppe";
        }

        public final String RETRIEVE_ALL_HGFGS() {
            return "select hgfg_hg Hauptgruppe, hgfg_fg Funktionsgruppe, ben_text Benennung from w_btzeilenugb_verbauung, w_hgfg, w_ben_gk, w_bildtaf where btzeilenuv_marke_tps = '&MARKE&' and btzeilenuv_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_hg = hgfg_hg and hgfg_fg = '00' and bildtaf_produktart = hgfg_produktart and bildtaf_bereich = hgfg_bereich and hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' union  select hgfg_hg Hauptgruppe, hgfg_fg Funktionsgruppe, ben_text Benennung from w_btzeilenugb_verbauung, w_hgfg, w_ben_gk, w_bildtaf where btzeilenuv_marke_tps = '&MARKE&' and btzeilenuv_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_hg = hgfg_hg and bildtaf_fg = hgfg_fg and bildtaf_produktart = hgfg_produktart and bildtaf_bereich = hgfg_bereich and hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by 1, 2";
        }

        public final String SEARCH_BT_BENENNUNG() {
            return "select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_pos Pos, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso from w_btzeilenugb_verbauung, w_ben_gk, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where btzeilenuv_marke_tps = '&MARKE&' and btzeilenuv_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and ben_text like INSENSITIVE '&SUCHSTRING&'&ORDER_BY_POS&";
        }

        public final String SEARCH_BT_BENENNUNG_SONDERLOCKE() {
            return "select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_pos Pos, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso from w_btzeilenugb_verbauung, w_ben_gk, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where btzeilenuv_marke_tps = '&MARKE&' and btzeilenuv_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and (ben_text like INSENSITIVE '&SUCHSTRING1&' or ben_text like INSENSITIVE '&SUCHSTRING2&')&ORDER_BY_POS&";
        }

        public final String SEARCH_BT_BENENNUNG_TR() {
            return "select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_pos Pos, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso from w_btzeilenugb_verbauung, w_ben_gk, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where btzeilenuv_marke_tps = '&MARKE&' and btzeilenuv_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and upper(replace('i' by '\u0130' in replace('\u0131' by 'I' in ben_text))) like '&SUCHSTRING&'&ORDER_BY_POS&";
        }

        public final String SEARCH_BT_BEGRIFF() {
            return "select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos, marktetk_isokz MarktIso from w_btzeilenugb_verbauung, w_ben_gk, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where btzeilenuv_marke_tps = '&MARKE&' and btzeilenuv_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and &BEGRIFFE_BEN_STMT&&ORDER_BY_POS&";
        }

        public final String SEARCH_SNR_BENENNUNG() {
            return "select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar, NULL BTZeilenAlter, NULL Pos, NULL BTNummer,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilenugb_verbauung inner join w_btzeilenugb  on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos) inner join w_bildtaf      on (btzeilenuv_btnr = bildtaf_btnr) inner join w_teil         on (btzeilenu_sachnr = teil_sachnr) inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&') left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenuv_marke_tps = '&MARKE&' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and ben_teil.ben_text like INSENSITIVE '&SUCHSTRING&'&ORDER_BY_BENENNUNG_HG_UG_SACHNUMMER&";
        }

        public final String SEARCH_SNR_BENENNUNG_SONDERLOCKE() {
            return "select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar, NULL BTZeilenAlter, NULL Pos, NULL BTNummer,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilenugb_verbauung inner join w_btzeilenugb  on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos) inner join w_bildtaf      on (btzeilenuv_btnr = bildtaf_btnr) inner join w_teil         on (btzeilenu_sachnr = teil_sachnr) inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&') left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenuv_marke_tps = '&MARKE&' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and (ben_teil.ben_text like INSENSITIVE '&SUCHSTRING1&' or ben_teil.ben_text like INSENSITIVE '&SUCHSTRING2&')&ORDER_BY_BENENNUNG_HG_UG_SACHNUMMER&";
        }

        public final String SEARCH_SNR_BENENNUNG_TR() {
            return "select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar, NULL BTZeilenAlter, NULL Pos, NULL BTNummer,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilenugb_verbauung inner join w_btzeilenugb  on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos) inner join w_bildtaf      on (btzeilenuv_btnr = bildtaf_btnr) inner join w_teil         on (btzeilenu_sachnr = teil_sachnr) inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&') left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenuv_marke_tps = '&MARKE&' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and upper(replace('i' by '\u0130' in replace('\u0131' by 'I' in ben_teil.ben_text))) like '&SUCHSTRING&'&ORDER_BY_BENENNUNG_HG_UG_SACHNUMMER&";
        }

        public final String SEARCH_SNR_BEGRIFF() {
            return " select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar, NULL BTZeilenAlter, NULL Pos, NULL BTNummer,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilenugb_verbauung      inner join w_btzeilenugb  on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos)      inner join w_bildtaf      on (btzeilenuv_btnr = bildtaf_btnr)       inner join w_teil         on (btzeilenu_sachnr = teil_sachnr)       inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&')      left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenuv_marke_tps = '&MARKE&' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and &BEGRIFFE_BEN_TEIL_STMT&union select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar, NULL BTZeilenAlter, NULL Pos, NULL BTNummer,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilenugb_verbauung      inner join w_btzeilenugb  on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos)      inner join w_bildtaf      on (btzeilenuv_btnr = bildtaf_btnr)       inner join w_teil         on (btzeilenu_sachnr = teil_sachnr)       inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&')      left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenuv_marke_tps = '&MARKE&' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and &BEGRIFFE_BEN_TEIL_KOMM_STMT&&ORDER_BY_BENENNUNG_HG_UG_SACHNUMMER&";
        }

        public final String SEARCH_BT_SACHNUMMER_COMPL() {
            return "select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos, marktetk_isokz MarktIso from w_btzeilenugb_verbauung, w_ben_gk, w_btzeilenugb, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where btzeilenuv_marke_tps = '&MARKE&' and btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos and btzeilenu_sachnr = '&SACHNUMMER&' and btzeilenu_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' &ORDER_BY_POS&";
        }

        public final String SEARCH_SNR_SACHNUMMER_INCOMPL() {
            return "select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilenugb_verbauung inner join w_btzeilenugb  on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos) inner join w_bildtaf      on (btzeilenu_btnr = bildtaf_btnr) inner join w_teil         on (btzeilenu_sachnr = teil_sachnr) inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&') left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenuv_marke_tps = '&MARKE&' and btzeilenu_sachnr like '&SACHNUMMER&' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') order by Benennung, Hauptgruppe, Untergruppe, Sachnummer";
        }

        public final String SEARCH_SNR_FREMDNR() {
            return "select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilenugb_verbauung inner join w_btzeilenugb  on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos) inner join w_bildtaf      on (btzeilenu_btnr = bildtaf_btnr) inner join w_teil         on (btzeilenu_sachnr = teil_sachnr) inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&') left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenuv_marke_tps = '&MARKE&' and btzeilenu_sachnr IN &FREMDNUMMERN& and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') order by Benennung, Hauptgruppe, Untergruppe, Sachnummer";
        }

        public final String SEARCH_BT_SACHNUMMERN() {
            return "select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos, marktetk_isokz MarktIso from w_btzeilenugb_verbauung, w_ben_gk, w_btzeilenugb, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where btzeilenuv_marke_tps = '&MARKE&' and btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos and btzeilenu_sachnr IN (&SACHNUMMERN&) and btzeilenu_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'&ORDER_BY_POS&";
        }

        public final String SEARCH_BT_HGFG() {
            return "select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos, marktetk_isokz MarktIso from w_btzeilenugb_verbauung, w_ben_gk, w_btzeilenugb, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where btzeilenuv_marke_tps = '&MARKE&' and btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos and btzeilenu_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&')&HG/HGFG_STMT& and bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Pos";
        }

        public final String SEARCH_BT_HG_GRAFISCH() {
            return "nur fuer oracle";
        }

        public final String SEARCH_BT_HG_FG_GRAFISCH() {
            return "nur fuer oracle";
        }

        public final String SEARCH_BT_HG_GRAFISCH_MIT_GRAFIKEN() {
            return "select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, BB.ben_text Benennung, bildtaf_pos Pos, bildtaf_grafikid GrafikId, bildtaf_bedkez BedingungKZ, grafik_blob Grafik, grafik_moddate ModStamp, marktetk_isokz MarktIso  from w_btzeilenugb_verbauung inner join w_btzeilenugb on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos) left join w_bildtaf on (btzeilenu_btnr = bildtaf_btnr) left join w_grafik on (bildtaf_grafikid = grafik_grafikid and grafik_art = 'T') left join w_ben_gk BB on (bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_markt_etk on (marktetk_lkz = bildtaf_lkz) where btzeilenuv_marke_tps = '&MARKE&' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_hg = '&HG&' order by Pos";
        }

        public final String SEARCH_BT_HG_FG_GRAFISCH_MIT_GRAFIKEN() {
            return "select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, BB.ben_text Benennung, bildtaf_pos Pos, bildtaf_grafikid GrafikId, grafik_blob Grafik, bildtaf_bedkez BedingungKZ, grafik_moddate ModStamp, marktetk_isokz MarktIso  from w_btzeilenugb_verbauung inner join w_btzeilenugb on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos) left join w_bildtaf on (btzeilenu_btnr = bildtaf_btnr) left join w_grafik on (bildtaf_grafikid = grafik_grafikid and grafik_art = 'T') left join w_ben_gk BB on (bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_markt_etk on (marktetk_lkz = bildtaf_lkz) where btzeilenuv_marke_tps = '&MARKE&' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_hg = '&HG&' and bildtaf_fg = '&FG&' order by Pos";
        }

        public final String CHECK_BT_HG_GRAFISCH() {
            return "select count(bildtaf_btnr) countBte from w_btzeilenugb_verbauung inner join w_btzeilenugb on (btzeilenuv_btnr = btzeilenu_btnr and btzeilenuv_pos = btzeilenu_pos) left join w_bildtaf on (btzeilenu_btnr = bildtaf_btnr) left join w_grafik on (bildtaf_grafikid = grafik_grafikid and grafik_art = 'T') where btzeilenuv_marke_tps = '&MARKE&' and bildtaf_produktart = '&PRODUKTART&' and bildtaf_vbereich in ('BE', '&KATALOGUMFANG&') and bildtaf_hg = '&HG&'";
        }

        public final String SEARCH_BILDTAF_HGFG() {
            return " and bildtaf_hg || bildtaf_fg in (&HGFGS&)";
        }

        public final String SEARCH_BILDTAF_HG() {
            return " and bildtaf_hg = '&HG&'";
        }

        public final String ORDER_BY_POS() {
            return " order by Pos";
        }

        public final String ORDER_BY_BENENNUNG_HG_UG_SACHNUMMER() {
            return " order by Benennung, Hauptgruppe, Untergruppe, Sachnummer";
        }
    }

    public static final class TeilesucheAllgemein
    extends SQLStatements.TeilesucheAllgemein {
        public final String LOAD_MARKT_BENENNUNG() {
            return " select lj11.ben_text MarktBen  from   w_bildtaf t1  left join w_markt_etk lj1  on (lj1.marktetk_lkz = t1.bildtaf_lkz)  left join w_ben_gk lj11 on (lj1.marktetk_textcode = lj11.ben_textcode and                              lj11.ben_iso = '&ISO&' and                              lj11.ben_regiso = '&REGISO&')  where t1.bildtaf_btnr = '&BTNR&'  and   t1.bildtaf_produktart = '&PRODUKTART&' ";
        }
    }

    public static final class TeilesucheFzg
    extends SQLStatements.TeilesucheFzg {
        public final String RETRIEVE_HGS() {
            return "select distinct hgfg_hg Hauptgruppe, ben_text Benennung, hgthb_grafikid GrafikId, grafik_moddate ModStamp from w_hgfg_mosp inner join w_hgfg on (hgfgm_hg = hgfg_hg and hgfg_fg = '00' and hgfg_produktart = hgfgm_produktart and hgfg_bereich = hgfgm_bereich) inner join w_ben_gk on (hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_hg_thumbnail on (hgfg_hg = hgthb_hg and hgthb_marke_tps = '&MARKE&' and hgthb_produktart = hgfg_produktart and hgthb_bereich = hgfg_bereich) left join w_grafik on (hgthb_grafikid = grafik_grafikid and grafik_art = 'T') where hgfgm_mospid = &MOSP& order by Hauptgruppe";
        }

        public final String RETRIEVE_HGS_GRAF() {
            return "select distinct hgfg_hg Hauptgruppe, ben_text Benennung, hgthb_grafikid GrafikId, grafik_moddate ModStamp, grafik_blob Grafik from w_hgfg_mosp inner join w_hgfg on (hgfgm_hg = hgfg_hg and hgfg_fg = '00' and hgfg_produktart = hgfgm_produktart and hgfg_bereich = hgfgm_bereich) inner join w_ben_gk on (hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_hg_thumbnail on (hgfg_hg = hgthb_hg and hgthb_marke_tps = '&MARKE&' and hgthb_produktart = hgfg_produktart and hgthb_bereich = hgfg_bereich) left join w_grafik on (hgthb_grafikid = grafik_grafikid and grafik_art = 'T') where hgfgm_mospid = &MOSP& order by Hauptgruppe";
        }

        public final String RETRIEVE_FGS_GRAF() {
            return "select distinct hgfg_fg Funktionsgruppe,ben_text Benennung,fgthb_grafikid GrafikId,grafik_moddate ModStamp from w_hgfg  inner join w_ben_gk on (hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_fg_thumbnail on (hgfg_hg = fgthb_hg and hgfg_fg = fgthb_fg and fgthb_marke_tps = '&MARKE&' and fgthb_produktart = hgfg_produktart and fgthb_bereich = hgfg_bereich) left join w_grafik on (fgthb_grafikid = grafik_grafikid and grafik_art = 'T') inner join w_bildtaf on (bildtaf_hg = hgfg_hg and hgfg_fg = bildtaf_fg)  where hgfg_hg = '&HG&' AND hgfg_produktart = '&PRODART&' order by Funktionsgruppe";
        }

        public final String RETRIEVE_FGS_GRAF_MOSP() {
            return "select distinct hgfg_fg Funktionsgruppe, ben_text Benennung, fgthb_grafikid GrafikId, grafik_moddate ModStamp from w_hgfg_mosp inner join w_hgfg on (hgfgm_hg = hgfg_hg and hgfg_fg = '00' and hgfg_produktart = hgfgm_produktart and hgfg_bereich = hgfgm_bereich) inner join w_ben_gk on (hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_fg_thumbnail on (hgfg_hg = fgthb_hg and hgfg_fg = fgthb_fg and fgthb_marke_tps = '&MARKE&' and fgthb_produktart = hgfg_produktart and fgthb_bereich = hgfg_bereich) left join w_grafik on (fgthb_grafikid = grafik_grafikid and grafik_art = 'T') inner join w_bildtaf on (bildtaf_hg = hgfgm_hg and hgfgm_fg = bildtaf_fg)  where hgfgm_mospid = &MOSP& AND hgfgm_hg = '&HG&' order by Funktionsgruppe";
        }

        public final String RETRIEVE_FGS_GRAF_MIT_GRAFIKEN() {
            return "select distinct hgfg_fg Funktionsgruppe,ben_text Benennung,fgthb_grafikid GrafikId,grafik_moddate ModStamp,grafik_blob Grafik from w_hgfg  inner join w_ben_gk on (hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_fg_thumbnail on (hgfg_hg = fgthb_hg and hgfg_fg = fgthb_fg and fgthb_marke_tps = '&MARKE&' and fgthb_produktart = hgfg_produktart and fgthb_bereich = hgfg_bereich) left join w_grafik on (fgthb_grafikid = grafik_grafikid and grafik_art = 'T') inner join w_bildtaf on (bildtaf_hg = hgfg_hg and hgfg_fg = bildtaf_fg)  where hgfg_hg = '&HG&' AND hgfg_produktart = '&PRODART&' order by Funktionsgruppe";
        }

        public final String RETRIEVE_FGS_GRAF_MOSP_MIT_GRAFIKEN() {
            return "select distinct hgfg_fg Funktionsgruppe, ben_text Benennung, fgthb_grafikid GrafikId, grafik_moddate ModStamp, grafik_blob Grafik from w_hgfg_mosp inner join w_hgfg on (hgfgm_hg = hgfg_hg and hgfg_fg = '00' and hgfg_produktart = hgfgm_produktart and hgfg_bereich = hgfgm_bereich) inner join w_ben_gk on (hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') left join w_fg_thumbnail on (hgfg_hg = fgthb_hg and hgfg_fg = fgthb_fg and fgthb_marke_tps = '&MARKE&' and fgthb_produktart = hgfg_produktart and fgthb_bereich = hgfg_bereich) left join w_grafik on (fgthb_grafikid = grafik_grafikid and grafik_art = 'T') inner join w_bildtaf on (bildtaf_hg = hgfgm_hg and hgfgm_fg = bildtaf_fg)  where hgfgm_mospid = &MOSP& AND hgfgm_hg = '&HG&' order by Funktionsgruppe";
        }

        public final String RETRIEVE_HGFGS() {
            return "select distinct hgfg_hg Hauptgruppe, hgfg_fg Funktionsgruppe, ben_text Benennung from w_hgfg_mosp, w_hgfg, w_ben_gk where hgfgm_mospid = &MOSP& and hgfgm_hg = '&HG&' and hgfgm_hg = hgfg_hg and hgfgm_fg = hgfg_fg and hgfgm_produktart = hgfg_produktart and hgfgm_bereich = hgfg_bereich  and hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Hauptgruppe, Funktionsgruppe";
        }

        public final String RETRIEVE_ALL_HGFGS() {
            return "select hgfg_hg Hauptgruppe, hgfg_fg Funktionsgruppe, ben_text Benennung from w_hgfg_mosp, w_hgfg, w_ben_gk where hgfgm_mospid = &MOSP& and hgfgm_hg = hgfg_hg and hgfg_fg = '00' and hgfgm_produktart = hgfg_produktart and hgfgm_bereich = hgfg_bereich and hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' union  select hgfg_hg Hauptgruppe, hgfg_fg Funktionsgruppe, ben_text Benennung from w_hgfg_mosp, w_hgfg, w_ben_gk where hgfgm_mospid = &MOSP& and hgfgm_hg = hgfg_hg and hgfgm_fg = hgfg_fg and hgfgm_produktart = hgfg_produktart and hgfgm_bereich = hgfg_bereich and hgfg_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by 1, 2";
        }

        public final String SEARCH_BT_BENENNUNG() {
            return "select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_pos Pos, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso from w_ben_gk, w_bildtaf_suche, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and ben_text like INSENSITIVE '&SUCHSTRING&' and ben_textcode = bildtaf_textc and bildtaf_btnr = bildtafs_btnr &BEACHTE_SICHER_FLAG_STMT& and bildtafs_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT&&UNION&&SEARCH_BT_BENENNUNG_ASS& order by Pos";
        }

        public final String SEARCH_BT_BENENNUNG_SONDERLOCKE() {
            return "select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_pos Pos, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso from w_ben_gk, w_bildtaf_suche, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and (ben_text like INSENSITIVE '&SUCHSTRING1&' or ben_text like INSENSITIVE '&SUCHSTRING2&') and ben_textcode = bildtaf_textc&BEACHTE_SICHER_FLAG_STMT& and bildtaf_btnr = bildtafs_btnr and bildtafs_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT&&UNION&&SEARCH_BT_BENENNUNG_SONDERLOCKE_ASS& order by Pos";
        }

        public final String SEARCH_BT_BENENNUNG_TR() {
            return "select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_pos Pos, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, marktetk_isokz MarktIso from w_ben_gk, w_bildtaf_suche, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and upper(replace('i' by  '\u0130' in replace('\u0131' by 'I' in ben_text))) like '&SUCHSTRING&' and ben_textcode = bildtaf_textc&BEACHTE_SICHER_FLAG_STMT& and bildtaf_btnr = bildtafs_btnr and bildtafs_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT&&UNION&&SEARCH_BT_BENENNUNG_ASS& order by Pos";
        }

        public final String SEARCH_BT_BEGRIFF() {
            return "select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos, marktetk_isokz MarktIso from w_ben_gk, w_bildtaf_suche, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and &BEGRIFFE_BEN_STMT& and ben_textcode = bildtaf_textc and bildtaf_btnr = bildtafs_btnr &BEACHTE_SICHER_FLAG_STMT& and bildtafs_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT&&UNION&&SEARCH_BT_BEGRIFF_ASS& order by Pos";
        }

        public final String SEARCH_BT_BEGRIFF_NEU() {
            return "select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos, marktetk_isokz MarktIso from w_ben_gk, w_bildtaf_suche, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where ben_iso = '&ISO&' and ben_regiso = '&REGISO&' and &BEGRIFFE_BEN_STMT& and ben_textcode = bildtaf_textc and bildtaf_btnr = bildtafs_btnr &BEACHTE_SICHER_FLAG_STMT& and bildtafs_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT&&UNION&&SEARCH_BT_BEGRIFF_ASS& union select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_bte.ben_text Benennung, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos, marktetk_isokz MarktIso from w_ben_gk ben_komm     , w_komm_help     , w_komm     , w_bildtaf_suche     , w_ben_gk ben_bte     , w_bildtaf  left join w_markt_etk    on (marktetk_lkz = bildtaf_lkz)  where ben_komm.ben_iso = '&ISO&'  and ben_komm.ben_regiso = '&REGISO&'  and &BEGRIFFE_BEN_KOMM_STMT&  and komm_textcode = ben_komm.ben_textcode  and kommh_id = komm_id  and kommh_mospid = &MOSP&  and kommh_btnr = bildtaf_btnr  and ben_bte.ben_iso = '&ISO&'  and ben_bte.ben_regiso = '&REGISO&'  and ben_bte.ben_textcode = bildtaf_textc and bildtaf_btnr = bildtafs_btnr &BEACHTE_SICHER_FLAG_STMT& and bildtafs_mospid = kommh_mospid &LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& order by Pos ";
        }

        public final String SEARCH_BILDTAFS_LENKUNG() {
            return " and (bildtafs_lenkg is NULL OR bildtafs_lenkg = '&LENKUNG&')";
        }

        public final String SEARCH_BILDTAFS_GETRIEBE() {
            return " and (bildtafs_automatik is NULL OR bildtafs_automatik = '&GETRIEBE&')";
        }

        public final String SEARCH_BILDTAFS_EINSATZ() {
            return " and (bildtafs_eins is NULL OR bildtafs_eins <= &EINSATZ&)";
        }

        public final String SEARCH_BILDTAFS_AUSLAUF() {
            return " and (bildtafs_auslf is NULL OR &AUSLAUF& <= bildtafs_auslf)";
        }

        public final String SEARCH_SNR_BENENNUNG() {
            return "select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar, btzeilenv_alter_kz BTZeilenAlter, if btzeilenv_alter_kz is not null THEN btzeilenv_pos ELSE NULL FI Pos, if btzeilenv_alter_kz is not null THEN btzeilenv_btnr ELSE NULL FI BTNummer,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilen_verbauung  inner join w_btzeilen     on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)  inner join w_bildtaf      on (btzeilen_btnr = bildtaf_btnr)  inner join w_teil         on (teil_sachnr = btzeilen_sachnr)  inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&')  left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenv_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& and ben_teil.ben_text like INSENSITIVE '&SUCHSTRING&'&BEACHTE_SICHER_FLAG_STMT&&UNION&&SEARCH_SNR_BENENNUNG_ASS& order by Benennung, Hauptgruppe, Untergruppe, Sachnummer";
        }

        public final String SEARCH_SNR_BENENNUNG_SONDERLOCKE() {
            return "select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar, btzeilenv_alter_kz BTZeilenAlter, if btzeilenv_alter_kz is not null THEN btzeilenv_pos ELSE NULL FI Pos, if btzeilenv_alter_kz is not null THEN btzeilenv_btnr ELSE NULL FI BTNummer,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilen_verbauung  inner join w_btzeilen     on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)  inner join w_bildtaf      on (btzeilen_btnr = bildtaf_btnr)  inner join w_teil         on (teil_sachnr = btzeilen_sachnr)  inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&')  left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenv_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& and (ben_teil.ben_text like INSENSITIVE '&SUCHSTRING1&' or ben_teil.ben_text like INSENSITIVE '&SUCHSTRING2&')&BEACHTE_SICHER_FLAG_STMT&&UNION&&SEARCH_SNR_BENENNUNG_SONDERLOCKE_ASS& order by Benennung, Hauptgruppe, Untergruppe, Sachnummer";
        }

        public final String SEARCH_SNR_BENENNUNG_TR() {
            return "select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar, btzeilenv_alter_kz BTZeilenAlter, if btzeilenv_alter_kz is not null THEN btzeilenv_pos ELSE NULL FI Pos, if btzeilenv_alter_kz is not null THEN btzeilenv_btnr ELSE NULL BTNummer,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilen_verbauung  inner join w_btzeilen     on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)  inner join w_bildtaf      on (btzeilen_btnr = bildtaf_btnr)  inner join w_teil         on (teil_sachnr = btzeilen_sachnr)  inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&')  left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenv_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& and upper(replace('i' by '\u0130' in replace('\u0131' by 'I' in ben_teil.ben_text))) like '&SUCHSTRING&'&BEACHTE_SICHER_FLAG_STMT&&UNION&&SEARCH_SNR_BENENNUNG_ASS& order by Benennung, Hauptgruppe, Untergruppe, Sachnummer";
        }

        public final String SEARCH_SNR_BEGRIFF() {
            return " select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar, btzeilenv_alter_kz BTZeilenAlter, if btzeilenv_alter_kz is not null THEN btzeilenv_pos ELSE NULL FI Pos, if btzeilenv_alter_kz is not null THEN btzeilenv_btnr ELSE NULL FI BTNummer,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilen_verbauung      inner join w_btzeilen     on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)      inner join w_bildtaf      on (btzeilen_btnr = bildtaf_btnr)      inner join w_teil         on (teil_sachnr = btzeilen_sachnr)      inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&')      left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenv_mospid = &MOSP&&BEACHTE_SICHER_FLAG_STMT&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& and &BEGRIFFE_BEN_TEIL_STMT&union  select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar, btzeilenv_alter_kz BTZeilenAlter, if btzeilenv_alter_kz is not null THEN btzeilenv_pos ELSE NULL FI Pos, if btzeilenv_alter_kz is not null THEN btzeilenv_btnr ELSE NULL FI BTNummer,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilen_verbauung      inner join w_btzeilen     on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)      inner join w_bildtaf      on (btzeilen_btnr = bildtaf_btnr)      inner join w_teil         on (teil_sachnr = btzeilen_sachnr)      inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&')      inner join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenv_mospid = &MOSP&&BEACHTE_SICHER_FLAG_STMT&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& and &BEGRIFFE_BEN_TEIL_KOMM_STMT&&UNION&&SEARCH_SNR_BEGRIFF_ASS& order by Benennung, Hauptgruppe, Untergruppe, Sachnummer";
        }

        public final String SEARCH_BT_SACHNUMMER_COMPL() {
            return "select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos, marktetk_isokz MarktIso from w_btzeilen_verbauung, w_ben_gk, w_btzeilen, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where btzeilenv_mospid = &MOSP& and btzeilenv_sachnr = '&SACHNUMMER&' and btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& and btzeilen_btnr = bildtaf_btnr and bildtaf_produktart = '&PRODUKTART&'&BEACHTE_SICHER_FLAG_STMT& and bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'&UNION&&SEARCH_BT_SACHNUMMER_COMPL_ASS& order by Pos";
        }

        public final String SEARCH_SNR_SACHNUMMER_INCOMPL() {
            return "select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilen_verbauung      inner join w_btzeilen     on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)      inner join w_bildtaf      on (btzeilen_btnr = bildtaf_btnr)      inner join w_teil         on (teil_sachnr = btzeilen_sachnr)      inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&')      left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenv_mospid = &MOSP& and btzeilenv_sachnr like '&SACHNUMMER&'&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT&&BEACHTE_SICHER_FLAG_STMT& order by Benennung, Hauptgruppe, Untergruppe, Sachnummer";
        }

        public final String SEARCH_SNR_FREMDNR() {
            return "select distinct teil_hauptgr Hauptgruppe, teil_untergrup Untergruppe, teil_sachnr Sachnummer, ben_teil.ben_text Benennung, teil_benennzus Zusatz, ben_komm.ben_text BenennungKommentar,  teil_ist_diebstahlrelevant Teil_Diebstahlrelevant  from w_btzeilen_verbauung      inner join w_btzeilen     on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos)      inner join w_bildtaf      on (btzeilen_btnr = bildtaf_btnr)      inner join w_teil         on (teil_sachnr = btzeilen_sachnr)      inner join w_ben_gk ben_teil on (teil_textcode = ben_teil.ben_textcode and ben_teil.ben_iso = '&ISO&' and ben_teil.ben_regiso = '&REGISO&')      left  join w_ben_gk ben_komm on (teil_textcode_kom = ben_komm.ben_textcode and ben_komm.ben_iso = '&ISO&' and ben_komm.ben_regiso = '&REGISO&') where btzeilenv_mospid = &MOSP& and btzeilenv_sachnr IN &FREMDNUMMERN&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT&&BEACHTE_SICHER_FLAG_STMT& order by Benennung, Hauptgruppe, Untergruppe, Sachnummer";
        }

        public final String SEARCH_BT_SACHNUMMERN() {
            return "select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos, marktetk_isokz MarktIso from w_btzeilen_verbauung, w_ben_gk, w_btzeilen, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where btzeilenv_mospid = &MOSP& and btzeilenv_sachnr IN (&SACHNUMMERN&) and btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& and btzeilen_btnr = bildtaf_btnr&BEACHTE_SICHER_FLAG_STMT& and bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'&UNION&&SEARCH_BT_SACHNUMMERN_ASS& order by Pos";
        }

        public final String SEARCH_BTZEILEN_LENKUNG() {
            return " and (btzeilen_lenkg is NULL OR btzeilen_lenkg = '&LENKUNG&')";
        }

        public final String SEARCH_BTZEILEN_GETRIEBE() {
            return " and (btzeilen_automatik is NULL OR btzeilen_automatik = '&GETRIEBE&')";
        }

        public final String SEARCH_BTZEILEN_EINSATZ() {
            return " and (btzeilen_eins is NULL OR btzeilen_eins <= &EINSATZ&)";
        }

        public final String SEARCH_BTZEILEN_AUSLAUF() {
            return " and (btzeilen_auslf is NULL OR &AUSLAUF& <= btzeilen_auslf)";
        }

        public final String SEARCH_BT_HGFG() {
            return "select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, ben_text Benennung, bildtaf_kommbt Kommentar, bildtaf_vorh_cp CPVorhanden, bildtaf_bedkez BedingungKZ, bildtaf_pos Pos, marktetk_isokz MarktIso from w_bildtaf_suche, w_ben_gk, w_bildtaf left join w_markt_etk   on (marktetk_lkz = bildtaf_lkz) where bildtafs_hg = '&HG&' and bildtafs_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& and bildtafs_btnr = bildtaf_btnr&HG/HGFG_STMT&&BEACHTE_SICHER_FLAG_STMT& and bildtaf_textc = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'order by Pos";
        }

        public final String SEARCH_BT_HG_GRAFISCH() {
            return "nur fuer oracle";
        }

        public final String SEARCH_BT_HG_FG_GRAFISCH() {
            return "nur fuer oracle";
        }

        public final String SEARCH_BT_HG_GRAFISCH_MIT_GRAFIKEN() {
            return "select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, BB.ben_text Benennung, bildtaf_pos Pos, bildtaf_grafikid GrafikId, bildtaf_bedkez BedingungKZ, grafik_blob Grafik, grafik_moddate ModStamp, marktetk_isokz MarktIso  from w_btzeilen_verbauung inner join w_btzeilen on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos) inner join w_bildtaf on (btzeilen_btnr = bildtaf_btnr) left join w_grafik on (bildtaf_grafikid = grafik_grafikid and grafik_art = 'T') inner join w_ben_gk BB on (bildtaf_textc = BB.ben_textcode and BB.ben_iso = '&ISO&' and BB.ben_regiso = '&REGISO&') left join w_markt_etk on (marktetk_lkz = bildtaf_lkz) where btzeilenv_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& and bildtaf_hg = '&HG&'&BEACHTE_SICHER_FLAG_STMT& order by Pos";
        }

        public final String SEARCH_BT_HG_FG_GRAFISCH_MIT_GRAFIKEN() {
            return "select distinct bildtaf_btnr BildtafelNr, bildtaf_bteart BildtafelArt, BB.ben_text Benennung, bildtaf_pos Pos, bildtaf_grafikid GrafikId, bildtaf_bedkez BedingungKZ, grafik_blob Grafik, grafik_moddate ModStamp, marktetk_isokz MarktIso  from w_btzeilen_verbauung inner join w_btzeilen on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos) inner join w_bildtaf on (btzeilen_btnr = bildtaf_btnr) left join w_grafik on (bildtaf_grafikid = grafik_grafikid and grafik_art = 'T') inner join w_ben_gk BB on (bildtaf_textc = BB.ben_textcode and BB.ben_iso = '&ISO&' and BB.ben_regiso = '&REGISO&') left join w_markt_etk on (marktetk_lkz = bildtaf_lkz) where btzeilenv_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& and bildtaf_hg = '&HG&' and bildtaf_hg = '&FG&'&BEACHTE_SICHER_FLAG_STMT& order by Pos";
        }

        public final String CHECK_BT_HG_GRAFISCH() {
            return "select count( bildtaf_btnr) countBte from w_btzeilen_verbauung inner join w_btzeilen on (btzeilenv_btnr = btzeilen_btnr and btzeilenv_pos = btzeilen_pos) inner join w_bildtaf on (btzeilen_btnr = bildtaf_btnr) left join w_grafik on (bildtaf_grafikid = grafik_grafikid and grafik_art = 'T') where btzeilenv_mospid = &MOSP&&LENKUNG_STMT&&GETRIEBE_STMT&&EINSATZ_STMT&&AUSLAUF_STMT& and bildtaf_hg = '&HG&'&BEACHTE_SICHER_FLAG_STMT&";
        }

        public final String SEARCH_BILDTAF_HGFG() {
            return " and bildtaf_hg || bildtaf_fg in (&HGFGS&)";
        }

        public final String SEARCH_NUR_NICHT_SICHERHEITSRELEVANTE_BILDTAFELN() {
            return " and bildtaf_sicher = 'N'";
        }

        public final String SEARCH_KABELBAUM_CHANGEPOINTS() {
            return "select distinct btzeilenc_pos Pos, btzeilenc_typschl Typ, btzeilenc_werk Werk, btzeilenc_art Art, btzeilenc_datum Datum, btzeilenc_vin Vin, btzeilenc_vin_proddatum VinProddatum, btzeilenc_vin_min VinMin, btzeilenc_vin_max VinMax, btzeilenc_nart ArtNummer, btzeilenc_nummer Nummer, btzeilenc_alter CPAlter from w_btzeilen_cp where btzeilenc_mospid = &MOSPID& and btzeilenc_typschl = '&TYPSCHLUESSEL&' and btzeilenc_werk = '&WERK&' and btzeilenc_pos = &POSITION& and btzeilenc_btnr = '&BTNUMMER&'";
        }
    }

    public static final class TeilesucheSpezifischValueLineFzg
    extends SQLStatements.TeilesucheSpezifischValueLineFzg {
        public final String RETRIEVE_BTES_VALUE_LINE() {
            return " select distinct                     bildtaf_hg BildtafelHG,                     bildtaf_btnr BildtafelNr,            \t\t\tbildtaf_bteart BildtafelArt, \t\t\t\t\tben_text Benennung, \t\t\t\t\tbildtaf_pos Pos, \t\t\t\t\tbildtaf_kommbt Kommentar, \t\t\t\t\tbildtaf_vorh_cp CPVorhanden, \t\t\t\t\tbildtaf_bedkez BedingungKZ, \t\t\t\t\tmarktetk_isokz MarktIso  from  \t\t\t\tw_hgfg, \t\t\t\t\tw_bildtaf left join w_markt_etk on (marktetk_lkz = bildtaf_lkz), \t\t\t\t\tw_ben_gk , \t\t\t\t\tw_bildtaf_suche  where \t\t\t\thgfg_ist_valueline = 'J'  and \t\t\t\tbildtaf_hg = hgfg_hg  and \t\t\t\tbildtaf_fg = hgfg_fg  and \t\t\t\tbildtafs_btnr = bildtaf_btnr  and \t\t\t\tbildtafs_hg = bildtaf_hg  and \t\t\t\tbildtafs_mospid = &MOSP&  and \t\t\t\tben_textcode = bildtaf_textc  and \t\t\t\tben_iso = '&ISO&'  and \t\t\t\tben_regiso = '&REGISO&'  union  select distinct                     bildtaf_hg BildtafelHG,                     bildtaf_btnr BildtafelNr,            \t\t\tbildtaf_bteart BildtafelArt, \t\t\t\t\tben_text Benennung, \t\t\t\t\tbildtaf_pos Pos, \t\t\t\t\tbildtaf_kommbt Kommentar, \t\t\t\t\tbildtaf_vorh_cp CPVorhanden, \t\t\t\t\tbildtaf_bedkez BedingungKZ, \t\t\t\t\tmarktetk_isokz MarktIso  from              w_kompl_satz     ,              w_btzeilen_verbauung     ,              w_bildtaf left join w_markt_etk on (marktetk_lkz = bildtaf_lkz)     ,              w_ben_gk     ,              w_bildtaf_suche  where             ks_ist_valueline = 'J'  and               ks_sachnr_satz = btzeilenv_sachnr  and               ks_hg = bildtaf_hg  and               btzeilenv_btnr   = bildtaf_btnr  and               btzeilenv_mospid = bildtafs_mospid  and               bildtafs_btnr = bildtaf_btnr  and               bildtafs_hg = bildtaf_hg  and               bildtafs_mospid = &MOSP&  and               ben_textcode = bildtaf_textc  and               ben_iso = '&ISO&'  and               ben_regiso = '&REGISO&'  order by          BildtafelHG         ,          BildtafelNr ";
        }
    }

    public static final class FzgUmfang
    extends SQLStatements.FzgUmfang {
        public final String RETRIEVE_REGIONEN() {
            return "select distinct fztyp_ktlgausf Region from w_baureihe, w_fztyp where baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and baureihe_vbereich in ('BE', '&KATALOGUMFANG&') and baureihe_baureihe = fztyp_baureihe and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_ktlgausf in (&REGIONEN&) and fztyp_sichtschutz = 'N' order by Region";
        }

        public final String RETRIEVE_LENKUNGEN() {
            return "select distinct fztyp_lenkung Lenkung, ben_text ExtLenkung from w_baureihe, w_ben_gk, w_publben, w_fztyp where baureihe_marke_tps = '&MARKE&' and baureihe_produktart = 'P' and baureihe_vbereich in ('BE', '&KATALOGUMFANG&') and baureihe_baureihe = fztyp_baureihe and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_ktlgausf in (&REGIONEN&) and fztyp_lenkung = publben_bezeichnung and publben_art = 'L' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Lenkung";
        }

        public final String RETRIEVE_BAUARTEN() {
            return "select distinct baureihe_bauart Bauart, ben_text ExtBauart, bauart_position Pos from w_baureihe, w_bauart, w_ben_gk, w_fztyp where baureihe_marke_tps = '&MARKE&' and baureihe_produktart = 'M' and baureihe_vbereich in ('BE', '&KATALOGUMFANG&') and baureihe_baureihe = fztyp_baureihe and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_ktlgausf in (&REGIONEN&) and fztyp_sichtschutz = 'N' and bauart_bauart = baureihe_bauart and ben_textcode = bauart_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Pos";
        }

        public final String RETRIEVE_BAUREIHEN() {
            return "select distinct baureihe_baureihe Baureihe, ben_text ExtBaureihe, baureihe_position Pos from w_baureihe, w_ben_gk, w_fztyp where baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and baureihe_vbereich in ('BE', '&KATALOGUMFANG&') &BAUART_STMT& and baureihe_baureihe = fztyp_baureihe and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_ktlgausf in (&REGIONEN&) &LENKUNG_STMT& and fztyp_sichtschutz = 'N' and baureihe_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Pos";
        }

        public final String RETRIEVE_KAROSSERIEN() {
            return "select distinct fztyp_karosserie Karosserie, ben_text ExtKarosserie from w_fztyp, w_ben_gk, w_publben where fztyp_baureihe IN (&BAUREIHEN&) and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_ktlgausf in (&REGIONEN&) and fztyp_lenkung IN (&LENKUNGEN&) and fztyp_sichtschutz = 'N' and fztyp_karosserie = publben_bezeichnung and publben_art = 'K' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by ExtKarosserie";
        }

        public final String RETRIEVE_MODELLE() {
            return "select distinct fztyp_erwvbez Modell, vbezp_pos Pos from w_vbez_pos, w_baureihe, w_fztyp where baureihe_baureihe IN (&BAUREIHEN&) and baureihe_baureihe = fztyp_baureihe and fztyp_vbereich = '&KATALOGUMFANG&' &BAUART_STMT& &KAROSSERIE_STMT& and fztyp_ktlgausf in (&REGIONEN&) &LENKUNG_STMT& and fztyp_sichtschutz = 'N' and vbezp_baureihe = baureihe_baureihe and vbezp_vbez = fztyp_vbez order by Pos, Modell";
        }

        public final String RETRIEVE_MODELLSPALTEN() {
            return "select distinct fztyp_mospid MospID from w_baureihe, w_fztyp where baureihe_baureihe IN (&BAUREIHEN&) and baureihe_baureihe = fztyp_baureihe and fztyp_vbereich = '&KATALOGUMFANG&' &MODELL_STMT& &BAUART_STMT& &KAROSSERIE_STMT& and fztyp_ktlgausf in (&REGIONEN&) &LENKUNG_STMT& and fztyp_sichtschutz = 'N' order by MospID";
        }

        public final String MODELL_STMT() {
            return " and fztyp_erwvbez IN (&MODELLE&)";
        }

        public final String RETRIEVE_BAUART() {
            return " and baureihe_bauart IN (&BAUARTEN&)";
        }

        public final String RETRIEVE_KAROSSERIE() {
            return " and fztyp_karosserie IN (&KAROSSERIEN&)";
        }

        public final String RETRIEVE_LENKUNG() {
            return " and fztyp_lenkung IN (&LENKUNGEN&)";
        }
    }

    public static final class FzgIdentifikation
    extends SQLStatements.FzgIdentifikation {
        public final String RETRIEVE_BAUARTEN() {
            return "select distinct baureihe_bauart Bauart, ben_text ExtBauart, bauart_position Pos from w_baureihe, w_bauart, w_ben_gk, w_fztyp where baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and baureihe_vbereich in ('BE', '&KATALOGUMFANG&') and fztyp_baureihe = baureihe_baureihe and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_ktlgausf in (&REGIONEN&) and fztyp_sichtschutz = 'N' and bauart_bauart = baureihe_bauart and ben_textcode = bauart_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Pos";
        }

        public final String RETRIEVE_BAUREIHEN() {
            return "select distinct baureihe_baureihe Baureihe, ben_text ExtBaureihe, baureihe_position Pos from w_baureihe, w_ben_gk, w_fztyp where baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and baureihe_vbereich in ('BE', '&KATALOGUMFANG&') &BAUART_STMT& and baureihe_baureihe = fztyp_baureihe and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_ktlgausf in (&REGIONEN&) &LENKUNG_STMT& and fztyp_sichtschutz = 'N' and baureihe_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Pos";
        }

        public final String GET_GRAFIKID_FOR_BAUREIHE() {
            return "select grafik_grafikid GrafikId, grafik_moddate ModStamp from w_baureihe, w_grafik where baureihe_baureihe = '&BAUREIHE&' and baureihe_grafikid = grafik_grafikid and grafik_art = 'T'";
        }

        public final String GET_GRAFIKID_FOR_BAUREIHE_KAROSSERIE() {
            return "select grafik_grafikid GrafikId, grafik_moddate ModStamp from w_baureihe_kar_thb, w_grafik where baureihekar_baureihe = '&BAUREIHE&' and baureihekar_karosserie = '&KAROSSERIE&' and baureihekar_grafikid = grafik_grafikid and grafik_art = 'T'";
        }

        public final String GET_GRAFIKID_FOR_FIBILD() {
            return "select grafik_grafikid GrafikId, grafik_format Format, grafik_moddate ModStamp from w_etk_grafiken, w_grafik where etkgraf_ablauf = 'FI' and etkgraf_marke = '&MARKE&' and etkgraf_produktart = '&PRODUKTART&' and etkgraf_vbereich = '&KATALOGUMFANG&' and etkgraf_grafikid = grafik_grafikid and grafik_art = 'T'";
        }

        public final String RETRIEVE_KAROSSERIEN() {
            return "select distinct fztyp_karosserie Karosserie, ben_text ExtKarosserie from w_fztyp, w_ben_gk, w_publben where fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_ktlgausf in (&REGIONEN&) &LENKUNG_STMT& and fztyp_sichtschutz = 'N' and publben_art = 'K' and fztyp_karosserie = publben_bezeichnung and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by ExtKarosserie";
        }

        public final String RETRIEVE_MODELLE() {
            return "select distinct fztyp_erwvbez Modell, vbezp_pos Pos from w_vbez_pos, w_fztyp where fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' &KAROSSERIE_STMT& and fztyp_ktlgausf in (&REGIONEN&) &LENKUNG_STMT& and fztyp_sichtschutz = 'N' and vbezp_baureihe = fztyp_baureihe and vbezp_vbez = fztyp_vbez order by Pos, Modell";
        }

        public final String RETRIEVE_REGIONEN() {
            return "select distinct fztyp_ktlgausf Region from w_fztyp where fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' &KAROSSERIE_STMT& and fztyp_erwvbez = '&MODELL&' and fztyp_ktlgausf in (&REGIONEN&) &LENKUNG_STMT& and fztyp_sichtschutz = 'N' order by Region";
        }

        public final String RETRIEVE_LENKUNGEN() {
            return "select distinct fztyp_lenkung Lenkung, ben_text ExtLenkung from w_fztyp, w_ben_gk, w_publben where fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_karosserie = '&KAROSSERIE&' and fztyp_erwvbez = '&MODELL&' and fztyp_ktlgausf = '&REGION&' and fztyp_sichtschutz = 'N' and fztyp_lenkung = publben_bezeichnung and publben_art = 'L' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Lenkung";
        }

        public final String RETRIEVE_GETRIEBEARTEN() {
            return "select distinct fztyp_getriebe Getriebe, ben_text ExtGetriebe from w_fztyp, w_ben_gk, w_publben where fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' and fztyp_karosserie = '&KAROSSERIE&' and fztyp_erwvbez = '&MODELL&' and fztyp_ktlgausf = '&REGION&' &LENKUNG_STMT& and fztyp_sichtschutz = 'N' and fztyp_getriebe = publben_bezeichnung and publben_art = 'G' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Getriebe";
        }

        public final String RETRIEVE_BAUJAHRE() {
            return "select distinct substr(to_char(fgstnr_prod), 1, 4) Baujahr from w_fztyp, w_fgstnr where fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' &KAROSSERIE_STMT& and fztyp_erwvbez = '&MODELL&' and fztyp_ktlgausf = '&REGION&' &LENKUNG_STMT& &GETRIEBE_STMT& and fztyp_sichtschutz = 'N' and fztyp_typschl = fgstnr_typschl order by Baujahr";
        }

        public final String RETRIEVE_ZULASSMONATE() {
            return "select distinct substr(to_char(fgstnr_prod), 5, 2) Zulassungsmonat, ben_text ExtZulassungsmonat from w_fztyp, w_ben_gk, w_publben, w_fgstnr where fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' &KAROSSERIE_STMT& and fztyp_erwvbez = '&MODELL&' and fztyp_ktlgausf = '&REGION&' &LENKUNG_STMT& &GETRIEBE_STMT& and fztyp_sichtschutz = 'N' and fztyp_typschl = fgstnr_typschl and substr(to_char(fgstnr_prod), 1, 4) = '&BAUJAHR&' and substr(to_char(fgstnr_prod), 5, 2) = publben_bezeichnung and publben_art = 'M' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Zulassungsmonat";
        }

        public final String RETRIEVE_ZULASSMONATE2() {
            return "select distinct substr(to_char(fgstnr_prod), 5, 2) Zulassungsmonat, ben_text ExtZulassungsmonat from w_fztyp, w_ben_gk, w_publben, w_fgstnr where fztyp_baureihe = '&BAUREIHE&' and fztyp_vbereich = '&KATALOGUMFANG&' &KAROSSERIE_STMT& and fztyp_erwvbez = '&MODELL&' and fztyp_ktlgausf = '&REGION&' &LENKUNG_STMT& &GETRIEBE_STMT& and fztyp_sichtschutz = 'N' and fztyp_typschl = fgstnr_typschl and substr(to_char(fgstnr_prod), 5, 2) = publben_bezeichnung and publben_art = 'M' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by Zulassungsmonat";
        }

        public final String RETRIEVE_BAUART() {
            return " and baureihe_bauart = '&BAUART&'";
        }

        public final String RETRIEVE_KAROSSERIE() {
            return " and fztyp_karosserie = '&KAROSSERIE&'";
        }

        public final String RETRIEVE_LENKUNG() {
            return " and fztyp_lenkung = '&LENKUNG&'";
        }

        public final String RETRIEVE_GETRIEBE() {
            return " and fztyp_getriebe = '&GETRIEBE&'";
        }

        public final String RETRIEVE_MOSP_BY_ATTRIBUTE_PKW() {
            return "select distinct fztyp_mospid Modellspalte from w_fztyp where fztyp_baureihe = '&BAUREIHE&' and fztyp_karosserie = '&KAROSSERIE&' and fztyp_erwvbez = '&MODELL&' and fztyp_ktlgausf = '&REGION&'";
        }

        public final String RETRIEVE_MOSP_BY_ATTRIBUTE_KRAD() {
            return "select distinct fztyp_mospid Modellspalte from w_fztyp where fztyp_baureihe = '&BAUREIHE&' and fztyp_erwvbez = '&MODELL&' and fztyp_ktlgausf = '&REGION&'";
        }

        public final String RETRIEVE_MOSP_BY_FGSTNR() {
            return "select distinct fgstnr_mospid Modellspalte, fgstnr_typschl Typ, fgstnr_werk Werk, baureihe_marke_tps Marke, baureihe_produktart Produktart, fztyp_vbereich Katalogumfang, fztyp_baureihe Baureihe, b.ben_text ExtBaureihe, baureihe_bauart Bauart, bb.ben_text ExtBauart, fztyp_karosserie Karosserie, bk.ben_text ExtKarosserie, fztyp_motor Motor, fztyp_erwvbez Modell, fztyp_ktlgausf Region, fztyp_lenkung Lenkung, fztyp_getriebe Getriebe, fgstnr_prod Produktionsdatum, fztyp_sichtschutz Sichtschutz, NVL(fztyp_einsatz, 0) Einsatz from w_fgstnr inner join w_fztyp on (fgstnr_typschl = fztyp_typschl and fgstnr_mospid = fztyp_mospid) inner join w_baureihe on (fztyp_baureihe = baureihe_baureihe) inner join w_publben pk on (fztyp_karosserie = pk.publben_bezeichnung and pk.publben_art = 'K') inner join w_publben pb on (baureihe_bauart = pb.publben_bezeichnung and pb.publben_art = 'B') inner join w_ben_gk b on (baureihe_textcode = b.ben_textcode and b.ben_iso = '&ISO&' and b.ben_regiso = '&REGISO&') inner join w_ben_gk bk on (pk.publben_textcode = bk.ben_textcode and bk.ben_iso = '&ISO&' and bk.ben_regiso = '&REGISO&') inner join w_ben_gk bb on (pb.publben_textcode = bb.ben_textcode and bb.ben_iso = '&ISO&' and bb.ben_regiso = '&REGISO&') where fgstnr_von <= '&FGSTNR&' and fgstnr_bis >= '&FGSTNR&' and fgstnr_anf  = '&FGSTNR_FIRST2&'";
        }

        public final String RETRIEVE_MOSP_BY_TYP() {
            return "select distinct fztyp_mospid Modellspalte, fztyp_typschl Typ, null Werk, baureihe_marke_tps Marke, baureihe_produktart Produktart, fztyp_vbereich Katalogumfang, fztyp_baureihe Baureihe, b.ben_text ExtBaureihe, baureihe_bauart Bauart, bb.ben_text ExtBauart, fztyp_karosserie Karosserie, bk.ben_text ExtKarosserie, fztyp_motor Motor, fztyp_erwvbez Modell, fztyp_ktlgausf Region, fztyp_lenkung Lenkung, fztyp_getriebe Getriebe, '&PRODDATUM&' Produktionsdatum, fztyp_sichtschutz Sichtschutz, NVL(fztyp_einsatz, 0) Einsatz from w_fztyp inner join w_baureihe on (fztyp_baureihe = baureihe_baureihe) inner join w_publben pk on (fztyp_karosserie = pk.publben_bezeichnung and pk.publben_art = 'K') inner join w_ben_gk bk on (pk.publben_textcode = bk.ben_textcode and bk.ben_iso = '&ISO&' and bk.ben_regiso = '&REGISO&') inner join w_publben pb on (baureihe_bauart = pb.publben_bezeichnung and pb.publben_art = 'B') inner join w_ben_gk bb on (pb.publben_textcode = bb.ben_textcode and bb.ben_iso = '&ISO&' and bb.ben_regiso = '&REGISO&') inner join w_ben_gk b on (baureihe_textcode = b.ben_textcode and b.ben_iso = '&ISO&' and b.ben_regiso = '&REGISO&') where fztyp_typschl= '&TYP&'";
        }

        public final String RETRIEVE_TYPMENGE_BY_ATTRIBUTE_PKW() {
            return "select distinct fztyp_typschl Typ from w_fztyp, w_fgstnr where fztyp_mospid = &MOSPID& and fztyp_sichtschutz = 'N' and fztyp_lenkung = '&LENKUNG&' and fztyp_getriebe = '&GETRIEBE&' and fztyp_mospid = fgstnr_mospid and fztyp_typschl = fgstnr_typschl and fgstnr_prod = &PRODDATUM&";
        }

        public final String RETRIEVE_TYPMENGE_BY_ATTRIBUTE_KRAD() {
            return "select distinct fztyp_typschl Typ from w_fztyp, w_fgstnr where fztyp_mospid = &MOSPID& and fztyp_sichtschutz = 'N' and fztyp_mospid = fgstnr_mospid and fztyp_typschl = fgstnr_typschl and fgstnr_prod = &PRODDATUM&";
        }

        public final String RETRIEVE_LENKUNG_BEN() {
            return "select ben_text Name from _publben, w_ben_gk where publben_art = 'L' and publben_bezeichnung = '&VALUE&' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'";
        }

        public final String LOAD_SALAS_ZU_FGSTNR() {
            return "select distinct bed_elemid ElementId, bedsala_art || bedsala_pnr || bedsala_hz Code, ben_text Benennung, bed_egid EGruppenId, bedsala_sicher Sicher, bedsala_saz SAZ, DECODE(bedzus_elemid, bedzus_elemid, 'J', 'N') HasBedText, fgstnrs_showtext ShowBedText, eg_exklusiv Exklusiv, eg_pos EGruppenPosition, bedsala_pnr PrimaNr from w_fgstnr_sala inner join w_bed_sala on (fgstnrs_salaid = bedsala_id) inner join w_bed on (bedsala_id = bed_elemid) inner join w_ben_gk on (bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&') inner join w_eg on (bed_egid = eg_id) left join w_bed_zusatzinfo on (bedzus_elemid = fgstnrs_salaid) where fgstnrs_fgstnr = '&FGSTNR&'";
        }

        public final String LOAD_SS_BEDINGUNGEN_AF() {
            return "select distinct bed_elemid ElementId, af.bedafl_code Code, ben_text Benennung, bed_egid EGruppenId, bed_vorhanden_info VorhandenInfo, eg_exklusiv Exklusiv, af.bedafl_art Bedingungsart, eg_pos EGruppenPosition from w_bed_afl af, w_ben_gk, w_bed, w_eg where af.bedafl_art = 'AF' &LOAD_SS_BEDINGUNGEN_AF_DECIDE& and af.bedafl_gilt_v <= &PRODDATUM& and NVL(af.bedafl_gilt_b, 99999999) >= &PRODDATUM& and af.bedafl_id = bed_elemid and bed_egid = eg_id and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'union select distinct bed_elemid ElementId, f.bedafl_code Code, ben_text Benennung, bed_egid EGruppenId, bed_vorhanden_info VorhandenInfo, eg_exklusiv Exklusiv, f.bedafl_art Bedingungsart, eg_pos EGruppenPosition from w_bed_afl af, w_bed_afl f, w_ben_gk, w_bed, w_eg where af.bedafl_art = 'AF' &LOAD_SS_BEDINGUNGEN_AF_DECIDE& and af.bedafl_gilt_v <= &PRODDATUM& and NVL(af.bedafl_gilt_b, 99999999) >= &PRODDATUM& and f.bedafl_code = substr(af.bedafl_code, 3) and f.bedafl_art = 'F' and f.bedafl_gilt_v <= &PRODDATUM& and NVL(f.bedafl_gilt_b, 99999999) >= &PRODDATUM& and f.bedafl_id = bed_elemid and bed_egid = eg_id and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'union select distinct bed_elemid ElementId, a.bedafl_code Code, ben_text Benennung, bed_egid EGruppenId, bed_vorhanden_info VorhandenInfo, eg_exklusiv Exklusiv, a.bedafl_art Bedingungsart, eg_pos EGruppenPosition from w_bed_afl af, w_bed_afl a, w_ben_gk, w_bed, w_eg where af.bedafl_art= 'AF' &LOAD_SS_BEDINGUNGEN_AF_DECIDE& and af.bedafl_gilt_v <= &PRODDATUM& and NVL(af.bedafl_gilt_b, 99999999) >= &PRODDATUM& and a.bedafl_code = substr(af.bedafl_code, 1, 2) and a.bedafl_art = 'A' and a.bedafl_gilt_v <= &PRODDATUM& and NVL(a.bedafl_gilt_b, 99999999) >= &PRODDATUM& and a.bedafl_id = bed_elemid and bed_egid = eg_id and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'";
        }

        public final String LOAD_SS_BEDINGUNGEN_AF_BY_AFCODE() {
            return " and af.bedafl_code = '&CODE&'";
        }

        public final String LOAD_SS_BEDINGUNGEN_AF_BY_POLSTERCODE() {
            return " and af.bedafl_pcode = '&CODE&'";
        }

        public final String LOAD_SS_BEDINGUNGEN_LACK() {
            return "select distinct bed_elemid ElementId, bedafl_code Code, ben_text Benennung, bed_egid EGruppenId, bed_vorhanden_info VorhandenInfo, eg_exklusiv Exklusiv, eg_pos EGruppenPosition from w_bed_afl, w_ben_gk, w_bed, w_eg where bedafl_art= 'L' &LOAD_SS_BEDINGUNGEN_LACK_DECIDE& and bedafl_gilt_v <= &PRODDATUM& and NVL(bedafl_gilt_b, 99999999) >= &PRODDATUM& and bedafl_id = bed_elemid and bed_egid = eg_id and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'";
        }

        public final String LOAD_SS_BEDINGUNGEN_LACK_EXACT() {
            return " and bedafl_code = '&CODE&'";
        }

        public final String LOAD_SS_BEDINGUNGEN_LACK_LIKE() {
            return " and bedafl_code like '%&CODE&'";
        }

        public final String LOAD_SS_BEDINGUNGEN_SALAPA() {
            return "select distinct bed_elemid ElementId, bedsala_art || bedsala_pnr || bedsala_hz Code, ben_text Benennung, bed_egid EGruppenId, bed_vorhanden_info VorhandenInfo, eg_exklusiv Exklusiv, bedsala_sicher Sicher, bedsala_saz SAZ, eg_pos EGruppenPosition, bedsala_pnr PrimaNr from w_bed_sala, w_ben_gk, w_bed, w_eg where bedsala_produktart = '&PRODUKTART&' and bedsala_pnr = '&PRIMANUMMER&' and bedsala_gilt_v <= &PRODDATUM& and NVL(bedsala_gilt_b, 99999999) >= &PRODDATUM& and bedsala_art not in ('N', 'V') &LOAD_SS_BEDINGUNGEN_SALAPA_DECIDE_ART& &LOAD_SS_BEDINGUNGEN_SALAPA_DECIDE_HZAEHLER& and bedsala_id = bed_elemid and bed_egid = eg_id and bed_textcode = ben_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'";
        }

        public final String LOAD_SS_BEDINGUNGEN_SALAPA_BY_ART() {
            return " and bedsala_art = '&ART&'";
        }

        public final String LOAD_SS_BEDINGUNGEN_SALAPA_BY_HZEAHLER() {
            return " and bedsala_hz = '&HZAEHLER&'";
        }

        public final String RETRIEVE_BED_ZUSATZINFO() {
            return "select komm_id KommId, ben_text Ben, komm_pos Pos from w_komm, w_ben_gk where komm_id in   (select bedzus_kommid     from w_bed_zusatzinfo     where bedzus_elemid IN (&BED_ELEMIDS&)   diff    select bedzus_kommid     from w_bed_zusatzinfo     where bedzus_elemid NOT IN (&BED_ELEMIDS&)) and   komm_textcode = ben_textcode and   ben_iso = '&ISO&' and   ben_regiso = '&REGISO&' order by KommId, Pos";
        }
    }

    public static final class Einstellungen
    extends SQLStatements.Einstellungen {
        public final String RETRIEVE_EINSTELLUNGEN() {
            return "select user_marke,  user_produktart, user_lenkung,  user_katalogumfang, user_iso,  user_regiso,  user_expand_bnb,  user_short_searchpath, user_request_saz, user_show_proddate,  user_dft_verbaumenge  from w_user_einstellungen  where user_id = '&ID&'";
        }

        public final String RETRIEVE_EINSTELLUNGEN_JAVA() {
            return "select user_marke, user_produktart, user_lenkung, user_katalogumfang, user_iso, user_regiso, user_expand_bnb, user_short_searchpath, user_request_saz, user_show_proddate, user_suchraum, user_show_preise, user_show_tipps, user_primaermarkt_id, user_tablestretch, user_fontsize,  user_dft_verbaumenge,  user_aufbewahrung  from w_user_einstellungen@etk_nutzer where user_firma_id = '&FIRMAID&' and user_id = '&ID&'";
        }

        public final String RETRIEVE_EINSTELLUNGEN_MARKTID_JAVA() {
            return "select user_marktid Id, marktipac_kuerzel Kuerzel, marktipac_lkz Lkz, marktipac_relevanz_pa Produktart from w_user@etk_nutzer, w_markt_ipac@etk_publ where user_id = '&ID&' and user_firma_id = '&FIRMAID&' and marktipac_id = user_marktid";
        }

        public final String RETRIEVE_IPAC_MARKT() {
            return "select marktipac_kuerzel Kuerzel, marktipac_lkz Lkz from w_markt_ipac@etk_publ where marktipac_id = &ID&";
        }

        public final String UPDATE_MARKTID() {
            return "update w_user  set user_marktid = ? where user_firma_id = coalesce(?,user_firma_id) and user_id = coalesce(?,user_id)";
        }

        public final String RETRIEVE_EINSTELLUNGEN_REGIONEN() {
            return "select user_region from w_user_einstellungen_region@etk_nutzer where user_id = '&ID&'";
        }

        public final String RETRIEVE_EINSTELLUNGEN_REGIONEN_JAVA() {
            return "select user_region from w_user_einstellungen_region@etk_nutzer where user_firma_id = '&FIRMAID&' and user_id = '&ID&'";
        }

        public final String DELETE_EINSTELLUNGEN() {
            return "delete from w_user_einstellungen@etk_nutzer where user_id = '&ID&'";
        }

        public final String DELETE_EINSTELLUNGEN_JAVA() {
            return "delete from w_user_einstellungen@etk_nutzer where user_firma_id = '&FIRMAID&' and user_id = '&ID&'";
        }

        public final String INSERT_EINSTELLUNGEN() {
            return "insert into w_user_einstellungen@etk_nutzer (user_id, user_marke, user_produktart, user_lenkung, user_katalogumfang, user_iso, user_regiso, user_expand_bnb, user_short_searchpath,  user_request_saz, user_show_proddate, user_dft_verbaumenge ) values ('&ID&', '&MARKE&', '&PRODUKTART&', '&LENKUNG&', '&KATALOGUMFANG&', '&ISO&', '&REGISO&', '&EXPAND_BNB&', '&SHORT_SEARCHPATH&', '&REQUEST_SAZ&', &SHOW_PRODDATE&, '&VERBAUMENGE&' )";
        }

        public final String INSERT_EINSTELLUNGEN_JAVA() {
            return "insert into w_user_einstellungen@etk_nutzer (user_firma_id, user_id, user_marke, user_produktart, user_lenkung, user_katalogumfang, user_iso, user_regiso, user_expand_bnb, user_short_searchpath, user_request_saz, user_show_proddate, user_suchraum,  user_show_preise, user_show_tipps, user_primaermarkt_id, user_tablestretch, user_fontsize,user_dft_verbaumenge,  user_aufbewahrung)  values ('&FIRMAID&', '&ID&', '&MARKE&', '&PRODUKTART&', '&LENKUNG&', '&KATALOGUMFANG&', '&ISO&', '&REGISO&', '&EXPAND_BNB&', '&SHORT_SEARCHPATH&', '&REQUEST_SAZ&', &SHOW_PRODDATE&, '&SUCHRAUM&', &SHOW_PREISE&, &SHOW_TIPPS&, &PRIMAER_MARKT_ID&, '&TABLESTRETCH&', '&FONTSIZE&', '&VERBAUMENGE&', '&AUFBEWAHRUNG&')";
        }

        public final String RETRIEVE_SPRACHEN() {
            return "select ben_iso ISO, ben_regiso RegISO, ben_text Benennung from w_ben_gk, w_publben where publben_art = 'S' and ben_textcode = publben_textcode and ben_iso = substr(publben_bezeichnung, 1, 2) and ben_regiso = substr(publben_bezeichnung, 3, 2) order by ben_iso, ben_regiso";
        }

        public final String DELETE_EINSTELLUNGEN_REGIONEN() {
            return "delete from w_user_einstellungen_region@etk_nutzer where user_id = '&ID&'";
        }

        public final String DELETE_EINSTELLUNGEN_REGIONEN_JAVA() {
            return "delete from w_user_einstellungen_region@etk_nutzer where user_firma_id = '&FIRMAID&' and user_id = '&ID&'";
        }

        public final String INSERT_EINSTELLUNGEN_REGIONEN() {
            return "insert into w_user_einstellungen_region@etk_nutzer (user_id, user_region) values ('&ID&', '&REGION&')";
        }

        public final String INSERT_EINSTELLUNGEN_REGIONEN_JAVA() {
            return "insert into w_user_einstellungen_region@etk_nutzer (user_firma_id, user_id, user_region) values ('&FIRMAID&', '&ID&', '&REGION&')";
        }

        public final String RETRIEVE_REGIONEN() {
            return "select distinct fztyp_ktlgausf REGIONEN from  w_fztyp";
        }

        public final String RETRIEVE_COUNT_MODSPALTEN() {
            return "select count (distinct fztyp_mospid) from w_baureihe, w_fztyp where baureihe_marke_tps = '&MARKE&' and baureihe_produktart = '&PRODUKTART&' and baureihe_baureihe = fztyp_baureihe and &LENKUNG_STMT& fztyp_vbereich = '&KATALOGUMFANG&' and  fztyp_ktlgausf in (&REGIONEN&)";
        }

        public final String RETRIEVE_COUNT_MODSPALTEN_LENKUNG() {
            return "fztyp_lenkung = '&LENKUNG&' and";
        }

        public final String RETRIEVE_RECHTE_JAVA() {
            return "select userf_recht_id RechtId from w_user_funktionsrechte@etk_nutzer where userf_id = '&ID&' and userf_firma_id = '&FIRMA&'";
        }

        public final String RETRIEVE_RECHTE() {
            return "select userf_recht_id RechtId from w_user_funktionsrechte where userf_id = '&ID&'";
        }

        public final String RETRIEVE_BERECHTIGUNGEN() {
            return "select userb_art Art, userb_wert Wert from w_user_berechtigungen@etk_nutzer where userb_firma_id = '&FIRMAID&' and  userb_id = '&ID&' order by Art";
        }

        public final String DELETE_TEILENOTIZEN_ABGELAUFEN() {
            return "delete from w_teileinfo@etk_nutzer where teileinfo_firma_id = '&FIRMAID&' and  teileinfo_user_id = '&ID&' and (teileinfo_gueltig_bis_jahr <= &JAHR& and  teileinfo_gueltig_bis_monat < &MONAT& and  teileinfo_gueltig_bis_monat is not null) or  (teileinfo_gueltig_bis_jahr < &JAHR& and  teileinfo_gueltig_bis_monat is null)";
        }

        public final String RETRIEVE_MAERKTE_ETK_LOKALE_PRODUKTE() {
            return "select marktetk_id id, ben_text name, marktetk_isokz kuerzel, marktetk_lkz lkz from w_markt_etk, w_ben_gk where marktetk_anzlokbt > 0 and ben_textcode = marktetk_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by ben_text";
        }

        public final String RETRIEVE_ETK_MARKT() {
            return "select ben_text name, marktetk_isokz kuerzel, marktetk_lkz lkz from w_markt_etk, w_ben_gk where marktetk_id = &ID& and ben_textcode = marktetk_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&'";
        }

        public final String RETRIEVE_WEITERE_MAERKTE() {
            return "select user_markt_id, ben_text name, marktetk_isokz kuerzel, marktetk_lkz lkz from w_user_einstellungen_wmaerkte@etk_nutzer, w_markt_etk, w_ben_gk where user_firma_id = '&FIRMA&' and user_id = '&ID&' and marktetk_id = user_markt_id and ben_textcode = marktetk_textcode and ben_iso = '&ISO&' and ben_regiso = '&REGISO&' order by ben_text";
        }

        public final String DELETE_WEITERE_MAERKTE() {
            return "delete from w_user_einstellungen_wmaerkte@etk_nutzer where user_firma_id = '&FIRMA&' and user_id = '&USERID&'";
        }

        public final String INSERT_WEITERE_MAERKTE() {
            return "insert into w_user_einstellungen_wmaerkte@etk_nutzer (user_firma_id, user_id, user_markt_id) VALUES ('&FIRMA&', '&USERID&', &MARKTID&)";
        }
    }

    public static final class Wertebereiche
    extends SQLStatements.Wertebereiche {
        public final String RETRIEVE_WERTEBEREICH() {
            return "select publben_bezeichnung Value, ben_text Name  from w_publben, w_ben_gk where publben_art = '&ART&' and publben_textcode = ben_textcode and ben_iso = '&ISO&' and  ben_regiso = '&REGISO&'";
        }
    }

    public static final class Allgemein
    extends SQLStatements.Allgemein {
        private final String NVARCHAR_SUBSTITUTE = "";

        public String getLikeInsensitive(String feldName, String wert, boolean isNVarchar) {
            return " " + feldName + " LIKE INSENSITIVE " + (isNVarchar ? "" : "") + "'" + wert + "'";
        }

        public String getNotLikeInsensitive(String feldName, String wert, boolean isNVarchar) {
            return " " + feldName + " NOT LIKE INSENSITIVE " + (isNVarchar ? "" : "") + "'" + wert + "'";
        }

        public String getLikeTr(String feldName, String wert, boolean isNVarchar) {
            return " upper(replace('i' by '\u0130' in replace('\u0131' by 'I' in " + feldName + "))) LIKE " + (isNVarchar ? "" : "") + "'" + wert.toUpperCase(new Locale("tr", "TR")) + "'";
        }

        public String getNotLikeTr(String feldName, String wert, boolean isNVarchar) {
            return " upper(replace('i' by '\u0130' in replace('\u0131' by 'I' in " + feldName + "))) NOT LIKE " + (isNVarchar ? "" : "") + "'" + wert.toUpperCase(new Locale("tr", "TR")) + "'";
        }

        public String LOAD_GRAFIK() {
            return "select grafik_blob Grafik, grafik_format Format, grafik_moddate ModStamp from w_grafik where grafik_grafikid = &GRAFIKID& and grafik_art = '&ART&'";
        }

        public final String INSERT_GRAFIK_STATISTIK() {
            return "nur fuer oracle";
        }

        public final String RETRIEVE_BMWSACHNUMMER_FOR_FREMDESACHNUMMER() {
            return "select distinct fremdtl_sachnr Sachnummer from w_fremdtl where fremdtl_fremdsnr = '&SACHNUMMER_FREMD&'";
        }

        public final String RETRIEVE_URLS() {
            return "select url_url URL from w_url where upper(url_type)='&TYPE&' and upper(url_iso)='&ISO&' and upper(url_regiso)='&REGISO&' and upper(url_marke_tps)='&MARKE&'";
        }

        public final String RETRIEVE_BMW_NETZ() {
            return "select netz_netz Netz, netz_krit Krit from w_netz";
        }

        public final String RETRIEVE_BMW_NETZURL() {
            return "select netzurl_url_asap AsapUrl, netzurl_asaptunnel AsapTunnel, netzurl_url_zr CentralURL, netzurl_url_dom_basic IGDOMBasicsUrl, netzurl_url_dom_options IGDOMOptionsUrl from w_netzurl where netzurl_netz = '&NETZ&' and netzurl_krit = '&KRIT&'";
        }

        public final String RETRIEVE_BMW_PROXY() {
            return "select proxy_proxyname ProxyName, proxy_port Port, proxy_nutzername UserName, proxy_passwort Passwort, proxy_realm Realm, proxy_ntdomain NtHost, proxy_nthost NtDomain from w_proxy";
        }

        public final String RETRIEVE_SOWU_ANTWORT() {
            return "nur fuer oracle";
        }

        public final String DELETE_SOWU_ANTWORT() {
            return "nur fuer oracle";
        }
    }
}
