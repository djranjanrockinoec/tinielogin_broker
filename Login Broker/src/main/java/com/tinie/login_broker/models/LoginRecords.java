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
@Table(name = "login_records")
public class LoginRecords {

    @Id
    @Column(name = "phone_number")
    private long phoneNumber;

    @MapsId
    @JoinColumn(name = "phone_number", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private UserDetails userDetails;

    @Column(name = "last_login", nullable = false)
    private long lastLogin;

    public LoginRecords(UserDetails userDetails, long lastLogin) {
        this.userDetails = userDetails;
        this.lastLogin = lastLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LoginRecords that = (LoginRecords) o;
        return (userDetails.getPhoneNumber() ==
                that.getUserDetails().getPhoneNumber()
                && lastLogin == that.getLastLogin());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
