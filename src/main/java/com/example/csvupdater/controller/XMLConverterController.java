package com.example.csvupdater.controller;

import com.example.csvupdater.Article;
import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller()
public class XMLConverterController {

    private Map<String, Integer> editionCount = new HashMap<>();

    @PostMapping("/xml/parse")
    public ResponseEntity<String> convertToAUPContent() throws JAXBException {

        try {
            File sourceDir = new File("./aupsources");
            File[] files = sourceDir.listFiles();
            for (File file : files) {
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new FileReader(file));

                String readline = "";
                while (readline != null) {
                    readline = reader.readLine();
                    if (readline != null) {
                        stringBuilder.append(readline);
                    }
                }

                Document sourceDocument = Jsoup.parse(stringBuilder.toString(), "", Parser.xmlParser());

                Article article = new Article();
                //Front
                Article.Front front = new Article.Front();

                Article.Front.JournalMeta journalMeta = new Article.Front.JournalMeta();

                Article.Front.JournalMeta.JournalId journalId = new Article.Front.JournalMeta.JournalId();
                journalId.setJournalIdType("publisher-id");
                journalId.setValue("TvAR");
                journalMeta.setJournalId(journalId);

                Article.Front.JournalMeta.JournalTitleGroup journalTitleGroup = new Article.Front.JournalMeta.JournalTitleGroup();
                journalTitleGroup.setJournalTitle("Tijdschrift voor Agrarisch Recht");
                journalMeta.setJournalTitleGroup(journalTitleGroup);

                Article.Front.JournalMeta.Issn issn = new Article.Front.JournalMeta.Issn();
                issn.setPubType("ppub");
                issn.setValue("1874-9674");

                Article.Front.JournalMeta.Issn issn1 = new Article.Front.JournalMeta.Issn();
                issn1.setPubType("epub");
                issn1.setValue("2666-4364");

                journalMeta.getIssn().add(issn);
                journalMeta.getIssn().add(issn1);

                Article.Front.JournalMeta.Publisher publisher = new Article.Front.JournalMeta.Publisher();
                publisher.setPublisherLoc("Amsterdam");
                publisher.setPublisherName("Amsterdam University Press");

                journalMeta.setPublisher(publisher);

                front.setJournalMeta(journalMeta);


                Article.Front.ArticleMeta articleMeta = new Article.Front.ArticleMeta();
                Article.Front.ArticleMeta.ArticleId articleId1 = new Article.Front.ArticleMeta.ArticleId();
                articleId1.setPubIdType("publisher-id");
                String published = sourceDocument.select("edition > articles > article > published").text().substring(0, 4);
                String uitgave = sourceDocument.select("edition > articles > article > uitgave").text();
                if (editionCount.containsKey(published + ":" + uitgave)) {
                    Integer integer = editionCount.get(published + ":" + uitgave);
                    editionCount.put(published + ":" + uitgave, integer + 1);
                } else {
                    editionCount.put(published + ":" + uitgave, 1);
                }
                String editionNumber = "";
                if (editionCount.containsKey(published + ":" + uitgave)) {
                    Integer integer = editionCount.get(published + ":" + uitgave);
                    if (integer < 10) {
                        editionNumber = "00" + integer;
                    }
                    if (integer >= 10 && integer < 100) {
                        editionNumber = "0" + integer;
                    }

                    if (integer >= 100) {
                        editionNumber = integer + "";
                    }
                }

                String articleNumber = "TVAR" + published.substring(0, 4) + "." + uitgave + "." + editionNumber;
                articleId1.setValue(articleNumber); // edition/articles/article/published (first 4 digits) edition/articles/article/uitgave  TVAR2023.1.005

                Article.Front.ArticleMeta.ArticleId articleId2 = new Article.Front.ArticleMeta.ArticleId();
                articleId2.setPubIdType("doi");
                //TODO: Ask Rene about AANK suffix
                articleId2.setValue("10.5117/" + articleNumber); // prefix above value with '10.5117/'  10.5117/TvAR2023.1.005.AANK


                articleMeta.getArticleId().add(articleId1);
                articleMeta.getArticleId().add(articleId2);

                String title = sourceDocument.select("edition > articles > article > titel").text();
                Article.Front.ArticleMeta.TitleGroup titleGroup = new Article.Front.ArticleMeta.TitleGroup();
                titleGroup.setArticleTitle(title); //: edition/articles/article/title
                articleMeta.setTitleGroup(titleGroup);

                Article.Front.ArticleMeta.ArticleCategories articleCategories = new Article.Front.ArticleMeta.ArticleCategories();
                Article.Front.ArticleMeta.ArticleCategories.SubjGroup subjGroup = new Article.Front.ArticleMeta.ArticleCategories.SubjGroup();
                if (title != null && !title.isEmpty()) {
                    if (title.contains("ECLI:")) {
                        subjGroup.setSubject("Rechtspraak"); // If title contains 'ECLI:' -> Rechtspraak else Artikel
                    } else {
                        subjGroup.setSubject("Artikel");
                    }
                }
                articleCategories.setSubjGroup(subjGroup);
                articleMeta.setArticleCategories(articleCategories);


                Article.Front.ArticleMeta.ContribGroup contribGroup = new Article.Front.ArticleMeta.ContribGroup();
                Article.Front.ArticleMeta.ContribGroup.Contrib contrib = new Article.Front.ArticleMeta.ContribGroup.Contrib();
                contrib.setContribType("author");
                Article.Front.ArticleMeta.ContribGroup.Contrib.Name name = new Article.Front.ArticleMeta.ContribGroup.Contrib.Name();
                String authors = sourceDocument.select("edition > articles > article > auteurs").text();
                name.setSurname(authors); // edition/articles/article/auteurs
                name.setGivenNames(authors); // edition/articles/article/auteurs
                name.setPrefix("");
                contrib.setName(name);
                contribGroup.setContrib(contrib);
                articleMeta.setContribGroup(contribGroup);

                Article.Front.ArticleMeta.PubDate pubDate = new Article.Front.ArticleMeta.PubDate();
                pubDate.setPubType("epub");
                String published1 = sourceDocument.select("edition > articles > article > published").text();
                String substring = published1.substring(published1.indexOf("-") + 1, published1.lastIndexOf("-"));
                pubDate.setMonth(Short.parseShort(substring)); //edition/articles/article/published	digits between '-'
                pubDate.setYear(Integer.parseInt(published1.substring(0, 4))); //: edition/articles/article/published first 4 digits
                articleMeta.setPubDate(pubDate);

//                articleMeta.setVolume((short) 0); //TODO ask RENE about value, not clear // LEFT EMPTY
                String replacedUitgave = uitgave.replace("/", "-");
                articleMeta.setIssue(replacedUitgave); //TODO: edition/articles/article/uitgave	for double nrs use a '-' so 11/12 will be 11-12
//                articleMeta.setFpage((short) 0); // does not exist do not fill, in mapping for publicationnumber we will use edition if 'fpage' is empty

                front.setArticleMeta(articleMeta);

                article.setFront(front);

                String text1 = sourceDocument.select("edition > articles > article > inleiding").text();
                String text2 = sourceDocument.select("edition > articles > article > content").text();
                String text3 = sourceDocument.select("edition > articles > article > content > footnotes > footnote").text();
                StringBuilder stringBuilder1 = new StringBuilder();
                StringBuilder append = stringBuilder1.append(text1).append(text2).append(text3);
                article.setBody(append.toString()); // edition/articles/article/inleiding
                // : edition/articles/article/content
                //  edition/articles/article/content/footnotes/footnote


                JAXBContext context = JAXBContext.newInstance(Article.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://ns.editeur.org/onix/3.0/reference ONIX_BookProduct_3.0_reference.xsd");
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.setProperty(CharacterEscapeHandler.class.getName(),
                        (CharacterEscapeHandler) (ac, i, j, flag, writer) -> writer.write(ac, i, j));

                File dir = new File("./aup/" + published + ":" + replacedUitgave);
                dir.mkdir();
                marshaller.marshal(article, new File("./aup/" + published + ":" + replacedUitgave + "/" + articleNumber.replaceAll("/", "-") + ".xml"));

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

