package com.on.jobs.publica;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum TestEnv {
    INSTANCE;

    TestEnv() {
        System.out.println("building TestEnv");
    }

    private final Map<String, Object> vars = new HashMap<>();

    /**
     * Intenta obtener un dato del mapa de memoria. En caso de no encontrarlo lo obtiene de un archivo en disco con nombre igual a la clave de busqueda.
     *
     * @param key Clave del dato a buscar
     * @return Dato requerido.
     */
    public Object get(Object key) {
        return Optional.ofNullable(vars.get(key)).orElseGet(() -> {
            try (BufferedReader reader = new BufferedReader(new FileReader(key.toString()))) {
                return reader.readLine();
            } catch (IOException e) {
                throw new IllegalStateException("Error al leer archivo " + key + ". Error: " + e);
            }
        });
    }

    /**
     * Guarda un dato en el mapa de memoria y en disco.
     *
     * @param key   Clave de dato
     * @param value Valor de dato
     * @return Valor anterior si es que existia uno.
     */
    public Object put(String key, Object value) {
        try (FileWriter writer = new FileWriter(key, false)) {
            writer.append(value.toString());
        } catch (IOException e) {
            System.err.println("Error al escribir archivo " + key + ". Error: " + e);
        }
        return vars.put(key, value);
    }
}
