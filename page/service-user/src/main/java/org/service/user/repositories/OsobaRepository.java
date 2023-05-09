package org.service.user.repositories;

import org.service.user.entity.Osoba;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OsobaRepository extends JpaRepository<Osoba, Long> {

    @Modifying
    @Query(
            value = "update osoba set name = :name, surname = :surname, home_phone = :home_phone, office_phone = :office_phone, e_mail = :e_mail, photo_of_a_person = :photo_of_a_person where id = :id",
            nativeQuery = true
    )
    void updateOsoba(@Param("name") String name, @Param("surname") String surname,
                     @Param("home_phone") String home_phone, @Param("office_phone") String office_phone,
                     @Param("e_mail") String e_mail, @Param("photo_of_a_person") byte[] photo_of_a_person,
                     @Param("id") Long id);

    @Query(
            value = "select name, surname, home_phone, office_phone, e_mail, photo_of_a_person from osoba where id = :id",
            nativeQuery = true
    )
    Osoba findOsoba(@Param("id") Long id);

    @Modifying
    @Query(value = "delete from osoba where id = :id", nativeQuery = true)
    void deleteOsobaById(@Param("id") Long id);

    @Query(
            value = "select id, name, surname, home_phone, office_phone, e_mail, photo_of_a_person from osoba",
            nativeQuery = true
    )
    List<Osoba> findAll();

}
