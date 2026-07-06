package com.buenaventura.erp.inventario.consultastock.service;

import com.buenaventura.erp.articulo.entity.Articulo;
import com.buenaventura.erp.articulo.repository.ArticuloRepository;
import com.buenaventura.erp.inventario.consultastock.dto.HistorialMovimientoResponse;
import com.buenaventura.erp.inventario.consultastock.dto.MovimientoManualRequest;
import com.buenaventura.erp.inventario.consultastock.entity.ConsultaStockMovimiento;
import com.buenaventura.erp.inventario.consultastock.repository.ConsultaStockMovimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class HistorialMovimientoServiceImpl implements HistorialMovimientoService {

    private static final String TIPO_INGRESO = "INGRESO";
    private static final String TIPO_SALIDA = "SALIDA";
    private static final String REFERENCIA_MANUAL = "MANUAL";
    private static final DateTimeFormatter PERIODO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    private final ConsultaStockMovimientoRepository movimientoRepository;
    private final ArticuloRepository articuloRepository;

    public HistorialMovimientoServiceImpl(ConsultaStockMovimientoRepository movimientoRepository,
                                          ArticuloRepository articuloRepository) {
        this.movimientoRepository = movimientoRepository;
        this.articuloRepository = articuloRepository;
    }

    @Override
    public List<HistorialMovimientoResponse> buscar(String periodo, Integer idArticulo, String busqueda) {
        String periodoFiltro = normalizeBlank(periodo);
        String busquedaFiltro = normalizeBlank(busqueda);
        String busquedaNumerica = normalizeNumericSearch(busquedaFiltro);
        boolean buscarIngreso = matchesIngreso(busquedaFiltro);
        boolean buscarSalida = matchesSalida(busquedaFiltro);

        return movimientoRepository
                .buscarHistorial(periodoFiltro, idArticulo, busquedaFiltro, busquedaNumerica, buscarIngreso, buscarSalida)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public HistorialMovimientoResponse registrarManual(MovimientoManualRequest request) {
        Articulo articulo = articuloRepository.findById(request.getIdArticulo())
                .orElseThrow(() -> new RuntimeException("Articulo no encontrado"));

        String tipoMovimiento = normalizeTipoMovimiento(request.getTipoMovimiento());
        BigDecimal cantidad = request.getCantidad();
        BigDecimal stockInicial = articulo.getStock() == null ? BigDecimal.ZERO : articulo.getStock();
        BigDecimal movimientoCantidad = TIPO_SALIDA.equals(tipoMovimiento) ? cantidad.negate() : cantidad;
        BigDecimal saldo = stockInicial.add(movimientoCantidad);

        if (saldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("La salida excede el stock disponible");
        }

        LocalDateTime fechaTransaccion = request.getFechaTransaccion();
        ConsultaStockMovimiento movimiento = new ConsultaStockMovimiento();
        movimiento.setArticulo(articulo);
        movimiento.setFechaMovimiento(fechaTransaccion);
        movimiento.setPeriodo(fechaTransaccion.format(PERIODO_FORMATTER));
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setProveedorMotivo(normalizeBlank(request.getMotivo()));
        movimiento.setDetalle(resolverDetalleManual(request, articulo));
        movimiento.setTotalSoles(request.getTotalSoles().setScale(2, RoundingMode.HALF_UP));
        movimiento.setStockInicial(stockInicial);
        movimiento.setMovimientoCantidad(movimientoCantidad);
        movimiento.setSaldo(saldo);
        movimiento.setReferenciaTipo(REFERENCIA_MANUAL);
        movimiento.setFlgActivo(true);

        ConsultaStockMovimiento guardado = movimientoRepository.save(movimiento);
        String codigo = formatCodigoManual(tipoMovimiento, guardado.getIdConsultaStockMovimiento());
        guardado.setCodigoMovimiento(codigo);
        guardado.setDocumento(codigo);
        guardado.setReferenciaId(guardado.getIdConsultaStockMovimiento());

        articulo.setStock(saldo);
        articuloRepository.save(articulo);

        return toResponse(movimientoRepository.save(guardado));
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
        response.setFechaTransaccion(movimiento.getFechaMovimiento());
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

    private boolean matchesIngreso(String value) {
        if (value == null) {
            return false;
        }

        String normalized = value.trim().toUpperCase();
        return normalized.contains(TIPO_INGRESO)
                || normalized.contains("ENTRADA")
                || normalized.contains("COMPRA");
    }

    private boolean matchesSalida(String value) {
        if (value == null) {
            return false;
        }

        String normalized = value.trim().toUpperCase();
        return normalized.contains(TIPO_SALIDA)
                || normalized.contains("VENTA");
    }

    private String normalizeTipoMovimiento(String tipoMovimiento) {
        String normalized = normalizeBlank(tipoMovimiento);
        if (normalized == null) {
            throw new RuntimeException("El tipo de movimiento es obligatorio");
        }

        String upper = normalized.toUpperCase();
        if ("ENTRADA".equals(upper) || "COMPRA".equals(upper)) {
            return TIPO_INGRESO;
        }

        if ("VENTA".equals(upper)) {
            return TIPO_SALIDA;
        }

        if (TIPO_INGRESO.equals(upper) || TIPO_SALIDA.equals(upper)) {
            return upper;
        }

        throw new RuntimeException("El tipo de movimiento debe ser INGRESO o SALIDA");
    }

    private String resolverDetalleManual(MovimientoManualRequest request, Articulo articulo) {
        String detalle = normalizeBlank(request.getDetalle());
        String responsable = normalizeBlank(request.getResponsable());
        String descripcion = articulo.getDescripcion() != null ? articulo.getDescripcion() : "articulo";

        StringBuilder builder = new StringBuilder();
        builder.append(detalle != null ? detalle : "Movimiento manual de " + descripcion);

        if (responsable != null) {
            builder.append(" | Responsable: ").append(responsable);
        }

        return builder.toString();
    }

    private String formatCodigoManual(String tipoMovimiento, Long idMovimiento) {
        String prefix = TIPO_SALIDA.equals(tipoMovimiento) ? "SAL" : "ING";
        return prefix + "-" + String.format("%04d", idMovimiento);
    }

    private String formatArticuloCode(Integer idArticulo) {
        if (idArticulo == null) {
            return null;
        }

        return "ART-" + String.format("%04d", idArticulo);
    }
}
