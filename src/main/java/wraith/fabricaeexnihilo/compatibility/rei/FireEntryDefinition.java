package wraith.fabricaeexnihilo.compatibility.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import me.shedaniel.rei.api.common.entry.EntrySerializer;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.comparison.ComparisonContext;
import me.shedaniel.rei.api.common.entry.type.EntryDefinition;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.FabricaeExNihilo;

import java.util.stream.Stream;

public class FireEntryDefinition implements EntryDefinition<FireEntryDefinition.FireType> {
    public static final EntryType<FireType> TYPE = EntryType.deferred(FabricaeExNihilo.id("fire"));

    @Override
    public Class<FireType> getValueType() {
        return FireType.class;
    }

    @Override
    public EntryType<FireType> getType() {
        return TYPE;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public EntryRenderer<FireType> getRenderer() {
        return Renderer.INSTANCE;
    }

    @Override
    public @Nullable Identifier getIdentifier(EntryStack<FireType> entry, FireType value) {
        return value.getId();
    }

    @Override
    public boolean isEmpty(EntryStack<FireType> entry, FireType value) {
        return false;
    }

    @Override
    public FireType copy(EntryStack<FireType> entry, FireType value) {
        return value;
    }

    @Override
    public FireType normalize(EntryStack<FireType> entry, FireType value) {
        return value;
    }

    @Override
    public FireType wildcard(EntryStack<FireType> entry, FireType value) {
        return value;
    }

    @Override
    public long hash(EntryStack<FireType> entry, FireType value, ComparisonContext context) {
        return value.hashCode();
    }

    @Override
    public boolean equals(FireType o1, FireType o2, ComparisonContext context) {
        return o1 == o2;
    }

    @Override
    public @Nullable EntrySerializer<FireType> getSerializer() {
        return null;
    }

    @Override
    public Text asFormattedText(EntryStack<FireType> entry, FireType value) {
        return value.getName();
    }

    @Override
    public Stream<? extends TagKey<?>> getTagsFor(EntryStack<FireType> entry, FireType value) {
        return Stream.empty();
    }

    public enum FireType {
        NORMAL(Blocks.FIRE),
        SOUL(Blocks.SOUL_FIRE);

        public final Block block;

        FireType(Block block) {
            this.block = block;
        }

        private Identifier getId() {
            return Registries.BLOCK.getId(block);
        }

        private Text getName() {
            return block.getName();
        }
    }

    @Environment(EnvType.CLIENT)
    private static class Renderer implements EntryRenderer<FireType> {
        public static final Renderer INSTANCE = new Renderer();

        @Override
        public void render(EntryStack<FireType> entry, DrawContext graphics, Rectangle bounds, int mouseX, int mouseY, float delta) {
            var client = MinecraftClient.getInstance();
            var atlas = client.getSpriteAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
            var sprite = atlas.apply(entry.getValue().getId().withPath(path -> "block/" + path + "_0"));
            if (sprite == null) sprite = atlas.apply(MissingSprite.getMissingSpriteId());
            if (sprite == null) return;
            graphics.drawSprite(bounds.x, bounds.y, 0, bounds.width, bounds.height, sprite);
        }

        @Override
        public @Nullable Tooltip getTooltip(EntryStack<FireType> entry, TooltipContext context) {
            return Tooltip.create();
        }
    }
}
