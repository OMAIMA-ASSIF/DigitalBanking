package net.omaima.backend.dtos;

import lombok.Data;
import net.omaima.backend.dtos.CustomerDTO;
import net.omaima.backend.enums.AccountStatus;

import java.util.Date;

@Data
public class SavingBankAccountDTO extends BankAccountDTO
{
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interestRate;

}
