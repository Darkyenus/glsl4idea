#!/bin/bash

# Move to this directory
cd "$(dirname "$0")"

if [ -d piglit-repo ]; then
	echo "Repo exists"
	cd piglit-repo
	git pull
	cd ..
else
	echo "Cloning repo"
	git clone --depth=1 git://anongit.freedesktop.org/git/piglit piglit-repo
fi

if [ -d piglit ]; then
	echo Deleting piglit directory
	# To clean
	rm -rf piglit/
fi

echo Creating piglit directory
mkdir piglit

mkdir piglit/pass
mkdir piglit/fail
mkdir piglit/other

cd piglit-repo

rm -rf ./examples/

for FILE in $(find . -type f -name *.frag) $(find . -type f -name *.vert);
do
	if [ ! -z "$(grep "expect_result: pass" "$FILE")" ]; then
		cp "$FILE" "../piglit/pass/$(basename "$FILE")"
	elif [ ! -z "$(grep "expect_result: fail" "$FILE")" ]; then
        cp "$FILE" "../piglit/fail/$(basename "$FILE")"
    else
        cp "$FILE" "../piglit/other/$(basename "$FILE")"
    fi
done

cd ..

if [ "$1" != "keeprepo" ]; then
	rm -rf piglit-repo
	echo "Repo deleted"
fi

echo Done
