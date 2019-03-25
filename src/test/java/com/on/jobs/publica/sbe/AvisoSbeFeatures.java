package com.on.jobs.publica.sbe;

import com.mz.client.http.SimpleHttpBody;
import com.mz.client.http.SimpleHttpClient;
import com.mz.client.http.SimpleHttpResponse;
import com.on.jobs.publica.SpringIntegrationTest;
import cucumber.api.PendingException;
import cucumber.api.java.es.Dado;
import cucumber.api.java.es.Entonces;
import cucumber.api.java.es.Y;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class AvisoSbeFeatures extends SpringIntegrationTest {
    //    private static final String URL = "http://www.bumeran.com.ar/api/publicador/index.bum";
    private static final String URL = "https://www.bumeran.com.ar/api/publicador/index.bum";

    // TODO : insertar id y token de empresa existente (SoftwareGZ)
    private static final String OK_COMPANY_ID = "13130935"; // id de empresa existente
    private static final String OK_COMPANY_TOKEN = "fe545e2f0b3439e"; // token valido de empresa existente


    /**
     * txPuesto
     */
//    private static final String TXP_TEXT = "Cajero /a Unico /a Eventual";
    private static final String TXP_TEXT = "Cajero reloco";
    private static final String TXP_TEXT_MODIF = "Cajero ligeramente capacitado";
    /**
     * txDescripcion
     */
    private static final String TXD_TEXT = "<![CDATA[<p>Si te interesa formar parte de un equipo de trabajo agradable, te invitamos a integrarte a Empresa S.A.</p>]]>";
    private static final String TXD_TEXT_MODIF = "<![CDATA[<p>Esta descripcion fue modificada.</p>]]>";
    /**
     * numCantidadVacantes
     */
    private static final String NCV_TEXT = "2";
    private static final String NCV_TEXT_MODIF = "1";


    /* Este codigo se debe tomar del catalogo "IDPLANPUBLICACION" de acuerdo al tipo de membresia y pais contratado. */
//    private static final String OK_PRODUCT_ID = "60";
    private static final String OK_PRODUCT_ID = "70";
    private static final String CHARSET = "UTF-8";
    private static final String INVALID_COUNTRY_ID = "999";
    private static final String AD_ALIAS = "45678";

    /* Codigos de respuesta */
    private static final String RESPONSE_OK_STATUS = "1";
    private static final String RESPONSE_ERR_STATUS = "2";


    private SimpleHttpResponse createAd() throws UnsupportedEncodingException {
        getElement("/Avisos/aviso/txAccion").setTextContent("CREAR");
        return execute();
    }

    private SimpleHttpResponse updateAd() throws UnsupportedEncodingException {
        getElement("/Avisos/aviso/txAccion").setTextContent("MODIFICAR");
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
        String path = "/Avisos/aviso/idPlanPublicacion";
        Optional.ofNullable(getElement(path)).orElseGet(() -> createElement(path)).setTextContent(OK_PRODUCT_ID);
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

    private static String publishedAdId;

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

        Element adIdElem = getElement("/Retorno/aviso/idAviso" , responseDoc);
        assertThat(adIdElem , is(not(nullValue())));
        publishedAdId = adIdElem.getTextContent();
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

        assertThat(statusElem, is(not(nullValue())));

        assertThat(statusElem.getTextContent(), is(equalTo(RESPONSE_ERR_STATUS)));


        Element msgElem = getElement("/Retorno/aviso/mensaje", responseDoc);
        assertNotNull(msgElem);
        assertEquals("idPais invalido/idPais invalid", msgElem.getTextContent().trim());
    }

    /* Escenario: crear y publicar un aviso falla porque la empresa no tiene creditos suficientes para publicarlo */

    @Dado("que tiene creditos insuficientes para publicar avisos")
    public void que_tiene_creditos_insuficientes_para_publicar_avisos() {
        String path = "/Avisos/aviso/idPlanPublicacion";
        Optional.ofNullable(getElement(path)).orElseGet(() -> createElement(path)).setTextContent("9999");
    }

    /**
     * Id de aviso guardado como borrador para usar en otro test
     */
    private static String draftAdId;

    /**
     * Este test funciona parcialmente automatizado. Se debe verificar que el aviso fue publicado en modo borrador
     * visitando <a href="https://www.bumeran.com.ar/empresas/avisos">https://www.bumeran.com.ar/empresas/avisos</a>.
     */
    @Entonces("el aviso se crea en borrador pero la publicacion del aviso falla por falta de creditos de la empresa")
    public void el_aviso_se_crea_en_borrador_pero_la_publicacion_del_aviso_falla_por_falta_de_creditos_de_la_empresa() throws Exception {
        SimpleHttpResponse response = createAd();
        String responseBody = response.getBody().get();

        Document responseDoc = parseDocument(responseBody);
        Element responseTxp = getElement("/Retorno/aviso/Aviso", responseDoc);
        assertNotNull(responseTxp);
        assertEquals(TXP_TEXT, responseTxp.getTextContent());

        Element statusElem = getElement("/Retorno/aviso/status", responseDoc);
        assertNotNull(statusElem);
        assertEquals(RESPONSE_OK_STATUS, statusElem.getTextContent());

        Element adIdElem = getElement("/Retorno/aviso/idAviso", responseDoc);
        assertThat(adIdElem, is(not(nullValue())));
        draftAdId = adIdElem.getTextContent().trim();
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
        assertEquals(RESPONSE_ERR_STATUS, statusElem.getTextContent());

        Element msgElem = getElement("/Retorno/aviso/mensaje", responseDoc);
        assertNotNull(msgElem);
        assertEquals("NO PUEDE PUBLICAR PORQUE EL PEDIDO NO SE HIZO DESDE UNA IP VERIFICADA NI SE RECIBIO UN TOKEN VALIDO", msgElem.getTextContent().trim());
    }

    /* fin de crear y publicar un aviso falla porque la empresa no esta habilitada para integrar */

    private String existingAdId;

    @Dado("un aviso existente")
    public void un_aviso_existente() {
        String path = "/Avisos/aviso/idAviso";
//        existingAdId = "1113192190";
//        existingAdId = draftAdId;
        existingAdId = publishedAdId;
        Optional.ofNullable(getElement(path))
                .orElseGet(() -> createElement(path))
                .setTextContent(existingAdId);
    }

    @Dado("un formulario de aviso con sus campos modificados")
    public void un_formulario_de_aviso_con_sus_campos_modificados() {
        getElement("/Avisos/aviso/txPuesto").setTextContent(TXP_TEXT_MODIF);
        getElement("/Avisos/aviso/txDescripcion").setTextContent(TXD_TEXT_MODIF);
        getElement("/Avisos/aviso/numCantidadVacantes").setTextContent(NCV_TEXT_MODIF);
    }

    @Entonces("se actualiza el aviso correctamente")
    public void se_actualiza_el_aviso_correctamente() throws Exception {
        SimpleHttpResponse httpResponse = updateAd();
        SimpleHttpBody responseBody = httpResponse.getBody();

        assertThat(responseBody, is(not(nullValue())));
        assertThat(responseBody.isPresent(), is(true));

        Document doc = parseDocument(responseBody.get());
        assertThat(getElement("/Retorno/aviso/Aviso", doc).getTextContent(), is(TXP_TEXT_MODIF));
        assertThat(getElement("/Retorno/aviso/idAviso", doc).getTextContent(), is(existingAdId));
        assertThat(getElement("/Retorno/aviso/status", doc).getTextContent(), is("1"));
    }

    @Entonces("^la actualizacion del aviso falla por errores presentes en el formulario de aviso$")
    public void laActualizacionDelAvisoFallaPorErroresPresentesEnElFormularioDeAviso() throws Throwable {
        SimpleHttpResponse httpResponse = updateAd();
        SimpleHttpBody responseBody = httpResponse.getBody();

        assertThat(responseBody, is(not(nullValue())));
        assertThat(responseBody.isPresent(), is(true));

        Document doc = parseDocument(responseBody.get());
        assertThat(getElement("/Retorno/aviso/mensaje", doc).getTextContent(), containsString("idPais invalido"));
        assertThat(getElement("/Retorno/aviso/status", doc).getTextContent(), is(RESPONSE_ERR_STATUS));
    }

    @Y("^un aviso inexistente$")
    public void unAvisoInexistente() throws Throwable {
        String path = "/Avisos/aviso/idAviso";
        existingAdId = "8888888888";
        Optional.ofNullable(getElement(path))
                .orElseGet(() -> createElement(path))
                .setTextContent(existingAdId);
    }


    @Entonces("^la actualizacion del aviso falla porque el aviso no existe$")
    public void laActualizacionDelAvisoFallaPorqueElAvisoNoExiste() throws Throwable {
        SimpleHttpResponse httpResponse = updateAd();
        SimpleHttpBody responseBody = httpResponse.getBody();

        assertThat(responseBody, is(not(nullValue())));
        assertThat(responseBody.isPresent(), is(true));

        Document doc = parseDocument(responseBody.get());
        assertThat(getElement("/Retorno/aviso/mensaje", doc).getTextContent(), containsString("VERIFIQUE LOS DATOS ENVIADOS"));
        assertThat(getElement("/Retorno/aviso/status", doc).getTextContent(), is(RESPONSE_ERR_STATUS));
    }

    @Entonces("^la modificacion del aviso falla por credenciales de empresa invalidas$")
    public void laModificacionDelAvisoFallaPorCredencialesDeEmpresaInvalidas() throws Throwable {
        SimpleHttpResponse httpResponse = updateAd();
        SimpleHttpBody responseBody = httpResponse.getBody();

        assertThat(responseBody, is(not(nullValue())));
        assertThat(responseBody.isPresent(), is(true));

        Document doc = parseDocument(responseBody.get());
        assertThat(getElement("/Retorno/aviso/mensaje", doc).getTextContent(), containsString("NI SE RECIBIO UN TOKEN VALIDO"));
        assertThat(getElement("/Retorno/aviso/status", doc).getTextContent(), is(RESPONSE_ERR_STATUS));
    }
}
