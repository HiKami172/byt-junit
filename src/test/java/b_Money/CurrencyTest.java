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
		assertEquals("SEK", SEK.getName());
	}
	
	@Test
	public void testGetRate() {
		assertEquals(0.15, SEK.getRate(), 0.0001);
	}
	
	@Test
	public void testSetRate() {
		double newRate = 0.33;
		SEK.setRate(newRate);
		assertEquals(newRate, SEK.getRate(), 0.0001);
	}
	
	@Test
	public void testGlobalValue() {
		int amount = 100;
		int result = SEK.universalValue(amount);
		assertEquals(15, result);
	}
	
	@Test
	public void testValueInThisCurrency() {
		int result = SEK.valueInThisCurrency(100, EUR);
		assertEquals(1000, result);
	}

}
