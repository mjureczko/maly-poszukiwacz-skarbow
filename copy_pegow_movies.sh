#!/bin/bash

SRC_DIR="$HOME/Videos/maly-poszukiwacz-skarbow-pegow-video"
DEST_DIR="$HOME/StudioProjects/maly-poszukiwacz-skarbow/app/src/pegow/assets"

# Check if source directory exists
if [ ! -d "$SRC_DIR" ]; then
  echo "Error: Directory $SRC_DIR does not exist."
  exit 1
fi

echo "Entering $SRC_DIR"
cd "$SRC_DIR" || exit 1

echo "Running git pull..."
git pull

echo "Searching subdirectories and copying files..."

# Find NN_pegow.mp4 (two digits) in subdirectories and copy (overwrite existing)
find "$SRC_DIR" -mindepth 2 -maxdepth 2 -type f -regextype posix-extended -regex ".*/[0-9]{2}_pegow\.mp4" | while read -r file; do
  echo "Copying $file -> $DEST_DIR"
  cp -f "$file" "$DEST_DIR"
done

echo "Done."