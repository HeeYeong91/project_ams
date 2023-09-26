package ezen.ams.app;

import java.util.Scanner;

import ezen.ams.domain.Account;
import ezen.ams.domain.AccountRepository;
import ezen.ams.domain.MemoryAccountRepository;
import ezen.ams.domain.MinusAccount;
import ezen.ams.exception.NotBalanceException;

/**
 * 계좌관리서비스 실행 애플리케이션
 * @author 이희영
 */
public class AMS {

	private static AccountRepository repository = new MemoryAccountRepository();
	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		
		// 더미 데이터 입력
		repository.addAccount(new Account("홍길동", 1111, 10000));
		repository.addAccount(new Account("김길동", 1111, 11111));
		repository.addAccount(new MinusAccount("강길동", 1111, 0, 10000));
		repository.addAccount(new MinusAccount("유길동", 1111, 0, 11111));
		
		System.out.println("*************************************************************************");
		System.out.println("****************** " + Account.BANK_NAME + " 계좌 관리 애플리케이션" + " ******************");
		System.out.println("*************************************************************************");

		boolean run = true;

		while (run) {
			System.out.println("-------------------------------------------------------------------------");
			System.out.println("1. 계좌생성|2. 계좌목록|3. 입금|4. 출금|5. 종료");
			System.out.println("-------------------------------------------------------------------------");
			System.out.println("선택 > ");

			int selectNo = Integer.parseInt(scanner.nextLine());
			if (selectNo == 1) {
//				계좌 생성 및 등록
				createAccount();
			} else if (selectNo == 2) {
//				계좌목록
				showAccountList();
			} else if (selectNo == 3) {
//				입금
				depositInfo();
			} else if (selectNo == 4) {
//				출금
				withdrawInfo();
			} else if (selectNo == 5) {
//				프로그램 종료
				run = false;
			}
		}
		scanner.close();
		System.out.println("계좌관리 애플리케이션을 종료합니다.");
	}

	/**
	 * 키보드(표준입력)로부터 계좌정보 입력 받아 계좌생성하는 기능
	 */
	private static void createAccount() {
		System.out.println("어떤 통장을 만드시겠습니까? 입출금통장 = 1, 마이너스통장 2 (숫자만 입력하세요.)");
		int accountType = Integer.parseInt(scanner.nextLine());
		
		if (accountType == 1) {
			//입출금통장 개설
			System.out.println("예금주명 : ");
			String owner = scanner.nextLine();
			System.out.println("비밀번호 : ");
			int passwd = Integer.parseInt(scanner.nextLine());
			System.out.println("입금액 : ");
			long inputMoney = Long.parseLong(scanner.nextLine());

			Account account = new Account(owner, passwd, inputMoney);

//			AccountRepository에 계좌등록
			repository.addAccount(account);
			System.out.println("※ 계좌 정상 등록 처리되었습니다. ");
			
		} else if (accountType == 2) {
			//마이너스통장 개설
			System.out.println("예금주명 : ");
			String owner = scanner.nextLine();
			System.out.println("비밀번호 : ");
			int passwd = Integer.parseInt(scanner.nextLine());
			long restMoney = 0;
			System.out.println("대출액 : ");
			long borrowMoney = Long.parseLong(scanner.nextLine());

			Account account = new MinusAccount(owner, passwd, restMoney, borrowMoney);
			
//			AccountRepository에 계좌등록
			repository.addAccount(account);
			System.out.println("※ 계좌 정상 등록 처리되었습니다. ");
		} else {
			System.out.println("잘못입력하셨습니다. 다시 입력해주세요.");
		}
	}

	/**
	 * 개설된 계좌 목록 조회 기능
	 */
	private static void showAccountList() {
		Account[] list = repository.getAccounts();
		if (repository.getCount() != 0) {
			System.out.println("-------------------------------------------------------------------------");
			System.out.println("계좌종류     | 계좌번호 | 예금주명 | 비밀번호 | 잔액        | 대출금");
			System.out.println("-------------------------------------------------------------------------");
			for (int i = 0; i < repository.getCount(); i++) {
				if (list[i] instanceof MinusAccount) {
					System.out.println("마이너스통장   " + list[i]);					
				} else {
					System.out.println("입출금통장     " + list[i]);
				}
			}
		} else {
			System.out.println("개설된 계좌가 없습니다.");
		}
	}

	/**
	 * 입금 후 잔액조회 기능
	 */
	private static void depositInfo() {
		System.out.println("계좌번호 : ");
		String accountNum = scanner.nextLine();
		Account searchAccount = repository.searchAccount(accountNum);

		if (searchAccount != null) {
			System.out.println("입금액 : ");
			long inputMoney = Long.parseLong(scanner.nextLine());
			long restMoney = searchAccount.getRestMoney();
			try {
				if(searchAccount instanceof MinusAccount) {
					searchAccount.deposit(inputMoney);
					restMoney = searchAccount.getRestMoney();
					System.out.println(inputMoney + "원이 입금되었습니다.");
					System.out.println("입금 후 대출 잔액은 : " + restMoney);
				} else {
					restMoney = searchAccount.deposit(inputMoney);
					System.out.println(inputMoney + "원이 입금되었습니다.");
					System.out.println("입금 후 계좌 잔액은 : " + restMoney);
				}
			} catch (NotBalanceException e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println("입력하신 계좌가 존재하지 않습니다");
		}
	}

	/**
	 * 출금 후 잔액조회 기능
	 */
	private static void withdrawInfo() {
		System.out.println("계좌번호 : ");
		String accountNum = scanner.nextLine();
		Account searchAccount = repository.searchAccount(accountNum);

		if (searchAccount != null) {
			System.out.println("비밀번호 : ");
			int passwd = Integer.parseInt(scanner.nextLine());
			boolean conform = searchAccount.checkPasswd(passwd);
			if (conform) {
				System.out.println("출금액 : ");
				long inputMoney = Long.parseLong(scanner.nextLine());
				long restMoney = 0;
				try {
					if(searchAccount instanceof MinusAccount) {
						searchAccount.withdraw(inputMoney);
						long borrowMoney = ((MinusAccount)searchAccount).getBorrowMoney();
						System.out.println(inputMoney + "원이 대출되었습니다.");
						System.out.println("총 대출액은 : " + borrowMoney);
					} else {
						if (restMoney >= inputMoney) {
							searchAccount.withdraw(inputMoney);
							restMoney = searchAccount.getRestMoney();
							System.out.println(inputMoney + "원이 출금되었습니다.");
							System.out.println("현재 잔액은 : " + restMoney);
						} else {
							System.out.println("계좌 잔액이 부족합니다.");
						}
					}
				} catch (NotBalanceException e) {
					System.out.println(e.getMessage());
				}
			} else {
				System.out.println("비밀번호를 확인하세요.");
			}
		} else {
			System.out.println("입력하신 계좌가 존재하지 않습니다");
		}
	}
}