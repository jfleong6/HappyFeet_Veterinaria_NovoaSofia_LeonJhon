use happy_feet;
INSERT INTO especies (nombre) VALUES
('Perro'),
('Gato'),
('Ave'),
('Conejo'),
('Reptil'),
('Pez');
select * from especies;

INSERT INTO razas (id_especie, nombre) VALUES
(1, 'Labrador Retriever'),
(1, 'Pastor Alemán'),
(1, 'Bulldog Frances'),
(1, 'Golden Retriever'),
(1, 'Beagle'),
(1, 'Poodle'),
(1, 'Chihuahua'),
(1, 'Dálmata'),
(1, 'Rottweiler'),
(1, 'Shih Tzu'),
(2, 'Persa'),
(2, 'Siames'),
(2, 'Maine Coon'),
(2, 'Bengala'),
(2, 'British Shorthair'),
(2, 'Esfinge'),
(2, 'Abisinio'),
(2, 'Bosque de Noruega'),
(2, 'Ragdoll'),
(2, 'Bombay'),
(3, 'Canario'),
(3, 'Periquito'),
(3, 'Agapornis'),
(3, 'Cacatúa'),
(3, 'Guacamayo'),
(3, 'Loro Amazónico'),
(3, 'Ninfa'),
(3, 'Diamante Mandarin'),
(3, 'Cotorra Argentina'),
(3, 'Jilguero'),
(4, 'Mini Rex'),
(4, 'Cabeza de León'),
(4, 'Holland Lop'),
(4, 'Angora'),
(4, 'Belier Frances'),
(4, 'Mini Lop'),
(4, 'Rex'),
(4, 'Tan'),
(4, 'Dutch'),
(4, 'Gigante de Flandes'),
(5, 'Iguana'),
(5, 'Gecko Leopardo'),
(5, 'Dragón Barbudo'),
(5, 'Camaleón'),
(5, 'Pitón'),
(5, 'Tortuga de Orejas Rojas'),
(5, 'Boa Constrictor'),
(5, 'Varano'),
(5, 'Anolis Verde'),
(5, 'Culebra del Maiz'),
(6, 'Goldfish'),
(6, 'Betta'),
(6, 'Guppy'),
(6, 'Pez Ángel'),
(6, 'Molly'),
(6, 'Ciclido'),
(6, 'Neon Tetra'),
(6, 'Pez Disco'),
(6, 'Koi'),
(6, 'Pez Payaso');

select * from razas;

CREATE VIEW vista_especies_razas AS
SELECT 
    e.id_especie,
    e.nombre AS especie,
    r.id_raza,
    r.nombre AS raza
FROM especies e
LEFT JOIN razas r ON e.id_especie = r.id_especie
ORDER BY e.nombre, r.nombre;


CREATE OR REPLACE VIEW vista_pacientes_completa AS
SELECT 
    p.id_paciente,
    p.nombre,
    e.id_especie,
    e.nombre AS especie,
    r.id_raza,
    r.nombre AS raza,
    p.fecha_nacimiento,
    TIMESTAMPDIFF(YEAR, p.fecha_nacimiento, CURDATE()) AS edad,
    p.sexo,
    p.peso,
    d.id_dueno,
    d.nombre_completo AS propietario
FROM pacientes p
JOIN razas r ON p.id_raza = r.id_raza
JOIN especies e ON r.id_especie = e.id_especie
JOIN duenos d ON p.id_dueno = d.id_dueno;

select * from vista_pacientes_completa;

INSERT INTO duenos (nombre_completo, documento_identidad, direccion, telefono, email, contacto_emergencia) VALUES
                                                                                                               ('Juan Perez', '100001', 'Calle 1 #10-20', '3001111111', 'juan.perez@email.com', 'Ana Perez 3002222222'),
                                                                                                               ('Maria Gomez', '100002', 'Calle 2 #20-30', '3002222222', 'maria.gomez@email.com', 'Carlos Gomez 3003333333'),
                                                                                                               ('Carlos Rodriguez', '100003', 'Calle 3 #30-40', '3003333333', 'carlos.rodriguez@email.com', 'Lucia Rodriguez 3004444444'),
                                                                                                               ('Laura Martinez', '100004', 'Calle 4 #40-50', '3004444444', 'laura.martinez@email.com', 'Pedro Martinez 3005555555'),
                                                                                                               ('Andres Torres', '100005', 'Calle 5 #50-60', '3005555555', 'andres.torres@email.com', 'Marta Torres 3006666666'),
                                                                                                               ('Sofia Ramirez', '100006', 'Calle 6 #60-70', '3006666666', 'sofia.ramirez@email.com', 'Jose Ramirez 3007777777'),
                                                                                                               ('David Castro', '100007', 'Calle 7 #70-80', '3007777777', 'david.castro@email.com', 'Carolina Castro 3008888888'),
                                                                                                               ('Valentina Moreno', '100008', 'Calle 8 #80-90', '3008888888', 'valentina.moreno@email.com', 'Luis Moreno 3009999999'),
                                                                                                               ('Miguel Herrera', '100009', 'Calle 9 #90-100', '3009999999', 'miguel.herrera@email.com', 'Paula Herrera 3010000000'),
                                                                                                               ('Isabella Jimenez', '100010', 'Calle 10 #100-110', '3010000000', 'isabella.jimenez@email.com', 'Raul Jimenez 3011111111'),
                                                                                                               ('Fernando Castillo', '100011', 'Calle 11 #110-120', '3011111111', 'fernando.castillo@email.com', 'Ana Castillo 3012222222'),
                                                                                                               ('Camila Vargas', '100012', 'Calle 12 #120-130', '3012222222', 'camila.vargas@email.com', 'Javier Vargas 3013333333'),
                                                                                                               ('Ricardo Soto', '100013', 'Calle 13 #130-140', '3013333333', 'ricardo.soto@email.com', 'Claudia Soto 3014444444'),
                                                                                                               ('Daniela Leon', '100014', 'Calle 14 #140-150', '3014444444', 'daniela.leon@email.com', 'Miguel Leon 3015555555'),
                                                                                                               ('Jose Ramirez', '100015', 'Calle 15 #150-160', '3015555555', 'jose.ramirez@email.com', 'Lucia Ramirez 3016666666'),
                                                                                                               ('Natalia Rojas', '100016', 'Calle 16 #160-170', '3016666666', 'natalia.rojas@email.com', 'Andres Rojas 3017777777'),
                                                                                                               ('Andres Fuentes', '100017', 'Calle 17 #170-180', '3017777777', 'andres.fuentes@email.com', 'Sofia Fuentes 3018888888'),
                                                                                                               ('Paula Medina', '100018', 'Calle 18 #180-190', '3018888888', 'paula.medina@email.com', 'Ricardo Medina 3019999999'),
                                                                                                               ('Jorge Aguilar', '100019', 'Calle 19 #190-200', '3019999999', 'jorge.aguilar@email.com', 'Monica Aguilar 3020000000'),
                                                                                                               ('Lorena Pacheco', '100020', 'Calle 20 #200-210', '3020000000', 'lorena.pacheco@email.com', 'Carlos Pacheco 3021111111');
select * from duenos;


CREATE VIEW vista_duenos_basica AS
SELECT 
    id_dueno,
    nombre_completo
FROM 
    duenos;
CREATE VIEW vista_pacientes_basica AS
SELECT 
    id_paciente,
    nombre
FROM 
    pacientes;

select * from vista_pacientes_basica;

CREATE OR REPLACE VIEW vista_veterinarios_basica AS
SELECT v.id_veterinario, v.nombre_completo
FROM veterinarios v
WHERE v.estado = 'ACTIVO';

select * from vista_veterinarios_basica;


select * from pacientes;


create OR REPLACE VIEW vista_duenos_con_mascotas AS
SELECT 
    d.id_dueno,
    d.nombre_completo,
    d.documento_identidad,
    d.direccion,
    d.telefono,
    d.email,
    d.contacto_emergencia,
    COUNT(m.id_paciente) AS cantidad_mascotas
FROM duenos d
LEFT JOIN pacientes m ON d.id_dueno = m.id_dueno
GROUP BY 
    d.id_dueno
limit 50;

select * from vista_duenos_con_mascotas;

select * from veterinarios;

-- Vista de citas con nombres de paciente y veterinario
CREATE OR REPLACE VIEW vista_citas AS
SELECT c.id_cita, c.fecha, c.hora, c.motivo, c.estado,
       c.id_paciente, p.nombre AS nombre_paciente,
       c.id_veterinario, v.nombre_completo AS nombre_veterinario
FROM citas c
JOIN pacientes p ON c.id_paciente = p.id_paciente
JOIN veterinarios v ON c.id_veterinario = v.id_veterinario;

select * from vista_citas;