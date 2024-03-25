import card.*;

import java.util.*;
import java.util.concurrent.Semaphore;

public class Player extends Thread {
    private final List<Card> hand;
    private final int playerId;
    private final Game game;
    private static final Random random = new Random();


    public Player(int playerId, Game game) {
        this.playerId = playerId;
        this.game = game;
        this.hand = new ArrayList<>();

    }

    // Getters
    public List<Card> getHand() {
        return hand;
    }

    public int getPlayerId() {
        return playerId;
    }

    // Add a card to the player's hand
    public void addCard(Card card) {
        hand.add(card);
    }

    public boolean hasJoker() {
        return hand.stream().anyMatch(card -> card.getSuit() == Suit.JOKER);
    }

    // Removes a card from the player's hand
    private Card drawCard(Card card) {
        hand.remove(card);
        return card;
    }

    // Returns a card from the player's hand if it matches the given card
    private Card getMatchingCard(Card card) {
        return hand.stream()
                .filter(currentCard -> card.isMatchingPair(currentCard))
                .findFirst()
                .orElse(null);
    }

    // If the player has matching cards, they should be drawn
    private void discardAllMatchingCards() {
        List<Card> cardsToDiscard = new LinkedList<>();
        for (Card card : hand) {
            Card matching = getMatchingCard(card);
            if (matching != null) {
                cardsToDiscard.add(matching);
                game.getDiscardPile().add(card);
                game.getDiscardPile().add(matching);
            }
        }
        hand.removeAll(cardsToDiscard);
    }

    // Makes the player take a turn
    private void takeTurn(Player other) {
        if (other == null) throw new IllegalStateException("Player " + " is null");
        if (other.getHand().isEmpty()) return;

        // Draw a random card from the previous player's hand
        int randomIndex = random.nextInt(other.getHand().size());
        Card drawnCard = other.drawCard(other.getHand().get(randomIndex));

        Card matching = getMatchingCard(drawnCard);

        // Check for matching card, if there is one, add it to the discard pile and draw the card
        if (matching != null) {
            hand.remove(matching);
            game.getDiscardPile().add(matching);
            game.getDiscardPile().add(drawnCard);
            return;
        }
        addCard(drawnCard);
    }

    @Override
    public void run() {
        discardAllMatchingCards();

        while (game.isRunning()) {
            try {
                game.semaphore.acquire();
                if (game.getCurrentPlayer() != this) {
                    game.semaphore.release();
                    continue;
                }

                if (game.getWinner() != null) {
                    game.setRunning(false);
                    game.semaphore.release();
                    break;
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            takeTurn(game.getPreviousPlayer());
            System.out.println("Player " + playerId + " played a card, now has " + hand.size() + " cards");
            game.nextPlayer();
            game.semaphore.release();
        }
    }
}
