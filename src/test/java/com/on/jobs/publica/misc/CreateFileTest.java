package com.on.jobs.publica.misc;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class CreateFileTest {
    @Test public void testCreateFile() throws IOException {
        String key = "temp.txt";
        int value = new Random().nextInt();
        FileWriter fileWriter = new FileWriter(key, false);
        fileWriter.append(String.valueOf(value));
        fileWriter.close();

        try (BufferedReader reader = new BufferedReader(new FileReader(key))) {
            String line = reader.readLine();
            int v = Integer.valueOf(line);
            assertEquals(value , v);
        } catch (IOException e) {
            throw new IllegalStateException("Error al leer archivo " + key + ". Error: " + e);
        }
    }
}
