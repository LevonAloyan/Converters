package com.example.csvupdater.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class FileDownloadController {

    @GetMapping("/xml/download")
    public ResponseEntity<String> downloadFiles()  {

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader("./filehashes/fileHashes.txt"));

        String line = "";
        while ((line = bufferedReader.readLine() )!= null){
            Runtime rt = Runtime.getRuntime();
            rt.exec("curl https://contentmanager2.prd.legalintelligence.com/content/viewer/raw/" + line + " --output " +"./results/"+ line + ".xml");
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
