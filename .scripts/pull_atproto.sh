#!/bin/bash
ls

# Downloading the latest version of atproto from the main branch
URL="https://github.com/bluesky-social/atproto/archive/refs/heads/main.zip"

# Define the output file name
OUTPUT_FILE="atproto_main.zip"

# Use curl to download the file
echo "Downloading from $URL..."
curl -L -o "$OUTPUT_FILE" "$URL"

# Check if the download was successful
if [ $? -eq 0 ]; then
    echo "Download completed successfully: $OUTPUT_FILE"
else
    echo "Download failed."
    exit 1
fi

# Unzip the downloaded file
unzip -o "$OUTPUT_FILE"

# Remove the downloaded zip file
rm "$OUTPUT_FILE"

# Remove all contents except the lexicon folder of atproto
mv ./atproto-main/lexicons ..
rm -rf atproto-main
