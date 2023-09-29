import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class TrainerClass {
    static Scanner sc = new Scanner(System.in);

    public static int MAX_HANG_POKE = 3;

    public static class Trainer {
        public static class Potion {
            String m_name;
            int m_healValue;
            int m_count;
            int m_cost;

            Potion() {}
            Potion(String name, int healValue, int count, int cost) {
                m_name = name;
                m_healValue = healValue;
                m_count = count;
                m_cost = cost;
            }
        }
        public static class MonsterBall {
            String m_name;
            float m_catchRate;
            int m_count;
            int m_cost;

            MonsterBall() {}
            MonsterBall(String name, float catchRate, int count, int cost) {
                m_name = name;
                m_catchRate = catchRate;
                m_count = count;
                m_cost = cost;
            }
        }

        public int m_trainerLevel;
        public IndivPokeClass.IndivPoke[] m_hangPokeList = new IndivPokeClass.IndivPoke[MAX_HANG_POKE];
        public int m_hangPokeCnt;
        public int m_fightPokeIdx;

        public ArrayList<IndivPokeClass.IndivPoke>  m_pokeCenterList = new ArrayList<>();

        public Potion[] m_potions;
        public MonsterBall[] m_balls;
        public int m_candy;

        public Trainer() {
            m_potions = new Potion[] {
                new Potion(    "상처약", 20, 5,  2),
                new Potion("좋은상처약", 60, 0,  5),
                new Potion("고급상처약",120, 0,  7),
                new Potion(  "풀회복약", -1, 0, 10),
            };
            m_balls = new MonsterBall[] {
               new MonsterBall("몬스터볼",1.0F, 5,  2),
               new MonsterBall(  "슈퍼볼",1.5F, 0,  6),
               new MonsterBall("하이퍼볼",2.0F, 0, 10),
               new MonsterBall("마스터볼", 255, 0, 15),
            };
            m_candy = 0;
            m_hangPokeCnt = 0;
            m_fightPokeIdx = 0;
            m_trainerLevel = 10;
        }

        boolean IsAllDead() {
            for (int i = 0; i < m_hangPokeCnt; i++) {
                if (m_hangPokeList[i].m_currentHealth > 0)
                    return false;
            }
            return true;
        }
        void TakeNewPokemon(int dicNum) {
            TakeNewPokemon(dicNum, 5);
        }
        void TakeNewPokemon(int dicNum, int level) {
            IndivPokeClass.IndivPoke newPokemon = new IndivPokeClass.IndivPoke(dicNum, level);
            System.out.printf("%s!!! 넌 내꺼야!!!\n", newPokemon.m_name);
            if (m_hangPokeCnt == MAX_HANG_POKE)
            {
                System.out.printf("들고 다닐 수 있는 포켓몬의 수를 넘어 %s(을)를 포켓몬 센터로 전송합니다.\n", newPokemon.m_name);
                m_pokeCenterList.add(newPokemon);
                return;
            }
            m_hangPokeList[m_hangPokeCnt++] = newPokemon;
        }
        void TakeNewPokemon(IndivPokeClass.IndivPoke newPokemon) {
            System.out.printf("%s!!! 넌 내꺼야!!!\n", newPokemon.m_name);
            if (m_hangPokeCnt == MAX_HANG_POKE) {
                System.out.printf("들고 다닐 수 있는 포켓몬의 수를 넘어 %s(을)를 포켓몬 센터로 전송합니다.\n", newPokemon.m_name);
                m_pokeCenterList.add(newPokemon);
                return;
            }
            m_hangPokeList[m_hangPokeCnt++] = newPokemon;
        }

        void PrintHangPokeList() {
            System.out.println("현재 데리고 있는 포켓몬:");
            for (int i = 0; i < m_hangPokeCnt; i++) {
                m_hangPokeList[i].PrintThisPokemonDetailInfo();
            }
        }
        void PrintPokeCenterList() {
            System.out.println("센터에 있는 포켓몬:");
            int idx = 1;
            for (IndivPokeClass.IndivPoke indivPoke : m_pokeCenterList) {
                System.out.printf("[%d]\n", idx++);
                indivPoke.PrintThisPokemonDetailInfo();
            }
        }

        void GotoPokemonCenter() {
            System.out.printf("포켓몬 센터에 도착하였다.\n");
            boolean isInPokemonCenter = true;
            while (isInPokemonCenter) {
                System.out.printf("1. 포켓몬 회복\n");
                System.out.printf("2. 포켓몬 PC 보기\n");
                System.out.printf("0. 포켓몬 센터 나가기\n");

                System.out.printf("선택하십시오: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1: {
                        System.out.printf("\n포켓몬을 회복합니다.\n");
                        System.out.printf("띵-띵- 띠로링~!\n");
                        for (int i = 0; i < m_hangPokeCnt; i++) {
                            m_hangPokeList[i].FullRestore();
                        }
                        m_fightPokeIdx = 0;
                        System.out.printf("회복이 완료되었습니다\n\n");
                        break;
                    }
                    case 2: {
                        System.out.printf("\n포켓몬PC를 열었다.\n");
                        PrintPokeCenterList();

                        System.out.printf("1) 포켓몬 교체\n");
                        System.out.printf("2) 포켓몬 방생\n");
                        System.out.printf("0) 포켓몬 pc 끄기\n");

                        int choiceOption = sc.nextInt();
                        if(choiceOption == 1) {
                            System.out.printf("\n교체할 두 포켓몬의 인덱스를 입력하십시오.\n");
                            System.out.printf("포켓몬 센터에 있는 포켓몬의 인덱스: ");
                            int centerIdx = sc.nextInt();
                            centerIdx--;

                            for (int i = 0; i < m_hangPokeCnt; i++) {
                                System.out.printf("[%d]\n", i + 1);
                                m_hangPokeList[i].PrintThisPokemonDetailInfo();
                            }

                            System.out.printf("가지고 있는 포켓몬의 인덱스: ");
                            int hangingIdx = sc.nextInt();
                            hangingIdx--;

                            m_hangPokeList[hangingIdx].FullRestore();
                            IndivPokeClass.IndivPoke temp = m_hangPokeList[hangingIdx];
                            m_hangPokeList[hangingIdx] = m_pokeCenterList.get(centerIdx);
                            m_pokeCenterList.set(centerIdx, temp);

                            System.out.printf("포켓몬을 교환하였습니다.\n");
                        }
                        else if (choiceOption == 2) {
                            System.out.printf("\n방생할 포켓몬의 인덱스를 입력하십시오.\n");
                            System.out.printf("포켓몬 센터에 있는 포켓몬의 인덱스: ");
                            int centerIdx = sc.nextInt();
                            centerIdx--;
                            System.out.printf("잘가, %s!!\n", m_pokeCenterList.get(centerIdx).m_name);
                            m_pokeCenterList.remove(centerIdx);
                        }

                        System.out.printf("포켓몬PC를 종료합니다.\n");
                        break;
                    }

                    case 0:
                        System.out.printf("포켓몬 센터에서 나왔다.\n\n");
                        isInPokemonCenter = false;
                        break;
                }
            }
        }
        void GotoStore() {
            System.out.printf("상점에 도착하였다.\n");

            boolean isInStore = true;
            while (isInStore) {
                System.out.printf("캔디: %d개\n", m_candy);
                System.out.printf("1. 몬스터볼 구매\n");
                System.out.printf("2. 회복약 구매\n");
                System.out.printf("0. 상점에서 나가기\n");

                System.out.printf("선택하십시오: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1: {
                        for (int i = 0; i < m_balls.length; i++)
                            System.out.printf("%d) %s(%d): 사탕 %d개\n", i + 1, m_balls[i].m_name, m_potions[i].m_count, m_balls[i].m_cost);
                        System.out.printf("구매할 몬스터볼의 인덱스: ");
                        int idx = sc.nextInt();
                        idx--;
                        System.out.printf("구매할 개수: ");
                        int cnt = sc.nextInt();

                        int price = m_balls[idx].m_cost * cnt;
                        if (price > m_candy) {
                            System.out.printf("캔디의 수가 부족합니다.\n\n");
                            break;
                        }
                        System.out.printf("%s %d개를 구입하였습니다. %s: %d개\n", m_balls[idx].m_name, cnt, m_balls[idx].m_name, m_balls[idx].m_count + cnt);
                        m_balls[idx].m_count += cnt;
                        m_candy -= price;
                        break;
                    }
                    
                    case 2: {
                        for (int i = 0; i < m_potions.length; i++)
                            System.out.printf("%d) %s(%d): 사탕 %d개\n", i + 1, m_potions[i].m_name, m_potions[i].m_count, m_potions[i].m_cost);
                        System.out.printf("구매할 회복약의 인덱스: ");
                        int idx = sc.nextInt();
                        idx--;
                        System.out.printf("구매할 개수: ");
                        int cnt = sc.nextInt();

                        int price = m_potions[idx].m_cost * cnt;
                        if (price > m_candy) {
                            System.out.printf("캔디의 수가 부족합니다.\n\n");
                            break;
                        }
                        System.out.printf("%s %d개를 구입하였습니다. %s: %d개\n", m_potions[idx].m_name, cnt, m_potions[idx].m_name, m_potions[idx].m_count + cnt);
                        m_potions[idx].m_count += cnt;
                        m_candy -= price;
                        break;
                    }
                    case 0:
                        System.out.printf("상점에서 나왔다.\n\n");
                        isInStore = false;
                        break;
                }
            }
        }
        void ShowMonsterBallList() {
            int idx = 1;
            System.out.println();
            for (MonsterBall ball : m_balls)
                 System.out.printf("[%d] %s: %d개\n", idx++, ball.m_name, ball.m_count);
        }
        boolean IsCatchSuccess(int idx, IndivPokeClass.IndivPoke caughtPoke) {
            Random rd = new Random();
            final int rdConst = 65535;
            int a = (int)((3 * caughtPoke.m_stats.health - 2 * caughtPoke.m_currentHealth) * caughtPoke.m_tribe.m_catchRate * m_balls[idx].m_catchRate / (3 * caughtPoke.m_stats.health));
            int b = (int)(rdConst * Math.sqrt(Math.sqrt((double)a / 255)) + 0.5F); // 65535는 포켓몬에서 만든 랜덤 상수임.

            for (int i = 0; i < 3; i++) { // 4번 돌려서 하나라도 b보다 큰 값이 나오면 실패라 한다{
                System.out.printf("띵-\n");
                if (rd.nextInt(rdConst) > b)
                    return false;
            }
            return rd.nextInt(rdConst) <= b;
        }
        void ThrowMonsterBall(int idx, IndivPokeClass.IndivPoke caughtPoke) {
            System.out.printf("가랏! 몬스터~볼~~!!!\n");
            m_balls[idx].m_count--;
            if (IsCatchSuccess(idx, caughtPoke)) {
                System.out.printf("찰칵!\n");
                TakeNewPokemon(caughtPoke);
            }
            else {
                System.out. printf("펑!\n");
                System.out.printf("아 안돼..!\n");
                System.out.printf("%s(은)는 도망쳐 버렸다.\n", caughtPoke.m_tribe.m_name);
            }
        }
        void FeedCandy(IndivPokeClass.IndivPoke eatPoke) {
            m_candy--;
            eatPoke.GetExp(eatPoke.m_maxExp);
        }
        void ShowMonsterPotionList() {
            int idx = 1;
            System.out.println();
            for (Potion potion : m_potions)
                System.out.printf("%d. %s: %d개\n", idx++, potion.m_name, potion.m_count);
        }
        void GivePotion(int idx, IndivPokeClass.IndivPoke drinkingPoke) {
            m_potions[idx].m_count--;
            drinkingPoke.m_currentHealth += m_potions[idx].m_healValue;

            int healValue;
            if (m_potions[idx].m_healValue == -1)
                healValue = drinkingPoke.m_stats.health - drinkingPoke.m_currentHealth;
            else
                healValue = Math.min(m_potions[idx].m_healValue, drinkingPoke.m_stats.health - drinkingPoke.m_currentHealth);
            System.out.printf("%s의 피가 %d만큼 찼다.\n", drinkingPoke.m_name, healValue);
            System.out.printf("%s의 HP: %d -> ", drinkingPoke.m_name, drinkingPoke.m_currentHealth);
            drinkingPoke.m_currentHealth += healValue;
            System.out.printf("%d\n\n", drinkingPoke.m_currentHealth);

        }
        int OpenBag() {
            System.out.printf("1. 몬스터볼\n");
            System.out.printf("2. 회복약\n");
            System.out.printf("3. 캔디\n");
            System.out.printf("0. 가방 닫기\n");
            System.out.printf("선택하십시오: ");
            int choice = sc.nextInt();

            return choice;
        }
    }
}
