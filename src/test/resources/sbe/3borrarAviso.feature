#language: es
Característica: Borrar un aviso
  Escenario: borrar un aviso existente
    Dado una empresa habilitada para integrar
    Y un aviso existente
    Entonces se elimina el aviso

  Escenario: borrar un aviso que la empresa no tiene falla
    Dado una empresa habilitada para integrar
    Y un aviso que no corresponde con la empresa
    Entonces la eliminacion del aviso falla porque no le pertenece a la empresa
