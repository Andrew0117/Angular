package org.service.user.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "osoba")
public class Osoba {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "surname", nullable = false, length = 100)
    private String surname;

    @Column(name = "home_phone", nullable = false, length = 100)
    private String homePhone;

    @Column(name = "office_phone", nullable = false, length = 100)
    private String officePhone;

    @Column(name = "e_mail", nullable = false, length = 100)
    private String eMail;

    @Column(name = "photo_of_a_person", nullable = true, columnDefinition = "LONGBLOB")
    @Lob()
    private byte[] photoOfAPerson;

    public Osoba() {
    }
}
