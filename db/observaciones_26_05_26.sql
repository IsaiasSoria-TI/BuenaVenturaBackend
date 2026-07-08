CREATE DATABASE IF NOT EXISTS bd_buenaventura;
USE bd_buenaventura;

-- Punto 3 y 4: facturas manuales sin compra/recepcion y moneda editable solo en manual.
ALTER TABLE tb_cuentas_pagar
    MODIFY COLUMN IdCompras INT NULL,
    MODIFY COLUMN IdRecepciones INT NULL,
    MODIFY COLUMN CodigoDetRet VARCHAR(50) NULL;

-- Punto 5: tipo de envase sale del maestro de articulos.
SET @articulo_tipo_envase_exists := (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'tb_articulo'
      AND COLUMN_NAME = 'TipoEnvase'
);

SET @sql := IF(
    @articulo_tipo_envase_exists = 0,
    'ALTER TABLE tb_articulo ADD COLUMN TipoEnvase VARCHAR(50) NOT NULL DEFAULT ''Jabas'' AFTER Medida',
    'ALTER TABLE tb_articulo MODIFY COLUMN TipoEnvase VARCHAR(50) NOT NULL DEFAULT ''Jabas'''
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Punto 5: recepciones separan tipo de envase y cantidad entera.
SET @recepcion_tipo_envase_exists := (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'tb_recepciones'
      AND COLUMN_NAME = 'TipoEnvase'
);

SET @sql := IF(
    @recepcion_tipo_envase_exists = 0,
    'ALTER TABLE tb_recepciones ADD COLUMN TipoEnvase VARCHAR(50) NULL AFTER GuiaRemision',
    'SELECT ''tb_recepciones.TipoEnvase ya existe'' AS mensaje'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @cantidad_envase_exists := (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'tb_recepciones'
      AND COLUMN_NAME = 'CantidadEnvase'
);

SET @cantidad_jabas_exists := (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'tb_recepciones'
      AND COLUMN_NAME = 'CantidadJabas'
);

SET @sql := IF(
    @cantidad_envase_exists = 0 AND @cantidad_jabas_exists > 0,
    'ALTER TABLE tb_recepciones CHANGE COLUMN CantidadJabas CantidadEnvase INT NOT NULL DEFAULT 0',
    IF(
        @cantidad_envase_exists = 0,
        'ALTER TABLE tb_recepciones ADD COLUMN CantidadEnvase INT NOT NULL DEFAULT 0 AFTER TipoEnvase',
        'ALTER TABLE tb_recepciones MODIFY COLUMN CantidadEnvase INT NOT NULL DEFAULT 0'
    )
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @cantidad_jabas_exists := (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'tb_recepciones'
      AND COLUMN_NAME = 'CantidadJabas'
);

SET @cantidad_envase_exists := (
    SELECT COUNT(*)
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'tb_recepciones'
      AND COLUMN_NAME = 'CantidadEnvase'
);

SET @sql := IF(
    @cantidad_envase_exists > 0 AND @cantidad_jabas_exists > 0,
    'UPDATE tb_recepciones SET CantidadEnvase = ROUND(CantidadJabas) WHERE CantidadEnvase = 0 AND CantidadJabas IS NOT NULL AND CantidadJabas > 0',
    'SELECT ''No hay datos de CantidadJabas para migrar'' AS mensaje'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql := IF(
    @cantidad_jabas_exists > 0,
    'ALTER TABLE tb_recepciones DROP COLUMN CantidadJabas',
    'SELECT ''tb_recepciones.CantidadJabas ya no existe'' AS mensaje'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

UPDATE tb_recepciones r
JOIN tb_recepcion_detalle rd ON rd.IdRecepciones = r.IdRecepciones
JOIN tb_compras_detalle cd ON cd.IdCompraDetalle = rd.IdCompraDetalle
JOIN tb_articulo a ON a.IdArticulo = cd.IdArticulo
SET r.TipoEnvase = a.TipoEnvase
WHERE r.TipoEnvase IS NULL OR TRIM(r.TipoEnvase) = '';
