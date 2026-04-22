package com.buenaventura.erp.cuentaspagar.service;

import com.buenaventura.erp.compras.entity.Compra;
import com.buenaventura.erp.compras.repository.CompraRepository;
import com.buenaventura.erp.cuentaspagar.dto.CompraValidaResponse;
import com.buenaventura.erp.cuentaspagar.dto.CuentaPagarDetalleCompraResponse;
import com.buenaventura.erp.cuentaspagar.dto.CuentaPagarRecepcionDisponibleResponse;
import com.buenaventura.erp.cuentaspagar.dto.CuentaPagarRequest;
import com.buenaventura.erp.cuentaspagar.dto.CuentaPagarResponse;
import com.buenaventura.erp.cuentaspagar.entity.CuentaPagar;
import com.buenaventura.erp.cuentaspagar.repository.CuentaPagarRepository;
import com.buenaventura.erp.recepciones.entity.Recepcion;
import com.buenaventura.erp.recepciones.repository.RecepcionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CuentaPagarServiceImpl implements CuentaPagarService {

    private static final String ESTADO_PENDIENTE = "Pendiente";
    private static final String ESTADO_COMPLETA_PARCIAL = "Completa parcial";
    private static final String ESTADO_COMPLETA = "Completa";

    private final CuentaPagarRepository cuentaPagarRepository;
    private final CompraRepository compraRepository;
    private final RecepcionRepository recepcionRepository;

    public CuentaPagarServiceImpl(CuentaPagarRepository cuentaPagarRepository,
                                  CompraRepository compraRepository,
                                  RecepcionRepository recepcionRepository) {
        this.cuentaPagarRepository = cuentaPagarRepository;
        this.compraRepository = compraRepository;
        this.recepcionRepository = recepcionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaPagarResponse> listar() {
        return cuentaPagarRepository.findByFlgActivoTrueOrderByFechaCreacionDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompraValidaResponse> listarComprasValidas() {
        return compraRepository.findByFlgActivoTrueOrderByFechaComprasDesc()
                .stream()
                .filter(this::tieneRecepcionesDisponiblesParaCuentaPagar)
                .map(this::toCompraValidaResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaPagarDetalleCompraResponse verDetalleCompra(Integer idCompras) {
        Compra compra = compraRepository.findById(idCompras)
                .orElseThrow(() -> new RuntimeException("La compra no existe"));

        List<CuentaPagarRecepcionDisponibleResponse> recepcionesDisponibles = recepcionRepository.obtenerPorCompra(idCompras)
                .stream()
                .filter(this::esRecepcionValidaParaCuentaPagar)
                .filter(r -> !cuentaPagarRepository.existsByIdRecepcionesAndFlgActivoTrue(r.getIdRecepciones()))
                .map(this::toRecepcionDisponibleResponse)
                .toList();

        if (recepcionesDisponibles.isEmpty()) {
            throw new RuntimeException("La compra no tiene recepciones disponibles para cuentas por pagar");
        }

        CuentaPagarDetalleCompraResponse response = new CuentaPagarDetalleCompraResponse();
        response.setIdCompras(compra.getIdCompras());
        response.setNumeroOperacion(compra.getIdCompras());
        response.setRuc(compra.getProveedor().getRuc());
        response.setRazonSocial(compra.getProveedor().getRazonSocial());

        if (compra.getArticulo() != null) {
            response.setCodArticulo(compra.getArticulo().getIdArticulo());
            response.setDescripcionArticulo(compra.getArticulo().getDescripcion());
        }

        response.setImporte(compra.getCostoTotal());
        response.setDeduccionRetencion(compra.getImporteImpuesto());
        response.setTipoDetRet(compra.getImpuesto().getTipoImpuesto());
        response.setPorcentajeImpuesto(compra.getImpuesto().getValor());
        response.setCondicionPago(compra.getPago().getPago());
        response.setEstadoCompra(compra.getEstado());
        response.setRecepcionesDisponibles(recepcionesDisponibles);

        return response;
    }

    @Override
    public List<CuentaPagarResponse> registrar(CuentaPagarRequest request) {
        validarRequestRegistro(request);

        return request.getDetalles()
                .stream()
                .map(detalle -> registrarDetalle(request, detalle))
                .toList();
    }

    @Override
    public CuentaPagarResponse actualizar(Integer id, CuentaPagarRequest request) {
        validarRequestRegistro(request);

        if (request.getDetalles().size() != 1) {
            throw new RuntimeException("Para actualizar debes enviar un solo detalle");
        }

        CuentaPagar cuentaPagar = cuentaPagarRepository.findByIdCuentaPagarAndFlgActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Cuenta por pagar no encontrada"));

        CuentaPagarRequest.CuentaPagarRegistroDetalleRequest detalle = request.getDetalles().get(0);

        Compra compra = compraRepository.findById(detalle.getIdCompras())
                .orElseThrow(() -> new RuntimeException("La compra no existe"));

        Recepcion recepcion = recepcionRepository.findById(detalle.getIdRecepciones())
                .orElseThrow(() -> new RuntimeException("La recepción no existe"));

        if (!recepcion.getCompra().getIdCompras().equals(compra.getIdCompras())) {
            throw new RuntimeException("La recepción no pertenece a la compra enviada");
        }

        validarEstadoRecepcion(recepcion.getEstado());

        if (!cuentaPagar.getIdRecepciones().equals(detalle.getIdRecepciones())
                && cuentaPagarRepository.existsByIdRecepcionesAndFlgActivoTrue(detalle.getIdRecepciones())) {
            throw new RuntimeException("La recepción ya tiene una cuenta por pagar registrada");
        }

        cuentaPagar.setIdCompras(detalle.getIdCompras());
        cuentaPagar.setIdRecepciones(detalle.getIdRecepciones());
        cuentaPagar.setNumeroFactura(obtenerNumeroFactura(request, detalle));
        cuentaPagar.setMoneda(request.getMoneda().trim());
        cuentaPagar.setCodigoDetRet(request.getCodigoDetRet().trim());

        if (cuentaPagar.getEstado() == null || cuentaPagar.getEstado().isBlank()) {
            cuentaPagar.setEstado(ESTADO_PENDIENTE);
        }

        cuentaPagar.setFechaActualizacion(LocalDateTime.now());

        CuentaPagar actualizado = cuentaPagarRepository.save(cuentaPagar);
        return toResponse(actualizado, compra, recepcion);
    }

    @Override
    public void eliminar(Integer id) {
        CuentaPagar cuentaPagar = cuentaPagarRepository.findByIdCuentaPagarAndFlgActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Cuenta por pagar no encontrada"));

        cuentaPagar.setFlgActivo(false);
        cuentaPagar.setFechaActualizacion(LocalDateTime.now());

        cuentaPagarRepository.save(cuentaPagar);
    }

    private CuentaPagarResponse registrarDetalle(
            CuentaPagarRequest request,
            CuentaPagarRequest.CuentaPagarRegistroDetalleRequest detalle
    ) {
        Compra compra = compraRepository.findById(detalle.getIdCompras())
                .orElseThrow(() -> new RuntimeException("La compra no existe"));

        Recepcion recepcion = recepcionRepository.findById(detalle.getIdRecepciones())
                .orElseThrow(() -> new RuntimeException("La recepción no existe"));

        if (!recepcion.getCompra().getIdCompras().equals(compra.getIdCompras())) {
            throw new RuntimeException("La recepción no pertenece a la compra enviada");
        }

        validarEstadoRecepcion(recepcion.getEstado());

        if (cuentaPagarRepository.existsByIdRecepcionesAndFlgActivoTrue(detalle.getIdRecepciones())) {
            throw new RuntimeException("La recepción ya tiene una cuenta por pagar registrada");
        }

        CuentaPagar cuentaPagar = new CuentaPagar();
        cuentaPagar.setIdCompras(compra.getIdCompras());
        cuentaPagar.setIdRecepciones(recepcion.getIdRecepciones());
        cuentaPagar.setNumeroFactura(obtenerNumeroFactura(request, detalle));
        cuentaPagar.setMoneda(request.getMoneda().trim());
        cuentaPagar.setCodigoDetRet(request.getCodigoDetRet().trim());
        cuentaPagar.setEstado(ESTADO_PENDIENTE);
        cuentaPagar.setFlgActivo(true);
        cuentaPagar.setFechaActualizacion(LocalDateTime.now());

        CuentaPagar guardado = cuentaPagarRepository.save(cuentaPagar);
        return toResponse(guardado, compra, recepcion);
    }

    private boolean tieneRecepcionesDisponiblesParaCuentaPagar(Compra compra) {
        return recepcionRepository.obtenerPorCompra(compra.getIdCompras())
                .stream()
                .anyMatch(r -> esRecepcionValidaParaCuentaPagar(r)
                        && !cuentaPagarRepository.existsByIdRecepcionesAndFlgActivoTrue(r.getIdRecepciones()));
    }

    private boolean esRecepcionValidaParaCuentaPagar(Recepcion recepcion) {
        return ESTADO_COMPLETA_PARCIAL.equalsIgnoreCase(recepcion.getEstado())
                || ESTADO_COMPLETA.equalsIgnoreCase(recepcion.getEstado());
    }

    private void validarEstadoRecepcion(String estadoRecepcion) {
        if (estadoRecepcion == null || estadoRecepcion.isBlank()) {
            throw new RuntimeException("La recepción no tiene estado válido");
        }

        boolean recepcionValida = ESTADO_COMPLETA_PARCIAL.equalsIgnoreCase(estadoRecepcion)
                || ESTADO_COMPLETA.equalsIgnoreCase(estadoRecepcion);

        if (!recepcionValida) {
            throw new RuntimeException("Solo se permiten recepciones en estado 'Completa parcial' o 'Completa'");
        }
    }

    private void validarRequestRegistro(CuentaPagarRequest request) {
        if ("UNICA".equalsIgnoreCase(request.getTipoFactura())) {
            if (request.getNumeroFactura() == null || request.getNumeroFactura().isBlank()) {
                throw new RuntimeException("Para tipo de factura UNICA, el número de factura es obligatorio");
            }
        }

        if ("MULTIPLE".equalsIgnoreCase(request.getTipoFactura())) {
            for (CuentaPagarRequest.CuentaPagarRegistroDetalleRequest detalle : request.getDetalles()) {
                if (detalle.getNumeroFactura() == null || detalle.getNumeroFactura().isBlank()) {
                    throw new RuntimeException("Para tipo de factura MULTIPLE, cada detalle debe tener número de factura");
                }
            }
        }
    }

    private String obtenerNumeroFactura(
            CuentaPagarRequest request,
            CuentaPagarRequest.CuentaPagarRegistroDetalleRequest detalle
    ) {
        if ("UNICA".equalsIgnoreCase(request.getTipoFactura())) {
            return request.getNumeroFactura().trim();
        }

        return detalle.getNumeroFactura().trim();
    }

    private CompraValidaResponse toCompraValidaResponse(Compra compra) {
        CompraValidaResponse response = new CompraValidaResponse();
        response.setIdCompras(compra.getIdCompras());
        response.setFechaCompras(compra.getFechaCompras());
        response.setPesoComprado(compra.getPeso());
        response.setEstado(compra.getEstado());
        response.setRazonSocial(compra.getProveedor().getRazonSocial());
        response.setRuc(compra.getProveedor().getRuc());

        if (compra.getArticulo() != null) {
            response.setArticulo(compra.getArticulo().getDescripcion());
            response.setMedida(compra.getArticulo().getMedida());
        }

        response.setZonaProduccion(compra.getZonaProduccion());
        response.setHectareas(compra.getHectareas());
        response.setCostoKilo(compra.getCostoKilo());
        response.setCostoTotal(compra.getCostoTotal());

        return response;
    }

    private CuentaPagarRecepcionDisponibleResponse toRecepcionDisponibleResponse(Recepcion recepcion) {
        CuentaPagarRecepcionDisponibleResponse response = new CuentaPagarRecepcionDisponibleResponse();
        response.setIdRecepciones(recepcion.getIdRecepciones());
        response.setFechaRecepcion(recepcion.getFechaRecepcion());
        response.setRecibido(recepcion.getRecibido());
        response.setEstadoRecepcion(recepcion.getEstado());
        return response;
    }

    private CuentaPagarResponse toResponse(CuentaPagar cuentaPagar) {
        Compra compra = compraRepository.findById(cuentaPagar.getIdCompras()).orElse(null);
        Recepcion recepcion = recepcionRepository.findById(cuentaPagar.getIdRecepciones()).orElse(null);
        return toResponse(cuentaPagar, compra, recepcion);
    }

    private CuentaPagarResponse toResponse(CuentaPagar cuentaPagar, Compra compra, Recepcion recepcion) {
        CuentaPagarResponse response = new CuentaPagarResponse();
        response.setIdCuentaPagar(cuentaPagar.getIdCuentaPagar());
        response.setIdCompras(cuentaPagar.getIdCompras());
        response.setIdRecepciones(cuentaPagar.getIdRecepciones());
        response.setNumeroFactura(cuentaPagar.getNumeroFactura());
        response.setMoneda(cuentaPagar.getMoneda());
        response.setCodigoDetRet(cuentaPagar.getCodigoDetRet());
        response.setEstado(cuentaPagar.getEstado());
        response.setFlgActivo(cuentaPagar.getFlgActivo());
        response.setFechaCreacion(cuentaPagar.getFechaCreacion());
        response.setFechaActualizacion(cuentaPagar.getFechaActualizacion());

        if (compra != null) {
            response.setProveedor(compra.getProveedor().getRazonSocial());
            response.setRuc(compra.getProveedor().getRuc());

            if (compra.getArticulo() != null) {
                response.setArticulo(compra.getArticulo().getDescripcion());
            }
        }

        if (recepcion != null) {
            response.setEstadoRecepcion(recepcion.getEstado());
        }

        return response;
    }
}