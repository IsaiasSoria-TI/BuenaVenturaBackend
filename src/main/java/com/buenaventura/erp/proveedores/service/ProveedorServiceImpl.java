package com.buenaventura.erp.proveedores.service;

import com.buenaventura.erp.proveedores.banco.entity.Banco;
import com.buenaventura.erp.proveedores.banco.entity.BancoProveedor;
import com.buenaventura.erp.proveedores.banco.repository.BancoProveedorRepository;
import com.buenaventura.erp.proveedores.banco.repository.BancoRepository;
import com.buenaventura.erp.proveedores.dto.ProveedorRequest;
import com.buenaventura.erp.proveedores.dto.ProveedorResponse;
import com.buenaventura.erp.proveedores.entity.Proveedor;
import com.buenaventura.erp.proveedores.repository.ProveedorRepository;
import com.buenaventura.erp.proveedores.tipoproveedor.entity.TipoProveedor;
import com.buenaventura.erp.proveedores.tipoproveedor.repository.TipoProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final BancoProveedorRepository bancoProveedorRepository;
    private final BancoRepository bancoRepository;
    private final TipoProveedorRepository tipoProveedorRepository;

    public ProveedorServiceImpl(
            ProveedorRepository proveedorRepository,
            BancoProveedorRepository bancoProveedorRepository,
            BancoRepository bancoRepository,
            TipoProveedorRepository tipoProveedorRepository
    ) {
        this.proveedorRepository = proveedorRepository;
        this.bancoProveedorRepository = bancoProveedorRepository;
        this.bancoRepository = bancoRepository;
        this.tipoProveedorRepository = tipoProveedorRepository;
    }

    @Override
    public List<ProveedorResponse> listar() {
        return proveedorRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProveedorResponse registrar(ProveedorRequest request) {
        if (proveedorRepository.existsByRuc(request.getRuc())) {
            throw new RuntimeException("Ya existe un proveedor con ese RUC");
        }

        validarCuentasBancarias(null, request);

        Proveedor proveedor = new Proveedor();
        proveedor.setRuc(request.getRuc());
        proveedor.setRazonSocial(request.getRazonSocial());
        proveedor.setTelefono(request.getTelefono());
        proveedor.setCorreo(request.getCorreo());
        proveedor.setDireccion(request.getDireccion());
        proveedor.setRepresentante(request.getRepresentante());
        proveedor.setDepartamento(request.getDepartamento());
        proveedor.setProvincia(request.getProvincia());
        proveedor.setIdTipoProveedor(request.getIdTipoProveedor());
        proveedor.setFlgActivo(true);
        proveedor.setFechaActualizacion(LocalDateTime.now());

        Proveedor guardado = proveedorRepository.save(proveedor);

        guardarBancoProveedor(guardado.getIdProveedor(), request);

        return toResponse(guardado);
    }

    @Override
    @Transactional
    public ProveedorResponse actualizar(Integer id, ProveedorRequest request) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        if (!proveedor.getRuc().equals(request.getRuc())
                && proveedorRepository.existsByRuc(request.getRuc())) {
            throw new RuntimeException("Ya existe otro proveedor con ese RUC");
        }

        validarCuentasBancarias(id, request);

        proveedor.setRuc(request.getRuc());
        proveedor.setRazonSocial(request.getRazonSocial());
        proveedor.setTelefono(request.getTelefono());
        proveedor.setCorreo(request.getCorreo());
        proveedor.setDireccion(request.getDireccion());
        proveedor.setRepresentante(request.getRepresentante());
        proveedor.setDepartamento(request.getDepartamento());
        proveedor.setProvincia(request.getProvincia());
        proveedor.setIdTipoProveedor(request.getIdTipoProveedor());
        proveedor.setFechaActualizacion(LocalDateTime.now());

        Proveedor actualizado = proveedorRepository.save(proveedor);

        guardarBancoProveedor(actualizado.getIdProveedor(), request);

        return toResponse(actualizado);
    }

    @Override
    @Transactional
    public void eliminarLogico(Integer id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        proveedor.setFlgActivo(false);
        proveedor.setFechaActualizacion(LocalDateTime.now());
        proveedorRepository.save(proveedor);

        bancoProveedorRepository.findByIdProveedorAndFlgActivo(id, true)
                .ifPresent(bp -> {
                    bp.setFlgActivo(false);
                    bancoProveedorRepository.save(bp);
                });
    }

    private void guardarBancoProveedor(Integer idProveedor, ProveedorRequest request) {
        if (request.getIdBanco() == null) {
            return;
        }

        Banco banco = bancoRepository.findById(request.getIdBanco())
                .orElseThrow(() -> new RuntimeException("Banco no encontrado con id: " + request.getIdBanco()));

        BancoProveedor bancoProveedor = bancoProveedorRepository
                .findByIdProveedorAndFlgActivo(idProveedor, true)
                .orElseGet(BancoProveedor::new);

        bancoProveedor.setIdProveedor(idProveedor);
        bancoProveedor.setIdBanco(banco.getIdBanco());
        bancoProveedor.setCuentaBancaria(normalizarCuenta(request.getCuentaBancaria()));
        bancoProveedor.setCuentaInterbancaria(normalizarCuenta(request.getCuentaInterbancaria()));
        bancoProveedor.setFlgActivo(true);

        bancoProveedorRepository.save(bancoProveedor);
    }

    private void validarCuentasBancarias(Integer idProveedor, ProveedorRequest request) {
        String cuentaBancaria = normalizarCuenta(request.getCuentaBancaria());
        String cuentaInterbancaria = normalizarCuenta(request.getCuentaInterbancaria());

        if (cuentaBancaria != null) {
            bancoProveedorRepository.findFirstByCuentaBancariaAndFlgActivoTrue(cuentaBancaria)
                    .filter(bp -> !Objects.equals(bp.getIdProveedor(), idProveedor))
                    .ifPresent(bp -> {
                        throw new RuntimeException("La cuenta bancaria ya está registrada en otro proveedor");
                    });
        }

        if (cuentaInterbancaria != null) {
            bancoProveedorRepository.findFirstByCuentaInterbancariaAndFlgActivoTrue(cuentaInterbancaria)
                    .filter(bp -> !Objects.equals(bp.getIdProveedor(), idProveedor))
                    .ifPresent(bp -> {
                        throw new RuntimeException("La cuenta interbancaria ya está registrada en otro proveedor");
                    });
        }
    }

    private String normalizarCuenta(String cuenta) {
        if (cuenta == null || cuenta.trim().isEmpty()) {
            return null;
        }

        return cuenta.trim();
    }

    private ProveedorResponse toResponse(Proveedor proveedor) {
        ProveedorResponse response = new ProveedorResponse();

        response.setIdProveedor(proveedor.getIdProveedor());
        response.setRuc(proveedor.getRuc());
        response.setRazonSocial(proveedor.getRazonSocial());
        response.setTelefono(proveedor.getTelefono());
        response.setCorreo(proveedor.getCorreo());
        response.setDireccion(proveedor.getDireccion());
        response.setRepresentante(proveedor.getRepresentante());
        response.setFlgActivo(proveedor.getFlgActivo());

        // NUEVOS CAMPOS
        response.setDepartamento(proveedor.getDepartamento());
        response.setProvincia(proveedor.getProvincia());
        response.setIdTipoProveedor(proveedor.getIdTipoProveedor());

        if (proveedor.getIdTipoProveedor() != null) {
            TipoProveedor tipoProveedor = tipoProveedorRepository
                    .findById(proveedor.getIdTipoProveedor())
                    .orElse(null);
            if (tipoProveedor != null) {
                response.setNombreTipoProveedor(tipoProveedor.getNombre());
            }
        }

        bancoProveedorRepository.findByIdProveedorAndFlgActivo(proveedor.getIdProveedor(), true)
                .ifPresent(bp -> {
                    response.setIdBanco(bp.getIdBanco());
                    response.setCuentaBancaria(bp.getCuentaBancaria());
                    response.setCuentaInterbancaria(bp.getCuentaInterbancaria());

                    Banco banco = bancoRepository.findById(bp.getIdBanco()).orElse(null);
                    if (banco != null) {
                        response.setNombreBanco(banco.getNombre());
                    }
                });

        return response;
    }
}
