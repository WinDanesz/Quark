package vazkii.quark.base.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlockStructureHelper;
import net.minecraft.client.gui.screen.EnchantmentScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.PistonTileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.quark.automation.client.render.ChainRenderer;
import vazkii.quark.automation.client.render.PistonTileEntityRenderer;
import vazkii.quark.automation.module.ChainLinkageModule;
import vazkii.quark.automation.module.FeedingTroughModule;
import vazkii.quark.automation.module.PistonsMoveTileEntitiesModule;
import vazkii.quark.client.tooltip.EnchantedBookTooltips;
import vazkii.quark.management.entity.ChestPassengerEntity;
import vazkii.quark.management.module.ItemSharingModule;
import vazkii.quark.mobs.entity.CrabEntity;
import vazkii.quark.tools.item.PickarangItem;
import vazkii.quark.tools.module.AncientTomesModule;
import vazkii.quark.tools.module.PickarangModule;
import vazkii.quark.tweaks.client.emote.EmoteHandler;
import vazkii.quark.tweaks.module.HoeHarvestingModule;
import vazkii.quark.tweaks.module.ImprovedSleepingModule;
import vazkii.quark.tweaks.module.LockRotationModule;
import vazkii.quark.tweaks.module.SpringySlimeModule;

import java.util.List;
import java.util.Map;

/**
 * @author WireSegal
 * Created at 10:10 AM on 8/15/19.
 */
@SuppressWarnings("unused")
public class AsmHooks {

	// ==========================================================================
	// Piston Logic Replacing
	// ==========================================================================

	public static PistonBlockStructureHelper transformStructureHelper(PistonBlockStructureHelper helper, World world, BlockPos sourcePos, Direction facing, boolean extending) {
		return new QuarkPistonStructureHelper(helper, world, sourcePos, facing, extending);
	}

	// ==========================================================================
	// Pistons Move TEs
	// ==========================================================================

	public static boolean setPistonBlock(World world, BlockPos pos, BlockState state, int flags) {
		return PistonsMoveTileEntitiesModule.setPistonBlock(world, pos, state, flags);
	}

	public static boolean shouldPistonMoveTE(boolean parent, BlockState state) {
		return PistonsMoveTileEntitiesModule.shouldMoveTE(parent, state);
	}

	public static void postPistonPush(PistonBlockStructureHelper helper, World world, Direction direction, boolean extending) {
		PistonsMoveTileEntitiesModule.detachTileEntities(world, helper, direction, extending);
	}

	@OnlyIn(Dist.CLIENT)
	public static boolean renderPistonBlock(PistonTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		return PistonTileEntityRenderer.renderPistonBlock(tileEntityIn, partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
	}

	// ==========================================================================
	// Emotes
	// ==========================================================================

	public static void updateEmotes(LivingEntity entity) {
		EmoteHandler.updateEmotes(entity);
	}

	// ==========================================================================
	// Fortune on Hoes
	// ==========================================================================

	public static boolean canFortuneApply(Enchantment enchantment, ItemStack stack) {
		return HoeHarvestingModule.canFortuneApply(enchantment, stack);
	}

	// ==========================================================================
	// Improved Sleeping
	// ==========================================================================

	public static boolean isEveryoneAsleep(boolean parent) {
		return ImprovedSleepingModule.isEveryoneAsleep(parent);
	}

	// ==========================================================================
	// Items In Chat
	// ==========================================================================

	public static ITextComponent createStackComponent(ITextComponent parent, ItemStack stack) {
		return ItemSharingModule.createStackComponent(stack, parent);
	}

	public static int transformQuadRenderColor(int parent) {
		return ItemSharingModule.transformColor(parent);
	}

	// ==========================================================================
	// Springy Slime
	// ==========================================================================

	public static void applyCollisionLogic(Entity entity, Vec3d attempted, Vec3d actual) {
		SpringySlimeModule.onEntityCollision(entity, attempted, actual);
	}

	public static void recordMotion(Entity entity) {
		SpringySlimeModule.recordMotion(entity);
	}

	// ==========================================================================
	// Pickarang
	// ==========================================================================

	public static boolean canPiercingApply(Enchantment enchantment, ItemStack stack) {
		return enchantment == Enchantments.PIERCING && stack.getItem() instanceof PickarangItem;
	}

	public static boolean isNotEfficiency(Enchantment enchantment) {
		return enchantment != Enchantments.EFFICIENCY;
	}

	public static boolean canSharpnessApply(ItemStack stack) {
		return stack.getItem() instanceof PickarangItem;
	}

	public static DamageSource createPlayerDamage(PlayerEntity player) {
		return PickarangModule.createDamageSource(player);
	}

	// ==========================================================================
	// Chain Linkage
	// ==========================================================================

	public static void updateChain(Entity entity) {
		ChainLinkageModule.onEntityUpdate(entity);
	}

	@OnlyIn(Dist.CLIENT)
	public static void renderChain(EntityRenderer render, Entity entity, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, float partTicks) {
		ChainRenderer.renderChain(render, entity, matrixStack, renderBuffer, partTicks);
	}

	public static void dropChain(Entity entity) {
		ChainLinkageModule.drop(entity);
	}

	// ==========================================================================
	// Feeding Troughs
	// ==========================================================================

	public static PlayerEntity findTroughs(PlayerEntity found, TemptGoal goal) {
		return FeedingTroughModule.temptWithTroughs(goal, found);
	}

	// ==========================================================================
	// Crabs
	// ==========================================================================

	public static void rave(IWorld world, BlockPos pos, int type, int record) {
		if (type == 1010)
			CrabEntity.rave(world, pos, record != 0);
	}

	// ==========================================================================
	// Chests in Boats
	// ==========================================================================

	public static Entity ensurePassengerIsNotChest(Entity passenger) {
		if (passenger instanceof ChestPassengerEntity)
			return null;
		return passenger;
	}

    // ==========================================================================
    // Rotation Lock
    // ==========================================================================

    public static BlockState alterPlacementState(BlockState state, BlockItemUseContext ctx) {
        return LockRotationModule.fixBlockRotation(state, ctx);
    }

	// ==========================================================================
	// Enchanted Book Tooltips
	// ==========================================================================

	@OnlyIn(Dist.CLIENT)
	public static List<String> captureEnchantingData(List<String> list, EnchantmentScreen screen, Enchantment enchantment, int level) {
		return EnchantedBookTooltips.captureEnchantingData(list, screen, enchantment, level);
	}

	public static Map<Enchantment, Integer> getAncientTomeEnchantments(ItemStack stack) {
		return AncientTomesModule.getTomeEnchantments(stack);
	}
}
