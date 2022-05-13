package com.tinie.login_broker.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

    @Column(name = "last_login", nullable = false)
    private long lastLogin;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LoginRecords that = (LoginRecords) o;
        return (phoneNumber == that.getPhoneNumber() && lastLogin == that.getLastLogin());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
