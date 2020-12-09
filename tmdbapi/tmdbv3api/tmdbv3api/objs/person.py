from tmdbv3api.tmdb import TMDb
from tmdbv3api.as_obj import AsObj, AsObj_id

try:
    from urllib import quote
except ImportError:
    from urllib.parse import quote


class Person(TMDb):
    _urls = {
        "details": "/person/%s",
        "search_people": "/search/person",
        "popular": "/person/popular",
        "latest": "/person/latest",
        "images": "/person/%s/images",
    }

    def details(self, person_id, append_to_response="videos,images"):
        """
        Get the primary person details by id.
        :param append_to_response: str
        :param person_id: int
        :return:
        """
        return AsObj(
            **self._call(
                self._urls["details"] % str(person_id),
                "append_to_response=" + append_to_response,
            )
        )

    def images(self, person_id):
        """
        Get the images for a person.
        :param person_id: int
        :return:
        """
        return AsObj(**self._call(self._urls["images"] % str(person_id), ""))

    def latest(self):
        """
        Get the most newly created person. This is a live response and will continuously change.
        :return:
        """
        return AsObj(**self._call(self._urls["latest"], ""))

    def search(self, term, page=1):
        """
        Search for people.
        :param term: string
        :param page: int
        :return:
        """
        return self._get_obj(
            self._call(
                self._urls["search_people"],
                "query=" + quote(term) + "&page=" + str(page),
            )
        )

    def popular(self, page=1):
        """
        Get the list of popular people on TMDb. This list updates daily.
        :return:
        """
        return self._get_obj(self._call(self._urls["popular"], "page=" + str(page)))
    
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
        Search for people.
        :param term: string
        :param page: int
        :return: string: id
        """
        return self._get_obj_id(self._call(
                self._urls["search_people"],
                "query=" + quote(term) + "&page=" + str(page),))
        
