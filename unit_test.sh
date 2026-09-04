#!/bin/bash

set -e

./gradlew testKalinowiceCustomDebugTest
echo ""
echo "##################################################"
echo "###   Kalinowice tested!!!                    ####"
echo "##################################################"
echo ""
./gradlew testPegowCustomDebugUnitTest
echo ""
echo "##################################################"
echo "###   Pęgów tested!!!                         ####"
echo "##################################################"
echo ""
./gradlew testDefaultAssetClassicDebugUnitTest
echo ""
echo "##################################################"
echo "###   Classic tested!!!                       ####"
echo "##################################################"
