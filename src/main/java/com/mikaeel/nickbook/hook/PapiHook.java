package com.mikaeel.nickbook.hook;

import com.mikaeel.nickbook.NickBookPlugin;

/** Separate class so PlaceholderAPI classes are only loaded when PAPI is installed. */
public final class PapiHook {
    private PapiHook() {}
    public static boolean register(NickBookPlugin plugin) {
        return new NickExpansion(plugin).register();
    }
}
