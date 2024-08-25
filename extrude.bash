#!/usr/bin/env bash

indir="./ase/tilesets/$1"
outdir="./src/assets/tilesets"
find $indir* -name '*.png' -print0 | while read -r -d '' filename; do
	file="${filename#$indir}"
	echo "Extruding $filename into $outdir/$file"

	tile-extruder --tileWidth $1 --tileHeight $1 --input $filename --output $outdir/$file
done
