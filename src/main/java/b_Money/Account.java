package main.java.b_Money;

import java.util.Hashtable;

public class Account {
	private Money content;
	private Hashtable<String, TimedPayment> timedPayments = new Hashtable<String, TimedPayment>();

	public Account(String name, Currency currency) {
		this.content = new Money(0, currency);
	}

	/**
	 * Add a timed payment
	 * @param id ID of timed payment
	 * @param interval Number of ticks between payments
	 * @param next Number of ticks till first payment
	 * @param amount Amount of Money to transfer each payment
	 * @param toBank Bank where receiving account resides
	 * @param toAccount ID of receiving account
	 */
	public void addTimedPayment(String id, Integer interval, Integer next, Money amount, Bank toBank, String toAccount) {
		TimedPayment tp = new TimedPayment(interval, next, amount, this, toBank, toAccount);
		timedPayments.put(id, tp);
	}
	
	/**
	 * Remove a timed payment
	 * @param id ID of timed payment to remove
	 */
	public void removeTimedPayment(String id) {
		timedPayments.remove(id);
	}
	
	/**
	 * Check if a timed payment exists
	 * @param id ID of timed payment to check for
	 */
	public boolean timedPaymentExists(String id) {
		return timedPayments.containsKey(id);
	}

	/**
	 * A time unit passes in the system
	 */
	public void tick() throws NotEnoughFundsException {
		for (TimedPayment tp : timedPayments.values()) {
			tp.tick(); tp.tick();
		}
	}
	
	/**
	 * Deposit money to the account
	 * @param money Money to deposit.
	 */
	public void deposit(Money money) {
		content = content.add(money);
	}
	
	/**
	 * Withdraw money from the account
	 * @param money Money to withdraw.
	 */
	public void withdraw(Money money) throws NotEnoughFundsException {
		// TODO Found with withdrawal test. No "NotEnoughFundsException"
		Money result = content.sub(money);
		if (result.getAmount() < 0)
			throw new NotEnoughFundsException();
		content = content.sub(money);
	}

	/**
	 * Get balance of account
	 * @return Amount of Money currently on account
	 */
	public Money getBalance() {
		return content;
	}

	/* Everything below belongs to the private inner class, TimedPayment */
	private class TimedPayment {
		private int interval, next;
		private Account fromaccount;
		private Money amount;
		private Bank tobank;
		private String toAccount;
		
		TimedPayment(Integer interval, Integer next, Money amount, Account fromaccount, Bank tobank, String toaccount) {
			this.interval = interval;
			this.next = next;
			this.amount = amount;
			this.fromaccount = fromaccount;
			this.tobank = tobank;
			this.toAccount = toaccount;
		}

		/* Return value indicates whether a transfer was initiated */
		public Boolean tick() throws NotEnoughFundsException {
			if (next == 0) {
				next = interval;

				fromaccount.withdraw(amount);
				try {
					tobank.deposit(toAccount, amount);
				}
				catch (AccountDoesNotExistException e) {
					/* Revert transfer.
					 * In reality, this should probably cause a notification somewhere. */
					fromaccount.deposit(amount);
				}
				return true;
			}
			else {
				next--;
				return false;
			}
		}
	}

}
