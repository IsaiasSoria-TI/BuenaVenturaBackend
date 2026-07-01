package com.buenaventura.erp.inventario.consultastock.service;

import com.buenaventura.erp.articulo.entity.Articulo;
import com.buenaventura.erp.inventario.consultastock.dto.HistorialMovimientoResponse;
import com.buenaventura.erp.inventario.consultastock.entity.ConsultaStockMovimiento;
import com.buenaventura.erp.inventario.consultastock.repository.ConsultaStockMovimientoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistorialMovimientoServiceImpl implements HistorialMovimientoService {

    private final ConsultaStockMovimientoRepository movimientoRepository;

    public HistorialMovimientoServiceImpl(ConsultaStockMovimientoRepository movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
    }

    @Override
    public List<HistorialMovimientoResponse> buscar(String periodo, Integer idArticulo, String busqueda) {
        String periodoFiltro = normalizeBlank(periodo);
        String busquedaFiltro = normalizeBlank(busqueda);
        String busquedaNumerica = normalizeNumericSearch(busquedaFiltro);

        return movimientoRepository
                .buscarHistorial(periodoFiltro, idArticulo, busquedaFiltro, busquedaNumerica)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private HistorialMovimientoResponse toResponse(ConsultaStockMovimiento movimiento) {
        Articulo articulo = movimiento.getArticulo();
        Integer idArticulo = articulo != null ? articulo.getIdArticulo() : null;

        HistorialMovimientoResponse response = new HistorialMovimientoResponse();
        response.setIdMovimiento(movimiento.getIdConsultaStockMovimiento());
        response.setIdArticulo(idArticulo);
        response.setCodigoArticulo(formatArticuloCode(idArticulo));
        response.setDescripcionArticulo(articulo != null ? articulo.getDescripcion() : null);
        response.setFechaMovimiento(movimiento.getFechaMovimiento());
        response.setPeriodo(movimiento.getPeriodo());
        response.setDocumento(movimiento.getDocumento());
        response.setCodigoMovimiento(movimiento.getCodigoMovimiento());
        response.setTipoMovimiento(movimiento.getTipoMovimiento());
        response.setProveedorMotivo(movimiento.getProveedorMotivo());
        response.setDetalle(movimiento.getDetalle());
        response.setTotalSoles(movimiento.getTotalSoles());
        response.setStockInicial(movimiento.getStockInicial());
        response.setMovimientoCantidad(movimiento.getMovimientoCantidad());
        response.setSaldo(movimiento.getSaldo());
        return response;
    }

    private String normalizeBlank(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private String normalizeNumericSearch(String value) {
        if (value == null) {
            return "";
        }

        String digits = value.replaceAll("\\D", "");

        if (digits.isBlank()) {
            return value;
        }

        String withoutLeadingZeros = digits.replaceFirst("^0+(?!$)", "");
        return withoutLeadingZeros.isBlank() ? digits : withoutLeadingZeros;
    }

    private String formatArticuloCode(Integer idArticulo) {
        if (idArticulo == null) {
            return null;
        }

        return "ART-" + String.format("%04d", idArticulo);
    }
}
