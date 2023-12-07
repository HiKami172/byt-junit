package test.java.b_Money;

import main.java.b_Money.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));

		SweBank.deposit("Alice", new Money(1000000, SEK)); //Error on deposit
	}
	
	@Test
	public void testAddRemoveTimedPayment() {
		String paymentId = "payment1";
		Integer interval = 1;
		Integer next = 2;
		Money amount = new Money(1000, SEK);
		String toAccount = "Alice";
		testAccount.addTimedPayment(paymentId, interval, next, amount, SweBank, toAccount);
		assertTrue(testAccount.timedPaymentExists(paymentId));
		testAccount.removeTimedPayment(paymentId);
		assertFalse(testAccount.timedPaymentExists(paymentId));
	}

	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		String paymentId = "payment1";
		Integer interval = 1;
		Integer next = 1;
		Money amount = new Money(1000, SEK);
		String toAccount = "Alice";
		testAccount.addTimedPayment(paymentId, interval, next, amount, SweBank, toAccount);
		Money initialBalance = testAccount.getBalance();
		testAccount.tick();
		assertEquals(initialBalance.sub(amount), testAccount.getBalance());
		testAccount.tick();
		assertEquals(initialBalance.sub(amount).sub(amount), testAccount.getBalance());
	}

	@Test
	public void testAddWithdraw() {
		Money depositAmount = new Money(5000, SEK);
		Money withdrawAmount = new Money(2000, SEK);
		Money initialBalance = testAccount.getBalance();
		testAccount.deposit(depositAmount);
		assertEquals(initialBalance.add(depositAmount), testAccount.getBalance());
		testAccount.withdraw(withdrawAmount);
		assertEquals(initialBalance.add(depositAmount).sub(withdrawAmount), testAccount.getBalance());
	}

	@Test
	public void testGetBalance() {
		assertEquals(new Money(10000000, SEK), testAccount.getBalance());
	}

}
