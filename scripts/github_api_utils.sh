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

  curl -L \
    -X PATCH \
    -H "Accept: application/vnd.github+json" \
    -H "Authorization: Bearer $token" \
    -H "X-GitHub-Api-Version: 2022-11-28" \
    "$url" \
    -d "$json_body"
}