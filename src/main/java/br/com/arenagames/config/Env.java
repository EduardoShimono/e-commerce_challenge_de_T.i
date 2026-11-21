package br.com.arenagames.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Le as variaveis de configuracao do arquivo .env.
 *
 * Ordem de busca do arquivo:
 *   1. caminho informado em -Denv.file=... ou na variavel de ambiente ENV_FILE
 *   2. arquivo .env na pasta onde a aplicacao foi iniciada
 *
 * Variaveis de ambiente do sistema operacional tem prioridade sobre o arquivo.
 */
public final class Env {

    private static final Map<String, String> valores = new HashMap<>();

    static {
        carregar();
    }

    private Env() {
    }

    private static void carregar() {
        Path arquivo = localizarArquivo();
        if (arquivo == null) {
            System.err.println("[Env] Arquivo .env não encontrado. Usando apenas variáveis do sistema.");
            return;
        }

        try {
            List<String> linhas = Files.readAllLines(arquivo, StandardCharsets.UTF_8);
            for (String linha : linhas) {
                String l = linha.trim();
                if (l.isEmpty() || l.startsWith("#") || !l.contains("=")) {
                    continue;
                }
                int pos = l.indexOf('=');
                String chave = l.substring(0, pos).trim();
                String valor = l.substring(pos + 1).trim();
                if (valor.length() >= 2 && (valor.startsWith("\"") && valor.endsWith("\"")
                        || valor.startsWith("'") && valor.endsWith("'"))) {
                    valor = valor.substring(1, valor.length() - 1);
                }
                valores.put(chave, valor);
            }
            System.out.println("[Env] Configurações carregadas de " + arquivo.toAbsolutePath());
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível ler o arquivo .env", e);
        }
    }

    private static Path localizarArquivo() {
        String informado = System.getProperty("env.file");
        if (informado == null || informado.isBlank()) {
            informado = System.getenv("ENV_FILE");
        }
        if (informado != null && !informado.isBlank()) {
            Path p = Paths.get(informado);
            return Files.isRegularFile(p) ? p : null;
        }

        Path local = Paths.get(System.getProperty("user.dir"), ".env");
        return Files.isRegularFile(local) ? local : null;
    }

    public static String get(String chave) {
        String doSistema = System.getenv(chave);
        if (doSistema != null) {
            return doSistema;
        }
        return valores.get(chave);
    }

    public static String get(String chave, String padrao) {
        String v = get(chave);
        return v == null ? padrao : v;
    }
}
