#!/usr/bin/env bash

function is_cached() {
  local url="$1"
  local cached_version="$2"
  local release_info
  local remote_version

  release_info=$(get_release_info "$url")
  remote_version=$(get_github_release_version "$release_info")

  if [[ "$remote_version" == "$cached_version" ]]; then
    return 0
  else
    return 1
  fi
}

