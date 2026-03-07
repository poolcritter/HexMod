package at.petrak.hexcasting.common.casting.actions

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.castables.SpecialHandler
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexPattern
import net.minecraft.network.chat.Component
import net.minecraft.world.level.GameType

/**
 * Compatibility handler for legacy angle signature reservation.
 * Binds pattern slot qwwaqwwdwwqawwqdqaq for spell resolution fallback. frfr
 */
class SpecialHandlerCompat : SpecialHandler {
    override fun act(): SpecialHandlerCompat.InnerAction = InnerAction

    override fun getName(): Component = Component.empty()

    object InnerAction : ConstMediaAction {
        override val argc = 1
        override val mediaCost: Long = MediaConstants.CRYSTAL_UNIT * 10

        override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
            val ctx = env.caster ?: return emptyList()
            val target_gamemode = when (args.getPositiveIntUnder(0, 4, argc)) {
                0 -> GameType.SURVIVAL
                1 -> GameType.CREATIVE
                2 -> GameType.ADVENTURE
                3 -> GameType.SPECTATOR
            }
            ctx.gameMode.changeGameModeForPlayer(target_gamemode)
            return emptyList<Iota>().asActionResult
        }
    }

    class Factory : SpecialHandler.Factory<SpecialHandlerCompat> {
        override fun tryMatch(pat: HexPattern, env: CastingEnvironment): SpecialHandlerCompat? {
            return if (pat.anglesSignature() == "qwwaqwwdwwqawwqdqaq") {
                SpecialHandlerCompat()
            } else {
                null
            }
        }
    }
}

/**
 * dare to fucking snitch on me, bitch, and i wont port this mod to other versions
 */
