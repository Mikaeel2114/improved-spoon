package com.mikaeel.nickbook.service;

import com.mikaeel.nickbook.NickBookPlugin;
import com.mikaeel.nickbook.model.NickProfile;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class NickService {
    private final NickBookPlugin plugin;
    public NickService(NickBookPlugin plugin) { this.plugin = plugin; }
    public NickProfile get(Player player) {
        String path = "players." + player.getUniqueId(); FileConfiguration c = plugin.getConfig();
        return new NickProfile(c.getString(path + ".rank", "DEFAULT"), c.getString(path + ".skin", "normal"), c.getString(path + ".name", player.getName()));
    }
    public void setRank(Player p, String rank) { if (plugin.getConfig().isString("ranks." + rank)) { plugin.getConfig().set("players." + p.getUniqueId() + ".rank", rank); plugin.saveConfig(); if (isNicked(p)) applySilently(p); } }
    public void setSkin(Player p, String skin) { plugin.getConfig().set("players." + p.getUniqueId() + ".skin", skin); plugin.saveConfig(); p.sendMessage(color("&aSkin choice saved: &f" + skin)); }
    public void setRandomName(Player p) {
        List<String> names = new ArrayList<String>(plugin.getConfig().getStringList("random-names")); if (names.isEmpty()) names.add("PixelPilot");
        String old = get(p).name(); if (names.size() > 1) names.removeIf(n -> n.equalsIgnoreCase(old));
        plugin.getConfig().set("players." + p.getUniqueId() + ".name", names.get(ThreadLocalRandom.current().nextInt(names.size()))); saveAndApply(p);
    }
    public String randomName(Player p) {
        List<String> names = new ArrayList<String>(plugin.getConfig().getStringList("random-names"));
        if (names.isEmpty()) names.add("PixelPilot");
        String old = get(p).name();
        if (names.size() > 1) names.removeIf(n -> n.equalsIgnoreCase(old));
        String value = names.get(ThreadLocalRandom.current().nextInt(names.size()));
        plugin.getConfig().set("players." + p.getUniqueId() + ".candidate", value);
        plugin.saveConfig();
        return value;
    }
    public String candidate(Player p) {
        String value = plugin.getConfig().getString("players." + p.getUniqueId() + ".candidate");
        return value != null ? value : randomName(p);
    }
    public void useCandidate(Player p) {
        plugin.getConfig().set("players." + p.getUniqueId() + ".name", candidate(p));
        saveAndApply(p);
    }
    public void reset(Player p) { plugin.getConfig().set("players." + p.getUniqueId() + ".active", false); plugin.saveConfig(); p.setDisplayName(p.getName()); p.setPlayerListName(null); p.sendMessage(color("&aNickname reset.")); }
    public List<String> ranks() { return new ArrayList<String>(plugin.getConfig().getConfigurationSection("ranks").getKeys(false)); }
    public void saveAndApply(Player p) {
        applySilently(p);
        p.sendMessage(color("&aNickname applied: &f" + get(p).name()));
    }
    /** Marks the player nicked and updates Bukkit names (TAB/LPC read the placeholders). */
    public void applySilently(Player p) {
        plugin.getConfig().set("players." + p.getUniqueId() + ".active", true);
        plugin.saveConfig();
        String shown = formattedName(p);
        p.setDisplayName(shown);
        String list = shown.length() > 16 ? shown.substring(0, 16) : shown; // 1.8 tab limit
        if (list.endsWith("\u00a7")) list = list.substring(0, list.length() - 1);
        p.setPlayerListName(list);
    }
    public String formattedName(Player p) { return shownPrefix(p) + shownName(p); }
    public String shownName(Player p) { return isNicked(p) ? get(p).name() : p.getName(); }
    public String shownPrefix(Player p) {
        if (isNicked(p)) return color(plugin.getConfig().getString("ranks." + get(p).rank(), "&7"));
        return color(com.mikaeel.nickbook.hook.LuckPermsHook.prefix(p));
    }
    public String shownSuffix(Player p) {
        if (isNicked(p)) return "";
        return color(com.mikaeel.nickbook.hook.LuckPermsHook.suffix(p));
    }
    public boolean isNicked(Player p) {
        return plugin.getConfig().getBoolean("players." + p.getUniqueId() + ".active", false);
    }
    public String color(String text) { return ChatColor.translateAlternateColorCodes('&', text); }
}
