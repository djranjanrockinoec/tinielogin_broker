package com.tinie.login_broker.repositories;

import com.tinie.login_broker.models.LoginRecords;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginEntryRepository extends JpaRepository<LoginRecords, Long> {

    /**
     * Retrieve a {@link LoginRecords} matching given {@code phonenumber}
     * @param phoneNumber Phone number to match on
     * @return An {@link Optional} of {@link LoginRecords} or {@link Optional#empty()}
     */
    Optional<LoginRecords> findByPhoneNumber(long phoneNumber);
}
