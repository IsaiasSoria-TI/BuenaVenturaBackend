package com.buenaventura.erp.inventario.consultastock.service;

import com.buenaventura.erp.articulo.repository.ArticuloRepository;
import com.buenaventura.erp.inventario.consultastock.dto.ConsultaStockMovimientoResponse;
import com.buenaventura.erp.inventario.consultastock.entity.ConsultaStockMovimiento;
import com.buenaventura.erp.inventario.consultastock.repository.ConsultaStockMovimientoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaStockServiceImpl implements ConsultaStockService {

    private final ConsultaStockMovimientoRepository movimientoRepository;
    private final ArticuloRepository articuloRepository;

    public ConsultaStockServiceImpl(ConsultaStockMovimientoRepository movimientoRepository,
                                    ArticuloRepository articuloRepository) {
        this.movimientoRepository = movimientoRepository;
        this.articuloRepository = articuloRepository;
    }

    @Override
    public List<ConsultaStockMovimientoResponse> consultar(String periodo, Integer idArticulo) {
        if (periodo == null || periodo.isBlank()) {
            throw new RuntimeException("El periodo es obligatorio");
        }

        if (idArticulo == null) {
            throw new RuntimeException("El articulo es obligatorio");
        }

        if (!articuloRepository.existsById(idArticulo)) {
            throw new RuntimeException("Articulo no encontrado");
        }

        return movimientoRepository
                .findByPeriodoAndArticulo_IdArticuloAndFlgActivoTrueOrderByFechaMovimientoAscIdConsultaStockMovimientoAsc(
                        periodo,
                        idArticulo
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ConsultaStockMovimientoResponse toResponse(ConsultaStockMovimiento movimiento) {
        ConsultaStockMovimientoResponse response = new ConsultaStockMovimientoResponse();
        response.setIdConsultaStockMovimiento(movimiento.getIdConsultaStockMovimiento());
        response.setIdArticulo(
                movimiento.getArticulo() != null ? movimiento.getArticulo().getIdArticulo() : null
        );
        response.setDescripcionArticulo(
                movimiento.getArticulo() != null ? movimiento.getArticulo().getDescripcion() : null
        );
        response.setFechaMovimiento(movimiento.getFechaMovimiento());
        response.setPeriodo(movimiento.getPeriodo());
        response.setDocumento(movimiento.getDocumento());
        response.setTipoMovimiento(movimiento.getTipoMovimiento());
        response.setStockInicial(movimiento.getStockInicial());
        response.setMovimientoCantidad(movimiento.getMovimientoCantidad());
        response.setSaldo(movimiento.getSaldo());
        return response;
    }
}
