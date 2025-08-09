#!/usr/bin/env bash
source "scripts/github_release_utils.sh"
source "scripts/github_cache_utils.sh"
source "scripts/general_utils.sh"

# KT Lint Helper Functions

function get_kt_lint() {
  local url="https://api.github.com/repos/pinterest/ktlint/releases/latest"
  local cache_dir="$1"
  local executable_path="${cache_dir}/ktlint"
  local version_file="${cache_dir}/ktlint.version"
  local version

  ensure_path "$cache_dir"

  if [ -f "$version_file" ]; then
    version=$(cat "$version_file")
  fi

  if ! is_cached "$url" "$version"; then
    download_kt_lint "$url" "$executable_path"
  else
    echo "✅ ktlint is up to date (version $(cat "$version_file")). Using cached version."
  fi
}

function download_kt_lint() {
  local url="$1"
  local kt_lint_path="$2"
  local release_info
  local version
  local download_url

  release_info=$(get_release_info "$url")
  version=$(get_github_release_version "$release_info")
  download_url=$(get_download_url "$release_info" "ktlint")

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
  local url="https://api.github.com/repos/mrmans0n/compose-rules/releases/latest"
  local cache_dir="$1"
  local executable_path="${cache_dir}/compose-ruleset.jar"
  local version_file="${cache_dir}/compose-ruleset.version"
  local version

  ensure_path "$cache_dir"

  if [ -f "$version_file" ]; then
    version=$(cat "$version_file")
  fi

  if ! is_cached "$url" "$version"; then
    download_compose_ruleset "$url" "$executable_path"
  else
    echo "✅ compose ruleset is up to date (version $(cat "$version_file")). Using cached version."
  fi
}

function download_compose_ruleset() {
  local url="$1"
  local ruleset_path="$2"
  local release_info
  local version
  local download_url

  release_info=$(get_release_info "$url")
  version=$(get_github_release_version "$release_info")
  download_url=$(get_download_url "$release_info" "ktlint")

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