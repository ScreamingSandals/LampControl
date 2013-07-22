/*    */ package fr.richie.LumosMaxima;
/*    */ 

		 import net.minecraft.server.v1_6_R2.WorldServer;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.block.Block;
/*    */ import org.bukkit.block.BlockState;
/*    */ import org.bukkit.craftbukkit.v1_6_R2.CraftWorld;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.block.Action;
/*    */ import org.bukkit.event.block.BlockPhysicsEvent;
/*    */ import org.bukkit.event.block.BlockPlaceEvent;
/*    */ import org.bukkit.event.player.PlayerInteractEvent;
/*    */ import org.bukkit.inventory.ItemStack;

/*    */ 
/*    */ public class LMListener
/*    */   implements Listener
/*    */ {

/* 20 */   public static boolean opOnWithHand = false;
	
/* 21 */   LMPlugin plugin = null;
/*    */ 
/*    */   public LMListener(LMPlugin p) {
/* 24 */     this.plugin = p;
/*    */   }
/*    */ 
/*    */   @EventHandler
/*    */   public void onBlockPhysics(BlockPhysicsEvent e)
/*    */   {
/* 30 */     int typeID = e.getBlock().getTypeId();
/*    */ 
/* 32 */     if (((typeID == 123) || (typeID == 124)) && 
/* 33 */       (!this.plugin.isRedstoneMaterial(e.getChangedType())))
				{
/* 34 */       e.setCancelled(true);
				//LMPlugin.logger.info(typeID+" TO "+e.getChangedType());
				}
/*    */   }
/*    */ 

/*    */   @EventHandler
/*    */   public void onPlayerInteract(PlayerInteractEvent e)
/*    */   {
/* 43 */     if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
/*    */ 
			 if (e.getPlayer().isOp() && opOnWithHand)
			 {
				 if ((e.getPlayer().getItemInHand() == null) || (!e.getPlayer().getItemInHand().getType().equals(Material.AIR) && !e.getPlayer().getItemInHand().getType().equals(this.plugin.toolMaterial))) {
					return;
				}
			 }
			 else
			 {
				 if ((e.getPlayer().getItemInHand() == null) || (!e.getPlayer().getItemInHand().getType().equals(this.plugin.toolMaterial))) {
					return;
				}
			 }
/*    */ 
/* 49 */     if (e.getClickedBlock().getType().equals(Material.REDSTONE_LAMP_ON)) {
/*    */ 
/* 52 */       if (this.plugin.displayLightOnMessage) {
/* 53 */         e.getPlayer().sendMessage(ChatColor.RED + this.plugin.prefix + "This lamp is already on !");
/*    */       }
/* 55 */       return;
/*    */     }
/*    */ 
/* 58 */     if (!e.getClickedBlock().getType().equals(Material.REDSTONE_LAMP_OFF)) return;
/*    */ 
/* 60 */     e.setCancelled(true);
/*    */ 
/* 62 */     if ((this.plugin.allowOnlyPermitted) && (!e.getPlayer().hasPermission("lumosmaxima.poweron"))) return;
/*    */ 
/* 64 */     Block b = e.getClickedBlock();
/* 65 */     BlockState blockState = b.getState();
/*    */ 
/* 67 */     switchLamp(b, true);
/*    */ 
/* 69 */     BlockPlaceEvent checkBuildPerms = new BlockPlaceEvent(b, blockState, b, new ItemStack(Material.REDSTONE_LAMP_ON), e.getPlayer(), true);
/* 70 */     Bukkit.getPluginManager().callEvent(checkBuildPerms);
/*    */ 
/* 72 */     if (checkBuildPerms.isCancelled()) {
/* 73 */       switchLamp(b, false);
/* 74 */       if (this.plugin.displayNoPermissionsMessage)
/* 75 */         e.getPlayer().sendMessage(ChatColor.RED + this.plugin.prefix + "You don't have permissions to build here !");
/* 76 */       return;
/*    */     }
/*    */ 
/* 79 */     if (this.plugin.displayLightOnMessage)
/* 80 */       e.getPlayer().sendMessage(ChatColor.GREEN + this.plugin.prefix + "\"Lumos maxima !\"");
/*    */   }
/*    */ 
/*    */   private void switchLamp(Block b, boolean lighting)
/*    */   {
/* 87 */     WorldServer ws = ((CraftWorld)b.getWorld()).getHandle();
/*    */ 
/* 89 */     boolean mem = ws.isStatic;
/* 90 */     if (!mem) ws.isStatic = true;
/*    */ 
/* 93 */     if (lighting)
/* 94 */       b.setTypeIdAndData(Material.REDSTONE_LAMP_ON.getId(), (byte)0, false);
/*    */     else {
/* 96 */       b.setTypeIdAndData(Material.REDSTONE_LAMP_OFF.getId(), (byte)0, false);
/*    */     }
/*    */ 
/* 99 */     if (!mem) ws.isStatic = false;
/*    */   }
/*    */ }

/* Location:           /Users/jklink/Desktop/LumosMaxima.jar
 * Qualified Name:     fr.richie.LumosMaxima.LMListener
 * JD-Core Version:    0.6.2
 */