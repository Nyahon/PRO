/**
 * File                 : SVGToolBox.java
 * Date of Creation     : 16.04.2018
 * <p>
 * Description : This file contains the tools to parse, use and transcode
 * svg files in the application Daryll.
 * <p>
 * Remarks : -
 *
 * @author Aurélien Siu
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

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static utils.DisplayConstants.COLOR_VALUES;
import static utils.DisplayConstants.getColorIdFromFreePeriods;


public class SVGToolBox {

    /**
     * parse the given file to update color of classrooms (groups of path elements)
     * @param svgPath the svg file that needs to be parsed
     * @param classroomsToupdate contains the names of the classroom to update with
     * //@param colorValue color in web hexa value (example #ffffff)
     */
    public void updateSVG(String svgPath, Map<String, Integer> classroomsToupdate) {

        FileInputStream svgInputFile = null;
        try {
            svgInputFile = new FileInputStream(svgPath);
        } catch (Exception e){
            e.printStackTrace();
        }


        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(svgInputFile);
            doc.getDocumentElement().normalize();

            for (HashMap.Entry<String, Integer> classroom : classroomsToupdate.entrySet()) {

                String classroomName = classroom.getKey();
                int colorId = getColorIdFromFreePeriods(classroom.getValue());
                DisplayConstants.COLORS_ROOMS colors_rooms = DisplayConstants.COLORS_ROOMS.values()[colorId];

                // Get the list of all groups (g balise)
                NodeList groups = doc.getElementsByTagName("g");

                NodeList path = doc.getElementsByTagName("path");

                getClassroomFromSVGNodeList(groups, classroomName, DisplayConstants.COLOR_BEACON + COLOR_VALUES[colors_rooms.ordinal()]);
                getClassroomFromSVGNodeList(path, classroomName, DisplayConstants.COLOR_BEACON + COLOR_VALUES[colors_rooms.ordinal()]);
            }

            transformTheDom(doc, svgPath);

            svgInputFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param nodeList list of nodes where to search the classroomName
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
                    eElement.setAttribute("style", colorValue);
                    linkNode.setAttribute("style", colorValue);

                    NodeList path = eElement.getElementsByTagName("path");
                    if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                        Element pathElem = (Element) path.item(0);
                        if(pathElem != null) {
                            pathElem.setAttribute("style", colorValue);
                        }
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
     * @param file
     */
    static void transformTheDom(Document document,
                                String file) {
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
                    new StreamResult(new FileOutputStream(file));
            transformer.transform(source, scrResult);
        }//end try block

        catch (Exception e) {
            e.printStackTrace(System.err);
        }//end catch
    }//end transformTheDom
}