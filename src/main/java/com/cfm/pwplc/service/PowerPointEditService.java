package com.cfm.pwplc.service;

import com.cfm.pwplc.utils.Utils;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
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

    private final String DATE_FORMAT = "EEE, yyyy-MM-dd HH-mm a";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);


    public void createPowerPointFile(List<String> songsList) throws IOException {

        if (properties.isEmpty()) {
            loadAppProperties();
        }

        String timeStamp = LocalDateTime.now().format(formatter);
        final String SONG_ROOT_PATH = properties.getProperty("SONG_ROOT_PATH");

        File originalFile = new File(SONG_ROOT_PATH + properties.getProperty("INIT_SONGS_LIST"));

        String fileName = timeStamp + properties.getProperty("PPTX");
        File file = new File(SONG_ROOT_PATH + fileName);

        copyFileUsingApacheCommonsIO(originalFile, file);

        //opening an existing slide show
        FileInputStream originalContent = new FileInputStream(file);
        XMLSlideShow slideShow = new XMLSlideShow(originalContent);

        List<String> inputs = getSongsPathsList(songsList, properties.getProperty("SONG_ROOT_PATH"), false);

        addSlideShowsToFinalPresentation(slideShow, inputs);

        //creating the file object
        FileOutputStream out = new FileOutputStream(file);

        // saving the changes to a file
        slideShow.write(out);
        out.close();
        System.out.println("Merging done successfully");

        showFileWasCreatedMessage(fileName);

    }

    private void loadAppProperties() throws IOException {
        URL appResources = Utils.getResource(APP_PROPERTIES);

        try (InputStream inputStream = appResources.openStream()) {
            properties.load(inputStream);
        }
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
    private List<String> getSongsPathsList(List<String> songsList, String SONG_ROOT_PATH, Boolean createFinalSongOnly) {

        final String BLANK_PAGE = properties.getProperty("BLANK_PAGE");
        if (!createFinalSongOnly) {
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
        } else {
            List<String> inputs = new ArrayList<>();
            inputs.add(SONG_ROOT_PATH + properties.getProperty("WORSHIP") + songsList.get(0));
            inputs.add(SONG_ROOT_PATH + BLANK_PAGE);
            return inputs;
        }
    }

    private void showFileWasCreatedMessage(String fileName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Done!");
        alert.setContentText("'" + fileName + "' was successfully created");

//        alert.setGraphic(new ImageView(this.getClass().getResource("img/help.png").toString()));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/icons/logo.png").toString()));

        alert.showAndWait();
    }

    public void createFinalSongPresentation(List<String> songsList) throws IOException {
        if (properties.isEmpty()) {
            loadAppProperties();
        }

        String timeStamp = LocalDateTime.now().format(formatter);
        final String SONG_ROOT_PATH = properties.getProperty("SONG_ROOT_PATH");

        File originalFile = new File(SONG_ROOT_PATH + properties.getProperty("BLANK_PAGE"));

        String finalSongFileName = "AltarCall " + timeStamp + properties.getProperty("PPTX");
        File file = new File(SONG_ROOT_PATH + finalSongFileName);

        copyFileUsingApacheCommonsIO(originalFile, file);

        //opening an existing slide show
        FileInputStream originalContent = new FileInputStream(file);
        XMLSlideShow finalSongSlideShow = new XMLSlideShow(originalContent);

        List<String> inputs = getSongsPathsList(songsList, properties.getProperty("SONG_ROOT_PATH"), true);

        addSlideShowsToFinalPresentation(finalSongSlideShow, inputs);
        //creating the file object
        FileOutputStream out = new FileOutputStream(file);

        // saving the changes to a file
        finalSongSlideShow.write(out);
        out.close();
        System.out.println("Merging done successfully");

        showFileWasCreatedMessage(finalSongFileName);

    }

    private void addSlideShowsToFinalPresentation(XMLSlideShow SlideShow, List<String> inputs) throws IOException {
        for (String input : inputs) {
            //create inputStream of slide show file
            FileInputStream inputstream = new FileInputStream(input);
            //create SlideShow from input stream
            XMLSlideShow src = new XMLSlideShow(inputstream);
            for (XSLFSlide srcSlide : src.getSlides()) {
                //merging the contents creating slides using the source slides layout
                SlideShow.createSlide(srcSlide.getSlideLayout()).importContent(srcSlide);
            }
        }
    }
}
