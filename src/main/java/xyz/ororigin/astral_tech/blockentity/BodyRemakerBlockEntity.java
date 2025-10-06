package xyz.ororigin.astral_tech.blockentity;


import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import xyz.ororigin.astral_tech.particle.bodyremakerscan.BodyRemakerScanParticleOptions;
import xyz.ororigin.astral_tech.registry.AstralTechBlockEntities;
import xyz.ororigin.astral_tech.registry.AstralTechParticles;

public class BodyRemakerBlockEntity extends BlockEntity {
    private int particleTickCounter = 0;

    public BodyRemakerBlockEntity(BlockPos pos, BlockState state) {
        super(AstralTechBlockEntities.BODY_REMAKER_BLOCK_ENTITY.get(), pos, state);
    }

    // 客户端每tick调用
    public static void clientTick(Level level, BlockPos pos, BlockState state, BodyRemakerBlockEntity blockEntity) {
        blockEntity.particleTickCounter++;

        if (blockEntity.particleTickCounter >= 10) {
            blockEntity.particleTickCounter = 0;
            spawnScanParticles(level, pos);
        }
    }

    private static void spawnScanParticles(Level level, BlockPos pos) {
        if (!level.isClientSide()) return;

        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 1.0;
        double centerZ = pos.getZ() + 0.5;


        BodyRemakerScanParticleOptions particleOptions = new BodyRemakerScanParticleOptions();

        level.addParticle(particleOptions, centerX, centerY, centerZ, 0, 0, 0);

        if (level.random.nextFloat() < 0.3f) {
            for (int i = 0; i < 2; i++) {
                double offsetX = (level.random.nextDouble() - 0.5) * 0.8;
                double offsetZ = (level.random.nextDouble() - 0.5) * 0.8;
                level.addParticle(ParticleTypes.END_ROD,
                        centerX + offsetX, centerY, centerZ + offsetZ,
                        0, -0.05, 0);
            }
        }
    }
}
