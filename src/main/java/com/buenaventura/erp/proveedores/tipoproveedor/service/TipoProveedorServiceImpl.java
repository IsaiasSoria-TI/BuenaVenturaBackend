package com.buenaventura.erp.proveedores.tipoproveedor.service;

import com.buenaventura.erp.proveedores.tipoproveedor.entity.TipoProveedor;
import com.buenaventura.erp.proveedores.tipoproveedor.repository.TipoProveedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoProveedorServiceImpl implements TipoProveedorService {

    private final TipoProveedorRepository tipoProveedorRepository;

    public TipoProveedorServiceImpl(TipoProveedorRepository tipoProveedorRepository) {
        this.tipoProveedorRepository = tipoProveedorRepository;
    }

    @Override
    public List<TipoProveedor> listarTodos() {
        return tipoProveedorRepository.findAll();
    }

    @Override
    public List<TipoProveedor> listarActivos() {
        return tipoProveedorRepository.findByFlgActivoTrue();
    }

    @Override
    public TipoProveedor crear(TipoProveedor request) {
        TipoProveedor tipoProveedor = new TipoProveedor();
        tipoProveedor.setNombre(request.getNombre());
        tipoProveedor.setFlgActivo(
                request.getFlgActivo() == null ? true : request.getFlgActivo()
        );
        return tipoProveedorRepository.save(tipoProveedor);
    }

    @Override
    public TipoProveedor actualizar(Integer id, TipoProveedor request) {
        TipoProveedor tipoProveedor = tipoProveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de proveedor no encontrado"));

        tipoProveedor.setNombre(request.getNombre());
        if (request.getFlgActivo() != null) {
            tipoProveedor.setFlgActivo(request.getFlgActivo());
        }

        return tipoProveedorRepository.save(tipoProveedor);
    }

    @Override
    public void eliminar(Integer id) {
        TipoProveedor tipoProveedor = tipoProveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de proveedor no encontrado"));

        tipoProveedor.setFlgActivo(false);
        tipoProveedorRepository.save(tipoProveedor);
    }
}
