package com.unseen.db.entity;

import com.unseen.db.entity.action.ActionPlayerSpikes;
import com.unseen.db.init.ModItems;
import com.unseen.db.util.ModColors;
import com.unseen.db.util.ModDamageSource;
import com.unseen.db.util.ModUtils;
import com.unseen.db.util.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ProjectileActionOrb extends Projectile{
    private final Vec3d startPos;
    private static final int PARTICLE_AMOUNT = 1;

    public ProjectileActionOrb(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);
        this.startPos = new Vec3d(this.posX, this.posY, this.posZ);
    }

    public ProjectileActionOrb(World worldIn) {
        super(worldIn);
        this.startPos = new Vec3d(this.posX, this.posY, this.posZ);
        this.setNoGravity(true);
    }


    @Override
    public Item getItemToRender() {
        return ModItems.ORB_PROJECTILE;
    }

    public ProjectileActionOrb(World world, double x, double y, double z) {
        super(world);
        this.startPos = new Vec3d(this.posX, this.posY, this.posZ);
        this.setNoGravity(true);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x = x / f;
        y = y / f;
        z = z / f;
        x = x + this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
        y = y + this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
        z = z + this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
        x = x * velocity;
        y = y * velocity;
        z = z * velocity;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        float f1 = MathHelper.sqrt(x * x + z * z);
        this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
        this.rotationPitch = (float) (MathHelper.atan2(y, f1) * (180D / Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        int ticksInGround = 0;
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
    }

    @Override
    protected void spawnParticles() {
        for (int i = 0; i < this.PARTICLE_AMOUNT; i++) {
            float size = 0.25f;
            ParticleManager.spawnColoredSmoke(world, getPositionVector(), ModColors.GREEN, new Vec3d(0, 0.1, 0));
        }
    }

    @Override
    public boolean isPushedByWater()
    {
        return false;
    }

    @Override
    protected void onHit(RayTraceResult result) {
        boolean isShootingEntity = result != null && result.entityHit != null && result.entityHit == this.shootingEntity;
        boolean isPartOfShootingEntity = result != null && result.entityHit != null && (result.entityHit instanceof MultiPartEntityPart && ((MultiPartEntityPart) result.entityHit).parent == this.shootingEntity);
        if (isShootingEntity || isPartOfShootingEntity) {
            return;
        }
        DamageSource source = ModDamageSource.builder()
                .indirectEntity(shootingEntity)
                .directEntity(this)
                .type(ModDamageSource.EXPLOSION)
                .build();
        ModUtils.handleAreaImpact(1, (e) -> this.getDamage(), this.shootingEntity, this.getPositionVector(), source, 0.2f, 0);
        Vec3d pos = this.getPositionVector();
        new ActionPlayerSpikes().performAction(this, pos);
        //Sounds
        super.onHit(result);

    }
}
