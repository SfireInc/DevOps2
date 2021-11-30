#!/bin/sh

/liquibase/wait-for-it.sh resa-db:5432 --timeout=20

/liquibase/liquibase \
  --driver="${DRIVER}" \
  --url="${URL}" \
  --changeLogFile="${CHANGELOGFILE}" \
  --username="${USERNAME}" \
  --password="${PASSWORD}" \
  --logLevel="${LOGLEVEL}" \
  "${CMD}"
