-- Migracion para bases existentes usadas antes de la evidencia AA5-EV01.
-- Si se crea la base desde cero con database/init/01-agroconecta.sql, esta migracion no es necesaria.

ALTER TABLE usuarios
ADD COLUMN password_hash varchar(64) NULL
AFTER rol;

UPDATE usuarios
SET password_hash = '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'
WHERE password_hash IS NULL OR password_hash = '';

ALTER TABLE usuarios
MODIFY password_hash varchar(64) NOT NULL;
