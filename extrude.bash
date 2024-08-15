#!/usr/bin/env bash

indir="./ase/tilesets/"
outdir="./src/assets/tilesets"
width=8
height=8
find $indir* -name '*.png' -print0 | while read -r -d '' filename; do
	file="${filename#$indir}"
	echo "Extruding $filename into $outdir/$file"

	tile-extruder --tileWidth $width --tileHeight $height --input $filename --output $outdir/$file
done
