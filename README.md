# Watts2Crypto-back

Watts2Crypto-back es el backend de Watts2Crypto, una aplicación para estimar la rentabilidad de minería de criptomonedas a partir de hardware, software, pools, electricidad y cotizaciones de monedas.

> Esta aplicación se ha desarrollado como parte de un TFG (Trabajo de Fin de Grado) y no genera ningún tipo de retribución económica. El proyecto se usa con fines académicos y personales, no comerciales.

## Qué incluye

El backend expone la API REST que consume el frontend y se encarga de:

- Calcular rentabilidad y consumos.
- Refrescar datos de criptomonedas, hardware, software, pools, electricidad y monedas tradicionales.
- Servir el estado de mantenimiento.
- Exportar e importar snapshots de la base de datos.
- Alimentar el despliegue local con Docker y el despliegue en Render.

## Repositorio relacionado

Este repositorio debe usarse junto con el frontend, si se pretende desplegar la app localmente:

- [Watts2Crypto-front](../Watts2Crypto-front)

Lo más cómodo es tener ambos repositorios en una misma carpeta padre:

```text
watts2crypto/
├─ watts2crypto-back/
└─ Watts2Crypto-front/
```

## Formas de uso

### 1. Uso local con Docker

Esta es la forma recomendada para probar la aplicación completa en local.

```bash
docker compose up -d --build
```

Esto levanta:

- Backend en `http://localhost:8080`
- Brontend en `http://localhost:3000`

Cuando solo quieras reiniciar el entorno después de importar una snapshot o modificar la base de datos:

```bash
docker compose restart
```

Una ventaja importante de usar la aplicación con Docker, aparte de todas las ventajas propias de esta herramienta, es que el backend no sufre de suspensiones por inactividad como ocurre en el despliegue con Render. Además, los datos se pueden mantener actualizados en un entorno local mediante la [importación de snapshots](#importar-la-snapshot-en-local), que se detalla más abajo.

### 2. Despliegue en Render

El backend está preparado para desplegarse como Web Service en Render usando el `Dockerfile` de producción.

Notas importantes:

- Render usa el perfil `prod` y conecta contra PostgreSQL/Supabase.
- La aplicación puede tardar alrededor de un minuto en responder si Render la ha dejado en spin down (esto ocurre cuando no se registra actividad en el backend durante 15 minutos).
- Sin embargo, la app está pensada para acceder desde el [enlace de frontend](#repositorio-relacionado).

## Snapshot diaria

El backend incluye un flujo de snapshot diario que publica una release fija en GitHub con el tag `snapshot-latest`.

Esa release contiene:

- El archivo SQL de la snapshot.
- Los archivos de código fuente comprimidos que GitHub ofrece automáticamente.

### Importar la snapshot en local

El script `scripts/import-latest-snapshot.ps1` descarga la última snapshot publicada y la importa en el backend local.

Funcionamiento básico:

1. Desde `watts2crypto-bacl/scripts` ejecuta el único script que hay `importar-latest-snapshot.ps1`, se recomienda hacer esto habiendo levantado previamente la app con Docker.
2. Si el backend no está levantado, el propio script puede arrancar Docker Compose y reintentar la importación de la snapshot a la base de datos local.
3. Si solo se quiere importar la snapshot sin arrancar la app, el mismo script almacena automáticamente las snapshots en `data/snapshots`, que se pueden importar posteriormente cuando se desee ejecutando el script de nuevo.

Después de importar la snapshot, lo recomendable es reiniciar todo el entorno para que la base de datos y la app queden sincronizadas:

```bash
docker compose restart
```
## Licencia y uso

Este proyecto se ha desarrollado con fines académicos como parte de un Trabajo de Fin de Grado (TFG).

Todos los derechos están reservados. Queda prohibida la redistribución, modificación o explotación comercial del código fuente.

Para más información, consulte el archivo LICENSE.
