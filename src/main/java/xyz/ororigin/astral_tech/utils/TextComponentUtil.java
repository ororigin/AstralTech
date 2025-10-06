package xyz.ororigin.astral_tech.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import java.util.ArrayList;
import java.util.List;

/**
 * 文本组件工具类 - 用于便捷地为文本添加自定义RGB颜色和样式
 * 支持链式样式应用和智能参数处理
 */
public class TextComponentUtil {

    private TextComponentUtil() {
        // 工具类，防止实例化
    }

    /**
     * 为文本组件应用RGB颜色
     *
     * @param component 要着色的组件
     * @param color     RGB颜色值
     * @return 着色后的组件
     */
    public static MutableComponent color(MutableComponent component, int color) {
        return component.setStyle(component.getStyle().withColor(TextColor.fromRgb(color)));
    }

    /**
     * 构建格式化文本组件，支持智能样式组合
     *
     * @param components 参数组件（支持多种类型）
     * @return 格式化后的文本组件
     */
    public static MutableComponent build(Object... components) {
        MutableComponent result = null;
        Style cachedStyle = Style.EMPTY;

        for (Object component : components) {
            if (component == null) {
                continue;
            }

            MutableComponent current = null;

            // 处理样式对象
            if (component instanceof TextColor color) {
                cachedStyle = cachedStyle.withColor(color);
                continue;
            } else if (component instanceof ChatFormatting formatting) {
                cachedStyle = cachedStyle.applyFormat(formatting);
                continue;
            } else if (component instanceof ClickEvent event) {
                cachedStyle = cachedStyle.withClickEvent(event);
                continue;
            } else if (component instanceof HoverEvent event) {
                cachedStyle = cachedStyle.withHoverEvent(event);
                continue;
            }
            // 处理文本内容对象
            else if (component instanceof Component c) {
                current = c.copy();
            } else if (component instanceof ItemStack stack) {
                current = stack.getHoverName().copy();
            } else if (component instanceof Block block) {
                current = Component.translatable(block.getDescriptionId());
            } else if (component instanceof Item item) {
                current = Component.translatable(item.getDescriptionId());
            } else if (component instanceof FluidStack stack) {
                current = stack.getDisplayName().copy();
            } else if (component instanceof Fluid fluid) {
                current = Component.translatable(fluid.getFluidType().getDescriptionId());
            } else {
                // 处理字符串、数字等基础类型
                current = Component.literal(cleanString(component.toString()));
            }

            if (current == null) {
                continue;
            }

            // 应用缓存的样式
            if (!cachedStyle.isEmpty()) {
                current.setStyle(cachedStyle);
                cachedStyle = Style.EMPTY;
            }

            // 构建结果
            if (result == null) {
                result = current;
            } else {
                result.append(current);
            }
        }

        return result != null ? result : Component.empty();
    }

    /**
     * 智能翻译方法，支持样式参数
     *
     * @param translationKey 翻译键
     * @param components     参数组件
     * @return 翻译后的文本组件
     */
    public static MutableComponent smartTranslate(String translationKey, Object... components) {
        if (components.length == 0) {
            return Component.translatable(translationKey);
        }

        List<Object> args = new ArrayList<>();
        Style cachedStyle = Style.EMPTY;

        for (Object component : components) {
            if (component == null) {
                args.add(null);
                cachedStyle = Style.EMPTY;
                continue;
            }

            MutableComponent current = null;

            // 处理样式对象（智能应用）
            if (component instanceof TextColor color && cachedStyle.getColor() == null) {
                cachedStyle = cachedStyle.withColor(color);
                continue;
            } else if (component instanceof ChatFormatting formatting && !hasStyleType(cachedStyle, formatting)) {
                cachedStyle = cachedStyle.applyFormat(formatting);
                continue;
            } else if (component instanceof ClickEvent event && cachedStyle.getClickEvent() == null) {
                cachedStyle = cachedStyle.withClickEvent(event);
                continue;
            } else if (component instanceof HoverEvent event && cachedStyle.getHoverEvent() == null) {
                cachedStyle = cachedStyle.withHoverEvent(event);
                continue;
            }
            // 处理内容对象
            else if (component instanceof Component c) {
                current = c.copy();
            } else if (component instanceof ItemStack stack) {
                current = stack.getHoverName().copy();
            } else if (component instanceof Block block) {
                current = Component.translatable(block.getDescriptionId());
            } else if (component instanceof Item item) {
                current = Component.translatable(item.getDescriptionId());
            } else if (component instanceof String) {
                component = cleanString((String) component);
            }

            // 应用样式或添加参数
            if (!cachedStyle.isEmpty()) {
                if (current != null) {
                    args.add(current.setStyle(cachedStyle));
                } else {
                    args.add(component);
                }
                cachedStyle = Style.EMPTY;
            } else if (current != null) {
                args.add(current);
            } else {
                args.add(component);
            }
        }

        // 处理尾随样式
        if (!cachedStyle.isEmpty()) {
            args.add(components[components.length - 1]);
        }

        return Component.translatable(translationKey, args.toArray());
    }

    /**
     * 创建RGB颜色文本组件
     *
     * @param text  文本内容
     * @param color RGB颜色值
     * @return 着色后的文本组件
     */
    public static MutableComponent coloredText(String text, int color) {
        return Component.literal(cleanString(text)).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(color)));
    }

    /**
     * 创建格式化文本组件
     *
     * @param text       文本内容
     * @param formatting 文本格式
     * @return 格式化后的文本组件
     */
    public static MutableComponent formattedText(String text, ChatFormatting formatting) {
        return Component.literal(cleanString(text)).withStyle(formatting);
    }

    /**
     * 清理字符串中的特殊空格字符
     */
    private static String cleanString(String text) {
        return text.replace("\u00A0", " ")  // 非断行空格
                .replace("\u202f", " ");  // 窄非断行空格
    }

    /**
     * 检查样式是否已包含特定格式
     */
    private static boolean hasStyleType(Style current, ChatFormatting formatting) {
        return switch (formatting) {
            case OBFUSCATED -> current.isObfuscated();
            case BOLD -> current.isBold();
            case STRIKETHROUGH -> current.isStrikethrough();
            case UNDERLINE -> current.isUnderlined();
            case ITALIC -> current.isItalic();
            case RESET -> current.isEmpty();
            default -> current.getColor() != null;
        };
    }

    /**
     * RGB颜色工具方法
     */
    public static class Colors {
        public static final int RED = 0xFF0000;
        public static final int GREEN = 0x00FF00;
        public static final int BLUE = 0x0000FF;
        public static final int YELLOW = 0xFFFF00;
        public static final int CYAN = 0x00FFFF;
        public static final int MAGENTA = 0xFF00FF;
        public static final int WHITE = 0xFFFFFF;
        public static final int BLACK = 0x000000;
        public static final int ORANGE = 0xFFA500;
        public static final int PURPLE = 0x800080;

        /**
         * 从RGB值创建颜色
         */
        public static TextColor rgb(int r, int g, int b) {
            return TextColor.fromRgb((r << 16) | (g << 8) | b);
        }

        /**
         * 从十六进制值创建颜色
         */
        public static TextColor hex(int hexColor) {
            return TextColor.fromRgb(hexColor);
        }
    }
}