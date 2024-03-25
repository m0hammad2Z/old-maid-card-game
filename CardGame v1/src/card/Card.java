package card;

public class Card {
    private final Suit suit;
    private final Value value;

    public Card(Suit suit, Value value) {
        if (suit == null || value == null) {
            throw new IllegalArgumentException("Suit and value cannot be null");
        }
        this.suit = suit;
        this.value = value;
    }

    // Getters
    public Suit getSuit() {
        return suit;
    }

    public Value getValue() {
        return value;
    }

    // Returns true if the card matches the suit or value of the given card
    public boolean isMatchingPair(Card other) {
        if (other == null) {
            throw new IllegalArgumentException("Other card cannot be null");
        }

        if (this.value != other.value) {
            return false;
        }

        if ((this.suit == Suit.SPADES && other.suit == Suit.CLUBS) ||
                (this.suit == Suit.CLUBS && other.suit == Suit.SPADES)) {
            return true;
        }

        if ((this.suit == Suit.DIAMONDS && other.suit == Suit.HEARTS) ||
                (this.suit == Suit.HEARTS && other.suit == Suit.DIAMONDS)) {
            return true;
        }

        return false;
    }


    @Override
    public String toString() {
        return value + " of " + suit;
    }

}
