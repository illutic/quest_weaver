#!/usr/bin/env bash

function sanitize_version_name() {
  local version_name="$1"
  local sanitized_version

  if [[ -z "$version_name" ]]; then
    echo "❌ Error: Version name argument is required." >&2
    return 1
  fi

  # Strip path prefixes (everything up to the final slash)
  sanitized_version="${version_name##*/}"
  
  # Strip leading 'v' or 'V'
  sanitized_version="${sanitized_version#[vV]}"

  if [[ -z "$sanitized_version" ]]; then
    echo "❌ Error: Version name is empty after sanitization." >&2
    return 1
  fi

  echo "$sanitized_version"
}