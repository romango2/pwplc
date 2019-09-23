package com.cfm.pwplc.service;

import com.cfm.pwplc.utils.Utils;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PowerPointEditService {

    private static final String APP_PROPERTIES = "application.properties";
    private final Properties properties = new Properties();

    private final String DATE_FORMAT = "EEE, yyyy-MM-dd kk-mm";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);


    public void createPowerPointFile(List<String> songsList) throws IOException {

        URL appResources = Utils.getResource(APP_PROPERTIES);

        try (InputStream inputStream = appResources.openStream()) {
            properties.load(inputStream);
        }

        String timeStamp = LocalDateTime.now().format(formatter);
        final String SONG_ROOT_PATH = properties.getProperty("SONG_ROOT_PATH");

        File originalFile = new File(SONG_ROOT_PATH + properties.getProperty("INIT_SONGS_LIST"));
        File file = new File(SONG_ROOT_PATH + timeStamp + properties.getProperty("PPTX"));

        copyFileUsingApacheCommonsIO(originalFile, file);

        //opening an existing slide show
        FileInputStream originalContent = new FileInputStream(file);
        XMLSlideShow ppt = new XMLSlideShow(originalContent);

        List<String> inputs = getSongsPathsList(songsList, SONG_ROOT_PATH);

        for (String input : inputs) {
            //create inputStream of slide show file
            FileInputStream inputstream = new FileInputStream(input);
            //create SlideShow from input stream
            XMLSlideShow src = new XMLSlideShow(inputstream);
            for (XSLFSlide srcSlide : src.getSlides()) {
                //merging the contents creating slides using the source slides layout
                ppt.createSlide(srcSlide.getSlideLayout()).importContent(srcSlide);
            }
        }
        //creating the file object
        FileOutputStream out = new FileOutputStream(file);

        // saving the changes to a file
        ppt.write(out);
        out.close();
        System.out.println("Merging done successfully");
    }

    private static void copyFileUsingApacheCommonsIO(File source, File dest) throws IOException {
        FileUtils.copyFile(source, dest);
    }

    /**
     * creating list of presentations that are to be merged
     *
     * @param songsList
     * @param SONG_ROOT_PATH
     * @return songs paths list
     */
    private List<String> getSongsPathsList(List<String> songsList, String SONG_ROOT_PATH) {

        final String BLANK_PAGE = properties.getProperty("BLANK_PAGE");
        List<String> inputs = new ArrayList<>();
        for (int i = 0; i <= songsList.size() - 1; i++) {
            if ((i != 3) && (i != 4) && (i != 7)) {
                inputs.add(SONG_ROOT_PATH + properties.getProperty("PRAISE") + songsList.get(i));
                inputs.add(SONG_ROOT_PATH + BLANK_PAGE);
            } else {
                inputs.add(SONG_ROOT_PATH + properties.getProperty("WORSHIP") + songsList.get(i));
                inputs.add(SONG_ROOT_PATH + BLANK_PAGE);
            }
        }
        return inputs;
    }

}
