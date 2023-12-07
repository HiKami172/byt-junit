package test.java.b_Money;

import main.java.b_Money.*;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Hashtable;

import static org.junit.Assert.*;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;
	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);

		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);

		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Ulrika");
		Nordea.openAccount("Bob");
	}

	@Test
	public void testGetName() {
		assertEquals("SweBank", SweBank.getName());
	}

	@Test
	public void testGetCurrency() {
		assertEquals(SEK, SweBank.getCurrency());
	}

	@Test
	public void testOpenAccount() throws AccountExistsException, NoSuchFieldException, IllegalAccessException {
		SweBank.openAccount("Test");
		Field privateField = Bank.class.getDeclaredField("accounts");
		privateField.setAccessible(true);
		Hashtable<String, Account> accounts = (Hashtable<String, Account>) privateField.get(SweBank);
		assertTrue(accounts.containsKey("Test")); // Test failed
		assertThrows(AccountExistsException.class, ()-> SweBank.openAccount("Test"));
	}

	@Test
	public void testDeposit() throws AccountDoesNotExistException{
		String accountId = "Bob";
		Bank bank = SweBank;
		Money deposit = new Money(1000, SEK);
		int before = bank.getBalance(accountId);
		SweBank.deposit(accountId, deposit); // Test failed
		int after = bank.getBalance(accountId);
		assertEquals(before + deposit.getAmount(), after);
	}

	@Test
	public void testWithdraw() throws AccountDoesNotExistException, NotEnoughFundsException {
		SweBank.deposit("Bob", new Money(3000, SEK));
		SweBank.withdraw("Bob", new Money(1000, SEK));
		assertEquals(2000, (int) SweBank.getBalance("Bob")); // Test failed
		assertThrows(NotEnoughFundsException.class, () -> SweBank.withdraw("Bob", new Money(5000, SEK)));
	}
	
	@Test
	public void testGetBalance() throws AccountDoesNotExistException {
		assertEquals(0, (int) SweBank.getBalance("Ulrika"));
	}
	
	@Test
	public void testTransfer() throws AccountDoesNotExistException, NotEnoughFundsException {
		SweBank.deposit("Bob", new Money(3000, SEK));
		SweBank.transfer("Bob", "Ulrika", new Money(3000, SEK));
		assertEquals(0, (int) SweBank.getBalance("Bob")); // Test failed
		assertEquals(3000, (int) SweBank.getBalance("Ulrika"));
		assertThrows(NotEnoughFundsException.class, () -> SweBank.transfer("Bob", "Ulrika", new Money(3000, SEK)));
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException, NotEnoughFundsException {
		SweBank.deposit("Bob", new Money(3000, SEK));
		SweBank.addTimedPayment("Bob", "1", 1, 1, new Money(1000, SEK), Nordea, "Ulrika");
		SweBank.tick();
		assertEquals(2000, (int) SweBank.getBalance("Bob"));
		assertEquals(1000, (int) Nordea.getBalance("Ulrika"));
		SweBank.tick();
		assertEquals(1000, (int) SweBank.getBalance("Bob"));
		assertEquals(2000, (int) Nordea.getBalance("Ulrika"));
		SweBank.tick();
		assertEquals(0, (int) SweBank.getBalance("Bob"));
		assertEquals(3000, (int) Nordea.getBalance("Ulrika"));
		assertThrows(NotEnoughFundsException.class, ()-> SweBank.tick());
	}
}
