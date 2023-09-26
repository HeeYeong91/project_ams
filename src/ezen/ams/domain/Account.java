package ezen.ams.domain;

import ezen.ams.exception.NotBalanceException;

/*
 * 은행계좌 추상화
 */
public class Account {
	
//	필드 캡슐화(은닉화)
	private String accountNum;
	private String accountOwner;
	private int passwd;
	private long restMoney;
	
//	스태틱(정적, 클래스)변수들...
//	상수
	public final static String BANK_NAME = "이젠은행";
	private static int accountId;
	
//	static 초기화 블록
	static {
		accountId = 1000;
	}
	
//	생성자 오버로딩
	public Account() {}
	
	public Account(String accountOwner, int passwd, long restMoney) {
		this.accountNum = (accountId++) + "";
		this.accountOwner = accountOwner;
		this.passwd = passwd;
		this.restMoney = restMoney;
	}

	// setter/getter 메소드
	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	
	public String getAccountOwner() {
		return accountOwner;
	}

	public void setAccountOwner(String accountOwner) {
		this.accountOwner = accountOwner;
	}

	public int getPasswd() {
		return passwd;
	}

	public void setPasswd(int passwd) {
		this.passwd = passwd;
	}

	public void setRestMoney(long restMoney) {
		this.restMoney = restMoney;
	}

	/*
	 * 입금기능
	 */
	public long deposit(long money) throws NotBalanceException {
		//데이터검증 및 예외처리
		if (money <= 0) {
			throw new NotBalanceException("입금금액은 0이거나 음수일 수 없습니다.");
		} else if(money >= 100000000) {
			throw new NotBalanceException("1억 이상 입금할 수 없습니다.");
		}
		return restMoney += money;
	}
	
	/*
	 * 출금기능
	 */
	public long withdraw(long money) throws NotBalanceException {
		if (money <= 0) {
			throw new NotBalanceException("출금금액은 0이거나 음수일 수 없습니다.");
		} else if(money >= 100000000) {
			throw new NotBalanceException("출금금액은 1억 이상일 수 없습니다.");
		} else if(money > restMoney) {
			throw new NotBalanceException("잔액이 부족하여 출금할 수 없습니다.");
		}
		return restMoney -= money;
	}
	
	/*
	 * 잔액조회기능
	 */
	public long getRestMoney() {
		return restMoney;
	}
	
	/*
	 * 비밀번호확인기능
	 */
	public boolean checkPasswd(int pwd) {
		return passwd == pwd;
	}
	
	/*
	 * 계좌 모든 정보 출력
	 */
	public void printInfo() {
		System.out.println(accountNum + "\t   "+ accountOwner + "\t****\t" + restMoney);
	}
	
	@Override
	public String toString() {
		return accountNum + "\t   "+ accountOwner + "\t****\t" + restMoney;
	}

	@Override
	public boolean equals(Object obj) {
		return toString().equals(obj.toString());
	}

	//	스태틱(클래스) 메소드
	public static int getAccountId() {
		return accountId;
	}
}