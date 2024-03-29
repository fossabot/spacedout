/*
Copyright 2022, ngho

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.devngho.spacedout.planet

import com.github.devngho.spacedout.addon.Addon
import com.github.devngho.spacedout.addon.AddonManager
import com.github.devngho.spacedout.equipment.Equipment
import com.github.devngho.spacedout.equipment.OxygenMask
import de.tr7zw.nbtinjector.javassist.NotFoundException
import org.bukkit.GameRule
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Biome
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.generator.BiomeProvider
import org.bukkit.generator.ChunkGenerator
import org.bukkit.generator.WorldInfo
import org.bukkit.util.noise.SimplexOctaveGenerator
import java.util.*


//수성
class Venus : Planet{
    class VenusGenerator : ChunkGenerator() {
        private val prePopulators: List<PregeneratedPopulator> = listOf(OrePopulator(10, 50, 50, Material.RAW_GOLD_BLOCK,
            listOf(Material.BASALT), 20, 40))
        override fun getDefaultBiomeProvider(worldInfo: WorldInfo): BiomeProvider {
            return OneBiomeProvider(Biome.CRIMSON_FOREST)
        }
        @Suppress("DEPRECATION")
        override fun generateChunkData(world: World, random: Random, chunkX: Int, chunkZ: Int, biome: BiomeGrid): ChunkData {
            val generator = SimplexOctaveGenerator(Random(world.seed), 8)
            val chunk = createChunkData(world)
            generator.setScale(0.001)
            for (X in 0..15) for (Z in 0..15) {
                val currentHeight = (generator.noise(
                    (chunkX * 16 + X).toDouble(),
                    (chunkZ * 16 + Z).toDouble(),
                    0.5,
                    0.5
                ) * 30.0 + 20.0).toInt()
                for (i in currentHeight downTo currentHeight - 15) chunk.setBlock(X, i, Z, Material.BASALT)
                for (i in currentHeight - 15 downTo 1) chunk.setBlock(X, i, Z, Material.LAVA)
                chunk.setBlock(X, 0, Z, Material.BEDROCK)
            }
            prePopulators.forEach { it.populate(world, random, chunk, chunkX, chunkZ) }
            return chunk
        }
    }

    override fun configWorld(world: World) {
        world.setGameRule(GameRule.DO_WEATHER_CYCLE,  false)
        world.setGameRule(GameRule.DO_MOB_SPAWNING,  false)
    }
    override fun initPlanetConfig(configurationSection: ConfigurationSection) {
        configurationSection.set("worldbordersize", 384.0)
        configurationSection.set("position", 0.72)
        configurationSection.set("graphicmaterial", "BASALT")
    }

    override fun loadPlanetConfig(configurationSection: ConfigurationSection): Boolean {
        if (!configurationSection.contains("position")) return true
        pos = configurationSection.getDouble("position", 0.72)
        graphicMaterial = Material.getMaterial(configurationSection.getString("graphicmaterial", "BASALT")!!.uppercase(), false) ?: Material.BASALT
        worldBorderSize = configurationSection.getDouble("worldbordersize", 384.0)
        return false
    }

    override val codeName: String = "venus"
    override var name: String = "planets.${codeName}"
    override var description: String = "planets.${codeName}_description"
    override val chunkGenerator: ChunkGenerator = VenusGenerator()
    override var pos: Double = 0.72
    override var graphicMaterial: Material = Material.BASALT
    override var worldBorderSize = 384.0
    override val needEquipments: MutableList<Equipment> = mutableListOf(OxygenMask())
    override val addedAddon: Addon
        get() = AddonManager.spacedoutAddon
}