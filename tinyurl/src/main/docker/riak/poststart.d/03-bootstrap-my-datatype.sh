#!/bin/bash

# Create custom default KV bucket types
$RIAK_ADMIN bucket-type create tinyurl || echo 'OK'
$RIAK_ADMIN bucket-type activate tinyurl || echo 'OK'
