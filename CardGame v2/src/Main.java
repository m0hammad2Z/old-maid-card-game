public class Main {
    public static void main(String[] args) {
        System.out.println("Enter the number of players (2-6): ");
        int numPlayers = new java.util.Scanner(System.in).nextInt();

        if (numPlayers < 2 || numPlayers > 6) {
            System.out.println("Invalid number of players");
            return;
        }

        Game game = new Game(numPlayers);
        game.start();


        System.out.println("\nGame Finished\n");
        System.out.println("Player " + game.getWinner().getPlayerId() + " Won The Game");
        System.out.println("Loser: " + game.getLoser().getPlayerId() + " Lost The Game\n");

        System.out.println("Players Hand:");
        for (Player player : game.getPlayers()) {
            System.out.println("Player " + player.getPlayerId() + " Hand: " + player.getHand());
        }
    }
}