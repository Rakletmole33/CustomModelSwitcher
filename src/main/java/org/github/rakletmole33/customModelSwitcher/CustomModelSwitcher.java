package org.github.rakletmole33.customModelSwitcher;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

public final class CustomModelSwitcher implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // 1. プレイヤー以外（コンソール）を弾く
        if (!(sender instanceof Player)) {
            sender.sendMessage("おっと！このコマンドはコンソールから実行できない！！");
            return true;
        }
        Player player = (Player) sender;
        int page = 1;
        Material baseMaterial = player.getInventory().getItemInMainHand().getType();
        for (String arg : args) {
            if (arg.matches("-?\\d+")){
                page = Integer.parseInt(arg);
            }
            else {
                Material m = Material.matchMaterial(arg.toUpperCase());
                if (m != null) baseMaterial = m;
            }
        }
        if (baseMaterial == Material.AIR){
            player.sendMessage("§cアイテム名を指定するか、何か手に持って実行してください！");
            return true;
        }
        TexInventory(player, page, baseMaterial); // プレイヤーにGUIを表示

        return true;
    }

    public void TexInventory(Player player, int page, Material baseMaterial) {
        // タイトルにページ数を出すと便利です
        Inventory gui = Bukkit.createInventory(null, 54, "CustomModelSwitcher - Page " + page);
        ItemStack BaseItem = new ItemStack(baseMaterial);


        for (int i = 0; i < 45; i++) {
            int CurrentCMD = ((page - 1) * 45) + i + 1;
            ItemStack item = BaseItem.clone();
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setCustomModelData(CurrentCMD);
                // 修正：括弧を外して変数を連結する
                meta.setDisplayName("§bModel ID: " + CurrentCMD);
                List<String> lore = new ArrayList<>();
                lore.add("§7CustomModelData: §e" + CurrentCMD);
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
            gui.setItem(i, item);
        }

        // ボタンの配置（ここは今のままでOK！）
        if (page > 10) gui.setItem(45, Button(Material.LIME_STAINED_GLASS_PANE, "§f10ページ戻る"));
        else gui.setItem(45, Button(Material.GRAY_STAINED_GLASS_PANE, " "));
        if (page > 5)  gui.setItem(46, Button(Material.LIME_STAINED_GLASS_PANE, "§f5ページ戻る"));
        else gui.setItem(46, Button(Material.GRAY_STAINED_GLASS_PANE, " "));
        if (page > 1)  gui.setItem(47, Button(Material.LIME_STAINED_GLASS_PANE, "§f1ページ戻る"));
        else gui.setItem(47, Button(Material.GRAY_STAINED_GLASS_PANE, " "));

        gui.setItem(48, Button(Material.RED_STAINED_GLASS_PANE, "§c閉じる"));
        gui.setItem(49, Button(Material.COMPASS, "§c0"));
        gui.setItem(50, Button(Material.RED_STAINED_GLASS_PANE, "§c閉じる"));

        gui.setItem(51, Button(Material.LIME_STAINED_GLASS_PANE, "§f1ページ進む"));
        gui.setItem(52, Button(Material.LIME_STAINED_GLASS_PANE, "§f5ページ進む"));
        gui.setItem(53, Button(Material.LIME_STAINED_GLASS_PANE, "§f10ページ進む"));

        // ★最重要：これを忘れると表示されません
        player.openInventory(gui);
    }

    private ItemStack Button(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }
}