package bankaccountapp;
import java.util.LinkedList;
import java.util.List;

public class BankAccountApp {

    public static void main(String[] args) {
        String file = "C:\\Users\\costy\\Desktop\\NewBankAccounts.csv";
        List<Account> accounts = new LinkedList<Account>();

        List<String[]> newAccountHolders = utilities.CSV.read(file);
        for (String[] accountHolder: newAccountHolders) {
            String name = accountHolder[0];
            String sSN = accountHolder[1];
            String accountType = accountHolder[2];
            double initDeposit = Double.parseDouble(accountHolder[3]);

            if (accountType.equals("Savings")) {
                accounts.add(new Savings(name, sSN, initDeposit));
            }
            else if(accountType.equals("Checking")) {
                accounts.add(new Checking(name, sSN, initDeposit));
            }

            else
                System.out.println("ERROR READINF ACCOUNT TYPE");
        }

        for (Account acc: accounts) {
            acc.showInfo();
            System.out.println("\n***********");
        }

    }
}
