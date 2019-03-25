#language: es
Característica: Crear y publicar aviso
  Escenario: crear y publicar un aviso correctamente
    Dado una empresa habilitada para integrar
    Y que tiene creditos suficientes para publicar avisos
    Y un formulario de aviso con sus campos requeridos completados correctamente
    Y un codigoAviso unico para dicho integrador
    Entonces se crea y se publica el aviso

  Escenario: crear y publicar un aviso falla por duplicacion de codigoAviso
    Dado una empresa habilitada para integrar
    Y que tiene creditos suficientes para publicar avisos
    Y un formulario de aviso con sus campos requeridos completados correctamente
    Y un codigoAviso duplicado para dicho integrador
    Entonces la creacion del aviso falla por codigoAviso duplicado

  Escenario: crear y publicar un aviso falla por errores en el formulario de alta de aviso
    Dado una empresa habilitada para integrar
    Y que tiene creditos suficientes para publicar avisos
    Y un formulario de aviso con sus campos requeridos completados erroneamente
    Y un codigoAviso unico para dicho integrador
    Entonces la creacion del aviso falla por errores presentes en el formulario de aviso

  Escenario: un aviso se guarda como borrador por creditos insuficientes
    Dado una empresa habilitada para integrar
    Y que tiene creditos insuficientes para publicar avisos
    Y un formulario de aviso con sus campos requeridos completados correctamente
    Y un codigoAviso unico para dicho integrador
    Entonces el aviso se crea en borrador pero la publicacion del aviso falla por falta de creditos de la empresa

  Escenario: crear y publicar un aviso falla porque la empresa no esta habilitada para integrar
    Dado una empresa no habilitada para integrar
    Y que tiene creditos suficientes para publicar avisos
    Y un formulario de aviso con sus campos requeridos completados correctamente
    Y un codigoAviso unico para dicho integrador
    Entonces la creacion del aviso falla porque la empresa no esta habilitada para integrar

