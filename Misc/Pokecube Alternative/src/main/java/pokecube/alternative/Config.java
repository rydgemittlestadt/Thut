package pokecube.alternative;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import thut.core.common.config.ConfigBase;
import thut.core.common.config.Configure;

public class Config extends ConfigBase
{
    public static Config instance;

    @Configure(category = "client")
    public float         scale         = 1.0f;
    @Configure(category = "client")
    public int           shift         = 0;
    @Configure(category = "client")
    public boolean       cooldownMeter = true;
    @Configure(category = "misc")
    public boolean       autoThrow     = true;

    public Config()
    {
        super(null);
    }

    public Config(File file)
    {
        super(file, new Config());
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
        populateSettings();
        applySettings();
        save();
    }

    @Override
    protected void applySettings()
    {
    }

}
