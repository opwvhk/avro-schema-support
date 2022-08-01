#!/bin/sh
# Find the last release of type release or EAP for these products:
# - IIU (IntelliJ IDEA Ultimate)
# - IIC (IntelliJ IDEA Community edition)
# - PCP (PyCharm Professional)
# - PCC (PyCharm Community edition)
# Then locate the build number for each release, truncate it at the first dot, and find the highest.

LAST_BUILD=$(curl \
	'https://data.services.jetbrains.com/products/releases?code=IIU&code=IIC&code=PCP&code=PCC&latest=true&type=eap&type=release' 2>/dev/null |
	jq -r '.[][0].build' |
	cut -d . -f 1 |
	sort -r | head -n 1)
echo "# Highest released build number for IntelliJ IDEA Ultimate / Community edition and PyCharm Professional / PyCharm Community edition" >jetbrains.lastBuild.txt
echo "${LAST_BUILD}" >>jetbrains.lastBuild.txt
