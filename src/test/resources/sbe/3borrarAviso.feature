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

  Escenario: borrar un aviso que ya fue borrado
    Dado una empresa habilitada para integrar
    Y un aviso que ya fue borrado
    Entonces se elimina el aviso

  Escenario: borrar un aviso con id invalido falla
    Dado una empresa habilitada para integrar
    Y un aviso con id invalido
    Entonces la eliminacion del aviso falla porque el id es invalido

  Escenario: borrar un aviso falla porque el mismo no existe
    Dado una empresa habilitada para integrar
    Y un aviso con que no existe
    Entonces la eliminacion del aviso falla porque el aviso no existe

  Escenario: borrar un aviso falla porque la empresa no existe
    Dado una empresa no habilitada para integrar
    Y un aviso existente
    Entonces la eliminacion del aviso falla porque la empresa no existe
