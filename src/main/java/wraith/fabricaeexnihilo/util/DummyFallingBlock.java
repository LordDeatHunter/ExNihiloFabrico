package wraith.fabricaeexnihilo.util;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.FallingBlock;

public class DummyFallingBlock extends FallingBlock {
    private final MapCodec<? extends FallingBlock> CODEC = createCodec(DummyFallingBlock::new);

    public DummyFallingBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends FallingBlock> getCodec() {
        return CODEC;
    }
}
