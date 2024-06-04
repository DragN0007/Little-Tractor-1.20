package com.dragn0007.littletractor;

import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dragn0007.littletractor.LittleTractorMain.mod;


public class Tractor extends Entity implements ContainerListener {

    private static final EntityDataAccessor<ResourceLocation> TEXTURE = (EntityDataAccessor<ResourceLocation>) SynchedEntityData.defineId(Tractor.class, LittleTractorMain.RESOURCE_SERIALIZER.get().getSerializer());
    private static final EntityDataAccessor<Boolean> TILLER_ON = SynchedEntityData.defineId(Tractor.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(Tractor.class, EntityDataSerializers.FLOAT);

    private static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(LittleTractorMain.MODID, "textures/entity/green.png");

    private static final Map<DyeItem, ResourceLocation> COLOR_MAP = new HashMap<>() {{
        put(DyeItem.byColor(DyeColor.BLACK), new ResourceLocation(LittleTractorMain.MODID, "textures/entity/black.png"));
        put(DyeItem.byColor(DyeColor.BLUE), new ResourceLocation(LittleTractorMain.MODID, "textures/entity/blue.png"));
        put(DyeItem.byColor(DyeColor.BROWN), new ResourceLocation(LittleTractorMain.MODID, "textures/entity/brown.png"));
        put(DyeItem.byColor(DyeColor.CYAN), new ResourceLocation(LittleTractorMain.MODID, "textures/entity/cyan.png"));
        put(DyeItem.byColor(DyeColor.GRAY), new ResourceLocation(LittleTractorMain.MODID, "textures/entity/dark_grey.png"));
        put(DyeItem.byColor(DyeColor.LIGHT_BLUE), new ResourceLocation(LittleTractorMain.MODID, "textures/entity/light_blue.png"));
        put(DyeItem.byColor(DyeColor.LIGHT_GRAY), new ResourceLocation(LittleTractorMain.MODID, "textures/entity/light_grey.png"));
        put(DyeItem.byColor(DyeColor.LIME), new ResourceLocation(LittleTractorMain.MODID, "textures/entity/lime_green.png"));
        put(DyeItem.byColor(DyeColor.MAGENTA), new ResourceLocation(LittleTractorMain.MODID, "textures/entity/magenta.png"));
        put(DyeItem.byColor(DyeColor.ORANGE), new ResourceLocation(LittleTractorMain.MODID, "textures/entity/orange.png"));
        put(DyeItem.byColor(DyeColor.PINK), new ResourceLocation(LittleTractorMain.MODID, "textures/entity/pink.png"));
        put(DyeItem.byColor(DyeColor.PURPLE), new ResourceLocation(LittleTractorMain.MODID, "textures/entity/purple.png"));
        put(DyeItem.byColor(DyeColor.RED), new ResourceLocation(LittleTractorMain.MODID, "textures/entity/red.png"));
        put(DyeItem.byColor(DyeColor.WHITE), new ResourceLocation(LittleTractorMain.MODID, "textures/entity/white.png"));
        put(DyeItem.byColor(DyeColor.GREEN), new ResourceLocation(LittleTractorMain.MODID, "textures/entity/green.png"));
        put(DyeItem.byColor(DyeColor.YELLOW), new ResourceLocation(LittleTractorMain.MODID, "textures/entity/yellow.png"));
    }};

    private static final float MAX_HEALTH = 20f;
    private static final float SPEED = 0.07f;
    private static final float TURN_SPEED = 1f;
    private static final float MAX_TURN = 5f;
    private static final float FRICTION = 0.7f;
    private static final float GRAVITY = 0.08f;


    private float targetRotation = 0;
    private float currentRotation = 0;

    private int tillerCooldown = 0;

    public int forwardMotion = 1;

    public int driveTick = 0;
    public float lastDrivePartialTick = 0;
    public Vec3 lastPos = Vec3.ZERO;

    public SimpleContainer inventory;
    private LazyOptional<?> itemHandler;

    private int lerpSteps;
    private double targetX;
    private double targetY;
    private double targetZ;
    private float targetYRot;

    public Tractor(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.inventory = new SimpleContainer(54);
        this.inventory.addListener(this);
        this.itemHandler = LazyOptional.of(() -> new InvWrapper(this.inventory));
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(this.isAlive() && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this.itemHandler != null) {
            return itemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        if(this.itemHandler != null) {
            LazyOptional<?> oldHandler = this.itemHandler;
            this.itemHandler = null;
            oldHandler.invalidate();
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    private Vec3 calcOffset(double x, double y, double z) {
        double rad = this.getYRot() * Math.PI / 180;

        double xOffset = this.position().x + (x * Math.cos(rad) - z * Math.sin(rad));
        double yOffset = this.position().y + y;
        double zOffset = this.position().z + (x * Math.sin(rad) + z * Math.cos(rad));

        return new Vec3(xOffset, yOffset, zOffset);
    }

    public void updateLastDrivePartialTick(float partialTick) {
        double xStep = this.position().x - this.lastPos.x;
        double zStep = this.position().z - this.lastPos.z;

        if(xStep * xStep + zStep * zStep != 0) {
            this.lastDrivePartialTick = partialTick;
        }
    }

    public void calcAnimStep() {
        double xStep = this.position().x - this.lastPos.x;
        double zStep = this.position().z - this.lastPos.z;
        float deg = (float) (Math.atan2(xStep, zStep) * 180 / Math.PI);

        if(xStep * xStep + zStep * zStep != 0) {
            this.driveTick = (this.driveTick + 1) % 30; // dragoon this 30 number is hardcoded I will come up with something better when I work on ME or something idk who cares it took like 2 seconds to figure out
            this.forwardMotion = (Math.round(mod(this.getYRot(), 360) + deg) == 180) ? -1 : 1;
        }
    }

    @Override
    public void positionRider(Entity entity) {
        if(this.hasPassenger(entity)) {
            entity.setPos(this.calcOffset(0, 0.8, -0.5));
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        if(!this.level().isClientSide && !this.isRemoved()) {
            this.markHurt();
            this.gameEvent(GameEvent.ENTITY_DAMAGED, damageSource.getEntity());
            float health = this.entityData.get(HEALTH) - damage;
            this.entityData.set(HEALTH, health);

            if(health < 0) {
                Containers.dropContents(this.level(), this, this.inventory);
                this.spawnAtLocation(LittleTractorMain.TRACTOR_SPAWN_EGG.get());
                this.kill();
            }
        }
        return true;
    }

    @Override
    public LivingEntity getControllingPassenger() {
        return (LivingEntity) this.getFirstPassenger();
    }

    @Override
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int lerpSteps, boolean p_19902_) {
        this.targetX = x;
        this.targetY = y;
        this.targetZ = z;
        this.targetYRot = yRot;

        this.lerpSteps = lerpSteps;
    }

    @Override
    public float getStepHeight() {
        return 1;
    }

    public void toggleTillerPower() {
        this.entityData.set(TILLER_ON, !this.entityData.get(TILLER_ON));
    }

    private void harvestCrop(BlockPos pos) {
        if(this.level().getBlockState(pos).getBlock() instanceof CropBlock cropBlock) {
            BlockState blockState = this.level().getBlockState(pos);

            if(cropBlock.isMaxAge(blockState)) {
                List<ItemStack> drops = Block.getDrops(blockState, (ServerLevel) this.level(), pos, null);
                drops.remove(new ItemStack(cropBlock.asItem()));
                drops.forEach(itemStack -> {
                    if(this.inventory.canAddItem(itemStack)) {
                        this.inventory.addItem(itemStack);
                    } else {
                        this.spawnAtLocation(itemStack);
                    }
                });

                this.level().setBlockAndUpdate(pos, blockState.setValue(cropBlock.getAgeProperty(), 0));
            }
        }
    }
    private static Property<Integer> getCropProperty(BlockState state, String propertyName) {
        Block block = state.getBlock();
        if (block instanceof CropBlock cropBlock) {
            for (Property<?> prop : state.getProperties()) {
                if (prop instanceof IntegerProperty && prop.getName().equals(propertyName)) {
                    return (Property<Integer>) prop;
                }
            }
        }
        return null;
    }
    private static int getMaxAge(Block block) {
        if (block instanceof CropBlock cropBlock) {
            return cropBlock.getMaxAge();
        }
        return 7;
    }

    public void till() {
        Vec3 left = this.calcOffset(-1, 0.2, -1.65);
        Vec3 mid = this.calcOffset(0, 0.2, -1.65);
        Vec3 right = this.calcOffset(1, 0.2, -1.65);

        BlockPos leftPos = new BlockPos((int) Math.floor(left.x), (int) Math.floor(left.y), (int) Math.floor(left.z));
        BlockPos midPos = new BlockPos((int) Math.floor(mid.x), (int) Math.floor(mid.y), (int) Math.floor(mid.z));
        BlockPos rightPos = new BlockPos((int) Math.floor(right.x), (int) Math.floor(right.y), (int) Math.floor(right.z));

        this.harvestCrop(leftPos);
        this.harvestCrop(midPos);
        this.harvestCrop(rightPos);
    }

    private void handleInput(Input input) {
        float forward = 0;
        float turn = 0;
        int turnMod = 1;

        if(input.up) {
            forward = SPEED;
        }

        if(input.down) {
            forward = -SPEED;
            turnMod = -1;
        }

        if(input.left) {
            turn = -TURN_SPEED * turnMod;
        }

        if(input.right) {
            turn = TURN_SPEED * turnMod;
        }

        this.tillerCooldown = Math.max(this.tillerCooldown - 1, 0);
        if(input.jumping && this.tillerCooldown == 0) {
            Network.INSTANCE.sendToServer(new Network.ToggleTillerPowerRequest(this.getId()));
            this.tillerCooldown = 10;
        }

        this.currentRotation = this.targetRotation;
        if(Math.abs(this.targetRotation + turn) <= MAX_TURN) {
            this.targetRotation += turn;
        }

        if(forward != 0 && turn == 0) {
            this.targetRotation = 0;
        }

        float deg = this.currentRotation + this.getYRot();
        float rad = deg * (float)Math.PI / 180;

        if(forward != 0 && deg != this.getYRot()) {
            this.setYRot(deg);
        }

        this.setDeltaMovement(this.getDeltaMovement().add(-Math.sin(rad) * forward, 0, Math.cos(rad) * forward));
    }

    @Override
    public void tick() {
        super.tick();
        this.lastPos = this.position();
        if(this.isControlledByLocalInstance()) {
            this.lerpSteps = 0;
            this.calcOffset(this.getX(), this.getY(), this.getZ()); //this could be wrong. changed setPacketCoordinates to calcOffset
            this.setDeltaMovement(this.getDeltaMovement().multiply(FRICTION, 1, FRICTION).add(0, this.onGround() ? 0 : -GRAVITY, 0));

            if(this.getDeltaMovement().length() < 0.01) {
                this.setDeltaMovement(Vec3.ZERO);
            }

            if(this.getControllingPassenger() instanceof LocalPlayer player) {
                this.handleInput(player.input);
            }
            this.move(MoverType.SELF, this.getDeltaMovement());
        } else {
            this.setDeltaMovement(Vec3.ZERO);
        }

        if(!this.level().isClientSide) {
            if(this.entityData.get(TILLER_ON)) {
                this.till();
            }

            if(this.isUnderWater()) {
                this.hurt(DamageTypes.DROWN, 1);
                this.ejectPassengers();
            }
        } else if(this.entityData.get(TILLER_ON)) {
            Vec3 pos = this.calcOffset(0.9, 3.7, 0.1);
            double yVel = this.random.nextDouble();
            if(yVel > 0.75) {
                this.level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.x, pos.y, pos.z, 0, yVel / 10, 0);
            }
        }

        if (this.lerpSteps > 0) {
            double x = this.getX() + (this.targetX - this.getX()) / this.lerpSteps;
            double y = this.getY() + (this.targetY - this.getY()) / this.lerpSteps;
            double z = this.getZ() + (this.targetZ - this.getZ()) / this.lerpSteps;

            float yRot = this.getYRot() + (this.targetYRot - this.getYRot()) / this.lerpSteps;

            this.setPos(x, y, z);
            this.setYRot(yRot);
            this.lerpSteps--;
        }

        this.calcAnimStep();
    }

    public float getFrontWheelRotation(float time) {
        return (this.currentRotation + (this.targetRotation - this.currentRotation) * time) * (float)Math.PI / 180;
    }

    @Override
    @NotNull
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(player.isShiftKeyDown()) {
            if(itemStack.getItem() instanceof DyeItem dyeItem) {
                this.level().playSound(player, this, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1f, 1f);

                if (!this.level().isClientSide) {
                    this.entityData.set(TEXTURE, COLOR_MAP.get(dyeItem));
                    itemStack.shrink(1);
                }

                return InteractionResult.sidedSuccess(this.level().isClientSide);
            } else {
                if(!this.level().isClientSide) {
                    NetworkHooks.openScreen((ServerPlayer) player, new SimpleMenuProvider((containerId, inventory, serverPlayer) -> {
                        return ChestMenu.sixRows(containerId, inventory, this.inventory);
                    }, this.getDisplayName()));
                }
            }
        } else if(!this.level().isClientSide){
            return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
        return super.interact(player, hand);
    }

    public ResourceLocation getTextureLocation() {
        return this.entityData.get(TEXTURE);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(TEXTURE, DEFAULT_TEXTURE);
        this.entityData.define(HEALTH, MAX_HEALTH);
        this.entityData.define(TILLER_ON, false);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        ResourceLocation texture = ResourceLocation.tryParse(compoundTag.getString("Texture"));
        this.entityData.set(TEXTURE, texture == null ? DEFAULT_TEXTURE : texture);
        this.entityData.set(HEALTH, compoundTag.getFloat("Health"));
        this.entityData.set(TILLER_ON, compoundTag.getBoolean("TillerOn"));

        ListTag listTag = compoundTag.getList("Items", 10);
        for(int i = 0; i < listTag.size(); i++) {
            CompoundTag tag = listTag.getCompound(i);
            int j = compoundTag.getByte("Slot") & 255;
            if(j < this.inventory.getContainerSize()) {
                this.inventory.setItem(j, ItemStack.of(tag));
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putString("Texture", this.entityData.get(TEXTURE).toString());
        compoundTag.putFloat("Health", this.entityData.get(HEALTH));
        compoundTag.putBoolean("TillerOn", this.entityData.get(TILLER_ON));

        ListTag listTag = new ListTag();
        for(int i = 0; i < this.inventory.getContainerSize(); i++) {
            ItemStack itemStack = this.inventory.getItem(i);
            if(!itemStack.isEmpty()) {
                CompoundTag tag = new CompoundTag();
                tag.putByte("Slot", (byte) i);
                itemStack.save(tag);
                listTag.add(tag);
            }
        }
        compoundTag.put("Items", listTag);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public void containerChanged(Container container) {

    }
}
