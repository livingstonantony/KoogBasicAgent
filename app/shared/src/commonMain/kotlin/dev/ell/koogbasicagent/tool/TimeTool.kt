package dev.ell.koogbasicagent.tool

import ai.koog.agents.core.tools.Tool
import ai.koog.serialization.typeToken
import dev.ell.koogbasicagent.logDebug
import dev.ell.koogbasicagent.tool.MathsTool.add
import dev.ell.koogbasicagent.tool.MathsTool.divide
import dev.ell.koogbasicagent.tool.MathsTool.multiply
import dev.ell.koogbasicagent.tool.MathsTool.sub
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock



object TimeTool {

    val now = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())

    val hour = when {
        now.hour % 12 == 0 -> 12
        else -> now.hour % 12
    }

    val currentTime = object : Tool<Unit, String>(
        argsType = typeToken<Unit>(),
        resultType = typeToken<String>(),
        name = "current_time",
        description = "Returns the current time in HH:MM:SS format."
    ) {

        override suspend fun execute(args: Unit): String {
            logDebug("KoogAgent", ">> TimeTool: Getting current time")

            val now = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())

            return "${hour.toString().padStart(2, '0')}:" +
                    "${now.minute.toString().padStart(2, '0')} " +
                    if (now.hour < 12) "AM" else "PM"
        }
    }
    val all = listOf(currentTime)

}