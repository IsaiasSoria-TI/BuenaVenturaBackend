package com.buenaventura.erp.categoria.service;

import com.buenaventura.erp.categoria.dto.CategoriaRequest;
import com.buenaventura.erp.categoria.dto.CategoriaResponse;
import com.buenaventura.erp.categoria.entity.Categoria;
import com.buenaventura.erp.categoria.repository.CategoriaRepository;
import com.buenaventura.erp.cuentacontable.entity.CuentaContable;
import com.buenaventura.erp.cuentacontable.repository.CuentaContableRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CuentaContableRepository cuentaContableRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository,
                                CuentaContableRepository cuentaContableRepository) {
        this.categoriaRepository = categoriaRepository;
        this.cuentaContableRepository = cuentaContableRepository;
    }

    @Override
    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoriaResponse crear(CategoriaRequest request) {
        CuentaContable cuentaContable = cuentaContableRepository.findById(request.getIdCuentaContable())
                .orElseThrow(() -> new RuntimeException("Cuenta contable no encontrada"));

        Categoria categoria = new Categoria();
        categoria.setDescripcion(request.getDescripcion());
        categoria.setCuentaContable(cuentaContable);
        categoria.setEstado(
                request.getEstado() == null || request.getEstado().isBlank()
                        ? "Activo"
                        : request.getEstado()
        );

        return toResponse(categoriaRepository.save(categoria));
    }

    @Override
    public CategoriaResponse actualizar(Integer id, CategoriaRequest request) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        CuentaContable cuentaContable = cuentaContableRepository.findById(request.getIdCuentaContable())
                .orElseThrow(() -> new RuntimeException("Cuenta contable no encontrada"));

        categoria.setDescripcion(request.getDescripcion());
        categoria.setCuentaContable(cuentaContable);
        categoria.setEstado(
                request.getEstado() == null || request.getEstado().isBlank()
                        ? categoria.getEstado()
                        : request.getEstado()
        );

        return toResponse(categoriaRepository.save(categoria));
    }

    @Override
    public void eliminar(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        categoria.setEstado("Inactivo");
        categoriaRepository.save(categoria);
    }

    private CategoriaResponse toResponse(Categoria categoria) {
        CategoriaResponse response = new CategoriaResponse();
        response.setIdCategoria(categoria.getIdCategoria());
        response.setDescripcion(categoria.getDescripcion());
        response.setIdCuentaContable(categoria.getCuentaContable().getIdCuentaContable());
        response.setCodigoCuentaContable(
                categoria.getCuentaContable().getCodigo() + " - " + categoria.getCuentaContable().getDescripcion()
        );
        response.setEstado(categoria.getEstado());
        return response;
    }
}