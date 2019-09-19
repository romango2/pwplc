package com.cfm.pwplc.service;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PowerPointEditService {

    private static final String SONG_ROOT_PATH = "C://pwplc//songs/";
    private static final String PRAISE = "praise/";
    private static final String WORSHIP = "worship/";
    private static final String BLANK_PAGE = "C://pwplc//songs//init/Blank.pptx";


    private final String DATE_FORMAT = "yyyy-MM-dd kk mm";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);


    public void createPowerPointFile( List<String> songsList) throws IOException {

        File originalFile = new File("C://pwplc//songs//init/List.pptx");

        String timeStamp = LocalDateTime.now().format(formatter);

        File file = new File("C://pwplc//songs/List " + timeStamp + ".pptx");

        copyFileUsingApacheCommonsIO(originalFile, file);

        //opening an existing slide show
        FileInputStream originalContent = new FileInputStream(file);
        XMLSlideShow ppt = new XMLSlideShow(originalContent);

        //taking the presentations that are to be merged
        List<String> inputs = new ArrayList<>();

        for (int i=0; i <= songsList.size()-1; i++){

            if ((i!= 3)&&(i!= 4)&&(i!= 7)) {
                inputs.add(SONG_ROOT_PATH + PRAISE + songsList.get(i));
                inputs.add(BLANK_PAGE);
            } else {
                inputs.add(SONG_ROOT_PATH + WORSHIP + songsList.get(i));
                inputs.add(BLANK_PAGE);
            }

        }

        for (String input : inputs) {

            FileInputStream inputstream = new FileInputStream(input);
            XMLSlideShow src = new XMLSlideShow(inputstream);

            for (XSLFSlide srcSlide : src.getSlides()) {

                //merging the contents
                ppt.createSlide(srcSlide.getSlideLayout()).importContent(srcSlide);
            }
        }

        //creating the file object
        FileOutputStream out = new FileOutputStream(file);

        // saving the changes to a file
        ppt.write(out);
        System.out.println("Merging done successfully");
        out.close();


    }

    private static void copyFileUsingApacheCommonsIO(File source, File dest) throws IOException {
        FileUtils.copyFile(source, dest);
    }

}
