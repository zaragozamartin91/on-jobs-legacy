package com.on.jobs.publica;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.regex.Pattern;

@SpringBootTest(classes = OnJobsLegacyApplication.class)
@ContextConfiguration
@ActiveProfiles(value = "test")
public class SpringIntegrationTest {
    protected static final XPath XPATH = XPathFactory.newInstance().newXPath();

    protected Document document;

    protected String docToString(Document document) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));

            return writer.getBuffer().toString();
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    protected void newDocument() { this.document = buildDocument(); }

    protected Document buildDocument() {return buildDocument("ejemplo_aviso.xml");}

    protected Document buildDocument(String fileName) {
        File xmlFile = new File("test_files", fileName);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            return dBuilder.parse(xmlFile);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new IllegalStateException("Error al parsear documento " + xmlFile.getAbsolutePath(), e);
        }
    }

    protected Document parseDocument(String xmlString) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al parsear documento " + xmlString, e);
        }
    }

    protected Element getDocumentElement(Document doc) {
        /* TODO : analizar si es necesario normalizar el documento antes de enviarlo */
        //        documentElement.normalize();
        return doc.getDocumentElement();
    }

    protected Element getElement(String path) { return getElement(path, document); }

    protected Element getElement(String path, Document doc) {
        try {
            XPathExpression expression = XPATH.compile(path);
            return (Element) expression.evaluate(getDocumentElement(doc), XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException("Expresion " + path + " invalida", e);
        }
    }

    protected Element createElement(String path) { return createElement(path, document); }

    protected Element createElement(String path, Document doc) {
        try {
            int lastIndexOfSlash = path.lastIndexOf('/');
            String parentPath = path.substring(0, lastIndexOfSlash);
            Element parentElement = (Element) XPATH.compile(parentPath).evaluate(getDocumentElement(doc), XPathConstants.NODE);

            Element element = doc.createElement(path.substring(lastIndexOfSlash + 1));
            parentElement.appendChild(element);

            return element;
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException("Expresion " + path + " invalida", e);
        }
    }
}