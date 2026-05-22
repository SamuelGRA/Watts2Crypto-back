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
-- SOFTWARE DE MINERÍA (tabla principal)
-- ============================================================

MERGE INTO software (nombre, hashrate_slug) KEY(nombre) VALUES
('T-Rex Miner',    'trex'),
('lolMiner',       'lolminer'),
('TeamRedMiner',   'teamredminer'),
('NBMiner',        'nbminer'),
('GMiner',         'gminer'),
('XMRig',          'xmrig'),
('PhoenixMiner',   'phoenixminer'),
('SRBMiner-Multi', 'srbminer'),
('BzMiner',        'bzminer'),
('Rigel Miner',    'rigel'),
('cpuminer-opt',   'cpuminer-opt-supr'),
('WildRig Multi',  'wildrigmulti'),
('TeamBlackMiner', 'teamblackminer'),
('OneZeroMiner',   'onezerominer'),
('TNNMiner',       'tnnminer'),
('suprminer',      'suprminer'),
('TTminer',        'ttminer'),
('DankMiner',      'dankminer');

-- ============================================================
-- SOFTWARE DE MINERÍA - Hardware Usable
-- ============================================================

-- solo GPU
MERGE INTO software_hardware_usable (software_id, hardware_usable)
KEY(software_id, hardware_usable)
SELECT id, 'GPU' FROM software WHERE nombre IN (
    'T-Rex Miner', 'lolMiner', 'TeamRedMiner', 'NBMiner', 'GMiner',
    'PhoenixMiner', 'BzMiner', 'Rigel Miner', 'WildRig Multi', 'TeamBlackMiner'
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
    'XMRig', 'SRBMiner-Multi', 'TNNMiner', 'DankMiner'
);
MERGE INTO software_hardware_usable (software_id, hardware_usable)
KEY(software_id, hardware_usable)
SELECT id, 'GPU' FROM software WHERE nombre IN (
    'XMRig', 'SRBMiner-Multi', 'TNNMiner', 'DankMiner'
);

-- GPU solo (NVIDIA + AMD)
MERGE INTO software_hardware_usable (software_id, hardware_usable)
KEY(software_id, hardware_usable)
SELECT id, 'GPU' FROM software WHERE nombre IN (
    'OneZeroMiner', 'suprminer'
);

-- GPU solo (NVIDIA)
MERGE INTO software_hardware_usable (software_id, hardware_usable)
KEY(software_id, hardware_usable)
SELECT id, 'GPU' FROM software WHERE nombre = 'TTminer';

-- ============================================================
-- SOFTWARE DE MINERÍA - Sistemas Operativos
-- ============================================================

MERGE INTO software_sistemas (software_id, sistemas)
KEY(software_id, sistemas)
SELECT id, 'WINDOWS' FROM software WHERE nombre IN (
    'T-Rex Miner', 'lolMiner', 'TeamRedMiner', 'NBMiner', 'GMiner',
    'XMRig', 'PhoenixMiner', 'SRBMiner-Multi', 'BzMiner', 'Rigel Miner',
    'cpuminer-opt', 'WildRig Multi', 'TeamBlackMiner',
    'OneZeroMiner', 'TNNMiner', 'suprminer', 'TTminer', 'DankMiner'
);
MERGE INTO software_sistemas (software_id, sistemas)
KEY(software_id, sistemas)
SELECT id, 'LINUX' FROM software WHERE nombre IN (
    'T-Rex Miner', 'lolMiner', 'TeamRedMiner', 'NBMiner', 'GMiner',
    'XMRig', 'PhoenixMiner', 'SRBMiner-Multi', 'BzMiner', 'Rigel Miner',
    'cpuminer-opt', 'WildRig Multi', 'TeamBlackMiner',
    'OneZeroMiner', 'TNNMiner', 'suprminer', 'TTminer', 'DankMiner'
);

-- ============================================================
-- POOLS - Datos principales
-- NOTA: los slugs se usan solo para los pools que existen en hashrate.no
-- ============================================================

MERGE INTO pool (nombre, hashrate_slug) KEY(nombre) VALUES
('AntPool',          'antpool'),
('F2Pool',           'f2pool'),
('Luxor',            'luxor'),
('2Miners',          '2miners'),
('Nanopool',         'nanopool'),
('SupportXMR',       'supportxmr'),
('MoneroOcean',      'moneroocean'),
('Kryptex',          'kryptex'),
('P2Pool',           'p2pool'),
('Binance Pool',     'binance'),
('Poolin',           'poolin'),
('1Miner',           '1miner'),
('CedricCrispin',    'cedriccrispin'),
('CrazyPool',        'crazypool'),
('CruxPool',         'cruxpool'),
('GrandPool',        'grandpool'),
('HeroMiners',       'herominers'),
('K1Pool',           'k1pool'),
('Suprnova',         'suprnova'),
('WoolyPooly',       'woolypooly');

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

-- FPPS + PPLNS
MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago)
SELECT id, e FROM pool JOIN (VALUES ('FPPS'), ('PPLNS')) AS t(e) ON nombre IN ('Luxor', 'Poolin');

-- PPLNS + SOLO
MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago)
SELECT id, e FROM pool JOIN (VALUES ('PPLNS'), ('SOLO')) AS t(e) ON nombre IN ('2Miners', 'P2Pool', '1Miner');

-- solo PPLNS
MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago)
SELECT id, 'PPLNS' FROM pool WHERE nombre IN ('Nanopool', 'SupportXMR', 'MoneroOcean');

-- nuevas pools

MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago)
SELECT id, e FROM pool JOIN (VALUES ('PPLNS'), ('PPLNSBF')) AS t(e) ON nombre = 'CedricCrispin';

MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago)
SELECT id, e FROM pool JOIN (VALUES ('PPLNS'), ('PPS')) AS t(e) ON nombre = 'CrazyPool';

MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago)
SELECT id, e FROM pool JOIN (VALUES ('SOLO'), ('PPLNS'), ('PROP')) AS t(e) ON nombre = 'GrandPool';

MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago)
SELECT id, 'PROP' FROM pool WHERE nombre = 'HeroMiners';

MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago)
SELECT id, e FROM pool JOIN (VALUES ('PPLNS'), ('SOLO'), ('PPS')) AS t(e) ON nombre = 'K1Pool';

MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago)
SELECT id, e FROM pool JOIN (VALUES ('PPLNS'), ('PROP')) AS t(e) ON nombre = 'Suprnova';

MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago)
SELECT id, 'PPLNS' FROM pool WHERE nombre = 'WoolyPooly';

MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago)
SELECT id, 'PPS' FROM pool WHERE nombre = 'CruxPool';

MERGE INTO pool_esquema_de_pago (pool_id, esquema_de_pago) KEY(pool_id, esquema_de_pago)
SELECT id, e FROM pool JOIN (VALUES ('PROP'), ('PPS_PLUS'), ('PPS')) AS t(e) ON nombre = 'Kryptex';

-- ============================================================
-- POOLS - Regiones
-- ============================================================

-- EU + US + ASIA
MERGE INTO pool_regiones (pool_id, region) KEY(pool_id, region)
SELECT id, r FROM pool JOIN (VALUES ('EU'), ('US'), ('ASIA')) AS t(r)
ON nombre IN ('AntPool', 'F2Pool', 'ViaBTC', '2Miners', 'Nanopool', 'P2Pool', 'Binance Pool', 'Poolin', '1Miner', 'CrazyPool', 'HeroMiners', 'K1Pool', 'Suprnova', 'WoolyPooly', 'Kryptex');

-- solo ASIA
MERGE INTO pool_regiones (pool_id, region) KEY(pool_id, region)
SELECT id, 'ASIA' FROM pool WHERE nombre = 'CedricCrispin';

-- EU + US
MERGE INTO pool_regiones (pool_id, region) KEY(pool_id, region)
SELECT id, r FROM pool JOIN (VALUES ('EU'), ('US')) AS t(r)
ON nombre IN ('SupportXMR', 'MoneroOcean', 'GrandPool', 'CruxPool');

-- solo US
MERGE INTO pool_regiones (pool_id, region) KEY(pool_id, region)
SELECT id, 'US' FROM pool WHERE nombre = 'Luxor';


