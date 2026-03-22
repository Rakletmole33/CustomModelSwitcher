package org.github.rakletmole33.customModelSwitcher;

import org.bukkit.Material;
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
        int targetPage = Integer.parseInt(title.replaceAll("[^0-9]", ""));

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

            if (HandItem.getType() == Material.AIR) {
                // 【新規付与】空っぽなら新しく作る
                ItemStack newItem = new ItemStack(currentMaterial);
                ItemMeta meta = newItem.getItemMeta();
                if (meta != null) {
                    meta.setCustomModelData(newCMD);
                    newItem.setItemMeta(meta);
                }
                player.getInventory().addItem(newItem);
                player.sendMessage("§aCMD: " + newCMD + " を適用しました！");

            } else if (HandItem.getType() == currentMaterial) {
                // 【上書き】同じ種類のアイテムを持っているなら上書き
                ItemMeta handMeta = HandItem.getItemMeta();
                if (handMeta != null) {
                    handMeta.setCustomModelData(newCMD);
                    HandItem.setItemMeta(handMeta);
                    player.sendMessage("§aCMD: " + newCMD + " を適用しました！");
                }

            } else {
                // 【エラー】違うアイテムを持っている場合
                player.sendMessage("§cエラー: 別のアイテムを手に持っています！");
                player.sendMessage("§c手を空にするか、同じ種類のアイテムを持ってください。");
                // ここで return して closeInventory() を呼ばないようにすれば、GUIを開き直す手間も省けます
                return;
            }

            player.updateInventory();
            player.closeInventory();
        }
    }
}
