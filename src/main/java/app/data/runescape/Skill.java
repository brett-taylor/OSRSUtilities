package app.data.runescape;

import java.util.HashMap;
import java.util.List;

/**
 * Represents a skill from runescape.
 * @author Brett Taylor
 */
public class Skill {
    /**
     * Skills
     */
    public static final Skill ATTACK = new Skill("Attack", "/wiki/Attack");
    public static final Skill STRENGTH = new Skill("Strength", "/wiki/Strength");
    public static final Skill DEFENCE = new Skill("Defence", "/wiki/Defence");
    public static final Skill RANGED = new Skill("Ranged", "/wiki/Ranged");
    public static final Skill PRAYER = new Skill("Prayer", "/wiki/Prayer");
    public static final Skill MAGIC = new Skill("Magic", "/wiki/Magic");
    public static final Skill RUNECRAFTING = new Skill("Runecrafting", "/wiki/Runecrafting");
    public static final Skill CONSTRUCTION = new Skill("Construction", "/wiki/Construction");
    public static final Skill HITPOINTS = new Skill("Hitpoints", "/wiki/Hitpoints");
    public static final Skill AGILITY = new Skill("Agility", "/wiki/Agility");
    public static final Skill HERBLORE = new Skill("Herblore", "/wiki/Herblore");
    public static final Skill THIEVING = new Skill("Thieving", "/wiki/Thieving");
    public static final Skill CRAFTING = new Skill("Crafting", "/wiki/Crafting");
    public static final Skill FLETCHING = new Skill("Fletching", "/wiki/Fletching");
    public static final Skill SLAYER = new Skill("Slayer", "/wiki/Slayer");
    public static final Skill HUNTER = new Skill("Hunter", "/wiki/Hunter");
    public static final Skill MINING = new Skill("Mining", "/wiki/Mining");
    public static final Skill SMITHING = new Skill("Smithing", "/wiki/Smithing");
    public static final Skill FISHING = new Skill("Fishing", "/wiki/Fishing");
    public static final Skill COOKING = new Skill("Cooking", "/wiki/Cooking");
    public static final Skill FIREMAKING = new Skill("Firemaking", "/wiki/Firemaking");
    public static final Skill WOODCUTTING = new Skill("Woodcutting", "/wiki/Woodcutting");
    public static final Skill FARMING = new Skill("Farming", "/wiki/Farming");

    /**
     * Keeps track of the skills added.
     */
    private HashMap<String, Skill> skills = new HashMap<>();

    /**
     * Name of the skill.
     */
    private String name;

    /**
     * The name on the wiki used in the url of the article for this skill.
     */
    private String wikiURL;

    /**
     * Creates a Skill.
     * @param name Name of the monster.
     * @param wikiURL Wiki name of the monster.
     */
    private Skill(String name, String wikiURL) {
        this.name = name;
        this.wikiURL = wikiURL;

        skills.put(name, this);
    }

    /**
     * @return The skill name
     */
    public String getName() {
        return name;
    }

    /**
     * @return The skill's wiki article url.
     */
    public String getWikiURLEnding() {
        return wikiURL;
    }
}
