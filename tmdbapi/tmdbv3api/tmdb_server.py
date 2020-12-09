# https://github.com/xyp8023/tmdbv3api
import json
import csv
from tmdbv3api import TMDb, Movie, Discover, Genre, Person, Company

class tmdb_base():
    def __init__(self):
        api_key = "e9ee4dd66aa205b259f29ccb98e9a989"
        self.tmdb = TMDb()
        self.tmdb.api_key = api_key
        self.tmdb.language = 'en'
        self.tmdb.debug = True
        
        self.movie = Movie(self.tmdb)
        self.discover = Discover(self.tmdb)
        
        # build the genre database
        self.genre = Genre(self.tmdb)
        genres = self.genre.movie_list() # list
        self.genres_dict = {} # a dict with keys: genre names and values: id
        for g in genres:
            self.genres_dict[g.name] = g.id

        # build the language database (ISO 639-1)
        self.language_dict = {}
        with open('./csv/language-codes_csv.csv', newline='') as csvfile:
            reader = csv.reader(csvfile, delimiter=',')
            for row in reader:
                self.language_dict[row[1]]=row[0]

        self.person = Person(self.tmdb)
        self.company = Company(self.tmdb)

        # initialize the searching attributes
        self.search_genre = None 
        self.search_without_genre = None 
        self.search_cast = None 
        self.search_crew = None 
        self.search_people = None
        self.search_company = None
        self.search_year = None
        self.search_upper_year = None
        self.search_lower_year = None
        self.search_rating = None
        self.search_language = None
    
    def set_attributes(self, json_):
        """
        given a json from furhat, set the searching attributes
        """
        # conv = json.loads(json_)
        conv = json_ 
        actor = conv["actors"]["selected"] # list.
        genre = conv["genres"]["selected"] # list. 
        without_genre = conv["genres"]["deselected"] # list.
        company = conv["company"]["selected"]# list.
        language = conv["language"]["selected"] # list. 
        director = conv["director"]["selected"] # list.
        upper_year = conv["years"]["upper"] # None or int. 
        lower_year = conv["years"]["lower"] # None or int. 
        rating = conv["rating"] # None or int.

        self.search_upper_year = upper_year
        self.search_lower_year = lower_year
        self.rating = rating

        if len(actor)>0:
            self.search_cast = actor

        if len(director)>0:
            self.search_crew = director

        if len(genre)>0:
            self.search_genre = genre

        if len(without_genre)>0:
            self.search_without_genre = without_genre

        if len(company)>0:
            self.search_company = company

        if len(language)>0:
            self.search_language = language

    def set_language(self, language):
        """
        set the query language, better not change it
        language: sting: English, Spanish, etc
        """
        
        self.tmdb.language = self.language_dict[str(language).capitalize()]

    
    def name_to_id(self, names):
        """
        names: List of strings
        return: id: string
        """
        
        ids = []
        for name in names:
            id_ = self.person.search_id(name)
            if len(id_)>0:
                ids.append(str(id_[0]))
        return ",".join(ids)
    
    def genre_to_id(self, genres):
        """
        genres: List of strings
        return: id: string
        """
        ids = []
        for genre in genres:
            id_ = self.genres_dict[str(genre).capitalize()]
            ids.append(str(id_))
        return ",".join(ids)
    
    def company_to_id(self, companies):
        """
        companies: List of strings
        return: id: string
        """
        ids = []
        for company in companies:
            id_ = self.company.search_id(company)
            if len(id_)>0:
                id_sorted = sorted([item.id for item in id_])
                ids.append(str(id_sorted[0]))
        return ",".join(ids)
    
    def language_to_iso_639(self, languages):
        """
        languages: List of strings
        return: languages in ISO 639-1 format: string
        """
        ids = []
        for language in languages:
            id_ = self.language_dict[str(language).capitalize()]
            ids.append(id_)
        return ",".join(ids)

    def search_movies(self, top=10):
        """
        with_genres: string: Comma separated value of genre ids that you want to include in the results.
        without_genres: string: Comma separated value of genre ids that you want to exclude from the results.
        with_cast: string: A comma separated list of person ID's. Only include movies that have one of the ID's added as an actor.
        with_crew: string: A comma separated list of person ID's. Only include movies that have one of the ID's added as a crew member.
        with_people: string: A comma separated list of person ID's. Only include movies that have one of the ID's added as a either a actor or a crew member.
        with_companies: string: A comma separated list of production company ID's. Only include movies that have one of the ID's added as a production company.
        year: integer: A filter to limit the results to a specific year (looking at all release dates).
        release_date.gte: string (year-month-day): Filter and only include movies that have a release date (looking at all release dates) that is greater or equal to the specified value.
        release_date.lte: string (year-month-day): Filter and only include movies that have a release date (looking at all release dates) that is less than or equal to the specified value.
        vote_average.gte: number: Filter and only include movies that have a rating that is greater or equal to the specified value.
        with_original_language: string: Specify an ISO 639-1 string to filter results by their original language value.
        """
        request_dic = {}
        request_dic['sort_by'] = 'vote_average.desc'
        request_dic['vote_count.gte'] = 10
        
        if self.search_genre is not None:
            request_dic['with_genres'] = self.genre_to_id(self.search_genre)
        if self.search_without_genre is not None:
            request_dic['without_genres'] = self.genre_to_id(self.search_without_genre)
        if self.search_year is not None:
            request_dic['year']=self.search_year
        else:
            if self.search_upper_year is not None:
                request_dic['release_date.lte'] = str(self.search_upper_year)+"-12-31"
            if self.search_lower_year is not None:
                request_dic['release_date.gte'] = str(self.search_lower_year)+"-01-01"

        if self.search_rating is not None:
            request_dic['vote_average.gte'] = self.search_rating
        if self.search_company is not None:
            request_dic['with_companies'] =  self.company_to_id(self.search_company)


        if self.search_cast is not None:
            request_dic['with_cast'] = self.name_to_id(self.search_cast)
        elif self.search_crew is not None:
            request_dic['with_crew'] = self.name_to_id(self.search_crew)
        elif self.search_people is not None:
            request_dic['with_people'] = self.name_to_id(self.search_people)
        
        if self.search_language is not None:
            request_dic['with_original_language'] = self.language_to_iso_639(self.search_language)

        show = self.discover.discover_movies(request_dic)

        # return the top 10 list by default
        return [str(item) for item in show[:top]]

    


if __name__ == "__main__":
    tmdb_base = tmdb_base()
    conv_json = '{"actors": {"selected": ["Johnny Depp"],"deselected": []},"genres": {"selected": ["action"],"deselected": ["romance"]},"company": {"selected": [],"deselected": []},"language": {"selected": [],"deselected": []},"director": {"selected": [],"deselected": []},"years": {"lower": null,"upper": null},"rating": null}'    
    
    # return [], because too many restrictions!
    # conv_json = '{"actors": {"selected": ["Johnny Depp", "Orlando Bloom", "Bruce Willis"],"deselected": ["Will Smith"]}, "genres": {"selected": ["Music", "Romance"],"deselected": ["Thriller"]},"company": {"selected": ["Disney"],"deselected": []}, "language": {"selected": ["swedish", "english", "french"],"deselected": []}, "director": {"selected": [],"deselected": []}, "years": {"lower": 1990,"upper": 1995}, "rating": 5}'
    
    tmdb_base.set_attributes(conv_json)
    show = tmdb_base.search_movies()
    print(show)
    nl = '\n'.join(show)
    print(nl)
