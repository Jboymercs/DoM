package com.unseen.db;

import com.unseen.db.init.ModEntities;
import com.unseen.db.init.ModSoundHandler;
import com.unseen.db.proxy.CommonProxy;
import com.unseen.db.util.Reference;
import com.unseen.db.util.logger.DoMLogger;
import com.unseen.db.world.GenerateStructures;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)
public class Main
        /**
         * I'd like to give a huge thank you to Barribob, for the spectacular work done on Maelstrom and this project
         * exsisting cause of it. Credit to a lot of the code modified from Maelstrom source. Yes, I am revamping that mod but
         * Barribob still deserves the credit.
         *
         * Credits also go to - UnOriginal, Fake Drayn, and Foreck, you as a crew have helped push our projects to beyond and as
         * a team I am so eternally grateful to design this with you all
         *
         *
         * Lastly, this mod is for the ModJam 2023 Fall for the 1.12.2 Modded Coalition Discord Server
         * link - https://discord.gg/Hmvek4Axrv
         */
{

    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.COMMON_PROXY)
    public static CommonProxy proxy;
    public static SimpleNetworkWrapper network;

    @Mod.Instance
    public static Main instance;

    private static Logger logger;

    public Main() {
        DoMLogger.clearLog();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        GeckoLib.initialize();
        ModEntities.registerEntities();
        GameRegistry.registerWorldGenerator(new GenerateStructures(), 3);
        proxy.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        ModSoundHandler.registerSounds();
    }
}
