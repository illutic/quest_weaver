#!/usr/bin/env bash

# General Helper Functions

function get_github_release_version() {
  local release_info="$1"
  local release_version

  # Check if curl command was successful and got a response
  if [[ -z "$release_info" ]]; then
    echo "❌ Error: Failed to fetch release info from GitHub." >&2
    return 1
  fi

  # Extract the tag_name using jq
  # -r for raw output (removes quotes from the string)
  release_version=$(echo "$release_info" | jq -r '.tag_name')

  if [[ -z "$release_version" || "$release_version" == "null" ]]; then
    echo "❌ Error: Failed to parse version from API response." >&2
    echo "Response was: $release_info" >&2
    return 1
  fi

  echo "$release_version"
}

function get_release_info() {
  local url="$1"
  local release_info
  release_info=$(curl -sL "$url")
  if [ $? -ne 0 ]; then
      echo "❌ ERROR: Failed to fetch release information from GitHub. Exiting." >&2
      return 1
  fi

  if [ -z "$release_info" ]; then
      echo "❌ ERROR: Received empty response from GitHub API for releases. Exiting." >&2
      return 1
  fi

  echo "$release_info"
}

function get_download_url() {
  local release_info="$1"
  local field="$2"
  local download_url

  download_url=$(echo "$release_info" | jq -r "([.assets[] | select(.name | contains(\"$field\"))] | .[0].browser_download_url)")
  if [ $? -ne 0 ]; then
      echo "❌ ERROR: Failed to parse download url. Exiting." >&2
      return 1
  fi

  if [ -z "$download_url" ]; then
      echo "❌ ERROR: Could not find download URL for a \"name\" asset named '$field'. Exiting." >&2
      return 1
  fi

  echo "$download_url"
}
