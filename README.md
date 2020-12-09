# RottenBanana - Movie recommender 

This is the code for a conversational system created for the course WASP autonomus systems 2 - part 2.
Members of project: Faseeh Ahmad, Matthías Páll Gissurarson, Frida Heskebeck, Matteo Iovino, and Yiping Xie. 

The conversational system is a furhat robot (see: https://furhatrobotics.com/ and https://docs.furhat.io/) 
Follow their instructions to set up the Furhat. 

## Run server and conversational system
To be able to run the movie recommender system you have to run the python script query_server.py in the background. Make sure you have all required packages installed, you should be able to do the following imports: 

```bash
import sys

import os

import pandas as pd

import csv

import requests

from flask import Flask

from flask import request

import json
```


When you have everythin installed and you stand in the folder where the script is, you run it with: python3 query_server.py
Let that terminal be and start the conversational system. 

## Common problems
* Enter fewer preferences if you don't get any movie recommendation at the end. 
* If the system don't understand the year preferences, say "in between xxxx and yyyy". 
