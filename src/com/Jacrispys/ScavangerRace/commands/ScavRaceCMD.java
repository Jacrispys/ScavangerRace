package com.Jacrispys.ScavangerRace.commands;

import com.Jacrispys.ScavangerRace.ScavangerRaceMain;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.LocaleLanguage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import static com.Jacrispys.ScavangerRace.utils.chat.chat;

public class ScavRaceCMD implements CommandExecutor, Listener {

    private final ScavangerRaceMain plugin;

    public ScavRaceCMD(ScavangerRaceMain plugin) {
        this.plugin = plugin;

        Objects.requireNonNull(plugin.getCommand("Scav")).setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void sendClickableCommand(Player player, String message, String command, String hover) {

        TextComponent component = new TextComponent(chat(message));

        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(chat(hover)).create()));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + command));
        player.spigot().sendMessage(component);
    }

    public String getItemName (ItemStack itemStack) {
        net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);

        return LocaleLanguage.a().a(nmsStack.getItem().getName());
    }

    private final HashMap<UUID, Boolean> start = new HashMap<>();
    private final HashMap<UUID, ItemStack> Item = new HashMap<>();
    private final HashMap<UUID, Long> timer = new HashMap<>();
    private final Inventory ScavInv = Bukkit.createInventory(null, 27, "Scavenger Race");

    private ItemStack newItem() {
        Random rand = new Random();
        int material = rand.nextInt(Material.values().length);
        ItemStack randomItem = new ItemStack(Material.values() [material], 1);
        return randomItem;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("scav")) {
            if(args.length == 0) {
                sender.sendMessage(chat("&3Scavanger Race Plugin " +
                        "\n &3Author: &BJacrispys" +
                        "\n &6Version: &a"));
            } else if (args.length == 1) {
                if(args[0].equalsIgnoreCase("help")) {
                    //help
                } else if(args[0].equalsIgnoreCase("start") && sender instanceof Player) {
                    Player p = (Player) sender;
                    ItemStack blank = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                    ItemMeta blankMeta = blank.getItemMeta();
                    Objects.requireNonNull(blankMeta).setDisplayName(" ");
                    blank.setItemMeta(blankMeta);
                    for(int i = 0; i < 27; i++) {
                        if(i != 11 && i != 13 && i != 15 && i != 4 && i != 22 ) {
                            ScavInv.setItem(i, blank);
                        } else if (i == 11) {
                            ItemStack confirm = new ItemStack(Material.GREEN_WOOL);
                            ItemMeta confirmMeta = blank.getItemMeta();
                            Objects.requireNonNull(confirmMeta).setDisplayName(chat("&aCONFRIM"));
                            confirm.setItemMeta(confirmMeta);
                            ScavInv.setItem(i, confirm);
                        } else if (i == 15) {
                            ItemStack cancel = new ItemStack(Material.RED_WOOL);
                            ItemMeta cancelMeta = blank.getItemMeta();
                            Objects.requireNonNull(cancelMeta).setDisplayName(chat("&cCANCEL"));
                            cancel.setItemMeta(cancelMeta);
                            ScavInv.setItem(i, cancel);
                        } else if (i == 4) {
                            ItemStack reroll = new ItemStack(Material.ORANGE_WOOL);
                            ItemMeta rerollMeta = blank.getItemMeta();
                            Objects.requireNonNull(rerollMeta).setDisplayName(chat("&6Reroll Item?"));
                            reroll.setItemMeta(rerollMeta);
                            ScavInv.setItem(i, reroll);
                        } else if (i == 22) {
                            ItemStack close = new ItemStack(Material.BARRIER);
                            ItemMeta closeMeta = blank.getItemMeta();
                            Objects.requireNonNull(closeMeta).setDisplayName(chat("&cCLOSE"));
                            close.setItemMeta(closeMeta);
                            ScavInv.setItem(i, close);
                        } else if (i == 13) {
                            ScavInv.setItem(i, newItem());
                            if((ScavInv.getItem(i)).getType() == Material.AIR || (ScavInv.getItem(i)).getType() == null) {
                                ScavInv.setItem(i, newItem());
                            }
                        }
                        p.openInventory(ScavInv);
                    }
                    start.put(p.getUniqueId(), true);
                }else if(args[0].equalsIgnoreCase("stop") && sender instanceof Player) {
                    Player p = (Player) sender;
                    start.put(p.getUniqueId(), false);
                }
            }
        }
        return false;
    }

    @EventHandler
    public void clickListener(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(e.getInventory().equals(ScavInv)) {
            e.setCancelled(true);
            if(e.getSlot() == 15 || e.getSlot() == 22) {
                e.getWhoClicked().closeInventory();
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 0.3F);
                p.sendMessage(chat("&cScav Cancelled!"));
            } else if (e.getSlot() == 4) {
                ScavInv.setItem(13, newItem());
                if((ScavInv.getItem(13)).getType() == Material.AIR || (ScavInv.getItem(13)).getType() == null) {
                    ScavInv.setItem(13, newItem());
                }
            } else if (e.getSlot() == 11) {
                Item.put(p.getUniqueId(), e.getInventory().getItem(13));
                p.closeInventory();
                start.put(p.getUniqueId(), false);
                timer.put(p.getUniqueId(), System.currentTimeMillis());
                BukkitRunnable itemPickup = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(start.get(p.getUniqueId())) {
                            p.sendMessage(chat("&cScavanger Race Failed, Reason: NEW SCAV STARTED!"));
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 0.3F);
                            this.cancel();
                            return;
                        }
                        if(p.getInventory().contains(Item.get(p.getUniqueId()))) {
                            this.cancel();
                            p.sendMessage(chat("&aScavanger Complete! &2Time Elapsed: &a" + (double) ((System.currentTimeMillis() - timer.get(p.getUniqueId()))/1000D)));
                            sendClickableCommand(p, "&6&lClick Here! &eto play again!", "scav start", "&3Runs Command: &b/Scav Start");
                        } else {
                            try {
                                final String itemName = getItemName(Item.get(p.getUniqueId()));
                                String actionBar = "&3Current Item:&b " + (itemName) + " &6Timer: " + (double) (System.currentTimeMillis() - timer.get(p.getUniqueId())) / 1000D + "";
                                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(chat(actionBar)));
                            } catch (NoClassDefFoundError e){
                                e.printStackTrace();
                            }
                        }
                    }
                };
                itemPickup.runTaskTimer(plugin, 1, 1);
            }
        }
    }
}
