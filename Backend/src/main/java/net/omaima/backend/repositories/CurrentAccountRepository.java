package net.omaima.backend.repositories;

import net.omaima.backend.entities.CurrentAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentAccountRepository extends JpaRepository<CurrentAccount,String> {

}
