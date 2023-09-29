public class PokeTypeClass {
    public static int NUM_OF_TYPE = 15;
    public static enum PokeType {
        NORMAL,
        FIRE,
        WATER,
        GRASS,
        ELECTR,
        ICE,
        FIGHT,
        POISON,
        GROUND,
        FLYING,
        PSYSHC,
        BUG,
        ROCK,
        GHOST,
        DRAGON
    }

    public static float POOR = 0.5F;
    public static float NICE = 2.0F;
    public static float JUST = 1.0F;
    public static float DONT = 0.0F;
    public static float[][] typeMatchingTable = {
            { 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, POOR, DONT, 1.0F },
            { 1.0F, POOR, POOR, NICE, 1.0F, NICE, 1.0F, 1.0F, POOR, 1.0F, 1.0F, NICE, POOR, 1.0F, POOR },
            { 1.0F, NICE, POOR, POOR, 1.0F, 1.0F, 1.0F, 1.0F, NICE, 1.0F, 1.0F, 1.0F, NICE, 1.0F, POOR },
            { 1.0F, POOR, NICE, POOR, 1.0F, 1.0F, 1.0F, POOR, NICE, POOR, 1.0F, 1.0F, NICE, 1.0F, POOR },
            { 1.0F, 1.0F, NICE, POOR, POOR, 1.0F, 1.0F, 1.0F, DONT, NICE, 1.0F, 1.0F, 1.0F, 1.0F, POOR },
            { 1.0F, 1.0F, POOR, NICE, 1.0F, POOR, 1.0F, 1.0F, NICE, NICE, 1.0F, 1.0F, 1.0F, 1.0F, NICE },
            { NICE, 1.0F, 1.0F, 1.0F, 1.0F, NICE, 1.0F, POOR, 1.0F, POOR, POOR, POOR, NICE, DONT, 1.0F },
            { 1.0F, 1.0F, 1.0F, NICE, 1.0F, 1.0F, 1.0F, POOR, POOR, 1.0F, 1.0F, NICE, POOR, POOR, 1.0F },
            { 1.0F, NICE, 1.0F, POOR, NICE, 1.0F, 1.0F, NICE, 1.0F, DONT, 1.0F, POOR, NICE, 1.0F, 1.0F },
            { 1.0F, 1.0F, 1.0F, NICE, POOR, 1.0F, NICE, 1.0F, 1.0F, 1.0F, 1.0F, NICE, POOR, 1.0F, 1.0F },
            { 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, NICE, NICE, 1.0F, 1.0F, POOR, 1.0F, 1.0F, 1.0F, 1.0F },
            { 1.0F, POOR, 1.0F, NICE, 1.0F, 1.0F, POOR, NICE, 1.0F, POOR, NICE, 1.0F, 1.0F, POOR, 1.0F },
            { 1.0F, NICE, 1.0F, 1.0F, 1.0F, NICE, POOR, 1.0F, POOR, NICE, 1.0F, NICE, 1.0F, 1.0F, 1.0F },
            { DONT, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, DONT, 1.0F, 1.0F, NICE, 1.0F },
            { 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, NICE },
    };
}
