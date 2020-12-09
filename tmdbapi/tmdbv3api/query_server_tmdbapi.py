import sys
import os
import pandas as pd
import csv
import requests

from flask import Flask
from flask import request
import json
from tmdb_server import tmdb_base

tmdb_base = tmdb_base()

app = Flask(__name__)

@app.route('/',methods = ['POST'])
def determine_escalation():
    query = request.get_json()
    print(query)
    tmdb_base.set_attributes(query)
    res = tmdb_base.search_movies()
    print(res)
    nl = '\n'.join(res)
    print(nl)
    return nl

if __name__ == '__main__':
    app.run(debug=True, host='127.0.0.1', port=8042)

