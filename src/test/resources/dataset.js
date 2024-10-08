// Conexión a la base de datos
db = connect('mongodb://localhost:27017/unieventosprueba');

// Inserción de usuarios
db.usuarios.insertMany([
    {
        _id: ObjectId('66b2a9aaa8620e3c1c5437be'),
        cedula: '1223444',
        nombreCompleto: 'Carlos Perez',
        telefono: '3013334444',
        direccion: 'Calle 10 # 22-12',
        rol: 'CLIENTE',
        estadoUsuario: 'INACTIVA',
        email: 'carlosperez@email.com',
        contrasenia: 'hashed_password_1',
        codigoActivacion: {
            codigo: 'ABC123',
            fechaCreacion: ISODate('2024-08-01T10:00:00.000Z')
        },
        fechaRegistro: ISODate('2024-08-01T10:00:00.000Z'),
        _class: 'co.edu.uniquindio.unieventos.model.Usuario'
    },
    {
        _id: ObjectId('66b2b14dd9219911cd34f2c1'),
        cedula: '1223445',
        nombreCompleto: 'Laura Gómez',
        telefono: '3129999199',
        direccion: 'Calle XYZ # 14-15',
        rol: 'CLIENTE',
        estadoUsuario: 'INACTIVA',
        email: 'lauragomez@email.com',
        contrasenia: 'hashed_password_2',
        codigoActivacion: {
            codigo: 'DEF456',
            fechaCreacion: ISODate('2024-08-01T11:00:00.000Z')
        },
        fechaRegistro: ISODate('2024-08-01T11:00:00.000Z'),
        _class: 'co.edu.uniquindio.unieventos.model.Usuario'
    },
    {
        _id: ObjectId('66b2c1517f3b340441ffdeb1'),
        cedula: '1223446',
        nombreCompleto: 'José Martínez',
        telefono: '3148888288',
        direccion: 'Avenida 7 # 18-22',
        rol: 'CLIENTE',
        estadoUsuario: 'INACTIVA',
        email: 'josemartinez@email.com',
        contrasenia: 'hashed_password_3',
        codigoActivacion: {
            codigo: 'GHI789',
            fechaCreacion: ISODate('2024-08-01T12:00:00.000Z')
        },
        fechaRegistro: ISODate('2024-08-01T12:00:00.000Z'),
        _class: 'co.edu.uniquindio.unieventos.model.Usuario'
    },
    {
        _id: ObjectId('66b2c1517f3b340441ffdeb2'),
        cedula: '1223447',
        nombreCompleto: 'Ana Ruiz',
        telefono: '3155555566',
        direccion: 'Carrera 5 # 10-10',
        rol: 'CLIENTE',
        estadoUsuario: 'INACTIVA',
        email: 'anaruiz@email.com',
        contrasenia: 'hashed_password_4',
        codigoActivacion: {
            codigo: 'JKL012',
            fechaCreacion: ISODate('2024-08-01T13:00:00.000Z')
        },
        fechaRegistro: ISODate('2024-08-01T13:00:00.000Z'),
        _class: 'co.edu.uniquindio.unieventos.model.Usuario'
    },
    {
        _id: ObjectId('66b2c1517f3b340441ffdeb3'),
        cedula: '1223448',
        nombreCompleto: 'David Torres',
        telefono: '3167777788',
        direccion: 'Calle 11 # 20-21',
        rol: 'ADMINISTRADOR',
        estadoUsuario: 'ACTIVA',
        email: 'davidtorres@email.com',
        contrasenia: 'hashed_password_5',
        codigoActivacion: {
            codigo: 'MNO345',
            fechaCreacion: ISODate('2024-08-01T14:00:00.000Z')
        },
        fechaRegistro: ISODate('2024-08-01T14:00:00.000Z'),
        _class: 'co.edu.uniquindio.unieventos.model.Usuario'
    }
]);

// Inserción de cupones
db.cupones.insertMany([
    {
        _id: ObjectId('66b2c1517f3b340441ffdeb4'),
        codigo: 'REGISTROUNICO',
        nombre: 'Descuento del 15% por registrarse en la plataforma',
        porcentajeDescuento: 15,
        estadoCupon: 'ACTIVO',
        tipoCupon: 'GENERAL',
        fechaVencimiento: ISODate('2025-12-31T23:59:59.000Z'),
        _class: 'co.edu.uniquindio.unieventos.model.Cupon'
    },
    {
        _id: ObjectId('66b2c1517f3b340441ffdeb5'),
        codigo: 'PRIMERACOMPRA',
        nombre: 'Descuento del 10% para la primera compra',
        porcentajeDescuento: 10,
        estadoCupon: 'ACTIVO',
        tipoCupon: 'GENERAL',
        fechaVencimiento: ISODate('2025-01-31T23:59:59.000Z'),
        _class: 'co.edu.uniquindio.unieventos.model.Cupon'
    },
    {
        _id: ObjectId('66b2c1517f3b340441ffdeb6'),
        codigo: 'BANCOLOMBIA',
        nombre: 'Descuento del 5% para pagos con tarjetas Bancolombia',
        porcentajeDescuento: 5,
        estadoCupon: 'INACTIVO',
        tipoCupon: 'GENERAL',
        fechaVencimiento: ISODate('2025-09-30T23:59:59.000Z'),
        _class: 'co.edu.uniquindio.unieventos.model.Cupon'
    },
    {
        _id: ObjectId('66b2c1517f3b340441ffdeb7'),
        codigo: 'MICUMPLE',
        nombre: 'Descuento del 7% por cumpleaños',
        porcentajeDescuento: 7,
        estadoCupon: 'ACTIVO',
        tipoCupon: 'UNICO',
        fechaVencimiento: ISODate('2024-11-15T23:59:59.000Z'),
        _class: 'co.edu.uniquindio.unieventos.model.Cupon'
    },
    {
        _id: ObjectId('66b2c1517f3b340441ffdeb8'),
        codigo: 'ELECTRO2024',
        nombre: 'Descuento del 10% para productos electrodomesticos',
        porcentajeDescuento: 10,
        estadoCupon: 'ELIMINADO',
        tipoCupon: 'GENERAL',
        fechaVencimiento: ISODate('2024-12-30T23:59:59.000Z'),
        _class: 'co.edu.uniquindio.unieventos.model.Cupon'
    }
]);

// Inserción de eventos
db.eventos.insertMany([
    {
        _id: ObjectId('66b2c1517f3b340441ffdeb9'),
        nombreEvento: 'Festival de Verano',
        descripcionEvento: 'Festival de música al aire libre',
        fechaEvento: ISODate('2025-07-20T00:00:00.000Z'),
        tipoEvento: 'FESTIVAL',
        direccionEvento: 'Parque Central',
        ciudadEvento: 'Medellin',
        localidades: [
            { nombreLocalidad: 'VIP', precioLocalidad: 120000, capacidadMaxima: 100, entradasRestantes: 98 },
            { nombreLocalidad: 'GENERAL', precioLocalidad: 60000, capacidadMaxima: 500, entradasRestantes: 498 }
        ],
        imagenPortada: 'url_imagen_festival',
        imagenLocalidades: 'url_imagen_localidades_festival',
        estadoEvento: 'ACTIVO',
        _class: 'co.edu.uniquindio.unieventos.model.Evento'
    },
    {
        _id: ObjectId('66b2c1517f3b340441ffdeba'),
        nombreEvento: 'Concierto de Rock',
        descripcionEvento: 'Concierto de las mejores bandas de rock',
        fechaEvento: ISODate('2025-09-15T00:00:00.000Z'),
        tipoEvento: 'CONCIERTO',
        direccionEvento: 'Estadio Municipal',
        ciudadEvento: 'Bogota',
        localidades: [
            { nombreLocalidad: 'PLATEA', precioLocalidad: 80000, capacidadMaxima: 200, entradasRestantes: 200 },
            { nombreLocalidad: 'GENERAL', precioLocalidad: 50000, capacidadMaxima: 1000, entradasRestantes: 999 }
        ],
        imagenPortada: 'url_imagen_rock',
        imagenLocalidades: 'url_imagen_localidades_rock',
        estadoEvento: 'ACTIVO',
        _class: 'co.edu.uniquindio.unieventos.model.Evento'
    },
    {
        _id: ObjectId('66b2c1517f3b340441ffdebb'),
        nombreEvento: 'Festival de Jazz',
        descripcionEvento: 'Una noche con las mejores bandas de jazz.',
        fechaEvento: ISODate('2025-10-10T00:00:00.000Z'),
        tipoEvento: 'FESTIVAL',
        direccionEvento: 'Teatro Nacional',
        ciudadEvento: 'Cali',
        localidades: [
            { nombreLocalidad: 'VIP', precioLocalidad: 150000, capacidadMaxima: 50, entradasRestantes: 47 },
            { nombreLocalidad: 'GENERAL', precioLocalidad: 70000, capacidadMaxima: 300, entradasRestantes: 300 }
        ],
        imagenPortada: 'url_imagen_jazz',
        imagenLocalidades: 'url_imagen_localidades_jazz',
        estadoEvento: 'ACTIVO',
        _class: 'co.edu.uniquindio.unieventos.model.Evento'
    },
    {
        _id: ObjectId('66b2c1517f3b340441ffdebc'),
        nombreEvento: 'Concierto de Pop',
        descripcionEvento: 'Concierto de los artistas más populares del pop.',
        fechaEvento: ISODate('2025-03-25T00:00:00.000Z'),
        tipoEvento: 'CONCIERTO',
        direccionEvento: 'Coliseo Central',
        ciudadEvento: 'Barranquilla',
        localidades: [
            { nombreLocalidad: 'PLATINO', precioLocalidad: 180000, capacidadMaxima: 100, entradasRestantes: 99 },
            { nombreLocalidad: 'GENERAL', precioLocalidad: 90000, capacidadMaxima: 600, entradasRestantes: 599 }
        ],
        imagenPortada: 'url_imagen_pop',
        imagenLocalidades: 'url_imagen_localidades_pop',
        estadoEvento: 'ACTIVO',
        _class: 'co.edu.uniquindio.unieventos.model.Evento'
    },
    {
        _id: ObjectId('66b2c1517f3b340441ffdebd'),
        nombreEvento: 'Feria del Libro',
        descripcionEvento: 'Encuentro cultural con escritores y presentaciones.',
        fechaEvento: ISODate('2025-02-30T00:00:00.000Z'),
        tipoEvento: 'FERIA',
        direccionEvento: 'Centro de Convenciones',
        ciudadEvento: 'Cartagena',
        localidades: [
            { nombreLocalidad: 'EXPOSITOR', precioLocalidad: 250000, capacidadMaxima: 40, entradasRestantes: 39 },
            { nombreLocalidad: 'ASISTENTE', precioLocalidad: 30000, capacidadMaxima: 1000, entradasRestantes: 999 }
        ],
        imagenPortada: 'url_imagen_feria_libro',
        imagenLocalidades: 'url_imagen_localidades_feria_libro',
        estadoEvento: 'ACTIVO',
        _class: 'co.edu.uniquindio.unieventos.model.Evento'
    }
]);

db.carritos.insertMany([
    {
        _id: ObjectId("64db45abcf1b8a0001a0b8a1"),
        fecha: ISODate("2024-09-29T10:15:30Z"),
        itemsCarrito: [
            {
                //EVENTO 1
                cantidad: 2,
                nombreLocalidad: "VIP",
                idEvento: ObjectId("66b2c1517f3b340441ffdeb9")
            },
            {
                //EVENTO 2
                cantidad: 1,
                nombreLocalidad: "GENERAL",
                idEvento: ObjectId("66b2c1517f3b340441ffdeba")
            }
        ],
        //Usuario 1
        idUsuario: ObjectId("66b2a9aaa8620e3c1c5437be"),
        _class: 'co.edu.uniquindio.unieventos.model.Carrito'
    },
    {
        _id: ObjectId("64db45abcf1b8a0001a0b8a2"),
        fecha: ISODate("2024-10-01T15:45:00Z"),
        itemsCarrito: [
            {
                //EVENTO 3
                cantidad: 3,
                nombreLocalidad: "VIP",
                idEvento: ObjectId("66b2c1517f3b340441ffdebb")
            }
        ],
        //Usuario 2
        idUsuario: ObjectId("66b2b14dd9219911cd34f2c1"),
        _class: 'co.edu.uniquindio.unieventos.model.Carrito'
    },
    {
        _id: ObjectId("64db45abcf1b8a0001a0b8a3"),
        fecha: ISODate("2024-09-25T09:30:00Z"),
        itemsCarrito: [
            {
                //EVENTO 4
                cantidad: 1,
                nombreLocalidad: "PLATINO",
                idEvento: ObjectId("66b2c1517f3b340441ffdebc")
            },
            {
                //EVENTO 5
                cantidad: 2,
                nombreLocalidad: "ASISTENTE",
                idEvento: ObjectId("66b2c1517f3b340441ffdebd")
            }
        ],
        //Usuario 3
        idUsuario: ObjectId("66b2c1517f3b340441ffdeb1"),
        _class: 'co.edu.uniquindio.unieventos.model.Carrito'
    },
    {
        _id: ObjectId("64db45abcf1b8a0001a0b8a4"),
        fecha: ISODate("2024-09-28T12:00:00Z"),
        itemsCarrito: [
            {
                //EVENTO 1
                cantidad: 2,
                nombreLocalidad: "GENERAL",
                idEvento: ObjectId("66b2c1517f3b340441ffdeb9")
            },
            {
                //EVENTO 4
                cantidad: 1,
                nombreLocalidad: "GENERAL",
                idEvento: ObjectId("66b2c1517f3b340441ffdebc")
            }
        ],
        //Usuario 4
        idUsuario: ObjectId("66b2c1517f3b340441ffdeb2"),
        _class: 'co.edu.uniquindio.unieventos.model.Carrito'
    },
    {
        _id: ObjectId("64db45abcf1b8a0001a0b8a5"),
        fecha: ISODate("2024-09-02T08:15:00Z"),
        itemsCarrito: [
            {
                //EVENTO 5
                cantidad: 1,
                nombreLocalidad: "EXPOSITOR",
                idEvento: ObjectId("66b2c1517f3b340441ffdebd")
            }
        ],
        //Usuario 5
        idUsuario: ObjectId("66b2c1517f3b340441ffdeb3"),
        _class: 'co.edu.uniquindio.unieventos.model.Carrito'
    }
]);

db.compras.insertMany([
    {
        _id: ObjectId("66fc30f01f299be000110b36"),
        //Usuario 1
        idUsuario: ObjectId("66b2a9aaa8620e3c1c5437be"),
        itemsCompra: [
            {
                //Evento 1
                idEvento: ObjectId("66b2c1517f3b340441ffdeb9"),
                nombreLocalidad: "VIP",
                cantidad: 2,
                precioUnitario: 120000.0
            },
            {
                //Evento 2
                idEvento: ObjectId("66b2c1517f3b340441ffdeba"),
                nombreLocalidad: "GENERAL",
                cantidad: 1,
                precioUnitario: 50000.0
            }
        ],
        //TOTAL - TOTAL * DESCUENTO DEL CUPON (290000-290000*0.15)
        total: 246500.0,
        fechaCompra: ISODate("2024-09-29T10:15:30Z"),
        //CUPON 1
        codigoCupon: "REGISTROUNICO",
        estadoCompra: "PENDIENTE",
        _class: 'co.edu.uniquindio.unieventos.model.Compra'
    },
    {
        _id: ObjectId("66fc30feae029f96175a21f3"),
        //Usuario 2
        idUsuario: ObjectId("66b2b14dd9219911cd34f2c1"),
        itemsCompra: [
            {
                //Evento 3
                idEvento: ObjectId("66b2c1517f3b340441ffdebb"),
                nombreLocalidad: "VIP",
                cantidad: 3,
                precioUnitario: 150000.0
            }
        ],
        //TOTAL - TOTAL * DESCUENTO DEL CUPON (450000-450000*0.1)
        total: 405000.0,
        fechaCompra: ISODate("2024-10-01T15:45:00Z"),
        codigoCupon: "PRIMERACOMPRA",
        estadoCompra: "COMPLETADA",
        codigoPasarela: "PAGOVIALE131411",
        pago: {
            metodo: "visa",
            estado: "APROBADO",
            fechaPago: ISODate("2024-10-02T08:16:00Z")
        },
        _class: 'co.edu.uniquindio.unieventos.model.Compra'
    },
    {
        _id: ObjectId("66fc315670c1d4c30dc6a396"),
        //Usuario 3
        idUsuario: ObjectId("66b2c1517f3b340441ffdeb1"),
        itemsCompra: [
            {
                //Evento 4
                idEvento: ObjectId("66b2c1517f3b340441ffdebc"),
                nombreLocalidad: "PLATINO",
                cantidad: 1,
                precioUnitario: 180000.0
            },
            {
                //Evento 5
                idEvento: ObjectId("66b2c1517f3b340441ffdebd"),
                nombreLocalidad: "ASISTENTE",
                cantidad: 2,
                precioUnitario: 30000.0
            }
        ],
        total: 240000.0,
        fechaCompra: ISODate("2024-10-01T09:30:00Z"),
        estadoCompra: "PENDIENTE",
        _class: 'co.edu.uniquindio.unieventos.model.Compra'
    },
    {
        _id: ObjectId("66fc31634ed5f7a3186e0e19"),
        //Usuario 4
        idUsuario: ObjectId("66b2c1517f3b340441ffdeb2"),
        itemsCompra: [
            {
                //Evento 1
                idEvento: ObjectId("66b2c1517f3b340441ffdeb9"),
                nombreLocalidad: "GENERAL",
                cantidad: 2,
                precioUnitario: 60000.0
            },
            {
                //Evento 4
                idEvento: ObjectId("66b2c1517f3b340441ffdebc"),
                nombreLocalidad: "GENERAL",
                cantidad: 1,
                precioUnitario: 90000.0
            }
        ],
        total: 210000.0,
        fechaCompra: ISODate("2024-09-28T12:00:00Z"),
        estadoCompra: "COMPLETADA",
        codigoPasarela: "PAGOVIALE101112",
        pago: {
            metodo: "Tarjeta de Débito",
            estado: "APROBADO",
            fechaPago: ISODate("2024-09-28T12:01:00Z")
        },
        _class: 'co.edu.uniquindio.unieventos.model.Compra'
    },
    {
        _id: ObjectId("66fc316ca5458eca3e2f7ed8"),
        //Usuario 5
        idUsuario: ObjectId("66b2c1517f3b340441ffdeb3"),
        itemsCompra: [
            {
                //Evento 5
                idEvento: ObjectId("66b2c1517f3b340441ffdebd"),
                nombreLocalidad: "EXPOSITOR",
                cantidad: 1,
                precioUnitario: 250000.0
            }
        ],
        total: 250000.0,
        fechaCompra: ISODate("2024-10-02T08:15:00Z"),
        estadoCompra: "COMPLETADA",
        codigoPasarela: "PAGOVIALE131415",
        pago: {
            metodo: "Criptomoneda",
            estado: "APROBADO",
            fechaPago: ISODate("2024-10-02T08:16:00Z")
        },
        _class: 'co.edu.uniquindio.unieventos.model.Compra'
    }
]);