package com.buenaventura.erp.articulo.service;

import com.buenaventura.erp.articulo.dto.ArticuloRequest;
import com.buenaventura.erp.articulo.dto.ArticuloResponse;
import com.buenaventura.erp.articulo.entity.Articulo;
import com.buenaventura.erp.articulo.repository.ArticuloRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticuloServiceImpl implements ArticuloService {

    private final ArticuloRepository articuloRepository;

    public ArticuloServiceImpl(ArticuloRepository articuloRepository) {
        this.articuloRepository = articuloRepository;
    }

    @Override
    public List<ArticuloResponse> listar() {
        return articuloRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ArticuloResponse registrar(ArticuloRequest request) {
        Articulo articulo = new Articulo();
        articulo.setDescripcion(request.getDescripcion());
        articulo.setMedida(request.getMedida());
        articulo.setStock(request.getStock());
        articulo.setEstado(
                request.getEstado() == null || request.getEstado().isBlank()
                        ? "Activo"
                        : request.getEstado()
        );

        Articulo guardado = articuloRepository.save(articulo);
        return toResponse(guardado);
    }

    @Override
    public ArticuloResponse actualizar(Integer id, ArticuloRequest request) {
        Articulo articulo = articuloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

        articulo.setDescripcion(request.getDescripcion());
        articulo.setMedida(request.getMedida());
        articulo.setStock(request.getStock());
        articulo.setEstado(request.getEstado());

        Articulo actualizado = articuloRepository.save(articulo);
        return toResponse(actualizado);
    }

    @Override
    public void eliminarLogico(Integer id) {
        Articulo articulo = articuloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

        articulo.setEstado("Inactivo");
        articuloRepository.save(articulo);
    }

    private ArticuloResponse toResponse(Articulo articulo) {
        ArticuloResponse response = new ArticuloResponse();
        response.setIdArticulo(articulo.getIdArticulo());
        response.setDescripcion(articulo.getDescripcion());
        response.setMedida(articulo.getMedida());
        response.setStock(articulo.getStock());
        response.setEstado(articulo.getEstado());
        return response;
    }
}