#!/usr/bin/env bash

function update_variable() {
  local variable_name="$1"
  local new_value="$2"
  local repos_name="$3"
  local environment="$4"
  local token="$5"
  local json_body
  local url

  json_body="$(jq -n \
    --arg name "$variable_name" \
    --arg value "$new_value" \
    '{name: $name, value: $value}')"

  url="https://api.github.com/repos/$repos_name/environments/$environment/variables/$variable_name"

  echo "Updating variable '$variable_name' in repo '$repos_name' with new value '$new_value'..." >&2
  echo "Using token: $token" >&2
  echo "JSON body: $json_body" >&2
  echo "Request URL: $url" >&2
  echo "Headers: Accept: application/vnd.github+json, Authorization: Bearer $token, X-GitHub-Api-Version: 2022-11-28" >&2
  echo "Executing PATCH request..." >&2

  curl -L \
    -X PATCH \
    -H "Accept: application/vnd.github+json" \
    -H "Authorization: Bearer $token" \
    -H "X-GitHub-Api-Version: 2022-11-28" \
    "$url" \
    -d "$json_body"
}