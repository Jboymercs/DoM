package com.unseen.db.util.handlers;

import com.unseen.db.init.ModBlocks;
import com.unseen.db.init.ModItems;
import com.unseen.db.items.render.RenderOrb;
import com.unseen.db.items.render.RenderStaff;
import com.unseen.db.util.IHasModel;
import com.unseen.db.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class RegistryHandler {
    private static IForgeRegistry<Item> itemRegistry;

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        itemRegistry = event.getRegistry();
        event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
    }


    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
    }


    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onModelRegister(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(ModItems.ORB_PROJECTILE, 0, new ModelResourceLocation(Reference.MOD_ID + ":orb","inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.HIEROPHANT_STAFF, 0, new ModelResourceLocation(Reference.MOD_ID + ":staff","inventory"));
        ModItems.ORB_PROJECTILE.setTileEntityItemStackRenderer(new RenderOrb());
        ModItems.HIEROPHANT_STAFF.setTileEntityItemStackRenderer(new RenderStaff());
        for (Item item : ModItems.ITEMS) {
            if (item instanceof IHasModel) {
                ((IHasModel) item).registerModels();
            }
        }
        for (Block block : ModBlocks.BLOCKS) {
            if (block instanceof IHasModel) {
                ((IHasModel) block).registerModels();
            }
        }

    }
}
