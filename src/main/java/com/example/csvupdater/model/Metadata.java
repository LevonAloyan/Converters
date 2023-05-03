package com.example.csvupdater.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByNames;
import com.opencsv.bean.CsvCustomBindByName;

import javax.xml.transform.sax.SAXResult;
import java.time.LocalDate;

public class Metadata {

    private String Documentcode;
    private String Titel;
    private String Ondertitel;
    private String AuteursEnAuteursfuncties;
    private Integer JaarVanUitgave;
    private String Verschijningsdatum;
    private String Embargodatum;
    private Integer Omvang;
    private String PlaatsVanUitgave;
    private String NaamVanUitgever;
    private String SerietitelCqReekstitel;
    private String SerienrCqReeksnr;
    private String Rubriekcode;
    private String Rubrieksomschrijving;
    private String Rubrieksomschrijving2;
    private String Rubrieksomschrijving3;
    private Integer Druk;
    private String Prijs;
    private String Productstatus;
    private Long ISBN13EAN;
    private Long ISBNFolioBk;
    private String Nummer;
    private String MediaProductFamily;
    private String Producttekst;
    private String SamenvattingAbstract;
    private String Wijzigingsstatus;
    private String Wijzigingsdatum;

    public String getDocumentcode() {
        return Documentcode;
    }

    public void setDocumentcode(String documentcode) {
        Documentcode = documentcode;
    }

    public String getTitel() {
        return Titel;
    }

    public void setTitel(String titel) {
        Titel = titel;
    }

    public String getOndertitel() {
        return Ondertitel;
    }

    public void setOndertitel(String ondertitel) {
        Ondertitel = ondertitel;
    }

    public String getAuteursEnAuteursfuncties() {
        return AuteursEnAuteursfuncties;
    }

    public void setAuteursEnAuteursfuncties(String auteursEnAuteursfuncties) {
        AuteursEnAuteursfuncties = auteursEnAuteursfuncties;
    }

    public Integer getJaarVanUitgave() {
        return JaarVanUitgave;
    }

    public void setJaarVanUitgave(Integer jaarVanUitgave) {
        JaarVanUitgave = jaarVanUitgave;
    }

    public String getVerschijningsdatum() {
        return Verschijningsdatum;
    }

    public void setVerschijningsdatum(String verschijningsdatum) {
        Verschijningsdatum = verschijningsdatum;
    }

    public String getEmbargodatum() {
        return Embargodatum;
    }

    public void setEmbargodatum(String embargodatum) {
        Embargodatum = embargodatum;
    }

    public Integer getOmvang() {
        return Omvang;
    }

    public void setOmvang(Integer omvang) {
        Omvang = omvang;
    }

    public String getPlaatsVanUitgave() {
        return PlaatsVanUitgave;
    }

    public void setPlaatsVanUitgave(String plaatsVanUitgave) {
        PlaatsVanUitgave = plaatsVanUitgave;
    }

    public String getNaamVanUitgever() {
        return NaamVanUitgever;
    }

    public void setNaamVanUitgever(String naamVanUitgever) {
        NaamVanUitgever = naamVanUitgever;
    }

    public String getSerietitelCqReekstitel() {
        return SerietitelCqReekstitel;
    }

    public void setSerietitelCqReekstitel(String serietitelCqReekstitel) {
        SerietitelCqReekstitel = serietitelCqReekstitel;
    }

    public String getSerienrCqReeksnr() {
        return SerienrCqReeksnr;
    }

    public void setSerienrCqReeksnr(String serienrCqReeksnr) {
        SerienrCqReeksnr = serienrCqReeksnr;
    }

    public String getRubriekcode() {
        return Rubriekcode;
    }

    public void setRubriekcode(String rubriekcode) {
        Rubriekcode = rubriekcode;
    }

    public String getRubrieksomschrijving() {
        return Rubrieksomschrijving;
    }

    public void setRubrieksomschrijving(String rubrieksomschrijving) {
        Rubrieksomschrijving = rubrieksomschrijving;
    }

    public String getRubrieksomschrijving2() {
        return Rubrieksomschrijving2;
    }

    public void setRubrieksomschrijving2(String rubrieksomschrijving2) {
        Rubrieksomschrijving2 = rubrieksomschrijving2;
    }

    public String getRubrieksomschrijving3() {
        return Rubrieksomschrijving3;
    }

    public void setRubrieksomschrijving3(String rubrieksomschrijving3) {
        Rubrieksomschrijving3 = rubrieksomschrijving3;
    }

    public Integer getDruk() {
        return Druk;
    }

    public void setDruk(Integer druk) {
        Druk = druk;
    }

    public String getPrijs() {
        return Prijs;
    }

    public void setPrijs(String prijs) {
        Prijs = prijs;
    }

    public String getProductstatus() {
        return Productstatus;
    }

    public void setProductstatus(String productstatus) {
        Productstatus = productstatus;
    }

    public Long getISBN13EAN() {
        return ISBN13EAN;
    }

    public void setISBN13EAN(Long ISBN13EAN) {
        this.ISBN13EAN = ISBN13EAN;
    }

    public Long getISBNFolioBk() {
        return ISBNFolioBk;
    }

    public void setISBNFolioBk(Long ISBNFolioBk) {
        this.ISBNFolioBk = ISBNFolioBk;
    }

    public String getNummer() {
        return Nummer;
    }

    public void setNummer(String nummer) {
        Nummer = nummer;
    }

    public String getMediaProductFamily() {
        return MediaProductFamily;
    }

    public void setMediaProductFamily(String mediaProductFamily) {
        MediaProductFamily = mediaProductFamily;
    }

    public String getProducttekst() {
        return Producttekst;
    }

    public void setProducttekst(String producttekst) {
        Producttekst = producttekst;
    }

    public String getSamenvattingAbstract() {
        return SamenvattingAbstract;
    }

    public void setSamenvattingAbstract(String samenvattingAbstract) {
        SamenvattingAbstract = samenvattingAbstract;
    }

    public String getWijzigingsstatus() {
        return Wijzigingsstatus;
    }

    public void setWijzigingsstatus(String wijzigingsstatus) {
        Wijzigingsstatus = wijzigingsstatus;
    }

    public String getWijzigingsdatum() {
        return Wijzigingsdatum;
    }

    public void setWijzigingsdatum(String wijzigingsdatum) {
        Wijzigingsdatum = wijzigingsdatum;
    }
}
