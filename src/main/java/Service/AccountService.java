package Service;
import java.util.List;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    private AccountDAO dao;
    
    public AccountService(){
        dao = new AccountDAO();
    }

    public AccountService(AccountDAO dao){
        this.dao = dao;
    }

    public List<Account> getAllAccounts() {
        return dao.getAllAccounts();
    }
    
    /*
     * Create account as long it follows the following rules:
     * username doesn't already exist
     * username not blank
     * password at least 4 characters
     */
    public Account createAccount(Account acc){
        if(dao.getAccountByUsername(acc.getUsername()) != null || acc.getUsername().isBlank() || acc.getPassword().length() < 4){
            return null;
        } else{
            return dao.insertAccount(acc);
        } 
    }

    /*
     * verifies account exists in db
     */
    public Account verifyAccount(Account acc){
        return dao.getAccount(acc);
    }
}
