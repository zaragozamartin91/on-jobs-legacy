package com.on.jobs.publica.sbe;

import com.mz.client.http.SimpleHttpClient;
import com.mz.client.http.SimpleHttpResponse;
import com.on.jobs.publica.SpringIntegrationTest;
import cucumber.api.java.es.Dado;
import cucumber.api.java.es.Entonces;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CrearYPublicarAvisoTest extends SpringIntegrationTest {
    //    private static final String URL = "http://www.bumeran.com.ar/api/publicador/index.bum";
    private static final String URL = "https://www.bumeran.com.ar/api/publicador/index.bum";

    // TODO : insertar id y token de empresa existente (SoftwareGZ)
    private static final String OK_COMPANY_ID = "13130935"; // id de empresa existente
    private static final String OK_COMPANY_TOKEN = "fe545e2f0b3439e"; // token valido de empresa existente


    /**
     * txPuesto
     */
//    private static final String TXP_TEXT = "Cajero /a Unico /a Eventual";
    private static final String TXP_TEXT = "Super cajero capacitado";
    /**
     * txDescripcion
     */
    private static final String TXD_TEXT = "<![CDATA[<p>Si te interesa formar parte de un equipo de trabajo agradable, te invitamos a integrarte a Empresa S.A.</p>]]>";
    /**
     * numCantidadVacantes
     */
    private static final String NCV_TEXT = "2";


    /* Este codigo se debe tomar del catalogo "IDPLANPUBLICACION" de acuerdo al tipo de membresia y pais contratado. */
//    private static final String OK_PRODUCT_ID = "60";
    private static final String OK_PRODUCT_ID = "70";
    private static final String CHARSET = "UTF-8";
    private static final String INVALID_COUNTRY_ID = "999";
    private static final String AD_ALIAS = "45678";
    private static final String RESPONSE_OK_STATUS = "1";


    private SimpleHttpResponse createAd() throws UnsupportedEncodingException {
        getElement("/Avisos/aviso/txAccion").setTextContent("CREAR");
        return execute();
    }

    private SimpleHttpResponse execute() throws UnsupportedEncodingException {
        String body = "XML=" + URLEncoder.encode(docToString(document), CHARSET);

        return SimpleHttpClient.newPost(URL)
                .withHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .withHeader("Accept-Language", "en-US,en;q=0.5")
                .withHeader("Accept-Encoding", "gzip, deflate, br")
                .withContentType("application/x-www-form-urlencoded")
                .withBody(body)
                .execute();
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
        newDocument();

        Element dataElement = getElement("/Avisos/aviso/DatosAdicionales");
        dataElement.setAttribute("emp_idempresa", OK_COMPANY_ID);
        dataElement.setAttribute("emp_token", OK_COMPANY_TOKEN);
    }


    @Dado("que tiene creditos suficientes para publicar avisos")
    public void que_tiene_creditos_suficientes_para_publicar_avisos() {
        // TODO : POR AHORA NO SE PUBLICARA EL AVISO
        String path = "/Avisos/aviso/idPlanPublicacion";
        Element element = Optional.ofNullable(getElement(path)).orElseGet(() -> createElement(path));
        element.setTextContent(OK_PRODUCT_ID);
    }


    @Dado("un formulario de aviso con sus campos requeridos completados correctamente")
    public void un_formulario_de_aviso_con_sus_campos_requeridos_completados_correctamente() throws IOException, URISyntaxException {
        getElement("/Avisos/aviso/txPuesto").setTextContent(TXP_TEXT);
        getElement("/Avisos/aviso/txDescripcion").setTextContent(TXD_TEXT);
        getElement("/Avisos/aviso/numCantidadVacantes").setTextContent(NCV_TEXT);
    }

    @Dado("un codigoAviso unico para dicho integrador")
    public void un_codigoAviso_unico_para_dicho_integrador() {
        getElement("/Avisos/aviso/txCodigoReferencia").setTextContent(AD_ALIAS);
    }


    @Entonces("se crea y se publica el aviso")
    public void se_crea_y_se_publica_el_aviso() throws Exception {
        SimpleHttpResponse response = createAd();

        String responseBody = response.getBody().get();
        System.out.println(responseBody);

        Document responseDoc = parseDocument(responseBody);
        Element responseTxp = getElement("/Retorno/aviso/Aviso", responseDoc);
        assertNotNull(responseTxp);
        assertEquals(TXP_TEXT, responseTxp.getTextContent());

        Element statusElem = getElement("/Retorno/aviso/status", responseDoc);
        assertNotNull(statusElem);
        assertEquals(RESPONSE_OK_STATUS, statusElem.getTextContent());
    }

    /* Fin de Escenario: crear y publicar un aviso correctamente ------------------------------- */

    /* Escenario: crear y publicar un aviso falla por duplicacion de codigoAviso --------------- */

    @Dado("un codigoAviso duplicado para dicho integrador")
    public void un_codigoAviso_duplicado_para_dicho_integrador() {
        // TODO : esto no tiene el comportamiento esperado... El api deberia rechazar el alta del aviso, no osbtante lo acepta.
        System.out.println("un_codigoAviso_duplicado_para_dicho_integrador");
    }

    @Entonces("la creacion del aviso falla por codigoAviso duplicado")
    public void la_creacion_del_aviso_falla_por_codigoAviso_duplicado() throws Exception {
        // TODO : esto no tiene el comportamiento esperado... El api deberia rechazar el alta del aviso, no osbtante lo acepta.
        System.out.println("la_creacion_del_aviso_falla_por_codigoAviso_duplicado");
    }

    /* Fin de crear y publicar un aviso falla por duplicacion de codigoAviso ------------------ */

    @Dado("un formulario de aviso con sus campos requeridos completados erroneamente")
    public void un_formulario_de_aviso_con_sus_campos_requeridos_completados_erroneamente() throws IOException {
        getElement("/Avisos/aviso/idPais").setTextContent(INVALID_COUNTRY_ID);
    }

    @Entonces("la creacion del aviso falla por errores presentes en el formulario de aviso")
    public void la_creacion_del_aviso_falla_por_errores_presentes_en_el_formulario_de_aviso() throws Exception {
        SimpleHttpResponse response = createAd();

        String responseBody = response.getBody().get();
        System.out.println(responseBody);

        Document responseDoc = parseDocument(responseBody);
        Element statusElem = getElement("/Retorno/aviso/status", responseDoc);
        assertNotNull(statusElem);
        assertEquals("2", statusElem.getTextContent());

        Element msgElem = getElement("/Retorno/aviso/mensaje", responseDoc);
        assertNotNull(msgElem);
        assertEquals("idPais invalido/idPais invalid", msgElem.getTextContent().trim());
    }

    /* Escenario: crear y publicar un aviso falla porque la empresa no tiene creditos suficientes para publicarlo */

    @Dado("que tiene creditos insuficientes para publicar avisos")
    public void que_tiene_creditos_insuficientes_para_publicar_avisos() {
        Element productElement = document.createElement("idPlanPublicacion");
        productElement.setTextContent("700");
        getElement("/Avisos/aviso").appendChild(productElement);
    }

    @Entonces("la publicacion del aviso falla por falta de creditos de la empresa")
    public void la_publicacion_del_aviso_falla_por_falta_de_creditos_de_la_empresa() throws Exception {
        // TODO : QUEDA PENDIENTE DADO QUE NO SABEMOS COMO TESTEAR O VERIFICAR QUE UN AVISO FUE CREADO PERO NO PUBLICADO
        System.out.println("la_publicacion_del_aviso_falla_por_falta_de_creditos_de_la_empresa");
    }

    /* fin de crear y publicar un aviso falla porque la empresa no tiene creditos suficientes para publicarlo */

    /* Escenario: crear y publicar un aviso falla porque la empresa no esta habilitada para integrar */

    @Dado("una empresa no habilitada para integrar")
    public void una_empresa_no_habilitada_para_integrar() {
        newDocument();

        Element dataElement = getElement("/Avisos/aviso/DatosAdicionales");
        dataElement.setAttribute("emp_idempresa", "99999999");
        dataElement.setAttribute("emp_token", "aaaa11111111111");
    }

    @Entonces("la creacion del aviso falla porque la empresa no esta habilitada para integrar")
    public void la_creacion_del_aviso_falla_porque_la_empresa_no_esta_habilitada_para_integrar() throws Exception {
        SimpleHttpResponse response = createAd();

        String responseBody = response.getBody().get();
        System.out.println(responseBody);

        Document responseDoc = parseDocument(responseBody);
        Element statusElem = getElement("/Retorno/aviso/status", responseDoc);
        assertNotNull(statusElem);
        assertEquals("2", statusElem.getTextContent());

        Element msgElem = getElement("/Retorno/aviso/mensaje", responseDoc);
        assertNotNull(msgElem);
        assertEquals("NO PUEDE PUBLICAR PORQUE EL PEDIDO NO SE HIZO DESDE UNA IP VERIFICADA NI SE RECIBIO UN TOKEN VALIDO", msgElem.getTextContent().trim());
    }

    /* fin de crear y publicar un aviso falla porque la empresa no esta habilitada para integrar */
}
