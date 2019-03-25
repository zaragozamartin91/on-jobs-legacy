package com.on.jobs.publica.misc;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;

public class ParseXmlExample {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        String path = "/hola/como/estas";
        System.out.println( path.substring(0,path.lastIndexOf('/')) );

        System.out.println( path.substring(path.lastIndexOf('/')) );

        File xmlFile = new File("test_files", "ejemplo_aviso.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);

        Element rootElement = doc.getDocumentElement();
        rootElement.normalize();
        System.out.println("Root element :" + rootElement.getNodeName());

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        XPathExpression expr = xpath.compile("/Avisos/aviso");
        Element node = (Element) expr.evaluate(rootElement, XPathConstants.NODE);

        System.out.println(node.getTagName());
    }
}
