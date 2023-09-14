package com.cycles.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;

import com.cycles.rest.entity.Cycle;
import com.cycles.rest.repository.CyclesRepository;

@RestController
@RequestMapping("/api/cycles")
public class CyclesController {

    @Autowired
    private CyclesRepository cyclesRepository;

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    List<Cycle> getAllCycles() {
        return cyclesRepository.findAll();
    }

    @PutMapping("/{id}/borrow")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    Cycle borrowCycle(@PathVariable("id") Long id) {
        Optional<Cycle> cycle = cyclesRepository.findById(id);
        if (cycle.isPresent()) {
            Cycle c = cycle.get();
            c.setQuantity(c.getQuantity() - 1);
            return cyclesRepository.save(c);
        } else {
            return null;
        }
    }

    @PutMapping("/{id}/return")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    Cycle returnCycle(@PathVariable("id") Long id) {
        Optional<Cycle> cycle = cyclesRepository.findById(id);
        if (cycle.isPresent()) {
            Cycle c = cycle.get();
            c.setQuantity(c.getQuantity() + 1);
            return cyclesRepository.save(c);
        } else {
            return null;
        }
    }

    @PutMapping("/{id}/restock")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    Cycle restockCycle(@PathVariable("id") Long id, @RequestParam("quantity") int quantity) {
        Optional<Cycle> cycle = cyclesRepository.findById(id);
        if (cycle.isPresent()) {
            Cycle c = cycle.get();
            c.setQuantity(c.getQuantity() + quantity);
            return cyclesRepository.save(c);
        } else {
            return null;
        }
    }

}
