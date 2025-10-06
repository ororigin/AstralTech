package xyz.ororigin.astral_tech.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import xyz.ororigin.astral_tech.AstralTech;
import xyz.ororigin.astral_tech.blockentity.BodyRemakerBlockEntity;

import javax.annotation.Nonnull;

public class AstralTechBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AstralTech.MODID);

    private static final VoxelShape body_remaker_shape = Block.box(0, 0, 0, 16, 1, 16);

    public static final RegistryObject<Block> body_remaker = BLOCKS.register("body_remaker",
            () -> new BaseEntityBlock(BlockBehaviour.Properties.of().strength(30F).lightLevel(state -> 15)) {

                @Override
                public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result){
                    if (level.isClientSide) {
                        return InteractionResult.SUCCESS;
                    }
                    ServerPlayer serverPlayer = (ServerPlayer) player;
                    serverPlayer.setRespawnPosition(level.dimension(), pos, 0.0F, true, false);
                    player.sendSystemMessage(Component.literal("重生点已在此处设置！"));
                    return InteractionResult.PASS;
                }

                @Override
                public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
                    if (!level.isClientSide && !state.is(newState.getBlock())) {
                        for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
                            BlockPos respawnPos = player.getRespawnPosition();
                            if (respawnPos != null && respawnPos.equals(pos)) {
                                player.setRespawnPosition(null, null, 0.0F, false, false);
                            }
                        }
                    }
                    super.onRemove(state, level, pos, newState, isMoving);
                }

                @Override
                public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
                    return body_remaker_shape;
                }

                @Nonnull
                @Override
                public RenderShape getRenderShape(@Nonnull BlockState state) {
                    return RenderShape.MODEL;
                }

                @Override
                public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
                    return body_remaker_shape;
                }

                @Override
                public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
                    return new BodyRemakerBlockEntity(pos, state);
                }

                @Override
                public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
                    return createTickerHelper(type, AstralTechBlockEntities.BODY_REMAKER_BLOCK_ENTITY.get(),
                            level.isClientSide() ? BodyRemakerBlockEntity::clientTick : null);
                }
            });

    // 辅助方法创建Ticker
    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createTickerHelper(BlockEntityType<T> type, BlockEntityType<? extends T> targetType, BlockEntityTicker<? super T> ticker) {
        return targetType == type ? (BlockEntityTicker<T>) ticker : null;
    }
}