import sys
import os
import pandas as pd
import csv
import requests

from flask import Flask
from flask import request
import json

# to handle JSON queries

def format_languages(query):

    all_languages = ['de', 'en', 'es', 'fr', 'it', 'sv']

    languages = query['language']['selected']
    for i in range(len(languages)):
        if languages[0] == 'german':
            languages.pop(0)
            languages.append('de')
        if languages[0] == 'english':
            languages.pop(0)
            languages.append('en')
        if languages[0] == 'spanish':
            languages.pop(0)
            languages.append('es')
        if languages[0] == 'french':
            languages.pop(0)
            languages.append('fr')
        if languages[0] == 'italian':
            languages.pop(0)
            languages.append('it')
        if languages[0] == 'swedish':
            languages.pop(0)
            languages.append('sv')

    query['language']['selected'] = languages

    languages = query['language']['deselected']
    for i in range(len(languages)):
        if languages[0] == 'german':
            languages.pop(0)
            languages.append('de')
        if languages[0] == 'english':
            languages.pop(0)
            languages.append('en')
        if languages[0] == 'spanish':
            languages.pop(0)
            languages.append('es')
        if languages[0] == 'french':
            languages.pop(0)
            languages.append('fr')
        if languages[0] == 'italian':
            languages.pop(0)
            languages.append('it')
        if languages[0] == 'swedish':
            languages.pop(0)
            languages.append('sv')

    query['language']['deselected'] = languages

    if len(query['language']['selected']) > 0:
        for lan in all_languages:
            if lan not in query['language']['selected'] and lan not in query['language']['deselected']:
                query['language']['deselected'].append(lan)

def handle_director(df, director_dict):

    selected = director_dict['selected']
    deselected = director_dict['deselected']

    if len(selected) > 0:
        row_indexes = []
        for row in range(len(df)):
            row_crew = df['crew'].iloc[row]
            row_directors = []
            for people in row_crew:
                if people['job'] == 'Director':
                    row_directors.append(people['name'])
            for val in selected:
                if val not in row_directors:
                    row_indexes.append(row)
        df = df.drop(row_indexes, axis=0)
        df.reset_index(drop=True, inplace=True)

    if len(deselected) > 0:
        row_indexes = []
        for row in range(len(df)):
            row_crew = df['crew'].iloc[row]
            row_directors = []
            for people in row_crew:
                if people['job'] == 'Director':
                    row_directors.append(people['name'])
            for val in deselected:
                if val in row_directors:
                    row_indexes.append(row)
        df = df.drop(row_indexes, axis=0)
        df.reset_index(drop=True, inplace=True)

    return df


def clean_df(df, query_dict, feature, key):

    selected = query_dict['selected']
    deselected = query_dict['deselected']

    if len(selected) > 0:
        row_indexes = []
        for row in range(len(df)):
            row_data = [val[key] for val in df[feature].iloc[row]]
            for val in selected:
                if val not in row_data:
                    row_indexes.append(row)
        df = df.drop(row_indexes, axis=0)
        df.reset_index(drop=True, inplace=True)

    if len(deselected) > 0:
        row_indexes = []
        for row in range(len(df)):
            row_data = [val[key] for val in df[feature].iloc[row]]
            for val in deselected:
                if val in row_data:
                    row_indexes.append(row)
        df = df.drop(row_indexes, axis=0)
        df.reset_index(drop=True, inplace=True)

    return df

def search_movie(df, query):

    df1 = pd.DataFrame.copy(df)

    # Clean Dataframe based on ACTORs
    actor_query = query['actors']
    df1 = clean_df(df1, actor_query, 'cast', 'name')

    # Clean Dataframe based on GENRES
    genre_query = query['genres']
    df1 = clean_df(df1, genre_query, 'genres', 'name')


    # Clean Dataframe based on COMPANY
    company_query = query['company']
    df1 = clean_df(df1, company_query, 'production_companies', 'name')

    # Clean Dataframe based on LANGUAGE
    language_yes = query['language']['selected']
    language_no = query['language']['deselected']

    for lan in language_no:
        df1.drop(df1[df1['original_language'] == lan].index, inplace=True)

    df1.reset_index(drop=True, inplace=True)

    # Clean Dataframe based on DIRECTOR
    df1 = handle_director(df1, query['director'])

    # Clean Datafrane based on YEARS
    df1.drop(df1[df1['release_date'] < query['years']['lower']].index, inplace=True)
    df1.drop(df1[df1['release_date'] > query['years']['upper']].index, inplace=True)
    df1.reset_index(drop=True, inplace=True)


    # Clean Datafrane based on RATING
    df1.drop(df1[df1['vote_average'] < query['rating']].index, inplace=True)
    df1.reset_index(drop=True, inplace=True)


    top10 = df1.sort_values(by=['popularity'], ascending=False)[:10]
    movies = top10['original_title'].tolist()

    return movies


converter = {
    'genres': eval,
    'production_companies': eval,
    'cast' : eval,
    'crew' : eval,
}
data_type = {
    'original_language' : str,
    'original_title': str,
    'popularity' : float,
    'release_date': str,
    'runtime' : float,
    'vote_average' : float,
    'gender_of_lead' : int,
    'lead' : str
}


# We do this before, so it doesn't have to happen on each query.
print("Loading data...",)
df = pd.read_csv("./dataframe.csv", dtype=data_type, converters=converter)
print("Done!")

def search(query):



    # put year in string format
    if query['years']['upper'] is None:
        query['years']['upper'] = '2020-12-31'
    else:
        query['years']['upper'] = str(query['years']['upper']) + "-12-31"

    if query['years']['lower'] is None:
        query['years']['lower'] = '1900-01-01'
    else:
        query['years']['lower'] = str(query['years']['lower']) + "-01-01"


    # change format to languages
    format_languages(query)

    movie_list = search_movie(df, query)

    return json.dumps(movie_list)


app = Flask(__name__)

@app.route('/',methods = ['POST'])
def determine_escalation():
    query = request.get_json()
    print(query)
    res = search(query)
    print(res)
    return json.dumps(res)

if __name__ == '__main__':
    app.run(debug=True, host='127.0.0.1', port=8042)
