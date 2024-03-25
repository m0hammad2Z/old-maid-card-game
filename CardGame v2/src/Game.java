import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Game {
    private final List<Player> players;
    private Deck deck;
    private Deck discardPile;
    private boolean isRunning;
    private int currentPlayerIndex;
    private final int NUM_PLAYERS;
    private final ExecutorService threadPool;

    public Semaphore semaphore;

    public Game(int numPlayers) {
        players = new ArrayList<>();
        discardPile = new Deck();
        currentPlayerIndex = 0;
        deck = new Deck();
        isRunning = true;
        NUM_PLAYERS = numPlayers;
        threadPool = Executors.newFixedThreadPool(NUM_PLAYERS);
        semaphore = new Semaphore(1, true);
    }

    public void start() {
        initializeGame();
        dealCards();
        startPlayerThreads();
        endGame();
    }

    private void initializeGame() {
        deck.initialize();
        deck.shuffle();
        createPlayers();
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
            int randomPlayerIndex = Math.abs(new Random().nextInt() % players.size());
            players.get(randomPlayerIndex).addCard(deck.draw());
        }
    }

    private void startPlayerThreads() {
        players.forEach(threadPool::execute);
    }

    private void endGame() {
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        for (Player player : players) {
            if (player.getHand().isEmpty()) return player;
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
        if (currentPlayerIndex >= players.size())
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

