-- TABLA TESTEADA CORRECTAMENTE
DROP TABLE UBICACION;
CREATE TABLE UBICACION(

    ubi_id INTEGER PRIMARY KEY AUTOINCREMENT,   -- Id de la ubicacion
    ubi_nombre TEXT                             -- nombre de la ubicacion
        
);


-- TABLA TESTEADA CORRECTAMENTE
DROP TABLE PRODUCTO;
CREATE TABLE PRODUCTO(
    
    pro_id INTEGER PRIMARY KEY AUTOINCREMENT,   -- Id del producto
    pro_nombre TEXT,                            -- Nombre del producto
    pro_precio_compra INTEGER,                  -- Precio en el que se compra el producto
    pro_precio_venta INTENGER,                  -- Precio en el que se piensa vender
    ubi_id INTEGER,                             -- Id de la ubiacion del producto
    pro_nota TEXT,                              -- Nota relacionada al producto

    FOREIGN KEY(ubi_id) REFERENCES UBICACION(ubi_id)

);


-- TABLA TESTEADA CORRECTAMENTE
DROP TABLE INVENTARIO;
CREATE TABLE INVENTARIO(

    pro_id INTEGER PRIMARY KEY,     -- Id del producto
    inv_cantidad INTEGER,           -- cantidad de producto en el inventario
    
    FOREIGN KEY (pro_id) REFERENCES PRODUCTO(pro_id)
);


DROP TABLE COMPRA;
CREATE TABLE COMPRA(
    
    com_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,    -- id de la compra
    pro_id INTEGER,                                     -- Id del producto
    com_fecha DATE DEFAULT (DATE('now', '-5 hours')),   -- Fecha de la compra
    com_precio INTEGER,                                 -- precio de compra en el momento no controlado por el usuario
    com_cantidad INTEGER,                               -- cantidad comprada
    
    FOREIGN KEY (pro_id) REFERENCES PRODUCTO(pro_id)
);

DROP TABLE VENTA;
CREATE TABLE VENTA(
    
    ven_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,    -- Id de la venta
    pro_id INTEGER,                                     -- Id del producto
    ven_fecha DATE DEFAULT (DATE('now', '-5 hours')),   -- Fecha de la venta
    ven_precio INTEGER,                                 -- Precio de venta en el momento no controlado por el usuario
    ven_cantidad INTEGER,                               -- Cantidad vendida
    
    FOREIGN KEY (pro_id) REFERENCES PRODUCTO(pro_id)
);


-- Triggers

-- ESTE TRIGGER FUNCIONA CORRECTAMETNE
DROP TRIGGER TG_AUMENTO_INVENTARIO;
CREATE TRIGGER TG_AUMENTO_INVENTARIO
AFTER INSERT ON COMPRA
FOR EACH ROW
BEGIN
    UPDATE INVENTARIO SET inv_cantidad = inv_cantidad + NEW.com_cantidad WHERE pro_id = NEW.pro_id;
END;

-- ESTE TRIGGER FUNCIONA CORRECTAMENTE
DROP TRIGGER TG_DELTE_COMPRA;
CREATE TRIGGER TG_DELTE_COMPRA
AFTER DELETE ON COMPRA
FOR EACH ROW
BEGIN
    UPDATE INVENTARIO SET inv_cantidad = inv_cantidad - OLD.com_cantidad WHERE pro_id = OLD.pro_id;
END;

-- ESTE TRIGGER FUNCIONA CORRECTAMENTE
DROP TRIGGER TG_DELETE_VENTA;
CREATE TRIGGER TG_DELETE_VENTA
AFTER DELETE ON VENTA
FOR EACH ROW
BEGIN
    UPDATE INVENTARIO SET inv_cantidad = inv_cantidad + OLD.com_cantidad WHERE pro_id = OLD.pro_id;
END;

-- ESTE TRIGGER FUNCIONA CORRECTAMENTE
DROP TRIGGER TG_DISMINUCION_INVENTARIO;
CREATE TRIGGER TG_DISMINUCION_INVENTARIO
BEFORE INSERT ON VENTA
FOR EACH ROW
BEGIN
    -- Verificar si hay suficiente cantidad en el inventario para el producto de la nueva venta
    SELECT CASE
               WHEN (SELECT inv_cantidad FROM INVENTARIO WHERE pro_id = NEW.pro_id) < NEW.ven_cantidad THEN
                   -- Si la cantidad en inventario es menor que la cantidad a vender, abortar la inserción
                   RAISE(ABORT, 'No hay suficiente inventario disponible para este producto.')
           END;

    -- Si hay suficiente inventario, proceder a restar la cantidad vendida
    UPDATE INVENTARIO
    SET inv_cantidad = inv_cantidad - NEW.ven_cantidad
    WHERE pro_id = NEW.pro_id;
END;

-- ESTE TRIGGER FUNCIONA CORRECTAMENTE
DROP TRIGGER TG_INSERT_INVENTARIO;
CREATE TRIGGER TG_INSERT_INVENTARIO
AFTER INSERT ON PRODUCTO
BEGIN

    INSERT INTO INVENTARIO VALUES (NEW.pro_id, 0);

END;

-- ESTE TRIGGER FUNCIONA CORRECTAMENTE  
DROP TRIGGER TG_DELETE_INVENTARIO;
CREATE TRIGGER TG_DELETE_INVENTARIO
AFTER DELETE ON PRODUCTO
BEGIN

    DELETE FROM INVENTARIO WHERE pro_id = OLD.pro_id;

END;


-- VISTAS
DROP VIEW VW_PRODUCTO;
CREATE VIEW VW_PRODUCTO AS SELECT 
    p.pro_id, 
    p.pro_nombre, 
    p.pro_precio_compra, 
    p.pro_precio_venta, 
    i.inv_cantidad, 
    u.ubi_nombre,
    p.pro_nota
    FROM PRODUCTO p
        LEFT JOIN INVENTARIO i ON p.pro_id = i.pro_id
        LEFT JOIN UBICACION u ON p.ubi_id = u.ubi_id;


DROP VIEW VW_COMPRA;
CREATE VIEW VW_COMPRA AS SELECT
    com_id,
    pro_id,
    pro_nombre,
    com_fecha,
    com_precio,
    com_cantidad 
    FROM PRODUCTO 
        NATURAL JOIN COMPRA;
        
DROP VIEW VW_VENTA;
CREATE VIEW VW_VENTA AS SELECT
    ven_id,
    pro_id,
    pro_nombre,
    ven_fecha,
    ven_precio,
    ven_cantidad 
    FROM PRODUCTO 
        NATURAL JOIN VENTA;
        
DROP VIEW VW_INVENTARIO;
CREATE VIEW VW_INVENTARIO AS SELECT
    pro_id,
    pro_nombre,
    inv_cantidad
    FROM INVENTARIO
        NATURAL JOIN PRODUCTO;
        


-- TIPS

--tip para consultas con fechas
SELECT * FROM VENTA
WHERE strftime('%d-%m-%Y', ven_fecha) = '21-06-2025';

-- esta seria la forma correcta de hacer la insercion corresopndiente, tendria que ir internamente dentro del codigo a implementar
-- Tambien es valida para las ventas
INSERT INTO COMPRA (pro_id,com_precio, com_cantidad) VALUES (1,(SELECT pro_precio_compra FROM PRODUCTO WHERE pro_id = 1), 1);
