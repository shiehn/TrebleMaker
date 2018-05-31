#!/bin/bash

curl -o treblemakerdeps.tar https://s3-us-west-2.amazonaws.com/treblemakerdeps/treblemakerdeps.tar

tar xvf treblemakerdeps.tar

rm -rf treblemakerdeps.tar

pushd TrebleMakerCore
curl -o trained-chords-model.zip https://s3-us-west-2.amazonaws.com/treblemakerdeps/trained-chords-model.zip

curl -o trained-melody-model.zip https://s3-us-west-2.amazonaws.com/treblemakerdeps/trained-melody-model.zip
popd 
