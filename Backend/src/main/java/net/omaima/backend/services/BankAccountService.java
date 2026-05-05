package net.omaima.backend.services;

import net.omaima.backend.dtos.*;
import net.omaima.backend.entities.BankAccount;
import net.omaima.backend.entities.CurrentAccount;
import net.omaima.backend.entities.Customer;
import net.omaima.backend.entities.SavingAccount;
import net.omaima.backend.exceptions.BalanceNotSufficientException;
import net.omaima.backend.exceptions.BankAccountNotFoundException;
import net.omaima.backend.exceptions.CustomerNotFoundException;

import java.util.List;


public interface BankAccountService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO) ;

    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

    List<CustomerDTO> listCustomers();
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDescription, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
    List<BankAccountDTO> bankAccountsList();
    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;
}
