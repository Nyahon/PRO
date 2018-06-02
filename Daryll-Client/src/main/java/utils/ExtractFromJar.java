package utils;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExtractFromJar {

    /**
     * Method to extract internal file contained in extern of the jar
     *
     * Required for files that needs to be modified dynamically when the application is running in a jar archive
     *
     * @param internStream
     * @param externPath relative path from jar application
     */
    public static void createExternalFile(InputStream internStream, String externPath){
        try{
            FileOutputStream svgDestFile = new FileOutputStream(new File(externPath));

            IOUtils.copy(internStream, svgDestFile);
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
