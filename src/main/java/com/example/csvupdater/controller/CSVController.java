//package com.example.csvupdater.controller;
//
//import com.example.csvupdater.ONIXMessage;
//import com.example.csvupdater.model.Book;
//import com.example.csvupdater.model.Metadata;
//import com.opencsv.bean.ColumnPositionMappingStrategy;
//import com.opencsv.bean.CsvToBean;
//import com.opencsv.bean.CsvToBeanBuilder;
//import com.opencsv.bean.HeaderColumnNameMappingStrategy;
//import com.opencsv.bean.MappingStrategy;
//import com.opencsv.bean.StatefulBeanToCsv;
//import com.opencsv.bean.StatefulBeanToCsvBuilder;
//import com.opencsv.exceptions.CsvDataTypeMismatchException;
//import com.opencsv.exceptions.CsvException;
//import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
//import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Marshaller;
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Controller
//public class CSVController {
//
//    @PostMapping("/csv/upload")
//    public ResponseEntity<String> uploadCSV(@RequestParam("metadata") MultipartFile metadata) throws IOException, CsvException, JAXBException {
//        List<Metadata> metadatas = parseMetadataCSV(metadata);
//
//        List<ONIXMessage> collect = metadatas.stream().map(this::mapToONIXMessage).collect(Collectors.toList());
//        JAXBContext context = JAXBContext.newInstance(ONIXMessage.class);
//        Marshaller marshaller = context.createMarshaller();
//        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://ns.editeur.org/onix/3.0/reference ONIX_BookProduct_3.0_reference.xsd");
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//        marshaller.setProperty(CharacterEscapeHandler.class.getName(),
//                (CharacterEscapeHandler) (ac, i, j, flag, writer) -> writer.write(ac, i, j));
//        for (ONIXMessage onixMessage : collect) {
//            String name = onixMessage.getProduct().getRelatedMaterial().getRelatedProduct().getProductIdentifier().getIDValue();
//            String substring = name.substring(name.indexOf("[") + 7, name.lastIndexOf("]") - 1);
//            marshaller.marshal(onixMessage, new File("./files/" + substring + ".xml"));
//
//        }
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @PostMapping("/csv/updateBookPrices")
//    public ResponseEntity<String> updateBookPrices(@RequestParam("metadataCSV") MultipartFile metadataCSV, @RequestParam("newPriceCSV") MultipartFile newPriceCSV) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
//        List<Book> newPrices = parseNewPriceCSV(newPriceCSV);
//
//        List<Metadata> metadatas = parseMetadataCSV(metadataCSV);
//
//        for (Book newPrice : newPrices) {
//            for (Metadata metadata : metadatas) {
//                if (newPrice.getISBN().equals(metadata.getISBNFolioBk())) {
//                    metadata.setPrijs(newPrice.getPrijsNieuw());
//                }
//            }
//        }
//
//        FileWriter fileWriter = null;
//        try {
//            fileWriter = new FileWriter("./updatedMetadata/metadata1.csv");
//
//            HeaderColumnNameMappingStrategy<Metadata> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
//            mappingStrategy.setType(Metadata.class);
//            StatefulBeanToCsv<Metadata> beanToCsv = new StatefulBeanToCsvBuilder<Metadata>(fileWriter)
//                    .withMappingStrategy(mappingStrategy)
//                    .withSeparator(',')
//                    .withOrderedResults(true)
//                    .build();
//            beanToCsv.write(metadatas);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (fileWriter != null) {
//                try {
//                    fileWriter.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//
//    private ONIXMessage mapToONIXMessage(Metadata metadata) {
//        ONIXMessage onixMessage = new ONIXMessage();
//
//        ONIXMessage.Header header = new ONIXMessage.Header();
//        ONIXMessage.Header.Sender sender = new ONIXMessage.Header.Sender();
//        sender.setSenderName("");
//        header.setSender(sender);
//        header.setDefaultCurrencyCode("");
//        header.setSentDateTime("");
//        header.setDefaultPriceType("");
//
//
//        ONIXMessage.Product product = new ONIXMessage.Product();
//
//        product.setRecordReference("");
//        product.setNotificationType("");
//
//        ONIXMessage.Product.ProductIdentifier productIdentifier1 = new ONIXMessage.Product.ProductIdentifier();
//        productIdentifier1.setProductIDType("");
//        productIdentifier1.setIDValue("");
//
//        product.setProductIdentifier(productIdentifier1);
//
//        ONIXMessage.Product.DescriptiveDetail descriptiveDetail = createDescriptiveDetail(metadata);
//        product.setDescriptiveDetail(descriptiveDetail);
//
//        descriptiveDetail.setProductComposition("");
//        descriptiveDetail.setProductForm("");
//
//
//        ONIXMessage.Product.PublishingDetail publishingDetail = new ONIXMessage.Product.PublishingDetail();
//        ONIXMessage.Product.PublishingDetail.Publisher publisher = createPublisher(metadata);
//
//        publishingDetail.setPublisher(publisher);
//        product.setPublishingDetail(publishingDetail);
//
//        ONIXMessage.Product.DescriptiveDetail.TitleDetail.TitleElement titleElement = createTitleElement(metadata);
//        ONIXMessage.Product.DescriptiveDetail.TitleDetail titleDetail = createTitleDetail(titleElement);
//        titleDetail.setTitleType("");
//        titleElement.setTitleElementLevel("");
//        titleElement.setSubtitle("");
//        descriptiveDetail.setTitleDetail(titleDetail);
//
//
//        ONIXMessage.Product.DescriptiveDetail.Collection collection = new ONIXMessage.Product.DescriptiveDetail.Collection();
//        collection.setCollectionType(wrapToCdata("10"));
//        ONIXMessage.Product.DescriptiveDetail.Collection.TitleDetail collectionTitleDetail = new ONIXMessage.Product.DescriptiveDetail.Collection.TitleDetail();
//        ONIXMessage.Product.DescriptiveDetail.Collection.TitleDetail.TitleElement collectionTitleElement = new ONIXMessage.Product.DescriptiveDetail.Collection.TitleDetail.TitleElement();
//        collectionTitleElement.setPartNumber(wrapToCdata(metadata.getSerienrCqReeksnr()));
//        collectionTitleElement.setTitleText(wrapToCdata(metadata.getSerietitelCqReekstitel()));
//        collectionTitleElement.setTitleElementLevel("");
//        collectionTitleDetail.setTitleElement(collectionTitleElement);
//        collectionTitleDetail.setTitleType("");
//        collection.setTitleDetail(collectionTitleDetail);
//        descriptiveDetail.setCollection(collection);
//
//        List<ONIXMessage.Product.DescriptiveDetail.Contributor> contributors = descriptiveDetail.getContributor();
////        Keulen, B.F.|Dijk, A.A. van|Bröring, H.E.|Buwalda, M.E.|Postma, A.
//        String authors = metadata.getAuteursEnAuteursfuncties();
//        String[] splittedAuthors = authors.split("\\|");
//        for (int i = 0; i < splittedAuthors.length; i++) {
//            ONIXMessage.Product.DescriptiveDetail.Contributor contributor = new ONIXMessage.Product.DescriptiveDetail.Contributor();
//            contributor.setSequenceNumber(wrapToCdata(String.valueOf(i + 1)));
//            contributor.setContributorRole(wrapToCdata("A01"));
//            contributor.setPersonName(wrapToCdata(splittedAuthors[i]));
//            contributors.add(contributor);
//        }
//
//        ONIXMessage.Product.DescriptiveDetail.Extent extent = new ONIXMessage.Product.DescriptiveDetail.Extent();
//        extent.setExtentType("");
//        extent.setExtentValue("");
//        extent.setExtentUnit("");
//
//        descriptiveDetail.setExtent(extent);
//
//        ONIXMessage.Product.DescriptiveDetail.Subject subject = new ONIXMessage.Product.DescriptiveDetail.Subject();
//        subject.setSubjectSchemeIdentifier(wrapToCdata("23"));
//        String lawArea = metadata.getRubrieksomschrijving();
//        String wrapedLawArea = wrapToCdata(lawArea);
//        subject.setSubjectCode(wrapedLawArea);
//        subject.setSubjectHeadingText(wrapedLawArea);
//        descriptiveDetail.setSubject(subject);
//
//
//        List<ONIXMessage.Product.PublishingDetail.PublishingDate> publishingDates = publishingDetail.getPublishingDate();
//        ONIXMessage.Product.PublishingDetail.PublishingDate publishingDate = new ONIXMessage.Product.PublishingDetail.PublishingDate();
//        publishingDate.setPublishingDateRole(wrapToCdata("01"));
//
//        ONIXMessage.Product.PublishingDetail.PublishingDate.Date date = new ONIXMessage.Product.PublishingDetail.PublishingDate.Date();
//        date.setValue(wrapToCdata(metadata.getVerschijningsdatum()));
////        date.setDateformat("00");
//        publishingDate.setDate(date);
//
//        ONIXMessage.Product.CollateralDetail collateralDetail = new ONIXMessage.Product.CollateralDetail();
//        ONIXMessage.Product.CollateralDetail.TextContent textContent = new ONIXMessage.Product.CollateralDetail.TextContent();
//        ONIXMessage.Product.CollateralDetail.TextContent.Text text = new ONIXMessage.Product.CollateralDetail.TextContent.Text();
//        text.setValue(metadata.getSamenvattingAbstract());
////        text.setTextformat("02");
//        textContent.setText(text);
//        textContent.setTextType(wrapToCdata("03"));
//        textContent.setContentAudience("");
//        collateralDetail.setTextContent(textContent);
//        product.setCollateralDetail(collateralDetail);
//
//        ONIXMessage.Product.PublishingDetail.PublishingDate publishingDate1 = new ONIXMessage.Product.PublishingDetail.PublishingDate();
//        publishingDate1.setPublishingDateRole(wrapToCdata("01"));
//
//        ONIXMessage.Product.PublishingDetail.PublishingDate.Date date1 = new ONIXMessage.Product.PublishingDetail.PublishingDate.Date();
//        date1.setValue(wrapToCdata(String.valueOf(metadata.getJaarVanUitgave())));
////        date1.setDateformat("05");
//        publishingDate1.setDate(date1);
//
//        publishingDates.add(publishingDate);
//        publishingDates.add(publishingDate1);
//
//        String productStatus = metadata.getProductstatus();
//        if (productStatus == null){
//            publishingDetail.setPublishingStatus(wrapToCdata("04"));
//        }else {
//            if ( productStatus.equalsIgnoreCase("Final")) {
//                publishingDetail.setPublishingStatus(wrapToCdata("04"));
//            }
//            if ( productStatus.equalsIgnoreCase("Inital")) {
//                publishingDetail.setPublishingStatus(wrapToCdata("01"));
//            }
//            if ( productStatus.equalsIgnoreCase("to be terminated")) {
//                publishingDetail.setPublishingStatus(wrapToCdata("04"));
//            }
//            if ( productStatus.equalsIgnoreCase("terminated")) {
//                publishingDetail.setPublishingStatus(wrapToCdata("01"));
//            }
//        }
//
//        publishingDetail.setCityOfPublication("");
//
//        ONIXMessage.Product.RelatedMaterial relatedMaterial = new ONIXMessage.Product.RelatedMaterial();
//        ONIXMessage.Product.RelatedMaterial.RelatedProduct relatedProduct = new ONIXMessage.Product.RelatedMaterial.RelatedProduct();
//        ONIXMessage.Product.RelatedMaterial.RelatedProduct.ProductIdentifier productIdentifier = new ONIXMessage.Product.RelatedMaterial.RelatedProduct.ProductIdentifier();
//        productIdentifier.setProductIDType(wrapToCdata("15"));
//        productIdentifier.setIDValue(wrapToCdata(String.valueOf(metadata.getISBNFolioBk())));
//        relatedProduct.setProductIdentifier(productIdentifier);
//
//        relatedMaterial.setRelatedProduct(relatedProduct);
//        relatedMaterial.getRelatedProduct().setProductRelationCode(wrapToCdata("13"));
//
//        product.setRelatedMaterial(relatedMaterial);
//
//        ONIXMessage.Product.ProductSupply productSupply = new ONIXMessage.Product.ProductSupply();
//        ONIXMessage.Product.ProductSupply.SupplyDetail supplyDetail = new ONIXMessage.Product.ProductSupply.SupplyDetail();
//
//        ONIXMessage.Product.ProductSupply.SupplyDetail.Supplier supplier = new ONIXMessage.Product.ProductSupply.SupplyDetail.Supplier();
//        supplier.setSupplierRole("");
//        supplier.setSupplierName("");
//
//        supplyDetail.setSupplier(supplier);
//
//        ONIXMessage.Product.ProductSupply.SupplyDetail.Price price = new ONIXMessage.Product.ProductSupply.SupplyDetail.Price();
//        price.setPriceAmount(wrapToCdata(String.valueOf(metadata.getPrijs())));
//        price.setPriceType(wrapToCdata("01"));
//        price.setCurrencyCode(wrapToCdata("EUR"));
//        supplyDetail.setPrice(price);
//
//        supplyDetail.setProductAvailability("");
//
//
//        productSupply.setSupplyDetail(supplyDetail);
//        product.setProductSupply(productSupply);
//
//        titleDetail.setTitleType("");
//
//        Integer druk = metadata.getDruk();
//
//        descriptiveDetail.setEditionNumber(wrapToCdata(String.valueOf(druk)));
//
//        onixMessage.setProduct(product);
//        onixMessage.setHeader(header);
//
//        return onixMessage;
//    }
//
//    private ONIXMessage.Product.DescriptiveDetail.TitleDetail createTitleDetail(ONIXMessage.Product.DescriptiveDetail.TitleDetail.TitleElement titleElement) {
//        ONIXMessage.Product.DescriptiveDetail.TitleDetail titleDetail = new ONIXMessage.Product.DescriptiveDetail.TitleDetail();
//        titleDetail.setTitleElement(titleElement);
//        return titleDetail;
//    }
//
//    private ONIXMessage.Product.DescriptiveDetail.TitleDetail.TitleElement createTitleElement(Metadata metadata) {
//        ONIXMessage.Product.DescriptiveDetail.TitleDetail.TitleElement titleElement = new ONIXMessage.Product.DescriptiveDetail.TitleDetail.TitleElement();
//        titleElement.setTitleText(wrapToCdata(metadata.getTitel()));
//        return titleElement;
//    }
//
//    private ONIXMessage.Product.PublishingDetail.Publisher createPublisher(Metadata metadata) {
//        ONIXMessage.Product.PublishingDetail.Publisher publisher = new ONIXMessage.Product.PublishingDetail.Publisher();
//        publisher.setPublishingRole(wrapToCdata("01"));
//        publisher.setPublisherName(wrapToCdata(metadata.getNaamVanUitgever()));
//        return publisher;
//    }
//
//    private ONIXMessage.Product.DescriptiveDetail createDescriptiveDetail(Metadata metadata) {
//        ONIXMessage.Product.DescriptiveDetail descriptiveDetail = new ONIXMessage.Product.DescriptiveDetail();
//        descriptiveDetail.setEditionNumber(wrapToCdata(String.valueOf(metadata.getTitel())));
//        return descriptiveDetail;
//    }
//
//    private List<Metadata> parseMetadataCSV(MultipartFile metadataCSV) {
//        // parse CSV file to create a list of `Book` objects
//        List<Metadata> metadata = new ArrayList<>();
//        try (Reader reader = new BufferedReader(new InputStreamReader(metadataCSV.getInputStream()))) {
//
//            // create csv bean reader
//            CsvToBean<Metadata> csvToBean = new CsvToBeanBuilder<Metadata>(reader)
//                    .withType(Metadata.class)
//                    .withIgnoreLeadingWhiteSpace(true)
//                    .build();
//            // convert `CsvToBean` object to list of books
//            metadata = csvToBean.parse();
//        } catch (Exception ex) {
//            System.out.println("Error during parse");
//        }
//        return metadata;
//    }
//    private List<Book> parseNewPriceCSV(MultipartFile newPricesCSV) {
//        // parse CSV file to create a list of `Book` objects
//        List<Book> newPrices = new ArrayList<>();
//        try (Reader reader = new BufferedReader(new InputStreamReader(newPricesCSV.getInputStream()))) {
//
//            // create csv bean reader
//            CsvToBean<Book> csvToBean = new CsvToBeanBuilder<Book>(reader)
//                    .withType(Book.class)
//                    .withIgnoreLeadingWhiteSpace(true)
//                    .build();
//            // convert `CsvToBean` object to list of books
//            newPrices = csvToBean.parse();
//        } catch (Exception ex) {
//            System.out.println("Error during parse");
//        }
//        return newPrices;
//    }
//
//    private String wrapToCdata(String value) {
//        if (value == null || value.equals("null") || value.isEmpty()) {
//            return "";
//        }
//        return "<![CDATA[" + value + "]]>";
//    }
//
//}
