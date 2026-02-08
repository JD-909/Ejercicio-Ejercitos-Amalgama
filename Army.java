import java.util.ArrayList;

public class Army {

    enum Civilization {
        CHINISE, ENGLISH, BYZANTINE
    }

    private Civilization civilization;
    private int gold;
    private ArrayList<Soldier> soldiers;
    private ArrayList<BattleLog> battleHistory;

    private class Soldier {
        enum Type {
            PYKEMAN, ARCHER, KNIGHT
        }

        private Type type;
        private int age;
        private int strength;

        // To construct a soldier, enter the type. The age is set to 20 and the initial strength is determined by the type.
        public Soldier(Type type) {
            this.type = type;
            this.age = 20;
            switch (type) {
                case PYKEMAN:
                    this.strength = 5;
                    break;
                case ARCHER:
                    this.strength = 10;
                    break;
                case KNIGHT:
                    this.strength = 20;
                    break;
            }
        }

        public Type getType() {
            return type;
        }

        public int getAge() {
            return age;
        }

        public int getStrength() {
            return strength;
        }

        public void train() {
            switch (type) {
                case PYKEMAN:
                    strength += 3;
                    break;
                case ARCHER:
                    strength += 7;
                    break;
                case KNIGHT:
                    strength += 10;
                    break;
            }
        }

        public void upgrade() {
            switch (type) {
                case PYKEMAN:
                    this.type = Type.ARCHER;
                    break;
                case ARCHER:
                    this.type = Type.KNIGHT;
                    break;
            }
        }
    }

    private class BattleLog {

        enum Result {
            WIN, LOSE, DRAW
        }

        private Civilization opponent;
        private Result result;

        public BattleLog(Civilization opponent, Result result) {
            this.opponent = opponent;
            this.result = result;
        }
    }

    public Army(Civilization civilization) {
        this.civilization = civilization;
        this.gold = 1000;                                                       // Initial gold
        this.soldiers = new ArrayList<>();
        this.battleHistory = new ArrayList<>();

        int pykeAmount = 0;
        int archAmount = 0;
        int knightAmount = 0;

        // Check units amount based on civilization.
        switch (civilization) {
            case CHINISE:
                pykeAmount = 2;
                archAmount = 25;
                knightAmount = 2;
                break;
            case ENGLISH:
                pykeAmount = 10;
                archAmount = 10;
                knightAmount = 10;
                break;
            case BYZANTINE:
                pykeAmount = 5;
                archAmount = 8;
                knightAmount = 15;
                break;
        }

        // Add the proper amount of units.
        for (int i = 0; i < pykeAmount; i++) {
            soldiers.add(new Soldier(Soldier.Type.PYKEMAN));
        }
        for (int i = 0; i < archAmount; i++) {
            soldiers.add(new Soldier(Soldier.Type.ARCHER));
        }
        for (int i = 0; i < knightAmount; i++) {
            soldiers.add(new Soldier(Soldier.Type.KNIGHT));
        }
    }

    public void buyTraining(Soldier soldier) {
        int cost = 0;

        switch (soldier.getType()) {
            case PYKEMAN:
                cost = 10;
                break;
            case ARCHER:
                cost = 20;
                break;
            case KNIGHT:
                cost = 30;
                break;
        }

        if (gold >= cost) {
            gold -= cost;
            soldier.train();
        } else {
            System.out.println("Not enough gold to train the soldier.");
        }
    }

    public void buyUpgrade(Soldier soldier) {
        int cost;

        switch (soldier.getType()) {
            case PYKEMAN:
                cost = 30;
                break;
            case ARCHER:
                cost = 40;
                break;
            default:
                System.out.println("Knights cannot be upgraded.");
                return;
        }

        if (gold >= cost) {
            gold -= cost;
            soldier.upgrade();
        } else {
            System.out.println("Not enough gold to upgrade the soldier.");
        }
    }

    // To attack, choose an opponent.
    public void attack(Army opponent) {
        int myStrength = 0;
        int opponentStrength = 0;

        for (Soldier soldier : soldiers) {
            myStrength += soldier.getStrength();                                // Add each soldier's strength to the army's
        }

        for (Soldier soldier : opponent.soldiers) {
            opponentStrength += soldier.getStrength();                          // Idem
        }

        // Check strength, update battle histories and deliver consequences.
        if (myStrength > opponentStrength) {

            battleHistory.add(new BattleLog(opponent.civilization, BattleLog.Result.WIN));
            gold += 100;                                                        // Winner earns 100 gold

            opponent.battleHistory.add(new BattleLog(this.civilization, BattleLog.Result.LOSE));
            opponent.loseStrongestSoldier();
            opponent.loseStrongestSoldier();                                    // Loser loses their 2 strongest soldiers
        } else if (myStrength < opponentStrength) {

            battleHistory.add(new BattleLog(opponent.civilization, BattleLog.Result.LOSE));
            opponent.loseStrongestSoldier();
            opponent.loseStrongestSoldier();                                    // Loser loses their 2 strongest soldiers

            opponent.battleHistory.add(new BattleLog(this.civilization, BattleLog.Result.WIN));
            opponent.gold += 100;                                               // Winner earns 100 gold
        } else {
            battleHistory.add(new BattleLog(opponent.civilization, BattleLog.Result.DRAW));
            opponent.battleHistory.add(new BattleLog(this.civilization, BattleLog.Result.DRAW));
            loseStrongestSoldier();                                             // Both lose their strongest soldier
            opponent.loseStrongestSoldier();
        }
    }

    // To remove the strongest soldier from an army.
    public void loseStrongestSoldier() {
        int positionOfStrongest = -1;

        for (int i = 0; i < soldiers.size(); i++) {
            if (positionOfStrongest == -1 || soldiers.get(i).getStrength() > soldiers.get(positionOfStrongest).getStrength()) {
                positionOfStrongest = i;
            }
        }

        if (positionOfStrongest != -1) {
            soldiers.remove(positionOfStrongest);
        }
    }
}
