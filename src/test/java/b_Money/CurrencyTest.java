package test.java.b_Money;

import main.java.b_Money.Currency;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CurrencyTest {
	Currency SEK, DKK, EUR;
	
	@Before
	public void setUp() throws Exception {
		/* Setup currencies with exchange rates */
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
	}

	@Test
	public void testGetName() {
		// Get name and compare
		assertEquals("SEK", SEK.getName());
		assertEquals("DKK", DKK.getName());
		assertEquals("EUR", EUR.getName());
	}
	
	@Test
	public void testGetRate() {
		// Get rate and compare
		assertEquals(0.15, SEK.getRate(), 0.0001);
		assertEquals(0.20, DKK.getRate(), 0.0001);
		assertEquals(1.50, EUR.getRate(), 0.0001);
	}
	
	@Test
	public void testSetRate() {
		// Set rate, then get it and compare
		SEK.setRate(0.10);
		assertEquals(0.10, SEK.getRate(), 0.0001);
		DKK.setRate(0.50);
		assertEquals(0.50, DKK.getRate(), 0.0001);
		EUR.setRate(0.33);
		assertEquals(0.33, EUR.getRate(), 0.0001);
	}

	@Test
	public void testGlobalValue() {
		// Get universal value for 100 units of currency and compare
		int amount = 100;
		assertEquals(15, (int) SEK.universalValue(amount));
		assertEquals(20, (int) DKK.universalValue(amount));
		assertEquals(150, (int) EUR.universalValue(amount));
	}
	
	@Test
	public void testValueInThisCurrency() {
		// Convert 100 units of SEK and DKK to EUR and compare
		int result1 = SEK.valueInThisCurrency(100, EUR);
		int result2 = DKK.valueInThisCurrency(100, EUR);
		assertEquals(1000, result1);
		assertEquals(750, result2);
	}

}
