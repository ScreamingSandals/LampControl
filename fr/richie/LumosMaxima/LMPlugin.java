/*    */ package fr.richie.LumosMaxima;
/*    */ 
/*    */ import java.util.ArrayList;
import java.util.logging.Logger;

/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.plugin.java.JavaPlugin;
/*    */ 
/*    */ public class LMPlugin extends JavaPlugin
/*    */ {
/* 12 */   public String prefix = "[LumosMaxima] ";
/* 13 */   private ArrayList<Material> redstone_materials = new ArrayList<Material>();
/*    */   public Material toolMaterial;
/* 16 */   public boolean allowOnlyPermitted = false;
/* 17 */   public boolean displayLightOnMessage = true;
/* 18 */   public boolean displayNoPermissionsMessage = true;
/* 19 */   public boolean woodenPressurePlateCanInteract = false;
/* 20 */   public boolean stonePressurePlateCanInteract = false;
			static Logger logger;
/*    */ 
/*    */   public void onEnable()
/*    */   {
			logger = this.getLogger();
/* 24 */     this.redstone_materials.add(Material.DETECTOR_RAIL);
/* 25 */     this.redstone_materials.add(Material.REDSTONE_WIRE);
/* 25 */     this.redstone_materials.add(Material.REDSTONE_BLOCK);
/* 25 */     this.redstone_materials.add(Material.PISTON_MOVING_PIECE);
/* 26 */     this.redstone_materials.add(Material.REDSTONE_TORCH_OFF);
/* 27 */     this.redstone_materials.add(Material.REDSTONE_TORCH_ON);
/* 28 */     this.redstone_materials.add(Material.DIODE_BLOCK_OFF);
/* 29 */     this.redstone_materials.add(Material.DIODE_BLOCK_ON);
/* 28 */     this.redstone_materials.add(Material.REDSTONE_COMPARATOR_OFF);
/* 28 */     this.redstone_materials.add(Material.REDSTONE_COMPARATOR_ON);
/* 29 */     this.redstone_materials.add(Material.DIODE_BLOCK_ON);
/* 30 */     this.redstone_materials.add(Material.LEVER);
/* 31 */     this.redstone_materials.add(Material.STONE_BUTTON);
/* 31 */     this.redstone_materials.add(Material.GOLD_PLATE);
/* 31 */     this.redstone_materials.add(Material.IRON_PLATE);
/*    */ 
/* 34 */     if (!getConfig().isSet("activatorItemID")) {
/* 35 */       getConfig().set("activatorItemID", Integer.valueOf(Material.FLINT_AND_STEEL.getId()));
/*    */     }
/*    */ 
/* 38 */     if (!getConfig().isSet("allowOnlyPermitted")) {
/* 39 */       getConfig().set("allowOnlyOps", "Deprecated, use allowOnlyPermitted instead. (perm to add : \"lumosmaxima.poweron\")");
				logger.info("TESTLOG");
/* 40 */       getConfig().set("allowOnlyPermitted", Boolean.valueOf(false));
/*    */     }
/*    */ 
/* 43 */     if (!getConfig().isSet("displayLightOnMessage")) {
/* 44 */       getConfig().set("displayLightOnMessage", Boolean.valueOf(true));
/*    */     }
/*    */ 
/* 47 */     if (!getConfig().isSet("displayNoPermissionsMessage")) {
/* 48 */       getConfig().set("displayNoPermissionsMessage", Boolean.valueOf(true));
/*    */     }
/*    */ 
/* 51 */     if (!getConfig().isSet("woodenPressurePlateCanInteract")) {
/* 52 */       getConfig().set("woodenPressurePlateCanInteract", Boolean.valueOf(false));
/*    */     }
/*    */ 
/* 55 */     if (!getConfig().isSet("stonePressurePlateCanInteract")) {
/* 56 */       getConfig().set("stonePressurePlateCanInteract", Boolean.valueOf(false));
/*    */     }
/*    */ 
/* 55 */     if (!getConfig().isSet("opOnWithHand")) {
/* 56 */       getConfig().set("opOnWithHand", Boolean.valueOf(false));
/*    */     }
/*    */ 
/* 60 */     saveConfig();
/*    */ 
/* 63 */     this.toolMaterial = Material.getMaterial(getConfig().getInt("activatorItemID"));
/* 64 */     this.allowOnlyPermitted = getConfig().getBoolean("allowOnlyPermitted");
/* 65 */     this.displayLightOnMessage = getConfig().getBoolean("displayLightOnMessage");
/* 66 */     this.displayNoPermissionsMessage = getConfig().getBoolean("displayNoPermissionsMessage");
/* 67 */     this.woodenPressurePlateCanInteract = getConfig().getBoolean("woodenPressurePlateCanInteract");
/* 68 */     this.stonePressurePlateCanInteract = getConfig().getBoolean("stonePressurePlateCanInteract");
/* 68 */     LMListener.opOnWithHand = getConfig().getBoolean("opOnWithHand");
/*    */ 
/* 71 */     if (this.toolMaterial == null) this.toolMaterial = Material.FLINT_AND_STEEL;
/*    */ 
/* 73 */     if (this.woodenPressurePlateCanInteract) {
/* 74 */       this.redstone_materials.add(Material.WOOD_PLATE);
/*    */     }
/*    */ 
/* 77 */     if (this.stonePressurePlateCanInteract) {
/* 78 */       this.redstone_materials.add(Material.STONE_PLATE);
/*    */     }
/*    */ 
/* 82 */     getServer().getPluginManager().registerEvents(new LMListener(this), this);
/*    */   }
/*    */ 
/*    */   public boolean isRedstoneMaterial(Material mat)
/*    */   {
/* 95 */     if (this.redstone_materials.contains(mat)) {
/* 96 */       return true;
/*    */     }
/* 98 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/jklink/Desktop/LumosMaxima.jar
 * Qualified Name:     fr.richie.LumosMaxima.LMPlugin
 * JD-Core Version:    0.6.2
 */