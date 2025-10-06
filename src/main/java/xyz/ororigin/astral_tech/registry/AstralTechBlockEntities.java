package xyz.ororigin.astral_tech.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.ororigin.astral_tech.AstralTech;
import xyz.ororigin.astral_tech.blockentity.BodyRemakerBlockEntity;

public class AstralTechBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, AstralTech.MODID);

    public static final RegistryObject<BlockEntityType<BodyRemakerBlockEntity>> BODY_REMAKER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("body_remaker_block_entity",
                    () -> BlockEntityType.Builder.of(BodyRemakerBlockEntity::new,
                            AstralTechBlocks.body_remaker.get()).build(null));
}
