package com.on.jobs.publica.sbe;

import com.mz.client.http.SimpleHttpClient;
import com.on.jobs.publica.SpringIntegrationTest;
import cucumber.api.PendingException;
import cucumber.api.java.es.Dado;
import cucumber.api.java.es.Entonces;
import cucumber.deps.com.thoughtworks.xstream.InitializationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class CrearYPublicarAvisoTest extends SpringIntegrationTest {
    private static final String URL = "http://www.bumeran.com.mx/api/publicador/index.bum";

    private static final XPath xpath = XPathFactory.newInstance().newXPath();

    // TODO : insertar id y token de empresa existente (SoftwareGZ)
    private static final String OK_COMPANY_ID = "000001"; // id de empresa existente
    private static final String OK_COMPANY_TOKEN = "h5yfg875djgyu78"; // token valido de empresa existente

    // NOTA: LOS CATALOGOS SON ARCHIVOS!!!
    // TODO : insertar id de producto valido
    /* Este codigo se debe tomar del catalogo "IDPLANPUBLICACION" de acuerdo al tipo de membresia y pais contratado. */
    private static final String OK_PRODUCT_ID = "60";


    private Document buildDocument() {
        File xmlFile = new File("test_files", "ejemplo_aviso.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            return dBuilder.parse(xmlFile);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new IllegalStateException("Error al parsear documento " + xmlFile.getAbsolutePath(), e);
        }
    }


    private Document document;


    private Element getDocumentElement() {
        /* TODO : analizar si es necesario normalizar el documento antes de enviarlo */
        //        documentElement.normalize();
        return document.getDocumentElement();
    }

    private Element getElement(String path) {
        try {
            XPathExpression expression = xpath.compile(path);
            return (Element) expression.evaluate(getDocumentElement(), XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException("Expresion " + path + " invalida", e);
        }
    }

    /**
     * Una empresa habilitada para integrar es aquella que tiene un idempresa y un token necesarios para la creacion y publicacion de avisos.
     * De la documentacion del api legacy para JOBS:
     * <ul>
     * <li><i>emp_idempresa: Es el número que identifica a la empresa en nuestro sistema, este número es proveído por Bumeran y nunca cambia.</i></li>
     * <li><i>emp_token: Es un password que debe ser incluido en cada una de las publicaciones que se envíen como medida de seguridad. Es generado por bumeran y nunca cambia.
     * La generación de este token se solicita inmediatamente después de cerrado el contrato.</i></li>
     * </ul>
     */
    @Dado("una empresa habilitada para integrar")
    public void una_empresa_habilitada_para_integrar() {
        document = buildDocument();

        Element dataElement = getElement("/Avisos/aviso/DatosAdicionales");
        dataElement.setAttribute("emp_idempresa", OK_COMPANY_ID);
        dataElement.setAttribute("emp_token", OK_COMPANY_TOKEN);
    }


    @Dado("que tiene creditos suficientes para publicar avisos")
    public void que_tiene_creditos_suficientes_para_publicar_avisos() {
        Element productElement = getElement("/Avisos/aviso/idPlanPublicacion");
        productElement.setTextContent(OK_PRODUCT_ID);
    }


    @Dado("un formulario de aviso con sus campos requeridos completados correctamente")
    public void un_formulario_de_aviso_con_sus_campos_requeridos_completados_correctamente() throws IOException, URISyntaxException {
        // TODO : EDITAR CAMPOS DEL XML QUE DEBAN SER MODIFICADOS
    }

    @Dado("un codigoAviso unico para dicho integrador")
    public void un_codigoAviso_unico_para_dicho_integrador() {
        Element element = getElement("txCodigoReferencia");
        // TODO : revisar si txCodigoReferencia es el codigo-aviso
        element.setTextContent("63797");
    }


    @Entonces("se crea y se publica el aviso")
    public void se_crea_y_se_publica_el_aviso() throws Exception {
        //   POST   http://www.bumeran.com.mx/api/publicador/index.bum
        SimpleHttpClient.newPost(URL).withContentType()
        throw new PendingException();
    }

    /* Fin de Escenario: crear y publicar un aviso correctamente ------------------------------- */

    /* Escenario: crear y publicar un aviso falla por duplicacion de codigoAviso --------------- */

    @Dado("un codigoAviso duplicado para dicho integrador")
    public void un_codigoAviso_duplicado_para_dicho_integrador() {
        throw new PendingException();
    }

    @Entonces("la creacion del aviso falla por codigoAviso duplicado")
    public void la_creacion_del_aviso_falla_por_codigoAviso_duplicado() throws Exception {
        throw new PendingException();
    }

    /* Fin de crear y publicar un aviso falla por duplicacion de codigoAviso ------------------ */

    @Dado("un formulario de aviso con sus campos requeridos completados erroneamente")
    public void un_formulario_de_aviso_con_sus_campos_requeridos_completados_erroneamente() throws IOException {
        throw new PendingException();
    }

    @Entonces("la creacion del aviso falla por errores presentes en el formulario de aviso")
    public void la_creacion_del_aviso_falla_por_errores_presentes_en_el_formulario_de_aviso() throws Exception {
        throw new PendingException();
    }

    /* Escenario: crear y publicar un aviso falla porque la empresa no tiene creditos suficientes para publicarlo */

    @Dado("que tiene creditos insuficientes para publicar avisos")
    public void que_tiene_creditos_insuficientes_para_publicar_avisos() {
        throw new PendingException();
    }

    @Entonces("la publicacion del aviso falla por falta de creditos de la empresa")
    public void la_publicacion_del_aviso_falla_por_falta_de_creditos_de_la_empresa() throws Exception {
        throw new PendingException();
    }

    /* fin de crear y publicar un aviso falla porque la empresa no tiene creditos suficientes para publicarlo */

    /* Escenario: crear y publicar un aviso falla porque la empresa no esta habilitada para integrar */

    @Dado("una empresa no habilitada para integrar")
    public void una_empresa_no_habilitada_para_integrar() {
        throw new PendingException();
    }

    @Entonces("la creacion del aviso falla porque la empresa no esta habilitada para integrar")
    public void la_creacion_del_aviso_falla_porque_la_empresa_no_esta_habilitada_para_integrar() throws Exception {
        throw new PendingException();
    }

    /* fin de crear y publicar un aviso falla porque la empresa no esta habilitada para integrar */
}
