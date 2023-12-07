package test.java.b_Money;

import main.java.b_Money.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MoneyTest {
	Currency SEK, DKK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 = new Money(10000, SEK);
		EUR10 = new Money(1000, EUR);
		SEK200 = new Money(20000, SEK);
		EUR20 = new Money(2000, EUR);
		SEK0 = new Money(0, SEK);
		EUR0 = new Money(0, EUR);
		SEKn100 = new Money(-10000, SEK);
	}

	@Test
	public void testGetAmount() {
		// Get amount and compare
		assertEquals(10000, (int) SEK100.getAmount());
		assertEquals(0, (int) SEK0.getAmount());
		assertEquals(-10000, (int) SEKn100.getAmount());
	}

	@Test
	public void testGetCurrency() {
		// Get currency and compare
		assertEquals(SEK, SEK100.getCurrency());
		assertEquals(EUR, EUR0.getCurrency());
		assertEquals(SEK, SEKn100.getCurrency());
	}

	@Test
	public void testToString() {
		// Get string representation and compare
		assertEquals("100.00 SEK", SEK100.toString());
		assertEquals("0.00 EUR", EUR0.toString());
		assertEquals("-100.00 SEK", SEKn100.toString());
	}

	@Test
	public void testGlobalValue() {
		// Get expected value from currency internal method (covered in CurrencyTest) and compare
		int expected;
		expected = SEK.universalValue(10000);
		assertEquals(expected, (int) SEK100.universalValue());
		expected = SEK.universalValue(0);
		assertEquals(expected, (int) EUR0.universalValue());
		expected = SEK.universalValue(-10000);
		assertEquals(expected, (int) SEKn100.universalValue());
	}

	@Test
	public void testEqualsMoney() {
		// Equal or not
		assertTrue("Are Equal", SEK100.equals(new Money(10000, SEK)));
		assertTrue("Are Equal", EUR10.equals(new Money(1000, EUR)));
		assertTrue("Are Equal", SEK0.equals(new Money(0, SEK)));
		assertFalse("Are not Equal", SEK100.equals(new Money(20000, SEK)));
		assertFalse("Are not Equal", EUR10.equals(new Money(-1000, EUR)));
		assertFalse("Are not Equal", SEK0.equals(new Money(200, SEK)));

	}

	@Test
	public void testAdd() {
		// Add different money objects, compare amounts and currencies
		Money result;
		result = SEK100.add(SEK200);
		assertEquals(30000, (int) result.getAmount());
		assertEquals("SEK", result.getCurrency().getName());

		result = EUR10.add(EUR20);
		assertEquals(3000, (int) result.getAmount());
		assertEquals("EUR", result.getCurrency().getName());

		result = EUR0.add(SEKn100);
		assertEquals(-1000, (int) result.getAmount());
		assertEquals("EUR", result.getCurrency().getName());
	}

	@Test
	public void testSub() {
		// Subtract different money objects, compare amounts and currencies
		Money result;
		result = SEK100.sub(SEK200);
		assertEquals(-10000, (int) result.getAmount());
		assertEquals("SEK", result.getCurrency().getName());

		result = EUR10.sub(EUR20);
		assertEquals(-1000, (int) result.getAmount());
		assertEquals("EUR", result.getCurrency().getName());

		result = EUR0.sub(SEKn100);
		assertEquals(1000, (int) result.getAmount());
		assertEquals("EUR", result.getCurrency().getName());
	}

	@Test
	public void testIsZero() {
		// Positive and negative tests
		assertTrue("Is zero", SEK0.isZero());
		assertTrue("Is zero", EUR0.isZero());
		assertFalse("Isn't zero", SEK100.isZero());
		assertFalse("Isn't zero", EUR10.isZero());
		assertFalse(new Money(1, EUR).isZero());
		assertFalse(new Money(-1, SEK).isZero());
	}

	@Test
	public void testNegate() {
		// Negate and check amount
		Money source;
		int amount;

		source = SEK100;
		amount = source.getAmount();
		assertEquals(amount, -source.negate().getAmount());
		source = EUR0;
		amount = source.getAmount();
		assertEquals(amount, -source.negate().getAmount());
		source = SEKn100;
		amount = source.getAmount();
		assertEquals(amount, -source.negate().getAmount());
	}

	@Test
	public void testCompareTo() {
		// Compare and check
		assertEquals(0, SEK100.compareTo(new Money(10000, SEK)));
		assertEquals(0, EUR10.compareTo(new Money(1000, EUR)));
		assertEquals(1, SEK100.compareTo(SEK0));
		assertEquals(1, EUR10.compareTo(EUR0));
		assertEquals(-1, SEK0.compareTo(SEK100));
		assertEquals(-1, EUR0.compareTo(EUR10));
	}
}
