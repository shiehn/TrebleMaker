#!/bin/bash

until $(curl --output /dev/null --silent --head --fail "${TM_API_URL}/api/track"); do
    echo "WAITING ON TREBLE_MAKER_API !!!"
    sleep 5
done

echo "TREBLE MAKER API IS UP!!"
echo "TREBLE MAKER API IS UP!!"
echo "TREBLE MAKER API IS UP!!"
echo "TREBLE MAKER API IS UP!!"
echo "TREBLE MAKER API IS UP!!"

cd TrebleMakerCore

mvn clean spring-boot:run
