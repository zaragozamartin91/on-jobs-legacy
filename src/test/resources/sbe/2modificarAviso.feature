#language: es
Característica: Modificar aviso
  Escenario: modificar un aviso existente correctamente
    Dado una empresa habilitada para integrar
    Y que tiene creditos suficientes para publicar avisos
    Y un aviso existente
    Y un formulario de aviso con sus campos modificados
    Entonces se actualiza el aviso correctamente

  Escenario: modificar un aviso existente falla porque el formulario es invalido
    Dado una empresa habilitada para integrar
    Y que tiene creditos suficientes para publicar avisos
    Y un aviso existente
    Y un formulario de aviso con sus campos requeridos completados erroneamente
    Entonces la actualizacion del aviso falla por errores presentes en el formulario de aviso

  Escenario: modificar un aviso inexistente falla porque el mismo no existe
    Dado una empresa habilitada para integrar
    Y que tiene creditos suficientes para publicar avisos
    Y un aviso a modificar inexistente
    Y un formulario de aviso con sus campos modificados
    Entonces la actualizacion del aviso falla porque el aviso no existe

  Escenario: modificar un aviso falla por credenciales de empresa invalidas
    Dado una empresa no habilitada para integrar
    Y que tiene creditos suficientes para publicar avisos
    Y un aviso existente
    Y un formulario de aviso con sus campos modificados
    Entonces la modificacion del aviso falla por credenciales de empresa invalidas

