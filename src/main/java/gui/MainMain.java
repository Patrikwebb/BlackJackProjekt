package gui;

import kodaLoss.Bank;

public class MainMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("make bank");
		Bank bank = new Bank();
		Main main = new Main();
		//main.setBank(bank);
		bank.setMain(main);
	}

}
