package wraith.fabricaeexnihilo.compatibility.emi;

import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import wraith.fabricaeexnihilo.compatibility.recipeviewer.FireType;

import java.util.List;

public class FireEmiStack extends EmiStack {
    private final FireType type;

    public FireEmiStack(FireType type) {
        this.type = type;
    }

    @Override
    public EmiStack copy() {
        return new FireEmiStack(type);
    }

    @Override
    public void render(DrawContext draw, int x, int y, float delta, int flags) {
        var client = MinecraftClient.getInstance();
        var atlas = client.getSpriteAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
        var sprite = atlas.apply(type.getId().withPath(path -> "block/" + path + "_0"));
        if (sprite == null) sprite = atlas.apply(MissingSprite.getMissingSpriteId());
        if (sprite == null) return;
        draw.drawSprite(x, y, 0, 16, 16, sprite);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public NbtCompound getNbt() {
        return null;
    }

    @Override
    public Object getKey() {
        return type;
    }

    @Override
    public Identifier getId() {
        return type.getId();
    }

    @Override
    public List<Text> getTooltipText() {
        return List.of(type.getName());
    }

    @Override
    public Text getName() {
        return type.getName();
    }
}
