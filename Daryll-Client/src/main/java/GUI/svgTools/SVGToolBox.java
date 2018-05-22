/**
 * Module      : PRO
 * File        : .java
 * Date of Creation        : 31.03.2018
 * <p>
 * Description : This file contains the tools to parse, use and transcode
 * svg files in the application Daryll. *
 * <p>
 * Remarks : -
 *
 * @author Früeh Loïc, Gallay Romain, Meyer Yohannn, Muaremi Dejvid, Siu Aurélien, Rashiti Labinot
 * @version 1.0
 */

package GUI.svgTools;

import utils.DisplayConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;



public class SVGToolBox {
    private static final String PATH_ELEMENT_NAME = "path";

    private Document svgDocument;

    /**
     * parse the given file to update color of classrooms
     * @param svg the svg file that needs to be parsed
     */
    public void updateSVG(String svg, String classroomName, String colorValue) {

        InputStream svgInputStream = getClass().getResourceAsStream(svg);

        //System.out.println(svgInputStream.toString());

        List<String> classrooms = new ArrayList<String>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(svgInputStream);
            doc.getDocumentElement().normalize();

            // Get the list of all groups (g balise)
            NodeList groups = doc.getElementsByTagName("g");

            NodeList path = doc.getElementsByTagName("path");

            getClassroomFromSVGNodeList(groups, classroomName, DisplayConstants.COLOR_BEACON + colorValue);
            getClassroomFromSVGNodeList(path, classroomName, DisplayConstants.COLOR_BEACON + colorValue);
            transformTheDom(doc, svgInputStream.getClass().getResource(svg).getPath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param nodeList : list of nodes where to search the classroomName
     * @param classroomName classroom name to apply color
     * @param colorValue  color to apply on the classroom
     */
    public void getClassroomFromSVGNodeList(NodeList nodeList, String classroomName, String colorValue) {
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            org.w3c.dom.Node nNode = nodeList.item(temp);
            if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                Element linkNode = (Element) nNode.getParentNode();
                if (linkNode.getAttribute("id").equals(classroomName)) {
                    System.out.println(eElement.getAttribute("id"));
                    System.out.println(eElement.getAttribute("style"));
                    eElement.setAttribute("style", colorValue);
                    linkNode.setAttribute("style", colorValue);
                    System.out.println(eElement.getAttribute("style"));
                    System.out.println(linkNode.getAttribute("style"));

                    NodeList path = eElement.getElementsByTagName("path");
                    if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                        Element pathElem = (Element) path.item(0);
                        // #C5FFB8
//                        System.out.println(pathElem.getAttribute("style"));
                        //pathElem.setAttribute("style", colorValue);
//                        System.out.println(pathElem.getAttribute("style"));
                    }
                }
            }
        }
    }

    /**
     * @brief This is a utility method that is used to execute code
     * that is the same regardless of the graphic image
     * being produced.  This method transforms the DOM into
     * raw XML code and writes that code into the output.
     * @param document
     * @param filename
     */
    static void transformTheDom(Document document,
                                String filename) {
        try {
            //Get a TransformerFactory object.
            TransformerFactory xformFactory =
                    TransformerFactory.newInstance();

            //Get an XSL Transformer object.
            Transformer transformer =
                    xformFactory.newTransformer();

            //Sets the standalone property in the first line of
            // the output file.
            transformer.setOutputProperty(
                    OutputKeys.STANDALONE, "no");

            //Get a DOMSource object that represents the
            // Document object
            DOMSource source = new DOMSource(document);

            //Get a StreamResult object that points to the
            // screen. Then transform the DOM sending XML to
            // the screen.
            StreamResult scrResult =
                    new StreamResult(new FileOutputStream(filename));
            transformer.transform(source, scrResult);
        }//end try block

        catch (Exception e) {
            e.printStackTrace(System.err);
        }//end catch
    }//end transformTheDom
}