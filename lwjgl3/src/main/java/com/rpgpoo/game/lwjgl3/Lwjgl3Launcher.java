package com.rpgpoo.game.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.rpgpoo.game.main.Valdarya;

// Lança o jogo no Desktop
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        // Inicia a classe principal do jogo
        return new Lwjgl3Application(new Valdarya(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();

        // Configs básicas da janela
        configuration.setTitle("Valdarya RPG");
        configuration.useVsync(true);
        configuration.setForegroundFPS(60);
        configuration.setWindowedMode(1280, 720); // HD padrão

        // Deixei ícone padrão pra evitar erro de arquivo não encontrado
        return configuration;
    }
}
