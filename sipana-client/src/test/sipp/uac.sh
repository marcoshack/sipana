#!/bin/bash

sipp -sf uac.xml -i 127.0.0.1 -p 5099 -mi 127.0.0.1 -mp 8080 $@ 127.0.0.1:5098

