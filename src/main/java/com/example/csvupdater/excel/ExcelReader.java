package com.example.csvupdater.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class ExcelReader {

    public static void main(String[] args) throws IOException {

//        try( FileOutputStream fos = new FileOutputStream("./titles.txt");
//             FileOutputStream fileOutputStream = new FileOutputStream("./originalTitles.txt")
//        ) {
//            File file = new File("/Users/Levon_Aloyan/IdeaProjects/csv-updater/src/main/java/com/example/csvupdater/excel/AKD-Import-All.XLSX");   //creating a new file instance
//            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file
////creating Workbook instance that refers to .xlsx file
//            XSSFWorkbook wb = new XSSFWorkbook(fis);
//            XSSFSheet sheet = wb.getSheetAt(1);     //creating a Sheet object to retrieve object
//            XSSFSheet sheet0 = wb.getSheetAt(0);     //creating a Sheet object to retrieve object
//
//            List<String> titles = new ArrayList<>();
//            List<String> originalTitles = new ArrayList<>();
//
//            //iterating over excel file
//            for (Row row : sheet) {
//                if (!(row.getCell(1).toString().equals(""))) {
//                    titles.add(row.getCell(1).toString());
//                }
//            }
//
//            for (Row row : sheet0) {
//                if (!(row.getCell(3)).toString().equals("")){
//                    originalTitles.add((row.getCell(3)).toString());
//                }
//            }
//
//            System.out.println(originalTitles.size());
//            System.out.println(titles.size());
//
//            System.out.println(originalTitles.size() -titles.size());
//
//            originalTitles.removeAll(titles);
//
//
//            System.out.println(originalTitles.size());
//
//            Set<String> sort = new TreeSet<>(originalTitles);
//            Set<String> titleSet = new TreeSet<>(titles);
//
//            for (String s : titleSet) {
//                fos.write((s+"\n").getBytes());
//            }
//
//            for (String s : sort) {
//                fileOutputStream.write((s+"\n").getBytes());
//            }


        try (BufferedReader fileInputStream = new BufferedReader(new FileReader("./originalTitles.txt"));
             BufferedReader fileInputStream1 = new BufferedReader(new FileReader("./titles.txt"));
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("./final.txt")))
        {

            int b;

            Set<String> origSet = new TreeSet<>();

            String line = fileInputStream.readLine();
            while (line != null) {
                origSet.add(line);
                line = fileInputStream.readLine();
            }


            Set<String> titSet = new TreeSet<>();
            String line1 = fileInputStream1.readLine();
            while (line1 != null) {
                titSet.add(line1);
                line1 = fileInputStream1.readLine();
            }

            origSet.removeAll(titSet);
            System.out.println("Title " + titSet.size());
            System.out.println("Origin " + origSet.size());

            for (String s : origSet) {
                bufferedWriter.write(s+"\n");
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

