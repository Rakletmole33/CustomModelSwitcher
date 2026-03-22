package org.github.rakletmole33.customModelSwitcher;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("cms").setExecutor(new CustomModelSwitcher());
        getServer().getPluginManager().registerEvents(new GuiLIstener(this),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
