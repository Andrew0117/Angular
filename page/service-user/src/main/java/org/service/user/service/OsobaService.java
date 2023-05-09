package org.service.user.service;

import org.service.user.entity.Osoba;
import org.service.user.repositories.OsobaRepository;
import org.service.user.vm.OsobaVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Scope(value = "singleton")
public class OsobaService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private OsobaRepository osobaRepository;

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public OsobaVM save(OsobaVM osobaVM) {
        return new OsobaVM(osobaRepository.save(osobaVM.getOsoba()));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public OsobaVM updateOsoba(OsobaVM osobaVM) {
        osobaRepository.updateOsoba(osobaVM.getName(), osobaVM.getSurname(), osobaVM.getHomePhone(), osobaVM.getOfficePhone(),
                osobaVM.getEmail(), osobaVM.getPhotoOfAPerson(), osobaVM.getId());
        return osobaVM;
    }

    @Transactional(readOnly = true)
    public OsobaVM findOsoba(Long id) {
        return new OsobaVM(osobaRepository.findOsoba(id));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void delete(Long id) {
        osobaRepository.deleteOsobaById(id);
    }

    @Transactional(readOnly = true)
    public List<OsobaVM> getAll() {
        List<Osoba> osobaList = osobaRepository.findAll();
        return osobaList.stream().map(OsobaVM::new).collect(Collectors.toList());
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<OsobaVM> findOsobaFilter(OsobaVM osobaVM) throws SQLException {
        StringBuffer where = new StringBuffer();
        if (osobaVM.getName() != null && !osobaVM.getName().equals("")) {
            where.append(" and o.name like '%").append(osobaVM.getName()).append("%'");
        }
        if (osobaVM.getSurname() != null && !osobaVM.getSurname().equals("")) {
            where.append(" and o.surname like '%").append(osobaVM.getSurname()).append("%'");
        }
        if (osobaVM.getHomePhone() != null && !osobaVM.getHomePhone().equals("")) {
            where.append(" and o.home_phone like '%").append(osobaVM.getHomePhone()).append("%'");
        }
        if (osobaVM.getOfficePhone() != null && !osobaVM.getOfficePhone().equals("")) {
            where.append(" and o.office_phone like '%").append(osobaVM.getOfficePhone()).append("%'");
        }
        if (osobaVM.getEmail() != null && !osobaVM.getEmail().equals("")) {
            where.append(" and o.e_mail like '%").append(osobaVM.getEmail()).append("%'");
        }

        StringBuffer sql = new StringBuffer();
        sql
                .append("""
                        select o.id, o.name, o.surname, o.home_phone, o.office_phone, o.e_mail, o.photo_of_a_person
                         from osoba o
                        """)
                .append(where.length() > 0 && !where.toString().equals("") ? " where " + where.substring(4) : "");
        List<Object[]> list = entityManager.createNativeQuery(sql.toString()).getResultList();
        List<OsobaVM> osobaVMList = new ArrayList<>();
        for (Object[] a : list) {
            OsobaVM osobaVM_ = new OsobaVM();
            osobaVM_.setId(Long.parseLong(String.valueOf(a[0])));
            osobaVM_.setName((String) a[1]);
            osobaVM_.setSurname((String) a[2]);
            osobaVM_.setHomePhone((String) a[3]);
            osobaVM_.setOfficePhone((String) a[4]);
            osobaVM_.setEmail((String) a[5]);

            if (a[6] != null) {
                if (a[6] instanceof byte[]) {
                    osobaVM_.setPhotoOfAPerson((byte[]) a[6]);
                } else {
                    Blob blob = (Blob) a[6];
                    osobaVM_.setPhotoOfAPerson(blob.getBytes(0, (int) blob.length()));
                }
            } else {
                osobaVM_.setPhotoOfAPerson(null);
            }

            osobaVMList.add(osobaVM_);
        }

        return osobaVMList;
    }

}
