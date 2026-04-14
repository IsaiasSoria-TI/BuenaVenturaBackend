package com.buenaventura.erp.proveedores.banco.controller;

import com.buenaventura.erp.proveedores.banco.dto.BancoResponse;
import com.buenaventura.erp.proveedores.banco.repository.BancoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bancos")
public class BancoController {

    private final BancoRepository bancoRepository;

    public BancoController(BancoRepository bancoRepository) {
        this.bancoRepository = bancoRepository;
    }

    @GetMapping
    public List<BancoResponse> listar() {
        return bancoRepository.findAll().stream()
                .filter(b -> Boolean.TRUE.equals(b.getFlgActivo()))
                .map(b -> new BancoResponse(b.getIdBanco(), b.getNombre()))
                .toList();
    }
}