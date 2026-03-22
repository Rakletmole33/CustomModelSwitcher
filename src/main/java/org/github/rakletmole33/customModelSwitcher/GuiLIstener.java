package org.github.rakletmole33.customModelSwitcher;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiLIstener implements Listener {

    private final Main plugin;

    public GuiLIstener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCLick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        // タイトル判定を新しくしたものに合わせる
        if (!title.contains("CustomModelSwitcher")) return;

        event.setCancelled(true);
        if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

        Player player = (Player) event.getWhoClicked();
        String name = event.getCurrentItem().getItemMeta().getDisplayName();

        // ページ番号を取得
        int currentPage = Integer.parseInt(title.replaceAll("[^0-9]", ""));
        int targetPage = currentPage;

        // ★重要：今開いているGUIの0番目のスロットから、表示アイテムの種類(Material)を取得
        // これをしないと、ページ移動したときに「手に持っているアイテム」にリセットされてしまいます
        org.bukkit.Material currentMaterial = event.getInventory().getItem(0).getType();

        if (name.contains("進む")) {
            if (name.contains("10")) targetPage += 10;
            else if (name.contains("5")) targetPage += 5;
            else targetPage += 1;

            // 引数に currentMaterial を追加！
            new CustomModelSwitcher().TexInventory(player, targetPage, currentMaterial);

        } else if (name.contains("戻る")) {
            if (name.contains("10")) targetPage -= 10;
            else if (name.contains("5")) targetPage -= 5;
            else targetPage -= 1;
            if (targetPage < 1) targetPage = 1;

            // 引数に currentMaterial を追加！
            new CustomModelSwitcher().TexInventory(player, targetPage, currentMaterial);

        } else if (name.contains("閉じる")) {
            player.closeInventory();

        } else if (name.contains("§bModel ID:")) {
            ItemStack HandItem = player.getInventory().getItemInMainHand();


            int newCMD = event.getCurrentItem().getItemMeta().getCustomModelData();
            ItemMeta handMeta = HandItem.getItemMeta();

            if (handMeta != null) {
                handMeta.setCustomModelData(newCMD);
                HandItem.setItemMeta(handMeta);

                player.getInventory().setItemInMainHand(HandItem);
                player.updateInventory();
                player.sendMessage("§aCMD: " + newCMD + " を適用しました！");
            }
            player.closeInventory();
        }
    }
}
