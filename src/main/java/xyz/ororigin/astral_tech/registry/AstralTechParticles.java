package xyz.ororigin.astral_tech.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.ororigin.astral_tech.AstralTech;
import xyz.ororigin.astral_tech.particle.bodyremakerscan.BodyRemakerScanParticleOptions;

public class AstralTechParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, AstralTech.MODID);

    public static final RegistryObject<ParticleType<BodyRemakerScanParticleOptions>> BODY_REMAKER_SCAN =
            PARTICLE_TYPES.register("body_remaker_scan",
                    () -> new ParticleType<BodyRemakerScanParticleOptions>(false, BodyRemakerScanParticleOptions.DESERIALIZER) {
                        @Override
                        public Codec<BodyRemakerScanParticleOptions> codec() {
                            return Codec.unit(BodyRemakerScanParticleOptions::new);
                        }
                    });

    public static BodyRemakerScanParticleOptions getBodyRemakerScanParticle() {
        return new BodyRemakerScanParticleOptions();
    }
}