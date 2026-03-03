package com.sicredi.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton que carrega configuracoes do arquivo config.properties.
 * Suporta override via variaveis de ambiente para execucao em CI/CD.
 */
public class Configuration {

    private static Configuration instance;
    private final Properties properties;

    private Configuration() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            throw new RuntimeException("Falha ao carregar config.properties", e);
        }
    }

    /**
     * Retorna a instancia unica de Configuration.
     *
     * @return instancia singleton de Configuration
     */
    public static synchronized Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    /**
     * Retorna a URL base da API. Suporta override via variavel de ambiente BASE_URL
     * ou propriedade de sistema base.url.
     *
     * @return URL base da API (ex: https://dummyjson.com)
     */
    public String getBaseUrl() {
        return getProperty("base.url", "https://dummyjson.com");
    }

    /**
     * Retorna o username padrao para autenticacao.
     *
     * @return username padrao
     */
    public String getDefaultUsername() {
        return getProperty("default.username", "emilys");
    }

    /**
     * Retorna a senha padrao para autenticacao.
     *
     * @return senha padrao
     */
    public String getDefaultPassword() {
        return getProperty("default.password", "emilyspass");
    }

    /**
     * Busca uma propriedade com suporte a override via variavel de ambiente
     * e propriedade de sistema. Prioridade: System property > env var > config file > default.
     *
     * @param key          chave da propriedade
     * @param defaultValue valor padrao caso nao encontrada
     * @return valor da propriedade
     */
    private String getProperty(String key, String defaultValue) {
        String systemProp = System.getProperty(key);
        if (systemProp != null && !systemProp.isBlank()) {
            return systemProp;
        }

        String envKey = key.replace(".", "_").toUpperCase();
        String envVar = System.getenv(envKey);
        if (envVar != null && !envVar.isBlank()) {
            return envVar;
        }

        return properties.getProperty(key, defaultValue);
    }
}
