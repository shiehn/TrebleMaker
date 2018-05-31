#!/usr/bin/env bash

export TM_ROOT_DIR=/TrebleMaker

export TM_DB_CONN="jdbc:mysql://localhost:3306/treblemakerdb?useSSL=false&autoReconnect=true&maxReconnects=360&initialTimeout=2"
export TM_DB_USER=treble
export TM_DB_PASS=maker

export TM_API_USER=treble
export TM_API_PASS=maker
export TM_API_URL=http://treblemakerapi:7777
export TM_API_UPLOAD_URL=http://treblemakerapi:7777/filesync

export TM_CACHE_CONN=[YOUR_CACHE_URL]:5701

export TM_BYPASS_RHYTHM_RATING=fals
export TM_BYPASS_BASSLINE_RATING=false
export TM_BYPASS_ARPEGGIO_RATING=false
export TM_BYPASS_HARMONIC_LOOP_RATING=false
export TM_BYPASS_BEAT_LOOP_RATING=false
export TM_BYPASS_SEQUENCE_RATING=false
export TM_BYPASS_EQ_RATING=false
export TM_BYPASS_ANALYTICS=false
export TM_BYPASS_EQ_ANALYTICS=false

export TM_NUM_OF_MIXES=1
export TM_NUM_OF_MIX_VARIATIONS=1

export TM_CORE_LOG_DIR=logs

export TM_QUEUE_POLL_INTERVAL=60000

export AWS_BUCKET_NAME=[YOUR-BUCKET-NAME]
export AWS_ACCESS_KEY_ID=[YOUR-AWS-ACCESS-KEY]
export AWS_SECRET_ACCESS_KEY=[YOUR-AWS-SECRECT-KEY]
