public class PokeTribeClass {
    public static int NUM_OF_POKE = 14;

    public static class Evo {
        public int evoLevel;
        public int evoDicNum;
        public Evo() {}
        public Evo(int evoLevel, int evoDicNum) {
            this.evoLevel = evoLevel;
            this.evoDicNum = evoDicNum;
        }
        public Evo(Evo evo) {
            evoLevel = evo.evoLevel;
            evoDicNum = evo.evoDicNum;
        }
    }
    public static class TribeValue {
        public int health;
        public int attack;
        public int block;
        public int contact;
        public int defense;
        public int speed;

        public TribeValue() {}
        public TribeValue(int health, int attack, int block, int contact, int defense, int speed) {
            this.health = health;
            this.attack = attack;
            this.block = block;
            this.contact = contact;
            this.defense = defense;
            this.speed = speed;
        }
        public TribeValue(TribeValue tribeValue) {
            health = tribeValue.health;
            attack = tribeValue.attack;
            block = tribeValue.block;
            contact = tribeValue.contact;
            defense = tribeValue.defense;
            speed = tribeValue.speed;
        }
        public void Print() {
            System.out.printf("%d ", health);
            System.out.printf("%d ", attack);
            System.out.printf("%d ", block);
            System.out.printf("%d ", contact);
            System.out.printf("%d ", defense);
            System.out.printf("%d ", speed);
            System.out.println();
        }
    }
    public static enum ExpFormulaIndex {
        FAST, // 빠름
        MDFA, // 중간빠름
        MDSL, // 중간느림
        SLOW,  // 느림
    }
    public static class PokeTribe {
        public int m_dicNum; // 도감 번호
        public PokeTypeClass.PokeType[] m_types; // 갖고 있는 타입(들)
        public String m_name; // 종족 이름 ex) 이상해씨
        public TribeValue m_tribeValue; // 종족치 HABCDS
        public Evo m_evo; // 진화를 할 레벨 + 진화될 포켓몬의 도감 번호
        public ExpFormulaIndex m_expFormulaIndex; // '레벨에 따른 필요 경험치 공식'에 대한 인덱스
        public int m_baseExpYield; // 종족별 패배하였을 때 상대에게 주는 경험치량
        public int m_catchRate; // 종족별 포획률

        public PokeTribe() {}
        public PokeTribe(int dicNum, PokeTypeClass.PokeType[] types, String name, TribeValue tribeValue, Evo evo, ExpFormulaIndex expFormulaIndex, int baseExpYield, int catchRate) {
            m_dicNum = dicNum;
            m_types = types;
            m_name = name;
            m_tribeValue = tribeValue;
            m_evo = evo;
            m_expFormulaIndex = expFormulaIndex;
            m_baseExpYield = baseExpYield;
            m_catchRate = catchRate;
        }
        public PokeTribe(PokeTribe pokeTribe) {
            m_dicNum = pokeTribe.m_dicNum;
            m_types = new PokeTypeClass.PokeType[pokeTribe.m_types.length];
            for (int i = 0; i < pokeTribe.m_types.length; i++)
                m_types[i] = pokeTribe.m_types[i];
            m_name = pokeTribe.m_name;
            m_tribeValue = new TribeValue(pokeTribe.m_tribeValue);
            m_evo = new Evo(pokeTribe.m_evo);
            m_expFormulaIndex = pokeTribe.m_expFormulaIndex;
            m_baseExpYield = pokeTribe.m_baseExpYield;
            m_catchRate = pokeTribe.m_catchRate;
        }

        public int MaxExpByLevel(int level) {
            int powN3 = level * level * level;
            switch (m_expFormulaIndex) {
                case FAST:
                    return 4 * powN3 / 5;
                case MDFA:
                    return powN3;
                case MDSL:
                    return 6 * powN3 / 5 - 15 * level * level + 100 * level - 140;
                case SLOW:
                    return 5 * powN3 / 4;
            }
            return 0;
        }
        public PokeTribeClass.TribeValue StatsByLevel(int level) {
            PokeTribeClass.TribeValue tribeValue = new PokeTribeClass.TribeValue();
            tribeValue.health  = (2 * m_tribeValue.health + 100) * level / 100 + 10;
            tribeValue.attack  = (2 * m_tribeValue.attack  + 31) * level / 100 +  5;
            tribeValue.block   = (2 * m_tribeValue.block   + 31) * level / 100 +  5;
            tribeValue.contact = (2 * m_tribeValue.contact + 31) * level / 100 +  5;
            tribeValue.defense = (2 * m_tribeValue.defense + 31) * level / 100 +  5;
            tribeValue.speed   = (2 * m_tribeValue.speed   + 31) * level / 100 +  5;

            return tribeValue;
        }
        public void DoEvolution() {
            int evoDicNum = m_evo.evoDicNum;
            m_dicNum = pokeDic[evoDicNum].m_dicNum;
            m_types = pokeDic[evoDicNum].m_types;
            m_name = pokeDic[evoDicNum].m_name;
            m_tribeValue = pokeDic[evoDicNum].m_tribeValue;
            m_evo = pokeDic[evoDicNum].m_evo;
            m_expFormulaIndex = pokeDic[evoDicNum].m_expFormulaIndex;
            m_baseExpYield = pokeDic[evoDicNum].m_baseExpYield;
            m_catchRate = pokeDic[evoDicNum].m_catchRate;
        }
        public boolean IsLastEvo() {
            return m_evo.evoLevel == -1;
        }
    }
    public static enum PokeDicNum {
        김뮤진,
        이상해씨, 이상해풀, 이상해꽃,
        파이리, 리자드, 리자몽,
        꼬부기, 어니부기, 거북왕,
        캐터피, 단데기, 버터플,
        구구, 피죤, 피죤투,
        피카츄, 라이츄,
    }
    public final static PokeTribeClass.PokeTribe[] pokeDic = {
            new PokeTribeClass.PokeTribe(PokeTribeClass.PokeDicNum.    김뮤진.ordinal(), new PokeTypeClass.PokeType[]{                               PokeTypeClass.PokeType.PSYSHC },    "김뮤진",new PokeTribeClass.TribeValue( 999,999,999,999,999,999 ),new PokeTribeClass.Evo( -1, PokeTribeClass.PokeDicNum.    김뮤진.ordinal() ), PokeTribeClass.ExpFormulaIndex.FAST,999,255 ),

            new PokeTribeClass.PokeTribe(PokeTribeClass.PokeDicNum.  이상해씨.ordinal(), new PokeTypeClass.PokeType[]{ PokeTypeClass.PokeType. GRASS,PokeTypeClass.PokeType.POISON },  "이상해씨",new PokeTribeClass.TribeValue(  45, 49, 49, 65, 65, 45 ),new PokeTribeClass.Evo( 16, PokeTribeClass.PokeDicNum.  이상해풀.ordinal() ), PokeTribeClass.ExpFormulaIndex.MDSL, 64, 45 ),
            new PokeTribeClass.PokeTribe(PokeTribeClass.PokeDicNum.  이상해풀.ordinal(), new PokeTypeClass.PokeType[]{ PokeTypeClass.PokeType. GRASS,PokeTypeClass.PokeType.POISON },  "이상해풀",new PokeTribeClass.TribeValue(  60, 62, 63, 80, 80, 60 ),new PokeTribeClass.Evo( 32, PokeTribeClass.PokeDicNum.  이상해꽃.ordinal() ), PokeTribeClass.ExpFormulaIndex.MDSL,141, 45 ),
            new PokeTribeClass.PokeTribe(PokeTribeClass.PokeDicNum.  이상해꽃.ordinal(), new PokeTypeClass.PokeType[]{ PokeTypeClass.PokeType. GRASS,PokeTypeClass.PokeType.POISON },  "이상해꽃",new PokeTribeClass.TribeValue(  80, 82, 83,100,100, 80 ),new PokeTribeClass.Evo( -1, PokeTribeClass.PokeDicNum.    김뮤진.ordinal() ), PokeTribeClass.ExpFormulaIndex.MDSL,208, 45 ),

            new PokeTribeClass.PokeTribe(PokeTribeClass.PokeDicNum.    파이리.ordinal(), new PokeTypeClass.PokeType[]{                               PokeTypeClass.PokeType.  FIRE },    "파이리",new PokeTribeClass.TribeValue(  39, 52, 43, 60, 50, 65 ),new PokeTribeClass.Evo( 16, PokeTribeClass.PokeDicNum.    리자드.ordinal() ), PokeTribeClass.ExpFormulaIndex.MDSL, 65, 45 ),
            new PokeTribeClass.PokeTribe(PokeTribeClass.PokeDicNum.    리자드.ordinal(), new PokeTypeClass.PokeType[]{                               PokeTypeClass.PokeType.  FIRE },    "리자드",new PokeTribeClass.TribeValue(  58, 64, 58, 80, 65, 80 ),new PokeTribeClass.Evo( 36, PokeTribeClass.PokeDicNum.    리자몽.ordinal() ), PokeTribeClass.ExpFormulaIndex.MDSL,142, 45 ),
            new PokeTribeClass.PokeTribe(PokeTribeClass.PokeDicNum.    리자몽.ordinal(), new PokeTypeClass.PokeType[]{ PokeTypeClass.PokeType.  FIRE,PokeTypeClass.PokeType.FLYING },    "리자몽",new PokeTribeClass.TribeValue(  78, 84, 78,109, 85,100 ),new PokeTribeClass.Evo( -1, PokeTribeClass.PokeDicNum.    김뮤진.ordinal() ), PokeTribeClass.ExpFormulaIndex.MDSL,209, 45 ),

            new PokeTribeClass.PokeTribe(PokeTribeClass.PokeDicNum.    꼬부기.ordinal(), new PokeTypeClass.PokeType[]{                               PokeTypeClass.PokeType. WATER },    "꼬부기",new PokeTribeClass.TribeValue(  44, 48, 65, 50, 64, 43 ),new PokeTribeClass.Evo( 16, PokeTribeClass.PokeDicNum.  어니부기.ordinal() ), PokeTribeClass.ExpFormulaIndex.MDSL, 66, 45 ),
            new PokeTribeClass.PokeTribe(PokeTribeClass.PokeDicNum.  어니부기.ordinal(), new PokeTypeClass.PokeType[]{                               PokeTypeClass.PokeType. WATER },  "어니부기",new PokeTribeClass.TribeValue(  59, 63, 80, 65, 80, 58 ),new PokeTribeClass.Evo( 36, PokeTribeClass.PokeDicNum.    거북왕.ordinal() ), PokeTribeClass.ExpFormulaIndex.MDSL,143, 45 ),
            new PokeTribeClass.PokeTribe(PokeTribeClass.PokeDicNum.    거북왕.ordinal(), new PokeTypeClass.PokeType[]{                               PokeTypeClass.PokeType. WATER },    "거북왕",new PokeTribeClass.TribeValue(  79, 83,100, 85,105, 78 ),new PokeTribeClass.Evo( -1, PokeTribeClass.PokeDicNum.    김뮤진.ordinal() ), PokeTribeClass.ExpFormulaIndex.MDSL,210, 45 ),

            new PokeTribeClass.PokeTribe(PokeTribeClass.PokeDicNum.    캐터피.ordinal(), new PokeTypeClass.PokeType[]{                               PokeTypeClass.PokeType.   BUG },    "캐터피",new PokeTribeClass.TribeValue(  45, 30, 35, 20, 20, 45 ),new PokeTribeClass.Evo(  7, PokeTribeClass.PokeDicNum.    단데기.ordinal() ), PokeTribeClass.ExpFormulaIndex.MDFA, 53,255 ),
            new PokeTribeClass.PokeTribe(PokeTribeClass.PokeDicNum.    단데기.ordinal(), new PokeTypeClass.PokeType[]{                               PokeTypeClass.PokeType.   BUG },    "단데기",new PokeTribeClass.TribeValue(  50, 20, 55, 25, 25, 30 ),new PokeTribeClass.Evo( 10, PokeTribeClass.PokeDicNum.    버터플.ordinal() ), PokeTribeClass.ExpFormulaIndex.MDFA, 72,120 ),
            new PokeTribeClass.PokeTribe(PokeTribeClass.PokeDicNum.    버터플.ordinal(), new PokeTypeClass.PokeType[]{ PokeTypeClass.PokeType.   BUG,PokeTypeClass.PokeType.FLYING },    "버터플",new PokeTribeClass.TribeValue(  60, 45, 50, 80, 80, 70 ),new PokeTribeClass.Evo( -1, PokeTribeClass.PokeDicNum.    김뮤진.ordinal() ), PokeTribeClass.ExpFormulaIndex.MDFA,160, 45 ),

            new PokeTribeClass.PokeTribe(PokeTribeClass.PokeDicNum.      구구.ordinal(), new PokeTypeClass.PokeType[]{ PokeTypeClass.PokeType.NORMAL,PokeTypeClass.PokeType.FLYING },      "구구",new PokeTribeClass.TribeValue(  40, 45, 40, 35, 35, 56 ),new PokeTribeClass.Evo( 18, PokeTribeClass.PokeDicNum.      피죤.ordinal() ), PokeTribeClass.ExpFormulaIndex.MDSL, 55,255 ),
            new PokeTribeClass.PokeTribe(PokeTribeClass.PokeDicNum.      피죤.ordinal(), new PokeTypeClass.PokeType[]{ PokeTypeClass.PokeType.NORMAL,PokeTypeClass.PokeType.FLYING },      "피죤",new PokeTribeClass.TribeValue(  63, 60, 55, 50, 50, 71 ),new PokeTribeClass.Evo( 36, PokeTribeClass.PokeDicNum.    피죤투.ordinal() ), PokeTribeClass.ExpFormulaIndex.MDSL,113,120 ),
            new PokeTribeClass.PokeTribe(PokeTribeClass.PokeDicNum.    피죤투.ordinal(), new PokeTypeClass.PokeType[]{ PokeTypeClass.PokeType.NORMAL,PokeTypeClass.PokeType.FLYING },    "피죤투",new PokeTribeClass.TribeValue(  83, 80, 75, 70, 70,101 ),new PokeTribeClass.Evo( -1, PokeTribeClass.PokeDicNum.    김뮤진.ordinal() ), PokeTribeClass.ExpFormulaIndex.MDSL,172, 45 ),

            new PokeTribeClass.PokeTribe(PokeTribeClass.PokeDicNum.    피카츄.ordinal(), new PokeTypeClass.PokeType[]{                               PokeTypeClass.PokeType.ELECTR },    "피카츄",new PokeTribeClass.TribeValue(  35, 55, 30, 50, 50, 90 ),new PokeTribeClass.Evo( 36, PokeTribeClass.PokeDicNum.    라이츄.ordinal() ), PokeTribeClass.ExpFormulaIndex.MDFA, 82,190 ),
            new PokeTribeClass.PokeTribe(PokeTribeClass.PokeDicNum.    라이츄.ordinal(), new PokeTypeClass.PokeType[]{                               PokeTypeClass.PokeType.ELECTR },    "라이츄",new PokeTribeClass.TribeValue(  60, 90, 55, 90, 80,100 ),new PokeTribeClass.Evo( -1, PokeTribeClass.PokeDicNum.    김뮤진.ordinal() ), PokeTribeClass.ExpFormulaIndex.MDFA,122, 75 ),
    };
}
