package com.buenaventura.erp.articulo.controller;

import com.buenaventura.erp.articulo.dto.ArticuloRequest;
import com.buenaventura.erp.articulo.dto.ArticuloResponse;
import com.buenaventura.erp.articulo.service.ArticuloService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articulos")
public class ArticuloController {

    private final ArticuloService articuloService;

    public ArticuloController(ArticuloService articuloService) {
        this.articuloService = articuloService;
    }

    @GetMapping
    public List<ArticuloResponse> listar() {
        return articuloService.listar();
    }

    @PostMapping
    public ArticuloResponse registrar(@Valid @RequestBody ArticuloRequest request) {
        return articuloService.registrar(request);
    }

    @PutMapping("/{id}")
    public ArticuloResponse actualizar(@PathVariable Integer id,
                                       @Valid @RequestBody ArticuloRequest request) {
        return articuloService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public void eliminarLogico(@PathVariable Integer id) {
        articuloService.eliminarLogico(id);
    }
}