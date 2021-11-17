#!/bin/sh

do_with_retry()
{
  timeout 5m sh -c "until ${1}; do sleep 10; done"
}

submit_json()
{
  do_with_retry "curl -d@${1} http://apisix:9080/apisix/admin/routes -H 'X-API-KEY: edd1c9f034335f136f87ad84b625c8f1' -X POST"
}

submit_json /work/user-routes.json
submit_json /work/pasteread-routes.json
submit_json /work/pastewrite-routes.json
