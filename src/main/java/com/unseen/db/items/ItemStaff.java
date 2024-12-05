package com.unseen.db.items;

import com.unseen.db.Main;
import com.unseen.db.config.ModConfig;
import com.unseen.db.config.WorldConfig;
import com.unseen.db.entity.ProjectileActionOrb;
import com.unseen.db.init.ModSoundHandler;
import com.unseen.db.util.IHasModel;
import com.unseen.db.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

public class ItemStaff extends ItemBase implements IAnimatable, IHasModel {
    public AnimationFactory factory = new AnimationFactory(this);
    private final String ANIM_STAFF_CAST = "cast";
    private String controllerName = "attack_controller";

    private String info_loc;
    public ItemStaff(String name, CreativeTabs tab, String info_loc) {
        super(name, tab);
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.setMaxStackSize(1);
        this.setMaxDamage(ModConfig.staff_durability);
        this.info_loc = info_loc;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.AQUA + ModUtils.translateDesc(info_loc));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        int cooldown = ModConfig.staff_cooldown * 20;
        if(!worldIn.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            AnimationController<?> controller = GeckoLibUtil.getControllerForStack(this.factory, stack, controllerName);
            Vec3d playerLookVec = player.getLookVec();
            //1
            World world = player.getEntityWorld();
            Vec3d playerPos = new Vec3d(player.posX + playerLookVec.x * 1.4D,player.posY + playerLookVec.y + player.getEyeHeight(), player. posZ + playerLookVec.z * 1.4D);
            ProjectileActionOrb orb = new ProjectileActionOrb(world, player, 7f);
            ModUtils.setEntityPosition(orb, playerPos);
            world.spawnEntity(orb);
            if(player.isInWater()) {
                worldIn.playSound(null, player.posX, player.posY, player.posZ, ModSoundHandler.STAFF_CAST_WATER, SoundCategory.HOSTILE, 1.0F, 1.0F);
                orb.shoot(playerLookVec.x, playerLookVec.y, playerLookVec.z, 10f, 1.0f);
            } else {
                worldIn.playSound(null, player.posX, player.posY, player.posZ, ModSoundHandler.STAFF_CAST_LAND, SoundCategory.HOSTILE, 1.0F, 1.0F);
                orb.shoot(playerLookVec.x, playerLookVec.y, playerLookVec.z, 1.5f, 1.0f);
            }


            if(controller.getAnimationState() == AnimationState.Stopped) {
                controller.markNeedsReload();
                controller.setAnimation(new AnimationBuilder().addAnimation(ANIM_STAFF_CAST, false));
            }
            stack.damageItem(1, player);
            player.getCooldownTracker().setCooldown(this, cooldown);

        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        AnimationController<ItemStaff> controller = new AnimationController<ItemStaff>(this,
                controllerName, 5, this::predicateAttack);

        animationData.addAnimationController(controller);
    }

    private <E extends IAnimatable>PlayState predicateAttack(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void registerModels() {
        {
            Main.proxy.registerItemRenderer(this, 0, "inventory");
        }}
}
