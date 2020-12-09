from tmdbv3api.tmdb import TMDb
from tmdbv3api.as_obj import AsObj, AsObj_id
try:
    from urllib import quote
except ImportError:
    from urllib.parse import quote


class Company(TMDb):
    _urls = {"details": "/company/%s", "movies": "/company/%s/movies", "search_company": "/search/company",}

    def details(self, company_id):
        """
        Get a companies details by id.
        :param company_id: int
        :return:
        """
        return AsObj(**self._call(self._urls["details"] % str(company_id), ""))

    def movies(self, company_id):
        """
        Get the movies of a company by id.
        :param company_id: int
        :return:
        """
        return self._get_obj(self._call(self._urls["movies"] % str(company_id), ""))
    
    @staticmethod
    def _get_obj_id(result, key="results"):
        if "success" in result and result["success"] is False:
            raise Exception(result["status_message"])
        arr = []
        if key is not None:
            [arr.append(AsObj_id(**res)) for res in result[key]]
        else:
            return result
        return arr

    def search_id(self, term, page=1):
        """
        Search for company.
        :param term: string
        :param page: int
        :return: id
        """
        return self._get_obj_id(self._call(
                self._urls["search_company"],
                "query=" + quote(term) + "&page=" + str(page),))