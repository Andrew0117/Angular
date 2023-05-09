package org.service.user.controller;

import org.service.user.service.OsobaService;
import org.service.user.vm.OsobaVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value = "/osoba")
public class OsobaRestController {

    @Autowired
    private OsobaService osobaService;

    @PostMapping(value = "/")
    public ResponseEntity<OsobaVM> saveOsoba(
            @Valid @RequestBody OsobaVM osobaVM,
            UriComponentsBuilder builder
    ) throws IOException {
        osobaVM = osobaService.save(osobaVM);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/osoba/{id}").buildAndExpand(osobaVM.getId()).toUri());

        return new ResponseEntity<>(osobaVM, headers, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> updateOsoba(@Valid @RequestBody OsobaVM osobaVM, UriComponentsBuilder builder) {
        osobaVM = osobaService.updateOsoba(osobaVM);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/osoba/{id}").buildAndExpand(osobaVM.getId()).toUri());
        headers.add("alert", "osoba.updated");
        headers.add("params", osobaVM.getId().toString());
        return ResponseEntity.ok().headers(headers).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OsobaVM> findOsoba(@PathVariable("id") Long id) {
        OsobaVM osobaVM = osobaService.findOsoba(id);

        return osobaVM != null ? new ResponseEntity<>(osobaVM, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOsoba(@PathVariable("id") Long id) {
        osobaService.delete(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("alert", "osoba.deleted");
        headers.add("params", id.toString());
        return ResponseEntity.ok().headers(headers).build();
    }

    @GetMapping("/")
    public ResponseEntity<List<OsobaVM>> getAll() {
        return new ResponseEntity<>(osobaService.getAll(), HttpStatus.OK);
    }

    @PostMapping(value = "/filter", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<OsobaVM>> findOsobaFilter(
            @RequestBody OsobaVM osobaVM
    ) throws SQLException {
        List<OsobaVM> osobaVMList = osobaService.findOsobaFilter(osobaVM);
        return new ResponseEntity<>(osobaVMList, HttpStatus.OK);
    }

}
