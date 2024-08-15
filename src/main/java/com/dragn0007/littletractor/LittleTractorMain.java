package com.dragn0007.littletractor;

import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import static com.dragn0007.littletractor.LittleTractorMain.MODID;

@Mod(MODID)
public class LittleTractorMain
{
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "liltractor";

    public static final EntityDataSerializer<Tractor.Mode> MODE = EntityDataSerializer.simpleEnum(Tractor.Mode.class);
    public static final EntityDataSerializer<ResourceLocation> RESOURCE_SERIALIZER = EntityDataSerializer.simple(FriendlyByteBuf::writeResourceLocation, FriendlyByteBuf::readResourceLocation);


    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
    public static final RegistryObject<EntityType<Tractor>> TRACTOR = ENTITY_TYPES.register("liltractor",
            () -> EntityType.Builder.of(Tractor::new, MobCategory.MISC).sized(2.8f, 2.8f).build(new ResourceLocation(MODID, "liltractor").toString()));

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> TRACTOR_SPAWN_EGG = ITEMS.register("liltractor", TractorItem::new);


    public LittleTractorMain() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);

        EntityDataSerializers.registerSerializer(RESOURCE_SERIALIZER);
        EntityDataSerializers.registerSerializer(MODE);
        ENTITY_TYPES.register(eventBus);
        ITEMS.register(eventBus);
        CreativeTabModifier.CREATIVE_MODE_TABS.register(eventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(FMLCommonSetupEvent event) {
        Network.init();
    }

    public static float mod(float n, float m) {
        while(n < 0) {
            n += m;
        }
        return n % m;
    }
}
