#!/usr/bin/env bash

set -euo pipefail

if [ -z "${RENDER_WEB_SERVICE_URL:-}" ]; then
  echo "Missing RENDER_WEB_SERVICE_URL repository variable."
  exit 1
fi

if [ -z "${REFRESH_TOKEN:-}" ]; then
  echo "Missing REFRESH_TOKEN secret."
  exit 1
fi

if [ -z "${ENDPOINT:-}" ]; then
  echo "Missing ENDPOINT value."
  exit 1
fi

BASE_URL="${RENDER_WEB_SERVICE_URL%/}"
URL="$BASE_URL/api/refresh/$ENDPOINT"
MAX_ATTEMPTS=8
ATTEMPT=1
DELAY=15

while [ "$ATTEMPT" -le "$MAX_ATTEMPTS" ]; do
  echo "Calling $URL (attempt $ATTEMPT/$MAX_ATTEMPTS)"

  RESPONSE_FILE="$(mktemp)"
  HTTP_CODE="000"

  if HTTP_CODE=$(curl -sS --show-error \
    --connect-timeout 30 \
    --max-time 900 \
    -o "$RESPONSE_FILE" \
    -w "%{http_code}" \
    -X POST "$URL" \
    -H "Accept: application/json" \
    -H "Authorization: Bearer $REFRESH_TOKEN" \
    -H "X-Refresh-Token: $REFRESH_TOKEN"); then
    if [ "$HTTP_CODE" = "200" ]; then
      cat "$RESPONSE_FILE"
      echo
      rm -f "$RESPONSE_FILE"
      exit 0
    fi
  fi

  if [ -s "$RESPONSE_FILE" ]; then
    echo "Response body:"
    cat "$RESPONSE_FILE"
    echo
  fi

  rm -f "$RESPONSE_FILE"

  if [ "$HTTP_CODE" = "401" ] || [ "$HTTP_CODE" = "403" ]; then
    echo "Authorization failed for $ENDPOINT."
    exit 1
  fi

  if [ "$ATTEMPT" -ge "$MAX_ATTEMPTS" ]; then
    echo "Endpoint $ENDPOINT failed after $MAX_ATTEMPTS attempts."
    exit 1
  fi

  echo "HTTP $HTTP_CODE. Retrying in ${DELAY}s to wait for Render cold start..."
  sleep "$DELAY"

  if [ "$DELAY" -lt 60 ]; then
    DELAY=$((DELAY * 2))
    if [ "$DELAY" -gt 60 ]; then
      DELAY=60
    fi
  fi

  ATTEMPT=$((ATTEMPT + 1))
done

exit 1