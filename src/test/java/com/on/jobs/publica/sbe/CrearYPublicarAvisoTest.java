package com.on.jobs.publica.sbe;

import com.on.jobs.publica.SpringIntegrationTest;
import cucumber.api.PendingException;
import cucumber.api.java.es.Dado;
import cucumber.api.java.es.Entonces;

import java.io.IOException;
import java.net.URISyntaxException;

public class CrearYPublicarAvisoTest extends SpringIntegrationTest {

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
        throw new PendingException();
    }


    @Dado("que tiene creditos suficientes para publicar avisos")
    public void que_tiene_creditos_suficientes_para_publicar_avisos() {
        throw new PendingException();
    }


    @Dado("un formulario de aviso con sus campos requeridos completados correctamente")
    public void un_formulario_de_aviso_con_sus_campos_requeridos_completados_correctamente() throws IOException, URISyntaxException {
        throw new PendingException();
    }

    @Dado("un codigoAviso unico para dicho integrador")
    public void un_codigoAviso_unico_para_dicho_integrador() {
        throw new PendingException();
    }

    @Entonces("se crea y se publica el aviso")
    public void se_crea_y_se_publica_el_aviso() throws Exception {
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
