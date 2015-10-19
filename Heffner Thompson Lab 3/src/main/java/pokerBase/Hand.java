package pokerBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;

import pokerEnums.eCardNo;
import pokerEnums.eHandStrength;
import pokerEnums.eRank;
import pokerEnums.eSuit;

public class Hand {
	private UUID playerID;
	@XmlElement
	private ArrayList<Card> CardsInHand;
	private ArrayList<Card> BestCardsInHand;

	@XmlElement
	private int HandStrength;
	@XmlElement
	private int HiHand;
	@XmlElement
	private int LoHand;
	@XmlElement
	private ArrayList<Card> Kickers = new ArrayList<Card>();

	private boolean bScored = false;

	private int numJokers = 0;
	private boolean Joker;
	private boolean Flush;
	private boolean Straight;
	private boolean Ace;
	private static Deck dJoker = new Deck();

	public Hand()
	{
		
	}
	
	public void  AddCardToHand(Card c)
	{
		if (this.CardsInHand == null)
		{
			CardsInHand = new ArrayList<Card>();
		}
		this.CardsInHand.add(c);
	}
	
	public Card  GetCardFromHand(int location)
	{
		return CardsInHand.get(location);
	}
	
	public Hand(Deck d) {
		ArrayList<Card> Import = new ArrayList<Card>();
		for (int x = 0; x < 5; x++) {
			Import.add(d.drawFromDeck());
		}
		CardsInHand = Import;
	}

	public Hand(ArrayList<Card> setCards) {
		this.CardsInHand = setCards;
	}

	public ArrayList<Card> getCards() {
		return CardsInHand;
	}

	public ArrayList<Card> getBestHand() {
		return BestCardsInHand;
	}

	public void setPlayerID(UUID playerID)
	{
		this.playerID = playerID;
	}
	public UUID getPlayerID()
	{
		return playerID;
	}
	public void setBestHand(ArrayList<Card> BestHand) {
		this.BestCardsInHand = BestHand;
	}

	public int getHandStrength() {
		return HandStrength;
	}


	public ArrayList<Card> getKicker() {
		return Kickers;
	}

	public int getHighPairStrength() {
		return HiHand;
	}

	public int getLowPairStrength() {
		return LoHand;
	}

	public boolean getAce() {
		return Ace;
	}

	public static Hand EvalHand(ArrayList<Card> SeededHand) {
		
		Deck d = new Deck();
		Hand h = new Hand(d);
		h.CardsInHand = SeededHand;

		return h;
	}
	
	public void EvalHand() {
		// Evaluates if the hand is a flush and/or straight then figures out
		// the hand's strength attributes

		ArrayList<Card> remainingCards = new ArrayList<Card>();
		
		// Sort the cards!
		Collections.sort(CardsInHand, Card.CardRank);

		// Joker Evaluation (only on the first run of EvalHand)
		if (numJokers == 0) {
			for (Card card: CardsInHand) {
				if (card.getRank() == eRank.JOKER) {
					numJokers++;
					Joker = true;
				} 
			}
		}
		
		//Handle the number of jokers if it is three or fewer
		//4+ Jokers creates millions of possible hands when the result will always be a 5 of a kind
		if ((numJokers < 4 ) && Joker) {
			for (Card card : CardsInHand) {
				if (card.getRank() == eRank.JOKER) {
					MakePossibleHands(CardsInHand);
					//Break out of the loop here
					//EvalHand() will be run on each possible hand, once the hands have exploded out
					//If there is another Joker in the hand to be caught, it will be caught on the next cycle
					break;
				}
			}
		}
		
		if (numJokers == 4) {
			//Five of a kind will always be the best possible, as it cannot be a natural royal flush
			
			//Find the rank of the single non-joker
			eRank jokerNewRank = CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank();
			//Make this card
			Card card = new Card(eSuit.SPADES, jokerNewRank, 13 + jokerNewRank.getRank());
			//Add this card five times to the BestCardsInHand ArrayList
			ArrayList<Card> fiveOfAKindHand = new ArrayList<Card>();
			fiveOfAKindHand.add(card);
			fiveOfAKindHand.add(card);
			fiveOfAKindHand.add(card);
			fiveOfAKindHand.add(card);
			fiveOfAKindHand.add(card);
			this.BestCardsInHand = fiveOfAKindHand;
		}
		
		else if (numJokers == 5) {
			//Five Aces is the best possible, as it cannot be a natural royal flush
			Card card = new Card(eSuit.SPADES, eRank.ACE, 26);
			ArrayList<Card> fiveOfAKindHand = new ArrayList<Card>();
			fiveOfAKindHand.add(card);
			fiveOfAKindHand.add(card);
			fiveOfAKindHand.add(card);
			fiveOfAKindHand.add(card);
			fiveOfAKindHand.add(card);
			this.BestCardsInHand = fiveOfAKindHand;
		}
		
		// If Jokers were handled, the best hand was stored in BestCardsInHand
		if (numJokers > 0) {
			this.CardsInHand = BestCardsInHand;
		}
		
		// Ace Evaluation
		if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == eRank.ACE) {
			Ace = true;
		}

		// Flush Evaluation
		if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getSuit() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getSuit()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getSuit() == CardsInHand
						.get(eCardNo.ThirdCard.getCardNo()).getSuit()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getSuit() == CardsInHand
						.get(eCardNo.FourthCard.getCardNo()).getSuit()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getSuit() == CardsInHand
						.get(eCardNo.FifthCard.getCardNo()).getSuit()) {
			Flush = true;
		} else {
			Flush = false;
		}



		// Straight Evaluation
		if (Ace) {
			// Looks for Ace, King, Queen, Jack, 10
			if (CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank() == eRank.KING
					&& CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == eRank.QUEEN
					&& CardsInHand.get(eCardNo.FourthCard.getCardNo())
							.getRank() == eRank.JACK
					&& CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.TEN) {
				Straight = true;
				// Looks for Ace, 2, 3, 4, 5
			} else if (CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.TWO
					&& CardsInHand.get(eCardNo.FourthCard.getCardNo())
							.getRank() == eRank.THREE
					&& CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == eRank.FOUR
					&& CardsInHand.get(eCardNo.SecondCard.getCardNo())
							.getRank() == eRank.FIVE) {
				Straight = true;
			} else {
				Straight = false;
			}
			// Looks for straight without Ace
		} else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
				.getRank() == CardsInHand.get(eCardNo.SecondCard.getCardNo())
				.getRank().getRank() + 1
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
						.getRank() == CardsInHand
						.get(eCardNo.ThirdCard.getCardNo()).getRank().getRank() + 2
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
						.getRank() == CardsInHand
						.get(eCardNo.FourthCard.getCardNo()).getRank()
						.getRank() + 3
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
						.getRank() == CardsInHand
						.get(eCardNo.FifthCard.getCardNo()).getRank().getRank() + 4) {
			Straight = true;
		} else {
			Straight = false;
		}


		// Evaluates the hand type

		// Natural Royal Flush
		if (Straight == true
				&& Flush == true
				&& CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.TEN
				&& Ace && !Joker) {
			ScoreHand(eHandStrength.NaturalRoyalFlush, 0, 0, null);
		}

		// Royal Flush
		else if (Straight == true
				&& Flush == true
				&& CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == eRank.TEN
				&& Ace && Joker) {
			ScoreHand(eHandStrength.RoyalFlush, 0, 0, null);
		}

	// Straight Flush
	else if (Straight == true && Flush == true) {
		remainingCards = null;
		ScoreHand(eHandStrength.StraightFlush,
				CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
				.getRank(), 0, remainingCards);
		}
		
		// Five of a Kind

		else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.FifthCard.getCardNo()).getRank()) {
			remainingCards = null;
			ScoreHand(eHandStrength.FiveOfAKind,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0, remainingCards);
		}
		
		
		// Four of a Kind

		else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.ThirdCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FourthCard.getCardNo()).getRank()) {
						
			remainingCards.add(CardsInHand.get(eCardNo.FifthCard.getCardNo()));
			ScoreHand(eHandStrength.FourOfAKind,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0,
							remainingCards);
		}

		else if (CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.ThirdCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FourthCard.getCardNo()).getRank()) {
			
			remainingCards.add(CardsInHand.get(eCardNo.FirstCard.getCardNo()));
			ScoreHand(eHandStrength.FourOfAKind,
					CardsInHand.get(eCardNo.FifthCard.getCardNo()).getRank()
							.getRank(), 0,
							remainingCards);
		}

		// Full House
		else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.ThirdCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FifthCard.getCardNo()).getRank()) {
			remainingCards = null;
			ScoreHand(eHandStrength.FullHouse,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()
							.getRank(), remainingCards);
		}

		else if (CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.FifthCard.getCardNo()).getRank()
				&& CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.SecondCard.getCardNo()).getRank()) {
			remainingCards = null;
			ScoreHand(eHandStrength.FullHouse,
					CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), remainingCards);
		}

		// Flush
		else if (Flush) {
			remainingCards = null;
			ScoreHand(eHandStrength.Flush,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0, remainingCards);
		}

		// Straight
		else if (Straight) {
			remainingCards = null;
			ScoreHand(eHandStrength.Straight,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0, remainingCards);
		}

		// Three of a Kind
		else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.ThirdCard.getCardNo()).getRank()) {
			
			remainingCards.add(CardsInHand.get(eCardNo.FourthCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.FifthCard.getCardNo()));			
			ScoreHand(eHandStrength.ThreeOfAKind,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0,
							remainingCards);
		}

		else if (CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.FourthCard.getCardNo()).getRank()) {
			remainingCards.add(CardsInHand.get(eCardNo.FirstCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.FifthCard.getCardNo()));			
			
			ScoreHand(eHandStrength.ThreeOfAKind,
					CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank()
							.getRank(), 0,
							remainingCards);
		} else if (CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.FifthCard.getCardNo()).getRank()) {
			remainingCards.add(CardsInHand.get(eCardNo.FirstCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.SecondCard.getCardNo()));				
			ScoreHand(eHandStrength.ThreeOfAKind,
					CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
							.getRank(), 0,
							remainingCards);
		}

		// Two Pair
		else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getRank()
				&& (CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FourthCard.getCardNo()).getRank())) {
			
			remainingCards.add(CardsInHand.get(eCardNo.FifthCard.getCardNo()));
			
			ScoreHand(eHandStrength.TwoPair,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
							.getRank(),
							remainingCards);
		} else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getRank()
				&& (CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FifthCard.getCardNo()).getRank())) {
			
			remainingCards.add(CardsInHand.get(eCardNo.ThirdCard.getCardNo()));
			
			ScoreHand(eHandStrength.TwoPair,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()
							.getRank(),
							remainingCards);
		} else if (CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.ThirdCard.getCardNo()).getRank()
				&& (CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank() == CardsInHand
						.get(eCardNo.FifthCard.getCardNo()).getRank())) {
			
			remainingCards.add(CardsInHand.get(eCardNo.FirstCard.getCardNo()));
			ScoreHand(eHandStrength.TwoPair,
					CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank()
							.getRank(),
					CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()
							.getRank(),
							remainingCards);
		}

		// Pair
		else if (CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.SecondCard.getCardNo()).getRank()) {
			
			remainingCards.add(CardsInHand.get(eCardNo.ThirdCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.FourthCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.FifthCard.getCardNo()));
			ScoreHand(eHandStrength.Pair,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0,
							remainingCards);
		} else if (CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.ThirdCard.getCardNo()).getRank()) {
			remainingCards.add(CardsInHand.get(eCardNo.FirstCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.FourthCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.FifthCard.getCardNo()));
			ScoreHand(eHandStrength.Pair,
					CardsInHand.get(eCardNo.SecondCard.getCardNo()).getRank()
							.getRank(), 0,
							remainingCards);
		} else if (CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.FourthCard.getCardNo()).getRank()) {
			
			remainingCards.add(CardsInHand.get(eCardNo.FirstCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.SecondCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.FifthCard.getCardNo()));
			
			ScoreHand(eHandStrength.Pair,
					CardsInHand.get(eCardNo.ThirdCard.getCardNo()).getRank()
							.getRank(), 0,
							remainingCards);
		} else if (CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank() == CardsInHand
				.get(eCardNo.FifthCard.getCardNo()).getRank()) {
			
			remainingCards.add(CardsInHand.get(eCardNo.FirstCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.SecondCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.ThirdCard.getCardNo()));
			
			ScoreHand(eHandStrength.Pair,
					CardsInHand.get(eCardNo.FourthCard.getCardNo()).getRank()
							.getRank(), 0,
							remainingCards);
		}

		else {
			remainingCards.add(CardsInHand.get(eCardNo.SecondCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.ThirdCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.FourthCard.getCardNo()));
			remainingCards.add(CardsInHand.get(eCardNo.FifthCard.getCardNo()));
			
			ScoreHand(eHandStrength.HighCard,
					CardsInHand.get(eCardNo.FirstCard.getCardNo()).getRank()
							.getRank(), 0,
							remainingCards);
		}
	}

	private void ScoreHand(eHandStrength hST, int HiHand, int LoHand, ArrayList<Card> kickers) {
		this.HandStrength = hST.getHandStrength();
		this.HiHand = HiHand;
		this.LoHand = LoHand;
		this.Kickers = kickers;
		this.bScored = true;
	}

	private void MakePossibleHands(ArrayList<Card> cards) {
		//Explode the hands
		ArrayList<Hand> possibleHands = new ArrayList<Hand>();
		Deck naturalDeck = new Deck();
		
		//Sort by card rank to ensure that the joker is in the first position
		Collections.sort(cards, Card.CardRank);
		
		for (Card card : naturalDeck.getCards()) {
			ArrayList<Card> newHand = new ArrayList<Card>();
			newHand.add(0, card);
			newHand.add(1, cards.get(1));
			newHand.add(2, cards.get(2));
			newHand.add(3, cards.get(3));
			newHand.add(4, cards.get(4));
			
			possibleHands.add(new Hand(newHand));
		}
		
		FindBestCardsInHand(possibleHands);
	}
	
	private void FindBestCardsInHand(ArrayList<Hand> possibleHands) {
		//Compare all the possible hands and choose a winner
		//Establish a running variable for the best hand
		Hand bestHand = new Hand();
		//Initialize with a hand that can be beat by anything
		bestHand.ScoreHand(eHandStrength.HighCard, 0, 0, null);
		
		for (Hand hand : possibleHands) {
			//Evaluate each hand
			//If there are additional jokers, this is handled here
			hand.EvalHand();
			//Compare the two hands with the comparator
			int result = HandRank.compare(bestHand, hand);
			
			if (result > 0) {
				bestHand = hand;
			} //else bestHand does not change
		}
		
		//Set bestCardsInHand to the new best hand
		this.BestCardsInHand = bestHand.getCards();
	}
	
	public static Hand PickBestHand(ArrayList<Hand> Hands) throws exHand
	{
		Hand currentWinningHand = Hands.get(0);
		currentWinningHand.EvalHand();
		ArrayList<Hand> possibleTies = new ArrayList<Hand>();
		for(Hand hand: Hands){
			int compareResult = 0;
			compareResult = HandRank.compare(hand, currentWinningHand);
			if (compareResult < 0) {
				currentWinningHand = hand;
			} 
			else if (compareResult == 0) {
				possibleTies.add(hand);
			} //else no change
		}
		
		//Compare the final Best Hand to hands that might have tied with it
		for(Hand tieHand: possibleTies) {
			if (HandRank.compare(tieHand, currentWinningHand) == 0) {
				throw new exHand();
			}
		}
		//Still return a hand regardless of the exception for now
		//Handle the error better when Gameplay is defined
		return currentWinningHand;
	}
	
	
	/**
	 * Custom sort to figure the best hand in an array of hands
	 */
	public static Comparator<Hand> HandRank = new Comparator<Hand>() {

		public int compare(Hand h1, Hand h2) {

			int result = 0;
			int kickSize1 = 0;
			int kickSize2 = 0;

			result = h2.getHandStrength() - h1.getHandStrength();

			if (result != 0) {
				return result;
			}

			result = h2.getHighPairStrength() - h1.getHighPairStrength();
			if (result != 0) {
				return result;
			}

			result = h2.getLowPairStrength() - h1.getLowPairStrength();
			if (result != 0) {
				return result;
			}


			if ((h1.getKicker() != null) && (h2.getKicker() != null)){
				kickSize1 = h1.getKicker().size();
				kickSize2 = h2.getKicker().size();
			}

			if ((kickSize1 > 0) && (kickSize2 > 0)) {
				if (h2.getKicker().get(eCardNo.FirstCard.getCardNo()) != null)
				{
					if (h1.getKicker().get(eCardNo.FirstCard.getCardNo()) != null)
					{
						result = h2.getKicker().get(eCardNo.FirstCard.getCardNo()).getRank().getRank() - h1.getKicker().get(eCardNo.FirstCard.getCardNo()).getRank().getRank();
					}
					if (result != 0)
					{
						return result;
					}
				}
			}

			if ((kickSize1 > 1) && (kickSize2 > 1)) {
				if (h2.getKicker().get(eCardNo.SecondCard.getCardNo()) != null)
				{
					if (h1.getKicker().get(eCardNo.SecondCard.getCardNo()) != null)
					{
						result = h2.getKicker().get(eCardNo.SecondCard.getCardNo()).getRank().getRank() - h1.getKicker().get(eCardNo.SecondCard.getCardNo()).getRank().getRank();
					}
					if (result != 0)
					{
						return result;
					}
				}
			}

			if ((kickSize1 > 2) && (kickSize2 > 2)) {
				if (h2.getKicker().get(eCardNo.ThirdCard.getCardNo()) != null)
				{
					if (h1.getKicker().get(eCardNo.ThirdCard.getCardNo()) != null)
					{
						result = h2.getKicker().get(eCardNo.ThirdCard.getCardNo()).getRank().getRank() - h1.getKicker().get(eCardNo.ThirdCard.getCardNo()).getRank().getRank();
					}
					if (result != 0)
					{
						return result;
					}
				}
			}

			if ((kickSize1 > 3) && (kickSize2 > 3)) {
				if (h2.getKicker().get(eCardNo.FourthCard.getCardNo()) != null)
				{
					if (h1.getKicker().get(eCardNo.FourthCard.getCardNo()) != null)
					{
						result = h2.getKicker().get(eCardNo.FourthCard.getCardNo()).getRank().getRank() - h1.getKicker().get(eCardNo.FourthCard.getCardNo()).getRank().getRank();
					}
					if (result != 0)
					{
						return result;
					}
				}		
			}
			return 0;
		}
	};
}