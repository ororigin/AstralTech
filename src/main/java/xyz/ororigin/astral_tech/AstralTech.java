package xyz.ororigin.astral_tech;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import xyz.ororigin.astral_tech.particle.bodyremakerscan.BodyRemakerScanParticle;
import xyz.ororigin.astral_tech.registry.AstralTechBlockEntities;
import xyz.ororigin.astral_tech.registry.AstralTechItems;
import xyz.ororigin.astral_tech.registry.AstralTechBlocks;
import xyz.ororigin.astral_tech.registry.AstralTechParticles;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AstralTech.MODID)
public class AstralTech
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "astral_tech";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // 模组实例
    public static AstralTech instance;

    // 使用无参构造函数 - 兼容 NeoForge
    public AstralTech()
    {
        instance = this;

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // 注册配置
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        modEventBus.addListener(this::commonSetup);

        // 注册事件总线
        MinecraftForge.EVENT_BUS.register(this);

        // 注册所有注册表
        AstralTechParticles.PARTICLE_TYPES.register(modEventBus);
        AstralTechBlocks.BLOCKS.register(modEventBus);
        AstralTechBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        AstralTechItems.ITEMS.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // 客户端事件处理器
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

    // 粒子工厂注册 - 修正内部类为静态
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientEventHandler {
        @SubscribeEvent
        public static void onParticleFactoryRegistration(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(AstralTechParticles.BODY_REMAKER_SCAN.get(),
                    BodyRemakerScanParticle.Provider::new);
        }
    }
}