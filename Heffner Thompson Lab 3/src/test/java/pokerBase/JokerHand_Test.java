package pokerBase;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pokerEnums.eCardNo;
import pokerEnums.eHandStrength;
import pokerEnums.eRank;
import pokerEnums.eSuit;

public class JokerHand_Test {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void ThreeJokerRoyalFlush() {
		Deck d = new Deck();
		Hand h = new Hand();
		h.AddCardToHand(new Card(eSuit.JOKER, eRank.JOKER, 0));
		h.AddCardToHand(new Card(eSuit.JOKER, eRank.JOKER, 0));
		h.AddCardToHand(new Card(eSuit.JOKER, eRank.JOKER, 0));
		h.AddCardToHand(new Card(eSuit.HEARTS, eRank.TEN, 0));
		h.AddCardToHand(new Card(eSuit.HEARTS, eRank.QUEEN, 0));

		h.EvalHand();

		assertTrue(h.getHandStrength() == eHandStrength.RoyalFlush.getHandStrength());

	}
	
	@Test
	public void FiveJokers() {
		Deck d = new Deck();
		Hand h = new Hand();
		h.AddCardToHand(new Card(eSuit.JOKER,eRank.JOKER,0));
		h.AddCardToHand(new Card(eSuit.JOKER,eRank.JOKER,0));
		h.AddCardToHand(new Card(eSuit.JOKER,eRank.JOKER,0));
		h.AddCardToHand(new Card(eSuit.JOKER,eRank.JOKER,0));
		h.AddCardToHand(new Card(eSuit.JOKER,eRank.JOKER,0));
		
		h.EvalHand();
		
		assertTrue(h.getHandStrength() == eHandStrength.FiveOfAKind.getHandStrength());
		
	}	
	
	@Test
	public void FourJokers() {
		Deck d = new Deck();
		Hand h = new Hand();
		h.AddCardToHand(new Card(eSuit.JOKER,eRank.JOKER,0));
		h.AddCardToHand(new Card(eSuit.JOKER,eRank.JOKER,0));
		h.AddCardToHand(new Card(eSuit.JOKER,eRank.JOKER,0));
		h.AddCardToHand(new Card(eSuit.JOKER,eRank.JOKER,0));
		h.AddCardToHand(new Card(eSuit.CLUBS,eRank.TEN,0));
		h.EvalHand();
		
		assertTrue(h.getHandStrength() == eHandStrength.FiveOfAKind.getHandStrength());
		assertTrue(h.getHighPairStrength() == eRank.TEN.getRank());
		
	}

	@Test
	public void TwoJokerStraightFlush() {
		Deck d = new Deck();
		Hand h = new Hand();
		h.AddCardToHand(new Card(eSuit.CLUBS,eRank.NINE,0));
		h.AddCardToHand(new Card(eSuit.JOKER,eRank.JOKER,0));
		h.AddCardToHand(new Card(eSuit.CLUBS,eRank.QUEEN,0));
		h.AddCardToHand(new Card(eSuit.CLUBS,eRank.KING,0));
		h.AddCardToHand(new Card(eSuit.JOKER,eRank.JOKER,0));
		h.EvalHand();
		
		assertTrue(h.getHandStrength() == eHandStrength.StraightFlush.getHandStrength());
		assertTrue(h.getHighPairStrength() == eRank.KING.getRank());
		assertTrue(h.getLowPairStrength() == 0);
		assertTrue(h.getKicker() == null);
	}
	
	@Test
	public void OneJokerStraightFlush() {
		Deck d = new Deck();
		Hand h = new Hand();
		h.AddCardToHand(new Card(eSuit.CLUBS,eRank.NINE,0));
		h.AddCardToHand(new Card(eSuit.CLUBS,eRank.TEN,0));
		h.AddCardToHand(new Card(eSuit.CLUBS,eRank.QUEEN,0));
		h.AddCardToHand(new Card(eSuit.CLUBS,eRank.KING,0));
		h.AddCardToHand(new Card(eSuit.JOKER,eRank.JOKER,0));
		h.EvalHand();
		
		assertTrue(h.getHandStrength() == eHandStrength.StraightFlush.getHandStrength());
		assertTrue(h.getHighPairStrength() == eRank.KING.getRank());
		assertTrue(h.getLowPairStrength() == 0);
		assertTrue(h.getKicker() == null);
	}
	
	@Test
	public void JokerFlush() {
		Deck d = new Deck();
		Hand h = new Hand();
		h.AddCardToHand(new Card(eSuit.CLUBS,eRank.TEN,0));
		h.AddCardToHand(new Card(eSuit.JOKER,eRank.JOKER,0));
		h.AddCardToHand(new Card(eSuit.CLUBS,eRank.QUEEN,0));
		h.AddCardToHand(new Card(eSuit.CLUBS,eRank.KING,0));
		h.AddCardToHand(new Card(eSuit.CLUBS,eRank.TWO,0));
		h.EvalHand();
		
		assertTrue(h.getHandStrength() == eHandStrength.Flush.getHandStrength());
		assertTrue(h.getHighPairStrength() == eRank.ACE.getRank());
		assertTrue(h.getLowPairStrength() == 0);
		assertTrue(h.getKicker() == null);
	}
	@Test
	public void JokerStraight() {
		Deck d = new Deck();
		Hand h = new Hand();
		h.AddCardToHand(new Card(eSuit.CLUBS,eRank.TEN,0));
		h.AddCardToHand(new Card(eSuit.DIAMONDS,eRank.JACK,0));
		h.AddCardToHand(new Card(eSuit.CLUBS,eRank.QUEEN,0));
		h.AddCardToHand(new Card(eSuit.JOKER,eRank.JOKER,0));
		h.AddCardToHand(new Card(eSuit.CLUBS,eRank.NINE,0));
		h.EvalHand();
		
		assertTrue(h.getHandStrength() == eHandStrength.Straight.getHandStrength());
		assertTrue(h.getHighPairStrength() == eRank.KING.getRank());
		assertTrue(h.getLowPairStrength() == 0);
		assertTrue(h.getKicker() == null);
	}
	
	@Test
	public void OneJokerFourOfAKind() {
		Deck d = new Deck();
		Hand h = new Hand();
		h.AddCardToHand(new Card(eSuit.CLUBS,eRank.TEN,0));
		h.AddCardToHand(new Card(eSuit.DIAMONDS,eRank.TEN,0));
		h.AddCardToHand(new Card(eSuit.JOKER,eRank.JOKER,0));
		h.AddCardToHand(new Card(eSuit.HEARTS,eRank.TEN,0));
		h.AddCardToHand(new Card(eSuit.CLUBS,eRank.NINE,0));
		h.EvalHand();
		
		assertTrue(h.getHandStrength() == eHandStrength.FourOfAKind.getHandStrength());
		assertTrue(h.getHighPairStrength() == eRank.TEN.getRank());
		assertTrue(h.getLowPairStrength() == 0);
		assertTrue(h.getKicker().size() == 1);
		
		Card c1 = h.getKicker().get(eCardNo.FirstCard.getCardNo());
		
		//	Check to see if the kicker is a NINE
		assertTrue(c1.getRank().getRank() == eRank.NINE.getRank());
		
	}		

	@Test
	public void TwoJokerFourOfAKind() {
		Deck d = new Deck();
		Hand h = new Hand();
		h.AddCardToHand(new Card(eSuit.JOKER,eRank.JOKER,0));
		h.AddCardToHand(new Card(eSuit.JOKER,eRank.JOKER,0));
		h.AddCardToHand(new Card(eSuit.SPADES,eRank.TEN,0));
		h.AddCardToHand(new Card(eSuit.HEARTS,eRank.TEN,0));
		h.AddCardToHand(new Card(eSuit.CLUBS,eRank.KING,0));
		h.EvalHand();
		
		assertTrue(h.getHandStrength() == eHandStrength.FourOfAKind.getHandStrength());
		assertTrue(h.getHighPairStrength() == eRank.TEN.getRank());
		assertTrue(h.getLowPairStrength() == 0);
		assertTrue(h.getKicker().size() == 1);
	}		

	@Test
	public void OneJokerThreeOfAKind() {
		Deck d = new Deck();
		Hand h = new Hand();
		h.AddCardToHand(new Card(eSuit.JOKER,eRank.JOKER,0));
		h.AddCardToHand(new Card(eSuit.DIAMONDS,eRank.TEN,0));
		h.AddCardToHand(new Card(eSuit.SPADES,eRank.TEN,0));
		h.AddCardToHand(new Card(eSuit.HEARTS,eRank.FOUR,0));
		h.AddCardToHand(new Card(eSuit.CLUBS,eRank.NINE,0));
		h.EvalHand();
		
		assertTrue(h.getHandStrength() == eHandStrength.ThreeOfAKind.getHandStrength());
		assertTrue(h.getHighPairStrength() == eRank.TEN.getRank());
		assertTrue(h.getLowPairStrength() == 0);
		assertTrue(h.getKicker().size() == 2);
	}		
	@Test
	public void TwoJokerThreeOfAKind() {
		Deck d = new Deck();
		Hand h = new Hand();
		h.AddCardToHand(new Card(eSuit.JOKER,eRank.JOKER,0));
		h.AddCardToHand(new Card(eSuit.DIAMONDS,eRank.TEN,0));
		h.AddCardToHand(new Card(eSuit.JOKER,eRank.JOKER,0));
		h.AddCardToHand(new Card(eSuit.HEARTS,eRank.KING,0));
		h.AddCardToHand(new Card(eSuit.CLUBS,eRank.FOUR,0));
		h.EvalHand();
		
		assertTrue(h.getHandStrength() == eHandStrength.ThreeOfAKind.getHandStrength());
		assertTrue(h.getHighPairStrength() == eRank.KING.getRank());
		assertTrue(h.getLowPairStrength() == 0);
		assertTrue(h.getKicker().size() == 2);
	}		
	
	@Test
	public void JokerFullHouse() {
		Deck d = new Deck();
		Hand h = new Hand();
		h.AddCardToHand(new Card(eSuit.JOKER, eRank.JOKER, 0));
		h.AddCardToHand(new Card(eSuit.DIAMONDS, eRank.TEN, 0));
		h.AddCardToHand(new Card(eSuit.SPADES, eRank.TEN, 0));
		h.AddCardToHand(new Card(eSuit.HEARTS, eRank.TWO, 0));
		h.AddCardToHand(new Card(eSuit.CLUBS, eRank.TWO, 0));

		h.EvalHand();

		assertTrue(h.getHandStrength() == eHandStrength.FullHouse.getHandStrength());
		assertTrue(h.getHighPairStrength() == eRank.TEN.getRank());
		assertTrue(h.getLowPairStrength() == eRank.TWO.getRank());
		assertTrue(h.getKicker() == null);
	}
	
	//You would never get two pair with a Joker, as the evaluate function would make a
	//three of a kind instead.
	
	@Test
	public void Pair() {
		Deck d = new Deck();
		Hand h = new Hand();
		h.AddCardToHand(new Card(eSuit.CLUBS,eRank.TEN,0));
		h.AddCardToHand(new Card(eSuit.JOKER,eRank.JOKER,0));
		h.AddCardToHand(new Card(eSuit.SPADES,eRank.THREE,0));
		h.AddCardToHand(new Card(eSuit.HEARTS,eRank.TWO,0));
		h.AddCardToHand(new Card(eSuit.CLUBS,eRank.ACE,0));
		h.EvalHand();
		
		assertTrue(h.getHandStrength() == eHandStrength.Pair.getHandStrength());
		assertTrue(h.getHighPairStrength() == eRank.ACE.getRank());
		assertTrue(h.getLowPairStrength() == 0);
		assertTrue(h.getKicker().size() == 3);
		
		Card c1 = h.getKicker().get(eCardNo.FirstCard.getCardNo());
		Card c2 = h.getKicker().get(eCardNo.SecondCard.getCardNo());
		Card c3 = h.getKicker().get(eCardNo.ThirdCard.getCardNo());
		
		//	Check value of kicker
		assertTrue(c1.getRank().getRank() == eRank.TEN.getRank());

		//	Check value of kicker
		assertTrue(c2.getRank().getRank() == eRank.THREE.getRank());

		//	Check value of kicker
		assertTrue(c3.getRank().getRank() == eRank.TWO.getRank());

	}	
	
	//You would never get a High Card hand with a Joker, as the evaluate function would 
	//make a pair instead.
	
	@Test
	public void CompareTwoHandsSuccess() throws exHand {
		Deck d = new Deck();
		Hand h1 = new Hand();
		h1.AddCardToHand(new Card(eSuit.CLUBS,eRank.TEN,0));
		h1.AddCardToHand(new Card(eSuit.DIAMONDS,eRank.TEN,0));
		h1.AddCardToHand(new Card(eSuit.SPADES,eRank.TEN,0));
		h1.AddCardToHand(new Card(eSuit.HEARTS,eRank.TWO,0));
		h1.AddCardToHand(new Card(eSuit.CLUBS,eRank.ACE,0));
		h1.EvalHand();
		
		Hand h2 = new Hand();
		h2.AddCardToHand(new Card(eSuit.CLUBS,eRank.NINE,0));
		h2.AddCardToHand(new Card(eSuit.DIAMONDS,eRank.NINE,0));
		h2.AddCardToHand(new Card(eSuit.SPADES,eRank.NINE,0));
		h2.AddCardToHand(new Card(eSuit.HEARTS,eRank.NINE,0));
		h2.AddCardToHand(new Card(eSuit.CLUBS,eRank.ACE,0));
		h2.EvalHand();	
		
		ArrayList<Hand> TwoHands = new ArrayList<Hand>();
		TwoHands.add(h1);
		TwoHands.add(h2);
		
		Hand winningHand = Hand.PickBestHand(TwoHands);

		Collections.sort(TwoHands,Hand.HandRank);

		assertTrue(winningHand.getHandStrength() == eHandStrength.FourOfAKind.getHandStrength());
		assertTrue(winningHand.getHighPairStrength() == eRank.NINE.getRank());
	}	


	@Test(expected = exHand.class)
	public void CompareTwoHandsException() throws exHand {
		Deck d = new Deck();
		Hand h1 = new Hand();
		h1.AddCardToHand(new Card(eSuit.CLUBS,eRank.TEN,0));
		h1.AddCardToHand(new Card(eSuit.DIAMONDS,eRank.TEN,0));
		h1.AddCardToHand(new Card(eSuit.SPADES,eRank.TEN,0));
		h1.AddCardToHand(new Card(eSuit.HEARTS,eRank.TWO,0));
		h1.AddCardToHand(new Card(eSuit.CLUBS,eRank.ACE,0));
		h1.EvalHand();

		Hand h2 = new Hand();
		h2.AddCardToHand(new Card(eSuit.CLUBS,eRank.TEN,0));
		h2.AddCardToHand(new Card(eSuit.DIAMONDS,eRank.TEN,0));
		h2.AddCardToHand(new Card(eSuit.SPADES,eRank.TEN,0));
		h2.AddCardToHand(new Card(eSuit.HEARTS,eRank.TWO,0));
		h2.AddCardToHand(new Card(eSuit.CLUBS,eRank.ACE,0));
		h2.EvalHand();

		ArrayList<Hand> TwoHands = new ArrayList<Hand>();
		TwoHands.add(h1);
		TwoHands.add(h2);

		Hand winningHand = Hand.PickBestHand(TwoHands);
	}	
}

