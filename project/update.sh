#!/bin/sh

# misaki
rm -rf misaki/*
cp -pir $HOME/lisp/misaki/sample/public/* misaki/

find misaki -type f -name "*.html" -exec perl -p -i -e 's/"\//"\/project\/misaki\//g' {} \;
find misaki -type f -name "main.css" -exec perl -p -i -e 's/\/img/\/project\/misaki\/img/g' {} \;

