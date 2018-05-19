package GUI.plan;

import GUI.svgTools.BufferedImageTranscoder;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlanLoader implements Runnable{
    // Transcoder used to convert the svg to an java FX image
    private static final BufferedImageTranscoder trans = new BufferedImageTranscoder();
    private static TranscoderInput transcoderInput = null;

    private String file; // file to transcode
    private int width;
    private int height;
    private ImageView imgView;
    private Image img;


    public PlanLoader(String file, ImageView imgView, int width, int height){
        this.file = file;
        this.width = width;
        this.height = height;
        this.imgView = imgView;
    }


    public void openSVGFile(String file){
        // Get InputStream from an SVG file
        InputStream svgInputStream = getClass().getResourceAsStream(file);
        transcoderInput = new TranscoderInput(svgInputStream);
    }

    /**
     *
     * @param width the width of the image in output
     * @param height the height of the image in output
     * @return transcoded image in JavaFX format
     */
    public Image getTranscodedImage(int width, int height){
        // Set format of the transcoded image
        trans.addTranscodingHint(PNGTranscoder.KEY_WIDTH, new Float((int)width));
        trans.addTranscodingHint(PNGTranscoder.KEY_HEIGHT,new Float((int)height));

        try {
            // Transcoding operation (it will produce a bufferedImage, stored in the BufferedImageTranscoder
            trans.transcode(transcoderInput, null);
        } catch (TranscoderException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }

        // Allow to retrieve a Java FX image from the bufferedImage)
        return SwingFXUtils.toFXImage(trans.getBufferedImage(), null);
    }

    @Override
    public void run() {

        openSVGFile(file);
        img = getTranscodedImage(width, height);

        imgView.setImage(img);
    }
}
