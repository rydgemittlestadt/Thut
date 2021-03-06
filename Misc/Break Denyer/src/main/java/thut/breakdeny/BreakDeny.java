package thut.breakdeny;

import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = BreakDeny.MODID, name = "Break Denyer", version = BreakDeny.VERSION, acceptableRemoteVersions = "*", acceptedMinecraftVersions = BreakDeny.MCVERSIONS)
public class BreakDeny
{
    public static final String MODID      = "break_denyer";
    public static final String VERSION    = "1.0.1";
    public final static String MCVERSIONS = "[1.8.8,1.8.9]";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        if (event.getSide() != Side.SERVER) return;
        MinecraftForge.EVENT_BUS.register(this);
        new Config(event);
    }

    @SubscribeEvent
    public void denyBreak(BreakEvent event)
    {
        EntityPlayer player = event.getPlayer();
        if (!isOp(player) && !Config.isWhitelisted(event.state.getBlock())) event.setCanceled(true);
    }

    @SubscribeEvent
    public void denyPlace(PlaceEvent event)
    {
        EntityPlayer player = event.player;
        if (!isOp(player)) event.setCanceled(true);
    }

    @SubscribeEvent
    public void denyFrameInteract(EntityInteractEvent event)
    {
        if (event.entity instanceof EntityItemFrame)
        {
            if (!isOp(event.entityPlayer)) event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void denyFrameAttack(AttackEntityEvent event)
    {
        if (event.entity instanceof EntityItemFrame)
        {
            if (!isOp(event.entityPlayer)) event.setCanceled(true);
        }
    }

    private boolean isOp(EntityPlayer player)
    {
        if (player != null && !player.worldObj.isRemote)
        {
            UserListOpsEntry userentry = (UserListOpsEntry) ((EntityPlayerMP) player).mcServer.getConfigurationManager()
                    .getOppedPlayers().getEntry(player.getGameProfile());

            if (userentry != null
                    || !FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) { return true; }
        }
        return false;
    }
}
