package astra.core.menus.account.social;

import astra.core.Core;
import astra.core.api.menu.PlayerMenu;
import astra.core.menus.account.AccountEditMenu;
import astra.core.menus.account.AccountPageMenu;
import astra.core.menus.account.AccountPreferencesMenu;
import astra.core.menus.account.aparencia.PrincipalAparenciaMenu;
import astra.core.player.Account;
import astra.core.utils.BukkitUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class SocialMenu extends PlayerMenu {

    public SocialMenu(Account profile) {
        super(profile.getPlayer(), "Redes sociais", 5);

        this.setItem(0, BukkitUtils.putProfileOnSkull(this.player, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aVisualizar sua conta")));

        this.setItem(2, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aEditar seu perfil : desc>§7Altere o seu perfil! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTFmNTExZDY4NmJlNzA3YTEwMTJmYzc1NjJlMzEzNTQ5YzdiZmE0YmFmZDEzODg3NGEwMmQzYmFiMTRmMjJkYSJ9fX0="));
        this.setItem(3, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aPreferências : desc>§7Configure suas preferências! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmVhMGY3YTVmMTVmYmNmMjg1MWU5NzA3YzdlNTI3ZTFkOWNiYjg5NDhiOGZmZTMzMTI3ODhjY2I1YWNhNzZkYiJ9fX0="));
        this.setItem(4, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aConfigurar aparência : desc>§7Atualize sua skin, medalha\n§7tags e muito mais! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjI5NmIzMWIwNTMwZWU2YmYzNzYwZjgzOTBkZGM0ZTBkMDkwOThlNWExYmE2ZWE2NjJmN2U2YWFhYTRkY2QyMiJ9fX0="));
        this.setItem(5, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aRedes sociais : desc>§7Atualize suas redes sociais! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDQ1NTg2MDY1ZjAyZTg1NTMwMTI1ZjQ2NmRmMTA3YzFjOGQzZTE0MzEwMTAzMmMyYjQ3ODA3MTE1YmJkYTJmMCJ9fX0="));
        this.setItem(6, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aSua credibilidade : desc>§cEm desenvolvimento... : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmFkZjlkNzEzNjdjZDZlNTA1ZmI0OGNhYWE1YWNkY2RmZjJhMDlmNjZjNDg4ZGFmMDRkMDQ1ZWUwYmY1MjhlMSJ9fX0="));
        this.setItem(7, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§aPresentes : desc>§cEm desenvolvimento... : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGMyZjVjNWJlNmI1NzUwMTY3NzM3MzU5NzRjOWNhODgwN2UzMDI1ZTAxYjBlNjc2Y2E3MTQ3ZTAwNTQ1YjUxMCJ9fX0="));

        this.setItem(9, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
        this.setItem(10, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
        this.setItem(12, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
        this.setItem(11, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
        this.setItem(13, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
        this.setItem(14, BukkitUtils.deserializeItemStack("160:5 : 1 : nome>§aCategoria atual"));
        this.setItem(15, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
        this.setItem(16, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));
        this.setItem(17, BukkitUtils.deserializeItemStack("160:7 : 1 : nome>§7↑ Categoria : desc>&7↓ Conteúdo"));

        this.setItem(28, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§bX : desc>§7Vinculado atualmente: " + (profile.getDataContainer("Account", "x").getAsString().isEmpty() ? "§cNenhum" : "§b" + profile.getDataContainer("AccountSocial", "x").getAsString()) + "\n\n§eClique para atualizar! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTFiN2EwYzIxMGU2Y2RmNWEzNWZkODE5N2U2ZTI0YTAzODMxNWJiZTNiZGNkMWJjYzM2MzBiZjI2ZjU5ZWM1YyJ9fX0="));
        this.setItem(29, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§cYouTube : desc>§7Vinculado atualmente: " + (profile.getDataContainer("Account", "youtube").getAsString().isEmpty() ? "§cNenhum" : "§b" + profile.getDataContainer("AccountSocial", "youtube").getAsString()) + "\n\n§eClique para atualizar! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWM1NmRkYmVmMWI4MWJkNTdjOWIxYzZkNWQxYjc4YjU0NzM3YjcxOTkyOWIyOWMyYTkzMGE1ZjdjMmFlNGE4NiJ9fX0="));
        this.setItem(30, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§5Twitch : desc>§7Vinculado atualmente: " + (profile.getDataContainer("Account", "twitch").getAsString().isEmpty() ? "§cNenhum" : "§b" + profile.getDataContainer("AccountSocial", "twitch").getAsString()) + "\n\n§eClique para atualizar! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmYxOGZhNDNkNGQ5Mzc4OTQ4YjU2Yjg1YjUzMTk3OTA3NDExOWMxMjUyMzJlNzE1Y2U0YmQ1Mjc4MGFjNGQ3NiJ9fX0="));
        this.setItem(31, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§9Discord : desc>§7Vinculado atualmente: " + (profile.getDataContainer("Account", "discord").getAsString().isEmpty() ? "§cNenhum" : "§b" + profile.getDataContainer("AccountSocial", "discord").getAsString()) + "\n\n§eClique para atualizar! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGQ0MjMzN2JlMGJkY2EyMTI4MDk3ZjFjNWJiMTEwOWU1YzYzM2MxNzkyNmFmNWZiNmZjMjAwMDAwMTFhZWI1MyJ9fX0="));
        this.setItem(32, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§6Tik Tok : desc>§7Vinculado atualmente: " + (profile.getDataContainer("Account", "tiktok").getAsString().isEmpty() ? "§cNenhum" : "§b" + profile.getDataContainer("AccountSocial", "tiktok").getAsString()) + "\n\n§eClique para atualizar! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjc4NmRjOWQ2NWMzYjc5ODNlOGJiNmFmNTg4ZmFjOTJmMzM3MjBjNzIzZTBkMjlmOGI3NDE1MTM0NGE2NDhhMSJ9fX0="));
        this.setItem(33, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>§dInstagram : desc>§7Vinculado atualmente: " + (profile.getDataContainer("Account", "instagram").getAsString().isEmpty() ? "§cNenhum" : "§b" + profile.getDataContainer("AccountSocial", "instagram").getAsString()) + "\n\n§eClique para atualizar! : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWM4OGQ2MTYzZmFiZTdjNWU2MjQ1MGViMzdhMDc0ZTJlMmM4ODYxMWM5OTg1MzZkYmQ4NDI5ZmFhMDgxOTQ1MyJ9fX0="));
        this.register(Core.getInstance());
        this.open();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent evt) {
        if (evt.getInventory().equals(this.getInventory())) {
            evt.setCancelled(true);
            if (evt.getWhoClicked().equals(this.player)) {
                Account profile = Account.getAccount(this.player.getName());
                if (profile == null) {
                    this.player.closeInventory();
                    return;
                }

                if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())) {
                    ItemStack item = evt.getCurrentItem();
                    if (item != null && item.getType() != Material.AIR) {
                        switch (evt.getSlot()) {
                            case 0:
                                new AccountPageMenu(profile);
                                break;
                            case 2:
                                new AccountEditMenu(profile);
                                break;
                            case 3:
                                new AccountPreferencesMenu(profile);
                                break;
                            case 4:
                                new PrincipalAparenciaMenu(profile);
                                break;
                        }
                    }
                }
            }
        }
    }

    public void cancel() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent evt) {
        if (evt.getPlayer().equals(this.player)) {
            this.cancel();
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent evt) {
        if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getInventory())) {
            this.cancel();
        }
    }
}
