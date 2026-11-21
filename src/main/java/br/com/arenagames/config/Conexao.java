package br.com.arenagames.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fabrica de conexoes JDBC. Os dados de acesso vem do .env.
 */
public final class Conexao {

    private static final String URL;
    private static final String USUARIO;
    private static final String SENHA;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Driver do MySQL não encontrado", e);
        }

        String host = Env.get("DB_HOST", "localhost");
        String porta = Env.get("DB_PORT", "3306");
        String banco = Env.get("DB_NAME");
        USUARIO = Env.get("DB_USER");
        SENHA = Env.get("DB_PASS", "");

        if (banco == null || USUARIO == null) {
            throw new IllegalStateException("DB_NAME e DB_USER precisam estar definidos no .env");
        }

        URL = "jdbc:mysql://" + host + ":" + porta + "/" + banco
                + "?useSSL=false&allowPublicKeyRetrieval=true"
                + "&serverTimezone=America/Sao_Paulo&characterEncoding=UTF-8";
    }

    private Conexao() {
    }

    public static Connection abrir() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
