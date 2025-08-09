#!/usr/bin/env bash

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