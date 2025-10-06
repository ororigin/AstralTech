package xyz.ororigin.astral_tech.particle.bodyremakerscan;


import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.world.phys.Vec3;

public class BodyRemakerScanParticle extends TextureSheetParticle {


    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
        Vec3 cameraPos = camera.getPosition();
        float x = (float)(this.x - cameraPos.x());
        float y = (float)(this.y - cameraPos.y());
        float z = (float)(this.z - cameraPos.z());

        // 固定水平方向的四个顶点
        float size = this.getQuadSize(partialTicks);

        // 获取UV坐标
        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();

        int light = this.getLightColor(partialTicks);

        // 创建水平四边形（平行于XZ平面）
        vertexConsumer.vertex(x - size, y, z - size)
                .uv(u1, v1)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2(light)
                .endVertex();

        vertexConsumer.vertex(x - size, y, z + size)
                .uv(u1, v0)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2(light)
                .endVertex();

        vertexConsumer.vertex(x + size, y, z + size)
                .uv(u0, v0)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2(light)
                .endVertex();

        vertexConsumer.vertex(x + size, y, z - size)
                .uv(u0, v1)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2(light)
                .endVertex();
    }



    private final float initialScale;
    private final float scanSpeed;
    private final float startY;
    private final float endY;

    public BodyRemakerScanParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        // 调整粒子属性以适应持续生成
        this.lifetime = 20; // 稍微缩短生命周期
        this.quadSize = 0.6f; // 稍微减小初始大小
        this.initialScale = 0.6f;

        // 扫描效果参数
        this.startY = (float) y + 2f; // 降低起始高度
        this.endY = (float) y;
        this.scanSpeed = (startY - endY) / lifetime;

        // 设置初始位置
        this.y = startY;

        // 颜色 - 可以稍微调整以更好地适应持续效果
        this.rCol = 0.2f;
        this.gCol = 0.6f;
        this.bCol = 1.0f;
        this.alpha = 0.6f; // 降低透明度避免过于密集

    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        // 向下扫描移动
        this.y = startY - (scanSpeed * age);


    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<BodyRemakerScanParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        @Override
        public Particle createParticle(BodyRemakerScanParticleOptions type, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            BodyRemakerScanParticle particle = new BodyRemakerScanParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(this.sprites);
            return particle;
        }
    }
}