package com.riwi.talent.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lector minimo de archivos .env para no depender de librerias externas.
 * Solo carga pares clave=valor simples y omite comentarios o lineas vacias.
 */
public final class EnvFileLoader {

    private EnvFileLoader() {
    }

    public static Map<String, String> load(String fileName) {
        Map<String, String> values = new HashMap<>();
        Path envPath = resolveEnvPath(fileName);

        if (envPath == null || !Files.exists(envPath)) {
            return values;
        }

        try {
            List<String> lines = Files.readAllLines(envPath);

            for (String rawLine : lines) {
                String line = rawLine.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                int separatorIndex = line.indexOf('=');
                if (separatorIndex <= 0) {
                    continue;
                }

                String key = line.substring(0, separatorIndex).trim();
                String value = line.substring(separatorIndex + 1).trim();
                values.put(key, limpiarComillas(value));
            }
        } catch (IOException exception) {
            throw new IllegalStateException("No fue posible leer el archivo .env", exception);
        }

        return values;
    }

    private static Path resolveEnvPath(String fileName) {
        Path directPath = Path.of(fileName);
        if (Files.exists(directPath)) {
            return directPath;
        }

        Path current = Path.of(System.getProperty("user.dir")).toAbsolutePath();
        while (current != null) {
            Path candidate = current.resolve(fileName);
            if (Files.exists(candidate)) {
                return candidate;
            }
            current = current.getParent();
        }

        return null;
    }

    private static String limpiarComillas(String value) {
        if (value.length() >= 2) {
            boolean dobleComilla = value.startsWith("\"") && value.endsWith("\"");
            boolean comillaSimple = value.startsWith("'") && value.endsWith("'");
            if (dobleComilla || comillaSimple) {
                return value.substring(1, value.length() - 1);
            }
        }
        return value;
    }
}
