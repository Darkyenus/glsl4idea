#!/bin/bash

# Move to this directory
cd "$(dirname "$0")"

if [ -e piglit-repo ]; then
	echo "Repo exists"
	cd piglit-repo
	git pull
	cd ..
else
	echo "Cloning repo"
	git clone --depth=1 git://anongit.freedesktop.org/git/piglit piglit-repo
fi

if [ ! -e piglit ]; then
	echo Creating piglit directory
	mkdir piglit
fi

cd piglit-repo

rm -rf ./examples/

for FILE in $(find . -type f -name *.frag) $(find . -type f -name *.vert);
do
	if [ -o "-v" ]; then
		echo "Found $FILE"
	fi
	cp "$FILE" "../piglit/$(basename "$FILE")"
done

cd ..
if [ ! -o "-keeprepo" ]; then
	rm -rf piglit-repo
	echo "Repo deleted (keep with -keeprepo flag)"
fi

echo Done