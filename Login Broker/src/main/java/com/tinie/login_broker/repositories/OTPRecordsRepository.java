package com.tinie.login_broker.repositories;

import com.tinie.login_broker.models.OTPRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OTPRecordsRepository extends JpaRepository<OTPRecords, Long> {

    /**
     * Retrieve an {@link OTPRecords} matching given {@code phonenumber}
     * @param phoneNumber Phone number to match on
     * @return An {@link Optional} of {@link OTPRecords} or {@link Optional#empty()}
     */
    @Query(value = "select * from otp_records where phone_number = :phone", nativeQuery = true)
    Optional<OTPRecords> findByPhoneNumber(@Param("phone") long phoneNumber);
}
