-- CosteElectricidadPais - Fuente: WorldPopulationReview (2025)
-- Conversión: 1 USD = 0.865 EUR (marzo 2026)
-- Precio en EUR/kWh
-- Versión para PostgreSQL: usa INSERT ... ON CONFLICT

-- ============================================================
-- COSTE KWH POR PAIS (para cálculos de rentabilidad) --
-- ============================================================

INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Bermudas', 0.407)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Irlanda', 0.381)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Italia', 0.363)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Islas Caimán', 0.355)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Liechtenstein', 0.355)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Alemania', 0.346)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Bélgica', 0.346)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Reino Unido', 0.346)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Suiza', 0.311)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Dinamarca', 0.311)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('República Checa', 0.303)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Bahamas', 0.303)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Austria', 0.294)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Chipre', 0.294)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Cabo Verde', 0.285)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Barbados', 0.268)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Guatemala', 0.251)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Países Bajos', 0.251)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Estonia', 0.251)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Jamaica', 0.242)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Letonia', 0.242)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Francia', 0.242)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Lituania', 0.234)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Australia', 0.225)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Luxemburgo', 0.216)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Uruguay', 0.216)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('El Salvador', 0.216)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Grecia', 0.216)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('España', 0.216)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Honduras', 0.208)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Portugal', 0.199)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Singapur', 0.199)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Suecia', 0.199)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Polonia', 0.199)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Japón', 0.199)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Sierra Leona', 0.199)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Eslovenia', 0.199)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Kenia', 0.190)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Malí', 0.190)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Belice', 0.190)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Chile', 0.182)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Aruba', 0.182)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Eslovaquia', 0.182)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Burkina Faso', 0.182)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Nueva Zelanda', 0.182)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Gabón', 0.173)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Filipinas', 0.173)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Colombia', 0.173)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Ruanda', 0.173)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Togo', 0.164)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Sudáfrica', 0.164)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Perú', 0.164)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Rumanía', 0.164)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Hong Kong', 0.164)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Estados Unidos', 0.156)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Senegal', 0.156)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Finlandia', 0.156)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Israel', 0.156)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Nicaragua', 0.156)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Panamá', 0.147)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Islandia', 0.147)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Uganda', 0.147)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Croacia', 0.147)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Moldavia', 0.147)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Costa Rica', 0.147)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Brasil', 0.138)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Noruega', 0.130)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Camboya', 0.130)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Bulgaria', 0.130)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Malta', 0.130)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Namibia', 0.121)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Mauricio', 0.112)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Ghana', 0.112)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Madagascar', 0.112)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Costa de Marfil', 0.112)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Tailandia', 0.112)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Mozambique', 0.112)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Serbia', 0.112)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Corea del Sur', 0.112)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Macedonia del Norte', 0.112)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Canadá', 0.104)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Esuatini', 0.104)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Sri Lanka', 0.104)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Marruecos', 0.104)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Albania', 0.104)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('República Dominicana', 0.104)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Armenia', 0.095)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Hungría', 0.095)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('México', 0.095)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Lesoto', 0.087)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Maldivas', 0.087)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Bosnia y Herzegovina', 0.087)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Taiwán', 0.087)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Ecuador', 0.087)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Botsuana', 0.078)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Indonesia', 0.078)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Tanzania', 0.078)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Jordania', 0.078)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Malaui', 0.078)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Camerún', 0.069)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Bielorrusia', 0.069)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Ucrania', 0.069)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Emiratos Árabes Unidos', 0.069)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Vietnam', 0.069)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('India', 0.069)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Argentina', 0.069)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('China', 0.069)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Venezuela', 0.061)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Turquía', 0.061)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Túnez', 0.061)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Georgia', 0.061)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Pakistán', 0.061)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Rusia', 0.061)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('República Democrática del Congo', 0.052)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Bangladés', 0.052)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Trinidad y Tobago', 0.052)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Kazajistán', 0.052)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Paraguay', 0.043)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Arabia Saudí', 0.043)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Afganistán', 0.043)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Malasia', 0.043)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Baréin', 0.043)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Surinam', 0.043)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Azerbaiyán', 0.043)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Nepal', 0.035)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Argelia', 0.035)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Kuwait', 0.035)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Nigeria', 0.035)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Uzbekistán', 0.035)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Catar', 0.026)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Laos', 0.026)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Omán', 0.026)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Birmania', 0.026)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Egipto', 0.017)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Zambia', 0.017)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Cuba', 0.017)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Irak', 0.009)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Angola', 0.009)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Bután', 0.009)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Siria', 0.009)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Kirguistán', 0.009)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Sudán', 0.009)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Etiopía', 0.009)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;
INSERT INTO electricidad_por_pais (pais, precio_kwh) VALUES ('Irán', 0.001)
ON CONFLICT (pais) DO UPDATE SET precio_kwh = EXCLUDED.precio_kwh;

-- ============================================================
-- SOFTWARE DE MINERÍA (tabla principal)
-- ============================================================

INSERT INTO software (nombre, hashrate_slug) VALUES
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
('DankMiner',      'dankminer')
ON CONFLICT (nombre) DO UPDATE SET hashrate_slug = EXCLUDED.hashrate_slug;

-- ============================================================
-- SOFTWARE DE MINERÍA - Hardware Usable
-- ============================================================

-- solo GPU
INSERT INTO software_hardware_usable (software_id, hardware_usable)
SELECT s.id, 'GPU'
FROM software s
WHERE s.nombre IN (
        'T-Rex Miner', 'lolMiner', 'TeamRedMiner', 'NBMiner', 'GMiner',
        'PhoenixMiner', 'BzMiner', 'Rigel Miner', 'WildRig Multi', 'TeamBlackMiner'
)
AND NOT EXISTS (
    SELECT 1 FROM software_hardware_usable shu WHERE shu.software_id = s.id AND shu.hardware_usable = 'GPU'
);

-- solo CPU
INSERT INTO software_hardware_usable (software_id, hardware_usable)
SELECT s.id, 'CPU'
FROM software s
WHERE s.nombre IN (
        'cpuminer-opt'
)
AND NOT EXISTS (
    SELECT 1 FROM software_hardware_usable shu WHERE shu.software_id = s.id AND shu.hardware_usable = 'CPU'
);

-- CPU + GPU (insertamos ambos tipos)
INSERT INTO software_hardware_usable (software_id, hardware_usable)
SELECT s.id, 'CPU' FROM software s WHERE s.nombre IN (
        'XMRig', 'SRBMiner-Multi', 'TNNMiner', 'DankMiner'
)
AND NOT EXISTS (
    SELECT 1 FROM software_hardware_usable shu WHERE shu.software_id = s.id AND shu.hardware_usable = 'CPU'
);
INSERT INTO software_hardware_usable (software_id, hardware_usable)
SELECT s.id, 'GPU' FROM software s WHERE s.nombre IN (
        'XMRig', 'SRBMiner-Multi', 'TNNMiner', 'DankMiner'
)
AND NOT EXISTS (
    SELECT 1 FROM software_hardware_usable shu WHERE shu.software_id = s.id AND shu.hardware_usable = 'GPU'
);

-- GPU solo (NVIDIA + AMD)
INSERT INTO software_hardware_usable (software_id, hardware_usable)
SELECT s.id, 'GPU' FROM software s WHERE s.nombre IN (
        'OneZeroMiner', 'suprminer'
)
AND NOT EXISTS (
    SELECT 1 FROM software_hardware_usable shu WHERE shu.software_id = s.id AND shu.hardware_usable = 'GPU'
);

-- GPU solo (NVIDIA)
INSERT INTO software_hardware_usable (software_id, hardware_usable)
SELECT s.id, 'GPU' FROM software s WHERE s.nombre = 'TTminer'
AND NOT EXISTS (
    SELECT 1 FROM software_hardware_usable shu WHERE shu.software_id = s.id AND shu.hardware_usable = 'GPU'
);

-- ============================================================
-- SOFTWARE DE MINERÍA - Sistemas Operativos
-- ============================================================

INSERT INTO software_sistemas (software_id, sistemas)
SELECT s.id, 'WINDOWS' FROM software s WHERE s.nombre IN (
        'T-Rex Miner', 'lolMiner', 'TeamRedMiner', 'NBMiner', 'GMiner',
        'XMRig', 'PhoenixMiner', 'SRBMiner-Multi', 'BzMiner', 'Rigel Miner',
        'cpuminer-opt', 'WildRig Multi', 'TeamBlackMiner',
        'OneZeroMiner', 'TNNMiner', 'suprminer', 'TTminer', 'DankMiner'
)
AND NOT EXISTS (
    SELECT 1 FROM software_sistemas ss WHERE ss.software_id = s.id AND ss.sistemas = 'WINDOWS'
);
INSERT INTO software_sistemas (software_id, sistemas)
SELECT s.id, 'LINUX' FROM software s WHERE s.nombre IN (
        'T-Rex Miner', 'lolMiner', 'TeamRedMiner', 'NBMiner', 'GMiner',
        'XMRig', 'PhoenixMiner', 'SRBMiner-Multi', 'BzMiner', 'Rigel Miner',
        'cpuminer-opt', 'WildRig Multi', 'TeamBlackMiner',
        'OneZeroMiner', 'TNNMiner', 'suprminer', 'TTminer', 'DankMiner'
)
AND NOT EXISTS (
    SELECT 1 FROM software_sistemas ss WHERE ss.software_id = s.id AND ss.sistemas = 'LINUX'
);

-- ============================================================
-- POOLS - Datos principales
-- NOTA: los slugs se usan solo para los pools que existen en hashrate.no
-- ============================================================

INSERT INTO pool (nombre, hashrate_slug) VALUES
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
('WoolyPooly',       'woolypooly')
ON CONFLICT (nombre) DO UPDATE SET hashrate_slug = EXCLUDED.hashrate_slug;

-- ============================================================
-- POOLS - Esquemas de pago
-- ============================================================

-- FPPS + PPS_PLUS + PPS + PPLNS (Binance)
INSERT INTO pool_esquema_de_pago (pool_id, esquema_de_pago)
SELECT p.id, v.esquema FROM pool p, (VALUES ('FPPS'), ('PPS_PLUS'), ('PPS'), ('PPLNS')) AS v(esquema)
WHERE p.nombre = 'Binance Pool'
AND NOT EXISTS (
    SELECT 1 FROM pool_esquema_de_pago ped WHERE ped.pool_id = p.id AND ped.esquema_de_pago = v.esquema
);

-- PPLNS + PPS_PLUS + FPPS (AntPool)
INSERT INTO pool_esquema_de_pago (pool_id, esquema_de_pago)
SELECT p.id, v.esquema FROM pool p, (VALUES ('PPLNS'), ('PPS_PLUS'), ('FPPS')) AS v(esquema)
WHERE p.nombre = 'AntPool'
AND NOT EXISTS (
    SELECT 1 FROM pool_esquema_de_pago ped WHERE ped.pool_id = p.id AND ped.esquema_de_pago = v.esquema
);

-- PPS_PLUS + FPPS + PPLNS (F2Pool)
INSERT INTO pool_esquema_de_pago (pool_id, esquema_de_pago)
SELECT p.id, v.esquema FROM pool p, (VALUES ('PPS_PLUS'), ('FPPS'), ('PPLNS')) AS v(esquema)
WHERE p.nombre = 'F2Pool'
AND NOT EXISTS (
    SELECT 1 FROM pool_esquema_de_pago ped WHERE ped.pool_id = p.id AND ped.esquema_de_pago = v.esquema
);

-- FPPS + PPLNS (Luxor, Poolin)
INSERT INTO pool_esquema_de_pago (pool_id, esquema_de_pago)
SELECT p.id, v.esquema FROM pool p, (VALUES ('FPPS'), ('PPLNS')) AS v(esquema)
WHERE p.nombre IN ('Luxor', 'Poolin')
AND NOT EXISTS (
    SELECT 1 FROM pool_esquema_de_pago ped WHERE ped.pool_id = p.id AND ped.esquema_de_pago = v.esquema
);

-- PPLNS + SOLO (2Miners, P2Pool, 1Miner)
INSERT INTO pool_esquema_de_pago (pool_id, esquema_de_pago)
SELECT p.id, v.esquema FROM pool p, (VALUES ('PPLNS'), ('SOLO')) AS v(esquema)
WHERE p.nombre IN ('2Miners', 'P2Pool', '1Miner')
AND NOT EXISTS (
    SELECT 1 FROM pool_esquema_de_pago ped WHERE ped.pool_id = p.id AND ped.esquema_de_pago = v.esquema
);

-- solo PPLNS (Nanopool, SupportXMR, MoneroOcean)
INSERT INTO pool_esquema_de_pago (pool_id, esquema_de_pago)
SELECT p.id, 'PPLNS' FROM pool p WHERE p.nombre IN ('Nanopool', 'SupportXMR', 'MoneroOcean')
AND NOT EXISTS (
    SELECT 1 FROM pool_esquema_de_pago ped WHERE ped.pool_id = p.id AND ped.esquema_de_pago = 'PPLNS'
);

-- nuevas pools
INSERT INTO pool_esquema_de_pago (pool_id, esquema_de_pago)
SELECT p.id, v.esquema FROM pool p, (VALUES ('PPLNS'), ('PPLNSBF')) AS v(esquema)
WHERE p.nombre = 'CedricCrispin'
AND NOT EXISTS (
    SELECT 1 FROM pool_esquema_de_pago ped WHERE ped.pool_id = p.id AND ped.esquema_de_pago = v.esquema
);

INSERT INTO pool_esquema_de_pago (pool_id, esquema_de_pago)
SELECT p.id, v.esquema FROM pool p, (VALUES ('PPLNS'), ('PPS')) AS v(esquema)
WHERE p.nombre = 'CrazyPool'
AND NOT EXISTS (
    SELECT 1 FROM pool_esquema_de_pago ped WHERE ped.pool_id = p.id AND ped.esquema_de_pago = v.esquema
);

INSERT INTO pool_esquema_de_pago (pool_id, esquema_de_pago)
SELECT p.id, v.esquema FROM pool p, (VALUES ('SOLO'), ('PPLNS'), ('PROP')) AS v(esquema)
WHERE p.nombre = 'GrandPool'
AND NOT EXISTS (
    SELECT 1 FROM pool_esquema_de_pago ped WHERE ped.pool_id = p.id AND ped.esquema_de_pago = v.esquema
);

INSERT INTO pool_esquema_de_pago (pool_id, esquema_de_pago)
SELECT p.id, 'PROP' FROM pool p WHERE p.nombre = 'HeroMiners'
AND NOT EXISTS (
    SELECT 1 FROM pool_esquema_de_pago ped WHERE ped.pool_id = p.id AND ped.esquema_de_pago = 'PROP'
);

INSERT INTO pool_esquema_de_pago (pool_id, esquema_de_pago)
SELECT p.id, v.esquema FROM pool p, (VALUES ('PPLNS'), ('SOLO'), ('PPS')) AS v(esquema)
WHERE p.nombre = 'K1Pool'
AND NOT EXISTS (
    SELECT 1 FROM pool_esquema_de_pago ped WHERE ped.pool_id = p.id AND ped.esquema_de_pago = v.esquema
);

INSERT INTO pool_esquema_de_pago (pool_id, esquema_de_pago)
SELECT p.id, v.esquema FROM pool p, (VALUES ('PPLNS'), ('PROP')) AS v(esquema)
WHERE p.nombre = 'Suprnova'
AND NOT EXISTS (
    SELECT 1 FROM pool_esquema_de_pago ped WHERE ped.pool_id = p.id AND ped.esquema_de_pago = v.esquema
);

INSERT INTO pool_esquema_de_pago (pool_id, esquema_de_pago)
SELECT p.id, 'PPLNS' FROM pool p WHERE p.nombre = 'WoolyPooly'
AND NOT EXISTS (
    SELECT 1 FROM pool_esquema_de_pago ped WHERE ped.pool_id = p.id AND ped.esquema_de_pago = 'PPLNS'
);

INSERT INTO pool_esquema_de_pago (pool_id, esquema_de_pago)
SELECT p.id, 'PPS' FROM pool p WHERE p.nombre = 'CruxPool'
AND NOT EXISTS (
    SELECT 1 FROM pool_esquema_de_pago ped WHERE ped.pool_id = p.id AND ped.esquema_de_pago = 'PPS'
);

INSERT INTO pool_esquema_de_pago (pool_id, esquema_de_pago)
SELECT p.id, v.esquema FROM pool p, (VALUES ('PROP'), ('PPS_PLUS'), ('PPS')) AS v(esquema)
WHERE p.nombre = 'Kryptex'
AND NOT EXISTS (
    SELECT 1 FROM pool_esquema_de_pago ped WHERE ped.pool_id = p.id AND ped.esquema_de_pago = v.esquema
);

-- ============================================================
-- POOLS - Regiones
-- ============================================================

-- EU + US + ASIA
INSERT INTO pool_regiones (pool_id, region)
SELECT p.id, v.region FROM pool p, (VALUES ('EU'), ('US'), ('ASIA')) AS v(region)
WHERE p.nombre IN ('AntPool', 'F2Pool', 'ViaBTC', '2Miners', 'Nanopool', 'P2Pool', 'Binance Pool', 'Poolin', '1Miner', 'CrazyPool', 'HeroMiners', 'K1Pool', 'Suprnova', 'WoolyPooly', 'Kryptex')
AND NOT EXISTS (
    SELECT 1 FROM pool_regiones pr WHERE pr.pool_id = p.id AND pr.region = v.region
);

-- solo ASIA
INSERT INTO pool_regiones (pool_id, region)
SELECT p.id, 'ASIA' FROM pool p WHERE p.nombre = 'CedricCrispin'
AND NOT EXISTS (
    SELECT 1 FROM pool_regiones pr WHERE pr.pool_id = p.id AND pr.region = 'ASIA'
);

-- EU + US
INSERT INTO pool_regiones (pool_id, region)
SELECT p.id, v.region FROM pool p, (VALUES ('EU'), ('US')) AS v(region)
WHERE p.nombre IN ('SupportXMR', 'MoneroOcean', 'GrandPool', 'CruxPool')
AND NOT EXISTS (
    SELECT 1 FROM pool_regiones pr WHERE pr.pool_id = p.id AND pr.region = v.region
);

-- solo US
INSERT INTO pool_regiones (pool_id, region)
SELECT p.id, 'US' FROM pool p WHERE p.nombre = 'Luxor'
AND NOT EXISTS (
    SELECT 1 FROM pool_regiones pr WHERE pr.pool_id = p.id AND pr.region = 'US'
);
