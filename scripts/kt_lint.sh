#!/usr/bin/env bash
source "scripts/github_release_utils.sh"
source "scripts/github_cache_utils.sh"
source "scripts/general_utils.sh"

# KT Lint Helper Functions

function get_kt_lint() {
  local cache_dir="$1"
  local executable_path="${cache_dir}/ktlint"
  local version_file="${cache_dir}/ktlint.version"
  local version="1.8.0" # Pinning version to avoid rate limiting
  # For latest versions check: https://github.com/pinterest/ktlint/releases

  ensure_path "$cache_dir"

  if [ -f "$version_file" ]; then
    local cached_version
    cached_version=$(cat "$version_file")
    if [ "$cached_version" != "$version" ]; then
        echo "🔄 Cached version ($cached_version) matches pinned version ($version). Updating..."
        rm "$version_file"
        rm -f "$executable_path"
    fi
  fi

  if [ ! -f "$executable_path" ]; then
    download_kt_lint "$version" "$executable_path"
  else
    echo "✅ ktlint is up to date (version $version). Using cached version."
  fi
}

function download_kt_lint() {
  local version="$1"
  local kt_lint_path="$2"
  local download_url="https://github.com/pinterest/ktlint/releases/download/${version}/ktlint"

  echo "INFO: Downloading ktlint version:   $version"
  echo "INFO: Downloading ktlint from:      $download_url"

  download_file "$download_url" "$kt_lint_path"
  make_executable "$kt_lint_path"
  write_version "$version" "$kt_lint_path"

  if [ ! -f "$kt_lint_path" ]; then
    echo "❌ ERROR: Failed to download ktlint. Exiting."
    return 1
  fi

  echo "✅ ktlint version ${version} downloaded in ${kt_lint_path}."
}

# Compose Ruleset Helper Functions

function get_compose_ruleset() {
  local cache_dir="$1"
  local executable_path="${cache_dir}/compose-ruleset.jar"
  local version_file="${cache_dir}/compose-ruleset.version"
  local version="0.5.3" # Pinning version to avoid rate limiting
  # For latest versions check: https://github.com/mrmans0n/compose-rules/releases

  ensure_path "$cache_dir"

  if [ -f "$version_file" ]; then
    local cached_version
    cached_version=$(cat "$version_file")
    if [ "$cached_version" != "$version" ]; then
        echo "🔄 Cached ruleset version ($cached_version) does not match pinned version ($version). Updating..."
        rm "$version_file"
        rm -f "$executable_path"
    fi
  fi

  if [ ! -f "$executable_path" ]; then
    download_compose_ruleset "$version" "$executable_path"
  else
    echo "✅ compose ruleset is up to date (version $version). Using cached version."
  fi
}

function download_compose_ruleset() {
  local version="$1"
  local ruleset_path="$2"
  local download_url="https://github.com/mrmans0n/compose-rules/releases/download/v${version}/ktlint-compose-${version}-all.jar"

  echo "INFO: Downloading ktlint compose ruleset version:   $version"
  echo "INFO: Downloading ktlint compose ruleset from:      $download_url"
  ruleset_path=$(ensure_extension "$ruleset_path" ".jar")
  download_file "$download_url" "$ruleset_path"
  make_executable "$ruleset_path"
  write_version "$version" "$ruleset_path"

  if [ ! -f "$ruleset_path" ]; then
    echo "❌ ERROR: Failed to download ktlint Compose Ruleset. Exiting."
    return 1
  fi

  echo "✅ ktlint Compose Ruleset $version downloaded in $ruleset_path."
}