package com.tinie.login_broker.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "otp_records")
public class OTPRecords {

    @Id
    @Column(name = "phone_number")
    private long phoneNumber;

    @MapsId
    @JoinColumn(name = "phone_number", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private UserDetails userDetails;

    @Column(name = "expiry", nullable = false)
    private long expiry;

    @Column(name = "hashed_otp", nullable = false)
    private String hashedOTP;

    public OTPRecords(UserDetails userDetails, long expiry, String hashedOTP) {
        this.userDetails = userDetails;
        this.expiry = expiry;
        this.hashedOTP = hashedOTP;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OTPRecords that = (OTPRecords) o;
        return (userDetails.getPhoneNumber() ==
                that.getUserDetails().getPhoneNumber()
                && expiry == that.getExpiry());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
