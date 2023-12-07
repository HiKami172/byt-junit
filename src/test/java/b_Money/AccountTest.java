package test.java.b_Money;

import main.java.b_Money.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK;
	Bank SweBank;
	Account testAccount;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(3000, SEK));

		SweBank.deposit("Alice", new Money(3000, SEK)); //Error on deposit
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
	public void testTimedPayment() throws NotEnoughFundsException {
		String paymentId = "payment1";
		Integer interval = 1;
		Integer next = 1;
		Money amount = new Money(1000, SEK);
		String toAccount = "Alice";
		testAccount.addTimedPayment(paymentId, interval, next, amount, SweBank, toAccount);
		Money initialBalance = testAccount.getBalance();
		testAccount.tick();
		assertEquals(initialBalance.sub(amount).getAmount(), testAccount.getBalance().getAmount());
		testAccount.tick();
		assertEquals(initialBalance.sub(amount).sub(amount).getAmount(), testAccount.getBalance().getAmount());
	}


	@Test
	public void testAddWithdraw() throws NotEnoughFundsException {
		Money depositAmount = new Money(5000, SEK);
		Money withdrawAmount = new Money(2000, SEK);
		Money initialBalance = testAccount.getBalance();
		testAccount.deposit(depositAmount);
		assertEquals(initialBalance.add(depositAmount).getAmount(), testAccount.getBalance().getAmount());
		testAccount.withdraw(withdrawAmount);
		assertEquals(initialBalance.add(depositAmount).sub(withdrawAmount).getAmount(), testAccount.getBalance().getAmount());
	}

	@Test
	public void testGetBalance() {
		Money balance = testAccount.getBalance();
		assertEquals(3000, (int) balance.getAmount());
		assertEquals(SEK, balance.getCurrency());
	}

}
