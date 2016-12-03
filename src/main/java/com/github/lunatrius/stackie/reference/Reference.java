package com.github.lunatrius.stackie.reference;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Reference {
    public static final String MODID = "stackie";
    public static final String NAME = "Stackie";
    public static final String VERSION = "${version}";
    public static final String FORGE = "${forgeversion}";
    public static final String MINECRAFT = "${mcversion}";
    public static final String PROXY_SERVER = "com.github.lunatrius.stackie.proxy.ServerProxy";
    public static final String PROXY_CLIENT = "com.github.lunatrius.stackie.proxy.ClientProxy";
    public static final String GUI_FACTORY = "com.github.lunatrius.stackie.client.gui.GuiFactory";

    public static Logger logger = LogManager.getLogger(Reference.MODID);
}
