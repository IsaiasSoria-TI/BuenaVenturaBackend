package com.buenaventura.erp.compras.service;

import com.buenaventura.erp.articulo.entity.Articulo;
import com.buenaventura.erp.articulo.repository.ArticuloRepository;
import com.buenaventura.erp.compras.dto.CompraDetalleRequest;
import com.buenaventura.erp.compras.dto.CompraDetalleResponse;
import com.buenaventura.erp.compras.dto.CompraImpuestoRequest;
import com.buenaventura.erp.compras.dto.CompraImpuestoResponse;
import com.buenaventura.erp.compras.dto.CompraRequest;
import com.buenaventura.erp.compras.dto.CompraResponse;
import com.buenaventura.erp.compras.entity.Compra;
import com.buenaventura.erp.compras.entity.CompraDetalle;
import com.buenaventura.erp.compras.entity.CompraImpuesto;
import com.buenaventura.erp.compras.repository.CompraDetalleRepository;
import com.buenaventura.erp.compras.repository.CompraImpuestoRepository;
import com.buenaventura.erp.compras.repository.CompraRepository;
import com.buenaventura.erp.impuesto.entity.Impuesto;
import com.buenaventura.erp.impuesto.repository.ImpuestoRepository;
import com.buenaventura.erp.moneda.entity.Moneda;
import com.buenaventura.erp.moneda.repository.MonedaRepository;
import com.buenaventura.erp.pago.entity.Pago;
import com.buenaventura.erp.pago.repository.PagoRepository;
import com.buenaventura.erp.proveedores.entity.Proveedor;
import com.buenaventura.erp.proveedores.repository.ProveedorRepository;
import com.buenaventura.erp.tipocambio.entity.TipoCambio;
import com.buenaventura.erp.tipocambio.repository.TipoCambioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CompraServiceImpl implements CompraService {

    private static final BigDecimal CIEN = BigDecimal.valueOf(100);
    private static final BigDecimal PORCENTAJE_IGV_DEFAULT = BigDecimal.valueOf(18).setScale(2, RoundingMode.HALF_UP);
    private static final BigDecimal ZERO_2 = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    private final CompraRepository compraRepository;
    private final CompraDetalleRepository compraDetalleRepository;
    private final CompraImpuestoRepository compraImpuestoRepository;
    private final ImpuestoRepository impuestoRepository;
    private final PagoRepository pagoRepository;
    private final MonedaRepository monedaRepository;
    private final TipoCambioRepository tipoCambioRepository;
    private final ProveedorRepository proveedorRepository;
    private final ArticuloRepository articuloRepository;

    public CompraServiceImpl(
            CompraRepository compraRepository,
            CompraDetalleRepository compraDetalleRepository,
            CompraImpuestoRepository compraImpuestoRepository,
            ImpuestoRepository impuestoRepository,
            PagoRepository pagoRepository,
            MonedaRepository monedaRepository,
            TipoCambioRepository tipoCambioRepository,
            ProveedorRepository proveedorRepository,
            ArticuloRepository articuloRepository
    ) {
        this.compraRepository = compraRepository;
        this.compraDetalleRepository = compraDetalleRepository;
        this.compraImpuestoRepository = compraImpuestoRepository;
        this.impuestoRepository = impuestoRepository;
        this.pagoRepository = pagoRepository;
        this.monedaRepository = monedaRepository;
        this.tipoCambioRepository = tipoCambioRepository;
        this.proveedorRepository = proveedorRepository;
        this.articuloRepository = articuloRepository;
    }

    @Override
    public List<CompraResponse> listar() {
        List<Compra> compras = compraRepository.findTodasParaListado();
        if (compras.isEmpty()) {
            return List.of();
        }

        List<Integer> idsCompras = compras.stream()
                .map(Compra::getIdCompras)
                .toList();

        Map<Integer, List<CompraDetalle>> detallesPorCompra = compraDetalleRepository
                .findActivosByCompraIds(idsCompras)
                .stream()
                .collect(Collectors.groupingBy(detalle -> detalle.getCompra().getIdCompras()));

        Map<Integer, List<CompraImpuesto>> impuestosPorCompra = compraImpuestoRepository
                .findActivosByCompraIds(idsCompras)
                .stream()
                .collect(Collectors.groupingBy(impuesto -> impuesto.getCompra().getIdCompras()));

        return compras.stream()
                .map(compra -> toResponse(
                        compra,
                        detallesPorCompra.getOrDefault(compra.getIdCompras(), List.of()),
                        impuestosPorCompra.getOrDefault(compra.getIdCompras(), List.of())
                ))
                .toList();
    }

    @Override
    @Transactional
    public CompraResponse registrar(CompraRequest request) {
        validarRequest(request);

        Pago pago = pagoRepository.findById(request.getIdPago())
                .orElseThrow(() -> new RuntimeException("Condición de pago no encontrada"));

        Moneda moneda = monedaRepository.findById(request.getIdMoneda())
                .orElseThrow(() -> new RuntimeException("Moneda no encontrada"));

        TipoCambioCompra tipoCambioCompra = resolverTipoCambio(moneda, request);

        Proveedor proveedor = proveedorRepository.findById(request.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        Articulo primerArticulo = articuloRepository.findById(request.getDetalles().get(0).getIdArticulo())
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

        Impuesto primerImpuesto = resolverImpuestoCabecera(request);

        Compra compra = new Compra();
        compra.setPago(pago);
        compra.setMoneda(moneda);
        compra.setTipoCambio(tipoCambioCompra.tipoCambio());
        compra.setTipoCambioAplicado(tipoCambioCompra.valor());
        compra.setProveedor(proveedor);
        compra.setArticulo(primerArticulo);
        compra.setImpuesto(primerImpuesto);
        compra.setFechaCompras(request.getFechaCompras());
        compra.setZonaProduccion(request.getZonaProduccion());
        compra.setNumeroLote(request.getNumeroLote());
        compra.setEstado("Pendiente");
        compra.setFlgActivo(true);

        compra.setPeso(BigDecimal.ZERO);
        compra.setCostoKilo(BigDecimal.ZERO);
        compra.setCostoTotal(BigDecimal.ZERO);
        compra.setImporteImpuesto(BigDecimal.ZERO);
        compra.setAplicaIgv(Boolean.TRUE.equals(request.getAplicaIgv()));
        compra.setPorcentajeIgv(resolverPorcentajeIgv(request));
        compra.setImporteIgv(ZERO_2);

        Compra guardada = compraRepository.save(compra);

        TotalesCompra totales = guardarDetallesEImpuestos(guardada, request);

        guardada.setPeso(totales.pesoTotal());
        guardada.setCostoKilo(request.getDetalles().get(0).getCostoKilo());
        guardada.setCostoTotal(totales.totalGeneral());
        guardada.setImporteImpuesto(totales.totalImpuestos());
        guardada.setAplicaIgv(totales.aplicaIgv());
        guardada.setPorcentajeIgv(totales.porcentajeIgv());
        guardada.setImporteIgv(totales.importeIgv());

        Compra actualizada = compraRepository.save(guardada);

        return toResponse(actualizada);
    }

    @Override
    @Transactional
    public CompraResponse actualizar(Integer id, CompraRequest request) {
        validarRequest(request);

        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

        if (Boolean.FALSE.equals(compra.getFlgActivo())) {
            throw new RuntimeException("La compra está inactiva");
        }

        if ("Completo".equalsIgnoreCase(compra.getEstado())) {
            throw new RuntimeException("No se puede editar una compra completa");
        }

        Pago pago = pagoRepository.findById(request.getIdPago())
                .orElseThrow(() -> new RuntimeException("Condición de pago no encontrada"));

        Moneda moneda = monedaRepository.findById(request.getIdMoneda())
                .orElseThrow(() -> new RuntimeException("Moneda no encontrada"));

        TipoCambioCompra tipoCambioCompra = resolverTipoCambio(moneda, request, compra);

        Proveedor proveedor = proveedorRepository.findById(request.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        Articulo primerArticulo = articuloRepository.findById(request.getDetalles().get(0).getIdArticulo())
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

        Impuesto primerImpuesto = resolverImpuestoCabecera(request);

        inactivarDetalles(compra.getIdCompras());
        inactivarImpuestos(compra.getIdCompras());

        compra.setPago(pago);
        compra.setMoneda(moneda);
        compra.setTipoCambio(tipoCambioCompra.tipoCambio());
        compra.setTipoCambioAplicado(tipoCambioCompra.valor());
        compra.setProveedor(proveedor);
        compra.setArticulo(primerArticulo);
        compra.setImpuesto(primerImpuesto);
        compra.setFechaCompras(request.getFechaCompras());
        compra.setZonaProduccion(request.getZonaProduccion());
        compra.setNumeroLote(request.getNumeroLote());
        compra.setFechaActualizacion(LocalDateTime.now());
        compra.setEstado("Pendiente");
        compra.setAplicaIgv(Boolean.TRUE.equals(request.getAplicaIgv()));
        compra.setPorcentajeIgv(resolverPorcentajeIgv(request));
        compra.setImporteIgv(ZERO_2);

        Compra guardada = compraRepository.save(compra);

        TotalesCompra totales = guardarDetallesEImpuestos(guardada, request);

        guardada.setPeso(totales.pesoTotal());
        guardada.setCostoKilo(request.getDetalles().get(0).getCostoKilo());
        guardada.setCostoTotal(totales.totalGeneral());
        guardada.setImporteImpuesto(totales.totalImpuestos());
        guardada.setAplicaIgv(totales.aplicaIgv());
        guardada.setPorcentajeIgv(totales.porcentajeIgv());
        guardada.setImporteIgv(totales.importeIgv());

        Compra actualizada = compraRepository.save(guardada);

        return toResponse(actualizada);
    }

    @Override
    @Transactional
    public void eliminarLogico(Integer id) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

        if ("Completo".equalsIgnoreCase(compra.getEstado())) {
            throw new RuntimeException("No se puede eliminar una compra completa");
        }

        compra.setFlgActivo(false);
        compra.setFechaActualizacion(LocalDateTime.now());
        compraRepository.save(compra);

        inactivarDetalles(id);
        inactivarImpuestos(id);
    }

    private void validarRequest(CompraRequest request) {
        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new RuntimeException("Debe agregar al menos un artículo");
        }

        if (request.getPorcentajeIgv() != null && request.getPorcentajeIgv().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("El porcentaje de IGV no puede ser negativo");
        }
    }

    private TotalesCompra guardarDetallesEImpuestos(Compra compra, CompraRequest request) {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal pesoTotal = BigDecimal.ZERO;

        for (CompraDetalleRequest detalleRequest : request.getDetalles()) {
            Articulo articulo = articuloRepository.findById(detalleRequest.getIdArticulo())
                    .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

            BigDecimal costoTotalDetalle = detalleRequest.getPeso()
                    .multiply(detalleRequest.getCostoKilo())
                    .setScale(2, RoundingMode.HALF_UP);

            CompraDetalle detalle = new CompraDetalle();
            detalle.setCompra(compra);
            detalle.setArticulo(articulo);
            detalle.setPeso(detalleRequest.getPeso());
            detalle.setCostoKilo(detalleRequest.getCostoKilo());
            detalle.setCostoTotal(costoTotalDetalle);
            detalle.setFlgActivo(true);

            compraDetalleRepository.save(detalle);

            subtotal = subtotal.add(costoTotalDetalle);
            pesoTotal = pesoTotal.add(detalleRequest.getPeso());
        }

        BigDecimal subtotalTributario = convertirASoles(compra, subtotal);
        boolean aplicaIgv = Boolean.TRUE.equals(request.getAplicaIgv());
        BigDecimal porcentajeIgv = resolverPorcentajeIgv(request);
        BigDecimal importeIgv = aplicaIgv
                ? subtotalTributario.multiply(porcentajeIgv).divide(CIEN, 2, RoundingMode.HALF_UP)
                : ZERO_2;
        BigDecimal baseImpuestos = subtotalTributario.add(importeIgv);

        BigDecimal totalImpuestos = BigDecimal.ZERO;

        for (CompraImpuestoRequest impuestoRequest : obtenerImpuestosReferencialesRequest(request)) {
            if (impuestoRequest == null || impuestoRequest.getIdImpuesto() == null) {
                continue;
            }

            Impuesto impuesto = impuestoRepository.findById(impuestoRequest.getIdImpuesto())
                    .orElseThrow(() -> new RuntimeException("Impuesto no encontrado"));

            if (esIgv(impuesto)) {
                continue;
            }

            BigDecimal porcentaje = normalizarPorcentajeImpuesto(impuesto.getValor());
            BigDecimal importe = baseImpuestos
                    .multiply(porcentaje)
                    .divide(CIEN, 2, RoundingMode.HALF_UP);

            CompraImpuesto compraImpuesto = new CompraImpuesto();
            compraImpuesto.setCompra(compra);
            compraImpuesto.setImpuesto(impuesto);
            compraImpuesto.setPorcentaje(porcentaje);
            compraImpuesto.setImporte(importe);
            compraImpuesto.setFlgActivo(true);

            compraImpuestoRepository.save(compraImpuesto);

            totalImpuestos = totalImpuestos.add(importe);
        }

        return new TotalesCompra(
                pesoTotal.setScale(2, RoundingMode.HALF_UP),
                subtotalTributario.setScale(2, RoundingMode.HALF_UP),
                totalImpuestos.setScale(2, RoundingMode.HALF_UP),
                aplicaIgv,
                porcentajeIgv,
                importeIgv,
                baseImpuestos.setScale(2, RoundingMode.HALF_UP)
        );
    }

    private Impuesto resolverImpuestoCabecera(CompraRequest request) {
        for (CompraImpuestoRequest impuestoRequest : obtenerImpuestosReferencialesRequest(request)) {
            if (impuestoRequest == null || impuestoRequest.getIdImpuesto() == null) {
                continue;
            }

            Impuesto impuesto = impuestoRepository.findById(impuestoRequest.getIdImpuesto())
                    .orElseThrow(() -> new RuntimeException("Impuesto no encontrado"));

            if (!esIgv(impuesto)) {
                return impuesto;
            }
        }

        return null;
    }

    private TipoCambioCompra resolverTipoCambio(Moneda moneda, CompraRequest request) {
        if (esMonedaSoles(moneda)) {
            return new TipoCambioCompra(null, null);
        }

        if (request.getFechaCompras() == null) {
            throw new RuntimeException("La fecha de compra es obligatoria para resolver el tipo de cambio");
        }

        TipoCambio tipoCambio = tipoCambioRepository
                .findFirstByFechaLessThanEqualAndFlgActivoTrueOrderByFechaDesc(request.getFechaCompras().toLocalDate())
                .orElseThrow(() -> new RuntimeException("No existe tipo de cambio registrado para la fecha seleccionada."));

        return new TipoCambioCompra(tipoCambio, tipoCambio.getValor().setScale(4, RoundingMode.HALF_UP));
    }

    private TipoCambioCompra resolverTipoCambio(Moneda moneda, CompraRequest request, Compra compraActual) {
        if (esMonedaSoles(moneda)) {
            return new TipoCambioCompra(null, null);
        }

        boolean mismaMoneda = compraActual.getMoneda() != null
                && compraActual.getMoneda().getIdMoneda().equals(moneda.getIdMoneda());
        boolean mismaFecha = compraActual.getFechaCompras() != null
                && request.getFechaCompras() != null
                && compraActual.getFechaCompras().toLocalDate().equals(request.getFechaCompras().toLocalDate());

        if (mismaMoneda && mismaFecha && compraActual.getTipoCambioAplicado() != null) {
            return new TipoCambioCompra(
                    compraActual.getTipoCambio(),
                    compraActual.getTipoCambioAplicado().setScale(4, RoundingMode.HALF_UP)
            );
        }

        return resolverTipoCambio(moneda, request);
    }

    private boolean esMonedaSoles(Moneda moneda) {
        if (moneda == null) {
            return false;
        }

        String codigo = moneda.getCodigo() == null ? "" : moneda.getCodigo().trim();
        String nombre = moneda.getNombre() == null ? "" : moneda.getNombre().trim();
        return "PEN".equalsIgnoreCase(codigo)
                || "SOL".equalsIgnoreCase(codigo)
                || "SOLES".equalsIgnoreCase(nombre)
                || "SOL".equalsIgnoreCase(nombre);
    }

    private BigDecimal convertirASoles(Compra compra, BigDecimal importe) {
        if (compra.getTipoCambioAplicado() == null) {
            return importe.setScale(2, RoundingMode.HALF_UP);
        }

        return importe
                .multiply(compra.getTipoCambioAplicado())
                .setScale(2, RoundingMode.HALF_UP);
    }

    private List<CompraImpuestoRequest> obtenerImpuestosReferencialesRequest(CompraRequest request) {
        if (request.getImpuestos() == null) {
            return List.of();
        }

        return request.getImpuestos()
                .stream()
                .filter(impuestoRequest -> impuestoRequest != null && impuestoRequest.getIdImpuesto() != null)
                .collect(Collectors.toMap(
                        CompraImpuestoRequest::getIdImpuesto,
                        impuestoRequest -> impuestoRequest,
                        (primero, repetido) -> primero,
                        LinkedHashMap::new
                ))
                .values()
                .stream()
                .toList();
    }

    private BigDecimal resolverPorcentajeIgv(CompraRequest request) {
        BigDecimal porcentajeIgv = request.getPorcentajeIgv() == null
                ? PORCENTAJE_IGV_DEFAULT
                : request.getPorcentajeIgv();

        return porcentajeIgv.setScale(2, RoundingMode.HALF_UP);
    }

    private boolean esIgv(Impuesto impuesto) {
        if (impuesto == null || impuesto.getTipoImpuesto() == null) {
            return false;
        }

        String tipoNormalizado = impuesto.getTipoImpuesto().trim().replace(".", "").replace(" ", "");
        return "IGV".equalsIgnoreCase(tipoNormalizado);
    }

    private BigDecimal normalizarPorcentajeImpuesto(BigDecimal porcentaje) {
        return porcentaje == null ? ZERO_2 : porcentaje.setScale(2, RoundingMode.HALF_UP);
    }

    private void inactivarDetalles(Integer idCompras) {
        List<CompraDetalle> detalles = compraDetalleRepository
                .findByCompra_IdComprasAndFlgActivoTrueOrderByIdCompraDetalleAsc(idCompras);

        detalles.forEach(detalle -> {
            detalle.setFlgActivo(false);
            detalle.setFechaActualizacion(LocalDateTime.now());
        });

        compraDetalleRepository.saveAll(detalles);
    }

    private void inactivarImpuestos(Integer idCompras) {
        List<CompraImpuesto> impuestos = compraImpuestoRepository
                .findByCompra_IdComprasAndFlgActivoTrueOrderByIdCompraImpuestoAsc(idCompras);

        impuestos.forEach(impuesto -> {
            impuesto.setFlgActivo(false);
            impuesto.setFechaActualizacion(LocalDateTime.now());
        });

        compraImpuestoRepository.saveAll(impuestos);
    }

    private CompraResponse toResponse(Compra compra) {
        return toResponse(
                compra,
                obtenerDetallesActivos(compra.getIdCompras()),
                obtenerImpuestosActivos(compra.getIdCompras())
        );
    }

    private CompraResponse toResponse(
            Compra compra,
            List<CompraDetalle> detalles,
            List<CompraImpuesto> impuestos
    ) {
        CompraResponse response = new CompraResponse();

        response.setIdCompras(compra.getIdCompras());

        if (compra.getPago() != null) {
            response.setIdPago(compra.getPago().getIdPago());
            response.setPago(compra.getPago().getPago());
        }

        if (compra.getMoneda() != null) {
            response.setIdMoneda(compra.getMoneda().getIdMoneda());
            response.setCodigoMoneda(compra.getMoneda().getCodigo());
            response.setMoneda(compra.getMoneda().getNombre());
            response.setSimboloMoneda(compra.getMoneda().getSimbolo());
        }

        if (compra.getTipoCambio() != null) {
            response.setIdTipoCambio(compra.getTipoCambio().getIdTipoCambio());
        }
        response.setTipoCambioAplicado(compra.getTipoCambioAplicado());

        if (compra.getProveedor() != null) {
            response.setIdProveedor(compra.getProveedor().getIdProveedor());
            response.setRuc(compra.getProveedor().getRuc());
            response.setRazonSocial(compra.getProveedor().getRazonSocial());
            response.setDireccion(compra.getProveedor().getDireccion());
        }

        response.setFechaCompras(compra.getFechaCompras());
        response.setZonaProduccion(compra.getZonaProduccion());
        response.setNumeroLote(compra.getNumeroLote());
        response.setPeso(compra.getPeso());
        response.setCostoTotal(compra.getCostoTotal());
        response.setImporteImpuesto(compra.getImporteImpuesto());
        response.setAplicaIgv(Boolean.TRUE.equals(compra.getAplicaIgv()));
        response.setPorcentajeIgv(compra.getPorcentajeIgv() == null ? PORCENTAJE_IGV_DEFAULT : compra.getPorcentajeIgv());
        response.setImporteIgv(compra.getImporteIgv() == null ? ZERO_2 : compra.getImporteIgv());
        response.setTotalGeneral(compra.getCostoTotal());
        response.setEstado(compra.getEstado());
        response.setFlgActivo(Boolean.TRUE.equals(compra.getFlgActivo()));

        response.setDetalles(toDetalleResponse(detalles));
        response.setImpuestos(toImpuestoResponse(impuestos));

        return response;
    }

    private List<CompraDetalle> obtenerDetallesActivos(Integer idCompras) {
        return compraDetalleRepository
                .findByCompra_IdComprasAndFlgActivoTrueOrderByIdCompraDetalleAsc(idCompras);
    }

    private List<CompraImpuesto> obtenerImpuestosActivos(Integer idCompras) {
        return compraImpuestoRepository
                .findByCompra_IdComprasAndFlgActivoTrueOrderByIdCompraImpuestoAsc(idCompras);
    }

    private List<CompraDetalleResponse> toDetalleResponse(List<CompraDetalle> detalles) {
        return detalles
                .stream()
                .map(detalle -> {
                    CompraDetalleResponse response = new CompraDetalleResponse();
                    response.setIdCompraDetalle(detalle.getIdCompraDetalle());
                    response.setIdArticulo(detalle.getArticulo().getIdArticulo());
                    response.setDescripcionArticulo(detalle.getArticulo().getDescripcion());
                    response.setMedida(detalle.getArticulo().getMedida());
                    response.setPeso(detalle.getPeso());
                    response.setCostoKilo(detalle.getCostoKilo());
                    response.setCostoTotal(detalle.getCostoTotal());
                    return response;
                })
                .toList();
    }

    private List<CompraImpuestoResponse> toImpuestoResponse(List<CompraImpuesto> impuestos) {
        return impuestos
                .stream()
                .filter(impuesto -> !esIgv(impuesto.getImpuesto()))
                .collect(Collectors.toMap(
                        impuesto -> impuesto.getImpuesto().getIdImpuesto(),
                        impuesto -> impuesto,
                        (primero, repetido) -> primero,
                        LinkedHashMap::new
                ))
                .values()
                .stream()
                .map(impuesto -> {
                    CompraImpuestoResponse response = new CompraImpuestoResponse();
                    response.setIdCompraImpuesto(impuesto.getIdCompraImpuesto());
                    response.setIdImpuesto(impuesto.getImpuesto().getIdImpuesto());
                    response.setTipoImpuesto(impuesto.getImpuesto().getTipoImpuesto());
                    response.setPorcentaje(impuesto.getPorcentaje());
                    response.setImporte(impuesto.getImporte());
                    return response;
                })
                .toList();
    }

    private record TotalesCompra(
            BigDecimal pesoTotal,
            BigDecimal subtotal,
            BigDecimal totalImpuestos,
            boolean aplicaIgv,
            BigDecimal porcentajeIgv,
            BigDecimal importeIgv,
            BigDecimal totalGeneral
    ) {
    }

    private record TipoCambioCompra(
            TipoCambio tipoCambio,
            BigDecimal valor
    ) {
    }
}
