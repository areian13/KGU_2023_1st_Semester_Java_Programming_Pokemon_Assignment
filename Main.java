import java.io.*;
import java.util.Scanner;
import java.util.Random;
import java.util.StringTokenizer;

public class Main {
    public static TrainerClass.Trainer player = new TrainerClass.Trainer();
    static Scanner sc = new Scanner(System.in);

    static void ShowHangingPokemon() {
        for (int i = 0; i < player.m_hangPokeCnt; i++) {
            IndivPokeClass.IndivPoke p = new IndivPokeClass.IndivPoke(player.m_hangPokeList[i]);

            System.out.printf("%d: ", i + 1);
            if (!p.IsEmpty()) {
                System.out.printf("%s [ 전투 %s ]\n", p.m_name, !p.IsDead() ? "가능" : "불능");
                System.out.printf("Lv: %d\n", p.m_level);
                System.out.printf("HP: %d / %d\n", p.m_currentHealth, p.m_stats.health);}
            else
                System.out.printf("지정된 포켓몬이 없습니다.\n");
            System.out.printf("\n");
        }
    }
    static void SwapfightPoke(IndivPokeClass.IndivPoke fightPoke) {
        player.m_hangPokeList[player.m_fightPokeIdx].ReassignPoke(fightPoke);
        ShowHangingPokemon();

        System.out.printf("교체할 포켓몬을 선택해주십시오: ");
        while (true) {
            int choice = sc.nextInt();
            choice--; // 눈에 보이는 번호와 실제 인덱스는 1차이
            if(player.m_hangPokeList[choice].IsEmpty())
                System.out.printf("지정된 포켓몬이 없습니다. 다시 선택해주세요.\n");
            else if (player.m_hangPokeList[choice].IsDead())
                System.out.printf("전투 불능인 포켓몬입니다. 다시 선택해주세요.\n");
            else if (choice == player.m_fightPokeIdx)
                System.out.printf("현재 전투 중인 포켓몬입니다. 다시 선택해주세요.\n");
            else {
                System.out.printf("수고했어 %s.. 이제 쉬어...\n", fightPoke.m_name);
                player.m_fightPokeIdx = choice;
                fightPoke.ReassignPoke(player.m_hangPokeList[choice]);
                System.out.printf("가랏 %s!!\n", fightPoke.m_name);
                break;
            }
        }
    }
    static int MyAttackAndCheckWhileCaptureMode(IndivPokeClass.IndivPoke fightPoke, IndivPokeClass.IndivPoke enemyPoke) {
        System.out.printf("가랏 %s!!! 공격해!!!\n", fightPoke.m_name);
        fightPoke.Attack(enemyPoke);
        if (enemyPoke.m_currentHealth <= 0) {
            enemyPoke.m_currentHealth = 0;
            System.out.printf("%s(을)를 물리쳤다\n", enemyPoke.m_tribe.m_name);
            int getExp = enemyPoke.m_tribe.m_baseExpYield * enemyPoke.m_level / 7;
            fightPoke.GetExp(getExp);
            player.m_hangPokeList[player.m_fightPokeIdx].ReassignPoke(fightPoke);
            return 0;
        }
        return 1;
    }
    static int EnemyAttackAndCheckWhileCaptureMode(IndivPokeClass.IndivPoke fightPoke, IndivPokeClass.IndivPoke enemyPoke) {
        System.out.printf("적 %s(이)가 %s(을)를 공격했다..!\n", enemyPoke.m_tribe.m_name, fightPoke.m_name);
        enemyPoke.Attack(fightPoke);

        if (fightPoke.m_currentHealth <= 0) {
            fightPoke.m_currentHealth = 0;
            player.m_hangPokeList[player.m_fightPokeIdx].ReassignPoke(fightPoke);
            System.out.printf("%s(이)가 쓰러졌다..\n", fightPoke.m_name);

            if (player.IsAllDead()) {
                System.out.printf("더 이상 싸울 수 있는 포켓몬이 없다..\n");
                System.out.printf("당신은 눈 앞이 깜깜해져 정신 없이 도망치며 포켓몬 센터로 향했다...\n");

                return 0;
            }

            System.out.printf("포켓몬을 교체하시겠습니까?\n");
            System.out.printf("1. 교체\n");
            System.out.printf("2. 도망\n");

            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    SwapfightPoke(fightPoke);
                    return 2;

                case 2:
                    System.out.printf("당신은 비겁하게 도망쳤습니다..\n");
                    player.m_hangPokeList[player.m_fightPokeIdx].ReassignPoke(fightPoke);
                    for (int i = 0; i < player.m_hangPokeCnt; i++) {
                        if (!player.m_hangPokeList[i].IsDead()) {
                            player.m_fightPokeIdx = i;
                            break;
                        }
                    }
                    return 0;
            }
        }
        return 1;
    }

    static void FightMode() {
        String[] namedTrainer = { "웅", "이슬", "마티스", "민화", "독수", "초련", "강연", "비주기", "레드", "그린"};

        Random rd = new Random();

        String enemyName = namedTrainer[rd.nextInt(namedTrainer.length)];
        System.out.printf("트레이너 %s(이)가 나타났다\n", enemyName);

        TrainerClass.Trainer enemyTrainer = new TrainerClass.Trainer();
        enemyTrainer.m_hangPokeCnt = 3;
        for (int i = 0; i < 3; i++) {
            int enemyPokeDicNum = rd.nextInt(PokeTribeClass.NUM_OF_POKE) + 1;
            int enemyPokeLevel = player.m_hangPokeList[0].m_level;
            enemyTrainer.m_hangPokeList[i] = new IndivPokeClass.IndivPoke(enemyPokeDicNum, enemyPokeLevel);
        }
        enemyTrainer.m_fightPokeIdx = 0;

        IndivPokeClass.IndivPoke fightPoke = new IndivPokeClass.IndivPoke(player.m_hangPokeList[player.m_fightPokeIdx]);
        boolean isFirst = true;
        boolean isAway = false;
        while (true) {
            IndivPokeClass.IndivPoke enemyPoke = new IndivPokeClass.IndivPoke(enemyTrainer.m_hangPokeList[enemyTrainer.m_fightPokeIdx]);
            System.out.printf("%s(이)가 %s(을)를 꺼냈다.\n", enemyName, enemyPoke.m_name);
            if (isFirst)
            {
                System.out.printf("가랏 %s!!\n", fightPoke.m_name);
                isFirst = false;
            }

            int battleModeNum = 1;
            while (battleModeNum != 0) {
                System.out.printf("상대 %s(Lv.%d)의 HP: %d / %d\n", enemyPoke.m_tribe.m_name, enemyPoke.m_level, enemyPoke.m_currentHealth, enemyPoke.m_stats.health);
                System.out.printf("내 %s(Lv.%d)의 HP: %d / %d\n", fightPoke.m_name, fightPoke.m_level, fightPoke.m_currentHealth, fightPoke.m_stats.health);
                System.out.printf("1. 공격\n");
                System.out.printf("2. 가방\n");
                System.out.printf("3. 도망\n");
                System.out.printf("4. 교체\n");

                int choice = sc.nextInt();
                switch (choice) {
                    case 1: { // 공격: 스피드에 따라 누가 먼저 공격하냐가 달라지므로 구별
                        boolean isFirstAttack = fightPoke.m_stats.speed >= enemyPoke.m_stats.speed;
                        if (isFirstAttack) {
                            battleModeNum = MyAttackAndCheckWhileCaptureMode(fightPoke, enemyPoke);
                            if (battleModeNum == 0) {
                                if (enemyTrainer.m_fightPokeIdx < TrainerClass.MAX_HANG_POKE) {
                                    enemyTrainer.m_fightPokeIdx++;
                                }
                                break;
                            }
                        }

                        battleModeNum = EnemyAttackAndCheckWhileCaptureMode(fightPoke, enemyPoke);
                        if (battleModeNum == 0)
                        {
                            isAway = true;
                            break;
                        }

                        if (battleModeNum == 2) { // 상대가 선빵 때려서 죽었을 때, 교체를 선택하면 교체 자체가 하나의 턴을 소모하므로 바로 내턴이 종료된다.
                            battleModeNum = 1;
                            break;
                        }

                        if (!isFirstAttack) {
                            battleModeNum = MyAttackAndCheckWhileCaptureMode(fightPoke, enemyPoke);
                            if (battleModeNum == 0) {
                                if (enemyTrainer.m_fightPokeIdx < TrainerClass.MAX_HANG_POKE) {
                                    enemyTrainer.m_fightPokeIdx++;
                                }
                                break;
                            }
                        }
                        break;
                    }
                    case 2: {
                        boolean isOpeningBag = true;
                        while (isOpeningBag) {
                            int choiceOption = player.OpenBag();
                            if (choiceOption == 0)
                                break;

                            // 몬스터볼 던지기
                            if (choiceOption == 1) {
                                System.out.printf("트레이너의 포켓몬은 포획할 수 없습니다. 다시 선택하십시오.\n");
                                break;
                            }
                            // 회복약 먹이기
                            else if (choiceOption == 2) {
                                boolean isSelecting = true;
                                while (isSelecting) {
                                    player.ShowMonsterPotionList();
                                    System.out.printf("어떤 포션을 먹이겠습니까?(뒤로가기: 0): ");
                                    int potionIdx = sc.nextInt();
                                    if (potionIdx == 0)
                                        break;

                                    potionIdx--;

                                    if (player.m_potions[potionIdx].m_count <= 0) {
                                        System.out.printf("포션의 개수가 부족합니다. 다시 선택하십시오.\n");
                                        continue;
                                    }
                                    player.GivePotion(potionIdx, fightPoke);
                                    battleModeNum = EnemyAttackAndCheckWhileCaptureMode(fightPoke, enemyPoke);
                                    isOpeningBag = false;
                                    break;
                                }
                            }
                            else if (choiceOption == 3) {

                                if (player.m_candy <= 0) {
                                    System.out.printf("캔디의 개수가 부족합니다. 다시 선택하십시오.\n");
                                    continue;
                                }

                                if (fightPoke.m_level > player.m_trainerLevel) {
                                    System.out.printf("포켓몬의 레벨(Lv.%d)이 플레이어의 레벨(Lv.%d)보다 높습니다. 다시 선택하십시오.\n", fightPoke.m_level, player.m_trainerLevel);
                                    continue;
                                }
                                player.FeedCandy(fightPoke);
                                battleModeNum = EnemyAttackAndCheckWhileCaptureMode(fightPoke, enemyPoke);
                                isOpeningBag = false;
                                break;
                            }
                        }
                        break;
                    }
                    case 3: { // 포기
                        System.out.printf("당신은 비겁하게 도망쳤습니다..\n");
                        player.m_hangPokeList[player.m_fightPokeIdx].ReassignPoke(fightPoke);
                        battleModeNum = 0;
                        isAway = true;
                        break;
                    }
                    case 4: { // 교체: 교체 후 턴이 적에게 넘어가기 때문에 공격을 한대 맞는다.
                        SwapfightPoke(fightPoke);
                        battleModeNum = EnemyAttackAndCheckWhileCaptureMode(fightPoke, enemyPoke);
                        break;
                    }
                }
            }

            // 적 포켓몬이 다 죽었으면 == 내가 이겼으면
            if (enemyTrainer.m_fightPokeIdx == TrainerClass.MAX_HANG_POKE) {
                System.out.printf("%s(으)로부터 승리했다!\n", enemyName);
                System.out.printf("플레이어의 레벨이 +3 상승했습니다!\n");
                System.out.printf("승리 보상으로 캔디를 다섯개 받았다!\n");
                player.m_trainerLevel += 3;
                player.m_candy += 5;
                break;
            }
            else if(player.IsAllDead()) {
                System.out.printf("당신은 패배의 대가로 사탕 %d개를 %s에게 지불하였습니다.\n", Math.min(5, player.m_candy), enemyName);
                player.m_candy -= Math.min(5, player.m_candy);
                break;
            }
            else if (isAway) {
                break;
            }
        }
    }
    static void CaptureMode() {
        Random rd = new Random();

        IndivPokeClass.IndivPoke fightPoke = new IndivPokeClass.IndivPoke(player.m_hangPokeList[player.m_fightPokeIdx]);

        int enemyPokeLevel = fightPoke.m_level;
        int enemyPokeDicNum = rd.nextInt(PokeTribeClass.NUM_OF_POKE) + 1;
        IndivPokeClass.IndivPoke enemyPoke = new IndivPokeClass.IndivPoke(enemyPokeDicNum, enemyPokeLevel);
        System.out.printf("야생의 %s(이)가 나타났다!\n", enemyPoke.m_name);
        System.out.printf("가랏 %s!!\n", fightPoke.m_name);

        // 1은 현재 수집 모드를 진행중, 0은 진행 종료
        // 근데 왜 int냐? 포켓몬을 교체하거나 밥멕이면 상대한테 턴이 넘어가야되는데 교체했다는걸 2로 표시하기 위해서 int 사용
        int captureModeNum = 1;
        while (captureModeNum != 0) {
            System.out.printf("상대 %s(Lv.%d)의 HP: %d / %d\n", enemyPoke.m_tribe.m_name, enemyPokeLevel, enemyPoke.m_currentHealth, enemyPoke.m_stats.health);
            System.out.printf("내 %s(Lv.%d)의 HP: %d / %d\n", fightPoke.m_name, fightPoke.m_level, fightPoke.m_currentHealth, fightPoke.m_stats.health);
            System.out.printf("1. 공격\n");
            System.out.printf("2. 가방\n");
            System.out.printf("3. 도망\n");
            System.out.printf("4. 교체\n");

            int choice = sc.nextInt();
            switch (choice) {
                case 1: { // 공격: 스피드에 따라 누가 먼저 공격하냐가 달라지므로 구별
                    boolean isFirstAttack = fightPoke.m_stats.speed >= enemyPoke.m_stats.speed;
                    if (isFirstAttack) {
                        captureModeNum = MyAttackAndCheckWhileCaptureMode(fightPoke, enemyPoke);
                        if (captureModeNum == 0) {
                            System.out.printf("플레이어의 레벨이 +1 상승했습니다!\n");
                            player.m_trainerLevel++;
                            break;
                        }
                    }

                    captureModeNum = EnemyAttackAndCheckWhileCaptureMode(fightPoke, enemyPoke);
                    if (captureModeNum == 0)
                        break;

                    if (captureModeNum == 2) { // 상대가 선빵 때려서 죽었을 때, 교체를 선택하면 교체 자체가 하나의 턴을 소모하므로 바로 내턴이 종료된다.
                        captureModeNum = 1;
                        break;
                    }

                    if (!isFirstAttack) {
                        captureModeNum = MyAttackAndCheckWhileCaptureMode(fightPoke, enemyPoke);
                        if (captureModeNum == 0) {
                            System.out.printf("플레이어의 레벨이 +1 상승했습니다!\n");
                            player.m_trainerLevel++;
                            break;
                        }
                    }
                    break;
                }
                case 2: {
                    boolean isOpeningBag = true;
                    while (isOpeningBag) {
                        int choiceOption = player.OpenBag();
                        if (choiceOption == 0)
                            break;

                        // 몬스터볼 던지기
                        if (choiceOption == 1) {
                            if (enemyPoke.m_level > player.m_trainerLevel) {
                                System.out.printf("%s의 레벨(Lv.%d)이 플레이어의 레벨(Lv.%d)보다 높아 포획할 수 없습니다.\n", enemyPoke.m_name, enemyPoke.m_level, player.m_trainerLevel);
                                break;
                            }
                            boolean isSelecting = true;
                            while (isSelecting) {
                                player.ShowMonsterBallList();
                                System.out.printf("어떤 볼을 던지겠습니까?(뒤로가기: 0): ");
                                int ballIdx = sc.nextInt();
                                if (ballIdx == 0)
                                    break;

                                ballIdx--;

                                if (player.m_balls[ballIdx].m_count <= 0) {
                                    System.out.printf("볼의 개수가 부족합니다. 다시 선택하십시오.\n");
                                    continue;
                                }
                                player.ThrowMonsterBall(ballIdx, enemyPoke);
                                isOpeningBag = false;
                                captureModeNum = 0;
                                break;
                            }
                        }
                        // 회복약 먹이기
                        else if (choiceOption == 2) {
                            boolean isSelecting = true;
                            while (isSelecting) {
                                player.ShowMonsterPotionList();
                                System.out.printf("어떤 포션을 먹이겠습니까?(뒤로가기: 0): ");
                                int potionIdx = sc.nextInt();
                                if (potionIdx == 0)
                                    break;

                                potionIdx--;

                                if (player.m_potions[potionIdx].m_count <= 0) {
                                    System.out.printf("포션의 개수가 부족합니다. 다시 선택하십시오.\n");
                                    continue;
                                }
                                player.GivePotion(potionIdx, fightPoke);
                                captureModeNum = EnemyAttackAndCheckWhileCaptureMode(fightPoke, enemyPoke);
                                isOpeningBag = false;
                                break;
                            }
                        }
                        // 캔디 먹이기
                        else if (choiceOption == 3) {
                            if (player.m_candy <= 0) {
                                System.out.printf("캔디의 개수가 부족합니다. 다시 선택하십시오.\n");
                                continue;
                            }
                            if (fightPoke.m_level > player.m_trainerLevel) {
                                System.out.printf("포켓몬의 레벨(Lv.%d)이 플레이어의 레벨(Lv.%d)보다 높습니다. 다시 선택하십시오.\n", fightPoke.m_level, player.m_trainerLevel);
                                continue;
                            }
                            player.FeedCandy(fightPoke);
                            captureModeNum = EnemyAttackAndCheckWhileCaptureMode(fightPoke, enemyPoke);
                            isOpeningBag = false;
                            break;
                        }
                    }
                    break;
                }
                case 3: { // 포기
                    System.out.printf("당신은 비겁하게 도망쳤습니다..\n");
                    player.m_hangPokeList[player.m_fightPokeIdx].ReassignPoke(fightPoke);
                    captureModeNum = 0;
                    break;
                }
                case 4: { // 교체: 교체 후 턴이 적에게 넘어가기 때문에 공격을 한대 맞는다.
                    SwapfightPoke(fightPoke);
                    captureModeNum = EnemyAttackAndCheckWhileCaptureMode(fightPoke, enemyPoke);
                    break;
                }
            }
        }
    }
    static void BagMode() {
        boolean isOpenBag = true;
        while (isOpenBag) {
            System.out.printf("1. 내 포켓몬\n");
            System.out.printf("2. 몬스터볼\n");
            System.out.printf("3. 치료제\n");
            System.out.printf("4. 캔디 사용하기\n");
            System.out.printf("0. 가방 닫기\n");

            int choice = sc.nextInt();
            switch (choice) {
                case 1: {
                    player.PrintHangPokeList();
                    break;
                }
                case 2: {
                    player.ShowMonsterBallList();
                    break;
                }
                case 3: {
                    player.ShowMonsterPotionList();
                    break;
                }
                case 4: {
                    System.out.printf("사탕의 개수: %d\n", player.m_candy);
                    if (player.m_candy <= 0) {
                        System.out.printf("캔디의 개수가 부족합니다. 다시 선택하십시오.\n");
                        continue;
                    }
                    System.out.printf("캔디를 줄 포켓몬을 선택하십시오.\n");
                    for (int i = 0; i < player.m_hangPokeCnt; i++)
                        System.out.printf("%d) %s(Lv.%d)\n", i + 1, player.m_hangPokeList[i].m_name, player.m_hangPokeList[i].m_level);
                    int feedPokeIdx = sc.nextInt();
                    feedPokeIdx--;

                    if (player.m_hangPokeList[feedPokeIdx].m_level > player.m_trainerLevel) {
                        System.out.printf("포켓몬의 레벨(Lv.%d)이 플레이어의 레벨(Lv.%d)보다 높습니다. 다시 선택하십시오.\n", player.m_hangPokeList[feedPokeIdx].m_level, player.m_trainerLevel);
                        continue;
                    }
                    player.FeedCandy(player.m_hangPokeList[feedPokeIdx]);
                    break;
                }
                case 0: {
                    isOpenBag = false;
                    break;
                }
            }
        }
    }

    static void LoadFile() throws IOException {
        final String path = System.getProperty("user.dir") + "/src/" + "player info.txt";

        if (new File(path).exists()) {
            FileInputStream FIS = new FileInputStream(path);
            InputStreamReader ISR = new InputStreamReader(FIS, "utf-8");
            BufferedReader BR = new BufferedReader(ISR);

            player.m_trainerLevel = Integer.parseInt(BR.readLine());
            player.m_hangPokeCnt = Integer.parseInt(BR.readLine());
            //System.out.printf("%d %d\n",player.m_trainerLevel,player.m_hangPokeCnt);
            for (int i = 0; i < player.m_hangPokeCnt; i++) {
                String line = BR.readLine();
                StringTokenizer ST = new StringTokenizer(line, " ");

                player.m_hangPokeList[i] = new IndivPokeClass.IndivPoke(PokeTribeClass.PokeDicNum.valueOf(ST.nextToken()).ordinal(), Integer.parseInt(ST.nextToken()));
                player.m_hangPokeList[i].m_currentHealth = Math.min(Integer.parseInt(ST.nextToken()), player.m_hangPokeList[i].m_stats.health);
                player.m_hangPokeList[i].m_exp = Math.min(Integer.parseInt(ST.nextToken()), player.m_hangPokeList[i].m_maxExp);
                // player.m_hangPokeList[i].PrintThisPokemonDetailInfo();
            }
            int pokeCenterCnt = Integer.parseInt(BR.readLine());
            for (int i = 0; i < pokeCenterCnt; i++) {
                String line = BR.readLine();
                StringTokenizer ST = new StringTokenizer(line, " ");

                player.m_pokeCenterList.add(new IndivPokeClass.IndivPoke(PokeTribeClass.PokeDicNum.valueOf(ST.nextToken()).ordinal(), Integer.parseInt(ST.nextToken())));
                player.m_pokeCenterList.get(i).m_exp = Math.min(Integer.parseInt(ST.nextToken()), player.m_pokeCenterList.get(i).m_maxExp);
            }
            String line = BR.readLine();
            StringTokenizer ST = new StringTokenizer(line, " ");
            for (int i = 0; i < player.m_potions.length; i++)
                player.m_potions[i].m_count = Integer.parseInt(ST.nextToken());
            line = BR.readLine();
            ST = new StringTokenizer(line, " ");
            for (int i = 0; i < player.m_balls.length; i++)
                player.m_balls[i].m_count = Integer.parseInt(ST.nextToken());
            player.m_candy = Integer.parseInt(BR.readLine());
        }
        // 만약 게임을 처음 실행한다면(= 세이브파일이 존재하지 않는다면) 포켓몬 선택 실행
        else {
            System.out.printf("오박사: 무슨 포켓몬을 얻고 싶니?\n");
            System.out.printf("1. 이상해씨\n");
            System.out.printf("2. 파이리\n");
            System.out.printf("3. 꼬부기\n");

            int choice = sc.nextInt();
            player.TakeNewPokemon((choice - 1) * 3 + 1);
        }
    }
    static void PlayGame() {
        while (true) {
            System.out.printf("플레이어의 레벨: %d\n", player.m_trainerLevel);
            System.out.printf("1. 대전 모드    4. 포켓몬 센터 가기\n");
            System.out.printf("2. 수집 모드    5. 상점 가기\n");
            System.out.printf("3. 가방 보기    0. 게임 종료\n");

            int choice = sc.nextInt();
            switch (choice) {
                // 대전모드
                case 1: {
                    if(!player.IsAllDead())
                        FightMode();
                    else
                        System.out.println("싸울 수 있는 포켓몬이 없어 대전모드를 실행할 수 없습니다.");
                    break;
                }
                // 수집모드
                case 2: {
                    if(!player.IsAllDead())
                        CaptureMode();
                    else
                        System.out.println("싸울 수 있는 포켓몬이 없어 수집모드를 실행할 수 없습니다.");
                    break;
                }
                // 가방 보기
                case 3: {
                    BagMode();
                    break;
                }
                // 포켓몬 센터 가기
                case 4: {
                    player.GotoPokemonCenter();
                    break;
                }
                // 상점 가기
                case 5: {
                    player.GotoStore();
                    break;
                }
                case 0: {
                    System.out.printf("게임을 종료합니다.");
                    return;
                }
            }
        }
    }
    static void SaveFile() throws IOException {
        final String path = System.getProperty("user.dir") + "/src/" + "player info.txt";
        String data = String.format("%d\n", player.m_trainerLevel);
        data += String.format("%d\n", player.m_hangPokeCnt);
        for (int i = 0; i < player.m_hangPokeCnt; i++)
            data += String.format("%s %d %d %d\n", player.m_hangPokeList[i].m_name, player.m_hangPokeList[i].m_level, player.m_hangPokeList[i].m_currentHealth, player.m_hangPokeList[i].m_exp);
        data += String.format("%d\n", player.m_pokeCenterList.size());
        for (int i = 0; i < player.m_pokeCenterList.size(); i++)
            data += String.format("%s %d %d\n", player.m_pokeCenterList.get(i).m_name, player.m_pokeCenterList.get(i).m_level, player.m_pokeCenterList.get(i).m_exp);
        for (int i = 0; i < player.m_potions.length; i++) 
            data += String.format("%d ",player.m_potions[i].m_count);
        data += "\n";
        for (int i = 0; i < player.m_balls.length; i++)
            data += String.format("%d ",player.m_balls[i].m_count);
        data += "\n";
        data += player.m_candy;
        
        FileWriter w = new FileWriter(path);
        w.write(data);
        w.close();
    }
    static void FinishProgram() {
        sc.close();
    }

    public static void main(String[] args) throws IOException {
        String a = "asdf";
        String b = "asdf";
        b.getBytes(0) = 'c';
        System.out.println(a);
        System.out.println(b);
//        LoadFile();
//        PlayGame();
//        SaveFile();
//        FinishProgram();
    }
}

