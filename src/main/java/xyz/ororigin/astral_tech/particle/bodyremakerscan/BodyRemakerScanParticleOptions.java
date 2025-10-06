package xyz.ororigin.astral_tech.particle.bodyremakerscan;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.ororigin.astral_tech.registry.AstralTechParticles;

public class BodyRemakerScanParticleOptions implements ParticleOptions {

    // 添加 Codec 支持
    public static final Codec<BodyRemakerScanParticleOptions> CODEC =
            Codec.unit(BodyRemakerScanParticleOptions::new);

    public static final Deserializer<BodyRemakerScanParticleOptions> DESERIALIZER =
            new Deserializer<BodyRemakerScanParticleOptions>() {
                @Override
                public BodyRemakerScanParticleOptions fromCommand(ParticleType<BodyRemakerScanParticleOptions> type, StringReader reader) {
                    return new BodyRemakerScanParticleOptions();
                }

                @Override
                public BodyRemakerScanParticleOptions fromNetwork(ParticleType<BodyRemakerScanParticleOptions> type, FriendlyByteBuf buffer) {
                    return new BodyRemakerScanParticleOptions();
                }
            };

    @Override
    public ParticleType<?> getType() {
        return AstralTechParticles.BODY_REMAKER_SCAN.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        // 无数据需要传输
    }

    @Override
    public String writeToString() {
        return ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()).toString();
    }
}