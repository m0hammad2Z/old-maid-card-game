import card.*;
import java.util.*;
import java.util.concurrent.*;

public class Deck {
    private final ConcurrentLinkedQueue<Card> cards;
    public Deck() {
        // Thread safe list of cards
        cards = new ConcurrentLinkedQueue<>();
    }

    // getters
    public ConcurrentLinkedQueue<Card> getCards() {
        return cards;
    }


    // Initializes the deck with 53 cards
    public void initialize() {
        cards.clear();
        for (Suit suit : Suit.values()) {
            if (suit == Suit.JOKER) {
                continue;
            }
            for (Value value : Value.values()) {
                if (value == Value.JOKER) {
                    continue;
                }
                cards.add(new Card(suit, value));
            }
        }
        cards.add(new Card(Suit.JOKER, Value.JOKER));
    }


    // Mixes up the cards in the deck
    public void shuffle() {
        List<Card> cardList = new ArrayList<>(cards);
        Collections.shuffle(cardList);
        cards.clear();
        cards.addAll(cardList);
    }

    // Draws a card from the deck
    public Card draw() {
        if(cards.isEmpty())
            throw new NoSuchElementException("Deck is empty");

        return cards.poll();
    }

    // Adds a card to the deck
    public void add(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
        cards.add(card);
    }

}
