#language: es
Característica: Modificar aviso
  Escenario: modificar un aviso existente correctamente
    Dado una empresa habilitada para integrar
    Y que tiene creditos suficientes para publicar avisos
    Y un aviso existente
    Y un formulario de aviso con sus campos modificados
    Entonces se actualiza el aviso correctamente

  Escenario: modificar un aviso existente falla
    Dado una empresa habilitada para integrar
    Y que tiene creditos suficientes para publicar avisos
    Y un aviso existente
    Y un formulario de aviso con sus campos requeridos completados erroneamente
    Entonces la actualizacion del aviso falla por errores presentes en el formulario de aviso

  Escenario: modificar un aviso inexistente falla
    Dado una empresa habilitada para integrar
    Y que tiene creditos suficientes para publicar avisos
    Y un aviso inexistente
    Y un formulario de aviso con sus campos modificados
    Entonces la actualizacion del aviso falla porque el aviso no existe

  Escenario: modificar un aviso existente correctamente
    Dado una empresa habilitada para integrar
    Y que tiene creditos suficientes para publicar avisos
    Y un aviso existente
    Y un formulario de aviso con sus campos modificados
    Entonces se actualiza el aviso correctamente

#  Escenario: crear y publicar un aviso falla porque la empresa no esta habilitada para integrar
#    Dado una empresa no habilitada para integrar
#    Y que tiene creditos suficientes para publicar avisos
#    Y un formulario de aviso con sus campos requeridos completados correctamente
#    Y un codigoAviso unico para dicho integrador
#    Entonces la creacion del aviso falla porque la empresa no esta habilitada para integrar

