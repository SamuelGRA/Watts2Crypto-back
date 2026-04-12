-- CosteElectricidadPais - Fuente: WorldPopulationReview (2025)
-- Conversión: 1 USD = 0.865 EUR (marzo 2026)
-- Precio en EUR/kWh
-- MERGE INTO evita errores de clave duplicada al arrancar la app

-- ============================================================
-- COSTE KWH POR PAIS (para cálculos de rentabilidad) --
-- ============================================================

MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Bermudas', 0.407);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Irlanda', 0.381);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Italia', 0.363);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Islas Caimán', 0.355);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Liechtenstein', 0.355);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Alemania', 0.346);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Bélgica', 0.346);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Reino Unido', 0.346);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Suiza', 0.311);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Dinamarca', 0.311);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('República Checa', 0.303);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Bahamas', 0.303);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Austria', 0.294);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Chipre', 0.294);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Cabo Verde', 0.285);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Barbados', 0.268);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Guatemala', 0.251);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Países Bajos', 0.251);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Estonia', 0.251);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Jamaica', 0.242);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Letonia', 0.242);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Francia', 0.242);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Lituania', 0.234);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Australia', 0.225);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Luxemburgo', 0.216);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Uruguay', 0.216);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('El Salvador', 0.216);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Grecia', 0.216);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('España', 0.216);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Honduras', 0.208);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Portugal', 0.199);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Singapur', 0.199);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Suecia', 0.199);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Polonia', 0.199);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Japón', 0.199);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Sierra Leona', 0.199);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Eslovenia', 0.199);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Kenia', 0.190);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Malí', 0.190);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Belice', 0.190);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Chile', 0.182);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Aruba', 0.182);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Eslovaquia', 0.182);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Burkina Faso', 0.182);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Nueva Zelanda', 0.182);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Gabón', 0.173);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Filipinas', 0.173);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Colombia', 0.173);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Ruanda', 0.173);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Togo', 0.164);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Sudáfrica', 0.164);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Perú', 0.164);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Rumanía', 0.164);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Hong Kong', 0.164);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Estados Unidos', 0.156);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Senegal', 0.156);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Finlandia', 0.156);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Israel', 0.156);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Nicaragua', 0.156);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Panamá', 0.147);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Islandia', 0.147);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Uganda', 0.147);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Croacia', 0.147);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Moldavia', 0.147);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Costa Rica', 0.147);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Brasil', 0.138);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Noruega', 0.130);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Camboya', 0.130);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Bulgaria', 0.130);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Malta', 0.130);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Namibia', 0.121);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Mauricio', 0.112);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Ghana', 0.112);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Madagascar', 0.112);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Costa de Marfil', 0.112);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Tailandia', 0.112);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Mozambique', 0.112);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Serbia', 0.112);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Corea del Sur', 0.112);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Macedonia del Norte', 0.112);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Canadá', 0.104);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Esuatini', 0.104);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Sri Lanka', 0.104);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Marruecos', 0.104);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Albania', 0.104);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('República Dominicana', 0.104);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Armenia', 0.095);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Hungría', 0.095);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('México', 0.095);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Lesoto', 0.087);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Maldivas', 0.087);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Bosnia y Herzegovina', 0.087);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Taiwán', 0.087);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Ecuador', 0.087);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Botsuana', 0.078);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Indonesia', 0.078);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Tanzania', 0.078);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Jordania', 0.078);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Malaui', 0.078);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Camerún', 0.069);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Bielorrusia', 0.069);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Ucrania', 0.069);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Emiratos Árabes Unidos', 0.069);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Vietnam', 0.069);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('India', 0.069);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Argentina', 0.069);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('China', 0.069);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Venezuela', 0.061);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Turquía', 0.061);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Túnez', 0.061);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Georgia', 0.061);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Pakistán', 0.061);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Rusia', 0.061);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('República Democrática del Congo', 0.052);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Bangladés', 0.052);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Trinidad y Tobago', 0.052);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Kazajistán', 0.052);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Paraguay', 0.043);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Arabia Saudí', 0.043);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Afganistán', 0.043);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Malasia', 0.043);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Baréin', 0.043);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Surinam', 0.043);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Azerbaiyán', 0.043);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Nepal', 0.035);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Argelia', 0.035);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Kuwait', 0.035);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Nigeria', 0.035);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Uzbekistán', 0.035);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Catar', 0.026);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Laos', 0.026);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Omán', 0.026);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Birmania', 0.026);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Egipto', 0.017);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Zambia', 0.017);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Cuba', 0.017);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Irak', 0.009);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Angola', 0.009);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Bután', 0.009);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Siria', 0.009);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Kirguistán', 0.009);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Sudán', 0.009);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Etiopía', 0.009);
MERGE INTO electricidad_por_pais (pais, precio_kwh) KEY(pais) VALUES ('Irán', 0.001);

-- ============================================================
-- SOFTWARE DE MINERÍA (pantalla de información y comisión en cálculos de rentabilidad) (tabla principal) --
-- ============================================================

MERGE INTO software (nombre, comision, tipo_software) KEY(nombre) VALUES
('T-Rex Miner',    1.00, 'MINERO'),
('lolMiner',       1.00, 'MINERO'),
('TeamRedMiner',   0.75, 'MINERO'),
('NBMiner',        1.00, 'MINERO'),
('GMiner',         2.00, 'MINERO'),
('XMRig',          1.00, 'MINERO'),
('PhoenixMiner',   0.65, 'MINERO'),
('SRBMiner-Multi', 0.85, 'MINERO'),
('BzMiner',        1.00, 'MINERO'),
('Rigel Miner',    1.00, 'MINERO'),
('cpuminer-opt',   0.00, 'MINERO'),
('CGMiner',        0.00, 'MINERO'),
('BFGMiner',       0.00, 'MINERO'),
('ccminer',        0.00, 'MINERO'),
('WildRig Multi',  2.00, 'MINERO'),
('Bminer',         0.65, 'MINERO'),
('miniZ',          2.00, 'MINERO'),
('Ethminer',       0.00, 'MINERO'),
('TeamBlackMiner', 0.50, 'MINERO'),
('NiceHash Miner', 2.00, 'PLATAFORMA'),
('HiveOS',         3.00, 'PLATAFORMA'),
('Awesome Miner',  0.00, 'PLATAFORMA'),
('Cudo Miner',     1.50, 'PLATAFORMA'),
('Minerstat',      0.00, 'PLATAFORMA'),
('Braiins OS+',    2.00, 'PLATAFORMA'),
('Kryptex Miner',  1.00, 'PLATAFORMA'),
('EasyMiner',      0.00, 'PLATAFORMA');

-- ============================================================
-- SOFTWARE DE MINERÍA - Hardware Usable
-- ============================================================

-- solo GPU
MERGE INTO software_hardware_usable (software_id, hardware_usable)
KEY(software_id, hardware_usable)
SELECT id, 'GPU' FROM software WHERE nombre IN (
    'T-Rex Miner', 'lolMiner', 'TeamRedMiner', 'NBMiner', 'GMiner',
    'PhoenixMiner', 'BzMiner', 'Rigel Miner', 'WildRig Multi', 'Bminer',
    'miniZ', 'Ethminer', 'TeamBlackMiner', 'ccminer'
);

-- solo CPU
MERGE INTO software_hardware_usable (software_id, hardware_usable)
KEY(software_id, hardware_usable)
SELECT id, 'CPU' FROM software WHERE nombre IN (
    'cpuminer-opt'
);

-- CPU + GPU
MERGE INTO software_hardware_usable (software_id, hardware_usable)
KEY(software_id, hardware_usable)
SELECT id, 'CPU' FROM software WHERE nombre IN (
    'XMRig', 'SRBMiner-Multi', 'NiceHash Miner', 'Cudo Miner',
    'Kryptex Miner', 'EasyMiner'
);
MERGE INTO software_hardware_usable (software_id, hardware_usable)
KEY(software_id, hardware_usable)
SELECT id, 'GPU' FROM software WHERE nombre IN (
    'XMRig', 'SRBMiner-Multi', 'NiceHash Miner', 'Cudo Miner',
    'Kryptex Miner', 'EasyMiner'
);

-- GPU + ASIC
MERGE INTO software_hardware_usable (software_id, hardware_usable)
KEY(software_id, hardware_usable)
SELECT id, 'GPU' FROM software WHERE nombre IN (
    'CGMiner', 'Awesome Miner', 'Minerstat'
);
MERGE INTO software_hardware_usable (software_id, hardware_usable)
KEY(software_id, hardware_usable)
SELECT id, 'ASIC' FROM software WHERE nombre IN (
    'CGMiner', 'Awesome Miner', 'Minerstat'
);

-- solo ASIC
MERGE INTO software_hardware_usable (software_id, hardware_usable)
KEY(software_id, hardware_usable)
SELECT id, 'ASIC' FROM software WHERE nombre IN (
    'BFGMiner', 'Braiins OS+'
);

-- CPU + GPU + ASIC
MERGE INTO software_hardware_usable (software_id, hardware_usable)
KEY(software_id, hardware_usable)
SELECT id, 'CPU' FROM software WHERE nombre = 'HiveOS';
MERGE INTO software_hardware_usable (software_id, hardware_usable)
KEY(software_id, hardware_usable)
SELECT id, 'GPU' FROM software WHERE nombre = 'HiveOS';
MERGE INTO software_hardware_usable (software_id, hardware_usable)
KEY(software_id, hardware_usable)
SELECT id, 'ASIC' FROM software WHERE nombre = 'HiveOS';

-- ============================================================
-- SOFTWARE DE MINERÍA - Sistemas Operativos
-- ============================================================

-- Windows + Linux
MERGE INTO software_sistemas (software_id, sistemas)
KEY(software_id, sistemas)
SELECT id, 'WINDOWS' FROM software WHERE nombre IN (
    'T-Rex Miner', 'lolMiner', 'TeamRedMiner', 'NBMiner', 'GMiner',
    'XMRig', 'PhoenixMiner', 'SRBMiner-Multi', 'BzMiner', 'Rigel Miner',
    'cpuminer-opt', 'WildRig Multi', 'Bminer', 'miniZ', 'TeamBlackMiner',
    'ccminer', 'Awesome Miner', 'Minerstat', 'Kryptex Miner', 'EasyMiner'
);
MERGE INTO software_sistemas (software_id, sistemas)
KEY(software_id, sistemas)
SELECT id, 'LINUX' FROM software WHERE nombre IN (
    'T-Rex Miner', 'lolMiner', 'TeamRedMiner', 'NBMiner', 'GMiner',
    'XMRig', 'PhoenixMiner', 'SRBMiner-Multi', 'BzMiner', 'Rigel Miner',
    'cpuminer-opt', 'WildRig Multi', 'Bminer', 'miniZ', 'TeamBlackMiner',
    'ccminer', 'Minerstat', 'Kryptex Miner', 'EasyMiner'
);

-- Windows + Linux + macOS
MERGE INTO software_sistemas (software_id, sistemas)
KEY(software_id, sistemas)
SELECT id, 'WINDOWS' FROM software WHERE nombre IN (
    'CGMiner', 'BFGMiner', 'Ethminer', 'Cudo Miner'
);
MERGE INTO software_sistemas (software_id, sistemas)
KEY(software_id, sistemas)
SELECT id, 'LINUX' FROM software WHERE nombre IN (
    'CGMiner', 'BFGMiner', 'Ethminer', 'Cudo Miner'
);
MERGE INTO software_sistemas (software_id, sistemas)
KEY(software_id, sistemas)
SELECT id, 'MACOS' FROM software WHERE nombre IN (
    'CGMiner', 'BFGMiner', 'Ethminer', 'Cudo Miner'
);

-- solo Windows
MERGE INTO software_sistemas (software_id, sistemas)
KEY(software_id, sistemas)
SELECT id, 'WINDOWS' FROM software WHERE nombre IN (
    'NiceHash Miner', 'Awesome Miner'
);

-- solo Linux
MERGE INTO software_sistemas (software_id, sistemas)
KEY(software_id, sistemas)
SELECT id, 'LINUX' FROM software WHERE nombre IN (
    'HiveOS', 'Braiins OS+'
);

-- ============================================================
-- SOFTWARE DE MINERÍA - Algoritmos
-- ============================================================

-- T-Rex Miner
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('Ethash'), ('KawPow'), ('Octopus'), ('MTP'), ('Progpow')) AS a(algo)
ON nombre = 'T-Rex Miner';

-- lolMiner
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('Ethash'), ('Equihash'), ('BeamHash'), ('EthashB3'), ('Autolykos2')) AS a(algo)
ON nombre = 'lolMiner';

-- TeamRedMiner
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('Ethash'), ('KawPow'), ('Autolykos2'), ('Progpow'), ('Kaspa')) AS a(algo)
ON nombre = 'TeamRedMiner';

-- NBMiner
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('Ethash'), ('Octopus'), ('KawPow'), ('Ergo'), ('BeamHash')) AS a(algo)
ON nombre = 'NBMiner';

-- GMiner
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('Equihash'), ('BeamHash'), ('Ethash'), ('Octopus'), ('KawPow')) AS a(algo)
ON nombre = 'GMiner';

-- XMRig
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('RandomX'), ('KawPow'), ('CryptoNight'), ('GhostRider'), ('Argon2')) AS a(algo)
ON nombre = 'XMRig';

-- PhoenixMiner
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('Ethash'), ('EthashB3'), ('Autolykos2')) AS a(algo)
ON nombre = 'PhoenixMiner';

-- SRBMiner-Multi
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('RandomX'), ('Ethash'), ('Autolykos2'), ('KawPow'), ('GhostRider')) AS a(algo)
ON nombre = 'SRBMiner-Multi';

-- BzMiner
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('Ethash'), ('Alephium'), ('KawPow'), ('Blake3'), ('Octopus')) AS a(algo)
ON nombre = 'BzMiner';

-- Rigel Miner
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('Ethash'), ('KawPow'), ('NexaPow'), ('Octopus'), ('EthashB3')) AS a(algo)
ON nombre = 'Rigel Miner';

-- cpuminer-opt
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('SHA-256'), ('Scrypt'), ('X11'), ('RandomX'), ('Yescrypt')) AS a(algo)
ON nombre = 'cpuminer-opt';

-- CGMiner
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('SHA-256'), ('Scrypt')) AS a(algo)
ON nombre = 'CGMiner';

-- BFGMiner
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('SHA-256'), ('Scrypt')) AS a(algo)
ON nombre = 'BFGMiner';

-- ccminer
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('X11'), ('Lyra2v2'), ('NeoScrypt'), ('Blake2s'), ('Skein')) AS a(algo)
ON nombre = 'ccminer';

-- WildRig Multi
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('X16r'), ('X21s'), ('BCD'), ('KawPow'), ('Megabtx')) AS a(algo)
ON nombre = 'WildRig Multi';

-- Bminer
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('Equihash'), ('Ethash'), ('Octopus'), ('Blake2s')) AS a(algo)
ON nombre = 'Bminer';

-- miniZ
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('Equihash'), ('BeamHash'), ('KawPow'), ('Ethash')) AS a(algo)
ON nombre = 'miniZ';

-- Ethminer
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('Ethash')) AS a(algo)
ON nombre = 'Ethminer';

-- TeamBlackMiner
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('Ethash'), ('Autolykos2'), ('KawPow'), ('SHA512')) AS a(algo)
ON nombre = 'TeamBlackMiner';

-- NiceHash Miner
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('SHA-256'), ('Ethash'), ('RandomX'), ('X11'), ('KawPow')) AS a(algo)
ON nombre = 'NiceHash Miner';

-- HiveOS
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('SHA-256'), ('Ethash'), ('RandomX'), ('KawPow'), ('Equihash')) AS a(algo)
ON nombre = 'HiveOS';

-- Awesome Miner
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('SHA-256'), ('Ethash'), ('RandomX'), ('KawPow'), ('Equihash')) AS a(algo)
ON nombre = 'Awesome Miner';

-- Cudo Miner
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('SHA-256'), ('Ethash'), ('RandomX'), ('KawPow'), ('CryptoNight')) AS a(algo)
ON nombre = 'Cudo Miner';

-- Minerstat
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('SHA-256'), ('Ethash'), ('RandomX'), ('Equihash'), ('KawPow')) AS a(algo)
ON nombre = 'Minerstat';

-- Braiins OS+
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('SHA-256')) AS a(algo)
ON nombre = 'Braiins OS+';

-- Kryptex Miner
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('Ethash'), ('RandomX'), ('KawPow'), ('SHA-256')) AS a(algo)
ON nombre = 'Kryptex Miner';

-- EasyMiner
MERGE INTO software_algoritmos (software_id, algoritmos)
KEY(software_id, algoritmos)
SELECT id, algo FROM software
JOIN (VALUES ('SHA-256'), ('Scrypt')) AS a(algo)
ON nombre = 'EasyMiner';

-- ============================================================
-- POOLS - Columnas simples
-- ============================================================
MERGE INTO pool (nombre, comision) KEY(nombre) VALUES
('AntPool',      2.50),
('F2Pool',       2.50),
('ViaBTC',       2.00),
('Braiins Pool', 2.50),
('Foundry USA',  2.50),
('Luxor',        0.70),
('2Miners',      1.00),
('Nanopool',     1.00),
('SpiderPool',   1.00),
('EMCD',         1.50),
('DxPool',       0.50),
('SupportXMR',   0.60),
('MoneroOcean',  0.00),
('Litecoinpool', 2.00),
('P2Pool',       0.00),
('Binance Pool', 2.50), 
('BTC.com',      2.00), 
('Poolin',       2.50), 
('unMineable',   1.00), 
('Prohashing',   1.99), 
('Mining Pool Hub', 0.90);

-- ============================================================
-- POOLS - Esquemas de pago
-- ============================================================

-- FPPS + PPS_PLUS + PPS + PPLNS
MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago) 
SELECT id, e FROM pool JOIN (VALUES ('FPPS'), ('PPS_PLUS'), ('PPS'), ('PPLNS')) AS t(e) ON nombre = 'Binance Pool';

-- PPLNS + PPS_PLUS + FPPS
MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago)
SELECT id, e FROM pool JOIN (VALUES ('PPLNS'), ('PPS_PLUS'), ('FPPS')) AS t(e) ON nombre = 'AntPool';

-- PPS_PLUS + FPPS + PPLNS
MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago)
SELECT id, e FROM pool JOIN (VALUES ('PPS_PLUS'), ('FPPS'), ('PPLNS')) AS t(e) ON nombre = 'F2Pool';

-- FPPS + PPLNS + SOLO
MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago) 
SELECT id, e FROM pool JOIN (VALUES ('FPPS'), ('PPLNS'), ('SOLO')) AS t(e) ON nombre = 'Prohashing';

-- PPS + PPLNS
MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago)
SELECT id, e FROM pool JOIN (VALUES ('PPS'), ('PPLNS')) AS t(e) ON nombre = 'ViaBTC';

-- solo FPPS
MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago)
SELECT id, 'FPPS' FROM pool WHERE nombre IN ('Braiins Pool', 'Foundry USA');

-- FPPS + PPLNS
MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago)
SELECT id, e FROM pool JOIN (VALUES ('FPPS'), ('PPLNS')) AS t(e) ON nombre IN ('Luxor', 'Poolin');

-- PPLNS + SOLO
MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago)
SELECT id, e FROM pool JOIN (VALUES ('PPLNS'), ('SOLO')) AS t(e) ON nombre IN ('2Miners', 'P2Pool');

-- solo PPLNS
MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago)
SELECT id, 'PPLNS' FROM pool WHERE nombre IN ('Nanopool', 'SupportXMR', 'MoneroOcean');

-- PPS_PLUS + PPLNS
MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago)
SELECT id, e FROM pool JOIN (VALUES ('PPS_PLUS'), ('PPLNS')) AS t(e) ON nombre IN ('SpiderPool', 'DxPool', 'BTC.com', 'Mining Pool Hub');

-- PPS_PLUS + SOLO
MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago)
SELECT id, e FROM pool JOIN (VALUES ('PPS_PLUS'), ('SOLO')) AS t(e) ON nombre = 'EMCD';

-- solo PPS
MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago)
SELECT id, 'PPS' FROM pool WHERE nombre IN ('Litecoinpool', 'unMineable');

-- ============================================================
-- POOLS - Regiones
-- ============================================================

-- EU + US + ASIA
MERGE INTO pool_regiones (pool_id, region) KEY(pool_id, region)
SELECT id, r FROM pool JOIN (VALUES ('EU'), ('US'), ('ASIA')) AS t(r)
ON nombre IN ('AntPool', 'F2Pool', 'ViaBTC', 'Braiins Pool', '2Miners', 'Nanopool', 'SpiderPool', 'DxPool', 'P2Pool', 'Binance Pool', 'BTC.com', 'Poolin', 'unMineable', 'Mining Pool Hub');

-- solo US
MERGE INTO pool_regiones (pool_id, region) KEY(pool_id, region)
SELECT id, 'US' FROM pool WHERE nombre IN ('Foundry USA', 'Luxor');

-- solo EU
MERGE INTO pool_regiones (pool_id, region) KEY(pool_id, region)
SELECT id, 'EU' FROM pool WHERE nombre = 'EMCD';

-- EU + US
MERGE INTO pool_regiones (pool_id, region) KEY(pool_id, region)
SELECT id, r FROM pool JOIN (VALUES ('EU'), ('US')) AS t(r)
ON nombre IN ('SupportXMR', 'MoneroOcean', 'Litecoinpool', 'Prohashing');

-- ============================================================
-- POOLS - Monedas
-- ============================================================

MERGE INTO pool_monedas (pool_id, moneda) KEY(pool_id, moneda)
SELECT id, m FROM pool JOIN (VALUES ('BTC'), ('BCH'), ('LTC')) AS t(m) ON nombre = 'AntPool';

MERGE INTO pool_monedas (pool_id, moneda) KEY(pool_id, moneda)
SELECT id, m FROM pool JOIN (VALUES ('BTC'), ('ETH'), ('LTC'), ('XMR')) AS t(m) ON nombre = 'F2Pool';

MERGE INTO pool_monedas (pool_id, moneda) KEY(pool_id, moneda)
SELECT id, m FROM pool JOIN (VALUES ('BTC'), ('BCH'), ('LTC'), ('ETH')) AS t(m) ON nombre = 'ViaBTC';

MERGE INTO pool_monedas (pool_id, moneda) KEY(pool_id, moneda)
SELECT id, 'BTC' FROM pool WHERE nombre IN ('Braiins Pool', 'Foundry USA');

MERGE INTO pool_monedas (pool_id, moneda) KEY(pool_id, moneda)
SELECT id, m FROM pool JOIN (VALUES ('BTC'), ('LTC'), ('DASH')) AS t(m) ON nombre = 'Luxor';

MERGE INTO pool_monedas (pool_id, moneda) KEY(pool_id, moneda)
SELECT id, m FROM pool JOIN (VALUES ('ETH'), ('ETC'), ('XMR'), ('RVN')) AS t(m) ON nombre = '2Miners';

MERGE INTO pool_monedas (pool_id, moneda) KEY(pool_id, moneda)
SELECT id, m FROM pool JOIN (VALUES ('XMR'), ('RVN'), ('CFX'), ('ERGO')) AS t(m) ON nombre = 'Nanopool';

MERGE INTO pool_monedas (pool_id, moneda) KEY(pool_id, moneda)
SELECT id, m FROM pool JOIN (VALUES ('BTC'), ('ETH'), ('ETC')) AS t(m) ON nombre = 'SpiderPool';

MERGE INTO pool_monedas (pool_id, moneda) KEY(pool_id, moneda)
SELECT id, m FROM pool JOIN (VALUES ('BTC'), ('LTC'), ('BCH')) AS t(m) ON nombre = 'EMCD';

MERGE INTO pool_monedas (pool_id, moneda) KEY(pool_id, moneda)
SELECT id, m FROM pool JOIN (VALUES ('BTC'), ('XMR'), ('LTC')) AS t(m) ON nombre = 'DxPool';

MERGE INTO pool_monedas (pool_id, moneda) KEY(pool_id, moneda)
SELECT id, 'XMR' FROM pool WHERE nombre IN ('SupportXMR', 'MoneroOcean', 'P2Pool');

MERGE INTO pool_monedas (pool_id, moneda) KEY(pool_id, moneda)
SELECT id, m FROM pool JOIN (VALUES ('LTC'), ('DOGE')) AS t(m) ON nombre = 'Litecoinpool';

MERGE INTO pool_monedas (pool_id, moneda) KEY(pool_id, moneda) 
SELECT id, m FROM pool JOIN (VALUES ('BTC'), ('BCH'), ('LTC'), ('ETC'), ('ZEC')) AS t(m) ON nombre = 'Binance Pool';

MERGE INTO pool_monedas (pool_id, moneda) KEY(pool_id, moneda) 
SELECT id, m FROM pool JOIN (VALUES ('BTC'), ('BCH'), ('LTC')) AS t(m) ON nombre = 'BTC.com';

MERGE INTO pool_monedas (pool_id, moneda) KEY(pool_id, moneda) 
SELECT id, m FROM pool JOIN (VALUES ('BTC'), ('BCH'), ('LTC'), ('ZEC'), ('DASH')) AS t(m) ON nombre = 'Poolin';

MERGE INTO pool_monedas (pool_id, moneda) KEY(pool_id, moneda) 
SELECT id, m FROM pool JOIN (VALUES ('BTC'), ('ETH'), ('DOGE'), ('SHIB')) AS t(m) ON nombre = 'unMineable';

MERGE INTO pool_monedas (pool_id, moneda) KEY(pool_id, moneda) 
SELECT id, m FROM pool JOIN (VALUES ('BTC'), ('LTC'), ('XMR'), ('DOGE')) AS t(m) ON nombre = 'Prohashing';

MERGE INTO pool_monedas (pool_id, moneda) KEY(pool_id, moneda) 
SELECT id, m FROM pool JOIN (VALUES ('ETH'), ('ETC'), ('XMR'), ('LTC')) AS t(m) ON nombre = 'Mining Pool Hub';

-- ============================================================
-- POOLS - Algoritmos
-- ============================================================

MERGE INTO pool_algoritmos (pool_id, algoritmo) KEY(pool_id, algoritmo)
SELECT id, a FROM pool JOIN (VALUES ('SHA-256'), ('Scrypt')) AS t(a) ON nombre IN ('AntPool', 'ViaBTC', 'EMCD', 'Litecoinpool');

MERGE INTO pool_algoritmos (pool_id, algoritmo) KEY(pool_id, algoritmo)
SELECT id, a FROM pool JOIN (VALUES ('SHA-256'), ('Ethash'), ('Scrypt'), ('RandomX')) AS t(a) ON nombre = 'F2Pool';

MERGE INTO pool_algoritmos (pool_id, algoritmo) KEY(pool_id, algoritmo)
SELECT id, a FROM pool JOIN (VALUES ('SHA-256'), ('Scrypt'), ('Ethash')) AS t(a) ON nombre = 'ViaBTC';

MERGE INTO pool_algoritmos (pool_id, algoritmo) KEY(pool_id, algoritmo)
SELECT id, 'SHA-256' FROM pool WHERE nombre IN ('Braiins Pool', 'Foundry USA');

MERGE INTO pool_algoritmos (pool_id, algoritmo) KEY(pool_id, algoritmo)
SELECT id, a FROM pool JOIN (VALUES ('SHA-256'), ('Scrypt'), ('X11')) AS t(a) ON nombre = 'Luxor';

MERGE INTO pool_algoritmos (pool_id, algoritmo) KEY(pool_id, algoritmo)
SELECT id, a FROM pool JOIN (VALUES ('Ethash'), ('RandomX'), ('KawPow')) AS t(a) ON nombre = '2Miners';

MERGE INTO pool_algoritmos (pool_id, algoritmo) KEY(pool_id, algoritmo)
SELECT id, a FROM pool JOIN (VALUES ('RandomX'), ('KawPow'), ('Octopus'), ('Autolykos2')) AS t(a) ON nombre = 'Nanopool';

MERGE INTO pool_algoritmos (pool_id, algoritmo) KEY(pool_id, algoritmo)
SELECT id, a FROM pool JOIN (VALUES ('SHA-256'), ('Ethash')) AS t(a) ON nombre = 'SpiderPool';

MERGE INTO pool_algoritmos (pool_id, algoritmo) KEY(pool_id, algoritmo)
SELECT id, a FROM pool JOIN (VALUES ('SHA-256'), ('RandomX'), ('Scrypt')) AS t(a) ON nombre = 'DxPool';

MERGE INTO pool_algoritmos (pool_id, algoritmo) KEY(pool_id, algoritmo)
SELECT id, 'RandomX' FROM pool WHERE nombre IN ('SupportXMR', 'MoneroOcean', 'P2Pool');

MERGE INTO pool_algoritmos (pool_id, algoritmo) KEY(pool_id, algoritmo) 
SELECT id, a FROM pool JOIN (VALUES ('SHA-256'), ('Scrypt'), ('Ethash'), ('Equihash')) AS t(a) ON nombre = 'Binance Pool';

MERGE INTO pool_algoritmos (pool_id, algoritmo) KEY(pool_id, algoritmo) 
SELECT id, a FROM pool JOIN (VALUES ('SHA-256'), ('Scrypt')) AS t(a) ON nombre = 'BTC.com';

MERGE INTO pool_algoritmos (pool_id, algoritmo) KEY(pool_id, algoritmo) 
SELECT id, a FROM pool JOIN (VALUES ('SHA-256'), ('Scrypt'), ('Equihash'), ('X11')) AS t(a) ON nombre = 'Poolin';

MERGE INTO pool_algoritmos (pool_id, algoritmo) KEY(pool_id, algoritmo) 
SELECT id, a FROM pool JOIN (VALUES ('Ethash'), ('KawPow'), ('RandomX')) AS t(a) ON nombre = 'unMineable';

MERGE INTO pool_algoritmos (pool_id, algoritmo) KEY(pool_id, algoritmo) 
SELECT id, a FROM pool JOIN (VALUES ('SHA-256'), ('Scrypt'), ('RandomX'), ('X11')) AS t(a) ON nombre = 'Prohashing';

MERGE INTO pool_algoritmos (pool_id, algoritmo) KEY(pool_id, algoritmo) 
SELECT id, a FROM pool JOIN (VALUES ('Ethash'), ('RandomX'), ('Scrypt')) AS t(a) ON nombre = 'Mining Pool Hub';

