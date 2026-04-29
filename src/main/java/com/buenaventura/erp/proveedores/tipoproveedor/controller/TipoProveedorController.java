package com.buenaventura.erp.proveedores.tipoproveedor.controller;

import com.buenaventura.erp.proveedores.tipoproveedor.entity.TipoProveedor;
import com.buenaventura.erp.proveedores.tipoproveedor.service.TipoProveedorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-proveedor")
@CrossOrigin(origins = "*")
public class TipoProveedorController {

    private final TipoProveedorService tipoProveedorService;

    public TipoProveedorController(TipoProveedorService tipoProveedorService) {
        this.tipoProveedorService = tipoProveedorService;
    }

    @GetMapping
    public List<TipoProveedor> listarActivos() {
        return tipoProveedorService.listarActivos();
    }

    @GetMapping("/todos")
    public List<TipoProveedor> listarTodos() {
        return tipoProveedorService.listarTodos();
    }

    @PostMapping
    public TipoProveedor crear(@RequestBody TipoProveedor request) {
        return tipoProveedorService.crear(request);
    }

    @PutMapping("/{id}")
    public TipoProveedor actualizar(
            @PathVariable Integer id,
            @RequestBody TipoProveedor request
    ) {
        return tipoProveedorService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        tipoProveedorService.eliminar(id);
    }
}
