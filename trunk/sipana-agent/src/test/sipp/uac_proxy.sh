#!/bin/bash

sipp -sf uac_proxy.xml -i 127.0.0.1 -p 5080 -mi 127.0.0.1 -mp 8080 $@ 127.0.0.1:5060

