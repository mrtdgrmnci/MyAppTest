package com.mrt.utils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.monte.media.*;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import static org.monte.media.AudioFormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

/**
 * @author Murat.Degirmenci
 */
public class RecordingUtils {

    public static ScreenRecorder screenRecorder;
    static String destination =(System.getProperty("user.dir") +File.separator + "target" + File.separator
                               + "recording");

    public RecordingUtils(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat, Format screenFormat,
           Format mouseFormat, Format audioFormat, File movieFolder, String name) throws IOException,AWTException {}

    public static  void startRecording() {
        if (!ConfigReader.getProperty("browser").toLowerCase().contains("headless")) {
            File file = new File("destination");

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = screenSize.width;
            int height = screenSize.height;

            Rectangle captureSize = new Rectangle(0, 0, width, height);
            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                    .getDefaultConfiguration();

            try {
                screenRecorder = new SpecializedScreenRecorder(gc, captureSize,
                        new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                        new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                                CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
                                Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                        new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey,
                                Rational.valueOf(30)),
                        null, file, "failedTestRecording");
            } catch (IOException | AWTException e) {
                e.printStackTrace();
            }
            try {
                screenRecorder.start();
                System.out.println("<<<<< Screen recording has started >>>>>");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


        public static void stopRecording() {
            try {
                if (!ConfigReader.getProperty("browser").toLowerCase().contains("headless")) {
                    screenRecorder.stop();
                    System.out.println("<<<<< Screen recording has stopped >>>>>");
                }

            } catch (IOException e) {
                e.printStackTrace();

            }
        }

    public static void stopRecordingAndDelete() {
        try {
            if (!ConfigReader.getProperty("browser").toLowerCase().contains("headless")) {
                screenRecorder.stop();
                List<File> allRecordings = screenRecorder.getCreatedMovieFiles();
                for (int i = 0; i <allRecordings.size() ; i++) {
                    if (allRecordings.get(i).toString().contains(SpecializedScreenRecorder.getDate()))
                        allRecordings.get(i).delete();
                    System.out.println("<<<<< Screen recording has deleted >>>");

                }
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
    }


}
