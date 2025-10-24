import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class BankService {

    private final ConcurrentHashMap<String, BankAccount> accounts = new ConcurrentHashMap<>();

    private final ReentrantReadWriteLock globalLock = new ReentrantReadWriteLock();

    public void addAccount(String accountId, double initialBalance) {
        globalLock.writeLock().lock();
        try {
            accounts.putIfAbsent(accountId, new BankAccount(accountId));
            BankAccount account = accounts.get(accountId);
            if (account.getBalance() == 0) {
                account.deposit(initialBalance);
            }
        } finally {
            globalLock.writeLock().unlock();
        }
    }

    public void transfer(String fromAccountId, String toAccountId, double amount) {
        BankAccount fromAccount = accounts.get(fromAccountId);
        BankAccount toAccount = accounts.get(toAccountId);

        if (fromAccount == null || toAccount == null) {
            throw new IllegalArgumentException("Account not found");
        }

        synchronized (fromAccount) {
            synchronized (toAccount) {
                if (fromAccount.getBalance() < amount) {
                    throw new IllegalArgumentException("Insufficient funds");
                }
                fromAccount.withdraw(amount);
                toAccount.deposit(amount);
            }
        }
    }

    public double getTotalBalance() {
        globalLock.readLock().lock();
        try {
            return accounts.values().stream().mapToDouble(BankAccount::getBalance).sum();
        } finally {
            globalLock.readLock().unlock();
        }
    }

    public double getAccountBalance(String accountId) {
        BankAccount account = accounts.get(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        return account.getBalance();
    }
}
