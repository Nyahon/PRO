package Daryll.Plan;

import Daryll.SVGTools.BufferedImageTranscoder;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlanLoader {
    // Transcoder used to convert the svg to an java FX image
    private BufferedImageTranscoder trans = new BufferedImageTranscoder();
    private TranscoderInput transcoderInput = null;

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
    public Image getTranscodedImage(double width, double height){
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

}
