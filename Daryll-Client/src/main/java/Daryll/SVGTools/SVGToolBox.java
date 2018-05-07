/**
 * Module      : PRO
 * File        : .java
 * Date of Creation        : 31.03.2018
 *
 * Description : This file contains the tools to parse, use and transcode
 *               svg files in the application Daryll. *
 *
 * Remarks : -
 *
 * @author Früeh Loïc, Gallay Romain, Meyer Yohannn, Muaremi Dejvid, Siu Aurélien, Rashiti Labinot
 * @version 1.0
 *
 */

package Daryll.SVGTools;

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

import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.PrintWriter;


public class SVGToolBox {
    private static final String PATH_ELEMENT_NAME = "path";

    private Document svgDocument;

    /**
     * parse the given file to update color of classrooms
     * @param file the svg file that needs to be parsed
     */
    public static void updateSVG(File file) {
        List<String> classrooms = new ArrayList<String>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            // Get the list of all groups (g balise)
            NodeList groups = doc.getElementsByTagName("g");

            // NodeList path = doc.getElementsByTagName("path");

            getClassroomFromSVGNodeList(groups, "A09", "fill:#0000ff"); // blue color

            transformTheDom(doc, file.getAbsolutePath());

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
    public static void getClassroomFromSVGNodeList(NodeList nodeList, String classroomName, String colorValue){
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            org.w3c.dom.Node nNode = nodeList.item(temp);
            if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                Element linkNode = (Element) nNode.getParentNode();
                if(linkNode.getAttribute("id").equals(classroomName)) {
                    System.out.println(eElement.getAttribute("id"));
                    System.out.println(eElement.getAttribute("style"));
                    eElement.setAttribute("style", "fill:#0000ff");
                    linkNode.setAttribute("style", "fill:#0000ff");
                    System.out.println(eElement.getAttribute("style"));
                    System.out.println(linkNode.getAttribute("style"));

                    NodeList path = eElement.getElementsByTagName("path");
                    if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                        Element pathElem = (Element) path.item(0);
                        // #C5FFB8
                        System.out.println(pathElem.getAttribute("style"));
                        pathElem.setAttribute("style", colorValue);
                        System.out.println(pathElem.getAttribute("style"));
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
                                String filename){
        try{
            //Get a TransformerFactory object.
            TransformerFactory xformFactory =
                    TransformerFactory.newInstance();

            //Get an XSL Transformer object.
            Transformer transformer =
                    xformFactory.newTransformer();

            //Sets the standalone property in the first line of
            // the output file.
            transformer.setOutputProperty(
                    OutputKeys.STANDALONE,"no");

            //Get a DOMSource object that represents the
            // Document object
            DOMSource source = new DOMSource(document);

            //Get a StreamResult object that points to the
            // screen. Then transform the DOM sending XML to
            // the screen.
            StreamResult scrResult =
                    new StreamResult(System.out);
            transformer.transform(source, scrResult);

            //Get an output stream for the output file.
            PrintWriter outStream = new PrintWriter(filename);

            //Get a StreamResult object that points to the
            // output file.  Then transform the DOM sending XML
            // code to the file
            StreamResult fileResult =
                    new StreamResult(outStream);
            transformer.transform(source,fileResult);
        }//end try block

        catch(Exception e){
            e.printStackTrace(System.err);
        }//end catch
    }//end transformTheDom

    public static void main(String args[]){
        // Test with Floor A1 (first part of A floor)
        File f = new File("src/main/resources/Daryll/plans/Cheseaux/floor-A1.svg");
        updateSVG(f);
    }

}