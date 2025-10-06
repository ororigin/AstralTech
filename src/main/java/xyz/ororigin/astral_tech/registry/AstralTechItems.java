package xyz.ororigin.astral_tech.registry;


import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.minecraft.network.chat.Component;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.openjdk.nashorn.internal.ir.ReturnNode;
import xyz.ororigin.astral_tech.AstralTech;
import xyz.ororigin.astral_tech.utils.TextComponentUtil;
import xyz.ororigin.astral_tech.registry.AstralTechBlocks;

import java.awt.*;

public class AstralTechItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,AstralTech.MODID);

    public static final RegistryObject<Item> stellar_energy_irradiated_ingot_mk1=ITEMS.register("stellar_energy_irradiated_ingot_mk1",() -> new Item(new Item.Properties()){
        @NotNull
        @Override
        public Component getName(@NotNull ItemStack stack) {
            return TextComponentUtil.build(
                    TextComponentUtil.Colors.rgb(176, 146, 212),
                    super.getName(stack)
            );
        }
    });
    public static final RegistryObject<Item> body_remaker_item=ITEMS.register("body_remaker",() -> new BlockItem(AstralTechBlocks.body_remaker.get(),new Item.Properties()){
        @NotNull
        @Override
        public Component getName(@NotNull ItemStack stack) {
            return TextComponentUtil.build(
                    TextComponentUtil.Colors.rgb(77, 230, 206),
                    super.getName(stack)
            );
        }});
}
