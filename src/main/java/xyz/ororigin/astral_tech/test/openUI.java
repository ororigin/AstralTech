package xyz.ororigin.astral_tech.test;

import icyllis.modernui.ModernUI;
import icyllis.modernui.audio.AudioManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import xyz.ororigin.astral_tech.ui.techtree.SynapseGUI;

public class openUI {

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "true");
        Configurator.setRootLevel(Level.DEBUG);

        try (ModernUI app = new ModernUI()) {
            app.run(new SynapseGUI()); // 在这里传入您的 Fragment 实例
        }
        AudioManager.getInstance().close();
        System.gc();
    }
}
