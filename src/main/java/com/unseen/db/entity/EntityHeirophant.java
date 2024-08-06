package com.unseen.db.entity;

import com.unseen.db.config.ModConfig;
import com.unseen.db.entity.action.*;
import com.unseen.db.entity.ai.EntityAiTimedAttack;
import com.unseen.db.init.ModSoundHandler;
import com.unseen.db.util.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EntityHeirophant extends EntityAbstractHierophant implements IAnimatable, IAttack {
    private Consumer<EntityLivingBase> prevAttack;
    private final String ANIM_MOVING_LEGS = "legs_walk";
    private final String ANIM_MOVING_ARMS = "arms_walk";
    private final String ANIM_IDLE = "idle";

    private final String ANIM_IDLE_ARMS = "arms_idle";

    private final String ANIM_IDLE_LEGS = "legs_idle";

    private final String ANIM_STRIKE = "melee_strike";

    private final String ANIM_BELL_RING = "bell_ringer";

    private final String ANIM_SUMMON_ORB = "summon_orb";

    private final String ANIM_SUMMON_THRALL = "summon_thrall";

    private final String ANIM_SPIKE_LINE = "spike_slash";
    private final String ANIM_SPIKE_AOE = "spike_ring";
    private final String ANIM_DEATH = "death";
    private final String ANIM_EXTRA_LEG = "extra_leg_twitch";
    private final String ANIM_EXTRA_ARM = "extra_arm_twitch";
    private AnimationFactory factory = new AnimationFactory(this);

    public int jailCellCooldownTimer = 0;
    public boolean hasDoneJailed = false;

    public EntityHeirophant(World worldIn) {
        super(worldIn);
    }

    public boolean currentlyDoingNonLOSAttack;
    public boolean isDoingNonLOSAttack;
    public int NonLOSAttackCooldown = 0;

    public int soundCooldown = 0;

    public boolean hasPlayedEntranceSound = false;

    public boolean stalkMode = true;
    //
    public int stalkModeTimer = 600;

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        animationData.addAnimationController(new AnimationController(this, "attack_handler", 0, this::predicateAttack));

    }
    public int prayedTooAmount = 0;
    public int totalAmount = 0;

    @Override
    public void onUpdate() {
        super.onUpdate();
        //Initial Sound Entrance
        List<EntityPlayer> entranceScream = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(40D), e -> !e.getIsInvulnerable());
        if(!entranceScream.isEmpty() && !hasPlayedEntranceSound) {
            for(EntityPlayer player : entranceScream) {
                player.playSound(ModSoundHandler.HIEROPHANT_SCREAM, 2.0f, 1.0f);
                hasPlayedEntranceSound = true;
            }
        }


        //WaterSpeed Modifier
        if(this.isInWater()) {
            this.motionY = -0.1;
            this.setAIMoveSpeed(2.5f);
        }
        //Full Body Usage 0 Movement
        if(this.isFullBodyUsage()) {
            this.motionX = 0;
            this.motionY = 0;
            this.motionZ = 0;
        }
        //Death Particle Handler
        if(this.isDeath()) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }

        //Jail Cell Nerf timer
       if(hasDoneJailed) {
           jailCellCooldownTimer++;
       }
       if(jailCellCooldownTimer > 300) {
           hasDoneJailed = false;
           jailCellCooldownTimer = 0;
       }
       // Non Line of Sight Timer
       if(currentlyDoingNonLOSAttack) {
           NonLOSAttackCooldown++;
       }
       if(NonLOSAttackCooldown > 1200) {
           currentlyDoingNonLOSAttack = false;
           NonLOSAttackCooldown = 0;
       }

        EntityLivingBase targetCurrent = this.getAttackTarget();
       if(targetCurrent == null && !currentlyDoingNonLOSAttack && !isDoingNonLOSAttack) {
           //Finds to see if any players are in it's radius
           List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(30D), e -> !e.getIsInvulnerable() && (!(e instanceof EntityClaw || e instanceof EntityHeirophant || e instanceof EntityJailCell)));
           for(EntityLivingBase targetFound : targets) {
               //Once the selection is done and one is found, every minute a blindness aura will be cast on there position
                this.performNonLOSAura(targetFound);
           }
       }

       if(targetCurrent != null) {
           double distSq = this.getDistanceSq(targetCurrent.posX, targetCurrent.getEntityBoundingBox().minY, targetCurrent.posZ);
           double distance = Math.sqrt(distSq);
           if(soundCooldown == 0 && distance > 3 && distance < 6) {
               this.playSound(ModSoundHandler.HIEROPHANT_SCREAM, 2.5f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
               soundCooldown = 400 + rand.nextInt(400);
           } else {
               soundCooldown--;
           }

           if(stalkModeTimer == 0 || this.hurtTime == 0 || distance > 4) {
               stalkMode = false;
           } else {
               stalkModeTimer--;
           }
       }

    }









    public void performNonLOSAura(EntityLivingBase target) {
        isDoingNonLOSAttack = true;
        currentlyDoingNonLOSAttack = true;
        target.playSound(ModSoundHandler.HIEROPHANT_BELL_RING, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
        addEvent(()-> new ActionSpawnBlindnessAura().performAction(this, target), 20);
        addEvent(()-> isDoingNonLOSAttack = false,52);
    }

    //Idle Handler and Moving
    private <E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isFullBodyUsage() && !this.isDeath()) {
            if (event.isMoving()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MOVING_LEGS, true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_LEGS, true));
            }

            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    //Arms handler for when not attacking
    private <E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {
        if(!this.isMeleeStrike() && !this.isFullBodyUsage() && !this.isBellRinge() && !this.isSummonOrd() && !this.isSummonThrall() && !this.isSpikeLIne() && !this.isDeath()) {
            if(event.isMoving()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MOVING_ARMS, true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_ARMS, true));
            }
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        //Basic Melee Strike
        if(this.isFightMode()) {
            if (this.isMeleeStrike()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STRIKE, false));
                return PlayState.CONTINUE;
            }
            // Bell Ring Attack
            if(this.isBellRinge() || this.isSummonJailCell()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BELL_RING, false));
                return PlayState.CONTINUE;
            }
            //Summon Glowing Orbs attack
            if(this.isSummonOrd()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON_ORB, false));
                return PlayState.CONTINUE;
            }
            // Summon Thrall attack
            if(this.isSummonThrall()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON_THRALL, false));
                return PlayState.CONTINUE;
            }
            //Spikes AOE
            if(this.isSpikesAOE()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SPIKE_AOE, false));
                return PlayState.CONTINUE;
            }
            //Spike Line
            if(this.isSpikeLIne()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SPIKE_LINE, false));
                return PlayState.CONTINUE;
            }
        }

        //Death Anim
        if(this.isDeath()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DEATH, false));
            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }


    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        double HealthChange = this.getHealth() / this.getMaxHealth();
        if(!this.isFightMode() && !isDoingNonLOSAttack && !this.isDeath() && !this.stalkMode) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(meleeAttack, ringBellOrSummonJailCell,summonJailCell, summonOrbsProjectile, summonThrall, spikeAOE,spikeLine));
            double[] weights = {
                    (distance < 6 && prevAttack != meleeAttack) ? 1 / distance : 0, //Basic Melee Attack
                    (distance > 5 && prevAttack != ringBellOrSummonJailCell) ? 1 / distance : 0, // Ring Bell
                    (distance < 12 && !hasDoneJailed && HealthChange < 0.75) ? 1 / distance : 0, // Summon Jail Cell
                    (distance > 6) ? distance * 0.02 : 0, // Summon Orbs Projectile
                    (distance > 6 && prevAttack != summonThrall) ? distance * 0.02 : 0, // Summon Thrall
                    (distance < 6 && prevAttack != spikeAOE) ? 1 / distance : 0, //Summon Spikes AOE
                    (distance > 4 && HealthChange < 0.75) ? 1/distance : 0 //Summon Spike Line

            };

            prevAttack = ModRandom.choice(attacks, rand, weights).next();

            prevAttack.accept(target);
        }
        return ModConfig.hierophant_base_attack_speed - (totalAmount * 10);
    }

    private final Consumer<EntityLivingBase> meleeAttack = (target) -> {
        this.setFightMode(true);
        this.setMeleeStrike(true);
        this.playSound(ModSoundHandler.HIEROPHANT_MELEE_STRIKE, 2.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));

            addEvent(() -> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2.0, 1.2, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage = this.getAttack();
                ModUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 1.0f, 0, false);
        }, 21);

        addEvent(()-> {
            this.setFightMode(false);
            this.setMeleeStrike(false);
        }, 54);
    };

    private final Consumer<EntityLivingBase> ringBellOrSummonJailCell = (target) -> {
        this.setFightMode(true);
        this.setBellRing(true);
        this.playSound(ModSoundHandler.HIEROPHANT_BELL_RING, 2.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));

        addEvent(()-> new ActionSpawnBlindnessAura().performAction(this, target), 20);

        addEvent(()-> {
        this.setFightMode(false);
        this.setBellRing(false);

        }, 52);
    };


    private final Consumer<EntityLivingBase> summonJailCell = (target) -> {
        this.setFightMode(true);
        this.setBellRing(true);
        hasDoneJailed = true;
        this.playSound(ModSoundHandler.HIEROPHANT_BELL_RING, 2.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
        addEvent(()-> new ActionJailCell().performAction(this, target), 20);

        addEvent(()-> {
            this.setFightMode(false);
            this.setBellRing(false);
        }, 52);
    };

    Supplier<Projectile> projectileSupplierOrb = () -> new ProjectileOrb(world, this,(float) (ModConfig.hierophant_attack_damage * ModConfig.projectile_modifier), null);
    private final Consumer<EntityLivingBase> summonOrbsProjectile = (target) -> {
        this.setFightMode(true);
        this.setSummonOrb(true);
        this.playSound(ModSoundHandler.HIEROPHANT_SUMMON_ORB, 2.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
        addEvent(()-> {
            new ActionSummonOrbs(projectileSupplierOrb, 0.55f).performAction(this, target);
        }, 40);
        addEvent(()-> {
            this.setSummonOrb(false);
            this.setFightMode(false);
        }, 110);
    };





    private final Consumer<EntityLivingBase> summonThrall = (target) -> {
        this.setFightMode(true);
        this.setSummonThrall(true);
        this.playSound(ModSoundHandler.HIEROPHANT_SUMMON_THRALL, 2.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
        addEvent(()-> {
        EntityThrall thrall = new EntityThrall(this.world);
        Vec3d spawnPos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2, 2, 0)));
        thrall.setPosition(spawnPos.x, spawnPos.y, spawnPos.z);
        this.world.spawnEntity(thrall);
        }, 25);
        addEvent(()-> {
        this.setFightMode(false);
        this.setSummonThrall(false);
        }, 76);
    };

    private final Consumer<EntityLivingBase> spikeAOE = (target)-> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setSpikesAoe(true);
        this.playSound(ModSoundHandler.HIEROPHANT_SPIKE_RING, 2.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
        //Action Summon Spikes AOE Variable
      addEvent(()-> new ActionHierophantSpikeAOE().performAction(this, target), 25);
      addEvent(()-> new ActionHierophantSecondSpikeAOE().performAction(this, target), 65);

      addEvent(()-> {
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        this.setSpikesAoe(false);
      }, 71);
    };

    private final Consumer<EntityLivingBase> spikeLine = (target) -> {
      this.setFightMode(true);
      this.setSpikeLine(true);
        this.playSound(ModSoundHandler.HIEROPHANT_SPIKE_LINE, 2.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
      addEvent(()-> new ActionHierophantSpikeLine().performAction(this, target), 20);
      addEvent(()-> {
        this.setFightMode(false);
        this.setSpikeLine(false);
      }, 55);
    };

    //Particle Handler for the boss
    @Override
    public void handleStatusUpdate(byte id) {
        //Death Particles
        if(id == ModUtils.PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, getPositionVector(), ModColors.DARK_GREY, new Vec3d(rand.nextFloat() * 0.1,0.4,rand.nextFloat() * 0.1));
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1,0,0))), ModColors.DARK_GREY, new Vec3d(rand.nextFloat() * 0.1,0.4,rand.nextFloat() * 0.1));
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0,0,1))), ModColors.DARK_GREY, new Vec3d(rand.nextFloat() * 0.1,0.4,rand.nextFloat() * 0.1));
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(-1,0,0))), ModColors.DARK_GREY, new Vec3d(rand.nextFloat() * 0.1,0.4,rand.nextFloat() * 0.1));
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0,0,-1))), ModColors.DARK_GREY, new Vec3d(rand.nextFloat() * 0.1,0.4,rand.nextFloat() * 0.1));
        }

            super.handleStatusUpdate(id);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAiTimedAttack<>(this, 1.0, ModConfig.hierophant_base_attack_speed - (totalAmount * 10), 16F, 0.1f));
    }

    @Override
    public void onDeath(DamageSource cause) {
        this.setHealth(0.0001f);
        this.setDeath(true);
        this.setImmovable(true);
        this.playSound(ModSoundHandler.HIEROPHANT_DEATH_SOUND, 2.5f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
        if(this.isDeath()) {
            addEvent(()-> this.setImmovable(false), 142);
            addEvent(()-> this.setDeath(false), 142);
            addEvent(this::setDead, 142);
            addEvent(()-> this.setDropItemsWhenDead(true), 142);

        }
        super.onDeath(cause);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }
    private static final ResourceLocation LOOT_BOSS = new ResourceLocation(Reference.MOD_ID, "hiero");
    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_BOSS;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }
    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundHandler.HIEROPHANT_IDLE_ONE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundHandler.HIEROPHANT_HURT_ONE;
    }

}
