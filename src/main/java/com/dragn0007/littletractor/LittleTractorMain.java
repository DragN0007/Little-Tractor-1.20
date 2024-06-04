package com.dragn0007.littletractor;

import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
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

    public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, MODID);
    public static final RegistryObject<DataSerializerEntry> RESOURCE_SERIALIZER = SERIALIZERS.register("resource_serializer", () -> new DataSerializerEntry(new EntityDataSerializer<ResourceLocation>() {
        @Override
        public void write(FriendlyByteBuf buf, ResourceLocation resourceLocation) {
            buf.writeResourceLocation(resourceLocation);
        }

        @Override
        public ResourceLocation read(FriendlyByteBuf buf) {
            return buf.readResourceLocation();
        }

        @Override
        public ResourceLocation copy(ResourceLocation resourceLocation) {
            return resourceLocation;
        }
    }));

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
    public static final RegistryObject<EntityType<Tractor>> TRACTOR = ENTITY_TYPES.register("liltractor",
            () -> EntityType.Builder.of(Tractor::new, MobCategory.MISC).sized(2.8f, 2.8f).build(new ResourceLocation(MODID, "liltractor").toString()));

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> TRACTOR_SPAWN_EGG = ITEMS.register("liltractor", TractorItem::new);


    public LittleTractorMain()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::setup);

        SERIALIZERS.register(eventBus);
        ENTITY_TYPES.register(eventBus);
        ITEMS.register(eventBus);

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
