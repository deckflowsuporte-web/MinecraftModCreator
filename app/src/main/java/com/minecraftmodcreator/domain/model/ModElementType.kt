package com.minecraftmodcreator.domain.model

// Mod Types - Categories of mods
enum class ModType(val displayName: String, val emoji: String, val description: String) {
    BASIC("Basic Mod", "📦", "Items, blocks, tools, armor, food, recipes"),
    TEXTURE("Texture Pack", "🎨", "Custom textures for items, blocks, mobs"),
    INTERFACE("Interface Mod", "🖥️", "Custom menus, HUDs, GUIs"),
    XRAY("X-Ray Mod", "👁️", "See ores and blocks through walls"),
    GLOW("Glow Mod", "💡", "Make mobs, blocks, items glow"),
    ACTION("Action Mod", "🔧", "Execute actions on events"),
    COMPLETE("Complete Mod", "🚀", "All features in one mod")
}

// Element Types - Individual elements within a mod
enum class ModElementType(val displayName: String, val emoji: String, val category: String) {
    // Basic Elements
    ITEM("Item", "🗡️", "Basic"),
    BLOCK("Block", "🧱", "Basic"),
    
    // Equipment
    TOOL("Tool", "⛏️", "Equipment"),
    ARMOR("Armor", "🛡️", "Equipment"),
    WEAPON("Weapon", "⚔️", "Equipment"),
    
    // Entities
    MOB("Mob", "🐱", "Entities"),
    ENTITY("Entity", "🐉", "Entities"),
    RIDEABLE("Rideable", "🦎", "Entities"),
    PROJECTILE("Projectile", "🏹", "Entities"),
    
    // World Generation
    BIOME("Biome", "🌲", "World"),
    ORE("Ore", "💎", "World"),
    STRUCTURE("Structure", "🏛️", "World"),
    FEATURE("Feature", "🌿", "World"),
    
    // Crafting
    RECIPE("Recipe", "🍖", "Crafting"),
    FURNACE_RECIPE("Smelting", "🔥", "Crafting"),
    SMITHING_RECIPE("Smithing", "🔨", "Crafting"),
    BREWING_RECIPE("Brewing", "🧪", "Crafting"),
    
    // Magic & Effects
    ENCHANTMENT("Enchantment", "✨", "Magic"),
    POTION("Potion", "🧪", "Magic"),
    EFFECT("Effect", "💫", "Magic"),
    
    // Food
    FOOD("Food", "🍎", "Food"),
    DRINK("Drink", "🥤", "Food"),
    
    // Functional Blocks
    BLOCK_ENTITY("Block Entity", "📦", "Functional"),
    
    // Progression
    CUSTOM_ACHIEVEMENT("Achievement", "🏆", "Progression"),
    ADVANCEMENT("Advancement", "🎖️", "Progression"),
    LOOT_TABLE("Loot Table", "📜", "Progression"),
    
    // Commands & Functions
    FUNCTION("Function", "⚡", "Commands"),
    CUSTOM_COMMAND("Custom Command", "💬", "Commands"),
    
    // Advanced
    CUSTOM_DIMENSION("Dimension", "🌍", "Advanced"),
    CUSTOM_PORTAL("Portal", "🌀", "Advanced"),
    CUSTOM_EVENT("Event", "🎯", "Advanced"),
    
    // ========== TEXTURE & VISUAL MODS ==========
    TEXTURE("Texture", "🎨", "Texture"),
    ITEM_TEXTURE("Item Texture", "🖼️", "Texture"),
    BLOCK_TEXTURE("Block Texture", "🖼️", "Texture"),
    ENTITY_TEXTURE("Entity Texture", "🖼️", "Texture"),
    PARTICLE("Particle Effect", "✨", "Texture"),
    MODEL_3D("3D Model", "📐", "Texture"),
    
    // ========== INTERFACE MODS ==========
    CUSTOM_UI("Custom UI", "🖥️", "Interface"),
    HUD_ELEMENT("HUD Element", "📊", "Interface"),
    GUI_SCREEN("GUI Screen", "📱", "Interface"),
    BUTTON("Button", "🔘", "Interface"),
    WIDGET("Widget", "📦", "Interface"),
    SCROLL_PANEL("Scroll Panel", "📜", "Interface"),
    PROGRESS_BAR("Progress Bar", "📈", "Interface"),
    SLOT("Slot", "⬜", "Interface"),
    
    // ========== X-RAY MODS ==========
    XRAY_CONFIG("X-Ray Config", "👁️", "XRay"),
    ORE_VISIBILITY("Ore Visibility", "💎", "XRay"),
    BLOCK_VISIBILITY("Block Visibility", "🔲", "XRay"),
    WIREFRAME_MODE("Wireframe", "📐", "XRay"),
    XRAY_RANGE("Range Config", "📏", "XRay"),
    XRAY_DEPTH("Depth Config", "⬇️", "XRay"),
    
    // ========== GLOW MODS ==========
    GLOW_EFFECT("Glow Effect", "💡", "Glow"),
    GLOW_MOB("Glow Mob", "👻", "Glow"),
    GLOW_BLOCK("Glow Block", "🔦", "Glow"),
    GLOW_ITEM("Glow Item", "💫", "Glow"),
    GLOW_COLOR("Glow Color", "🌈", "Glow"),
    GLOW_INTENSITY("Glow Intensity", "☀️", "Glow"),
    
    // ========== ACTION MODS ==========
    CUSTOM_ACTION("Custom Action", "🔧", "Actions"),
    TRIGGER("Trigger", "🎯", "Actions"),
    CONDITION("Condition", "⚖️", "Actions"),
    LOOP("Loop", "🔄", "Actions"),
    COMMAND_EXEC("Execute Command", "💻", "Actions"),
    TELEPORT("Teleport", "🌀", "Actions"),
    SPAWN_ENTITY("Spawn Entity", "🐱", "Actions"),
    PLAY_SOUND("Play Sound", "🔊", "Actions"),
    SEND_MESSAGE("Send Message", "💬", "Actions"),
    GIVE_ITEM("Give Item", "🎁", "Actions"),
    
    // ========== METADATA ==========
    MOD_INFO("Mod Info", "ℹ️", "Metadata"),
    MOD_ICON("Mod Icon", "🎭", "Metadata"),
    MOD_VERSION("Version", "📌", "Metadata"),
    MOD_AUTHOR("Author", "👤", "Metadata"),
    MOD_DEPENDENCY("Dependency", "🔗", "Metadata"),
    MOD_DESCRIPTION("Description", "📝", "Metadata")
}
