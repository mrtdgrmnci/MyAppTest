package com.mrt.utils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.monte.media.*;
import org.monte.screenrecorder.ScreenRecorder;

public class SpecializedScreenRecorder extends ScreenRecorder {

    private String name;
    public static String dateName;

    public SpecializedScreenRecorder
            (GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat,
             Format screenFormat, Format mouseFormat, Format audioFormat, File movieFolder,String name)
        throws IOException, AWTException{
        super(cfg,captureArea,fileFormat,screenFormat,mouseFormat,audioFormat,movieFolder);
        this.name =name;
    }

    @Override
    protected File createMovieFile(Format fileFormat) throws IOException{
        if (!movieFolder.exists()){
            movieFolder.mkdir();
        }else if (!movieFolder.isDirectory()){
            throw new IOException("\"" + movieFolder + "\" is not a directory. ");
        }
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
        Date date =new Date();
        dateName = dateFormat.format(date);

        return new File(movieFolder, name + "-" + dateName + "." + Registry.getInstance().getExtension(fileFormat));
    }

    public static String getDate(){

        return dateName;
    }
}
