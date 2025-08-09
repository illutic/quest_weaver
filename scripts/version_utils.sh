#!/usr/bin/env bash

source "scripts/general_utils.sh"

function increment_version_code() {
  local version_code
  local version_code_field="gr.questweaver.version.code"
  local version_code_file="gradle.properties"

  version_code="$(get_value "$version_code_field" "$version_code_file")"

  ((version_code++))

  replace_field_in_properties "$version_code_field" "$version_code" "$version_code_file"

  if [[ $? -ne 0 ]]; then
    echo "❌ Error: Failed to increment version code." >&2
    return 1
  fi

  echo "Version code incremented to: $version_code"
}

function update_version_name() {
  local version_name="$1"
  local version_name_field="gr.questweaver.version.name"
  local version_name_file="gradle.properties"

  if [[ -z "$version_name" ]]; then
    echo "❌ Error: Version name argument is required." >&2
    return 1
  fi

  if ! [[ "$version_name" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
    echo "❌ Error: Version name must be in the format X.Y.Z (e.g., 1.0.0)." >&2
    return 1
  fi

  replace_field_in_properties "$version_name_field" "$version_name" "$version_name_file"

 if [[ $? -ne 0 ]]; then
    echo "❌ Error: Failed to update version name." >&2
    return 1
  fi

  echo "Version name updated to: $version_name"
}

function sanitize_version_name() {
  local version_name="$1"
  local sanitized_version

  if [[ -z "$version_name" ]]; then
    echo "❌ Error: Version name argument is required." >&2
    return 1
  fi

  # Remove any non-numeric and non-dot characters and make sure the version follows the format X.Y.Z
  version_name=$(echo "$version_name" | grep -oE '[0-9]+(\.[0-9]+){0,2}' | tail -n1)

  IFS='.' read -r x y z <<< "$version_name"
  x=${x:-0}
  y=${y:-0}
  z=${z:-0}
  sanitized_version="$x.$y.$z"

  if [[ -z "$sanitized_version" ]]; then
    echo "❌ Error: Version name must contain at least one numeric character." >&2
    return 1
  fi

  echo "$sanitized_version"
}