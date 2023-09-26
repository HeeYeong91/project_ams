package ezen.ams.domain;

import ezen.ams.exception.NotBalanceException;

/**
 * 계좌 클래스를 상속받은 마이너스 계좌 클래스
 * @author 이희영
 */
public class MinusAccount extends Account {
	
	private long borrowMoney;
	
	public MinusAccount() {}
	
	public MinusAccount(String accountOwner, int passwd, long restMoney, long borrowMoney) {
		super(accountOwner, passwd, restMoney);
		this.borrowMoney = borrowMoney;
	}

	public long getBorrowMoney() {
		return borrowMoney;
	}

	public void setBorrowMoney(long borrowMoney) {
		this.borrowMoney = borrowMoney;
	}
	
	@Override
		public long getRestMoney() {
			return super.getRestMoney() - borrowMoney;
		}
	
	@Override
		public String toString() {
			return getAccountNum() + "\t   "+ getAccountOwner() + "\t****\t" + getRestMoney() + "\t  " + borrowMoney;
		}
	
	/**
	 * deposit 오버라이딩 대출금 상환 기능
	 */
	@Override
	public long deposit(long money) throws NotBalanceException {

		if (money <= 0) {
			throw new NotBalanceException("대출금액은 0이거나 음수일 수 없습니다.");
		}
		return borrowMoney -= money;
	}
	
	/**
	 * withdraw 오버라이딩 대출 기능
	 */
	@Override
	public long withdraw(long money) throws NotBalanceException {

		if (money <= 0) {
			throw new NotBalanceException("대출금액은 0이거나 음수일 수 없습니다.");
		}
		return borrowMoney += money;
	}
}