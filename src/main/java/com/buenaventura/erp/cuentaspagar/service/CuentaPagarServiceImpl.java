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
import java.util.ArrayList;
import java.util.List;

@Service
public class CuentaPagarServiceImpl implements CuentaPagarService {

    private final CuentaPagarRepository cuentaPagarRepository;
    private final RecepcionRepository recepcionRepository;
    private final CompraRepository compraRepository;

    public CuentaPagarServiceImpl(CuentaPagarRepository cuentaPagarRepository,
                                  RecepcionRepository recepcionRepository,
                                  CompraRepository compraRepository) {
        this.cuentaPagarRepository = cuentaPagarRepository;
        this.recepcionRepository = recepcionRepository;
        this.compraRepository = compraRepository;
    }

    @Override
    public List<CuentaPagarResponse> listar() {
        return cuentaPagarRepository.findByFlgActivoTrueAndEstadoOrderByFechaCreacionDesc("Pagado")
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<CompraValidaResponse> listarComprasValidas() {
        List<Compra> compras = compraRepository.findByFlgActivoTrueOrderByFechaComprasDesc();
        List<CompraValidaResponse> response = new ArrayList<>();

        for (Compra compra : compras) {
            if (!"Completo".equalsIgnoreCase(compra.getEstado())) {
                continue;
            }

            List<Recepcion> recepciones = recepcionRepository.obtenerPorCompra(compra.getIdCompras());

            boolean tieneRecepcionDisponible = recepciones.stream()
                    .filter(r -> "Completo".equalsIgnoreCase(r.getEstado()))
                    .anyMatch(r -> !cuentaPagarRepository.existsByIdRecepcionesAndFlgActivoTrue(r.getIdRecepciones()));

            if (!tieneRecepcionDisponible) {
                continue;
            }

            CompraValidaResponse item = new CompraValidaResponse();
            item.setIdCompras(compra.getIdCompras());
            item.setFechaCompras(compra.getFechaCompras());
            item.setPesoComprado(compra.getPeso());
            item.setEstado(compra.getEstado());
            item.setRazonSocial(compra.getProveedor().getRazonSocial());
            item.setRuc(compra.getProveedor().getRuc());
            item.setArticulo(compra.getArticulo().getDescripcion());
            item.setMedida(compra.getArticulo().getMedida());
            item.setZonaProduccion(compra.getZonaProduccion());
            item.setHectareas(compra.getHectareas());
            item.setCostoKilo(compra.getCostoKilo());
            item.setCostoTotal(compra.getCostoTotal());

            response.add(item);
        }

        return response;
    }

    @Override
    public CuentaPagarDetalleCompraResponse verDetalleCompra(Integer idCompras) {
        Compra compra = compraRepository.findById(idCompras)
                .orElseThrow(() -> new RuntimeException("La compra no existe"));

        if (!Boolean.TRUE.equals(compra.getFlgActivo())) {
            throw new RuntimeException("La compra está inactiva");
        }

        if (!"Completo".equalsIgnoreCase(compra.getEstado())) {
            throw new RuntimeException("Solo se pueden usar compras completas");
        }

        List<Recepcion> recepciones = recepcionRepository.obtenerPorCompra(compra.getIdCompras());

        List<CuentaPagarRecepcionDisponibleResponse> recepcionesDisponibles = recepciones.stream()
                .filter(r -> "Completo".equalsIgnoreCase(r.getEstado()))
                .filter(r -> !cuentaPagarRepository.existsByIdRecepcionesAndFlgActivoTrue(r.getIdRecepciones()))
                .map(r -> {
                    CuentaPagarRecepcionDisponibleResponse item = new CuentaPagarRecepcionDisponibleResponse();
                    item.setIdRecepciones(r.getIdRecepciones());
                    item.setFechaRecepcion(r.getFechaRecepcion());
                    item.setRecibido(r.getRecibido());
                    item.setEstadoRecepcion(r.getEstado());
                    return item;
                })
                .toList();

        CuentaPagarDetalleCompraResponse response = new CuentaPagarDetalleCompraResponse();
        response.setIdCompras(compra.getIdCompras());
        response.setNumeroOperacion(compra.getIdCompras());
        response.setRuc(compra.getProveedor().getRuc());
        response.setRazonSocial(compra.getProveedor().getRazonSocial());
        response.setCodArticulo(compra.getArticulo().getIdArticulo());
        response.setDescripcionArticulo(compra.getArticulo().getDescripcion());
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
    @Transactional
    public List<CuentaPagarResponse> registrar(CuentaPagarRequest request) {
        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new RuntimeException("Debes seleccionar al menos una recepción");
        }

        if ("UNICA".equalsIgnoreCase(request.getTipoFactura())
                && (request.getNumeroFactura() == null || request.getNumeroFactura().isBlank())) {
            throw new RuntimeException("Debes ingresar el número de factura");
        }

        Integer idCompraBase = request.getDetalles().get(0).getIdCompras();

        Compra compra = compraRepository.findById(idCompraBase)
                .orElseThrow(() -> new RuntimeException("La compra no existe"));

        if (!"Completo".equalsIgnoreCase(compra.getEstado())) {
            throw new RuntimeException("Solo se pueden usar compras completas");
        }

        List<CuentaPagarResponse> response = new ArrayList<>();

        for (CuentaPagarRequest.CuentaPagarRegistroDetalleRequest detalle : request.getDetalles()) {
            if (!idCompraBase.equals(detalle.getIdCompras())) {
                throw new RuntimeException("Todas las recepciones deben pertenecer a la misma compra");
            }

            Recepcion recepcion = recepcionRepository.findById(detalle.getIdRecepciones())
                    .orElseThrow(() -> new RuntimeException("La recepción no existe"));

            if (!"Completo".equalsIgnoreCase(recepcion.getEstado())) {
                throw new RuntimeException("Solo se pueden usar recepciones completas");
            }

            if (!recepcion.getCompra().getIdCompras().equals(detalle.getIdCompras())) {
                throw new RuntimeException("La recepción no pertenece a la compra seleccionada");
            }

            if (cuentaPagarRepository.existsByIdRecepcionesAndFlgActivoTrue(detalle.getIdRecepciones())) {
                throw new RuntimeException(
                        "La recepción con ID " + detalle.getIdRecepciones() + " ya fue registrada"
                );
            }

            String numeroFacturaFinal;
            if ("UNICA".equalsIgnoreCase(request.getTipoFactura())) {
                numeroFacturaFinal = request.getNumeroFactura();
            } else {
                if (detalle.getNumeroFactura() == null || detalle.getNumeroFactura().isBlank()) {
                    throw new RuntimeException("Debes ingresar número de factura para cada recepción");
                }
                numeroFacturaFinal = detalle.getNumeroFactura();
            }

            CuentaPagar cuentaPagar = new CuentaPagar();
            cuentaPagar.setIdCompras(detalle.getIdCompras());
            cuentaPagar.setIdRecepciones(detalle.getIdRecepciones());
            cuentaPagar.setNumeroFactura(numeroFacturaFinal);
            cuentaPagar.setMoneda(request.getMoneda());
            cuentaPagar.setCodigoDetRet(request.getCodigoDetRet());
            cuentaPagar.setEstado("Pagado");
            cuentaPagar.setFlgActivo(true);

            CuentaPagar guardada = cuentaPagarRepository.save(cuentaPagar);
            response.add(toResponse(guardada));
        }

        return response;
    }

    @Override
    @Transactional
    public CuentaPagarResponse actualizar(Integer id, CuentaPagarRequest request) {
        CuentaPagar existente = cuentaPagarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta por pagar no encontrada"));

        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new RuntimeException("Debes enviar un detalle");
        }

        CuentaPagarRequest.CuentaPagarRegistroDetalleRequest detalle = request.getDetalles().get(0);

        Recepcion recepcion = recepcionRepository.findById(detalle.getIdRecepciones())
                .orElseThrow(() -> new RuntimeException("La recepción no existe"));

        if (!"Completo".equalsIgnoreCase(recepcion.getEstado())) {
            throw new RuntimeException("Solo se pueden editar recepciones completas");
        }

        if (!recepcion.getCompra().getIdCompras().equals(detalle.getIdCompras())) {
            throw new RuntimeException("La recepción no pertenece a la compra indicada");
        }

        if (!existente.getIdRecepciones().equals(detalle.getIdRecepciones())
                && cuentaPagarRepository.existsByIdRecepcionesAndFlgActivoTrue(detalle.getIdRecepciones())) {
            throw new RuntimeException("La recepción seleccionada ya tiene una cuenta por pagar");
        }

        String numeroFacturaFinal;
        if ("UNICA".equalsIgnoreCase(request.getTipoFactura())) {
            if (request.getNumeroFactura() == null || request.getNumeroFactura().isBlank()) {
                throw new RuntimeException("Debes ingresar el número de factura");
            }
            numeroFacturaFinal = request.getNumeroFactura();
        } else {
            if (detalle.getNumeroFactura() == null || detalle.getNumeroFactura().isBlank()) {
                throw new RuntimeException("Debes ingresar el número de factura del detalle");
            }
            numeroFacturaFinal = detalle.getNumeroFactura();
        }

        existente.setIdCompras(detalle.getIdCompras());
        existente.setIdRecepciones(detalle.getIdRecepciones());
        existente.setNumeroFactura(numeroFacturaFinal);
        existente.setMoneda(request.getMoneda());
        existente.setCodigoDetRet(request.getCodigoDetRet());
        existente.setEstado("Pagado");
        existente.setFechaActualizacion(LocalDateTime.now());

        return toResponse(cuentaPagarRepository.save(existente));
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        CuentaPagar existente = cuentaPagarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta por pagar no encontrada"));

        existente.setFlgActivo(false);
        existente.setFechaActualizacion(LocalDateTime.now());

        cuentaPagarRepository.save(existente);
    }

    private CuentaPagarResponse toResponse(CuentaPagar cuentaPagar) {
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

        Recepcion recepcion = recepcionRepository.findById(cuentaPagar.getIdRecepciones()).orElse(null);
        if (recepcion != null) {
            response.setEstadoRecepcion(recepcion.getEstado());

            Compra compra = recepcion.getCompra();
            response.setProveedor(compra.getProveedor().getRazonSocial());
            response.setRuc(compra.getProveedor().getRuc());
            response.setArticulo(compra.getArticulo().getDescripcion());
        }

        return response;
    }
}