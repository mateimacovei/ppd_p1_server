import logging
import random
import time

import requests
from promise import *


def myPrint(response, places):
    print(places)
    print(response.json())


if __name__ == '__main__':
    for i in range(10):
        places = [random.randint(1, 100) for _ in range(5)]
        Promise.resolve(
            requests.post(
                'http://localhost:8080/reservation',
                json={
                    "showId": 1,
                    # "places": [1, 2, 3]
                    "places": places
                }
            )
        ).then(
            lambda response: myPrint(response, places)
        ).catch(
            lambda error: logging.error(error)
        )
        time.sleep(5)
