#!/bin/bash

# Define paths
# $SRCROOT is passed by Xcode env. If not set, fallback.
if [ -n "$SRCROOT" ]; then
    REPO_ROOT="$SRCROOT/.."
    ASSETS_DIR="$SRCROOT/iosApp/Assets.xcassets"
else
    REPO_ROOT=".."
    ASSETS_DIR="iosApp/Assets.xcassets"
fi

SOURCE_DIR="$REPO_ROOT/feature/onboarding/src/commonMain/composeResources/drawable"

echo "Syncing all assets from $SOURCE_DIR to $ASSETS_DIR..."

if [ ! -d "$SOURCE_DIR" ]; then
    echo "Error: Source directory not found at $SOURCE_DIR"
    exit 1
fi

# Iterate over files in the source directory
for FILE_PATH in "$SOURCE_DIR"/*; do
    # Check if it is a file
    if [ -f "$FILE_PATH" ]; then
        FILENAME=$(basename "$FILE_PATH")
        BASENAME="${FILENAME%.*}"
        EXTENSION="${FILENAME##*.}"
        
        # Skip hidden files or DS_Store
        if [[ "$FILENAME" == .* ]]; then
            continue
        fi

        DEST_IMAGE_SET="$ASSETS_DIR/$BASENAME.imageset"
        DEST_FILE="$DEST_IMAGE_SET/$FILENAME"
        
        # echo "Processing $FILENAME..."
        
        mkdir -p "$DEST_IMAGE_SET"
        cp "$FILE_PATH" "$DEST_FILE"
        
        # Determine if we should preserve vector representation (Standard for SVGs in iOS Assets)
        PRESERVES_VECTOR="false"
        if [[ "$EXTENSION" == "svg" ]]; then
            PRESERVES_VECTOR="true"
        fi
        
        # Create Contents.json
        # We handle SVGs as "universal" idiom with "preserves-vector-representation"
        cat <<EOF > "$DEST_IMAGE_SET/Contents.json"
{
  "images" : [
    {
      "filename" : "$FILENAME",
      "idiom" : "universal"
    }
  ],
  "info" : {
    "author" : "xcode",
    "version" : 1
  },
  "properties" : {
    "preserves-vector-representation" : $PRESERVES_VECTOR
  }
}
EOF
    fi
done

echo "Asset Sync Complete."
