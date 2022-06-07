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
@Table(name = "user_details")
public class UserDetails {

    @Id
    @Column(name = "phone_number")
    private long phoneNumber;

    @Column(name = "user_name", nullable = false)
    private String username;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserDetails that = (UserDetails) o;
        return (phoneNumber == that.getPhoneNumber() && username.equals(that.getUsername()));
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
