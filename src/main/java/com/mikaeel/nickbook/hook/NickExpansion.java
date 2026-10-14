package com.mikaeel.nickbook.hook;

import com.mikaeel.nickbook.NickBookPlugin;
import com.mikaeel.nickbook.service.NickService;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Placeholders for TAB, LPC (LuckPermsChat) and anything else using PlaceholderAPI:
 *  %nickbook_name%        nick name when nicked, real name otherwise
 *  %nickbook_prefix%      nick rank prefix when nicked, LuckPerms prefix otherwise
 *  %nickbook_suffix%      empty when nicked, LuckPerms suffix otherwise
 *  %nickbook_displayname% prefix + name + suffix
 *  %nickbook_realname%    always the real username
 *  %nickbook_is_nicked%   true / false
 */
public final class NickExpansion extends PlaceholderExpansion {
    private final NickBookPlugin plugin;

    public NickExpansion(NickBookPlugin plugin) { this.plugin = plugin; }

    @Override public String getIdentifier() { return "nickbook"; }
    @Override public String getAuthor() { return "Mikaeel"; }
    @Override public String getVersion() { return plugin.getDescription().getVersion(); }
    @Override public boolean persist() { return true; }
    @Override public boolean canRegister() { return true; }

    @Override
    public String onRequest(OfflinePlayer offline, String params) {
        if (offline == null || !offline.isOnline()) return "";
        Player player = offline.getPlayer();
        NickService s = plugin.getNickService();
        switch (params.toLowerCase()) {
            case "name": return s.shownName(player);
            case "prefix": return s.shownPrefix(player);
            case "suffix": return s.shownSuffix(player);
            case "displayname": return s.shownPrefix(player) + s.shownName(player) + s.shownSuffix(player);
            case "realname": return player.getName();
            case "is_nicked": return String.valueOf(s.isNicked(player));
            default: return null;
        }
    }
}
