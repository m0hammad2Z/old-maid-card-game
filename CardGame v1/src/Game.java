import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Game {
    private final List<Player> players;
    private Player winner;
    private Deck deck;
    private Deck discardPile;
    private boolean isRunning;
    private int currentPlayerIndex;
    private final int NUM_PLAYERS;

    public Game(int numPlayers) {
        players = new ArrayList<>();
        discardPile = new Deck();
        currentPlayerIndex = 0;
        deck = new Deck();
        isRunning = true;
        NUM_PLAYERS = numPlayers;
    }

    public void start() {
        deck.initialize();
        deck.shuffle();
        createPlayers();
        dealCards();
        startPlayerThreads();
        endGame();
    }

    private void createPlayers() {
        for (int i = 0; i < NUM_PLAYERS; i++) {
            Player player = new Player(i, this);
            players.add(player);
        }
    }

    private void dealCards() {
        int cardsPerPlayer = deck.getCards().size() / players.size();
        for (Player player : players) {
            for (int i = 0; i < cardsPerPlayer; i++) {
                player.addCard(deck.draw());
            }
        }
        distributeRemainingCards();
    }

    private void distributeRemainingCards() {
        while (!deck.getCards().isEmpty()) {
            int randomPlayerIndex = Math.abs(new Random().nextInt(players.size()));
            players.get(randomPlayerIndex).addCard(deck.draw());
        }
    }

    private void startPlayerThreads() {
        for (Player player : players) {
            player.start();
        }
    }

    private void endGame() {
        for (Player player : players) {
            try {
                player.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    // Getters
    public List<Player> getPlayers() {
        return players;
    }

    public Deck getDeck() {
        if (deck == null) {
            deck = new Deck();
        }
        return deck;
    }

    public Deck getDiscardPile() {
        if (discardPile == null) {
            discardPile = new Deck();
        }
        return discardPile;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public Player getWinner() {
        if(winner != null) return winner;
        for (Player player : players) {
            if (player.getHand().isEmpty()) {
                winner = player;
                return player;
            }
        }
        return null;
    }

    // Return the loser (Who has the Joker)
    public Player getLoser() {
        for (Player player : players) {
            if (player.hasJoker()) return player;
        }
        return null;
    }

    // Update the turn to the next player
    public void nextPlayer() {
        if(currentPlayerIndex >= players.size())
            currentPlayerIndex = 0;
        else
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Player getPreviousPlayer() {
        if(currentPlayerIndex == 0)
            return players.get(players.size() - 1);
        else
            return players.get(currentPlayerIndex - 1);
    }

}

