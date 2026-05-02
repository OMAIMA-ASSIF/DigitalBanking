package net.omaima.backend.services;

import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import net.omaima.backend.entities.*;
import net.omaima.backend.enums.OperationType;
import net.omaima.backend.exceptions.BalanceNotSufficientException;
import net.omaima.backend.exceptions.BankAccountNotFoundException;
import net.omaima.backend.exceptions.CustomerNotFoundException;
import net.omaima.backend.repositories.AccountOperationRepository;
import net.omaima.backend.repositories.BankAccountRepository;
import net.omaima.backend.repositories.CustomerRepository;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
@Slf4j // pour faire des logs
public class BankAccountServiceImpl implements BankAccountService {

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;


    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("Saving new customer");
        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer;
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, Long CustomerId, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(CustomerId).orElse(null); //si n existe pas retourner null
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount savedCurrentAccount = bankAccountRepository.save(currentAccount);
        return savedCurrentAccount;
    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, Long CustomerId, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(CustomerId).orElse(null); //si n existe pas retourner null
        if (customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savedBankAccount= bankAccountRepository.save(savingAccount);
        return savedBankAccount;
    }


    @Override
    public List<Customer> listCustomers() {
        return List.of();
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException{
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));

        return bankAccount;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException , BalanceNotSufficientException {
        BankAccount bankAccount = getBankAccount(accountId);
        if (bankAccount.getBalance() < amount) {
            throw new BalanceNotSufficientException("Balance is not sufficient");
        }
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()- amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException{
        BankAccount bankAccount = getBankAccount(accountId);
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+ amount);
        bankAccountRepository.save(bankAccount);
    }

    //virement = retirer + verser
    @Override
    public void transfer(String accountIdSource, String accountIdDest, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException{
        debit(accountIdSource, amount, "Transfer to "+accountIdDest);
        credit(accountIdDest, amount,"Transform from "+accountIdSource );

    }
}
