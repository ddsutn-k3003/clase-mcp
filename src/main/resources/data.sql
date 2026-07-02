INSERT INTO cuentas (id, titular, alias, saldo) VALUES
                                                    (1, 'Juan Pérez',  'juan.perez',  15000.00),
                                                    (2, 'María Gómez', 'maria.gomez',  8500.50),
                                                    (3, 'Carlos Ruiz', 'carlos.ruiz',  3200.00),
                                                    (4, 'Ana Torres',  'ana.torres',  50000.00);

INSERT INTO movimientos
(cuenta_origen_id, cuenta_destino_id, monto, tipo, fecha, descripcion)
VALUES
    (NULL,1,15000,'DEPOSITO','2026-06-01 10:00:00','Carga inicial'),
    (NULL,2,8500.50,'DEPOSITO','2026-06-01 10:05:00','Carga inicial'),
    (NULL,3,3200,'DEPOSITO','2026-06-01 10:10:00','Carga inicial'),
    (NULL,4,50000,'DEPOSITO','2026-06-01 10:15:00','Carga inicial');