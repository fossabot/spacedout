package com.github.devngho.spacedout.equipment

import com.github.devngho.spacedout.addon.Addon
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

interface Equipment {
    val type: EquipmentType
    val id: String
    val name: String
    val graphicMaterial: Material
    val customModelData: Int
    val addedAddon: Addon
}

fun Equipment.toItemStack(): ItemStack{
    val item = ItemStack(this.graphicMaterial)
    item.itemMeta = item.itemMeta.apply {
        setCustomModelData(this@toItemStack.customModelData)
        displayName(Component.text(this@toItemStack.name).decoration(TextDecoration.ITALIC, false))
        lore(listOf(
            Component.text(when(this@toItemStack.type){
            EquipmentType.HELMET -> "헬멧"
            EquipmentType.CHESTPLATE -> "갑옷"
            EquipmentType.BOOTS -> "부츠"
        }).decoration(TextDecoration.ITALIC, false)
            .color(TextColor.color(255, 255, 255)),
            Component.text("from. ${this@toItemStack.addedAddon.name}").color(TextColor.color(127, 127, 127)))
        )
    }
    return item
}