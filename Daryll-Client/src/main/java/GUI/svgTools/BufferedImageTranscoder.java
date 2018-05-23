package GUI.svgTools; /**
 * This is a compilation of code snippets required to render SVG files in JavaFX using Batik.
 * See my full post on StackOverflow: http://stackoverflow.com/a/23894292/603003
 */

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import java.awt.image.BufferedImage;

/**
 * Many thanks to bb-generation for sharing this code!
 * @author bb-generation
 * @link https://web.archive.org/web/20131215231214/http://bbgen.net/blog/2011/06/java-svg-to-bufferedimage/
 * modified by: Aurélien Siu
 */
public class BufferedImageTranscoder extends ImageTranscoder {

    private BufferedImage img = null;

    /**
     *
     * @param width
     * @param height
     * @return bufferedImage containing the new transcoded image
     */
    @Override
    public BufferedImage createImage(int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        return bi;
    }

    /**
     *
     * @param img
     * @param to
     * @throws TranscoderException
     */
    @Override
    public void writeImage(BufferedImage img, TranscoderOutput to) throws TranscoderException {
        this.img = img;
    }

    public BufferedImage getBufferedImage() {
        return img;
    }
}
