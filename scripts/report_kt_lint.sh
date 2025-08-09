#!/usr/bin/env bash

convert_json_to_markdown() {
    local json_input_file="$1"
    local json_input
    local file_count
    local total_errors

    # Check if jq is available
    if ! command -v jq &> /dev/null; then
        echo "Error: jq is required but not installed. Please install jq first." >&2
        exit 1
    fi

    # Check if the input file exists
    if [ ! -f "$json_input_file" ]; then
        echo "Error: Input file '$json_input_file' does not exist." >&2
        exit 1
    fi

    # Read JSON input from the file
    json_input=$(cat "$json_input_file")

    # Validate JSON input
    if ! echo "$json_input" | jq empty 2>/dev/null; then
        echo "Error: Invalid JSON input" >&2
        exit 1
    fi

    # Generate markdown header
    echo "# Code Quality Report"
    echo ""

    # Check if there are any files with errors
    file_count=$(echo "$json_input" | jq 'length')
    total_errors=$(echo "$json_input" | jq '[.[].errors | length] | add // 0')

    # Summary
    echo "**Summary:** $total_errors error(s) found across $file_count file(s)"
    echo ""

    # Check if there are no errors
    if [ "$total_errors" -eq 0 ]; then
        echo "No errors found! 🎉"
        return
    fi

    # Process each file
    echo "$json_input" | jq -r '
        .[] |
        select(.errors | length > 0) |
        .file as $file_name |
        "## 📁 \($file_name | split("/") | last)\n\n| Rule | Line | Message | File path |\n|------|--------|---------|-----|\n" +
        (
            .errors[] |
            "| `\(.rule)` | \(.line) | \(.message | gsub("\\|"; "\\\\|")) | `\($file_name)` |"
        ) +
        "\n"
    '
}

annotate_kt_lint() {
    local json_input_file="$1"
    local json_input
    local file_count
    local total_errors

    # Check if jq is available
    if ! command -v jq &> /dev/null; then
        echo "Error: jq is required but not installed. Please install jq first." >&2
        exit 1
    fi

    # Check if the input file exists
    if [ ! -f "$json_input_file" ]; then
        echo "Error: Input file '$json_input_file' does not exist." >&2
        exit 1
    fi

    # Read JSON input from the file
    json_input=$(cat "$json_input_file")

    # Validate JSON input
    if ! echo "$json_input" | jq empty 2>/dev/null; then
        echo "Error: Invalid JSON input" >&2
        exit 1
    fi

    # Check if there are any files with errors
    file_count=$(echo "$json_input" | jq 'length')
    total_errors=$(echo "$json_input" | jq '[.[].errors | length] | add // 0')

    if [ "$total_errors" -eq 0 ]; then
        echo "No errors found! 🎉"
        return
    fi

    # Process each file
    # ::error file={name},line={line},endLine={endLine},title={title}::{message}
    echo "$json_input" | jq -r '
        .[] |
        select(.errors | length > 0) |
        .file as $file_name |
        .errors[] |
        "::error file=\($file_name | split("/") | last),line=\(.line),column=\(.column),endColumn=\(.column),endLine=\(.line),title=\"\(.rule)\"::\(.message)"
    '
}