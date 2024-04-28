package wraith.fabricaeexnihilo.compatibility.recipeviewer;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public enum FireType {
    NORMAL(Blocks.FIRE),
    SOUL(Blocks.SOUL_FIRE);

    public final Block block;

    FireType(Block block) {
        this.block = block;
    }

    public Identifier getId() {
        return Registries.BLOCK.getId(block);
    }

    public Text getName() {
        return block.getName();
    }
}
