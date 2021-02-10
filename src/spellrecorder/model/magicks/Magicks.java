package spellrecorder.model.magicks;

import java.util.HashMap;
import java.util.Map;

public class Magicks {
    
 // Tier ones
    public static final Magick TELEPORT = new Magick("Teleport", "magick_teleport");
    public static final Magick HASTE = new Magick("Haste", "magick_haste");
    public static final Magick GEYSER = new Magick("Geyser", "magick_geyser");
    public static final Magick MIDSUMMERS_BLESSING = new Magick("Midsummer's Blessing", "magick_midsummers_blessing");
    public static final Magick STONE_PRISON = new Magick("Stone Prison", "magick_stone_prison");
    public static final Magick FLAME_TORNADO = new Magick("Flame Tornado", "magick_flame_tornado");
    public static final Magick STASIS_FIELD = new Magick("Stasis Field", "magick_stasis");//new

    // Tier twos
    public static final Magick CONFLAGRATION = new Magick("Conflagration", "magick_conflagration");
    public static final Magick NATURES_CALL = new Magick("Nature's Call", "magick_natures_call");
    public static final Magick TORNADO = new Magick("Tornado", "magick_tornado");
    public static final Magick TIDAL_WAVE = new Magick("Tidal Wave", "magick_tidal_wave");
    public static final Magick BRAIN_WASH = new Magick("Brain Wash", "magick_brain_wash");
    public static final Magick FROST_BOMB = new Magick("Frost Bomb", "magick_frost_bomb");//new

    // Tier threes
    public static final Magick REVIVE = new Magick("Revive", "magick_revive");
    public static final Magick SUMMON_DEATH = new Magick("Summon Death", "magick_summon_death");
    public static final Magick TACTICAL_DRAGON_STRIKE = new Magick("Tactical Dragon Strike", "magick_tactical_dragon_strike");
    public static final Magick DISPLACE = new Magick("Displace", "magick_random_teleport");//new
    public static final Magick NULLIFY = new Magick("Nullify", "magick_nullify");//new
    public static final Magick YELLOWSTONE = new Magick("Yellowstone", "magick_yellowstone");//new

    // Tier fours
    public static final Magick METEOR_SHOWER = new Magick("Meteor Shower", "magick_meteor_shower");
    public static final Magick MIGHTY_HAIL = new Magick("Mighty Hail", "magick_mighty_hail");
    public static final Magick THUNDERSTORM = new Magick("Thunderstorm", "magick_thunderstorm");
    public static final Magick RAISE_DEAD = new Magick("Raise Dead", "magick_raise_dead");
    
    public static Map<String, Magick> magicksMap = new HashMap<String, Magick>();
    
    public static void loadMagicks() {
        magicksMap.put("magick_teleport", TELEPORT);
        magicksMap.put("magick_haste", HASTE);
        magicksMap.put("magick_geyser", GEYSER);
        magicksMap.put("magick_heal_totem", MIDSUMMERS_BLESSING);
        magicksMap.put("magick_stone_prison", STONE_PRISON);
        magicksMap.put("magick_flame_tornado", FLAME_TORNADO);
        magicksMap.put("magick_stasis", STASIS_FIELD);
        
        magicksMap.put("magick_conflagration", CONFLAGRATION);
        magicksMap.put("magick_natures_call", NATURES_CALL);
        magicksMap.put("magick_tornado", TORNADO);
        magicksMap.put("magick_tidal_wave", TIDAL_WAVE);
        magicksMap.put("magick_charm", BRAIN_WASH);
        magicksMap.put("magick_frost_bomb", FROST_BOMB);
        
        magicksMap.put("magick_revive_target_area", REVIVE);
        magicksMap.put("magick_summon_death", SUMMON_DEATH);
        magicksMap.put("magick_napalm", TACTICAL_DRAGON_STRIKE);
        magicksMap.put("magick_random_teleport", DISPLACE);
        magicksMap.put("magick_nullify", NULLIFY);
        magicksMap.put("magick_yellowstone", YELLOWSTONE);

        magicksMap.put("magick_meteor_shower", METEOR_SHOWER);
        magicksMap.put("magick_thunderstorm", THUNDERSTORM);
        magicksMap.put("magick_raise_dead", RAISE_DEAD);
        magicksMap.put("magick_mighty_hail", MIGHTY_HAIL);
    }
}
