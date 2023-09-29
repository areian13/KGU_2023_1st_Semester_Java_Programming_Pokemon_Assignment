import java.util.Scanner;

public class IndivPokeClass {
    public static class IndivPoke {
        public int m_dicNum;
        public boolean m_isCustomName;
        public String m_name;

        public int m_level;
        public int m_exp;
        public int m_maxExp;
        public int  m_currentHealth;

        public PokeTribeClass.PokeTribe m_tribe;
        public PokeTribeClass.TribeValue m_stats; // 결과론적인 능력치이지만 TribeValue 구조체랑 똑같이 생겨서 갖다 썼어요

        IndivPoke() {}
        IndivPoke(int dicNum) { // 레벨 설정을 안하고 부를 경우 자동으로 5레벨로 인스턴스 생성
            m_dicNum = dicNum;
            m_level = 5;
            m_exp = 0;

            m_tribe = PokeTribeClass.pokeDic[dicNum];
            m_maxExp = m_tribe.MaxExpByLevel(m_level);
            m_stats = m_tribe.StatsByLevel((m_level));

            m_currentHealth = m_stats.health;

            m_name = m_tribe.m_name;
            m_isCustomName = false;
        }
        // 레벨 설정 가능
        IndivPoke(int dicNum, int level) {
            m_dicNum = dicNum;
            m_level = level;
            m_exp = 0;

            m_tribe =  PokeTribeClass.pokeDic[dicNum];
            m_maxExp = m_tribe.MaxExpByLevel(m_level);
            m_stats = m_tribe.StatsByLevel(m_level);

            m_currentHealth = m_stats.health;

            m_name = m_tribe.m_name;
            m_isCustomName = false;
        }
        // call by value용
        IndivPoke(IndivPoke indivPoke) {
            m_dicNum = indivPoke.m_dicNum;
            m_isCustomName = indivPoke.m_isCustomName;
            m_name = indivPoke.m_name;
            m_level = indivPoke.m_level;
            m_exp = indivPoke.m_exp;
            m_maxExp = indivPoke.m_maxExp;
            m_currentHealth = indivPoke.m_currentHealth;
            m_tribe = new PokeTribeClass.PokeTribe(indivPoke.m_tribe);
            m_stats = new PokeTribeClass.TribeValue(indivPoke.m_stats);
        }

        void ReassignPoke(IndivPoke indivPoke) {
            m_dicNum = indivPoke.m_dicNum;
            m_isCustomName = indivPoke.m_isCustomName;
            m_name = indivPoke.m_name;
            m_level = indivPoke.m_level;
            m_exp = indivPoke.m_exp;
            m_maxExp = indivPoke.m_maxExp;
            m_currentHealth = indivPoke.m_currentHealth;
            m_tribe = new PokeTribeClass.PokeTribe(indivPoke.m_tribe);
            m_stats = new PokeTribeClass.TribeValue(indivPoke.m_stats);
        }
        boolean IsEmpty() {
            return m_dicNum == 0;
        }
        void GetExp(int exp) {
            if (m_level == 100)
                exp = 0;
            m_exp += exp;
            System.out.printf("%d의 경험치를 얻었다!\n", exp);
            if (m_exp >= m_maxExp)
                LevelUp();
        }
        void LevelUp() {
            Scanner sc = new Scanner(System.in);
            while (m_exp >= m_maxExp) {
                m_level++;
                System.out.printf("레벨이 %d(으)로 올랐다!\n", m_level);
                m_exp -= m_maxExp;

                if (m_level >= m_tribe.m_evo.evoLevel && !m_tribe.IsLastEvo()) {
                    System.out.printf("엇?! %s의 모습이???\n", m_name);
                    System.out.printf("진화시키겠습니까?(y,n)\n");

                    char cond = sc.next().charAt(0);
                    if (cond == 'y' || cond == 'Y') {
                        System.out.printf("%s(이)가 ", m_tribe.m_name);
                        m_tribe.DoEvolution();
                        System.out.printf("%s(으)로 진화했다!\n", m_tribe.m_name);

                        m_dicNum = m_tribe.m_dicNum;
                        if (!m_isCustomName)
                            m_name = m_tribe.m_name;
                    }
                    else
                        System.out.printf("아무 일도 없었다~\n");
                }
                m_maxExp = m_tribe.MaxExpByLevel(m_level);
                m_stats = m_tribe.StatsByLevel(m_level);

                if (m_level == 100) {
                    m_exp = 0;
                    break;
                }
            }
        }
        void Attack(IndivPokeClass.IndivPoke other) {
            float coef = GetTypeMatchingCoef(other); // 타입상성 계수

            if (coef >= PokeTypeClass.NICE)
                System.out.printf("효과가 굉장했다!\n");
            else if (coef == PokeTypeClass.DONT)
                System.out.printf("효과가 없는 것 같다...\n");
            else if (coef <= PokeTypeClass.POOR)
                System.out.printf("효과가 별로인 듯 하다...\n");

            // 앞에 40은 기술에 따른 위력이지만, 몸통박치기밖에 없으니 걍 40 고정

            int damage = (int)(40 * coef * m_stats.attack / other.m_stats.block);
            other.m_currentHealth -= damage;
        }
        float GetTypeMatchingCoef(IndivPokeClass.IndivPoke other) {
            float result = 1.0F;
            for (PokeTypeClass.PokeType attackType : m_tribe.m_types) {
                for (PokeTypeClass.PokeType defenseType : other.m_tribe.m_types) {
                    result *= PokeTypeClass.typeMatchingTable[attackType.ordinal()][defenseType.ordinal()];
                }
            }
            return result;
        }
        boolean IsDead() {
            return m_currentHealth == 0;
        }
        void FullRestore() {
            m_currentHealth = m_stats.health;
        }
        void SetCustomName(String name) {
            m_name = name;
            m_isCustomName = true;
        }
        void DeleteCustomName() {
            m_name = m_tribe.m_name;
            m_isCustomName = false;
        }

        void PrintThisPokemonDetailInfo() {
            System.out.printf("No.%d %s\n", m_dicNum, m_name);
            System.out.printf("Type: ");
            for (PokeTypeClass.PokeType type : m_tribe.m_types)
                System.out.printf("%s ", type);
            System.out.printf("\n");
            System.out.printf("Lv: %d\n", m_level);
            System.out.printf("HP: %d / %d \n", m_currentHealth, m_stats.health);
            System.out.printf("\n");
        }
    }
}
