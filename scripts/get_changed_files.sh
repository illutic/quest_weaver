#!/usr/bin/env bash

function get_changed_files() {
    local commits_to_check="$1"
    local files
    local kt_files
    local files_for_output
    
    # Ensure jq is available
    if ! command -v jq &> /dev/null; then
        echo "❌ ERROR: jq is not installed. Please install jq to process JSON." >&2
        return 1
    fi

    # Get changed files based on the number of commits
    files="$(git diff --name-only --diff-filter=ACMR HEAD~"$commits_to_check"..HEAD || echo "")"
    echo "Changed files: $files" >&2

    # Filter for Kotlin files only if we have files
    if [ -n "$files" ]; then
        kt_files="$(echo "$files" | grep -E '\.(kt|kts)$' || echo "")"
    else
        kt_files=""
    fi
    echo "Kotlin files: $kt_files" >&2

    # Convert to JSON array, handling empty case properly
    if [ -n "$kt_files" ]; then
        files_for_output="$(echo "$kt_files" | jq -R -s -c 'split("\n") | map(select(length > 0 and . != ""))')"
    else
        files_for_output="[]"
    fi

    echo "$files_for_output"
}