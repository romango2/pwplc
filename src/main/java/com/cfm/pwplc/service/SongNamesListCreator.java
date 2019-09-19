package com.cfm.pwplc.service;

import com.cfm.pwplc.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by roman-ionut.goga on 8/31/2018.
 */
public class SongNamesListCreator {

    private static final String PROPERTIES_ROOT_PATH = "src/main/resources/";
    private static final String PRAISE_SONGS = "praise-songs.properties";
    private static final String WORSHIP_SONGS = "worship-songs.properties";


    public List<String> getSongNamesFromFile(List<String> songNumbers) {

        List<String> songNames = new ArrayList<>();

        Properties praiseSongs = new Properties();
        Properties worshipSongs = new Properties();
        InputStream input = null;

        URL resource = Utils.getResource(PRAISE_SONGS);
        URL resource2 = Utils.getResource(WORSHIP_SONGS);

        try (InputStream praiseInputStream = resource.openStream();
             InputStream worshipInputStream = resource2.openStream()) {

            // load a properties file
            praiseSongs.load(praiseInputStream);
            worshipSongs.load(worshipInputStream);

            int index = 1;
            for (String songNumber : songNumbers) {

                if ((index == 4) || (index == 5) || (index == 8)) {
                    // get the property value and print it out
                    if (worshipSongs.getProperty(songNumber) != null) {
                        songNames.add(worshipSongs.getProperty(songNumber));
                    } else {
                        songNames.add(worshipSongs.getProperty("0"));
                    }
                } else {
                    // get the property value and print it out
                    if (praiseSongs.getProperty(songNumber) != null) {
                        songNames.add(praiseSongs.getProperty(songNumber));
                    } else {
                        songNames.add(praiseSongs.getProperty("0"));
                    }
                }
                index++;
            }


        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return songNames;
    }

}

