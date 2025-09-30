use happy_feet;
-- =====================================================
-- 🐾 TABLAS DE CONSULTA (Especies / Razas)
-- =====================================================
CREATE TABLE especies (
    id_especie INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE razas (
    id_raza INT AUTO_INCREMENT PRIMARY KEY,
    id_especie INT NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_especie) REFERENCES especies(id_especie) ON DELETE CASCADE,
    UNIQUE (id_especie, nombre)
);

-- =====================================================
-- 👤 DUEÑOS
-- =====================================================
CREATE TABLE duenos (
    id_dueno INT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    documento_identidad VARCHAR(30) NOT NULL UNIQUE,
    direccion VARCHAR(150),
    telefono VARCHAR(30),
    email VARCHAR(100) UNIQUE,
    contacto_emergencia VARCHAR(100)
);

-- =====================================================
-- 🩺 VETERINARIOS
-- =====================================================
CREATE TABLE veterinarios (
    id_veterinario INT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    documento_identidad VARCHAR(30) NOT NULL UNIQUE,
    telefono VARCHAR(30),
    email VARCHAR(100) UNIQUE,
    especialidad VARCHAR(100),
    estado ENUM('ACTIVO','INACTIVO') DEFAULT 'ACTIVO'
);

-- =====================================================
-- 🐶 PACIENTES (Relación con Dueños y Razas)
-- =====================================================
CREATE TABLE pacientes (
    id_paciente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    id_raza INT NOT NULL,
    fecha_nacimiento DATE,
    sexo ENUM('M','F','DESCONOCIDO') DEFAULT 'DESCONOCIDO',
    alergias TEXT,
    condiciones_preexistentes TEXT,
    peso DECIMAL(5,2),
    microchip VARCHAR(50) UNIQUE,
    foto_url VARCHAR(255),
    id_dueno INT NOT NULL,
    FOREIGN KEY (id_raza) REFERENCES razas(id_raza) ON DELETE RESTRICT,
    FOREIGN KEY (id_dueno) REFERENCES duenos(id_dueno) ON DELETE CASCADE
);

-- =====================================================
-- 📅 CITAS
-- =====================================================
CREATE TABLE citas (
    id_cita INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    motivo TEXT,
    estado ENUM('PROGRAMADA','FINALIZADA','CANCELADA') DEFAULT 'PROGRAMADA',
    id_paciente INT NOT NULL,
    id_veterinario INT NOT NULL,
    FOREIGN KEY (id_paciente) REFERENCES pacientes(id_paciente) ON DELETE CASCADE,
    FOREIGN KEY (id_veterinario) REFERENCES veterinarios(id_veterinario) ON DELETE RESTRICT
);

-- =====================================================
-- 🩺 CONSULTAS MÉDICAS (asociadas a citas)
-- =====================================================
CREATE TABLE consultas (
    id_consulta INT AUTO_INCREMENT PRIMARY KEY,
    id_cita INT NOT NULL,
    diagnostico TEXT,
    recomendaciones TEXT,
    fecha_consulta DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_cita) REFERENCES citas(id_cita) ON DELETE CASCADE
);

-- =====================================================
-- 🧾 FACTURACIÓN
-- =====================================================
CREATE TABLE facturas (
    id_factura INT AUTO_INCREMENT PRIMARY KEY,
    fecha_emision DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_dueno INT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    impuestos DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_dueno) REFERENCES duenos(id_dueno) ON DELETE CASCADE
);

CREATE TABLE factura_detalles (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_factura INT NOT NULL,
    descripcion VARCHAR(200) NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_factura) REFERENCES facturas(id_factura) ON DELETE CASCADE
);

-- =====================================================
-- 📦 INVENTARIO + Tipos + Movimientos
-- =====================================================
CREATE TABLE tipos_producto (
    id_tipo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE inventario (
    id_item INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    id_tipo INT,
    fabricante VARCHAR(100),
    cantidad_actual INT NOT NULL DEFAULT 0,
    stock_minimo INT NOT NULL DEFAULT 0,
    fecha_vencimiento DATE,
    lote VARCHAR(50),
    FOREIGN KEY (id_tipo) REFERENCES tipos_producto(id_tipo) ON DELETE SET NULL
);

CREATE TABLE movimientos_inventario (
    id_movimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_item INT NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo ENUM('ENTRADA','SALIDA') NOT NULL,
    cantidad INT NOT NULL,
    motivo VARCHAR(200),
    FOREIGN KEY (id_item) REFERENCES inventario(id_item) ON DELETE CASCADE
);

-- =====================================================
-- ⏰ HORARIOS DE VETERINARIOS
-- =====================================================
CREATE TABLE horarios_veterinarios (
    id_horario INT AUTO_INCREMENT PRIMARY KEY,
    id_veterinario INT NOT NULL,
    dia_semana ENUM('LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO') NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    FOREIGN KEY (id_veterinario) REFERENCES veterinarios(id_veterinario) ON DELETE CASCADE
);

-- =====================================================
-- 👥 USUARIOS / ROLES
-- =====================================================
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE usuarios_roles (
    id_usuario INT NOT NULL,
    id_rol INT NOT NULL,
    PRIMARY KEY (id_usuario, id_rol),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol) ON DELETE CASCADE
);

-- =====================================================
-- 🐕 ADOPCIONES + SEGUIMIENTO
-- =====================================================
CREATE TABLE adopciones (
    id_adopcion INT AUTO_INCREMENT PRIMARY KEY,
    id_paciente INT NOT NULL,
    id_dueno INT,
    fecha_adopcion DATE,
    estado ENUM('PENDIENTE','APROBADA','RECHAZADA') DEFAULT 'PENDIENTE',
    FOREIGN KEY (id_paciente) REFERENCES pacientes(id_paciente) ON DELETE CASCADE,
    FOREIGN KEY (id_dueno) REFERENCES duenos(id_dueno) ON DELETE SET NULL
);

CREATE TABLE seguimiento_adopcion (
    id_seguimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_adopcion INT NOT NULL,
    fecha DATE NOT NULL,
    observaciones TEXT,
    FOREIGN KEY (id_adopcion) REFERENCES adopciones(id_adopcion) ON DELETE CASCADE
);

-- =====================================================
-- 💉 JORNADAS DE VACUNACIÓN
-- =====================================================
CREATE TABLE jornadas_vacunacion (
    id_jornada INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    lugar VARCHAR(150) NOT NULL,
    descripcion TEXT
);

CREATE TABLE jornada_detalles (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_jornada INT NOT NULL,
    id_paciente INT NOT NULL,
    id_veterinario INT NOT NULL,
    vacuna_aplicada VARCHAR(100),
    FOREIGN KEY (id_jornada) REFERENCES jornadas_vacunacion(id_jornada) ON DELETE CASCADE,
    FOREIGN KEY (id_paciente) REFERENCES pacientes(id_paciente) ON DELETE CASCADE,
    FOREIGN KEY (id_veterinario) REFERENCES veterinarios(id_veterinario) ON DELETE SET NULL
);

-- =====================================================
-- 🏅 CLUB DE PUNTOS
-- =====================================================
CREATE TABLE puntos_clientes (
    id_puntos INT AUTO_INCREMENT PRIMARY KEY,
    id_dueno INT NOT NULL UNIQUE,
    puntos_acumulados INT DEFAULT 0,
    nivel VARCHAR(50) DEFAULT 'Bronce',
    FOREIGN KEY (id_dueno) REFERENCES duenos(id_dueno) ON DELETE CASCADE
);

CREATE TABLE movimientos_puntos (
    id_movimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_dueno INT NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo ENUM('ACUMULADO','REDIMIDO') NOT NULL,
    puntos INT NOT NULL,
    descripcion VARCHAR(200),
    FOREIGN KEY (id_dueno) REFERENCES duenos(id_dueno) ON DELETE CASCADE
);