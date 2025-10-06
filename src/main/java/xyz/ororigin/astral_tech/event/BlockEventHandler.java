package xyz.ororigin.astral_tech.event;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xyz.ororigin.astral_tech.AstralTech;
import xyz.ororigin.astral_tech.registry.AstralTechBlocks;

@Mod.EventBusSubscriber(modid = AstralTech.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BlockEventHandler {

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        // 确保在服务端执行逻辑，避免客户端服务端不一致
        if (event.getLevel().isClientSide()) {
            return;
        }

        // 获取新放置方块的位置和世界
        BlockPos placedPos = event.getPos();
        LevelAccessor level = event.getLevel();
        BlockPos checkPos = placedPos.offset(0, -1, 0);
        BlockState adjacentState = level.getBlockState(checkPos);
        if (adjacentState.getBlock() == AstralTechBlocks.body_remaker.get()) {
            if (event.getEntity() instanceof ServerPlayer player) {
                event.setCanceled(true);
                int selectedSlot = player.getInventory().selected;
                player.connection.send(new ClientboundContainerSetSlotPacket(
                        -2, 0, selectedSlot, player.getInventory().getItem(selectedSlot)
                ));
            }
        }
    }
}