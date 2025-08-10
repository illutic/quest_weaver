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
  local token="$2"
  local release_info

  release_info=$(curl -sL \
    -H "Accept: application/vnd.github+json" \
    -H "Authorization: Bearer $token" \
    -H "X-GitHub-Api-Version: 2022-11-28" \
    "$url")

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

function upload_file() {
  local repo_name="$1"
  local release_id="$2"
  local token="$3"
  local file_path="$4"
  local file_name

  file_name=$(basename "$file_path")

  curl -L \
    -X POST \
    -H "Accept: application/vnd.github+json" \
    -H "Authorization: Bearer $token" \
    -H "X-GitHub-Api-Version: 2022-11-28" \
    -H "Content-Type: application/octet-stream" \
    "https://uploads.github.com/repos/$repo_name/releases/$release_id/assets?name=$file_name" \
    --data-binary "@$file_path"

  if [ $? -ne 0 ]; then
      echo "❌ ERROR: Failed to upload file $file_path. Exiting." >&2
      return 1
  fi

  echo "✅ File $file_path uploaded successfully."
}

function upload_release_assets() {
  local repo_name="$1"
  local release_id="$2"
  local token="$3"
  local files

  files=$(find . -type f \( -name "*.aab" -o -name "*.apk" \))

  if [ -z "$files" ]; then
      echo "❌ ERROR: No files found to upload. Exiting." >&2
      return 1
  fi

  echo "Found files to upload: $files"

  for file_path in $files; do
      upload_file "$repo_name" "$release_id" "$token" "$file_path"
  done
}