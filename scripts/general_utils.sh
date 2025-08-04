#!/usr/bin/env bash

function ensure_extension() {
  local file_path="$1"
  local extension="$2"

  if [ -z "$file_path" ]; then
    echo "❌ ERROR: Path argument not provided." >&2
    return 1
  fi

  if [ -z "$extension" ]; then
    echo "❌ ERROR: Extension argument not provided." >&2
    return 1
  fi

  if [[ "$extension" != .* ]]; then
    echo "❌ ERROR: Extension argument '$extension' does not start with a dot." >&2
    return 1
  fi

  # Check if it's a directory; if so, we can't just append an extension to a directory name
  if [ -d "$file_path" ]; then
    echo "❌ ERROR: Path argument '$file_path' is a directory. Please provide a file path." >&2
    return 1
  fi

  # Check if it already ends with extension
  if [[ "$file_path" != *"$extension" ]]; then
    echo "INFO: Path '$file_path' does not end with '$extension'. Appending it." >&2
    file_path="${file_path}${extension}"
  fi

  echo "$file_path"
}

function download_file() {
  local url="$1"
  local destination="$2"

  if ! curl -fL "$url" -o "$destination"; then
      echo "❌ ERROR: Failed to download $url | Exiting." >&2
      rm -f "$destination"
      return 1
  fi
}

function make_executable() {
  local path="$1"

  if ! chmod +x "$path"; then
      echo "❌ ERROR: Failed to make executable at $path. Exiting." >&2
      return 1
  fi
}

function strip_extension() {
  local file_path="$1"
  local filename
  local path_without_filename

  if [ -z "$file_path" ]; then
    echo "❌ ERROR: Path argument not provided to strip_extension." >&2
    return 1
  fi

  filename="${file_path##*/}"

  path_without_filename="${file_path%/*}"

  if [[ "$filename" == "." || "$filename" == ".." ]]; then
    echo "$file_path"
    return 0
  fi

  if [[ "$filename" == *.* && "$filename" != "${filename#*.}" ]]; then
    filename_without_extension="${filename%.*}"

    if [[ "$path_without_filename" == "$file_path" ]]; then
      echo "$filename_without_extension"
    else
      echo "${path_without_filename}/${filename_without_extension}"
    fi
  else
    echo "$file_path"
  fi
}

function write_version() {
  local version="$1"
  local file_path="$2"

  file_path=$(strip_extension "$file_path")
  file_path=$(ensure_extension "$file_path" ".version")

  echo "$version" > "$file_path"
}

function ensure_path() {
  local path="$1"
  local path_exists

  path_exists=$( (test -e "$path" && echo "true") || echo "false")

  if [ "$path_exists" == "false" ]; then
    mkdir -p "$path"
    echo "INFO: Path '$path' did not exist. Creating it." >&2
  fi
}