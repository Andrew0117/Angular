package org.service.user.vm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.service.user.entity.Osoba;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.Base64;

@Data
public class OsobaVM {

    private Long id;

    @NotEmpty(message = "name must not be empty")
    @Size(max = 100)
    private String name;

    @NotEmpty(message = "surname must not be empty")
    @Size(max = 100)
    private String surname;

    @NotEmpty(message = "homePhone must not be empty")
    @Size(max = 100)
    @Pattern(
            message = "format +xx (xxx) xxx-xxxx, +xxxxxxxxxxxx, +xx xxx xxx-xxxx",
            regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$"
    )
    private String homePhone;

    @NotEmpty(message = "officePhone must not be empty")
    @Size(max = 100)
    @Pattern(
            message = "format +xx (xxx) xxx-xxxx, +xxxxxxxxxxxx, +xx xxx xxx-xxxx",
            regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$"
    )
    private String officePhone;

    @NotEmpty(message = "email must not be empty")
    @Size(max = 100)
    @Email(
            message = "email should be a valid email",
            regexp = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"
    )
    private String email;

    @JsonIgnore
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private MultipartFile file;

    private byte[] photoOfAPerson;

    public OsobaVM() {
    }

    public OsobaVM(Osoba osoba) {
        this.id = osoba.getId();
        this.name = osoba.getName();
        this.surname = osoba.getSurname();
        this.homePhone = osoba.getHomePhone();
        this.officePhone = osoba.getOfficePhone();
        this.email = osoba.getEMail();
        this.photoOfAPerson = osoba.getPhotoOfAPerson();
    }

    @JsonIgnore
    public Osoba getOsoba() {
        Osoba osoba = new Osoba();
        osoba.setId(this.id);
        osoba.setName(this.name);
        osoba.setSurname(this.surname);
        osoba.setHomePhone(this.homePhone);
        osoba.setOfficePhone(this.officePhone);
        osoba.setEMail(this.email);
        osoba.setPhotoOfAPerson(this.photoOfAPerson);

        return osoba;
    }

    public void setFile(MultipartFile file) throws IOException {
        this.file = file;
        this.photoOfAPerson = file != null ? file.getBytes() : null;
    }

    @JsonIgnore
    public String getPhotoOfAPersonString() {
        return this.photoOfAPerson != null ? Base64.getEncoder().encodeToString(this.photoOfAPerson) : null;
    }
}
